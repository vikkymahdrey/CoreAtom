package com.agiledge.atom.dto;

public class TripPickupTimeAlterDto {
private String time;
private String tripId;
private String traveltimeValue;

public String getTraveltimeValue() {
	return traveltimeValue;
}

public void setTraveltimeValue(String traveltimeValue) {
	this.traveltimeValue = traveltimeValue;
}

public String getTripId() {
	return tripId;
}

public void setTripId(String tripId) {
	this.tripId = tripId;
}

public String getTime() {
	return time;
}

public void setTime(String time) {
	this.time = time;
}
}
