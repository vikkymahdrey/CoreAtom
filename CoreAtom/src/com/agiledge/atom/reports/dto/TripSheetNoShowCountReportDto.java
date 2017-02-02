package com.agiledge.atom.reports.dto;

public class TripSheetNoShowCountReportDto {

	private String month;
	private String monthLabel;
	public String getMonthLabel() {
		return monthLabel;
	}
	public void setMonthLabel(String monthLabel) {
		this.monthLabel = monthLabel;
	}
	private String year;
	public String getGroupById() {
		return groupById;
	}
	public void setGroupById(String groupById) {
		this.groupById = groupById;
	}
	private int noShowCount;
	private String site;
	private String employeeId;
	private String employeeName;
	private String project;
	private String spoc;
	private String manager;
	private String shiftTime;
	private String tripDate;
	private String groupBy;
	private String groupByLabel;
	private String groupById;
	private String scheduledBy;
	private String tripLog;
	private String reason;
	
	
	public String getReason() {
		return reason;
	}
	public void setReason(String reason) {
		this.reason = reason;
	}
	public String getTripLog() {
		return tripLog;
	}
	public void setTripLog(String tripLog) {
		this.tripLog = tripLog;
	}
	public String getScheduledBy() {
		return scheduledBy;
	}
	public void setScheduledBy(String scheduledBy) {
		this.scheduledBy = scheduledBy;
	}
	public String getGroupBy() {
		return groupBy;
	}
	public void setGroupBy(String groupBy) {
		this.groupBy = groupBy;
	}
	public String getGroupByLabel() {
		return groupByLabel;
	}
	public void setGroupByLabel(String groupByLabel) {
		this.groupByLabel = groupByLabel;
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
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getSpoc() {
		return spoc;
	}
	public void setSpoc(String spoc) {
		this.spoc = spoc;
	}
	public String getManager() {
		return manager;
	}
	public void setManager(String manager) {
		this.manager = manager;
	}
	public String getShiftTime() {
		return shiftTime;
	}
	public void setShiftTime(String shiftTime) {
		this.shiftTime = shiftTime;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public int getNoShowCount() {
		return noShowCount;
	}
	public void setNoShowCount(int noShowCount) {
		this.noShowCount = noShowCount;
	}
	public String getTripDate() {
		return tripDate;
	}
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	
}
