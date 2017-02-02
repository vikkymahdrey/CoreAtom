package com.agiledge.atom.dto;

import java.util.HashMap;

public class AplTree {

	public static final String AREA = "AREA";
	public static final String PLACE = "PLACE";
	public static final String LANDMARK = "LANDMARK";
	
	public AplTree() {
		
	}
	
	public AplTree(String name) {
		label = name;
	}
	
	private String label;
	private HashMap<String, AplTree> map = new HashMap<String, AplTree>();
	
 
	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}
	
	private APLDto aplDto;

	 
 
 	public HashMap<String, AplTree> getMap() {
		return map;
	}

	public void setMap(HashMap<String, AplTree> map) {
		this.map = map;
	}

	public APLDto getAplDto() {
		return aplDto;
	}

	public void setAplDto(APLDto aplDto) {
		this.aplDto = aplDto;
	}
	
	 
}
