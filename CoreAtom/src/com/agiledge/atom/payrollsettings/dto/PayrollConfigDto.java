package com.agiledge.atom.payrollsettings.dto;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author noufalcc
 *
 */
@Entity(name="payroll_config")
@Table(name="payroll_config")
public class PayrollConfigDto {

 	@Override
	public String toString() {
		return "PayrollConfigDto [id=" + id + ", site=" + site
				+ ", transportType=" + transportType + ", payrollType="
				+ payrollType + ", flatRate=" + flatRate + ", fromDate="
				+ fromDate + ", toDate=" + toDate + ", oneWayDivider="
				+ oneWayDivider + ", oneWayDivRate=" + oneWayDivRate + "]";
	}
	private long id;
	private String site;
	private String transportType;
	private String payrollType;
	private Double flatRate;
	private Date fromDate;
	private Date toDate;
	private String oneWayDivider;
	private Double oneWayDivRate;
	 
	
	@Column(name="oneWayDivider", nullable=true)
	public String getOneWayDivider() {
		return oneWayDivider;
	}
	public void setOneWayDivider(String oneWayDivider) {
		this.oneWayDivider = oneWayDivider;
	}
 
	@Column(name="oneWayDivRate", nullable=true)
	public Double getOneWayDivRate() {
		return oneWayDivRate;
	}
	public void setOneWayDivRate(Double oneWayDivRate) {
		this.oneWayDivRate = oneWayDivRate;
	}
	@Column(name="site" )
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	@Column(name="transportType"  )
	public String getTransportType() {
		return transportType;
	}
	public void setTransportType(String transportType) {
		this.transportType = transportType;
	}
	
	@Column(name="payrollType" )
	public String getPayrollType() {
		return payrollType;
	}
	public void setPayrollType(String payrollType) {
		this.payrollType = payrollType;
	}
	
	@Column(name="flatRate" )
	public Double getFlatRate() {
		return flatRate;
	}
	public void setFlatRate(Double flatRate) {
		this.flatRate = flatRate;
	}
	
	@Column(name="fromDate" )
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	
	@Column(name="toDate", nullable=true)
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
	 
	@Id
	@GeneratedValue
	@Column(name="id" )
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
}
