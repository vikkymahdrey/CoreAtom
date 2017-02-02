package com.agiledge.atom.mobile.dto;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity(name = "cs")
@Table(name = "cometSettings")
public class CometSettingsDto {
	private long id;
	private String cometVersionNo;
	private String cometAuthType;
	
	@Id
	@GeneratedValue
	@Column(name="id", nullable=false)
	public long getId() {
		return id;
	}
	
	public void setId(long id) {
		this.id = id;
	}
	
	@Column(name="cometVersionNo", nullable=false)
	public String getCometVersionNo() {
		return cometVersionNo;
	}
	public void setCometVersionNo(String cometVersionNo) {
		this.cometVersionNo = cometVersionNo;
	}
	
	@Column(name="cometAuthType", nullable=false)
	public String getCometAuthType() {
		return cometAuthType;
	}
	public void setCometAuthType(String cometAuthType) {
		this.cometAuthType = cometAuthType;
	}
	 
//	@Column(name="cometAuthType", nullable=true)
	 
	 
}
