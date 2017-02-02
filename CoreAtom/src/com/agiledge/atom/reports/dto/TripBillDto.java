package com.agiledge.atom.reports.dto;

public class TripBillDto {

	 private int monthNo;
	public int getMonthNo() {
		return monthNo;
	}
	public void setMonthNo(int monthNo) {
		this.monthNo = monthNo;
	}
	private String month;
	private String type;
	private int actualTrips;
	private int submittedTrips;
	private int approvedTrips;
	private String site;
	private int totalCost;
	private int perTripCost;
	public int getApprovedTripsWithEscort() {
		return approvedTripsWithEscort;
	}
	public void setApprovedTripsWithEscort(int approvedTripsWithEscort) {
		this.approvedTripsWithEscort = approvedTripsWithEscort;
	}
	public int getTotalCostWithEscort() {
		return totalCostWithEscort;
	}
	public void setTotalCostWithEscort(int totalCostWithEscort) {
		this.totalCostWithEscort = totalCostWithEscort;
	}
	public int getPerTripCostWithEscort() {
		return perTripCostWithEscort;
	}
	public void setPerTripCostWithEscort(int perTripCostWithEscort) {
		this.perTripCostWithEscort = perTripCostWithEscort;
	}
	public int getApprovedTripsWithoutEscort() {
		return approvedTripsWithoutEscort;
	}
	public void setApprovedTripsWithoutEscort(int approvedTripsWithoutEscort) {
		this.approvedTripsWithoutEscort = approvedTripsWithoutEscort;
	}
	public int getTotalCostWithoutEscort() {
		return totalCostWithoutEscort;
	}
	public void setTotalCostWithoutEscort(int totalCostWithoutEscort) {
		this.totalCostWithoutEscort = totalCostWithoutEscort;
	}
	public int getPerTripCostWithoutEscort() {
		return perTripCostWithoutEscort;
	}
	public void setPerTripCostWithoutEscort(int perTripCostWithoutEscort) {
		this.perTripCostWithoutEscort = perTripCostWithoutEscort;
	}
	private int approvedTripsWithEscort;
	private int totalCostWithEscort;
	private int perTripCostWithEscort;
	private int approvedTripsWithoutEscort;
	private int totalCostWithoutEscort;
	private int perTripCostWithoutEscort;
	public String getMonth() {
		return month;
	}
	public void setMonth(String month) {
		this.month = month;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public int getActualTrips() {
		return actualTrips;
	}
	public void setActualTrips(int actualTrips) {
		this.actualTrips = actualTrips;
	}
	public int getSubmittedTrips() {
		return submittedTrips;
	}
	public void setSubmittedTrips(int submittedTrips) {
		this.submittedTrips = submittedTrips;
	}
	public int getApprovedTrips() {
		return approvedTrips;
	}
	public void setApprovedTrips(int approvedTrips) {
		this.approvedTrips = approvedTrips;
	}
	public String getSite() {
		return site;
	}
	public void setSite(String site) {
		this.site = site;
	}
	public int getTotalCost() {
		return totalCost;
	}
	public void setTotalCost(int totalCost) {
		this.totalCost = totalCost;
	}
	public int getPerTripCost() {
		return perTripCost;
	}
	public void setPerTripCost(int perTripCost) {
		this.perTripCost = perTripCost;
	}
	 

}
