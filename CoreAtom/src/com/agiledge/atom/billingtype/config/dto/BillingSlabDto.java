package com.agiledge.atom.billingtype.config.dto;

public class BillingSlabDto {

	private String id;
	private String slabBillingId;
	private String slabId;
	private String rate;
	private String startTime;
	private String speedPerKm;
	
	public String getStartTime() {
		return startTime;
	}
	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}
	public String getEndTime() {
		return endTime;
	}
	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}
	public String getTrafic() {
		return trafic;
	}
	public void setTrafic(String trafic) {
		this.trafic = trafic;
	}
	private String endTime;
	private String trafic;
	 
	 
	@Override
	public String toString() {
		return "BillingSlabDto [id=" + id + ", slabBillingId=" + slabBillingId
				+ ", slabId=" + slabId + ", rate=" + rate + ", startTime="
				+ startTime + ", endTime=" + endTime + ", trafic=" + trafic
				+ ", status=" + status + "]";
	}
	private String status;
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getSlabBillingId() {
		return slabBillingId;
	}
	public void setSlabBillingId(String slabBillingId) {
		this.slabBillingId = slabBillingId;
	}
	public String getSlabId() {
		return slabId;
	}
	public void setSlabId(String slabId) {
		this.slabId = slabId;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getSpeedPerKm() {
		return speedPerKm;
	}
	public void setSpeedPerKm(String speedPerKm) {
		this.speedPerKm = speedPerKm;
	}
	
	
	
	
}
