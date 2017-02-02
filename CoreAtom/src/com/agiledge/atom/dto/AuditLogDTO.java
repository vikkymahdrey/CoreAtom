package com.agiledge.atom.dto;

import java.util.Date;

public class AuditLogDTO {

	private int id;
	private long relatedNodeId;
	private String moduleName;
	private Date dateChanged;
	private String displayDate;
	private int changedBy;
	private String changedByName;
	private String previousState;
	private String currentState;
	private String action;
	private String tripCode;
	private String tripDate;
	private String tripTime;
	public String getTripCode() {
		return tripCode;
	}
	public void setTripCode(String tripCode) {
		this.tripCode = tripCode;
	}
	public String getTripDate() {
		return tripDate;
	}
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	public String getTripTime() {
		return tripTime;
	}
	public void setTripTime(String tripTime) {
		this.tripTime = tripTime;
	}
	public String getTripMode() {
		return tripMode;
	}
	public void setTripMode(String tripMode) {
		this.tripMode = tripMode;
	}
	private String tripMode;
	
	public String getDisplayDate() {
		return displayDate;
	}
	public void setDisplayDate(String displayDate) {
		this.displayDate = displayDate;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public long getRelatedNodeId() {
		return relatedNodeId;
	}
	public void setRelatedNodeId(long relatedNodeId) {
		this.relatedNodeId = relatedNodeId;
	}
	public String getModuleName() {
		return moduleName;
	}
	public void setModuleName(String moduleName) {
		this.moduleName = moduleName;
	}
	public Date getDateChanged() {
		return dateChanged;
	}
	public void setDateChanged(Date dateChanged) {
		this.dateChanged = dateChanged;
	}
	public int getChangedBy() {
		return changedBy;
	}
	public void setChangedBy(int changedBy) {
		this.changedBy = changedBy;
	}
	public String getPreviousState() {
		return previousState;
	}
	public void setPreviousState(String previousState) {
		this.previousState = previousState;
	}
	public String getCurrentState() {
		return currentState;
	}
	public void setCurrentState(String currentState) {
		this.currentState = currentState;
	}
	public String getAction() {
		return action;
	}
	public void setAction(String action) {
		this.action = action;
	}
	public String getChangedByName() {
		return changedByName;
	}
	public void setChangedByName(String changedByName) {
		this.changedByName = changedByName;
	}
	
	

}
