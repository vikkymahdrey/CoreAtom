/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

import java.util.ArrayList;



/*
 *
 * @author Administrator
 */
public class SchedulingDto {

	 

	@Override
	public String toString() {
		return "SchedulingDto [status=" + status + ", subscriptionId="
				+ subscriptionId + ", employeeId=" + employeeId
				+ ", employeeName=" + employeeName + ", subscriptionDate="
				+ subscriptionDate + ", subscriptionToDate="
				+ subscriptionToDate + ", project=" + project
				+ ", description=" + description + ", loginTime=" + loginTime
				+ ", logoutTime=" + logoutTime + ", schedulingFromDate="
				+ schedulingFromDate + ", schedulingToDate=" + schedulingToDate
				+ ", scheduledBy=" + scheduledBy + ", isUpdated=" + isUpdated
				+ ", subscriptionQuestDate=" + subscriptionQuestDate
				+ ", statusDate=" + statusDate + ", weeklyOff=" + weeklyOff
				+ ", personnelNo=" + personnelNo + ", scheduleId=" + scheduleId
				+ ", alterList=" + alterList + ", loginTimeDto=" + loginTimeDto
				+ ", logoutTimeDto=" + logoutTimeDto + ", scheduledFromDate="
				+ scheduledFromDate + ", scheduledToDate=" + scheduledToDate
				+ "]";
	}

	private String status;
	private String subscriptionId;
	public String employeeId;
	private String employeeName;
	private String subscriptionDate;
	private String subscriptionToDate;
	private String project;
	private String description;
	private String loginTime;
	private String logoutTime;
	private String schedulingFromDate;
	private String schedulingToDate;
	private String scheduledBy;
	private boolean isUpdated;
	private String subscriptionQuestDate;
	private String statusDate;
	private String weeklyOff;
	private String personnelNo;
	private long scheduleId;
	private ArrayList<ScheduleAlterDto > alterList = new ArrayList<ScheduleAlterDto>();
	private ArrayList<LogTimeDto> loginTimeDto;
	private ArrayList<LogTimeDto> logoutTimeDto;
	private String scheduledFromDate;
	private String scheduledToDate;

	public String getScheduledFromDate() {
		return scheduledFromDate;
	}

	public void setScheduledFromDate(String scheduledFromDate) {
		this.scheduledFromDate = scheduledFromDate;
	}

	public String getScheduledToDate() {
		return scheduledToDate;
	}

	public void setScheduledToDate(String scheduledToDate) {
		this.scheduledToDate = scheduledToDate;
	}
 
	public void setPersonnelNo(String personnelNo)
	{
		this.personnelNo = personnelNo;
	}
	public String getPersonnelNo()
	{
		return personnelNo;
	}
	public boolean isIsUpdated() {
		return isUpdated;
	}

	public void setIsUpdated(boolean isUpdated) {
		this.isUpdated = isUpdated;
	}

	public String getScheduledBy() {
		return scheduledBy;
	}

	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}

	public String getSchedulingFromDate() {
		return schedulingFromDate;
	}

	public void setSchedulingFromDate(String schedulingFromDate) {
		this.schedulingFromDate = schedulingFromDate;
	}

	public String getSchedulingToDate() {
		return schedulingToDate;
	}

	public void setSchedulingToDate(String schedulingToDate) {
		this.schedulingToDate = schedulingToDate;
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

	public String getSubscriptionDate() {
		return subscriptionDate;
	}

	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getSubscriptionQuestDate() {
		return subscriptionQuestDate;
	}

	public void setSubscriptionQuestDate(String subscriptionQuestDate) {
		this.subscriptionQuestDate = subscriptionQuestDate;
	}

	public String getStatusDate() {
		return statusDate;
	}

	public void setStatusDate(String statusDate) {
		this.statusDate = statusDate;
	}

	public String getWeeklyOff() {
		return weeklyOff;
	}

	public void setWeeklyOff(String weeklyOff) {
		if(weeklyOff!=null)
		{
			this.weeklyOff = weeklyOff;	
		}
		else
		{
			this.weeklyOff = "off";	
		}
		
	}

	public long getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(long scheduleId) {
		this.scheduleId = scheduleId;
	}

 
	 

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public ArrayList<ScheduleAlterDto > getAlterList() {
		return alterList;
	}

	public void setAlterList(ArrayList<ScheduleAlterDto > alterList) {
		this.alterList = alterList;
	}

	public ArrayList<LogTimeDto> getLoginTimeDto() {
		return loginTimeDto;
	}

	public void setLoginTimeDto(ArrayList<LogTimeDto> loginTimeDto) {
		this.loginTimeDto = loginTimeDto;
	}

	public ArrayList<LogTimeDto> getLogoutTimeDto() {
		return logoutTimeDto;
	}

	public void setLogoutTimeDto(ArrayList<LogTimeDto> logoutTimeDto) {
		this.logoutTimeDto = logoutTimeDto;
	}

	public String getSubscriptionToDate() {
		return subscriptionToDate;
	}

	public void setSubscriptionToDate(String subscriptionToDate) {
		this.subscriptionToDate = subscriptionToDate;
	}

}
