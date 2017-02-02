/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

import java.sql.Date;

/**
 * 
 * @author Administrator
 */
public class ScheduleAlterDto {

	@Override
	public String toString() {
		return "ScheduleAlterDto [scheduleId=" + scheduleId + ", scheduledBy="
				+ scheduledBy + ", status=" + status + ", date=" + date
				+ ", loginTime=" + loginTime + ", logoutTime=" + logoutTime
				+ "]";
	}
	private String scheduleId;
	private String scheduleStates;
	private String scheduledBy;
	private String scheduledfor;

	public String getScheduledBy() {
		return scheduledBy;
	}

	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}
	
	public String getScheduledfor() {
		return scheduledfor;
	}

	public void setScheduledfor(String scheduledfor) {
		this.scheduledfor = scheduledfor;
	}
	
	private String status;
	private String UpdatedById;
	private String UpdatedByDisplayName;

	public String getUpdatedById() {
		return UpdatedById;
	}

	public void setUpdatedById(String updatedById) {
		UpdatedById = updatedById;
	}

	public String getUpdatedByDisplayName() {
		return UpdatedByDisplayName;
	}

	public void setUpdatedByDisplayName(String updatedByDisplayName) {
		UpdatedByDisplayName = updatedByDisplayName;
	}

	public String getScheduleStates() {
		return scheduleStates;
	}

	public void setScheduleStates(String scheduleStates) {
		this.scheduleStates = scheduleStates;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	private String date;
	private String loginTime;
	private String logoutTime;
	private Date fromDate;
	private Date toDate;
	private String statusDate;


	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getLoginTime() {
		return loginTime;
	}

	public void setLoginTime(String loginTime) {
		this.loginTime = loginTime;
	}

	public String getLogoutTime() {
		return logoutTime;
	}

	public void setLogoutTime(String logoutTime) {
		this.logoutTime = logoutTime;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		if(statusDate==null)
		{
			this.statusDate="";
		}
		this.statusDate = statusDate;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}
}
