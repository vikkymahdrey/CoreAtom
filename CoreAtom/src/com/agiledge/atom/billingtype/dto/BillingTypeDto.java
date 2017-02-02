package com.agiledge.atom.billingtype.dto;
 

import java.util.Date;

public class BillingTypeDto {
	
	@Override
	public String toString() {
		return "BillingTypeDto [CATAGORY=" + CATAGORY + ", key=" + key
				+ ", name=" + name + ", description=" + description
				+ ", siteId=" + siteId + ", vendorId=" + vendorId
				+ ", transportTypeId=" + transportTypeId + ", status=" + status
				+ ", billingTypeId=" + billingTypeId + ", id=" + id
				+ ", transportType=" + transportType + ", doneBy=" + doneBy
				+ ", fromDate=" + fromDate + ", toDate=" + toDate + ", site="
				+ site + ", vendor=" + vendor + ", billingType=" + billingType
				+ ", conditionId=" + conditionId + ", conditionUrl="
				+ conditionUrl + "]";
	}
	public String CATAGORY;
	private String key;
	private String name;
	private String description;
	private String siteId;
	private String vendorId;
	private String transportTypeId;
	private String status;
	private String billingTypeId;
	private String id;
	private String transportType;
	private String doneBy;
	private Date fromDate;
	private Date  toDate;
	private String site;
	private String vendor;
	private String billingType;
	private String conditionId;
	private String conditionUrl;
	
	public String getConditionUrl() {
		return conditionUrl;
	}
	public void setConditionUrl(String conditionUrl) {
		this.conditionUrl = conditionUrl;
	}
	public String getConditionId() {
		return conditionId;
	}
	public void setConditionId(String conditionId) {
		this.conditionId = conditionId;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	public String getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBillingTypeId() {
		return billingTypeId;
	}
	public void setBillingTypeId(String billingTypeId) {
		this.billingTypeId = billingTypeId;
	}
	public String getBillingType() {
		return billingType;
	}
	public void setBillingType(String billingType) {
		this.billingType = billingType;
	}
	public String getKey() {
		return key;
	}
	public void setKey(String key) {
		this.key = key;
	}
	
	


}
