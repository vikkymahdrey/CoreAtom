package com.agiledge.atom.billingtype.config.dto;

import java.util.ArrayList;

import com.agiledge.atom.dto.VehicleTypeDto;

public class TripBasedBillingConfigDto {
	private String doneBy;
	private String vehicleTypeId;
	private String flatTripRate;
	private String dcYes;
	private String acYes;
	private ArrayList<DistanceConstraintDto> dcList;
	private ArrayList<AcConstraintDto> acList;
	private String escortRateType;
	private String escortRate;
	private String billingRefId;
	private String id;
	private VehicleTypeDto vehicleTypeDto;
	
	public String getFlatTripRate() {
		return flatTripRate;
	}
	public void setFlatTripRate(String flatTripRate) {
		this.flatTripRate = flatTripRate;
	}
	public String getDcYes() {
		return dcYes;
	}
	public void setDcYes(String dcYes) {
		this.dcYes = dcYes;
	}
	public String getAcYes() {
		return acYes;
	}
	public void setAcYes(String acYes) {
		this.acYes = acYes;
	}
	public ArrayList<DistanceConstraintDto> getDcList() {
		return dcList;
	}
	public void setDcList(ArrayList<DistanceConstraintDto> dcList) {
		this.dcList = dcList;
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
 
}
