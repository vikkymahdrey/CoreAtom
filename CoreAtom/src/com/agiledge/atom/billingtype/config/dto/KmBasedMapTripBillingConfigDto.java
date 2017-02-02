package com.agiledge.atom.billingtype.config.dto;

import java.util.ArrayList;

import com.agiledge.atom.dto.VehicleTypeDto;

public class KmBasedMapTripBillingConfigDto {
	private String doneBy;
	private String vehicleTypeId;
	private String flatTripRate;
 	private String billingRefId;
 	private String kmBillingRefId;
	private String id;
	private String distanceType;
	private String calculationType;
	private String tripRate;
	public String getTripRate() {
		return tripRate;
	}
	public void setTripRate(String tripRate) {
		this.tripRate = tripRate;
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
	public String getEscortRate() {
		return escortRate;
	}
	public void setEscortRate(String escortRate) {
		this.escortRate = escortRate;
	}
	private String rate;
	private String kmBillingType;
	private String acYes;
	private ArrayList<AcConstraintDto> acList= new ArrayList<AcConstraintDto>();
	private String escortRateType;
	private String escortRate;


	private VehicleTypeDto vehicleTypeDto;
	
	public String getFlatTripRate() {
		return flatTripRate;
	}
	public void setFlatTripRate(String flatTripRate) {
		this.flatTripRate = flatTripRate;
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
	public String getDistanceType() {
		return distanceType;
	}
	public void setDistanceType(String distanceType) {
		this.distanceType = distanceType;
	}
	public String getCalculationType() {
		return calculationType;
	}
	public void setCalculationType(String calculationType) {
		this.calculationType = calculationType;
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
	public String getKmBillingType() {
		return kmBillingType;
	}
	public void setKmBillingType(String kmBillingType) {
		this.kmBillingType = kmBillingType;
	}
 
}
