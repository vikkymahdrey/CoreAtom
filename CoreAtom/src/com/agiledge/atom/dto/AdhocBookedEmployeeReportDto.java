package com.agiledge.atom.dto;

public class AdhocBookedEmployeeReportDto {
	private String siteId;
	private String BookedFor;
	private String AdhocType;
	private String TravelDate;
	private String BookedDate;
	private String BookedBy;
	private String PickupDrop;
	private String StartTime;
	private String ApprovedBy;
	private String Status;
	private String personnelNo;
	public String getPersonnelNo() {
		return personnelNo;
	}
	public void setPersonnelNo(String personnelNo) {
		this.personnelNo = personnelNo;
	}
	public String getSiteId() {
		return siteId;
	}
	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}
	public String getBookedFor() {
		return BookedFor;
	}
	public void setBookedFor(String bookedFor) {
		BookedFor = bookedFor;
	}
	public String getAdhocType() {
		return AdhocType;
	}
	public void setAdhocType(String adhocType) {
		AdhocType = adhocType;
	}
	public String getTravelDate() {
		return TravelDate;
	}
	public void setTravelDate(String travelDate) {
		TravelDate = travelDate;
	}
	public String getBookedDate() {
		return BookedDate;
	}
	public void setBookedDate(String bookedDate) {
		BookedDate = bookedDate;
	}
	public String getBookedBy() {
		return BookedBy;
	}
	public void setBookedBy(String bookedBy) {
		BookedBy = bookedBy;
	}
	public String getPickupDrop() {
		return PickupDrop;
	}
	public void setPickupDrop(String pickupDrop) {
		PickupDrop = pickupDrop;
	}
	public String getStartTime() {
		return StartTime;
	}
	public void setStartTime(String startTime) {
		StartTime = startTime;
	}
	public String getApprovedBy() {
		return ApprovedBy;
	}
	public void setApprovedBy(String approvedBy) {
		ApprovedBy = approvedBy;
	}
	public String getStatus() {
		return Status;
	}
	public void setStatus(String status) {
		Status = status;
	}
}