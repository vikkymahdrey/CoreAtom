package com.agiledge.atom.reports.dto;

public class ProjectWiseTripBillDto {
	
	
	
	private String tripDate;
	private String project;
	private String projectcode;
	private String projectId;
	public String getProjectId() {
		return projectId;
	}
	public void setProjectId(String projectId) {
		this.projectId = projectId;
	}
	private int totalEmployees;
	private double plannedTotalcost;
	private double plannedPerInstanceCost;
	private double plannedCostPerEmployees;
	private double actualTotalCost;
	private double actualPerInstanceCost;
	private double actualCostPerEmployees;
	private String tripLog;
	private String tripTime;
	
	public String getTripLog() {
		return tripLog;
	}
	public void setTripLog(String tripLog) {
		this.tripLog = tripLog;
	}
	public String getTripTime() {
		return tripTime;
	}
	public void setTripTime(String tripTime) {
		this.tripTime = tripTime;
	}
	public String getTripDate() {
		return tripDate;
	}
	public void setTripDate(String tripDate) {
		this.tripDate = tripDate;
	}
	public String getProject() {
		return project;
	}
	public void setProject(String project) {
		this.project = project;
	}
	public String getProjectcode() {
		return projectcode;
	}
	public void setProjectcode(String projectcode) {
		this.projectcode = projectcode;
	}
	public int getTotalEmployees() {
		return totalEmployees;
	}
	public void setTotalEmployees(int totalEmployees) {
		this.totalEmployees = totalEmployees;
	}
	public double getPlannedTotalcost() {
		return plannedTotalcost;
	}
	public void setPlannedTotalcost(double plannedTotalcost) {
		this.plannedTotalcost = plannedTotalcost;
	}
	public double getPlannedPerInstanceCost() {
		return plannedPerInstanceCost;
	}
	public void setPlannedPerInstanceCost(double plannedPerInstanceCost) {
		this.plannedPerInstanceCost = plannedPerInstanceCost;
	}
	public double getPlannedCostPerEmployees() {
		return plannedCostPerEmployees;
	}
	public void setPlannedCostPerEmployees(double plannedCostPerEmployees) {
		this.plannedCostPerEmployees = plannedCostPerEmployees;
	}
	public double getActualTotalCost() {
		return actualTotalCost;
	}
	public void setActualTotalCost(double actualTotalCost) {
		this.actualTotalCost = actualTotalCost;
	}
	public double getActualPerInstanceCost() {
		return actualPerInstanceCost;
	}
	public void setActualPerInstanceCost(double actualPerInstanceCost) {
		this.actualPerInstanceCost = actualPerInstanceCost;
	}
	public double getActualCostPerEmployees() {
		return actualCostPerEmployees;
	}
	public void setActualCostPerEmployees(int actualCostPerEmployees) {
		this.actualCostPerEmployees = actualCostPerEmployees;
	}
	

}
