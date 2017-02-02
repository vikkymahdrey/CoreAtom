/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;

/**
 * 
 * @author muhammad
 */
public class TripDetailsDto extends TripSummaryDto implements Serializable {
	private VehicleTypeDto vehicleType;
	private String id;	
	private String trip_code;
	private String siteId;
	private String trip_date;
	private String trip_time;
	private String trip_log;
	private String routeId;
	private String vehicle_type;
	private int vehicle_typeInt;
	private String vehicle;
	private String status;
	private TripDetailsChildDto tripDetailsChildDtoObj;
	private ArrayList<TripDetailsChildDto> tripDetailsChildDtoList;
	private ArrayList<TripCommentDto> comments;
	private String isSecurity;
	private String actualLogTime;
	private String approvalStatus;
	private boolean canTravel = false;
	private String vehicleNo;
	private String onTimeStatus;
	private String doneBy;
	private String comment;
	private String escort;
	private String escortNo;
	private String routingType;
	private String travelTime;
	private String distance;
	private String srcDestLandmark;
	private float distanceFromPrevioustrip;
	private String timeFromPrevioustrip;
	private String previousTripId;
	private int seat;
	private String latitude;
	private String longitude;
	private int ladyInCount;
	private int empInCount;
	private String driverName;
	private String driverContact;
	private String vendorName;
	private String vendorContact;
	private String vendorSupervisor;
	private String vendorSupervisorContact;
	private String escortclock;
	private String escortName;
	private String escortContact;
	private String startTime;
	private String stopTime;
	private int reachedLadyCount;
	private int reachedempCount;
	private PanicDto panicdto;
	private String panicAck;
	private String tripCount;
	private int empCount;
	private int secCount;

	private double escortRate;
	private String label;
	private String scheduleId;
	private double tripRate;
	private String vendorId;
	private Date tripDate;
	private boolean selected;

	private String getInStatus;
	private String tripBasedDistance;

	private String driver;
	private int sittingCapasity;
	private String tripType;
	private String startPlace;
	private String endPlace;
	private String vehicleTypeId;
	private String fromKm;
	private String toKm;
	private int totalEmployees;
	private int showCount;
	private int noShowCount;
	private String driverId;
	private String vehicleId;
	private String driverPassword;
	private String trackingStatus;
	private long travelTimeInDate;
	private String escortId;
	private String escortPassword;
	private int projectEmpCount;
	private int notProjectEmpCount;
	private int projectFemaleCountInSecurityTrip;
	private int notProjectFemaleCountInSecurityTrip;
	private String tripBaseCost;
	private String escortCost;
	private String projectEmpCost;
	private String projectEscortCost;
	private double distanceDouble;
	private boolean forceStop;
	private String distanceRange;
	private String expectedArrivalTime;
	private double distanceCovered;
	private String downloadStatus;
	private long timeElapsed;
	public TripDetailsDto()
	{}

