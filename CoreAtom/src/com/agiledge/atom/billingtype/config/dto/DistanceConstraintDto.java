package com.agiledge.atom.billingtype.config.dto;

public class DistanceConstraintDto {

	private String parentId;
	private String fromKm;
	private String toKm;
	private String rate;
	private String dcAcRate;
	private String id;
	  
	public String getToKm() {
		return toKm;
	}
	public void setToKm(String toKm) {
		this.toKm = toKm;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getParentId() {
		return parentId;
	}
	public void setParentId(String parentId) {
		this.parentId = parentId;
	}
	public String getFromKm() {
		return fromKm;
	}
	public void setFromKm(String fromKm) {
		this.fromKm = fromKm;
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDcAcRate() {
		return dcAcRate==null?"0":dcAcRate;
	}
	public void setDcAcRate(String dcAcRate) {
		this.dcAcRate = dcAcRate;
	} 
}
