package com.agiledge.atom.usermanagement.dto;

public class UserManagementDto {
	
	private int id;
	private String name;
	private String description;
	private String userType;
	private String type;
	private String updatedBy;
	private String selectionStatus;
	
	
	public String getUpdatedBy() {
		return updatedBy;
	}
	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUserType() {
		return userType;
	}
	public void setUserType(String userType) {
		this.userType = userType;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getSelectionStatus() {
		return selectionStatus;
	}
	public void setSelectionStatus(String selectionStatus) {
		this.selectionStatus = selectionStatus;
	}

}
