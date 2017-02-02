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

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.LogTimeDto;


/**
 * 
 * @author Administrator
 */
public class LogTimeDao {
	/*
	 * public ArrayList<LogTimeDto> getAllPrimaryRouteLogtime() { DbConnect ob =
	 * DbConnect.getInstance(); Statement st = null; ResultSet rs = null;
	 * Connection con = ob.connectDB(); LogTimeDto logTimeDtoObj = null;
	 * ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>(); try { st
	 * = con.createStatement(); rs = st.executeQuery(
	 * "select * from logtime where status='active' and combroute='no'  order by logtime "
	 * ); while (rs.next()) { logTimeDtoObj = new LogTimeDto();
	 * logTimeDtoObj.setId(rs.getInt(1));
	 * logTimeDtoObj.setLogTime(rs.getString(2));
	 * logTimeDtoObj.setLogtype(rs.getString(4));
	 * logTimeDtoObj.setStatus(rs.getString("combroute"));
	 * logTimeList.add(logTimeDtoObj); } rs.close(); st.close(); con.close(); }
	 * catch (Exception e) { System.out.println("ERROR in Time1" + e); }
	 * 
	 * return logTimeList;
	 * 
	 * }
	 */

	public ArrayList<LogTimeDto> getAllRouteLogtime(int siteId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("(select l.id,l.logtime,l.logtype,'combained' as combroute from logtime l  where l.id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ siteId
					+ " and   ss.combainroute='yes')  and  l.status='active') union (select l.id,l.logtime,l.logtype,'primary' as combroute from logtime l  where l.id  in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ siteId
					+ " and   ss.combainroute='no')  and  l.status='active') order by logtime");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(3));
				logTimeDtoObj.setStatus(rs.getString("combroute"));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;

	}

	/*
	 * public ArrayList<LogTimeDto> getAllCombainedRouteLogtime() { DbConnect ob
	 * = DbConnect.getInstance(); Statement st = null; ResultSet rs = null;
	 * Connection con = ob.connectDB(); LogTimeDto logTimeDtoObj = null;
	 * ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>(); try { st
	 * = con.createStatement(); rs = st.executeQuery(
	 * "select * from logtime where status='active' and combroute='yes' order by logtime "
	 * ); while (rs.next()) { logTimeDtoObj = new LogTimeDto();
	 * logTimeDtoObj.setId(rs.getInt(1));
	 * logTimeDtoObj.setLogTime(rs.getString(2));
	 * logTimeDtoObj.setLogtype(rs.getString(4));
	 * logTimeDtoObj.setStatus(rs.getString("combroute"));
	 * logTimeList.add(logTimeDtoObj); } rs.close(); st.close(); con.close(); }
	 * catch (Exception e) { System.out.println("ERROR in Time2" + e); }
	 * 
	 * return logTimeList;
	 * 
	 * }
	 */
	public ArrayList<LogTimeDto> getAllGeneralLogtime() {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("(select *,'Shared' as shifttype from logtime where id not in (select shifttime from project_shifttime) ) union (select *,'Project' from logtime where id in (select shifttime from project_shifttime))  order by logtime  ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setActiveStatus(rs.getString(3));

				logTimeDtoObj.setLogtype(rs.getString(4));
				logTimeDtoObj.setStatus(rs.getString("combroute"));
				logTimeDtoObj.setShiftType(rs.getString("shifttype"));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time3" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}

	public ArrayList<LogTimeDto> getAllLogtime(String log) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from logtime where status='active' and logtype='"
					+ log + "'  order by logtime ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(4));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time4" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}

	public ArrayList<LogTimeDto> getAllLogtime(String log, String site) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			// System.out.println("select * from logtime where status='active' and logtype='"
			// + log +
			// "' and id in (select ss.shiftId  from site_shift ss where ss.siteId="+site+")  order by logtime ");
			System.out.println("select * from logtime where status='active' and logtype='"
					+ log
					+ "' and (id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ site + ") or id in (select ssa.shiftId  from siteshiftadhoc ssa where ssa.siteId="
					+ site + " and status='a') )  order by logtime ");
			rs = st.executeQuery("select * from logtime where status='active' and logtype='"
					+ log
					+ "' and (id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ site + ") or id in (select ssa.shiftId  from siteshiftadhoc ssa where ssa.siteId="
					+ site + " and status='a') )  order by logtime ");
			
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(4));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time4" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}

	
	
	public ArrayList<LogTimeDto> getAllLogtimeWithExtension(String log, String site) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			// System.out.println("select * from logtime where status='active' and logtype='"
			// + log +
			// "' and id in (select ss.shiftId  from site_shift ss where ss.siteId="+site+")  order by logtime ");
			rs = st.executeQuery("select * from logtime where status='active' and logtype='"
					+ log
					+ "' and ( id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ site + ") or id in (select ssa.shiftId  from siteShiftAdhoc ssa where ssa.siteId="
					+ site + " and status='a') ) order by logtime ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(4));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time4" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}
	
	public ArrayList<LogTimeDto> getAllInactiveLogtime(String log, String site) {
		// System.out.println("In inactive");
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select * from logtime where status='inactive' and logtype='"
					+ log
					+ "' and id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ site + ")  order by logtime ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(4));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time4" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}

	public ArrayList<LogTimeDto> getAllGeneralLogtime(String log, String site) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select  id, logtime, logtype from logtime l where l.status='active' and l.logtype='"
					+ log
					+ "' and l.id not in ( select shifttime as id from project_shifttime ) and l.id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ site + ") order by logtime ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt("id"));
				logTimeDtoObj.setLogTime(rs.getString("logtime"));
				logTimeDtoObj.setLogtype(rs.getString("logtype"));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time5" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;
	}

	public int insertLogtime(LogTimeDto logTimeDto) throws SQLException {
		int returnvalue=0;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null, autogenrs = null;
		int changedBy = 0, autoincNumber;
		Connection con = ob.connectDB();
		String curworkstate = AuditLogConstants.WORK_FLOW_STATE_CREATE_LOGOUT;
		try {
			changedBy = Integer.parseInt(logTimeDto.getDoneBy());
			pst = con
					.prepareStatement("select logtime from logtime where logtime=? and logtype=? and status='active' ");
			pst.setString(1, logTimeDto.getLogTime());
			pst.setString(2, logTimeDto.getLogtype());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("insert into logtime(logtime,logtype,status) values (?,?,'active')");
				pst.setString(1, logTimeDto.getLogTime());
				pst.setString(2, logTimeDto.getLogtype());
				 returnvalue = pst.executeUpdate();
				if (returnvalue == 1) {
					Statement st = con.createStatement();
					autogenrs = st
							.executeQuery("Select id from logtime where logtime='"
									+ logTimeDto.getLogTime()
									+ "' and logtype='"
									+ logTimeDto.getLogtype()
									+ "' and status='active'");
					while (autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
						if (logTimeDto.getLogtype().equals("IN")) {
							curworkstate = AuditLogConstants.WORK_FLOW_STATE_CREATE_LOGIN;
						}
						auditLogEntryforinsertlogtime(autoincNumber,
								AuditLogConstants.SHIFTTIME_MODULE, changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								curworkstate,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}
				}
			}

		} catch (Exception e) {

			System.out.println("ERROR in Time6" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeConnection(con);
		}
		return returnvalue;
	}

	public int updateLogtimeCombainRoute(String[] logTimeIds,
			String weekoffStatus, String siteId) throws SQLException {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("update site_shift set combainroute=? where siteId=?");
			pst.setString(1, "no");
			pst.setString(2, siteId);
			pst.executeUpdate();
			pst = con
					.prepareStatement("update site_shift set combainroute=? where siteId=? and shiftId=?");
			for (String logTimeId : logTimeIds) {
				pst.setString(1, "yes");
				pst.setString(2, siteId);
				pst.setString(3, logTimeId);
				retVal += pst.executeUpdate();
			}
			if (weekoffStatus != null && weekoffStatus.equalsIgnoreCase("on")) {
				pst = con
						.prepareStatement("update site set combainedRouteOnweekoff=1 where id=?");
				pst.setString(1, siteId);
				pst.executeUpdate();
			} else {
				pst = con
						.prepareStatement("update site set combainedRouteOnweekoff=0 where id=?");
				pst.setString(1, siteId);
				pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time7" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	private void auditLogEntryforinsertlogtime(int autoincNumber,
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

	public void updateLogtime(LogTimeDto logTimeDto) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int changedBy = 0, autoincNumber;
		ResultSet autogenrs = null;
		String currentstate = null, previousstate = null;
		Connection con = ob.connectDB();
		try {
			changedBy = Integer.parseInt(logTimeDto.getDoneBy());
			pst = con
					.prepareStatement("select logtime from logtime where logtime=? and logtype=(select logtype from logtime where id=?) and status='active' ");

			pst.setString(1, logTimeDto.getLogTime());
			pst.setInt(2, logTimeDto.getId());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update logtime set logtime=? where id=?");
				pst.setString(1, logTimeDto.getLogTime());
				pst.setInt(2, logTimeDto.getId());
				int returnvalue = pst.executeUpdate();
				if (returnvalue == 1) {
					autoincNumber = logTimeDto.getId();
					Statement st = con.createStatement();
					autogenrs = st
							.executeQuery("Select logtype from logtime where id='"
									+ logTimeDto.getId() + "'");
					while (autogenrs.next()) {
						if (autogenrs.getString(1).equals("IN")) {
							previousstate = AuditLogConstants.WORK_FLOW_STATE_CREATE_LOGIN;
							currentstate = AuditLogConstants.WORK_FLOW_STATE_EDIT_LOGIN;
						} else {
							previousstate = AuditLogConstants.WORK_FLOW_STATE_CREATE_LOGOUT;
							currentstate = AuditLogConstants.WORK_FLOW_STATE_EDIT_LOGOUT;
						}
					}
					auditLogEntryforupdatelogtime(autoincNumber,
							AuditLogConstants.SHIFTTIME_MODULE, changedBy,
							previousstate, currentstate,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time8" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
	}

	private void auditLogEntryforupdatelogtime(int autoincNumber,
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

	public LogTimeDto getLogtimeById(int id) throws SQLException {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		LogTimeDto logTimeDtoObj = new LogTimeDto();
		Connection con = ob.connectDB();
		try {
			String query = "select  lt.id, lt.logtime, lt.logtype,  (select count(*) from project_shifttime s where lt.id=s.shifttime   )  projects from logtime lt where id=? ";
			pst = con.prepareStatement(query);
			pst.setInt(1, id);
			rs = pst.executeQuery();
			if (rs.next()) {
				logTimeDtoObj.setId(rs.getInt("id"));
				logTimeDtoObj.setLogTime(rs.getString("logtime"));
				logTimeDtoObj.setLogtype(rs.getString("logtype"));
				logTimeDtoObj.setProjects(rs.getInt("projects"));
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time9" + e);

		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return logTimeDtoObj;
	}

	public int deleteLogTime(String id, String status, String doneBy) {
		// TODO Auto-generated method stub
		int returnInt = -1;
		String flag="";
		DbConnect ob = DbConnect.getInstance();
		String previousstate = null, currentworkstate = null;
		ResultSet autogenrs = null;
		int changedBy = 0, autoincNumber;
		String query = "";
		if (status.equals("disable")) {
			query = "update logtime set status='inactive'  where id=" + id;
			flag="disable";
		} else {
			query = "update logtime set status='active'  where id=" + id;
			flag="enable";
		}
		Connection con = ob.connectDB();
		Statement st = null;
		// PreparedStatement pst = null;
		try {
			changedBy = Integer.parseInt(doneBy);
			autoincNumber = Integer.parseInt(id);
			st = con.createStatement();
			autogenrs = st.executeQuery("Select logtype from logtime where id="
					+ id);
			while (autogenrs.next()) {
				if (autogenrs.getString(1).equals("IN")) {
					if(flag.equals("disable"))
					{
					previousstate = AuditLogConstants.WORK_FLOW_STATE_ENABLE_LOGIN;
					currentworkstate = AuditLogConstants.WORK_FLOW_STATE_DISABLE_LOGIN;
					}
					else
					{
						currentworkstate = AuditLogConstants.WORK_FLOW_STATE_ENABLE_LOGIN;
						previousstate = AuditLogConstants.WORK_FLOW_STATE_DISABLE_LOGIN;
					}
				}
				if (autogenrs.getString(1).equals("OUT")) {
					if(flag.equals("disable"))
					{
					previousstate = AuditLogConstants.WORK_FLOW_STATE_ENABLE_LOGOUT;
					currentworkstate = AuditLogConstants.WORK_FLOW_STATE_DISABLE_LOGOUT;
					}else
					{
						currentworkstate = AuditLogConstants.WORK_FLOW_STATE_ENABLE_LOGOUT;
						previousstate = AuditLogConstants.WORK_FLOW_STATE_DISABLE_LOGOUT;
					}
				}
			}
			returnInt = st.executeUpdate(query);
			if (returnInt == 1) {
				auditLogEntryfordeletelogtime(autoincNumber,
						AuditLogConstants.SHIFTTIME_MODULE, changedBy,
						previousstate, currentworkstate,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

			// System.out.println("        successssssss");
		} catch (Exception e) {
			System.out.println("Error in addSites " + e);
		} finally {
			DbConnect.closeResultSet(autogenrs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}

	private void auditLogEntryfordeletelogtime(int autoincNumber,
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

	//

	public ArrayList<LogTimeDto> getProjectSpecificTime(String projectId,
			String logType) {

		ArrayList<LogTimeDto> timeList = new ArrayList<LogTimeDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;

		try {
			st = con.createStatement();
			String query = "  select  t.id, t.logtime, t.logtype, pt.project from logtime t join project_shifttime pt on t.id=pt.shifttime    where status='active' and logtype='"
					+ logType
					+ "' and project="
					+ projectId
					+ "  order by logtime ";
			// System.out.println(query);
			rs = st.executeQuery(query);
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt("id"));
				logTimeDtoObj.setLogTime(rs.getString("logtime"));
				logTimeDtoObj.setLogtype(rs.getString("logtype"));
				timeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time10" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return timeList;
	}

	public ArrayList<LogTimeDto> getAllTimeWithProjectSpecific(
			String projectId, String logType, String siteId) {

		ArrayList<LogTimeDto> timeList = new ArrayList<LogTimeDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;

		try {
			
			st = con.createStatement();

			String query = " select s.id, s.logtime, s.status, s.logtype from ( select id, logtime, status, logtype from logtime where id not in (select shifttime id from project_shifttime)  ) s,site_shift ss where ss.siteId="
					+ siteId
					+ " and ss.shiftId=s.id and   s.status='active' and s.logtype='"
					+ logType
					+ "'"
					+ "union (select  t.id, t.logtime, t.status, t.logtype from logtime t join project_shifttime pt on t.id=pt.shifttime    where t.status='active' and   project="
					+ projectId
					+ " and t.logtype='"
					+ logType
					+ "')"
					+ "  order by logtime ";
			rs = st.executeQuery(query);
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt("id"));
				logTimeDtoObj.setLogTime(rs.getString("logtime"));
				logTimeDtoObj.setLogtype(rs.getString("logtype"));
				timeList.add(logTimeDtoObj);
			}			

		} catch (Exception e) {
			System.out.println("ERROR in Time11" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return timeList;
	}

	public ArrayList<LogTimeDto> getAllTimeWithProjectSpecificFromDao(Connection con,
			String projectId, String logType, String siteId) {

		ArrayList<LogTimeDto> timeList = new ArrayList<LogTimeDto>();
		Statement st1 = null;
		ResultSet rs1 = null;
		LogTimeDto logTimeDtoObj = null;

		try {
			if(projectId!=null && !projectId.equals("") ){
			st1 = con.createStatement();

			String query = " select s.id, s.logtime, s.status, s.logtype from ( select id, logtime, status, logtype from logtime where id not in (select shifttime id from project_shifttime)  ) s,site_shift ss where ss.siteId="
					+ siteId
					+ " and ss.shiftId=s.id and   s.status='active' and s.logtype='"
					+ logType
					+ "'"
					+ "union (select  t.id, t.logtime, t.status, t.logtype from logtime t join project_shifttime pt on t.id=pt.shifttime    where t.status='active' and   project="
					+ projectId
					+ " and t.logtype='"
					+ logType
					+ "')"
					+ "  order by logtime ";
			rs1 = st1.executeQuery(query);
			//.System.out.println(query);
			while (rs1.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs1.getInt("id"));
				logTimeDtoObj.setLogTime(rs1.getString("logtime"));
				logTimeDtoObj.setLogtype(rs1.getString("logtype"));
				timeList.add(logTimeDtoObj);
			}
			}
		} catch (Exception e) {
			System.out.println("ERROR in TimePP" + e);
		} finally {
			DbConnect.closeResultSet(rs1);
			DbConnect.closeStatement(st1);
		}

		return timeList;
	}

	
	
	public ArrayList<LogTimeDto> getAllLogtime(int siteId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("(select l.id,l.logtime,l.logtype,'primary' as combroute from logtime l  where l.id not  in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ siteId
					+ ")  and  l.status='active') union (select l.id,l.logtime,l.logtype,'combained' as combroute from logtime l  where l.id in (select ss.shiftId  from site_shift ss where ss.siteId="
					+ siteId + ")  and  l.status='active') order by logtime ");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(3));
				logTimeDtoObj.setStatus(rs.getString("combroute"));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;

	}

	public int updateSiteShift(String[] shifts, String siteId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("delete from  site_shift where siteId=?");
			pst.setString(1, siteId);
			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into site_shift(siteId,shiftId,combainroute,status) values(?,?,?,?)");
			for (String logTimeId : shifts) {
				pst.setString(1, siteId);
				pst.setString(2, logTimeId);
				pst.setString(3, "no");
				pst.setString(4, "active");
				retVal += pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time7" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}
	public ArrayList<LogTimeDto> getShiftAdhoc(int siteId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select lt.id,lt.logtime,lt.logtype from logtime lt,siteShiftAdhoc ssa where ssa.siteId="+siteId+" and  lt.id=ssa.shiftId and ssa.status='a'");
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(3));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;

	}

	public ArrayList<LogTimeDto> getAdhocShift(String pickDrop) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;
		ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select lt.id,lt.logtime,lt.logtype from logtime lt,siteShiftAdhoc ssa where lt.id=ssa.shiftId and ssa.status='a' and lt.logtype='"+pickDrop+"'");			
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt(1));
				logTimeDtoObj.setLogTime(rs.getString(2));
				logTimeDtoObj.setLogtype(rs.getString(3));
				logTimeList.add(logTimeDtoObj);
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return logTimeList;

		
	}

	public ArrayList<LogTimeDto> getNotAdhocLogtime(int siteId) {
			DbConnect ob = DbConnect.getInstance();
			Statement st = null;
			ResultSet rs = null;
			Connection con = ob.connectDB();
			LogTimeDto logTimeDtoObj = null;
			ArrayList<LogTimeDto> logTimeList = new ArrayList<LogTimeDto>();
			try {
				st = con.createStatement();
				rs = st.executeQuery("select lt.id,lt.logtime,lt.logtype from logtime lt  where lt.id not in (select shiftid from siteShiftAdhoc where  status='a' and siteId="+siteId+")");
				while (rs.next()) {
					logTimeDtoObj = new LogTimeDto();
					logTimeDtoObj.setId(rs.getInt(1));
					logTimeDtoObj.setLogTime(rs.getString(2));
					logTimeDtoObj.setLogtype(rs.getString(3));
					logTimeList.add(logTimeDtoObj);
				}

			} catch (Exception e) {
				System.out.println("ERROR in Time1" + e);
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}

			return logTimeList;
		

	}
	
	public int updateAdhocSiteShift(String[] shifts, String siteId) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("update siteShiftAdhoc set status='c'  where siteId=?");
			pst.setString(1, siteId);
			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into siteShiftAdhoc(siteId,shiftId,status) values(?,?,?)");
			for (String logTimeId : shifts) {
				pst.setString(1, siteId);
				pst.setString(2, logTimeId);
				pst.setString(3, "a");
				retVal += pst.executeUpdate();
			}

		} catch (Exception e) {
			System.out.println("ERROR in Time7" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	
	public ArrayList<LogTimeDto> getAllTimeWithProjectSpecificForEmployee(String employeeId,String logType) {

		ArrayList<LogTimeDto> timeList = new ArrayList<LogTimeDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		LogTimeDto logTimeDtoObj = null;

		try {
			
			st = con.createStatement();

			String query = " select lt.* from employee e,logtime lt,site_shift ss where e.id="+employeeId+" and e.site=ss.siteid and lt.id=ss.shiftid and lt.logtype='"+logType+"' and  lt.id not in (select shifttime id from project_shifttime) union select lt.* from employee e,logtime lt,site_shift ss where e.id="+employeeId+" and e.site=ss.siteid and lt.id=ss.shiftid and lt.logtype='"+logType+"' and  lt.id in (select shifttime id from project_shifttime ps,atsconnect pr where e.project=pr.project and  ps.project=pr.id)";			
			rs = st.executeQuery(query);
			while (rs.next()) {
				logTimeDtoObj = new LogTimeDto();
				logTimeDtoObj.setId(rs.getInt("id"));
				logTimeDtoObj.setLogTime(rs.getString("logtime"));
				logTimeDtoObj.setLogtype(rs.getString("logtype"));
				timeList.add(logTimeDtoObj);
			}			

		} catch (Exception e) {
			System.out.println("ERROR in Time11" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return timeList;
	}
	
	public ArrayList<LogTimeDto> getAllTime()
	{
		ArrayList<LogTimeDto> list = new ArrayList<LogTimeDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try{
			st = con.createStatement();
			String query = "select id,concat(logtime,'-',logtype) as logtime from logtime order by logtime asc";			
			rs = st.executeQuery(query);
			while (rs.next()) {
				 LogTimeDto logTimeDtoObj = new LogTimeDto();
				 logTimeDtoObj.setId(rs.getInt("id"));
				 logTimeDtoObj.setLogTime(rs.getString("logtime"));
				 list.add(logTimeDtoObj);
			}
		}catch(Exception e){e.printStackTrace();}
		 finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}

		return list;
	}
}
