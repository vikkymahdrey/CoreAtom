package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.usermanagement.dto.UserManagementDto;

public class AdhocDao {
	public int insertSetup(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		PreparedStatement pst = null;
		try {
			if (dto.getMaxPendingRequest() == null
					|| dto.getMaxPendingRequest().equals("")
					|| dto.getMaxPendingRequest().equals("null"))
				dto.setMaxPendingRequest("0");
			if (dto.getMaxRequest() == null || dto.getMaxRequest().equals("")
					|| dto.getMaxRequest().equals("null"))
				dto.setMaxRequest("0");
			pst = con.prepareStatement("delete from adhoctypes where type='"
					+ dto.getAdhocType() + "' and site='" + dto.getSiteId()
					+ "'and pickDrop = '"+dto.getPickupDrop()+"' and projectUnit='" + dto.getProjectUnit() + "' ");
			pst.executeUpdate();
			if (dto.getAdhocType().equalsIgnoreCase(
					SettingsConstant.SHIFT_EXTENSTION)) {
				pst = con
						.prepareStatement(
								"insert into adhoctypes(site,projectUnit,type,approval,requestCutoff,cancelCutoff,scheduleCancelMode,existingCancel,maxrequestPerDay,maxrequestWithoutApproval,pickDrop) values (?,?,?,?,?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getSiteId());
				pst.setString(2, dto.getProjectUnit());
				pst.setString(3, dto.getAdhocType());
				pst.setString(4, dto.getApproval());
				pst.setString(5, dto.getRequestCutoff());
				pst.setString(6, dto.getCancelCutoff());
				pst.setString(7, dto.getCancelMode());
				pst.setString(8, dto.getExistingCancelTime());
				pst.setString(9, dto.getMaxRequest());
				pst.setString(10, dto.getMaxPendingRequest());
				pst.setString(11, dto.getPickupDrop());
				retVal = pst.executeUpdate();
				rs = pst.getGeneratedKeys();

			} else {
				pst = con
						.prepareStatement(
								"insert into adhoctypes(site,projectUnit,type,approval,requestCutoff,cancelCutoff,maxrequestWithoutApproval) values (?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getSiteId());
				pst.setString(2, dto.getProjectUnit());
				pst.setString(3, dto.getAdhocType());
				pst.setString(4, dto.getApproval());
				pst.setString(5, dto.getRequestCutoff());
				pst.setString(6, dto.getCancelCutoff());
				pst.setString(7, dto.getMaxPendingRequest());
				retVal = pst.executeUpdate();
				rs = pst.getGeneratedKeys();
			}

			if (rs.next()) {
				dto.setId(rs.getLong(1));
				insertRequester(dto, con);
				if (dto.getApproval().equalsIgnoreCase("yes")) {
					insertApprovar(dto, con);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR In Inserting type" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public int insertRequester(AdhocDto dto, Connection con) {
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("insert into adhocRequester(adhoctypeId,requesterRoleId,status) values (?,?,?) ");
			for (String requester : dto.getRequesters()) {
				pst.setLong(1, dto.getId());
				pst.setString(2, requester);
				pst.setString(3, SettingsConstant.ACTIVE_STATUS);
				retVal += pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("ERROR in inserting requester " + e);
		} finally {
			DbConnect.closeStatement(pst);
			// DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int insertApprovar(AdhocDto dto, Connection con) {
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("insert into adhocApprovar(adhoctypeId,approvarRoleId,status) values (?,?,?) ");
			for (String approver : dto.getApprovers()) {
				pst.setLong(1, dto.getId());
				pst.setString(2, approver);
				pst.setString(3, SettingsConstant.ACTIVE_STATUS);
				retVal += pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("ERROR in inserting approver" + e);
		} finally {
			DbConnect.closeStatement(pst);
			// DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public AdhocDto getAdhoctypeDetails(String adhocType) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		AdhocDto dto = null;
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement("select * from adhoctypes where type='"
					+ adhocType + "'");
			rs = pst.executeQuery();
			if (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("type"));
				dto.setSiteId(rs.getString("site"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setRequester(rs.getString("requester"));
				dto.setApproval(rs.getString("approval"));
				dto.setRequestCutoff(rs.getString("requestCutoff"));
				dto.setCancelCutoff(rs.getString("cancelCutoff"));
				dto.setCancelMode(rs.getString("scheduleCancelMode"));
				dto.setExistingCancelTime(rs.getString("existingCancel"));
				dto.setMaxRequest(rs.getString("maxRequestPerDay"));
				dto.setMaxPendingRequest(rs
						.getString("maxrequestWithoutApproval"));

			}
			if (dto != null) {
				getRequesterDetails(dto);
				if (dto.getApproval().equalsIgnoreCase("yes")) {
					getApprovalDetails(dto);
				}
			}
		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dto;
		// TODO Auto-generated method stub

	}

	public ArrayList<AdhocDto> getAdhoctypeDetails(AdhocDto dto, String adhocType,
			String siteId, String projectUnit) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		//AdhocDto dto = null;
		ArrayList<AdhocDto> adhcocdetailslist = new ArrayList<AdhocDto>();
		PreparedStatement pst = null;
		try {

			pst = con.prepareStatement("select * from adhoctypes where type='"
					+ adhocType + "' and site=" + siteId
					+ " and (projectUnit='all' or projectUnit='" + projectUnit
					+ "')");
			rs = pst.executeQuery();
			while (rs.next()) {
				//if (dto2 == null) {
					 dto = new AdhocDto();
				//}
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("type"));
				dto.setSiteId(rs.getString("site"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				dto.setRequester(rs.getString("requester"));
				dto.setApproval(rs.getString("approval"));
				dto.setRequestCutoff(rs.getString("requestCutoff"));
				dto.setCancelCutoff(rs.getString("cancelCutoff"));
				dto.setCancelMode(rs.getString("scheduleCancelMode"));
				dto.setExistingCancelTime(rs.getString("existingCancel"));
				dto.setMaxRequest(rs.getString("maxRequestPerDay"));
				dto.setMaxPendingRequest(rs
						.getString("maxrequestWithoutApproval"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				adhcocdetailslist.add(dto);
				System.out.println("here in dao"+adhcocdetailslist.size());
							

			}
			
			for(AdhocDto d : adhcocdetailslist)
			{
			if (d != null) {
				System.out.println("1");
				getRequesterDetails(d);
				// if (dto.getApproval().equalsIgnoreCase("yes")) {
				getApprovalDetails(d);
				System.out.println("2");
				/*System.out.println("here " + dto2.getPickupDrop() +" " + d.getPickupDrop() + " "+d.getAdhocType());
				if(dto2 !=null && dto2.getPickupDrop().equalsIgnoreCase(d.getPickupDrop())&&dto2.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION))
				{
					System.out.println("I am here");
					//dto2 d;
					
				}
				else
				{
					//dto2=d;
				}
				// }
*/			}
			}
		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types" + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return adhcocdetailslist;
		// TODO Auto-generated method stub

	}

	public void getApprovalDetails(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();

		PreparedStatement pst = null;
		ArrayList<UserManagementDto> roles = new ArrayList<UserManagementDto>();
		UserManagementDto rolesDto = null;

		try {
			pst = con
					.prepareStatement("select aa.approvarRoleId roleId,r.name roleName,'selected=\"selected\"' as selectStatus from adhocApprovar aa,roles r where aa.approvarRoleId=r.id and aa.adhoctypeId="
							+ dto.getId()
							+ " and aa.status='a' union select r.id roleId,r.name roleName,'' as selectStatus from roles r where r.id not in (select aa.approvarRoleId from adhocApprovar aa,roles r where aa.approvarRoleId=r.id and aa.adhoctypeId="
							+ dto.getId() + "  and aa.status='a')");
			System.out
					.println("select aa.approvarRoleId roleId,r.name roleName,'selected=\"selected\"' as selectStatus from adhocApprovar aa,roles r where aa.approvarRoleId=r.id and aa.adhoctypeId="
							+ dto.getId()
							+ " and aa.status='a' union select r.id roleId,r.name roleName,'' as selectStatus from roles r where r.id not in (select aa.approvarRoleId from adhocApprovar aa,roles r where aa.approvarRoleId=r.id and aa.adhoctypeId="
							+ dto.getId() + "  and aa.status='a')");
			rs = pst.executeQuery();
			while (rs.next()) {
				rolesDto = new UserManagementDto();
				rolesDto.setId(rs.getInt("roleId"));
				rolesDto.setName(rs.getString("roleName"));
				rolesDto.setSelectionStatus(rs.getString("selectStatus"));
				roles.add(rolesDto);
			}
			dto.setApproverRoles(roles);

		} catch (Exception e) {
			System.out.println("Error in selecting approver details" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}

	}

	public void getRequesterDetails(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();

		PreparedStatement pst = null;
		ArrayList<UserManagementDto> roles = new ArrayList<UserManagementDto>();
		UserManagementDto rolesDto = null;

		try {
			pst = con
					.prepareStatement("select ar.requesterRoleId roleId,r.name roleName,'selected=\"selected\"' as selectStatus from adhocRequester ar,roles r where ar.requesterRoleId=r.id and ar.adhoctypeId="
							+ dto.getId()
							+ " and ar.status='a' union select r.id roleId,r.name roleName,'' as selectStatus from roles r where r.id not in (select ar.requesterRoleId from adhocrequester ar,roles r where ar.requesterRoleId=r.id and ar.adhoctypeId="
							+ dto.getId() + " and ar.status='a')");
			rs = pst.executeQuery();
			while (rs.next()) {
				rolesDto = new UserManagementDto();
				rolesDto.setId(rs.getInt("roleId"));
				rolesDto.setName(rs.getString("roleName"));
				rolesDto.setSelectionStatus(rs.getString("selectStatus"));
				roles.add(rolesDto);
			}
			dto.setRequesterRoles(roles);

		} catch (Exception e) {
			System.out.println("Error in selecting requester details " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}

	}

	public int updateSetup(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();

		int retVal = 0;
		PreparedStatement pst = null;
		String subQuery = "";
		String query = "";
		if (dto.getMaxPendingRequest() == null
				|| dto.getMaxPendingRequest().equals("")
				|| dto.getMaxPendingRequest().equals("null"))
			dto.setMaxPendingRequest("0");
		if (dto.getAdhocType().equalsIgnoreCase(
				SettingsConstant.SHIFT_EXTENSTION)) {
			subQuery = " ,scheduleCancelMode=?,existingCancel=?,maxrequestPerDay=?,pickDrop = ? ";

		}
		query = " update adhoctypes set  requester =? ,approval =? ,requestCutoff=? ,cancelCutoff=? "
				+ subQuery + ",maxrequestWithOutApproval=?  where id=? ";
		
		System.out.println(query +  " here " + dto.getPickupDrop());
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, dto.getRequester());
			pst.setString(2, dto.getApproval());
			pst.setString(3, dto.getRequestCutoff());
			pst.setString(4, dto.getCancelCutoff());
			
			if (!subQuery.equals("")) {
				pst.setString(5, dto.getCancelMode());
				pst.setString(6, dto.getExistingCancelTime());
				pst.setString(7, dto.getMaxRequest());
				pst.setString(8, dto.getPickupDrop());
				pst.setString(9, dto.getMaxPendingRequest());
				
				pst.setLong(10, dto.getId());
			} else {
				pst.setString(5, dto.getMaxPendingRequest());
				pst.setLong(6, dto.getId());
			}
			retVal += pst.executeUpdate();
			pst = con.prepareStatement("update adhocApprovar set status='"
					+ SettingsConstant.CANCELLED_STATUS
					+ "' where adhoctypeId=" + dto.getId() + " ");			
			pst.executeUpdate();
			pst = con.prepareStatement("update adhocrequester set status='"
					+ SettingsConstant.CANCELLED_STATUS
					+ "' where adhoctypeId=" + dto.getId() + " ");		
			pst.executeUpdate();
			insertApprovar(dto, con);
			insertRequester(dto, con);

		} catch (Exception e) {
			System.out.println("Error in updating adhoc set up" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<AdhocDto> getAdhocTypes() {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		AdhocDto dto = null;
		PreparedStatement pst = null;
		ArrayList<AdhocDto> adhocList = new ArrayList<AdhocDto>();
		try {
			pst = con
					.prepareStatement("select at.id,at.type,at.requester,at.approval,at.requestCutoff,at.cancelCutoff,at.scheduleCancelMode,at.maxRequestPerDay,at.maxrequestWithoutApproval,ss.Val from adhoctypes at,settingsString ss where ss.keyVal=at.type");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("type"));
				dto.setAdhocTypeString(rs.getString("Val"));
				dto.setRequester(rs.getString("requester"));
				dto.setApproval(rs.getString("approval"));
				dto.setRequestCutoff(rs.getString("requestCutoff"));
				dto.setCancelCutoff(rs.getString("cancelCutoff"));
				dto.setCancelMode(rs.getString("scheduleCancelMode"));
				dto.setMaxRequest(rs.getString("maxRequestPerDay"));
				dto.setMaxPendingRequest(rs
						.getString("maxrequestWithoutApproval"));
				/*
				 * if (dto.getApproval().equalsIgnoreCase("yes")) {
				 * getApprovalDetails(dto); }
				 */
				adhocList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 0 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return adhocList;
		// TODO Auto-generated method stub

	}

	public ArrayList<AdhocDto> getAdhocTypes(String site) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		AdhocDto dto = null;
		PreparedStatement pst = null;
		ArrayList<AdhocDto> adhocList = new ArrayList<AdhocDto>();
		try {
			pst = con
					.prepareStatement("select at.id,at.type,at.requester,at.approval,at.requestCutoff,at.cancelCutoff,at.scheduleCancelMode,at.maxRequestPerDay,at.maxrequestWithoutApproval,ss.Val,at.existingCancel,at.pickdrop from adhoctypes at,settingsString ss where ss.keyVal=at.type and at.site="
							+ site);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("type"));
				dto.setAdhocTypeString(rs.getString("Val"));
				dto.setRequester(rs.getString("requester"));
				dto.setApproval(rs.getString("approval"));
				dto.setRequestCutoff(rs.getString("requestCutoff"));
				dto.setCancelCutoff(rs.getString("cancelCutoff"));
				dto.setCancelMode(rs.getString("scheduleCancelMode"));
				dto.setMaxRequest(rs.getString("maxRequestPerDay"));
				dto.setMaxPendingRequest(rs
						.getString("maxrequestWithoutApproval"));
				dto.setExistingCancelTime(rs.getString("existingCancel"));
				dto.setPickupDrop(rs.getString("pickdrop"));
				adhocList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 0 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return adhocList;
		// TODO Auto-generated method stub

	}

	public int bookAdhocTravel(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		int retVal = 0;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		String status = "Pending";
		String approvedBy = null;
		String scheduleId = null;
		System.out.println(dto.getBookedBy() + "booked by");
		if (dto.getApproval().equalsIgnoreCase("no")
				|| isApproveRight(dto.getBookedBy(), dto.getAdhocType(),
						dto.getBookedFor(), dto.getSiteId(),
						dto.getProjectUnit(),dto.getRequesterRoleId())) {
			status = "approved";
			approvedBy = dto.getBookedBy();

		}
		// System.out.println(dto.getApproval()+" "+status+" "+approvedBy);
		try {
			con = ob.connectDB();
			if (dto.getAdhocType().equalsIgnoreCase(
					SettingsConstant.SHIFT_EXTENSTION)) {

				ResultSet rs = null;
				String query = "select es.id as scheduleid,esa.id schedulelaterId,ifnull(esa.login_time,es.login_time) as login_time,ifnull(esa.logout_time,es.logout_time) as logout_time,es.subscription_id from employee_schedule es left outer join employee_schedule_alter esa on es.id=esa.scheduleId and esa.date='"
						+ dto.getTravelDate()
						+ "' where es.from_date<='"
						+ dto.getTravelDate()
						+ "' and es.to_date>='"
						+ dto.getTravelDate()
						+ "' and es.employee_id="
						+ dto.getBookedFor() + "";
				System.out.println(query);
				pst = con.prepareStatement(query);
				rs = pst.executeQuery();
				if (rs.next()) {
					scheduleId = rs.getString("scheduleid");
					
						String scheduleQuery = "";
						String inOut = "";
						String notInOut = "";
						if (dto.getPickupDrop().equalsIgnoreCase("IN")) {
							inOut = "login_time";
							notInOut = "logout_time";
						} else {
							inOut = "logout_time";
							notInOut = "login_time";
						}
						if (dto.getCancelMode().equalsIgnoreCase("automatic")&&OtherFunctions.checkTime(dto.getTravelDate(),rs.getString(inOut),dto.getExistingCancelTime())) {						
							{
						
						if (rs.getString(2) == null
								|| rs.getString(2).equalsIgnoreCase("null")
								|| rs.getString(2).equals("")) {
							scheduleQuery = "insert into employee_schedule_alter (scheduleId,date,"
									+ inOut
									+ ","
									+ notInOut
									+ ",status,statusDate,updatedBy) values (?,?,?,?,?,now(),?)";
							pst1 = con.prepareStatement(scheduleQuery);
							pst1.setString(1, scheduleId);
							pst1.setString(2, dto.getTravelDate());
							pst1.setString(3, "");
							pst1.setString(4, rs.getString(notInOut));
							pst1.setString(5, "a");
							pst1.setString(6, dto.getBookedBy());
							pst1.executeUpdate();						
						} else {
							scheduleQuery = " update employee_schedule_alter set  "+inOut+"='' where id="+ rs.getString("scheduleid");
							pst1 = con.prepareStatement(scheduleQuery);
							pst1.executeUpdate();
						}
					}
						}
					pst = con
							.prepareStatement(
									"INSERT INTO adhocBooking (empId,adhocType,siteId,scheduleId,travelDate,bookedDate,bookedBy,"
											+ "startTime,pickDrop,status,approvedBy) VALUES (?,?,?,?,?,now(),?,?,?,?,?)",
									Statement.RETURN_GENERATED_KEYS);
					pst.setString(1, dto.getBookedFor());
					pst.setString(2, dto.getAdhocType());
					pst.setString(3, dto.getSiteId());
					pst.setString(4, scheduleId);
					pst.setString(5, dto.getTravelDate());
					pst.setString(6, dto.getBookedBy());
					pst.setString(7, dto.getShiftTime());
					pst.setString(8, dto.getPickupDrop());
					pst.setString(9, status);
					pst.setString(10, approvedBy);

					retVal = pst.executeUpdate();
					rs = pst.getGeneratedKeys();
					if (rs.next()) {
						retVal = rs.getInt(1);
					}
				}
				else
				{
					DbConnect.closeStatement(pst);
				
					if(SettingsConstant.shiftExtensionWithoutSchedule!=null&&SettingsConstant.shiftExtensionWithoutSchedule.equalsIgnoreCase("yes"))
					{
												
					ResultSet rs1 = null;
				String query1 = "select p.id as projectid,es.subscriptionid from employee e,atsconnect p,employee_subscription es where e.id="+dto.getBookedFor()+" and   e.id=es.empid and e.project=p.project and es.subscriptionstatus in ('pending','subscribed') ";
				System.out.println(query1);
				pst=con.prepareStatement(query1);
				rs1=pst.executeQuery();
				if(rs1.next())
				{
					SchedulingDto scheduleDto=new SchedulingDto();
					
					scheduleDto.setSubscriptionId(rs1.getString("subscriptionid"));
					scheduleDto.setProject(rs1.getString("projectid"));
					scheduleDto.setSchedulingToDate(dto.getTravelDate());
					scheduleDto.setSchedulingFromDate(dto.getTravelDate());
					scheduleDto.setLoginTime("");
					scheduleDto.setLogoutTime("");					
					scheduleDto.setScheduledBy(dto.getBookedBy());																																							
					 scheduleId= Long.toString(new SchedulingDao().scheduleSelf(scheduleDto));									
					pst = con
							.prepareStatement(
									"INSERT INTO adhocBooking (empId,adhocType,siteId,scheduleId,travelDate,bookedDate,bookedBy,"
											+ "startTime,pickDrop,status,approvedBy) VALUES (?,?,?,?,?,now(),?,?,?,?,?)");
					pst.setString(1, dto.getBookedFor());
					pst.setString(2, dto.getAdhocType());
					pst.setString(3, dto.getSiteId());
					pst.setString(4, scheduleId);
					pst.setString(5, dto.getTravelDate());
					pst.setString(6, dto.getBookedBy());
					pst.setString(7, dto.getShiftTime());
					pst.setString(8, dto.getPickupDrop());
					pst.setString(9, status);
					pst.setString(10, approvedBy);

					retVal = pst.executeUpdate();
					
					}
							
					else
						{
						
						retVal=-100;
						}
					}		
					retVal=-100;
				}
			}
			else {
				pst = con
						.prepareStatement(
								"INSERT INTO adhocBooking (empId,adhocType,siteId,travelDate,bookedDate,bookedBy,"
										+ "startTime,endTime,orgination,destination,reason,comment,status,approvedBy) VALUES (?,?,?,?,now(),?,?,?,?,?,?,?,?,?)",
								Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getBookedFor());
				pst.setString(2, dto.getAdhocType());
				pst.setString(3, dto.getSiteId());
				pst.setString(4, dto.getTravelDate());
				pst.setString(5, dto.getBookedBy());
				pst.setString(6, dto.getStartTime());
				pst.setString(7, dto.getEndTime());
				pst.setString(8, dto.getOrgination());
				pst.setString(9, dto.getDestination());
				pst.setString(10, dto.getReason());
				pst.setString(11, dto.getComment());
				pst.setString(12, status);
				pst.setString(13, approvedBy);
				retVal = pst.executeUpdate();
				ResultSet rs = pst.getGeneratedKeys();
				if (rs.next()) {
					retVal = rs.getInt(1);
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR In Adhoc Booking " + e);

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;

	}

	public AdhocDto getBookingDetails(int bookingId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		AdhocDto dto = new AdhocDto();
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con
					.prepareStatement("SELECT  id,adhocType,travelDate,bookedDate,pickDrop,startTime,endTime,orgination ,destination,reason,comment,status,approvedBy FROM adhocBooking where id="
							+ bookingId);
			rs = pst.executeQuery();
			if (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 1 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dto;
		// TODO Auto-generated method stub
	}

	public ArrayList<AdhocDto> getAllBookingDetails(String empId) {
		DbConnect ob = DbConnect.getInstance();

		ResultSet rs = null;
		Connection con = null;
		AdhocDto dto = null;
		ArrayList<AdhocDto> dtoList = new ArrayList<AdhocDto>();
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con
					.prepareStatement("SELECT  id,adhocType,empId,travelDate,bookedDate,pickDrop,startTime,endTime,orgination ,destination,reason,comment,status,approvedBy FROM adhocBooking where empId="
							+ empId
							+ " and (status is null or status!='cancelled') and travelDate>=curdate() order by travelDate desc");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setBookedFor(rs.getString("empId"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 2 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dtoList;
		// TODO Auto-generated method stub
	}

	public int modifyAdhocTravel(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;

		int retVal = 0;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			if (dto.getAdhocType().equalsIgnoreCase(
					SettingsConstant.SHIFT_EXTENSTION)) {
				String inOut = "";
				if (dto.getPickupDrop().equalsIgnoreCase("IN")) {
					inOut = "login_time";
				} else {
					inOut = "logout_time";
				}
				/*String scheduleQuery = " update adhocbooking,employee_schedule,employee_schedule_alter set employee_schedule_alter."
						+ inOut
						+ "='"
						+ dto.getShiftTime()
						+ "' where  adhocbooking.id="
						+ dto.getBookingId()
						+ " and adhocbooking.scheduleId=employee_schedule.id  and employee_schedule.id=employee_schedule_alter.scheduleId and employee_schedule_alter.date=adhocbooking.travelDate";
				pst = con.prepareStatement(scheduleQuery);
				pst.executeUpdate();*/
				pst = con
						.prepareStatement("Update adhocBooking set pickDrop=?,"
								+ "startTime=? where id=?");
				pst.setString(1, dto.getPickupDrop());
				pst.setString(2, dto.getShiftTime());
				pst.setString(3, dto.getBookingId());
				retVal = pst.executeUpdate();

			} else {
				pst = con
						.prepareStatement("Update adhocBooking set "
								+ "startTime=?,endTime=?,orgination=?,destination=?,reason=?,comment=? where id=?");
				pst.setString(1, dto.getStartTime());
				pst.setString(2, dto.getEndTime());
				pst.setString(3, dto.getOrgination());
				pst.setString(4, dto.getDestination());
				pst.setString(5, dto.getReason());
				pst.setString(6, dto.getComment());
				pst.setString(7, dto.getBookingId());
				retVal = pst.executeUpdate();
			}
			con.commit();
		} catch (Exception e) {
			System.out.println("ERROR In Adhoc Booking " + e);

		} finally {
			try {
				con.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public ArrayList<AdhocDto> getBookingDetailsForManager(String managerId,
			String roleId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		AdhocDto dto = null;
		ArrayList<AdhocDto> list = new ArrayList<AdhocDto>();

		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			String query="select ab.*,e.site,e.projectUnit,e.displayname,e.PersonnelNo from adhocBooking ab,employee e join  employee_subscription es  on e.id=es.empid  and   (es.subscriptionstatus='subscribed' or es.subscriptionstatus='pending')   left join roledelegation rd on rd.delegatedEmployeeId= "+managerId+"   and rd.from_Date<=curdate() and rd.status='a'   where ab.empId=e.id and ab.travelDate > date_add(curdate(),interval -30 day)  and    (ab.empId in   (select id from employee where  (  es.spoc="+managerId+" or employee.id="+managerId+" or rd.employeeid=es.spoc or rd.employeeid=linemanager or  LineManager="+managerId+" or LineManager   in(select id from employee where linemanager="+managerId+")  )  )  or   ab.empid in (select sc.employee_id from  spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+managerId+")) and (ab.status is  null or ab.status!='cancelled') order by ab.bookedDate desc ";							
			System.out.println(query);
			pst = con
					.prepareStatement(query);
			// System.out.println("select ab.*,e.site,e.projectUnit,e.displayname,e.PersonnelNo from adhocBooking ab,employee e where ab.empId=e.id  and (ab.empId in (select id from employee where LineManager="+
			// managerId+
			// ") or ab.empid in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+
			// managerId+
			// ")) and (ab.status is null or ab.status!='cancelled') order by ab.bookedDate desc");

			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setBookingId(rs.getString("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dto.setEmployeeName(rs.getString("displayname"));
				dto.setEmployeeCode(rs.getString("PersonnelNo"));
				dto.setSiteId(rs.getString("site"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 3 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return list;
		// TODO Auto-generated method stub
	}

	public int approveAdhocBooking(String adhocId, String approverId,
			String update) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		int retVal = 0;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con.prepareStatement("Update adhocBooking set status='"
					+ update + "', approvedBy='" + approverId + "' where id=?");
			pst.setString(1, adhocId);
			retVal += pst.executeUpdate();

		} catch (Exception e) {
			System.out.println("ERROR In Adhoc Booking " + e);

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	public String isAdhocBookingright(String empId, String adhocType,
			String bookingFor, String site, String projectUnit,String roleId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;
		String retn = "";
		try {
			con = ob.connectDB();
			String query = "select * from adhocrequester ar,adhoctypes at " 
					+ " where  ar.adhocTypeId=at.id and at.type='"+adhocType+"' and at.site="+ site+ " and (at.projectUnit='all' or at.projectUnit='"+ projectUnit+ "') and ar.requesterRoleId="+roleId+" and ar.status='"+SettingsConstant.ACTIVE_STATUS+"'";

			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				//if (rs.getInt(1) > 0 || rs.getInt(2) > 0) {
					retn = "all";
				//} else if (rs.getInt(3) > 0) {
				//	retn = "self";
				//}
			}

		} catch (Exception e) {
			System.out.println("ERROR in checking right " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retn;

	}

	public AdhocDto getBookingDetails(String bookingId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		AdhocDto dto = null;

		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con.prepareStatement("select *,e.personnelno,e.displayname from adhocBooking ab,employee e where ab.empid=e.id and  ab.id="
					+ bookingId + "");
			// System.out.println("select * from adhocBooking where empId in (select id from employee where LineManager="+managerId+") or empid in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+managerId+") order by bookedDate desc");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dto.setBookedBy(rs.getString("bookedBy"));
				dto.setEmployeeId(rs.getString("empId"));
				dto.setEmployeeCode(rs.getString("personnelno"));
				dto.setEmployeeName(rs.getString("displayname"));
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 4 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dto;
		// TODO Auto-generated method stub
	}

	public int cancelAdhocBooking(String adhocId, String isShiftExt,
			String scheduleId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		int retVal = 0;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con
					.prepareStatement("Update adhocBooking set status='cancelled'  where id=?");
			pst.setString(1, adhocId);
			retVal = pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("ERROR In Adhoc Booking " + e);

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;

	}

	public boolean isApproveRight(String empId, String adhocType,
			String bookingFor, String site, String projectUnit,String requesterRoleId) {

		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			/*
			String query = "select count(*) as manager ,"
					+ " (select count(*)  "
					+ " from spoc_child sc, spoc_parent sp ,adhocapprovar aa1 "
					+ " where sp.id=sc.spoc_id and sp.employee_id="
					+ empId
					+ " and aa1.adhocTypeId=(select id from adhoctypes where type='"
					+ adhocType
					+ "' and site="
					+ site
					+ " and (projectUnit='all' or projectUnit='"
					+ projectUnit
					+ "')) and aa1.approvarRoleId="
					+ " (select id from  roles where usertype='spoc') and aa1.status='"
					+ SettingsConstant.ACTIVE_STATUS
					+ "') as spoc, "
					+ " (select count(*)  from employee e,adhocapprovar aa1 where e.id="
					+ empId
					+ " and aa1.approvarRoleId=(select id from adhoctypes where type='"
					+ adhocType
					+ "' and site="
					+ site
					+ " and (projectUnit='all' or projectUnit='"
					+ projectUnit
					+ "')) and aa1.approvarRoleId="
					+ "(select id from roles where usertype='emp') and aa1.status='"
					+ SettingsConstant.ACTIVE_STATUS
					+ "' )as employee"
					+ " from employee e,adhocapprovar aa where e.LineManager="
					+ empId
					+ " and aa.adhocTypeId=(select id from adhoctypes where type='"
					+ adhocType
					+ "' and  site="
					+ site
					+ " and (projectUnit='all' or projectUnit='"
					+ projectUnit
					+ "')) and aa.approvarRoleId="
					+ "(select id from roles where usertype='hrm') and aa.status='"
					+ SettingsConstant.ACTIVE_STATUS + "'";
*/
			String query="select * from adhocapprovar aa,adhoctypes at where  aa.adhocTypeId=at.id and at.type='"+adhocType+"' and  site="+ site+ " and (at.projectUnit='all' or at.projectUnit='"+ projectUnit+ "') and aa.approvarRoleId="+requesterRoleId+" and aa.status='"+SettingsConstant.ACTIVE_STATUS+"'";
			//System.out.println(query);

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {				
					return true;
				} else {
					return false;
				}			

		} catch (Exception e) {
			System.out.println("ERROR in checking right " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return false;

	}

	public void getValidateDetails(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;

		try {
			dto.setId(0);
			con = ob.connectDB();
			String query = "select count(id) as pendingCount,(select count(id) from adhocBooking where empId='"
					+ dto.getBookedFor()
					+ "' and travelDate='"
					+ dto.getTravelDate()
					+ "' and adhocType='"
					+ dto.getAdhocType()
					+ "' and status!='cancelled') as bookingCount from adhocBooking where empId='"
					+ dto.getBookedFor()
					+ "' and adhocType='"
					+ dto.getAdhocType()
					+ "' and (status is null or status ='pending')  ";
			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				dto.setBookedDayBookingCount(rs.getString("bookingCount"));
				dto.setPendingBookedCount(rs.getString("pendingCount"));
			}
		} catch (Exception e) {
			System.out.println("Error in Validate Booking" + e);
		} finally {

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	public ArrayList<AdhocDto> getBookingDetailsForRouting(String siteId,
			String date) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		ArrayList<AdhocDto> dtos = new ArrayList<AdhocDto>();
		AdhocDto dto = null;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con
					.prepareStatement("select * from adhocBooking where siteid='"
							+ siteId
							+ "' and travelDate='"
							+ date
							+ "' and  adhoctype!='"
							+ SettingsConstant.SHIFT_EXTENSTION + "'");
			System.out.println("select * from adhocBooking where siteid='"
					+ siteId + "' and travelDate='" + date
					+ "' and  adhoctype!='" + SettingsConstant.SHIFT_EXTENSTION
					+ "'");

			// System.out.println("select * from adhocBooking where empId in (select id from employee where LineManager="+managerId+") or empid in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+managerId+") order by bookedDate desc");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setId(rs.getLong("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setSiteId(rs.getString("siteid"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedFor(rs.getString("empId"));
				dto.setEmployeeId(rs.getString("empId"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dtos.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for Routing "
					+ e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dtos;
		// TODO Auto-generated method stub
	}

	public VendorDto getVendorAdhocRoutingTrip(String tripId) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;

		VendorDto dto = null;
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			pst = con
					.prepareStatement("select v.id, v.company, v.address, v.contact, v.contact1, v.email, v.status from adhoctripvendorassign av, vendormaster v, adhoctrips t where av.vendorId=v.id and t.id=av.tripId and t.id="
							+ tripId);

			// System.out.println("select * from adhocBooking where empId in (select id from employee where LineManager="+managerId+") or empid in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+managerId+") order by bookedDate desc");
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new VendorDto();
				dto.setId(rs.getString("id"));
				dto.setCompany(rs.getString("company"));
				dto.setAddress(rs.getString("address"));
				dto.setContactNumber(rs.getString("contact"));
				dto.setEmail(rs.getString("email"));

			}

		} catch (Exception e) {
			System.out.println("Error in getVendorAdhocRoutingTrip " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return dto;
		// TODO Auto-generated method stub
	}

	public int[] getAdhocNotificationCount(String employeeId, String roleId,String role) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;

		int returnCount[]=new int[2];
		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			String query="";
			if(role.equalsIgnoreCase("admin")||role.equalsIgnoreCase("tm")||role.equalsIgnoreCase("ta")||role.equalsIgnoreCase("tc")||role.equalsIgnoreCase("v"))
			{
				query="select count(*) from employee e,adhocbooking ab where (ab.status is null or ab.status='Pending' ) and e.id=ab.empId";
				System.out.println(query);
				pst = con.prepareStatement(query);
				rs = pst.executeQuery();
				if (rs.next()) {
					returnCount[0]=rs.getInt(1);
				}
			}
			
			query="select count(*) from employee e,adhocbooking ab,adhoctypes at,adhocapprovar aa where (ab.status is null or ab.status='Pending') and ( e.LineManager="+employeeId+" or e.LineManager in (select id from employee where linemanager="+employeeId+")) and e.id=ab.empId and e.site=ab.siteid and at.type=ab.adhocType and e.site=at.site  and aa.adhocTypeId=at.id  and aa.approvarRoleId="+roleId+" and aa.status='a'  ";		
			System.out.println(query);
			returnCount[1]=0;
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				returnCount[1]=rs.getInt(1);
			}			

		} catch (Exception e) {
			System.out.println("Error in Adhoc Notification " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		System.err.println(returnCount[0]+"  "+returnCount[1]);
		return returnCount;
	}

	public ArrayList<AdhocDto> getBookingDetailsForTransportManager() {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = null;
		AdhocDto dto = null;
		ArrayList<AdhocDto> list = new ArrayList<AdhocDto>();

		PreparedStatement pst = null;
		try {
			con = ob.connectDB();
			String query="select ab.*,e.site,e.projectUnit,e.displayname,e.PersonnelNo from adhocBooking ab,employee e where ab.empId=e.id  and "
 +" (ab.status is null or ab.status!='cancelled') and ab.travelDate > date_add(curdate(),interval -30 day) order by ab.bookedDate desc";							
			System.out.println(query);
			pst = con
					.prepareStatement(query);
			// System.out.println("select ab.*,e.site,e.projectUnit,e.displayname,e.PersonnelNo from adhocBooking ab,employee e where ab.empId=e.id  and (ab.empId in (select id from employee where LineManager="+
			// managerId+
			// ") or ab.empid in (select sc.employee_id from spoc_parent sp,spoc_child sc where sc.spoc_id=sp.id and sp.employee_id="+
			// managerId+
			// ")) and (ab.status is null or ab.status!='cancelled') order by ab.bookedDate desc");

			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setBookingId(rs.getString("id"));
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("bookedDate"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setShiftTime(rs.getString("startTime"));
				dto.setPickupDrop(rs.getString("pickDrop"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dto.setEmployeeName(rs.getString("displayname"));
				dto.setEmployeeCode(rs.getString("PersonnelNo"));
				dto.setSiteId(rs.getString("site"));
				dto.setProjectUnit(rs.getString("projectUnit"));
				list.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in selecting adhoc types for emp 3 " + e);
		}

		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return list;
		// TODO Auto-generated method stub
	}
	
	
	public int bookAdhocTravel2(AdhocDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		int retVal = 0;
		PreparedStatement pst = null,pst1=null;
		ResultSet rs = null;
		String status="Pending",approvedBy="";
		
		try{
				con = ob.connectDB();
				if(isApproveRight(dto.getBookedBy(), dto.getAdhocType(),dto.getBookedFor(), dto.getSiteId(),dto.getProjectUnit(),dto.getRequesterRoleId()))
				{
					System.out.println("here in approval");
					status="Approved";
					approvedBy=dto.getBookedBy();
				}
				String bookingquery = "INSERT INTO ADHOCBOOKING(adhoctype,siteid,traveldate,bookeddate,bookedby,starttime,orgination,destination,reason,comment,status,empId,approvedBy,endtime)  VALUES (?,?,STR_TO_DATE(?, '%d/%m/%Y'),now(),?,?,?,?,?,?,?,?,?,?)";
				String empquery="INSERT INTO ADHOC_PASSENGER(empId,displayName,email,phone,gender,type,adhocBookingId) VALUES(?,?,?,?,?,?,?)",book_id="",emp_id="0";
				pst = con.prepareStatement(bookingquery,Statement.RETURN_GENERATED_KEYS);
				pst.setString(1, dto.getAdhocType());
				pst.setString(2, dto.getSiteId());
				pst.setString(3, dto.getTravelDate());
				pst.setString(4, dto.getBookedBy());
				pst.setString(5, dto.getStartTime());
				pst.setString(6, dto.getOrgination());
				pst.setString(7, dto.getDestination());
				pst.setString(8, dto.getReason());
				pst.setString(9, dto.getComment());
				pst.setString(10,status);
				pst.setString(11,dto.getBookedBy());
				pst.setString(12,approvedBy);
				pst.setString(13, dto.getEndTime());
				retVal=pst.executeUpdate();
				rs=pst.getGeneratedKeys();
				if(rs.next())
				{
					book_id=rs.getString(1);
				}
				if(!book_id.equalsIgnoreCase("")||book_id!=null)
				{
					pst1=con.prepareStatement(empquery);
					for(EmployeeDto empdto:dto.getPassengerList())
					{
						if(!empdto.getEmployeeID().equalsIgnoreCase("") && empdto.getEmployeeID()!=null)
						{
							emp_id=empdto.getEmployeeID();
						}
					pst1.setString(1, emp_id);
					pst1.setString(2, empdto.getDisplayName());
					pst1.setString(3, empdto.getEmailAddress());
					pst1.setString(4, empdto.getContactNo());
					pst1.setString(5, empdto.getGender());
					pst1.setString(6, empdto.getState());
					pst1.setString(7, book_id );
					pst1.executeUpdate();
					}
				}
				}catch(Exception e)
				{
					e.printStackTrace();
				} finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
				}

        return retVal;
		}
	
	public ArrayList<AdhocDto> getAdhocTrips(String site)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<AdhocDto> list = new ArrayList<AdhocDto>();
		AdhocDto dto = null;
		String query="SELECT ab.adhoctype,ab.id,ab.bookedby,ab.starttime,ab.endtime,ab.orgination,ab.destination,ab.reason,ab.comment,ab.status,ab.approvedby,DATE_FORMAT(ab.bookeddate,'%d/%m/%Y') as BookedDate,DATE_FORMAT(ab.traveldate,'%d/%m/%Y') as travelDate,e.displayname AS BookedName,e1.displayName AS approvedByName  FROM adhocbooking ab,employee e,employee e1 WHERE ab.siteId="+site+" AND e.id=ab.bookedBy AND e1.id=ab.approvedBy AND adhocType!='"+SettingsConstant.SHIFT_EXTENSTION+"' AND ab.status='Approved' AND ab.id not in (SELECT bookingId FROM adhoc_trip_details WHERE id IN (SELECT tripid FROM adhoctripvendorassign WHERE STATUS='a'))";
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				dto = new AdhocDto();
				dto.setAdhocType(rs.getString("adhocType"));
				dto.setBookingId(rs.getString("id"));
				dto.setTravelDate(rs.getString("travelDate"));
				dto.setBookedDate(rs.getString("BookedDate"));
				dto.setBookedBy(rs.getString("bookedby"));
				dto.setStartTime(rs.getString("startTime"));
				dto.setEndTime(rs.getString("endTime"));
				dto.setOrgination(rs.getString("orgination"));
				dto.setDestination(rs.getString("destination"));
				dto.setReason(rs.getString("reason"));
				dto.setComment(rs.getString("comment"));
				dto.setStatus(rs.getString("status"));
				dto.setApprovedBy(rs.getString("approvedBy"));
				dto.setApprovedByName(rs.getString("approvedByName"));
				dto.setBookedByname(rs.getString("BookedName"));	
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}
	
	public ArrayList<EmployeeDto> getAdhocPassengers(String bookingid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
		EmployeeDto dto = null;
		String query="SELECT * FROM ADHOC_PASSENGER WHERE ADHOCBOOKINGID="+bookingid;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				dto= new EmployeeDto();
				dto.setDisplayName(rs.getString("displayName"));
				dto.setEmailAddress(rs.getString("email"));
				dto.setContactNo(rs.getString("phone"));
				dto.setGender(rs.getString("gender"));
				list.add(dto);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return list;
	}
	
	public int vendorAssign(String id[], String vendorid) {
		int returnInt = 0,returntsheet=0;
		String tripsheetquery="INSERT INTO adhoc_trip_details(trip_code,siteId,trip_date,STATUS,trip_time,bookingId) SELECT 'ADHOC',siteId,travelDate,'Created' AS STATUS,starttime,Id FROM adhocbooking WHERE id=?";
		String query = "INSERT INTO ADHOCTRIPVENDORASSIGN(TRIPID,VENDORID,STATUS,UPDATED) VALUES(?,?,'a',NOW())";
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null,pst1=null;
		ResultSet rs=null;
		String trip_id="";
		try {
			pst = con.prepareStatement(tripsheetquery,Statement.RETURN_GENERATED_KEYS);
			for (String bookingid : id) {
				pst.setString(1, bookingid);
				returntsheet=pst.executeUpdate();
				if(returntsheet>0)
				{
					rs=pst.getGeneratedKeys();
					if(rs.next())
					{
						trip_id=rs.getString(1);
					}
					pst1=con.prepareStatement(query);
					if(!trip_id.equalsIgnoreCase(""))
					{
					pst1.setString(1,trip_id );
					pst1.setString(2,vendorid);
					returnInt=pst1.executeUpdate();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}
		finally
		{
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}

		return returnInt;
	}
		
	
}
