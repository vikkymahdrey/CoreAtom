package com.agiledge.atom.dto;

public class PanicDto {
	private String Id;
	private String tripId;
	private String alarmCause;
	private String primaryAction;
	private String acknowledgeBy;
	private String primaryActiontakenBy;
	private String primaryActiontakenTime;
	private String primaryActiontakenByName;
	private String approvedBy;
	private String approvedByTime;
	private String actiontime;
	private String vehicle;
	private String activatedby;
	
	
	public String getActivatedby() {
		return activatedby;
	}
	public void setActivatedby(String activatedby) {
		this.activatedby = activatedby;
	}
	public String getVehicle() {
		return vehicle;
	}
	public void setVehicle(String vehicle) {
		this.vehicle = vehicle;
	}
	
	
	public String getActiontime() {
		return actiontime;
	}
	public void setActiontime(String actiontime) {
		this.actiontime = actiontime;
	}
	public String getPrimaryActiontakenTime() {
		return primaryActiontakenTime;
	}
	public void setPrimaryActiontakenTime(String primaryActiontakenTime) {
		this.primaryActiontakenTime = primaryActiontakenTime;
	}
	public String getPrimaryActiontakenByName() {
		return primaryActiontakenByName;
	}
	public void setPrimaryActiontakenByName(String primaryActiontakenByName) {
		this.primaryActiontakenByName = primaryActiontakenByName;
	}
	public String getApprovedByTime() {
		return approvedByTime;
	}
	public void setApprovedByTime(String approvedByTime) {
		this.approvedByTime = approvedByTime;
	}
	public String getApprovedByName() {
		return approvedByName;
	}
	public void setApprovedByName(String approvedByName) {
		this.approvedByName = approvedByName;
	}
	private String approvedByName;
	private String curStatus;	
	public String getAlarmCause() {
		return alarmCause;
	}
	public void setAlarmCause(String alarmCause) {
		this.alarmCause = alarmCause;
	}
	public String getPrimaryActiontakenBy() {
		return primaryActiontakenBy;
	}
	public void setPrimaryActiontakenBy(String primaryActiontakenBy) {
		this.primaryActiontakenBy = primaryActiontakenBy;
	}
	public String getApprovedBy() {
		return approvedBy;
	}
	public void setApprovedBy(String approvedBy) {
		this.approvedBy = approvedBy;
	}
	public String getCurStatus() {
		return curStatus;
	}
	public void setCurStatus(String curStatus) {
		this.curStatus = curStatus;
	}
	public String getTripId() {
		return tripId;
	}
	public void setTripId(String tripId) {
		this.tripId = tripId;
	}
	public String getPrimaryAction() {
		return primaryAction;
	}
	public void setPrimaryAction(String primaryAction) {
		this.primaryAction = primaryAction;
	}
	public String getId() {
		return Id;
	}
	public void setId(String id) {
		Id = id;
	}
	public String getAcknowledgeBy() {
		return acknowledgeBy;
	}
	public void setAcknowledgeBy(String acknowledgeBy) {
		this.acknowledgeBy = acknowledgeBy;
	}
}
