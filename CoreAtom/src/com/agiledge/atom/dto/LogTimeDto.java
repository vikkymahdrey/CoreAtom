/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dto;

/**
 * 
 * @author Administrator
 */

public class LogTimeDto {
	private String logTime;
	private int id;
	private String status;
	private String logtype;
	private int projects;
	private String doneBy;
	private String shiftType;
	private String activeStatus;
	public String getDoneBy() {
		return doneBy;
	}



	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	/**
	 * @return
	 */
	public int getId() {
		return id;
	}

	 

	public void setId(int id) {
		this.id = id;
	}

	public String getLogTime() {
		return logTime;
	}

	public void setLogTime(String logTime) {
		this.logTime = logTime;
	}

	public String getLogtype() {
		return logtype;
	}

	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}



	public int getProjects() {
		return projects;
	}



	public void setProjects(int projects) {
		this.projects = projects;
	}



	public String getShiftType() {
		return shiftType;
	}



	public void setShiftType(String shiftType) {
		this.shiftType = shiftType;
	}



	public String getActiveStatus() {
		return activeStatus;
	}



	public void setActiveStatus(String activeStatus) {
		this.activeStatus = activeStatus;
	}
}
