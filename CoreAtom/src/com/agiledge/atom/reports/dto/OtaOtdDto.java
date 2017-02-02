package com.agiledge.atom.reports.dto;

import java.util.Date;

public class OtaOtdDto {

	Date trip_date;
	String tripId;
	String shift;
	String cabNo;
	String cabType;
	String plannedTime;
	String actualTime;
	String onTimeStatus;
	String site;
	String tripCode;
	int showCount;
	int noShowCount;
	
	public int getShowCount() {
		return showCount;
	}
	public void setShowCount(int showCount) {
		this.showCount = showCount;
	}
	public int getNoShowCount() {
		return noShowCount;
	}
	public void setNoShowCount(int noShowCount) {
		this.noShowCount = noShowCount;
	}
	public String getTripCode() {
		return tripCode;
	}
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	public Date getTrip_date() {
		return trip_date;
	}
	public void setTrip_date(Date trip_date) {
		this.trip_date = trip_date;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getShift() {
		return shift;
	}
	public void setShift(String shift) {
		this.shift = shift;
	}
	public String getCabNo() {
		return cabNo;
	}
	public void setCabNo(String cabNo) {
		this.cabNo = cabNo;
	}
	 
	public String getCabType() {
		return cabType;
	}
	public void setCabType(String cabType) {
		this.cabType = cabType;
	}
	public String getPlannedTime() {
		return plannedTime;
	}
	public void setPlannedTime(String plannedTime) {
		this.plannedTime = plannedTime;
	}
	public String getActualTime() {
		return actualTime;
	}
	public void setActualTime(String actualTime) {
		this.actualTime = actualTime;
	}
	public String getOnTimeStatus() {
		return onTimeStatus;
	}
	public void setOnTimeStatus(String onTimeStatus) {
		this.onTimeStatus = onTimeStatus;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	
	
}
