package com.agiledge.atom.trip.dto;

import com.agiledge.atom.dto.APLDto;

public class AplDistanceDto {
 
	private String sourceId;
	private String targetId;
	private APLDto sourceAplDto;
	private APLDto targetAplDto;
	private double distance;
	
	public String getSourceId() {
		return sourceId;
	}
	public void setSourceId(String sourceId) {
		this.sourceId = sourceId;
	}
	public String getTargetId() {
		return targetId;
	}
	public void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	public APLDto getSourceAplDto() {
		return sourceAplDto;
	}
	public void setSourceAplDto(APLDto sourceAplDto) {
		this.sourceAplDto = sourceAplDto;
	}
	public APLDto getTargetAplDto() {
		return targetAplDto;
	}
	public void setTargetAplDto(APLDto targetAplDto) {
		this.targetAplDto = targetAplDto;
	}
	public double getDistance() {
		return distance;
	}
	public void setDistance(double distance) {
		this.distance = distance;
	}
	
}
