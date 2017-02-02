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
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.ScheduleAlterDto;


/**
 * 
 * @author Administrator
 */
public class ScheduleAlterDao {

	public ScheduleAlterDto getScheduleDetails(long scheduleId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		ScheduleAlterDto scheduleAlterDto = new ScheduleAlterDto();
		try {
			st = con.createStatement();
			rs = st.executeQuery("select es.from_date,es.to_date,es.login_time,es.logout_time,es.statusDate as statusDate,es.scheduledBy,e.displayName from employee_schedule es, employee e where e.id=es.scheduledBy and es.id="
					+ scheduleId + " and status='a'");
			while (rs.next()) {
				scheduleAlterDto.setFromDate(rs.getDate("from_date"));
				scheduleAlterDto.setToDate(rs.getDate("to_date"));
				scheduleAlterDto.setLoginTime(rs.getString("login_time"));
				scheduleAlterDto.setLogoutTime(rs.getString("logout_time"));
				scheduleAlterDto.setStatusDate(rs.getString("statusDate"));
				scheduleAlterDto.setUpdatedById(rs.getString("scheduledBy"));
				scheduleAlterDto.setUpdatedByDisplayName(rs
						.getString("displayName"));
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		}
		try {
			con.close();
		} catch (SQLException ex) {
		}
		return scheduleAlterDto;
	}

	public ArrayList<ScheduleAlterDto> getExistingAlter(long scheduleID) {

		ArrayList<ScheduleAlterDto> scheduleExistingAlter = null;
		DbConnect ob = DbConnect.getInstance();
		ScheduleAlterDto scheduleAlterDtoObj = null;
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			scheduleExistingAlter = new ArrayList<ScheduleAlterDto>();
			st = con.createStatement();
			rs = st.executeQuery("select esa.id,esa.scheduleId,esa.date,esa.login_time,esa.logout_time,esa.status,esa.statusDate as statusDate,esa.updatedBy,e.displayName from employee_schedule_alter esa,employee e where e.id=esa.updatedBy and esa.scheduleId="
					+ scheduleID + "");
			while (rs.next()) {
				scheduleAlterDtoObj = new ScheduleAlterDto();
				scheduleAlterDtoObj.setScheduleId(rs.getString("scheduleId"));
				scheduleAlterDtoObj.setDate("" + rs.getDate("date"));
				scheduleAlterDtoObj.setLoginTime(""
						+ rs.getString("login_time"));
				scheduleAlterDtoObj.setLogoutTime(""
						+ rs.getString("logout_time"));
				scheduleAlterDtoObj.setStatus(rs.getString("status"));
				scheduleAlterDtoObj.setStatusDate(rs.getString("statusDate"));
				scheduleAlterDtoObj.setUpdatedById(rs.getString("updatedBy"));
				scheduleAlterDtoObj.setUpdatedByDisplayName(rs
						.getString("displayName"));
				scheduleExistingAlter.add(scheduleAlterDtoObj);
			}
		} catch (Exception e) {
			System.out.println("ERRO" + e);
		}
		finally {
			DbConnect.closeConnection(con);
		} 
		return scheduleExistingAlter;
	}

