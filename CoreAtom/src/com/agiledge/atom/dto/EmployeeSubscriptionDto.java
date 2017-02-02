/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dto;

/**
 * 
 * @author 123
 */
public class EmployeeSubscriptionDto {

	// change by noufal

	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	


	public int getTransAdminId() {
		return transAdminId;
	}

	public void setTransAdminId(int transAdminId) {
		this.transAdminId = transAdminId;
	}
	private String inRoute;
	private String outRoute;
	
	EmployeeDto supervisor = new EmployeeDto();
	EmployeeDto spoc = new EmployeeDto();
	EmployeeDto employee = new EmployeeDto();
	String source="Employee";
	int transAdminId=0;
	String subscriptionID;
	String landMarkName;
	String site;
	private String contactNo;
	private String empType;
	private String scheduleStatus;
	private String empAddress;	
	private String shiftIn;
	private String shiftOut;
	private String approvalStatus;
	private int waitingList;
	private int waitingListDays;
	private String approver;
	private String routeName;
	private String IN_OUT;
	private String reConfirmDate;
	private String EffectiveFrom;
	


	
	public String getShiftIn() {
		return shiftIn;
	}

	public void setShiftIn(String shiftIn) {
		this.shiftIn = shiftIn;
	}

	public String getShiftOut() {
		return shiftOut;
	}

	public void setShiftOut(String shiftOut) {
		this.shiftOut = shiftOut;
	}

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getLandMarkName() {
		return landMarkName;
	}

	public void setLandMarkName(String landMarkName) {
		this.landMarkName = landMarkName;
	}

	public String getSubscriptionID() {
		return subscriptionID;
	}

	public void setSubscriptionID(String subscriptionID) {
		this.subscriptionID = subscriptionID;
	}

	public EmployeeDto getEmployee() {
		return employee;
	}

	public void setEmployee(EmployeeDto employee) {
		this.employee = employee;
	}

	public EmployeeDto getSpoc() {
		return spoc;
	}

	public void setSpoc(EmployeeDto spoc) {
		this.spoc = spoc;
	}

	public EmployeeDto getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(EmployeeDto supervisor) {
		this.supervisor = supervisor;
	}

	// change by noufal ends

	String isContractEmployee;

	public String getIsContractEmployee() {
		return isContractEmployee;
	}

	public void setIsContractEmployee(String isContractEmployee) {
		this.isContractEmployee = isContractEmployee;
	}

	String employeeID;
	String supervisor1;
	String supervisor2;
	APLDto apl = new APLDto();

	String subscriptionStatus;
	String landMark;
	String subscriptionDate;
	String unsubscriptionDate;
	String unsubscriptionFromDate;
	String subscriptionFromDate;
	String doneBy;
	
	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	public APLDto getApl() {
		return apl;
	}

	public void setApl(APLDto apl) {
		this.apl = apl;
	}

	public String getSubscriptionFromDate() {
		return subscriptionFromDate;
	}

	public void setSubscriptionFromDate(String subscriptionFromDate) {
		this.subscriptionFromDate = subscriptionFromDate;
	}

	public void setEffectiveFrom(String EffectiveFrom)
	{
		this.EffectiveFrom = EffectiveFrom;
		
	}
	
	public String getEffectiveFrom()
	{
		return EffectiveFrom;
	}
	public String getSupervisor2() {
		return supervisor2;
	}

	public void setUnsubscriptionDate(String unsubscriptionDate) {
		this.unsubscriptionDate = unsubscriptionDate;
	}

	public void setUnsubscriptionFromDate(String unsubscriptionFromDate) {
		this.unsubscriptionFromDate = unsubscriptionFromDate;
	}

	public String getUnsubscriptionDate() {
		return unsubscriptionDate;
	}

	public String getUnsubscriptionFromDate() {
		return unsubscriptionFromDate;
	}

	public enum status {
		SUBSCRIBED {
			@Override
			public String toString() {
				return "subscribed";
			}

		},
		UNSUBSCRIBED {
			@Override
			public String toString() {
				return "unsubscribed";
			}
		},
		PENDING {
			@Override
			public String toString() {
				return "pending";
			}
		}
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmployeeID() {
		return this.employeeID;
	}

	public void setSupervisor1(String supervisor1) {
		this.supervisor1 = supervisor1;

	}

	public String getSupervisor1() {
		return this.supervisor1;
	}

	public void setSupervisor2(String supervisor2) {
		this.supervisor2 = supervisor2;
	}

	public void setSubscriptionStatus(String subscriptionStatus) {
		this.subscriptionStatus = subscriptionStatus;
	}

	public String getSubscriptionStatus() {
		return this.subscriptionStatus;
	}

	public void setLandMark(String landMark) {
		this.landMark = landMark;
	}

	public String getLandMark() {
		return this.landMark;
	}

	public void setSubscriptionDate(String subscriptionDate) {
		this.subscriptionDate = subscriptionDate;
	}

	public String getSubscriptionDate() {
		return this.subscriptionDate;
	}

	public String getScheduleStatus() {
		if (scheduleStatus == null)
			return "Not Scheduled";
		else
			return scheduleStatus;
	}

	public void setScheduleStatus(String scheduleStatus) {
		this.scheduleStatus = scheduleStatus;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String empType) {
		this.empType = empType;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmpAddress() {
		return empAddress;
	}

	public void setEmpAddress(String empAddress) {
		this.empAddress = empAddress;
	}
	
	

	public String getInRoute() {
		return inRoute;
	}

	public void setInRoute(String inRoute) {
		this.inRoute = inRoute;
	}

	public String getOutRoute() {
		return outRoute;
	}

	public void setOutRoute(String outRoute) {
		this.outRoute = outRoute;
	}

	public String getApprover() {
		return approver;
	}

	public void setApprover(String approver) {
		this.approver = approver;
	}

	public String getApprovalStatus() {
		return approvalStatus;
	}

	public void setApprovalStatus(String approvalStatus) {
		this.approvalStatus = approvalStatus;
	}

	public String getRouteName() {
		return routeName;
	}

	public void setRouteName(String routeName) {
		this.routeName = routeName;
	}

	public String getIN_OUT() {
		return IN_OUT;
	}

	public void setIN_OUT(String iN_OUT) {
		IN_OUT = iN_OUT;
	}

	public String getReConfirmDate() {
		return reConfirmDate;
	}

	public void setReConfirmDate(String reConfirmDate) {
		this.reConfirmDate = reConfirmDate;
	}

	public int getWaitingList() {
		return waitingList;
	}

	public void setWaitingList(int waitingList) {
		this.waitingList = waitingList;
	}

	public int getWaitingListDays() {
		return waitingListDays;
	}

	public void setWaitingListDays(int waitingListDays) {
		this.waitingListDays = waitingListDays;
	}



}
