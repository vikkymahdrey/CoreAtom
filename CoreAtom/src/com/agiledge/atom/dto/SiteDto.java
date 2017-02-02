/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

/**
 * 
 * @author muhammad
 */
public class SiteDto {
	private String id;
	private String name;
	private String landmark;
	private String landmarkName;
	private String night_shift_start;
	private String night_shift_end;
	private String lady_securiy;
	private String hasLadySecurity;
	private String vehicleType;
	private String vehicleTypeName;
	private String branch;
	private String doneBy;
	private String weekoffcombain;
	
	

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public String getVehicleTypeName() {
		return vehicleTypeName;
	}

	public void setVehicleTypeName(String vehicleTypeName) {
		this.vehicleTypeName = vehicleTypeName;
	}

	public String getVehicleType() {
		return vehicleType;
	}

	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getLady_securiy() {
		return lady_securiy;
	}

	public void setLady_securiy(String lady_securiy) {
		this.lady_securiy = lady_securiy;
	}

	public String getLandmark() {
		return landmark;
	}

	public void setLandmark(String landmark) {
		this.landmark = landmark;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNight_shift_end() {
		return night_shift_end;
	}

	public void setNight_shift_end(String night_shift_end) {
		this.night_shift_end = night_shift_end;
	}

	public String getNight_shift_start() {
		return night_shift_start;
	}

	public void setNight_shift_start(String night_shift_start) {
		this.night_shift_start = night_shift_start;
	}

	public String getBranch() {
		return branch;
	}

	public void setBranch(String branch) {
		this.branch = branch;
	}

	public String getLandmarkName() {
		return landmarkName;
	}

	public void setLandmarkName(String landmarkName) {
		this.landmarkName = landmarkName;
	}

	public String getHasLadySecurity() {
		return hasLadySecurity;
	}

	public void setHasLadySecurity(String hasLadySecurity) {
		this.hasLadySecurity = hasLadySecurity;
	}

	public String getWeekoffcombain() {
		return weekoffcombain;
	}

	public void setWeekoffcombain(String weekoffcombain) {
		this.weekoffcombain = weekoffcombain;
	}
}
