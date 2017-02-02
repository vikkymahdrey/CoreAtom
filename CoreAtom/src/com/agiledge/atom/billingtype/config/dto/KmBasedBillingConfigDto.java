package com.agiledge.atom.billingtype.config.dto;

 

import com.agiledge.atom.billingtype.dto.BillingTypeDto;

public class KmBasedBillingConfigDto extends BillingTypeDto {
	private String doneBy;
	private String kmBillingType;
	private String site;
	private String billingRefId; 
	 
	private String id;

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getBillingRefId() {
		return billingRefId;
	}

	public void setBillingRefId(String billingRefId) {
		this.billingRefId = billingRefId;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getKmBillingType() {
		return kmBillingType;
	}

	public void setKmBillingType(String kmBillingType) {
		this.kmBillingType = kmBillingType;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}
	 
	  	 
}
