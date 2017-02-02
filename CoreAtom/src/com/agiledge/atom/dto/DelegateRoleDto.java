package com.agiledge.atom.dto;

public class DelegateRoleDto {

	private String actualEmpId;
	private String delegatedEmpId;
	private String fromDate;
	private String toDate;
	private String employeeName;
	private String employeePersonnelNo;
	private String delegatedemployeeName;
	private String delegatedemployeePersonnelNo;
    private String doneBy;
    private int Id;
	public int getId() {
		return Id;
	}
	public void setId(int Id) {
		this.Id = Id;
	}
	public String getDoneBy() {
		return doneBy;
	}
	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}
	public String getEmployeeName() {
		return employeeName;
	}
	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}
	public String getEmployeePersonnelNo() {
		return employeePersonnelNo;
	}
	public void setEmployeePersonnelNo(String employeePersonnelNo) {
		this.employeePersonnelNo = employeePersonnelNo;
	}
	public String getDelegatedemployeeName() {
		return delegatedemployeeName;
	}
	public void setDelegatedemployeeName(String delegatedemployeeName) {
		this.delegatedemployeeName = delegatedemployeeName;
	}
	public String getDelegatedemployeePersonnelNo() {
		return delegatedemployeePersonnelNo;
	}
	public void setDelegatedemployeePersonnelNo(String delegatedemployeePersonnelNo) {
		this.delegatedemployeePersonnelNo = delegatedemployeePersonnelNo;
	}
	public String getActualEmpId() {
		return actualEmpId;
	}
	public void setActualEmpId(String actualEmpId) {
		this.actualEmpId = actualEmpId;
	}
	public String getDelegatedEmpId() {
		return delegatedEmpId;
	}
	public void setDelegatedEmpId(String delegatedEmpId) {
		this.delegatedEmpId = delegatedEmpId;
	}
	public String getFromDate() {
		return fromDate;
	}
	public void setFromDate(String fromDate) {
		this.fromDate = fromDate;
	}
	public String getToDate() {
		return toDate;
	}
	public void setToDate(String toDate) {
		this.toDate = toDate;
	}
}
