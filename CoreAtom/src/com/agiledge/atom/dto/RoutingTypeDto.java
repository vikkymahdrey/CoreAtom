package com.agiledge.atom.dto;

public class RoutingTypeDto {
	
	private String id;
	private String name;
	public RoutingTypeDto() {
		
		
	}
	
	public RoutingTypeDto(String name, String id) {
		this.name=name;
		this.id = id;
		
	}
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}

}
