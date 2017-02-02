package com.agiledge.atom.reports.dto;

import java.util.Date;

public class TripBucketDto {

private String siteId;
 public String getSiteId() {
	return siteId;
}
public void setSiteId(String siteId) {
	this.siteId = siteId;
}
private Date tripDate;
 private String tripTime;
 private String tripLog;
 private int openCount;
 private int rtcCount;
 private int stcaCount;
 private int rtmCount;
 private int stmaCount;
 private int atmCount;
 
public Date getTripDate() {
	return tripDate;
}
public void setTripDate(Date tripDate) {
	this.tripDate = tripDate;
}
public String getTripTime() {
	return tripTime;
}
public void setTripTime(String tripTime) {
	this.tripTime = tripTime;
}
public String getTripLog() {
	return tripLog;
}
public void setTripLog(String tripLog) {
	this.tripLog = tripLog;
}
public int getOpenCount() {
	return openCount;
}
public void setOpenCount(int openCount) {
	this.openCount = openCount;
}
public int getRtcCount() {
	return rtcCount;
}
public void setRtcCount(int rtcCount) {
	this.rtcCount = rtcCount;
}
public int getStcaCount() {
	return stcaCount;
}
public void setStcaCount(int stcaCount) {
	this.stcaCount = stcaCount;
}
public int getRtmCount() {
	return rtmCount;
}
public void setRtmCount(int rtmCount) {
	this.rtmCount = rtmCount;
}
public int getStmaCount() {
	return stmaCount;
}
public void setStmaCount(int stmaCount) {
	this.stmaCount = stmaCount;
}
public int getAtmCount() {
	return atmCount;
}
public void setAtmCount(int atmCount) {
	this.atmCount = atmCount;
}
}
