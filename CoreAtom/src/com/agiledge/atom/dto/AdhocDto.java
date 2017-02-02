package com.agiledge.atom.dto;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.agiledge.atom.usermanagement.dto.UserManagementDto;

public class AdhocDto {


private long id;

private String adhocType;
private String adhocTypeString;
private String approval;
private String[] approvers;
private String requestCutoff;
private String cancelCutoff;
private String requester;
private String[] requesters;
private String cancelMode;
private String existingCancelTime;
private String maxRequest;
private String maxPendingRequest;
private ArrayList<UserManagementDto> requesterRoles;
private ArrayList<UserManagementDto> approverRoles;
private String TravelDate;
private String startTime;
private String endTime;
private String orgination;
private String destination;
private String reason;
private String comment;
private String bookedDate;
private String status;
private String approvedBy;
private String approvedByName;
private String bookedBy;
private String bookedFor;
private String pickupDrop;
private String shiftTime;
private String employeeName;
private String employeeCode;
private String siteId;
private String projectUnit;
private String bookedDayBookingCount;
private String pendingBookedCount;
private String bookingId;
private String vehicleType;
private String tripCode;
private String bookedByname;
public String getBookedByname() {
	return bookedByname;
}
public void setBookedByname(String bookedByname) {
	this.bookedByname = bookedByname;
}
@Override
public String toString() {
	return "AdhocDto [id=" + id + ", adhocType=" + adhocType
			+ ", adhocTypeString=" + adhocTypeString + ", approval=" + approval
			+ ", approvers=" + Arrays.toString(approvers) + ", requestCutoff="
			+ requestCutoff + ", cancelCutoff=" + cancelCutoff + ", requester="
			+ requester + ", requesters=" + Arrays.toString(requesters)
			+ ", cancelMode=" + cancelMode + ", existingCancelTime="
			+ existingCancelTime + ", maxRequest=" + maxRequest
			+ ", maxPendingRequest=" + maxPendingRequest + ", requesterRoles="
			+ requesterRoles + ", approverRoles=" + approverRoles
			+ ", TravelDate=" + TravelDate + ", startTime=" + startTime
			+ ", endTime=" + endTime + ", orgination=" + orgination
			+ ", destination=" + destination + ", reason=" + reason
			+ ", comment=" + comment + ", bookedDate=" + bookedDate
			+ ", status=" + status + ", approvedBy=" + approvedBy
			+ ", approvedByName=" + approvedByName + ", bookedBy=" + bookedBy
			+ ", bookedFor=" + bookedFor + ", pickupDrop=" + pickupDrop
			+ ", shiftTime=" + shiftTime + ", employeeName=" + employeeName
			+ ", employeeCode=" + employeeCode + ", siteId=" + siteId
			+ ", projectUnit=" + projectUnit + ", bookedDayBookingCount="
			+ bookedDayBookingCount + ", pendingBookedCount="
			+ pendingBookedCount + ", bookingId=" + bookingId
			+ ", vehicleType=" + vehicleType + ", tripCode=" + tripCode
			+ ", distance=" + distance + ", travelTime=" + travelTime
			+ ", employeeId=" + employeeId + ", escort=" + escort
			+ ", requesterRoleId=" + requesterRoleId + ", passengerList="
			+ passengerList + "]";
}
private String distance;
private String travelTime;
private String employeeId;
private String escort;
private String requesterRoleId;
private List<EmployeeDto> passengerList = new ArrayList<>();
public String getApproval() {
	return approval;
}
public void setApproval(String approval) {
	this.approval = approval;
}
public String[] getApprovers() {
	return approvers;
}
public void setApprovers(String[] approvers) {
	this.approvers = approvers;
}
public String getRequestCutoff() {
	return requestCutoff;
}
public void setRequestCutoff(String requestCutoff) {
	this.requestCutoff = requestCutoff;
}
public String getCancelCutoff() {
	return cancelCutoff;
}
public void setCancelCutoff(String cancelCutoff) {
	this.cancelCutoff = cancelCutoff;
}

public String getCancelMode() {
	return cancelMode;
}
public void setCancelMode(String cancelMode) {
	this.cancelMode = cancelMode;
}
public String getMaxRequest() {
	return maxRequest;
}
public void setMaxRequest(String maxrequest) {
	this.maxRequest = maxrequest;
}
public String getAdhocType() {
	return adhocType;
}
public long getId() {
	return id;
}
public void setId(long id) {
	this.id = id;
}
public void setAdhocType(String adhocType) {
	this.adhocType = adhocType;
}
 public String getRequester() {
	return requester;
}
public void setRequester(String requester) {
	this.requester = requester;
}
public String getMaxPendingRequest() {
	return maxPendingRequest;
}
public void setMaxPendingRequest(String maxPendingRequest) {
	this.maxPendingRequest = maxPendingRequest;
}
public String getAdhocTypeString() {
	return adhocTypeString;
}
public void setAdhocTypeString(String adhocTypeString) {
	this.adhocTypeString = adhocTypeString;
}
public String[] getRequesters() {
	return requesters;
}
public void setRequesters(String[] requesters) {
	this.requesters = requesters;
}
public ArrayList<UserManagementDto> getRequesterRoles() {
	return requesterRoles;
}
public void setRequesterRoles(ArrayList<UserManagementDto> requesterRoles) {
	this.requesterRoles = requesterRoles;
}
public ArrayList<UserManagementDto> getApproverRoles() {
	return approverRoles;
}
public void setApproverRoles(ArrayList<UserManagementDto> approverRoles) {
	this.approverRoles = approverRoles;
}
public String getTravelDate() {
	return TravelDate;
}
public void setTravelDate(String travelDate) {
	TravelDate = travelDate;
}
public String getStartTime() {
	return startTime;
}
public void setStartTime(String startTime) {
	this.startTime = startTime;
}
public String getEndTime() {
	return endTime;
}
public void setEndTime(String endTime) {
	this.endTime = endTime;
}
public String getOrgination() {
	return orgination;
}
public void setOrgination(String orgination) {
	this.orgination = orgination;
}
public String getDestination() {
	return destination;
}
public void setDestination(String destination) {
	this.destination = destination;
}
public String getReason() {
	return reason;
}
public void setReason(String reason) {
	this.reason = reason;
}
public String getComment() {
	return comment;
}
public void setComment(String comment) {
	this.comment = comment;
}
public String getBookedDate() {
	return bookedDate;
}
public void setBookedDate(String bookedDate) {
	this.bookedDate = bookedDate;
}
public String getApprovedBy() {
	return approvedBy;
}
public void setApprovedBy(String approvedBy) {
	this.approvedBy = approvedBy;
}
public String getStatus() {
	return status;
}
public void setStatus(String status) {
	this.status = status;
}
public String getApprovedByName() {
	return approvedByName;
}
public void setApprovedByName(String approvedByName) {
	this.approvedByName = approvedByName;
}
public String getBookedFor() {
	return bookedFor;
}
public void setBookedFor(String bookedFor) {
	this.bookedFor = bookedFor;
}
public String getBookedBy() {
	return bookedBy;
}
public void setBookedBy(String bookedBy) {
	this.bookedBy = bookedBy;
}
public String getExistingCancelTime() {
	return existingCancelTime;
}
public void setExistingCancelTime(String existingCancelTime) {
	this.existingCancelTime = existingCancelTime;
}
public String getPickupDrop() {
	return pickupDrop;
}
public void setPickupDrop(String pickupDrop) {
	this.pickupDrop = pickupDrop;
}
public String getShiftTime() {
	return shiftTime;
}
public void setShiftTime(String shiftTime) {
	this.shiftTime = shiftTime;
}
public String getEmployeeName() {
	return employeeName;
}
public void setEmployeeName(String employeeId) {
	this.employeeName = employeeId;
}
public String getEmployeeCode() {
	return employeeCode;
}
public void setEmployeeCode(String employeeCode) {
	this.employeeCode = employeeCode;
}
public String getSiteId() {
	return siteId;
}
public void setSiteId(String siteId) {
	this.siteId = siteId;
}
public String getProjectUnit() {
	return projectUnit;
}
public void setProjectUnit(String projectUnit) {
	this.projectUnit = projectUnit;
}
public String getBookedDayBookingCount() {
	return bookedDayBookingCount;
}
public void setBookedDayBookingCount(String bookedDayBookingCount) {
	this.bookedDayBookingCount = bookedDayBookingCount;
}
public String getPendingBookedCount() {
	return pendingBookedCount;
}
public void setPendingBookedCount(String pendingBookedCount) {
	this.pendingBookedCount = pendingBookedCount;
}
public String getBookingId() {
	return bookingId;
}
public void setBookingId(String bookingId) {
	this.bookingId = bookingId;
}
public String getVehicleType() {
	return vehicleType;
}
public void setVehicleType(String vehicleType) {
	this.vehicleType = vehicleType;
}
public String getTripCode() {
	return tripCode;
}
public void setTripCode(String tripCode) {
	this.tripCode = tripCode;
}
public String getDistance() {
	return distance;
}
public void setDistance(String distance) {
	this.distance = distance;
}
public String getTravelTime() {
	return travelTime;
}
public void setTravelTime(String travelTime) {
	this.travelTime = travelTime;
}
public String getEmployeeId() {
	return employeeId;
}
public void setEmployeeId(String employeeId) {
	this.employeeId = employeeId;
}
public String getEscort() {
	return escort;
}
public void setEscort(String escort) {
	this.escort = escort;
}
public String getRequesterRoleId() {
	return requesterRoleId;
}
public void setRequesterRoleId(String requesterRoleId) {
	this.requesterRoleId = requesterRoleId;
}
public List<EmployeeDto> getPassengerList() {
	return passengerList;
}
public void setPassengerList(List<EmployeeDto> passengerList) {
	this.passengerList = passengerList;
}

}

