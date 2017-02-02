package com.agiledge.atom.transporttype.dto;

import java.util.Date;

public class TransportTypeDto {

	private String name;
	private String description;
	private String siteId;
	private String vendorId;
	private String transportTypeId;
	private String status;
	private String employeeId;
	private String landmarkId;

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getVendorId() {
		return vendorId;
	}

	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}

	public String getTransportTypeId() {
		return transportTypeId;
	}

	public void setTransportTypeId(String transportTypeId) {
		this.transportTypeId = transportTypeId;
	}

	private int id;
	private String transportType;
	private int doneBy;
	private Date fromDate;
	private Date toDate;
	private String site;
	private String vendor;

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getVendor() {
		return vendor;
	}

	public void setVendor(String vendor) {
		this.vendor = vendor;
	}

	public Date getFromDate() {
		return fromDate;
	}

	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}

	public Date getToDate() {
		return toDate;
	}

	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}

	public int getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(int doneBy) {
		this.doneBy = doneBy;
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

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getTransportType() {
		return transportType;
	}

	public void setTransportType(String transportType) {
		this.transportType = transportType;
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
		if (landmarkId != null && landmarkId.equals(""))
			landmarkId = null;
		this.landmarkId = landmarkId;
	}

}
