package com.agiledge.atom.reports.dto;

import java.util.Date;

public class ByVehicleTypeReportDto {
	
	
	private Date date;
	private String vendorId;
	public String getVendorId() {
		return vendorId;
	}
	public void setVendorId(String vendorId) {
		this.vendorId = vendorId;
	}
	private String vehicleType;
	private String option;
	private String optionTitle;
	private int count;
	private double totalDistance;
	private long  averageTime;

	private int totalOnTimeCount;
	private double averageDistance;
	public int getTotalOnTimeCount() {
		return totalOnTimeCount;
	}
	public void setTotalOnTimeCount(int totalOnTimeCount) {
		this.totalOnTimeCount = totalOnTimeCount;
	}
	public void setAverageTime(long averageTime) {
		this.averageTime = averageTime;
	}
	private int minCount;
	private int maxCount;
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
 	public String getOption() {
		return option;
	}
	public void setOption(String option) {
		this.option = option;
	}
 	public String getOptionTitle() {
		return optionTitle;
	}
	public void setOptionTitle(String optionTitle) {
		this.optionTitle = optionTitle;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public double getTotalDistance() {
		return totalDistance;
	}
	public void setTotalDistance(double totalDistance) {
		this.totalDistance = totalDistance;
	}
	public long  getAverageTime() {
		return averageTime;
	}
	 
	public int getMinCount() {
		return minCount;
	}
	public void setMinCount(int minCount) {
		this.minCount = minCount;
	}
	public int getMaxCount() {
		return maxCount;
	}
	public void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}
	public double getAverageDistance() {
		return averageDistance;
	}
	public void setAverageDistance(double averageDistance) {
		this.averageDistance = averageDistance;
	}
	

}