	@Override
	public String toString() {
		return "TripDetailsDto [escortRate=" + escortRate + ", label=" + label
				+ ", scheduleId=" + scheduleId + ", tripRate=" + tripRate
				+ ", vendorId=" + vendorId + ", tripDate=" + tripDate
				+ ", getInStatus=" + getInStatus + ", tripBasedDistance="
				+ tripBasedDistance + ", driver=" + driver
				+ ", sittingCapasity=" + sittingCapasity + ", tripType="
				+ tripType + ", startPlace=" + startPlace + ", endPlace="
				+ endPlace + ", vehicleTypeId=" + vehicleTypeId + ", fromKm="
				+ fromKm + ", toKm=" + toKm + ", totalEmployees="
				+ totalEmployees + ", showCount=" + showCount
				+ ", noShowCount=" + noShowCount + ", distanceDouble="
				+ distanceDouble + ", forceStop=" + forceStop + ", driverId="
				+ driverId + ", vehicleId=" + vehicleId + ", driverPassword="
				+ driverPassword + ", trackingStatus=" + trackingStatus
				+ ", travelTimeInDate=" + travelTimeInDate + ", escortId="
				+ escortId + ", escortPassword=" + escortPassword
				+ ", distanceRange=" + distanceRange + ", projectEmpCount="
				+ projectEmpCount + ", notProjectEmpCount="
				+ notProjectEmpCount + ", projectFemaleCountInSecurityTrip="
				+ projectFemaleCountInSecurityTrip
				+ ", notProjectFemaleCountInSecurityTrip="
				+ notProjectFemaleCountInSecurityTrip + ", tripBaseCost="
				+ tripBaseCost + ", escortCost=" + escortCost
				+ ", projectEmpCost=" + projectEmpCost + ", projectEscortCost="
				+ projectEscortCost + ", expectedArrivalTime="
				+ expectedArrivalTime + ", distanceCovered=" + distanceCovered
				+ ", downloadStatus=" + downloadStatus + ", timeElapsed="
				+ timeElapsed + ", id=" + id + ", trip_code=" + trip_code
				+ ", siteId=" + siteId + ", trip_date=" + trip_date
				+ ", trip_time=" + trip_time + ", trip_log=" + trip_log
				+ ", routeId=" + routeId + ", vehicle_type=" + vehicle_type
				+ ", vehicle=" + vehicle + ", status=" + status
				+ ", tripDetailsChildDtoObj=" + tripDetailsChildDtoObj
				+ ", tripDetailsChildDtoList=" + tripDetailsChildDtoList
				+ ", comments=" + comments + ", isSecurity=" + isSecurity
				+ ", actualLogTime=" + actualLogTime + ", approvalStatus="
				+ approvalStatus + ", canTravel=" + canTravel + ", vehicleNo="
				+ vehicleNo + ", onTimeStatus=" + onTimeStatus + ", doneBy="
				+ doneBy + ", comment=" + comment + ", escort=" + escort
				+ ", escortNo=" + escortNo + ", routingType=" + routingType
				+ ", travelTime=" + travelTime + ", distance=" + distance
				+ ", srcDestLandmark=" + srcDestLandmark
				+ ", distanceFromPrevioustrip=" + distanceFromPrevioustrip
				+ ", timeFromPrevioustrip=" + timeFromPrevioustrip
				+ ", previousTripId=" + previousTripId + ", seat=" + seat
				+ ", latitude=" + latitude + ", longitude=" + longitude
				+ ", ladyInCount=" + ladyInCount + ", empInCount=" + empInCount
				+ ", driverName=" + driverName + ", driverContact="
				+ driverContact + ", vendorName=" + vendorName
				+ ", vendorContact=" + vendorContact + ", vendorSupervisor="
				+ vendorSupervisor + ", vendorSupervisorContact="
				+ vendorSupervisorContact + ", escortclock=" + escortclock
				+ ", escortName=" + escortName + ", escortContact="
				+ escortContact + ", startTime=" + startTime + ", stopTime="
				+ stopTime + ", reachedLadyCount=" + reachedLadyCount
				+ ", reachedempCount=" + reachedempCount + ", panicdto="
				+ panicdto + ", panicAck=" + panicAck + ", tripCount="
				+ tripCount + ", empCount=" + empCount + "]";
	}

