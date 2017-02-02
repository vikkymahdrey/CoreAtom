package com.agiledge.atom.billingtype.config.dto;

 

import java.util.ArrayList;

import com.agiledge.atom.dto.VehicleTypeDto;

public class KmBasedClassicTripBillingConfigDto extends KmBasedBillingConfigDto {
	private String doneBy;
	private String vehicleTypeId;
	private String rate;
	private String billingRefId; 
	private String kmBillingRefId;
	private String id;
	private VehicleTypeDto vehicleTypeDto;
	private String acYes;
	private ArrayList<AcConstraintDto> acList= new ArrayList<AcConstraintDto>();
	private String escortRateType;
	private String escortRate;

	
	 
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
	public String getEscortRate() {
		return escortRate;
	}
	public void setEscortRate(String escortRate) {
		this.escortRate = escortRate;
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
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getKmBillingRefId() {
		return kmBillingRefId;
	}
	public void setKmBillingRefId(String kmBillingRefId) {
		this.kmBillingRefId = kmBillingRefId;
	}
	 
}
