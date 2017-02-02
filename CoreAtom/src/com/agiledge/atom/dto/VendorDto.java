package com.agiledge.atom.dto;

public class VendorDto {

	private String id;
	private String name;
	private String loginId;
	private String password;
	private String address;
	private String email;
	private String contactNumber;
	private String doneBy;
	private String supervisor;
	private String company;
	private String companyId;
	private String companyAddress;
	private String companycontact;
	private String companycontact1;

	public String getCompany() {
		return company;
	}

	public void setCompany(String company) {
		this.company = company;
	}

	public String getCompanyAddress() {
		return companyAddress;
	}

	public void setCompanyAddress(String companyAddress) {
		this.companyAddress = companyAddress;
	}

	public String getCompanycontact() {
		return companycontact;
	}

	public void setCompanycontact(String companycontact) {
		this.companycontact = companycontact;
	}

	public String getCompanycontact1() {
		return companycontact1;
	}

	public void setCompanycontact1(String companycontact1) {
		this.companycontact1 = companycontact1;
	}

	public String getDoneBy() {
		return doneBy;
	}

	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}

	boolean noPasswordUpdate;
	String status;
	VendorContractDto contract;

	public VendorContractDto getContract() {
		return contract;
	}

	public void setContract(VendorContractDto contract) {
		this.contract = contract;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public boolean isNoPasswordUpdate() {
		return noPasswordUpdate;
	}

	public void setNoPasswordUpdate(boolean noPasswordUpdate) {
		this.noPasswordUpdate = noPasswordUpdate;
	}

	public String getContactNumber() {
		return contactNumber;
	}

	public void setContactNumber(String contactNumber) {
		this.contactNumber = contactNumber;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
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

	public String getLoginId() {
		return loginId;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSupervisor() {
		return supervisor;
	}

	public void setSupervisor(String supervisor) {
		this.supervisor = supervisor;
	}

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	 

}
