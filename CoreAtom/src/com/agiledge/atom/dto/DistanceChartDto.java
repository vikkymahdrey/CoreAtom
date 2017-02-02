package com.agiledge.atom.dto;

public class DistanceChartDto {
	private String sourceLandmarkId;
	private String destinationLandmarkId;
	private String sourceLattitude;
	private String sourceLongitude;
	private String destinationLattitude;
	private String destinationLongitude;

	private float distance;

	public String getSourceLandmarkId() {
		return sourceLandmarkId;
	}

	public void setSourceLandmarkId(String sourceLandmarkId) {
		this.sourceLandmarkId = sourceLandmarkId;
	}

	public String getDestinationLandmarkId() {
		return destinationLandmarkId;
	}

	public void setDestinationLandmarkId(String destinationLandmarkId) {
		this.destinationLandmarkId = destinationLandmarkId;
	}

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public String getSourceLattitude() {
		return sourceLattitude;
	}

	public void setSourceLattitude(String sourceLattitude) {
		this.sourceLattitude = sourceLattitude;
	}

	public String getSourceLongitude() {
		return sourceLongitude;
	}

	public void setSourceLongitude(String sourceLongitude) {
		this.sourceLongitude = sourceLongitude;
	}

	public String getDestinationLattitude() {
		return destinationLattitude;
	}

	public void setDestinationLattitude(String destinationLattitude) {
		this.destinationLattitude = destinationLattitude;
	}

	public String getDestinationLongitude() {
		return destinationLongitude;
	}

	public void setDestinationLongitude(String destinationLongitude) {
		this.destinationLongitude = destinationLongitude;
	}
}
