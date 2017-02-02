/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

/**
 * 
 * @author muhammad
 */
public class TripDetailsChildDto {
	@Override
	public String toString() {
		return "TripDetailsChildDto [getInStatus=" + getInStatus + ", keyPin="
				+ keyPin + ", transportType=" + transportType + ", tripId="
				+ tripId + ", personnelNo=" + personnelNo + ", employeeId="
				+ employeeId + ", routedOrder=" + routedOrder + ", landmarkId="
				+ landmarkId + ", status=" + status + ", gender=" + gender
				+ ", area=" + area + ", place=" + place + ", landmark="
				+ landmark + ", employeeName=" + employeeName
				+ ", contactNumber=" + contactNumber + ", approvedEmployee="
				+ approvedEmployee + ", showStatus=" + showStatus + ", doneBy="
				+ doneBy + ", reason=" + reason + ", time=" + time
				+ ", distance=" + distance + ", project=" + project
				+ ", scheduleId=" + scheduleId + ", pickUpTime=" + pickUpTime
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", inTime=" + inTime + ", outTime=" + outTime
				+ ", isCorrectPos=" + isCorrectPos + ", routeId=" + routeId
				+ ", inOutTime=" + inOutTime + "]";
	}
	private String getInStatus;
	
	public String getGetInStatus() {
		return getInStatus;
	}

	public void setGetInStatus(String getInStatus) {
		this.getInStatus = getInStatus;
	}
	private String keyPin;
	private String transportType;
	private String tripId;
	private String personnelNo;
	private String employeeId;
	private String routedOrder;
	private String landmarkId;
	private String status;
	private String gender;
	private String area;
	private String place;
	private String landmark;
	private String employeeName;
	private String contactNumber;
	private String approvedEmployee;
	private String showStatus;
	private String doneBy;
	private String reason;
	private String time;
	private float distance;
	private String project;
	private String scheduleId;
	private String pickUpTime;
	private String latitude;
	private String longitude;
	private String inTime;
	private String outTime;	
	private String isCorrectPos;
	private String routeId;
	private String inOutTime;

	public String getInTime() {
		return inTime;
	}

	public void setInTime(String inTime) {
		this.inTime = inTime;
	}

	public String getOutTime() {
		return outTime;
	}

	public void setOutTime(String outTime) {
		this.outTime = outTime;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getPersonnelNo() {
		return personnelNo;
	}

	public void setPersonnelNo(String personnelNo) {
		this.personnelNo = personnelNo;
	}
	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getReason() {
		return reason;
	}

	public void setReason(String reason) {
		this.reason = reason;
	}

	public String getShowStatus() {
		return showStatus;
	}

	public void setShowStatus(String showStatus) {
		this.showStatus = showStatus;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getArea() {
		return area;
	}

	public void setArea(String area) {		
		if(area==null||area.equals("")||area.equals("null"))
		{
			area="NA";	
		}
		this.area = area;
		
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		
		if(landmark==null||landmark.equals("")||landmark.equals("null"))
		{
			landmark="NA";			
		}
		this.landmark = landmark;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {		
		if(place==null||place.equals("")||place.equals("null"))
		{
			place="NA";			
		}
		this.place = place;	
	}

	public String getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

	public String getLandmarkId() {
		return landmarkId;
	}

	public void setLandmarkId(String landmarkId) {
		this.landmarkId = landmarkId;
	}

	public String getRoutedOrder() {
		return routedOrder;
	}

	public void setRoutedOrder(String routedOrder) {
		this.routedOrder = routedOrder;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTripId() {
		return tripId;
	}

	public void setTripId(String tripId) {
		this.tripId = tripId;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getApprovedEmployee() {
		return approvedEmployee;
	}

	public void setApprovedEmployee(String approvedEmployee) {
		this.approvedEmployee = approvedEmployee;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}
	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}
	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getIsCorrectPos() {
		return isCorrectPos;
	}

	public void setIsCorrectPos(String isCorrectPos) {
		this.isCorrectPos = isCorrectPos;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getInOutTime() {
		return inOutTime;
	}

	public void setInOutTime(String inOutTime) {
		this.inOutTime = inOutTime;
	}

	public String getKeyPin() {
		return keyPin;
	}

	public void setKeyPin(String keyPin) {
		this.keyPin = keyPin;
	}

	

}
