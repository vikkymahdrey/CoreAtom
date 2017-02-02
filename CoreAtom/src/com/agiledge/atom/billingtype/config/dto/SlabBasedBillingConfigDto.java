package com.agiledge.atom.billingtype.config.dto;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.dao.BillingTypeDao;
import com.agiledge.atom.dto.VehicleTypeDto;

public class SlabBasedBillingConfigDto extends BillingTypeDao {

	private String id;
	private String  refId;
	private String vehicleType;
	private String escortRateType;
	private String escortRate;
	private String tripRate;
	private String status;
	private String doneBy;
	private VehicleTypeDto vehicleTypeDto;
	 
	private ArrayList<BillingSlabDto> slabList; 
	
	@Override
	public String toString() {
		String s = "SlabBasedBillingConfigDto [id=" + id + ", refId=" + refId
				+ ", vehicleType=" + vehicleType + ", escortRateType="
				+ escortRateType + ", escortRate=" + escortRate + ", tripRate="
				+ tripRate + ", status=" + status + ", slabList=" + getSlabList()
				+ "]";
	   try{
		   for(BillingSlabDto dto : slabList ) {
			   s=s+"\n : " + dto.toString();
		   }
	   }catch(Exception e) {
		   System.out.println(e.getMessage());
	   }
	   return s;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getRefId() {
		return refId;
	}
	public void setRefId(String refId) {
		this.refId = refId;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
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
	public String getTripRate() {
		return tripRate;
	}
	public void setTripRate(String tripRate) {
		this.tripRate = tripRate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public ArrayList<BillingSlabDto> getSlabList() {
		return slabList;
	}
	public void setSlabList(ArrayList<BillingSlabDto> slabList) {
		this.slabList = slabList;
	}
	public String getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}
	public VehicleTypeDto getVehicleTypeDto() {
		return vehicleTypeDto;
	}
	public void setVehicleTypeDto(VehicleTypeDto vehicleTypeDto) {
		this.vehicleTypeDto = vehicleTypeDto;
	}
}
