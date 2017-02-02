package com.agiledge.atom.dto;

import java.util.ArrayList;

public class DayReportDto {
private String date;
private String site;
private ArrayList<ScheduledEmpDto> empScheduleList;

public String getDate() {
	return date;
}

public void setDate(String date) {
	this.date = date;
}

public String getSite() {
	return site;
}

public void setSite(String site) {
	this.site = site;
}

public ArrayList<ScheduledEmpDto> getEmpScheduleList() {
	return empScheduleList;
}

public void setEmpScheduleList(ArrayList<ScheduledEmpDto> empScheduleList) {
	this.empScheduleList = empScheduleList;
}
}