	public int getTotalEmployees() {
		return totalEmployees;
	}

	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}

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

	public double getTripRate() {
		return tripRate;
	}

	public void setTripRate(double tripRate) {
		this.tripRate = tripRate;
	}

	public Date getTripDate() {
		return tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public String getTripBasedDistance() {
		return tripBasedDistance;
	}

	public void setTripBasedDistance(String tripBasedDistance) {
		this.tripBasedDistance = tripBasedDistance;
	}

	public String getDriver() {
		return driver;
	}

	public void setDriver(String driver) {
		this.driver = driver;
	}

	public int getSittingCapasity() {
		return sittingCapasity;
	}

	public void setSittingCapasity(int sittingCapasity) {
		this.sittingCapasity = sittingCapasity;
	}

	public String getTripType() {
		return tripType;
	}

	public void setTripType(String tripType) {
		this.tripType = tripType;
	}

	public String getStartPlace() {
		return startPlace;
	}

	public void setStartPlace(String startPlace) {
		this.startPlace = startPlace;
	}

	public String getEndPlace() {
		return endPlace;
	}

	public void setEndPlace(String endPlace) {
		this.endPlace = endPlace;
	}

	public String getVehicleTypeId() {
		return vehicleTypeId;
	}

	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getFromKm() {
		return fromKm;
	}

	public void setFromKm(String fromKm) {
		this.fromKm = fromKm;
	}

	public String getToKm() {
		return toKm;
	}

	public void setToKm(String toKm) {
		this.toKm = toKm;
	}

	public String getDistanceRange() {
		return distanceRange;
	}

	public void setDistanceRange(String distanceRange) {
		this.distanceRange = distanceRange;
	}

	public int getProjectEmpCount() {
		return projectEmpCount;
	}

	public void setProjectEmpCount(int projectEmpCount) {
		this.projectEmpCount = projectEmpCount;
	}

	public int getNotProjectEmpCount() {
		return notProjectEmpCount;
	}

	public void setNotProjectEmpCount(int notProjectEmpCount) {
		this.notProjectEmpCount = notProjectEmpCount;
	}

	public int getProjectFemaleCountInSecurityTrip() {
		return projectFemaleCountInSecurityTrip;
	}

	public void setProjectFemaleCountInSecurityTrip(
			int projectFemaleCountInSecurityTrip) {
		this.projectFemaleCountInSecurityTrip = projectFemaleCountInSecurityTrip;
	}

	public int getNotProjectFemaleCountInSecurityTrip() {
		return notProjectFemaleCountInSecurityTrip;
	}

	public void setNotProjectFemaleCountInSecurityTrip(
			int notProjectFemaleCountInSecurityTrip) {
		this.notProjectFemaleCountInSecurityTrip = notProjectFemaleCountInSecurityTrip;
	}

	public String getTripBaseCost() {
		return tripBaseCost;
	}

	public void setTripBaseCost(String tripBaseCost) {
		this.tripBaseCost = tripBaseCost;
	}

	public String getEscortCost() {
		return escortCost;
	}

	public void setEscortCost(String escortCost) {
		this.escortCost = escortCost;
	}

	public String getProjectEmpCost() {
		return projectEmpCost;
	}

	public void setProjectEmpCost(String projectEmpCost) {
		this.projectEmpCost = projectEmpCost;
	}

	public String getProjectEscortCost() {
		return projectEscortCost;
	}

	public void setProjectEscortCost(String projectEscortCost) {
		this.projectEscortCost = projectEscortCost;
	}

	public String getEscortId() {
		return escortId;
	}

	public void setEscortId(String escortId) {
		this.escortId = escortId;
	}

	public String getEscortPassword() {
		return escortPassword;
	}

	public void setEscortPassword(String escortPassword) {
		this.escortPassword = escortPassword;
	}

	public String getDriverId() {
		return driverId;
	}

	public void setDriverId(String driverId) {
		this.driverId = driverId;
	}

	public String getGetInStatus() {
		return getInStatus;
	}

	public void setGetInStatus(String getInStatus) {
		this.getInStatus = getInStatus;
	}

	public double getDistanceCovered() {
		return distanceCovered;
	}

	public void setDistanceCovered(double distanceCovered) {
		this.distanceCovered = distanceCovered;
	}

	public long getTimeElapsed() {
		return timeElapsed;
	}

	public void setTimeElapsed(long timeElapsed) {
		this.timeElapsed = timeElapsed;
	}

	public PanicDto getPanicdto() {
		return panicdto;
	}

	public void setPanicdto(PanicDto panicdto) {
		this.panicdto = panicdto;
	}

	public String getEscortclock() {
		return escortclock;
	}

	public void setEscortclock(String escortclock) {
		this.escortclock = escortclock;
	}

	public String getEscortName() {
		return escortName;
	}

	public void setEscortName(String escortName) {
		this.escortName = escortName;
	}

	public String getEscortContact() {
		return escortContact;
	}

	public void setEscortContact(String escortContact) {
		this.escortContact = escortContact;
	}

	public String getDriverName() {
		return driverName;
	}

	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}

	public String getDriverContact() {
		return driverContact;
	}

	public void setDriverContact(String driverContact) {
		this.driverContact = driverContact;
	}

	public String getVendorName() {
		return vendorName;
	}

	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	public String getVendorContact() {
		return vendorContact;
	}

	public void setVendorContact(String vendorContact) {
		this.vendorContact = vendorContact;
	}

	public String getVendorSupervisor() {
		return vendorSupervisor;
	}

	public void setVendorSupervisor(String vendorSupervisor) {
		this.vendorSupervisor = vendorSupervisor;
	}

	public String getVendorSupervisorContact() {
		return vendorSupervisorContact;
	}

	public void setVendorSupervisorContact(String vendorSupervisorContact) {
		this.vendorSupervisorContact = vendorSupervisorContact;
	}

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getEscort() {
		return escort;
	}

	public void setEscort(String escort) {
		this.escort = escort;
	}

	public String getEscortNo() {
		return escortNo;
	}

	public void setEscortNo(String escortNo) {
		this.escortNo = escortNo;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getOnTimeStatus() {
		return onTimeStatus;
	}

	public void setOnTimeStatus(String onTimeStatus) {
		this.onTimeStatus = onTimeStatus;
	}

	public String getIsSecurity() {
		return isSecurity;
	}

	public void setIsSecurity(String isSecurity) {
		this.isSecurity = isSecurity;
	}

	public ArrayList<TripDetailsChildDto> getTripDetailsChildDtoList() {
		return tripDetailsChildDtoList;
	}

	public void setTripDetailsChildDtoList(
			ArrayList<TripDetailsChildDto> tripDetailsChildDtoList) {
		this.tripDetailsChildDtoList = tripDetailsChildDtoList;
	}

	public TripDetailsChildDto getTripDetailsChildDtoObj() {
		return tripDetailsChildDtoObj;
	}

	public void setTripDetailsChildDtoObj(
			TripDetailsChildDto tripDetailsChildDtoObj) {
		this.tripDetailsChildDtoObj = tripDetailsChildDtoObj;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRouteId() {
		return routeId;
	}

	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getTrip_code() {
		return trip_code;
	}

	public void setTrip_code(String trip_code) {
		this.trip_code = trip_code;
	}

	public String getTrip_date() {
		return trip_date;
	}

	public void setTrip_date(String trip_date) {
		this.trip_date = trip_date;
	}

	public String getTrip_log() {
		return trip_log;
	}

	public void setTrip_log(String trip_log) {
		this.trip_log = trip_log;
	}

	public String getTrip_time() {
		return trip_time;
	}

	public void setTrip_time(String trip_time) {
		this.trip_time = trip_time;
	}

	public String getVehicle_type() {
		return vehicle_type;
	}

	public void setVehicle_type(String vehicle_type) {
		this.vehicle_type = vehicle_type;
	}

	public String getActualLogTime() {
		return actualLogTime;
	}

	public void setActualLogTime(String actualLogTime) {
		this.actualLogTime = actualLogTime;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public boolean isCanTravel() {
		return canTravel;
	}

	public void setCanTravel(boolean canTravel) {
		this.canTravel = canTravel;
	}

	public String getVehicleNo() {
		return vehicleNo;
	}

	public void setVehicleNo(String vehicleNo) {
		this.vehicleNo = vehicleNo;
	}

	public String getTravelTime() {
		return travelTime;
	}

	public void setTravelTime(String travelTime) {
		this.travelTime = travelTime;
	}

	public String getDistance() {
		if(distance==null)
			return "0";
		return distance;
	}

	public void setDistance(String distance) {
		this.distance = distance;
	}

	public String getSrcDestLandmark() {
		return srcDestLandmark;
	}

	public void setSrcDestLandmark(String srcDestLandmark) {
		this.srcDestLandmark = srcDestLandmark;
	}

	public float getDistanceFromPrevioustrip() {
		return distanceFromPrevioustrip;
	}

	public void setDistanceFromPrevioustrip(float distanceFromPrevioustrip) {
		this.distanceFromPrevioustrip = distanceFromPrevioustrip;
	}

	public String getTimeFromPrevioustrip() {
		return timeFromPrevioustrip;
	}

	public void setTimeFromPrevioustrip(String timeFromPrevioustrip) {
		this.timeFromPrevioustrip = timeFromPrevioustrip;
	}

	public String getPreviousTripId() {
		return previousTripId;
	}

	public void setPreviousTripId(String previousTripId) {
		this.previousTripId = previousTripId;
	}

	public int getSeat() {
		return seat;
	}

	public void setSeat(int seat) {
		this.seat = seat;
	}

	public String getVehicle() {
		return vehicle;
	}

	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getRoutingType() {
		return routingType;
	}

	public void setRoutingType(String routingType) {
		this.routingType = routingType;
	}

	public ArrayList<TripCommentDto> getComments() {
		return comments;
	}

	public void setComments(ArrayList<TripCommentDto> comments) {
		this.comments = comments;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public int getLadyInCount() {
		return ladyInCount;
	}

	public void setLadyInCount(int ladyInCount) {
		this.ladyInCount = ladyInCount;
	}

	public int getEmpInCount() {
		return empInCount;
	}

	public void setEmpInCount(int empInCount) {
		this.empInCount = empInCount;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getStopTime() {
		return stopTime;
	}

	public void setStopTime(String stopTime) {
		this.stopTime = stopTime;
	}

	public int getReachedLadyCount() {
		return reachedLadyCount;
	}

	public void setReachedLadyCount(int reachedLadyCount) {
		this.reachedLadyCount = reachedLadyCount;
	}

	public int getReachedempCount() {
		return reachedempCount;
	}

	public void setReachedempCount(int reachedempCount) {
		this.reachedempCount = reachedempCount;
	}

	public String getPanicAck() {
		return panicAck;
	}

	public void setPanicAck(String panicAck) {
		this.panicAck = panicAck;
	}

	public String getDownloadStatus() {
		return downloadStatus;
	}

	public void setDownloadStatus(String downloadStatus) {
		this.downloadStatus = downloadStatus;
	}

	public String getTripCount() {
		return tripCount;
	}

	public void setTripCount(String tripCount) {
		this.tripCount = tripCount;
	}

	public String getDriverPassword() {
		return driverPassword;
	}

	public void setDriverPassword(String driverPassword) {
		this.driverPassword = driverPassword;
	}

	public String getVehicleId() {
		return vehicleId;
	}

	public void setVehicleId(String vehicleId) {
		this.vehicleId = vehicleId;
	}

	public String getExpectedArrivalTime() {
		return expectedArrivalTime;
	}

	public void setExpectedArrivalTime(String expectedArrivalTime) {
		this.expectedArrivalTime = expectedArrivalTime;
	}

	public String getTrackingStatus() {
		return trackingStatus;
	}

	public void setTrackingStatus(String trackingStatus) {
		this.trackingStatus = trackingStatus;
	}

	public boolean isForceStop() {
		return forceStop;
	}

	public void setForceStop(boolean forceStop) {
		this.forceStop = forceStop;
	}

	public long getTravelTimeInDate() {
		return travelTimeInDate;
	}

	public void setTravelTimeInDate(long tripTimeInDate) {
		this.travelTimeInDate = tripTimeInDate;
	}

	public double getDistanceDouble() {
		return distanceDouble;
	}

	public void setDistanceDouble(double distanceDouble) {
		this.distanceDouble = distanceDouble;
	}

	public int getEmpCount() {
		return empCount;
	}

	public void setEmpCount(int empCount) {
		this.empCount = empCount;
	}

	public double getEscortRate() {
		return escortRate;
	}

	public void setEscortRate(double escortRate) {
		this.escortRate = escortRate;
	}

	public String getScheduleId() {
		return scheduleId;
	}

	public void setScheduleId(String scheduleId) {
		this.scheduleId = scheduleId;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public boolean isSelected() {
		return selected;
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public int getSecCount() {
		return secCount;
	}

	public void setSecCount(int secCount) {
		this.secCount = secCount;
	}

	public VehicleTypeDto getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(VehicleTypeDto vehicleType) {
		this.vehicleType = vehicleType;
	}

	public int getVehicle_typeInt() {
		return vehicle_typeInt;
	}




}
