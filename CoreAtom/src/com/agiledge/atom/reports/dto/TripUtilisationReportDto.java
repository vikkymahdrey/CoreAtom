package com.agiledge.atom.reports.dto;

import java.util.Date;
import java.util.List;

import com.agiledge.atom.dto.TripDetailsDto;

public class TripUtilisationReportDto {

	private Date date;
	private int downloadedCount;
	private int startedCount;
	private int stoppedCount;
	private int pendingCount;
	private int totalCount;
	private int usage;
	private int totalcount;
	private int incount;
	private int outcount;
	private int escortcount;
	private int nonescortcount;
	private String trip_date;

	public String getTrip_date() {
		return trip_date;
	}
	public void setTrip_date(String trip_date) {
		this.trip_date = trip_date;
	}
	public int getTotalcount() {
		return totalcount;
	}
	public void setTotalcount(int totalcount) {
		this.totalcount = totalcount;
	}
	public int getIncount() {
		return incount;
	}
	public void setIncount(int incount) {
		this.incount = incount;
	}
	public int getOutcount() {
		return outcount;
	}
	public void setOutcount(int outcount) {
		this.outcount = outcount;
	}
	public int getEscortcount() {
		return escortcount;
	}
	public void setEscortcount(int escortcount) {
		this.escortcount = escortcount;
	}
	public int getNonescortcount() {
		return nonescortcount;
	}
	public void setNonescortcount(int nonescortcount) {
		this.nonescortcount = nonescortcount;
	}

	private int initialCount;


	public int getUsage() {
		return usage;
	}
	public void setUsage(int usage) {
		this.usage = usage;
	}

	private List<TripDetailsDto> startedTrips;
	private List<TripDetailsDto> stoppedTrips;
	private List<TripDetailsDto> downloadedTrips;
	private List<TripDetailsDto> totalTrips;
	private List<TripDetailsDto> notDownloaded;
	private List<TripDetailsDto> initialTrips;
	
	
	
	public List<TripDetailsDto> getInitialTrips() {
		return initialTrips;
	}
	public void setInitialTrips(List<TripDetailsDto> initialTrips) {
		this.initialTrips = initialTrips;
	}
	public int getInitialCount() {
		return initialCount;
	}
	public void setInitialCount(int initialCount) {
		this.initialCount = initialCount;
	}
 	public int getStoppedCount() {
		return stoppedCount;
	}
	public void setStoppedCount(int stoppedCount) {
		this.stoppedCount = stoppedCount;
	}
	public int getPendingCount() {
		return pendingCount;
	}
	public void setPendingCount(int pendingCount) {
		this.pendingCount = pendingCount;
	}
	public Date getDate() {
		return date;
	}
	public void setDate(Date date) {
		this.date = date;
	}
	public int getDownloadedCount() {
		return downloadedCount;
	}
	public void setDownloadedCount(int downloadedCount) {
		this.downloadedCount = downloadedCount;
	}
	public int getStartedCount() {
		return startedCount;
	}
	public void setStartedCount(int startedCount) {
		this.startedCount = startedCount;
	}
	public int getTotalCount() {
		return totalCount;
	}
	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}
	public List<TripDetailsDto> getStartedTrips() {
		return startedTrips;
	}
	public void setStartedTrips(List<TripDetailsDto> startedTrips) {
		this.startedTrips = startedTrips;
	}
	public List<TripDetailsDto> getDownloadedTrips() {
		return downloadedTrips;
	}
	public void setDownloadedTrips(List<TripDetailsDto> downloadedTrips) {
		this.downloadedTrips = downloadedTrips;
	}
	public List<TripDetailsDto> getStoppedTrips() {
		return stoppedTrips;
	}
	public void setStoppedTrips(List<TripDetailsDto> stoppedTrips) {
		this.stoppedTrips = stoppedTrips;
	}
	public List<TripDetailsDto> getTotalTrips() {
		return totalTrips;
	}
	public void setTotalTrips(List<TripDetailsDto> totalTrips) {
		this.totalTrips = totalTrips;
	}
	public List<TripDetailsDto> getNotDownloaded() {
		return notDownloaded;
	}
	public void setNotDownloaded(List<TripDetailsDto> notDownloaded) {
		this.notDownloaded = notDownloaded;
	}

}
