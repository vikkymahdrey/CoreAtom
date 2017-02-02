package com.agiledge.atom.reports.dto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class CapacityUtilizationReportDto {

	private String siteId;
	private String siteName;
	private Date tripDate;
	private String vehicleTypeId;
	private String vehicleTypeName;
	private int avaiableCapacity;
	private int plannedCapacity;
	private String tripTime;
	private String tripLog;
	private int actualCapcity;
	private double avaliableVsPlanned;
	private double plannedVsActual;

	public double getPlannedVsActual() {
		return plannedVsActual;
	}

	public void setPlannedVsActual(double plannedVsActual) {
		this.plannedVsActual = plannedVsActual;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getSiteName() {
		return siteName;
	}

	public void setSiteName(String siteName) {
		this.siteName = siteName;
	}

	public String getVehicleTypeId() {
		return vehicleTypeId;
	}

	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

	public int getAvaiableCapacity() {
		return avaiableCapacity;
	}

	public void setAvaiableCapacity(int avaiableCapacity) {
		this.avaiableCapacity = avaiableCapacity;
	}

	public int getPlannedCapacity() {
		return plannedCapacity;
	}

	public void setPlannedCapacity(int plannedCapacity) {
		this.plannedCapacity = plannedCapacity;
	}

	public String getTripTime() {
		return tripTime;
	}

	public void setTripTime(String tripTime) {
		this.tripTime = tripTime;
	}

	public String getTripLog() {
		return tripLog;
	}

	public void setTripLog(String tripLog) {
		this.tripLog = tripLog;
	}

	public int getActualCapcity() {
		return actualCapcity;
	}

	public void setActualCapcity(int actualCapcity) {
		this.actualCapcity = actualCapcity;
	}

	public double getAvaliableVsPlanned() {
		return avaliableVsPlanned;
	}

	public void setAvaliableVsPlanned(double avaliableVsPlanned) {
		this.avaliableVsPlanned = avaliableVsPlanned;
	}

	public Date getTripDate() {
		
		
		return tripDate;
	}

	public void setTripDate(Date tripDate) {
		this.tripDate = tripDate;
	}

	public Date getTripDateReport() throws ParseException {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		String returnDate = sdf.format(tripDate);
		return sdf.parse(returnDate);

	}

}
