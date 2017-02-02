/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.VehicleTypeDto;




/**
 * 
 * @author muhammad
 */
public class VehicleTypeDao {

	private String message;
	public int getSeatByTrip(String tripid) {
		int seat = 0;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();

		try {
			pst = con
					.prepareStatement("select sit_cap from vehicle_type where id=(select vehicle_type from trip_details where id=?)");
			pst.setString(1, tripid);
			rs = pst.executeQuery();
			if (rs.next()) {
				seat = rs.getInt(1);
			}
		} catch (SQLException ex) {
			System.out.println("err" + ex);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return seat;
	}

	public int insertVehicleType(VehicleTypeDto vehicleTypeDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		int returnvalue = 0;
		PreparedStatement pst = null;
		ResultSet rs = null, autogenrs = null;
		int changedBy = 0, autoincNumber = 0;

		// System.out.println("IN Insert");
		Connection con = ob.connectDB();
		try {
			changedBy = Integer.parseInt(vehicleTypeDtoObj.getDoneBy());
			pst = con
					.prepareStatement("select * from vehicle_type where type=?");
			pst.setString(1, vehicleTypeDtoObj.getType());
			rs = pst.executeQuery();
			if (rs.next()) {
				message= "Vehicle Type already exists.";
			} else {
				pst = con
						.prepareStatement("insert into vehicle_type(type,seat,sit_cap) values (?,?,?)");
				pst.setString(1, vehicleTypeDtoObj.getType());
				pst.setInt(2, vehicleTypeDtoObj.getSeat());
				pst.setInt(3, vehicleTypeDtoObj.getSittingCopacity());
				 returnvalue = pst.executeUpdate();
				if (returnvalue == 1) {
					Statement st = con.createStatement();
					autogenrs = st
							.executeQuery("Select id from vehicle_type where type='"
									+ vehicleTypeDtoObj.getType() + "'");
					if(autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
					
					auditLogEntryforinsertvehicle(autoincNumber,
							AuditLogConstants.VEHICLE_MODULE, changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORK_FLOW_STATE_VEHICLE_ADDED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					
					}
					message="Vehicle type inserted succefully..";
				} else {
					message="Vehicle type insertion failed..";
				}
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
			message = "Error :"+ e;
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnvalue;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	private void auditLogEntryforinsertvehicle(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public ArrayList<VehicleTypeDto> getAllVehicleType() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
			rs = st.executeQuery("select * from vehicle_type order by sit_cap");
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {
			System.out.println("erorr" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}

	public ArrayList<VehicleTypeDto> getAllOtherVehicleType(String siteId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
		try {
			st = con.createStatement();
			VehicleTypeDto vehicleTypeDtoObj = null;

			System.out.println(" >>>>>>>>>>>>>>>>>>>>>>>>>>>>" + siteId);
			rs = st.executeQuery("select * from vehicle_type where id not in (select site_vehicle.vehicleTypeId from site_vehicle where site_vehicle.siteId="
					+ siteId + ")");

			while (rs.next()) {

				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {
			System.out.println(" Error in getAllOtherVehicle type");
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return vehicleTypeDtoList;

	}

	public ArrayList<VehicleTypeDto> getSiteVehicleType(String siteId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;

		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
			rs = st.executeQuery("select * from vehicle_type where id in (select site_vehicle.vehicleTypeId from site_vehicle where site_vehicle.siteId="
					+ siteId + ")");
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}

	public VehicleTypeDto getVehicleType(String vehicleTypeId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		VehicleTypeDto vehicleTypeDtoObj = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from vehicle_type where id="
					+ vehicleTypeId + "");
			if (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoObj;
	}

	public void modifyVehicleType(VehicleTypeDto vehicleTypeDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int changedBy = 0, autoincNumber;

		// System.out.println("IN Insert");
		Connection con = ob.connectDB();
		try {
			changedBy = Integer.parseInt(vehicleTypeDtoObj.getDoneBy());
			autoincNumber = vehicleTypeDtoObj.getId();
			pst = con
					.prepareStatement("select * from vehicle_type where type=? and id!=?");
			pst.setString(1, vehicleTypeDtoObj.getType());
			pst.setInt(2, vehicleTypeDtoObj.getId());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update vehicle_type set type=?,seat=?,sit_cap=? where id=?");
				pst.setString(1, vehicleTypeDtoObj.getType());
				pst.setInt(2, vehicleTypeDtoObj.getSeat());
				pst.setInt(3, vehicleTypeDtoObj.getSittingCopacity());
				pst.setInt(4, vehicleTypeDtoObj.getId());
				int returnvalue = pst.executeUpdate();
				if (returnvalue == 1) {
					auditLogEntryformodifyvehicle(autoincNumber,
							AuditLogConstants.VEHICLE_MODULE, changedBy,
							AuditLogConstants.WORK_FLOW_STATE_VEHICLE_ADDED,
							AuditLogConstants.WORK_FLOW_STATE_VEHICLE_MODIFIED,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	private void auditLogEntryformodifyvehicle(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public VehicleTypeDto getMaximumCapacityVehicleTypeBySite(int siteid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;

		VehicleTypeDto vehicleTypeDtoObj = null;
		try {
			st = con.createStatement();
	rs = st.executeQuery("select vt.* from vehicle_type vt,site_vehicle sv where sv.siteid="
				+ siteid + " and sv.vehicleTypeId=vt.id order by sit_cap desc limit 1");			
			if (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
			}
		} catch (Exception e)

		{
			System.out.println("error in vehicletype" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoObj;
	}

	public ArrayList<VehicleTypeDto> getAllVehicleTypeBySite(int siteid) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
			rs = st.executeQuery("select vt.* from vehicle_type vt,site_vehicle sv where sv.siteid="
					+ siteid + " and sv.vehicleTypeId=vt.id order by sit_cap");
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}
	public void getAllVehicleTypeByToAppend(ArrayList<VehicleTypeDto> typeDtos,Connection con) {		
		
		Statement st = null;
		ResultSet rs = null;
		//VehicleTypeDto vehicleTypeDtoObj = null;
		//ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery("select vt.* from vehicle_type vt order by sit_cap");
			while (rs.next()) {								
				for(VehicleTypeDto dto: typeDtos)
				{
				if(rs.getInt("id")==dto.getId())
				{					
					dto.setType(rs.getString("type"));
					dto.setSeat(rs.getInt("seat"));
					dto.setSittingCopacity(rs.getInt("sit_cap"));
					break;
				}
				}
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
		}
	}

	public String getSuitedTypeBySeat(int size) {
		// TODO Auto-generated method stub
		return null;
	}
	

	public int addRateForVehicleType(VehicleTypeDto vehicleTypeDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pstUpdate = null;

		ResultSet rs = null;
		int returnInt = 0;
		int changedBy = 0, autoincNumber;

		// System.out.println("IN Insert");
		Connection con = ob.connectDB();
		try {
			con.setAutoCommit(false);
			con.rollback();
			changedBy = Integer.parseInt(vehicleTypeDtoObj.getDoneBy());
			autoincNumber = vehicleTypeDtoObj.getId();
			pst = con
					.prepareStatement(
							"insert into site_vehicle_rate_calander ( site ,vehicle_type ,ratePerTrip  ,fromDate  ,status) "
									+ " values (?,?,?,?,?) ",
							Statement.RETURN_GENERATED_KEYS);

			pst.setString(1, vehicleTypeDtoObj.getSite());
			pst.setString(2, vehicleTypeDtoObj.getType());
			pst.setDouble(3, vehicleTypeDtoObj.getRatePerTrip());
			pst.setString(4, OtherFunctions
					.changeDateFromatToIso(vehicleTypeDtoObj.getFromDate()));
			pst.setString(5, "new");

			pstUpdate = con

					.prepareStatement("update site_vehicle_rate_calander set toDate=?, status=? where  id in ( select max(id) from site_vehicle_rate_calander  where site=? and vehicle_type=? )");

			pstUpdate.setString(1, OtherFunctions
					.changeDateFromatToIso(vehicleTypeDtoObj.getFromDate()));
			pstUpdate.setString(2, "old");
			pstUpdate.setString(3, vehicleTypeDtoObj.getSite());
			pstUpdate.setString(4, vehicleTypeDtoObj.getType());

			pstUpdate.executeUpdate();

			returnInt = pst.executeUpdate();
			rs = pst.getGeneratedKeys();
			if (rs.next()) {
				autoincNumber = rs.getInt(1);
				auditLogEntryformodifyvehicle(autoincNumber,
						AuditLogConstants.VEHICLE_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORFLOW_STATE_CONTRACT_UPDATED,
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION);

			}
			if (returnInt > 0) {
				con.commit();
				con.setAutoCommit(true);
			} else {
				con.rollback();
				con.setAutoCommit(true);
			}
		} catch (Exception e) {
			System.out.println("Error in add Rate For vehicle :" + e);
			try {
				con.rollback();
			} catch (SQLException e1) {
				;
			}

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}

	public ArrayList<VehicleTypeDto> getVehicleTypeRateHistory() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;

		ArrayList<VehicleTypeDto> dtoList = new ArrayList<>();

		// System.out.println("IN Insert");
		Connection con = ob.connectDB();
		try {

			pst = con
					.prepareStatement(" SELECT  svrc.id  ,svrc.site, site.site_name ,vt.type vehicle_typeName ,svrc.ratePerTrip ,svrc.fromDate ,svrc.toDate ,svrc.status  FROM  site_vehicle_rate_calander svrc, vehicle_type vt, site where svrc.vehicle_type=vt.id and site.id=svrc.site order by  svrc.fromDate, svrc.toDate asc ");

			rs = pst.executeQuery();

			while (rs.next()) {
				VehicleTypeDto dto = new VehicleTypeDto();

				dto.setId(rs.getInt("id"));
				dto.setSite(rs.getString("site"));
				dto.setSiteName(rs.getString("site_name"));
				dto.setType(rs.getString("vehicle_typeName"));
				dto.setRatePerTrip(rs.getDouble("ratePerTrip"));
				dto.setFromDate(rs.getString("fromDate"));
				dto.setToDate(rs.getString("toDate"));
				dto.setStatus(rs.getString("status"));
				dto.setToDate(dto.getToDate() == null
						|| dto.getToDate().equalsIgnoreCase("null") ? "" : dto
						.getToDate());

				dtoList.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return dtoList;
	}

	public boolean checkTripRateDateOverlapping(VehicleTypeDto dto) {		
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		boolean flag = false;
		Connection con = ob.connectDB();
		try {

			pst = con
					.prepareStatement(" select * from site_vehicle_rate_calander where toDate is not null and '"
							+ OtherFunctions.changeDateFromatToIso(dto
									.getFromDate())
							+ "' >=fromDate and  '"
							+ OtherFunctions.changeDateFromatToIso(dto
									.getFromDate())
							+ "'<toDate and site="
							+ dto.getSite()
							+ " and vehicle_type="
							+ dto.getType() + " ");

			rs = pst.executeQuery();

			if (rs.next()) {
				flag = true;
			}
		} catch (Exception e) {
			System.out.println("Error" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return flag;
	}
	public String getMinimumCapacityVehicle(String siteId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;

		String vehicleType = "";
		try {
			st = con.createStatement();
			rs = st.executeQuery("select vt.* from vehicle_type vt,site_vehicle sv where sv.siteid="
					+ siteId + " and sv.vehicleTypeId=vt.id order by sit_cap LIMIT 1");
			if (rs.next()) {
				vehicleType = rs.getString("id");
			}
		} catch (Exception e)

		{
			System.out.println("error in vehicletype" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleType;
	}
	
	public ArrayList<VehicleTypeDto> getAllVehicleTypeBySiteandType(int siteid,String type) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
			rs = st.executeQuery("select vt.* from vehicle_type vt,site_vehicle sv where sv.siteid="
					+ siteid + " and sv.vehicleTypeId=vt.id order by sit_cap");
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}
	
	public ArrayList<VehicleTypeDto> getAllVehicleType(String siteId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;

		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
			String sitequery="";
			if(!siteId.equalsIgnoreCase("All"))
				sitequery="where id in (select site_vehicle.vehicleTypeId from site_vehicle where site_vehicle.siteId="
			+ siteId + ")";
			String query="select * from vehicle_type ";
			query=query+sitequery;
			rs = st.executeQuery(query);
			System.out.println("query"+query);
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setId(rs.getInt("id"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setSeat(rs.getInt("seat"));
				vehicleTypeDtoObj.setSittingCopacity(rs.getInt("sit_cap"));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}
	
	public ArrayList<VehicleTypeDto> getBySiteAndTypeVehicle(String siteId,String type) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		
		VehicleTypeDto vehicleTypeDtoObj = null;
		ArrayList<VehicleTypeDto> vehicleTypeDtoList = null;
		try {
			st = con.createStatement();
			vehicleTypeDtoList = new ArrayList<VehicleTypeDto>();
		String query="select sv.siteid,vt.type,v.id,v.regNo from vehicle_type vt join vehicles v on vt.id=v.vehicletype join site_vehicle sv on sv.vehicleTypeId=v.vehicletype where siteid='"+siteId+"' and vt.id='"+type+"'";

			rs = st.executeQuery(query);
			
			System.out.println("Emergency query is executed"+query);

				
			while (rs.next()) {
				vehicleTypeDtoObj = new VehicleTypeDto();
				vehicleTypeDtoObj.setSiteId(rs.getString("siteid"));
				vehicleTypeDtoObj.setType(rs.getString("type"));
				vehicleTypeDtoObj.setId(Integer.parseInt(rs.getString("id")));
				vehicleTypeDtoObj.setRegNo(rs.getString("regNo"));
				//vehicleTypeDtoObj.setId(Integer.parseInt(rs.getString("id")));
				vehicleTypeDtoList.add(vehicleTypeDtoObj);
			}
		} catch (Exception e) {

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return vehicleTypeDtoList;
	}


}
