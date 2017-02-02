/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.SiteDto;


/**
 * 
 * @author 123
 */
public class SiteDao {

	public List<SiteDto> getSites() {
		List<SiteDto> dtoList = new ArrayList<SiteDto>();
		DbConnect ob = DbConnect.getInstance();

		ResultSet rs = null;
		Statement st = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";

		String query = "select id, site_name,combainedRouteOnspecifiedTime,branch from site";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				SiteDto dto = new SiteDto();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("site_name"));
				dto.setWeekoffcombain(rs
						.getString("combainedRouteOnspecifiedTime"));
				dto.setBranch(rs.getString("branch"));
				dtoList.add(dto);

			}
			 
		} catch (Exception e) {
			System.out.println("Error in getSites " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dtoList;

	}

	public int setSiteVehicle(ArrayList<SiteDto> siteDtoList) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		String query = "insert into site_vehicle(siteId,vehicleTypeId) values (?,?)";
		String query1 = "delete from site_vehicle where siteId=?";
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		int changedBy = 0, autoincNumber;
		try {
			changedBy = Integer.parseInt(siteDtoList.get(0).getDoneBy());
			pst1 = con.prepareStatement(query1);
			pst1.setString(1, siteDtoList.get(0).getId());
			int returnvalue = pst1.executeUpdate();
			DbConnect.closeStatement(pst1);
			if (returnvalue == 1) {
				autoincNumber = Integer.parseInt(siteDtoList.get(0)
						.getVehicleType());
				auditLogEntryforsetsitevehicle(autoincNumber,
						AuditLogConstants.SITE_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_SITE_VEHICLE_ADDED,
						AuditLogConstants.WORK_FLOW_STATE_SITE_VEHICLE_REMOVED,
						AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
			}

			pst = con.prepareStatement(query);
			for (SiteDto siteDtoObj : siteDtoList) {
				pst.setString(1, siteDtoObj.getId());
				pst.setString(2, siteDtoObj.getVehicleType());
				returnInt += pst.executeUpdate();
				System.out.println("return" + returnInt);
				if (returnInt >= 1) {
					autoincNumber = Integer.parseInt(siteDtoObj
							.getVehicleType());
					auditLogEntryforsetsitevehicle(
							autoincNumber,
							AuditLogConstants.SITE_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORKFLOW_STATE_SITE_VEHICLE_ADDED,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}

			}

			// DbConnect.closeConnection(con);
		} catch (Exception e) {
			System.out.println("Error in getSites " + e);
		} finally {
			DbConnect.closeStatement(pst);

			DbConnect.closeConnection(con);
		}
		return returnInt;

	}

	private void auditLogEntryforsetsitevehicle(int autoincNumber,
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

	public ArrayList<SiteDto> getSiteVehicle(int siteId) {
		DbConnect ob = DbConnect.getInstance();
		ResultSet rs = null;
		Connection con = ob.connectDB();
		SiteDto siteDtoObj = null;
		ArrayList<SiteDto> siteDtoList = new ArrayList<SiteDto>();
		String query = "select s.id as siteId,s.site_name,v.id as vehicleId,v.type from site s,vehicle_type v ,site_vehicle sv where s.id=? and  sv.siteid=s.id and sv.vehicleTypeId=v.id";
		PreparedStatement pst = null;
		try {
			pst = con.prepareStatement(query);
			pst.setInt(1, siteId);
			rs = pst.executeQuery();
			while (rs.next()) {
				siteDtoObj = new SiteDto();
				siteDtoObj.setId(rs.getString("siteId"));
				siteDtoObj.setName(rs.getString("site_name"));
				siteDtoObj.setVehicleType(rs.getString("vehicleId"));
				siteDtoObj.setVehicleTypeName(rs.getString("type"));
				siteDtoList.add(siteDtoObj);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			System.out.println("Error in getSites " + e);
		} finally {
			DbConnect.closeConnection(con);
		}
		return siteDtoList;
	}

	public int addSite(SiteDto dto) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		ResultSet autogenrs = null;
		int changedBy = 0, autoincNumber = 0;
		String query = "insert into site(site_name,landmark,night_shift_start,night_shift_end, lady_security,branch) values (?,?,?,?,?,?)";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		PreparedStatement st = null;
		try {
			changedBy = Integer.parseInt(dto.getDoneBy());
			Statement st1 = con.createStatement();
			st = con.prepareStatement(query);
			st.setString(1, dto.getName());
			st.setInt(2, Integer.parseInt(dto.getLandmark()));
			st.setString(3, dto.getNight_shift_start());
			st.setString(4, dto.getNight_shift_end());
			st.setInt(5, (dto.getLady_securiy().equals("1") ? 1 : 0));
			st.setString(6, dto.getBranch());
			returnInt = st.executeUpdate();
			if (returnInt == 1) {
				autogenrs = st1
						.executeQuery("Select id from site where site_name='"
								+ dto.getName() + "' and landmark="
								+ Integer.parseInt(dto.getLandmark()));
				while (autogenrs.next()) {
					autoincNumber = autogenrs.getInt(1);
				}
				auditLogEntryforaddsite(autoincNumber,
						AuditLogConstants.SITE_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORKFLOW_STATE_SITE_ADDED,
						AuditLogConstants.AUDIT_LOG_CREATE_ACTION);

			}

		} catch (Exception e) {
			System.out.println("Error in addSites " + e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryforaddsite(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public ArrayList<SiteDto> getSites(String branchId) {

		ArrayList<SiteDto> dtoList = new ArrayList<SiteDto>();
		DbConnect ob = DbConnect.getInstance();

		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		PreparedStatement st = null;
		String query = "select s.id, s.site_name, s.landmark landmarkId, l.landmark landmarkName, s.night_shift_start, s.night_shift_end, case when s.lady_security=1  then 'yes' else 'No' end hasLadySecurity, s.lady_security, branch from site s left join landmark l on s.landmark=l.id  where s.branch=?";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			st = con.prepareStatement(query);
			st.setString(1, branchId);

			rs = st.executeQuery();
			while (rs.next()) {
				SiteDto dto = new SiteDto();

				dto.setId(rs.getString("id"));

				dto.setName(rs.getString("site_name"));

				dto.setLandmark(rs.getString("landmarkId"));
				dto.setLandmarkName(rs.getString("landmarkName"));

				dto.setNight_shift_start(rs.getString("night_shift_start"));
				dto.setNight_shift_end(rs.getString("night_shift_end"));

				dto.setLady_securiy(rs.getString("lady_security"));
				dto.setHasLadySecurity(rs.getString("hasLadySecurity"));

				dto.setBranch(rs.getString("branch"));

				dtoList.add(dto);

			}

		} catch (Exception e) {

			System.out.println("Error in getSites " + e);
		} finally {

			DbConnect.closeResultSet(rs);

			DbConnect.closeStatement(st);

			DbConnect.closeConnection(con);

		}

		return dtoList;
	}

	public int updateSite(SiteDto dto) {

		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		int changedBy = 0, autoincNumber = 0;

		String query = "update site set site_name=?,landmark=?,night_shift_start=?,night_shift_end=?, lady_security=? where id=?";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		PreparedStatement st = null;
		try {

			st = con.prepareStatement(query);
			st.setString(1, dto.getName());
			st.setInt(2, Integer.parseInt(dto.getLandmark()));
			st.setString(3, dto.getNight_shift_start());
			st.setString(4, dto.getNight_shift_end());
			st.setInt(5, (dto.getLady_securiy().equals("1") ? 1 : 0));
			st.setInt(6, Integer.parseInt(dto.getId()));

			returnInt = st.executeUpdate();
			if (returnInt == 1) {
				changedBy = Integer.parseInt(dto.getDoneBy());
				autoincNumber = Integer.parseInt(dto.getId());
				auditLogEntryforupdatesite(autoincNumber,
						AuditLogConstants.SITE_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_SITE_ADDED,
						AuditLogConstants.WORKFLOW_STATE_SITE_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

		} catch (Exception e) {
			System.out.println("Error in addSites " + e);
		} finally {

			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryforupdatesite(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public SiteDto getSite(String siteId) {

		DbConnect ob = DbConnect.getInstance();

		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		SiteDto dto = null;
		PreparedStatement st = null;
		String query = "select s.id, s.site_name ,s.landmark landmarkId, l.landmark landmarkName, s.night_shift_start, s.night_shift_end, case when s.lady_security=1  then 'yes' else 'No' end hasLadySecurity, s.lady_security, branch,s.combainedRouteOnweekoff from site s join landmark l on s.landmark=l.id  where s.id=?";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			st = con.prepareStatement(query);
			st.setString(1, siteId);
			rs = st.executeQuery();
			if (rs.next()) {
				dto = new SiteDto();
				dto.setId(rs.getString("id"));
				dto.setName(rs.getString("site_name"));
				dto.setLandmark(rs.getString("landmarkId"));
				dto.setLandmarkName(rs.getString("landmarkName"));
				dto.setNight_shift_start(rs.getString("night_shift_start"));
				dto.setNight_shift_end(rs.getString("night_shift_end"));
				dto.setLady_securiy(rs.getString("lady_security"));
				dto.setHasLadySecurity(rs.getString("hasLadySecurity"));
				dto.setBranch(rs.getString("branch"));
				dto.setWeekoffcombain(rs.getString("combainedRouteOnweekoff"));
			}

		} catch (Exception e) {

			System.out.println("Error in getSite " + e);
		} finally {

			DbConnect.closeResultSet(rs);

			DbConnect.closeStatement(st);

			DbConnect.closeConnection(con);

		}
		return dto;
	}

	public int addVendorToSite(String siteId, String[] vendors) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();

		String InserQuery = "insert into vendor_site (site, vendor) values(?,?)";
		String checkQuery = "select * from vendor_site where site=? and vendor=?";
		String deleteQuery = "delete from vendor_site where site=?  ";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PreparedStatement checkSt = null;
		PreparedStatement deleteSt = null;

		try {
			deleteSt = con.prepareStatement(deleteQuery);
			deleteSt.setString(1, siteId);
			deleteSt.executeUpdate();

			for (String vendor : vendors) {
				st = con.prepareStatement(InserQuery);
				checkSt = con.prepareStatement(checkQuery);
				checkSt.setString(1, siteId);
				checkSt.setString(2, vendor);

				rs = checkSt.executeQuery();
				if (!rs.next()) {

					st.setString(1, siteId);
					st.setString(2, vendor);
					returnInt += st.executeUpdate();

				}
				DbConnect.closeResultSet(rs);

			}

		} catch (Exception e) {
			System.out.println("Error in addvendortoSites  " + e);
		} finally {				
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(checkSt, st, deleteSt);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	public int deleteVendorsFromSite(String siteId) {

		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();

		String query = "delete from vendor_site where site=? ";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		PreparedStatement st = null;
		try {
			st = con.prepareStatement(query);
			st.setString(1, siteId);

			returnInt = st.executeUpdate();

		} catch (Exception e) {
			System.out.println("Error in delte vendor from site " + e);
		} finally {

			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}
	public String getsiteName(int siteid)
	{
		DbConnect ob = DbConnect.getInstance();
		Statement st=null;
		ResultSet rs=null; 
		Connection con=null;
		String sitename="";
		String query="Select site_name from site where id="+siteid;
		try{
			con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				sitename=rs.getString(1);
			}
		}catch (Exception e) {
			} finally {
                DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}

			return sitename;
		}
	public String setEscort(String siteId, String employeeId,
			String startTime, String endTime) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement pst = null;
	

		String query = "SELECT e.Gender,s.lady_security FROM site s,employee e WHERE e.id="
				+ employeeId
				+ " and e.site=s.id and  (('"
				+ startTime
				+ "'> night_shift_start or '"
				+ startTime
				+ "'<=night_shift_end) OR ('"
				+ endTime
				+ "'>night_shift_start or '"
				+ endTime
				+ "'<=night_shift_end))AND lady_security=? and s.id=? and e.gender='F'";
		try {
			con=ob.connectDB();
			System.out.println(query + " site " + siteId);
			pst = con.prepareStatement(query);
			pst.setString(1, SettingsConstant.INTEGER_ACTIVE);
			pst.setString(2, siteId);
			rs = pst.executeQuery();
			if (rs.next()) {
				return "YES";
			}
		} catch (Exception e) {
			System.out.println("Error in secuirty check in adhoc routing " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return "NO";
	}
}