	public int scheduleAlterInsert(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet autogenrs;
		int ChangedBy = 0;
		int autoincNumber = 0;
		try {
			con = ob.connectDB();
			String query = "";
			String query1 = "";
			query = "insert into employee_schedule_alter(scheduleId,date,login_time,logout_time,status,statusDate,updatedBy) values (?,?,?,?,?,curdate(),?)";
			query1 = "update employee_schedule_alter  set login_time=?,logout_time=?,statusDate=curdate(),updatedBy=? where scheduleId=? and date=?";
			pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst1 = con.prepareCall(query1);
			for (ScheduleAlterDto scheduleAlterDto : scheduleAlterDtoList) {
				if (scheduleAlterDto.getScheduleStates().equals("new")) {
					pst.setString(1, scheduleAlterDto.getScheduleId());
					pst.setString(2, scheduleAlterDto.getDate());
					pst.setString(3, scheduleAlterDto.getLoginTime());
					pst.setString(4, scheduleAlterDto.getLogoutTime());
					pst.setString(5, "a");
					pst.setString(6, scheduleAlterDto.getUpdatedById());
					returnInt = pst.executeUpdate();
					ChangedBy = Integer.parseInt(scheduleAlterDto
							.getUpdatedById());
					autogenrs = pst.getGeneratedKeys();
					while (autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
					}
					auditLogEntryforalterinsert(autoincNumber,
							AuditLogConstants.SCHEDULE_EMP, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.WORKFLOW_STATE_ALTER_SCHEDULE,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

				} else {
					pst1.setString(1, scheduleAlterDto.getLoginTime());
					pst1.setString(2, scheduleAlterDto.getLogoutTime());
					pst1.setString(3, scheduleAlterDto.getUpdatedById());
					pst1.setString(4, scheduleAlterDto.getScheduleId());
					pst1.setString(5, scheduleAlterDto.getDate());
					returnInt = pst1.executeUpdate();
					Statement st = con.createStatement();
					ChangedBy = Integer.parseInt(scheduleAlterDto
							.getScheduledBy());
					autogenrs = st
							.executeQuery("select id from employee_schedule_alter  where scheduleID='"
									+ scheduleAlterDto.getScheduleId()
									+ "' and date='"
									+ scheduleAlterDto.getDate() + "'");
					while (autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
				}
										auditLogEntryforalterinsert(autoincNumber,AuditLogConstants.SCHEDULE_EMP
							, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.WORKFLOW_STATE_ALTER_SCHEDULE,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
		}

} catch (Exception ex) {
			returnInt = 0;
			System.out.println("Erro" + ex);

		} finally {
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}
		private void auditLogEntryforalterinsert(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public int scheduleAlterCancel(
			ArrayList<ScheduleAlterDto> scheduleAlterDtoList) {
		int returnInt = -1;

		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet autogenrs;
		int ChangedBy;
		int autoincNumber = 0;
		try {
			String query = "";
			String query1 = "";
			query = "insert into employee_schedule_alter(scheduleId,date,status,statusdate) values (?,?,?,curdate())";
			query1 = "update employee_schedule_alter  set status=? where scheduleId=? and date=?";
			pst = con.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
			pst1 = con.prepareCall(query1);
			for (ScheduleAlterDto scheduleAlterDto : scheduleAlterDtoList) {
				if (scheduleAlterDto.getScheduleStates().equals("new")) {
					pst.setString(1, scheduleAlterDto.getScheduleId());
					pst.setString(2, scheduleAlterDto.getDate());
					pst.setString(3, "c");
					returnInt = pst.executeUpdate();
					ChangedBy=Integer.parseInt(scheduleAlterDto.getScheduledBy());
					autogenrs=pst.getGeneratedKeys();
					while (autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
					}
					auditLogEntryforaltercancel(autoincNumber,AuditLogConstants.SCHEDULE_EMP
							, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.WORKFLOW_STATE_CANCEL_SCHEDULE,
							AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
				} else {
					pst1.setString(1, "c");
					pst1.setString(2, scheduleAlterDto.getScheduleId());
					pst1.setString(3, scheduleAlterDto.getDate());
					ChangedBy = Integer.parseInt(scheduleAlterDto
							.getScheduledBy());
					returnInt = pst1.executeUpdate();
					Statement st=con.createStatement();
					autogenrs=st.executeQuery("select id from employee_schedule_alter  where scheduleID='"+scheduleAlterDto.getScheduleId()+"' and date='"+scheduleAlterDto.getDate()+"'");
					while (autogenrs.next()) {
						autoincNumber = autogenrs.getInt(1);
					}
					auditLogEntryforaltercancel(autoincNumber,AuditLogConstants.SCHEDULE_EMP
							, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
							AuditLogConstants.WORKFLOW_STATE_CANCEL_SCHEDULE,
							AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
				}
			}

		} catch (SQLException ex) {
			returnInt = 0;
			System.out.println("Erro" + ex);

		}

		finally {
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);

		}

		return returnInt;
	}
		private void auditLogEntryforaltercancel(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public String getScheduleDetailsByDate(String scheduleId, String date) {
		DbConnect ob = DbConnect.getInstance();		
		Connection con = ob.connectDB();
		String status="";
		Statement st = null;
		ResultSet rs = null;		
		try {			
			st = con.createStatement();
			rs = st.executeQuery("select * from employee_schedule_alter where scheduleId="
					+ scheduleId + " and date='" + date + "'");
			if (rs.next()) {
				status = "exist";
			} else {
				status = "new";
			}

		} catch (Exception e) {
			System.out.println("Errorr in scheduled date getting" + e);
		} finally {
			try {
				con.close();
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return status;

	}
	public int  getPendingAlterRequestCount(String role){
		DbConnect ob = DbConnect.getInstance();		
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		int count=0;
		try {
			st = con.createStatement();
			String getCount="select count(sar.empid) as count from schedulealterrequest sar,employee e1,employee e2,schedule_approve_config sac where sar.empid=e1.id and e1.role_id=sac.user_type and sac.approve_right=e2.role_id and sar.status='pending' and e2.usertype='"+role+"'";
			System.out.println(getCount);
			rs=st.executeQuery(getCount);
			if(rs.next()){
				count=rs.getInt("count");
			}
		} catch (SQLException e) {
			System.out.println("Error in ScheduleAlterDao@getPendingAlterRequestCount"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return count;
	}
	public ArrayList<ScheduleAlterDto>  getPendingAlterRequest(String role){
		DbConnect ob = DbConnect.getInstance();		
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		ArrayList<ScheduleAlterDto> list = new ArrayList<ScheduleAlterDto>();
		try {
			st = con.createStatement();
			String getCount="select e1.displayname,sar.scheduleId,sar.date,sar.logIn,sar.logOut,sar.status from schedulealterrequest sar,employee e1,employee e2,schedule_approve_config sac where sar.empid=e1.id and e1.role_id=sac.user_type and sac.approve_right=e2.role_id and sar.status='pending' and e2.usertype='"+role+"'";
			System.out.println(getCount);
			rs=st.executeQuery(getCount);
			while(rs.next()){
				ScheduleAlterDto dto= new ScheduleAlterDto();
				dto.setScheduledfor(rs.getString("displayname"));
				dto.setScheduleId(rs.getString("scheduleId"));
				dto.setDate(rs.getString("date"));
				dto.setLoginTime(rs.getString("logIn"));
				dto.setLogoutTime(rs.getString("logOut"));
				dto.setStatus(rs.getString("status"));
				list.add(dto);
			}
		} catch (SQLException e) {
			System.out.println("Error in ScheduleAlterDao@getPendingAlterRequest"+e);
		}finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return list;
	}

	public int approveScheduleAlter(String scheduleId, String ApprovedBy,
			String date, String status,String login,String logout) {
		DbConnect ob = DbConnect.getInstance();		
		Connection con = ob.connectDB();
		PreparedStatement pst = null,pst2=null;
		int result=0;
		try {
			String updateAlter="UPDATE schedulealterrequest SET status=? where scheduleId=? and date=?";
			pst=con.prepareStatement(updateAlter);
			pst.setString(1, status);
			pst.setString(2, scheduleId);
			pst.setString(3, date);
			pst.executeUpdate();
			
			String inserAlter="INSERT INTO employee_schedule_alter (scheduleId, date, login_time, logout_time,status, statusDate, updatedBy) VALUES (?, ?, ?, ?, 'a', curdate(), ?)";
			pst2=con.prepareStatement(inserAlter);
			pst2.setString(1, scheduleId);
			pst2.setString(2, date+" 00:00:00");
			pst2.setString(3, login);
			pst2.setString(4, logout);
			pst2.setString(5, ApprovedBy);
			result=pst2.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error in ScheduleAlterDao@approveScheduleAlter"+e);
		}finally{
			DbConnect.closeStatement(pst,pst2);
			DbConnect.closeConnection(con);
		}
		return result;
	}

	public ScheduleAlterDto getScheduleCutOff(){
		ScheduleAlterDto dto = new ScheduleAlterDto();
		
		
			DbConnect ob = DbConnect.getInstance();		
			Connection con = ob.connectDB();
			Statement st = null;
			ResultSet rs=null;
			try {
				if(SettingsConstant.scheduleSingleDayCutOff.equalsIgnoreCase("YES")){
				String qry="select login_cutoff,logout_cutoff from schedule_cutoff limit 1";
				st = con.createStatement();
				rs=st.executeQuery(qry);
				if(rs.next()){
					dto.setLoginTime(rs.getString("login_cutoff"));
					dto.setLogoutTime(rs.getString("logout_cutoff"));
				}
				
				}else{
					dto.setLoginTime("0");
					dto.setLogoutTime("0");
				}
			} catch (SQLException e) {
				System.out.println("Error in ScheduleAlterDao@getScheduleCutOff"+e);
			}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
		
		return dto;
	}
}
