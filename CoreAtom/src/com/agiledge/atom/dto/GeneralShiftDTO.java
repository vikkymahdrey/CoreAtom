package com.agiledge.atom.dto;

import java.util.ArrayList;

public class GeneralShiftDTO {
	private String deductionid;
	private String deductiontype;
	private String approval_req;
	private String approved_by;
	private String cutoffdays;
	private String deduction;
	private String deduction_amt;
	private String waitlist_reconf;
	private String waitlist_cutoffdays;
	private String cancelcutoff;
	private String site_id;
	private String status;
	private String from_date;
	private String to_date;
	private String logtime;
	private String logtype;
	private String editable;
	private String conf_id;
	private String vehicleType;
	private String vehicleTypeId;
	private int count;
	private String vehicleSeat;
	private String routeId;
	private String bookingId;
	private String date;
	private String employeeName;
	private String employeeId;
	private ArrayList<TripDetailsChildDto> empDtoList;
	public String getVehicleType() {
		return vehicleType;
	}
	public void setVehicleType(String vehicleType) {
		this.vehicleType = vehicleType;
	}
	public String getVehicleTypeId() {
		return vehicleTypeId;
	}
	public void setVehicleTypeId(String vehicleTypeId) {
		this.vehicleTypeId = vehicleTypeId;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public String getConf_id() {
		return conf_id;
	}
	public void setConf_id(String conf_id) {
		this.conf_id = conf_id;
	}
	public String getEditable() {
		return editable;
	}
	public void setEditable(String editable) {
		this.editable = editable;
	}
	public String getLogtime() {
		return logtime;
	}
	public void setLogtime(String logtime) {
		this.logtime = logtime;
	}
	public String getLogtype() {
		return logtype;
	}
	public void setLogtype(String logtype) {
		this.logtype = logtype;
	}
	public String getApproval_req() {
		return approval_req;
	}
	public void setApproval_req(String approval_req) {
		this.approval_req = approval_req;
	}
	public String getApproved_by() {
		return approved_by;
	}
	public void setApproved_by(String approved_by) {
		this.approved_by = approved_by;
	}
	public String getCutoffdays() {
		return cutoffdays;
	}
	public void setCutoffdays(String cutoffdays) {
		this.cutoffdays = cutoffdays;
	}
	public String getDeduction() {
		return deduction;
	}
	public void setDeduction(String deduction) {
		this.deduction = deduction;
	}
	public String getDeduction_amt() {
		return deduction_amt;
	}
	public void setDeduction_amt(String deduction_amt) {
		this.deduction_amt = deduction_amt;
	}
	public String getWaitlist_reconf() {
		return waitlist_reconf;
	}
	public void setWaitlist_reconf(String waitlist_reconf) {
		this.waitlist_reconf = waitlist_reconf;
	}
	public String getWaitlist_cutoffdays() {
		return waitlist_cutoffdays;
	}
	public void setWaitlist_cutoffdays(String waitlist_cutoffdays) {
		this.waitlist_cutoffdays = waitlist_cutoffdays;
	}
	public String getCancelcutoff() {
		return cancelcutoff;
	}
	public void setCancelcutoff(String cancelcutoff) {
		this.cancelcutoff = cancelcutoff;
	}
	public String getSite_id() {
		return site_id;
	}
	public void setSite_id(String site_id) {
		this.site_id = site_id;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
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
	public String getDeductionid() {
		return deductionid;
	}
	public void setDeductionid(String deductionid) {
		this.deductionid = deductionid;
	}
	public String getDeductiontype() {
		return deductiontype;
	}
	public void setDeductiontype(String deductiontype) {
		this.deductiontype = deductiontype;
	}
	public String getVehicleSeat() {
		return vehicleSeat;
	}
	public void setVehicleSeat(String vehicleSeat) {
		this.vehicleSeat = vehicleSeat;
	}
	public ArrayList<TripDetailsChildDto> getEmpDtoList() {
		return empDtoList;
	}
	public void setEmpDtoList(ArrayList<TripDetailsChildDto> empDtoList) {
		this.empDtoList = empDtoList;
	}
	public String getRouteId() {
		return routeId;
	}
	public void setRouteId(String routeId) {
		this.routeId = routeId;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	public String getBookingId() {
		return bookingId;
	}
	public void setBookingId(String bookingId) {
		this.bookingId = bookingId;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeeId() {
		return employeeId;
	}
	public void setEmployeeId(String employeeId) {
		this.employeeId = employeeId;
	}

}
