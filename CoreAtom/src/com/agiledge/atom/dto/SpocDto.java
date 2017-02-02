package com.agiledge.atom.dto;

public class SpocDto {
	private String spocName;
	private String spoc_id;
	private String employee_id;
	private String from_date;
	private String to_date;
	private String status;
	private String pers_no;
	public String getPers_no() {
		return pers_no;
	}
	public void setPers_no(String pers_no) {
		this.pers_no = pers_no;
	}
	public String getSpocName() {
		return spocName;
	}
	public void setSpocName(String spocName) {
		this.spocName = spocName;
	}
	public String getSpoc_id() {
		return spoc_id;
	}
	public void setSpoc_id(String spoc_id) {
		this.spoc_id = spoc_id;
	}
	public String getEmployee_id() {
		return employee_id;
	}
	public void setEmployee_id(String employee_id) {
		this.employee_id = employee_id;
	}
	public String getFrom_date() {
		return from_date;
	}
	public void setFrom_date(String from_date) {
		this.from_date = from_date;
	}
	public String getTo_date() {
		return to_date;
	}
	public void setTo_date(String to_date) {
		this.to_date = to_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}

}
