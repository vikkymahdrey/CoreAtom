package com.agiledge.atom.reports.dto;

public class OnTimeTripCountDto {
	int monthNo;
	public int getMonthNo() {
		return monthNo;
	}
	public void setMonthNo(int monthNo) {
		this.monthNo = monthNo;
	}
	String month;
	String site;
	String logType;
	String vehicleType;
	int totalCount;
	int onTimeCount;
	int offTimeCount;
	public String getLogType() {
		return logType;
	}
	public void setLogType(String logType) {
		this.logType = logType;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public int getOnTimeCount() {
		return onTimeCount;
	}
	public void setOnTimeCount(int onTimeCount) {
		this.onTimeCount = onTimeCount;
	}
	public int getOffTimeCount() {
		return offTimeCount;
	}
	public void setOffTimeCount(int offTimeCount) {
		this.offTimeCount = offTimeCount;
	}

}
