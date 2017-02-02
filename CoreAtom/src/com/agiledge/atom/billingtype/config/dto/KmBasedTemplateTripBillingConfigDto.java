package com.agiledge.atom.billingtype.config.dto;

 

import java.util.ArrayList;

import com.agiledge.atom.dto.VehicleTypeDto;

public class KmBasedTemplateTripBillingConfigDto {
	private String doneBy;
	private String tripRate;
	private String vehicleTypeId;
	private String templateName;
	private int templateId;
	private String templateUrl;
	private String swingRateType;
	private String swingRate;
	private String kmBillingRefId;
 	private String billingRefId;
	private String id;
	private VehicleTypeDto vehicleTypeDto;
	private String kmBillingType;
	private String acYes;
	private ArrayList<AcConstraintDto> acList= new ArrayList<AcConstraintDto>();
	private String escortRateType;
	private double escortRate;
	  
	
	public String getTemplateName() {
		return templateName;
	}
	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}
	public int getTemplateId() {
		return templateId;
	}
	public void setTemplateId(int templateId) {
		this.templateId = templateId;
	}
	public String getTemplateUrl() {
		return templateUrl;
	}
	public void setTemplateUrl(String templateUrl) {
		this.templateUrl = templateUrl;
	}
	 
	public String getSwingRate() {
		return swingRate;
	}
	public void setSwingRate(String swingRate) {
		this.swingRate = swingRate;
	}
	
	public String getVehicleTypeId() {
		return vehicleTypeId;
	}
	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}
	public String getBillingRefId() {
		return billingRefId;
	}
	public void setBillingRefId(String billingRefId) {
		this.billingRefId = billingRefId;
	}
	public String getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public VehicleTypeDto getVehicleTypeDto() {
		return vehicleTypeDto;
	}
	public void setVehicleTypeDto(VehicleTypeDto vehicleTypeDto) {
		this.vehicleTypeDto = vehicleTypeDto;
	}
	public String getKmBillingRefId() {
		return kmBillingRefId;
	}
	public void setKmBillingRefId(String kmBillingRefId) {
		this.kmBillingRefId = kmBillingRefId;
	}
	public String getKmBillingType() {
		return kmBillingType;
	}
	public void setKmBillingType(String kmBillingType) {
		this.kmBillingType = kmBillingType;
	}
	public String getSwingRateType() {
		return swingRateType;
	}
	public void setSwingRateType(String swingRateType) {
		this.swingRateType = swingRateType;
	}
	  
	public String getAcYes() {
		return acYes;
	}
	public void setAcYes(String acYes) {
		this.acYes = acYes;
	}
	public ArrayList<AcConstraintDto> getAcList() {
		return acList;
	}
	public void setAcList(ArrayList<AcConstraintDto> acList) {
		this.acList = acList;
	} 
	public String getEscortRateType() {
		return escortRateType;
	}
	public void setEscortRateType(String escortRateType) {
		this.escortRateType = escortRateType;
	}
	public double getEscortRate() {
		return escortRate;
	}
	public void setEscortRate(double escortRate) {
		this.escortRate = escortRate;
	}
	public String getTripRate() {
		return tripRate;
	}
	public void setTripRate(String tripRate) {
		this.tripRate = tripRate;
	}
	 
 
}
