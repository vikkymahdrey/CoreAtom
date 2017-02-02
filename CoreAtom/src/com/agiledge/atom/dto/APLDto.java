/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dto;

/**
 * 
 * @author 123
 */
public class APLDto {

	private String areaID;
	private String placeID;
	private String landMarkID;
	private String area;
	private String place;
	private String landMark;
	private String lattitude;
	private String longitude;
	private String doneby;
	private String location;
	private boolean isGeocodeUpdateMode=false;
	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getLocationName() {
		return locationName;
	}

	public void setLocationName(String locationName) {
		this.locationName = locationName;
	}

	private String locationName;

	public String getDoneby() {
		return doneby;
	}

	public void setDoneby(String doneby) {
		this.doneby = doneby;
	}

	public void setAreaID(String areaID) {
		this.areaID = areaID;
	}

	public String getAreaID() {
		return this.areaID;
	}

	public void setArea(String area) {
		this.area = area;
	}

	public String getArea() {
		return this.area;
	}

	public void setPlaceID(String placeID) {
		this.placeID = placeID;
	}

	public String getPlaceID() {
		return this.placeID;
	}

	public void setLandMarkID(String landMarkID) {
		this.landMarkID = landMarkID;
	}

	public String getLandMarkID() {
		return this.landMarkID;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getPlace() {
		return this.place;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;

	}

	public String getLandMark() {
		return this.landMark;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public boolean isGeocodeUpdateMode() {
		return isGeocodeUpdateMode;
	}

	public void setGeocodeUpdateMode(boolean isGeocodeUpdateMode) {
		this.isGeocodeUpdateMode = isGeocodeUpdateMode;
	}

}
