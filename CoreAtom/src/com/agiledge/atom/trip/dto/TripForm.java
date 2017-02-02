package com.agiledge.atom.trip.dto;

import java.util.ArrayList;

import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class TripForm {

	private ArrayList<TripDetailsDto> tripList;
	 
	private String siteId;
	private String trip_date;
	private String trip_time;
	private String trip_log;

	public ArrayList<TripDetailsDto> getTripList() {
		return tripList;
	}

	public void setTripList(ArrayList<TripDetailsDto> tripList) {
		this.tripList = tripList;
	}

	public String getTrip_date() {
		return trip_date;
	}

	public void setTrip_date(String trip_date) {
		this.trip_date = trip_date;
	}

	public String getSiteId() {
		return siteId;
	}

	public void setSiteId(String siteId) {
		this.siteId = siteId;
	}

	public String getTrip_time() {
		return trip_time;
	}

	public void setTrip_time(String trip_time) {
		this.trip_time = trip_time;
	}

	public String getTrip_log() {
		return trip_log;
	}

	public void setTrip_log(String trip_log) {
		this.trip_log = trip_log;
	}

 
}
