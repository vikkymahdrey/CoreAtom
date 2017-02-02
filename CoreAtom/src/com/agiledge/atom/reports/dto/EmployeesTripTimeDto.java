package com.agiledge.atom.reports.dto;


 

public class EmployeesTripTimeDto  {
	private String tripLog;
	private String averageTime;
	private String PersonnelNo;
	private String displayName;
	public String getPersonnelNo() {
		return PersonnelNo;
	}
	public void setPersonnelNo(String personnelNo) {
		PersonnelNo = personnelNo;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}
	public String getTripLog() {
		return tripLog;
	}
	public void setTripLog(String tripLog) {
		this.tripLog = tripLog;
	}
	public String getAverageTime() {
		return averageTime;
	}
	public void setAverageTime(String averageTime) {
		this.averageTime = averageTime;
	}
	
}
