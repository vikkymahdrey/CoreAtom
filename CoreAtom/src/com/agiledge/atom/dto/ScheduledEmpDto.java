/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

import java.text.ParseException;

/**
 * 
 * @author Administrator
 */
public class ScheduledEmpDto {

	private String employeeId;
	private String scheduleId;
	private String scheduledBy;
	private String employeePersonnelNo;
	private String employeeName;
	private String project;
	private String projectDescription;
	private String from_date;
	private String to_date;
	private String loginTime;
	private String logoutTime;
	private String status;
	private String cancelDate;
	private String statusDate;
	private String scheduleAlterId;
	private int multistatus;
	private String weekoffstatus;

	public String getScheduleAlterId() {
		return scheduleAlterId;
	}

	public void setScheduleAlterId(String scheduleAlterId) {
		this.scheduleAlterId = scheduleAlterId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getProjectDescription() {
		return projectDescription;
	}

	public void setProjectDescription(String projectDescription) {
		this.projectDescription = projectDescription;
	}

	public String getScheduledBy() {
		return scheduledBy;
	}

	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getEmployeePersonnelNo() {
		return employeePersonnelNo;
	}

	public void setEmployeePersonnelNo(String employeePersonnelNo) {
		this.employeePersonnelNo = employeePersonnelNo;
	}

	public String getFrom_date() throws ParseException {

		// return changeDateFromat(from_date);
		return from_date;
	}

	public void setFrom_date(String from_date) {
		this.from_date = from_date;
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

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTo_date() throws ParseException {
		return to_date;
	}

	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}

	public String getCancelDate() {
		return cancelDate;
	}

	public void setCancelDate(String cancelDate) {
		this.cancelDate = cancelDate;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public int getMultistatus() {
		return multistatus;
	}

	public void setMultistatus(int multistatus) {
		this.multistatus = multistatus;
	}

	public String getWeekoffstatus() {
		return weekoffstatus;
	}

	public void setWeekoffstatus(String weekoffstatus) {
		this.weekoffstatus = weekoffstatus;
	}
}
