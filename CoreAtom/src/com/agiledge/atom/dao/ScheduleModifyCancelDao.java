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

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.ScheduleModifyCancelDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.service.SchedulingService;


/**
 * 
 * @author Administrator
 */
public class ScheduleModifyCancelDao {

	public int scheduleCancel(String[] scheduleIdList,ScheduleModifyCancelDto dto1) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int ChangedBy,autoincNumber=0;
		try {
			pst = con
					.prepareCall("update employee_schedule set status ='c',cancel_date=curdate() where id =?");
			for (String scheduledId : scheduleIdList) {
				pst.setString(1, scheduledId);
				ChangedBy=Integer.parseInt(dto1.getScheduledBy());
				autoincNumber=Integer.parseInt(scheduledId);
				returnInt = pst.executeUpdate();
                if(returnInt==1)
				{
				auditLogEntryforschedulecancel(autoincNumber,AuditLogConstants.SCHEDULE_EMP
						, ChangedBy,
						AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
						AuditLogConstants.WORKFLOW_STATE_CANCEL_SCHEDULE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}
		} catch (Exception e) {
			returnInt = 0;
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}
		private void auditLogEntryforschedulecancel(int autoincNumber, String Module,
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

	public void scheduleCancel() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		ResultSet autogenrs = null;
		PreparedStatement pst = null;
		int autoincNumber = 0;
		int ChangedBy = 0;
		try {
			pst = con
					.prepareStatement("update employee_schedule set status ='c', cancel_date=curdate() where status='a' and subscription_id in (select subscriptionID from employee_subscription where subscriptionStatus='unsubscribed') and to_date>curdate() and subscription_id in (select subscriptionID from employee_subscription where subscriptionStatus='unsubscribed')");


Statement st=con.createStatement();
autogenrs=st.executeQuery("select id from employee_schedule where status='a'  and to_date>curdate() and subscription_id in (select subscriptionID from employee_subscription where subscriptionStatus='unsubscribed')");
while(autogenrs.next())
{
	autoincNumber=autogenrs.getInt(1);
	auditLogEntryforsccancel(autoincNumber,AuditLogConstants.SCHEDULE_EMP
			, ChangedBy,
			AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
			AuditLogConstants.WORKFLOW_STATE_CANCEL_SCHEDULE,
			AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
}
			pst.executeUpdate();
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			try {
				pst.close();
			} catch (Exception e) {
			}
			try {
				con.close();
			} catch (Exception e) {
			}

		}
	}
		private void auditLogEntryforsccancel(int autoincNumber, String Module,
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

	public int scheduleModify(
			ArrayList<ScheduleModifyCancelDto> scheduleModifyList) {
		int returnInt = 0;
ArrayList<SchedulingDto> schedulingDtos=new ArrayList<SchedulingDto>();
SchedulingDto schedulingDto=null;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet autogenrs;
		int ChangedBy, autoincNumber = 0;
		try {
			
			
			for (ScheduleModifyCancelDto scheduleModifyCancelDto : scheduleModifyList) {
				ChangedBy=Integer.parseInt(scheduleModifyCancelDto.getScheduledBy());
				
				if(scheduleModifyCancelDto.getLoginTime()==null||scheduleModifyCancelDto.getLoginTime().equals("")||scheduleModifyCancelDto.getLoginTime().equals("null"))
				{
					
					pst1=con.prepareStatement("delete  from employee_schedule_alter where scheduleId=?");
					pst1.setString(1, scheduleModifyCancelDto.getScheduleId());
					pst1.executeUpdate();
				pst = con.prepareStatement("update employee_schedule set project=?, statusDate=now() where id=?");
				pst.setString(1, scheduleModifyCancelDto.getProject());
				pst.setString(2, scheduleModifyCancelDto.getScheduleId());
				returnInt += pst.executeUpdate();
				autoincNumber=Integer.parseInt(scheduleModifyCancelDto.getScheduleId());
			    auditLogEntryformodify(autoincNumber,AuditLogConstants.SCHEDULE_EMP,
						 ChangedBy,
						AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
						AuditLogConstants.WORKFLOW_STATE_MODIFY_SCHEDULE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
				else
				{
					pst = con.prepareStatement("update employee_schedule set project=?,login_time=?,logout_time=?,statusDate=now() where id=?");
					pst1 = con.prepareStatement("delete  from employee_schedule_alter where scheduleId=? and date>curdate()");
					
					schedulingDto=new SchedulingDto();
					schedulingDto.setSchedulingFromDate(scheduleModifyCancelDto.getFromDate());
					schedulingDto.setSchedulingToDate(scheduleModifyCancelDto.getToDate());
					schedulingDto.setWeeklyOff(scheduleModifyCancelDto.getWeeklyoff());
					schedulingDto.setScheduleId(Long.parseLong(scheduleModifyCancelDto.getScheduleId()));
					schedulingDto.setScheduledBy(scheduleModifyCancelDto.getScheduledBy());
					
					schedulingDtos.add(schedulingDto);
					
				pst.setString(1, scheduleModifyCancelDto.getProject());
				pst.setString(2, scheduleModifyCancelDto.getLoginTime());
				pst.setString(3, scheduleModifyCancelDto.getLogoutTime());
				pst.setString(4, scheduleModifyCancelDto.getScheduleId());
				returnInt += pst.executeUpdate();				
				autoincNumber=Integer.parseInt(scheduleModifyCancelDto.getScheduleId());
			    auditLogEntryformodify(autoincNumber,AuditLogConstants.SCHEDULE_EMP,
						 ChangedBy,
						AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
						AuditLogConstants.WORKFLOW_STATE_MODIFY_SCHEDULE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				pst1.setString(1, scheduleModifyCancelDto.getScheduleId());
				Statement st = con.createStatement();
				autogenrs = st
						.executeQuery("Select id from employee_schedule_alter where scheduleId="
								+ scheduleModifyCancelDto.getScheduleId()
								+ " and date>curdate()");
				returnInt += pst1.executeUpdate();
				ChangedBy=Integer.parseInt(scheduleModifyCancelDto.getScheduledBy());
				if(returnInt>0)
				{
				while(autogenrs.next())
					{
						autoincNumber=autogenrs.getInt(1);
						auditLogEntryformodify(autoincNumber,AuditLogConstants.SCHEDULE_EMP
								, ChangedBy,
								AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
								AuditLogConstants.WORKFLOW_STATE_CANCEL_SCHEDULE,
								AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
					}
			}

				// System.out.println("returnInt : "+returnInt);
			}
			}			
			

			new SchedulingService().insertEmployeesWeeklyOff(schedulingDtos);
		} catch (Exception e) {
			returnInt = 0;
			System.out.println("Error in DAO" + e);
		} finally {
			
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}
		private void auditLogEntryformodify(int autoincNumber, String Module,
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

	public int scheduleModifySingle(
			ScheduleModifyCancelDto scheduleModifyCancelDto) {
		DbConnect ob = DbConnect.getInstance();
		int returnInt = -1;
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int ChangedBy,autoincNumber;
		try {
			pst = con
					.prepareCall("update employee_schedule set project=?,login_time=?,statusDate=curdate(),logout_time=? where id=?");
			pst.setString(1, scheduleModifyCancelDto.getProject());
			pst.setString(2, scheduleModifyCancelDto.getLoginTime());
			pst.setString(3, scheduleModifyCancelDto.getLogoutTime());
			pst.setString(4, scheduleModifyCancelDto.getScheduleId());
			// System.out.println("Schedule Id"+scheduleModifyCancelDto.getProject());
			ChangedBy = Integer.parseInt(scheduleModifyCancelDto
					.getScheduledBy());
			autoincNumber = Integer.parseInt(scheduleModifyCancelDto
					.getScheduleId());
			returnInt = pst.executeUpdate();
			auditLogEntryforsinglemodify(autoincNumber,
					AuditLogConstants.SCHEDULE_EMP, ChangedBy,
					AuditLogConstants.WORKFLOW_STATE_SCHEDULED,
					AuditLogConstants.WORKFLOW_STATE_MODIFY_SCHEDULE,
					AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			// pst1.setString(1, scheduleModifyCancelDto.getScheduleId());
			// pst1.executeUpdate();
		} catch (Exception e) {
			returnInt = 0;
			System.out.println("Error in DAO" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}
		private void auditLogEntryforsinglemodify(int autoincNumber, String Module,
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
}
