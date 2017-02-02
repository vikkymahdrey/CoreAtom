/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

/**
 * 
 * @author muhammad
 */
public class RoutingDto {
	private String date;
	private String time;
	private String travelMode;
	private String employeeId;
	private String employeegender;
	private String employeeLandmark;
	private String subscriptionId;
	private int siteId;
	private int siteLandmark;
	private float distance;
	private float travellingTime;
	private String project;
	private String timInDestionation;
	private String DestinationLandmark;
	private String doneBy;
	private String transportType;
	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getSubscriptionId() {
		return subscriptionId;
	}

	public void setSubscriptionId(String subscriptionId) {
		this.subscriptionId = subscriptionId;
	}

	private String scheduleId;

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeLandmark() {
		return employeeLandmark;
	}

	public void setEmployeeLandmark(String employeeLandmark) {
		this.employeeLandmark = employeeLandmark;
	}

	public String getEmployeegender() {
		return employeegender;
	}

	public void setEmployeegender(String employeegender) {
		this.employeegender = employeegender;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public String getTravelMode() {
		return travelMode;
	}

	public void setTravelMode(String travelMode) {
		this.travelMode = travelMode;
	}

	public int getSiteId() {
		return siteId;
	}

	public void setSiteId(int siteId) {
		this.siteId = siteId;
	}

	public int getSiteLandmark() {
		return siteLandmark;
	}

	public void setSiteLandmark(int siteLandmark) {
		this.siteLandmark = siteLandmark;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public float getTravellingTime() {
		return travellingTime;
	}

	public void setTravellingTime(float travellingTime) {
		this.travellingTime = travellingTime;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getTimInDestionation() {
		return timInDestionation;
	}

	public void setTimInDestionation(String timInDestionation) {
		this.timInDestionation = timInDestionation;
	}

	public String getDestinationLandmark() {
		return DestinationLandmark;
	}

	public void setDestinationLandmark(String destinationLandmark) {
		DestinationLandmark = destinationLandmark;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

}
