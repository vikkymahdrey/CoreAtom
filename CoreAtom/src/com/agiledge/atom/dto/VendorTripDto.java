package com.agiledge.atom.dto;

import java.util.Arrays;

 

public class VendorTripDto {

	private String vendor;
	private String[] selectedtrip;
	private String[] trip;
	String tripDate;
	 public String getTripDate() {
		return tripDate;
	}
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getTripMode() {
		return tripMode;
	}
	public void setTripMode(String tripMode) {
		this.tripMode = tripMode;
	}
	public String getTripTime() {
		return tripTime;
	}
	public void setTripTime(String tripTime) {
		this.tripTime = tripTime;
	}
	String siteId;
	 String tripMode;
	 String tripTime;
 
	 
	public String[] getSelectedtrip() {
		return selectedtrip;
	}
	public void setSelectedtrip(String[] selectedtrip) {
		this.selectedtrip = selectedtrip;
	}
	public String[] getTrip() {
		return trip;
	}
	public void setTrip(String[] trip) {
		this.trip = trip;
	}
	public String getVendor() {
		return vendor;
	}
	public void setVendor(String vendor) {
		this.vendor = vendor;
	}
	 
}
