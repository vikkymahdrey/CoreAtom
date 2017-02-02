package com.agiledge.atom.reports.dto;


public class AgileBillDto {

	
	private String monthName;
	private int month;
	private int year;
	private int firstCount;
	private int middleCount ;
	private int avgCount;
	private double pricePerSubscription;
	private double totalAmount;
	
	public int getAvgCount() {
		return avgCount;
	}
	public void setAvgCount(int avgCount) {
		this.avgCount = avgCount;
	}
	public double getPricePerSubscription() {
		return pricePerSubscription;
	}
	public void setPricePerSubscription(double pricePerSubscription) {
		this.pricePerSubscription = pricePerSubscription;
	}
	public double getTotalAmount() {
		return totalAmount;
	}
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	public int getMiddleCount() {
		return middleCount;
	}
	public void setMiddleCount(int middleCount) {
		this.middleCount = middleCount;
	}
	private int lastCount;
 
	 
	 
	public int getFirstCount() {
		return firstCount;
	}
	public void setFirstCount(int firstCount) {
		this.firstCount = firstCount;
	}
	public int getLastCount() {
		return lastCount;
	}
	public void setLastCount(int lastCount) {
		this.lastCount = lastCount;
	}
	 
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public int getMonth() {
		return month;
	}
	public void setMonth(int month) {
		this.month = month;
	}
	public int getYear() {
		return year;
	}
	public void setYear(int year) {
		this.year = year;
	}
 
 
	 
 
	
	
}
