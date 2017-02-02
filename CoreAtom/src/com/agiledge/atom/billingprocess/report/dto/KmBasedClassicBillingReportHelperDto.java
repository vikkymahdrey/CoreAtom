package com.agiledge.atom.billingprocess.report.dto;

import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.dto.TripDetailsDto;

public class KmBasedClassicBillingReportHelperDto {


	private String monthName;
	private Date fromDate;
	private Date toDate;
	private Date date;
	private String vehicleType;
	private long totalTrips;
	private double rate;
	private String year;
	private String month;
	private String fromKm;
	private String toKm;
	private String distance;
	private double escortRate;
	 
	private ArrayList<TripDetailsDto> trips;
	
	public String getFromKm() {
		return fromKm;
	}
	public void setFromKm(String fromKm) {
		this.fromKm = fromKm;
	}
	public String getToKm() {
		return toKm;
	}
	public void setToKm(String toKm) {
		this.toKm = toKm;
	} 
	 
	 
	public String getMonthName() {
		return monthName;
	}
	public void setMonthName(String monthName) {
		this.monthName = monthName;
	}
	public Date getFromDate() {
		return fromDate;
	}
	public void setFromDate(Date fromDate) {
		this.fromDate = fromDate;
	}
	public Date getToDate() {
		return toDate;
	}
	public void setToDate(Date toDate) {
		this.toDate = toDate;
	}
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
	public long getTotalTrips() {
		return totalTrips;
	}
	public void setTotalTrips(long totalTrips) {
		this.totalTrips = totalTrips;
	}
	public double getRate() {
		return rate;
	}
	public void setRate(double rate) {
		this.rate = rate;
	}
	public double getEscortRate() {
		return escortRate;
	}
	public void setEscortRate(double escortRate) {
		this.escortRate = escortRate;
	}
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	public ArrayList<TripDetailsDto> getTrips() {
		return trips;
	}
	public void setTrips(ArrayList<TripDetailsDto> trips) {
		this.trips = trips;
	}
	public String getDistance() {
		return distance;
	}
	public void setDistance(String distance) {
		this.distance = distance;
	}
	
	
}
