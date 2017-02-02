/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dto;

/**
 * 
 * @author 123
 */
public class EmployeeDto {

	private String userType;
	private String addressChangeDate;
	private String displayName;
	private String routingType;
	private String password;
	private String authtype;
	private String pickUpTime;
	private String contract;
	private String passwordchangedate;
	private String externaluser;
	private int roleId;
	private String spoc_id;
	private String spocName;
	private String keyPin;
	private String projectUnit;
	private String managerName;
	private String projectCode;
	private String project;
	private String date;
	
	private String login;
	private String logout;
	private String inroute;
	private String outroute;


	private String pickup;
	private String drop;
	private String APL;

	private String lattitude;
	private String longitude;
	public String getOutroute() {
		return outroute;
	}

	public void setOutroute(String outroute) {
		this.outroute = outroute;
	}
	public String getPickup() {
		return pickup;
	}

	public void setPickup(String pickup) {
		this.pickup = pickup;
	}

	public String getDrop() {
		return drop;
	}

	public void setDrop(String drop) {
		this.drop = drop;
	}

	public String getAPL() {
		return APL;
	}

	public void setAPL(String aPL) {
		APL = aPL;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getLogout() {
		return logout;
	}

	public void setLogout(String logout) {
		this.logout = logout;
	}

	public String getinroute() {
		return inroute;
	}

	public void setinroute(String route) {
		this.inroute = route;
	}

	public String getProjectCode() {
		return projectCode;
	}

	public void setProjectCode(String projectCode) {
		this.projectCode = projectCode;
	}

	public String getProject() {
		return project;
	}

	public void setProject(String project) {
		this.project = project;
	}

	private String employeeFirstName;
	private String employeeMiddleName;
	private String employeeLastName;
	private String loginId;
	private String gender;
	private String personnelNo;
	private String emailAddress;
	private String homeCountry;
	private String lineManager;
	private String staffManager;
	private String clientServiceManager;
	private String careerLevel;
	private String careerPathwayDesc;
	private String businessUnitCode;
	private String businessUnitDescription;
	private String deptno;
	private String deptName;
	private String operationCode;
	private String operationDescription;
	private String pathways;
	private String dateOfJoining;
	private String active;
	private String address;
	private String site;
	private String city;
	private String contactNo;
	private String empType;
	private String projectid;
	private String registerStatus;
	private String DesignationName;
	private String state;
	private String noshowcount;
	private String noshowdays;
	
	public String getNoshowcount() {
		return noshowcount;
	}

	public void setNoshowcount(String noshowcount) {
		this.noshowcount = noshowcount;
	}

	public String getNoshowdays() {
		return noshowdays;
	}

	public void setNoshowdays(String noshowdays) {
		this.noshowdays = noshowdays;
	}

	public String getSpoc_id() {
		return spoc_id;
	}

	public void setSpoc_id(String spoc_id) {
		this.spoc_id = spoc_id;
	}
	public String getExternaluser() {
		return externaluser;
	}

	public void setExternaluser(String externaluser) {
		this.externaluser = externaluser;
	}

	public String getPasswordchangedate() {
		return passwordchangedate;
	}

	public void setPasswordchangedate(String passwordchangedate) {
		this.passwordchangedate = passwordchangedate;
	}

	public String getContract() {
		return contract;
	}

	public void setContract(String contract) {
		this.contract = contract;
	}

	public String getAuthtype() {
		return authtype;
	}

	public void setAuthtype(String authtype) {
		this.authtype = authtype;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getActive() {
		return active;
	}

	public String getBusinessUnitCode() {
		if (businessUnitCode == null) {
			return "";
		} else {
			return businessUnitCode;
		}

	}

	public String getBusinessUnitDescription() {
		if (businessUnitDescription == null) {
			return "";
		} else {
			return businessUnitDescription;
		}
	}

	public String getCareerLevel() {
		if (careerLevel == null) {
			return "";
		} else {
			return careerLevel;
		}
	}

	public String getCareerPathwayDesc() {
		if (careerPathwayDesc == null) {
			return "";
		} else {
			return careerPathwayDesc;
		}
	}

	public String getClientServiceManager() {
		if (clientServiceManager == null) {
			return "";
		} else {
			return clientServiceManager;
		}
	}

	public String getDateOfJoining() {
		if (dateOfJoining != null&&dateOfJoining.equals("")) {
		return null;
		} else {
			return dateOfJoining;
		}
	}

	public String getDeptName() {
		if (deptName == null) {
			return "";
		} else {
			return deptName;
		}
	}

	public String getDeptno() {
		if (deptno == null) {
			return "";
		} else {
			return deptno;
		}
	}

	public String getEmailAddress() {

		if (emailAddress == null) {
			return "";
		} else {
			return emailAddress;
		}
	}

	public String getEmployeeFirstName() {
		if (employeeFirstName == null) {
			return "";
		} else {
			return employeeFirstName;
		}
	}

	public String getEmployeeLastName() {
		if (employeeLastName == null) {
			return "";
		} else {
			return employeeLastName;
		}
	}

	public String getEmployeeMiddleName() {
		if (employeeMiddleName == null) {
			return "";
		} else {
			return employeeMiddleName;
		}
	}

	public String getGender() {
		if (gender == null) {
			return "";
		} else {
			return gender;
		}
	}

	public String getHomeCountry() {
		if (homeCountry == null) {
			return "";
		} else {
			return homeCountry;
		}
	}

	public String getLineManager() {
		if (lineManager == null) {
			return "";
		} else {
			return lineManager;
		}
	}

	public String getLoginId() {
		return loginId;
	}

	public String getOperationCode() {
		if (operationCode == null) {
			return "";
		} else {
			return operationCode;
		}
	}

	public String getOperationDescription() {
		if (operationDescription == null) {
			return "";
		} else {
			return operationDescription;
		}
	}

	public String getPathways() {
		if (pathways == null) {
			return "";
		} else {
			return pathways;
		}
	}

	public String getPersonnelNo() {
		return personnelNo;
	}

	public String getStaffManager() {
		if (staffManager == null) {
			return "";
		} else {
			return staffManager;
		}
	}

	public void setActive(String active) {
		this.active = active;
	}

	public void setBusinessUnitCode(String businessUnitCode) {
		this.businessUnitCode = businessUnitCode;
	}

	public void setBusinessUnitDescription(String businessUnitDescription) {
		this.businessUnitDescription = businessUnitDescription;
	}

	public void setCareerLevel(String careerLevel) {
		this.careerLevel = careerLevel;
	}

	public void setCareerPathwayDesc(String careerPathwayDesc) {
		this.careerPathwayDesc = careerPathwayDesc;
	}

	public void setClientServiceManager(String clientServiceManager) {
		this.clientServiceManager = clientServiceManager;
	}

	public void setDateOfJoining(String dateOfJoining) {
		
		this.dateOfJoining = dateOfJoining;		
	}

	public void setDeptName(String deptName) {
		this.deptName = deptName;
	}

	public void setDeptno(String deptno) {
		this.deptno = deptno;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setEmployeeFirstName(String employeeFirstName) {
		this.employeeFirstName = employeeFirstName;
	}

	public void setEmployeeLastName(String employeeLastName) {
		this.employeeLastName = employeeLastName;
	}

	public void setEmployeeMiddleName(String employeeMiddleName) {
		this.employeeMiddleName = employeeMiddleName;
	}

	public void setGender(String gender) {
		this.gender = gender;
		if(gender!=null && gender.length()>1)
		this.gender = gender.substring(0,1);
			
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public void setLineManager(String lineManager) {
		this.lineManager = lineManager;
	}

	public void setLoginId(String loginId) {
		this.loginId = loginId;
	}

	public void setOperationCode(String operationCode) {
		this.operationCode = operationCode;
	}

	public void setOperationDescription(String operationDescription) {
		this.operationDescription = operationDescription;
	}

	public void setPathways(String pathways) {
		this.pathways = pathways;
	}

	public void setPersonnelNo(String personnelNo) {
		try {
			this.personnelNo = "" + Long.parseLong(personnelNo);

		} catch (Exception e) {
			this.personnelNo = personnelNo;

		}
	}

	public void setStaffManager(String staffManager) {
		this.staffManager = staffManager;
	}


	public String getProjectid() {
		return projectid;
	}

	public void setProjectid(String projectid) {
		this.projectid = projectid;
	}



	public String getSite() {
		if (site == null) {
			return "";
		} else {
			return site;
		}
	}

	public void setSite(String site) {
		this.site = site;
	}

	public void setAddress(String address) {

		this.address = address;
	}

	public String getAddress() {
		if (address == null) {
			return "";
		} else {
			return address;
		}
	}

	private String employeeID;

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String EmployeeID) {
		this.employeeID = EmployeeID;
	}

	public String getUserType() {

		return userType;
	}

	public void setUserType(String userType) {
		this.userType = userType;
	}

	public String getCity() {
		if (city == null) {
			return "";
		} else {
			return city;
		}
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getContactNo() {
		return contactNo;
	}

	public void setContactNo(String contactNo) {
		this.contactNo = contactNo;
	}

	public String getEmpType() {
		return empType;
	}

	public void setEmpType(String emptype) {
		if (emptype == null) {
			this.empType = "";
		} else if (emptype.equals("permanent") || emptype.equals("on Roll")) {
			this.empType = "on Roll";
		} else {
			this.empType = "contract";
		}

	}

	public String getAddressChangeDate() {
		return addressChangeDate;
	}

	public void setAddressChangeDate(String addressChangeDate) {
		this.addressChangeDate = addressChangeDate;
	}

	public String getDisplayName() {
		return displayName;
	}

	public void setDisplayName(String displayName) {
		this.displayName = displayName;
	}

	public String getRoutingType() {
		return routingType;
	}

	public void setRoutingType(String routingType) {
		this.routingType = routingType;
	}

	
	@Override
	public String toString() {
		return "EmployeeDto [userType=" + userType + ", addressChangeDate="
				+ addressChangeDate + ", displayName=" + displayName
				+ ", routingType=" + routingType + ", password=" + password
				+ ", authtype=" + authtype + ", pickUpTime=" + pickUpTime
				+ ", contract=" + contract + ", passwordchangedate="
				+ passwordchangedate + ", externaluser=" + externaluser
				+ ", roleId=" + roleId + ", spoc_id=" + spoc_id + ", spocName="
				+ spocName + ", keyPin=" + keyPin + ", projectUnit="
				+ projectUnit + ", managerName=" + managerName
				+ ", employeeFirstName=" + employeeFirstName
				+ ", employeeMiddleName=" + employeeMiddleName
				+ ", employeeLastName=" + employeeLastName + ", loginId="
				+ loginId + ", gender=" + gender + ", personnelNo="
				+ personnelNo + ", emailAddress=" + emailAddress
				+ ", homeCountry=" + homeCountry + ", lineManager="
				+ lineManager + ", staffManager=" + staffManager
				+ ", clientServiceManager=" + clientServiceManager
				+ ", careerLevel=" + careerLevel + ", careerPathwayDesc="
				+ careerPathwayDesc + ", businessUnitCode=" + businessUnitCode
				+ ", businessUnitDescription=" + businessUnitDescription
				+ ", deptno=" + deptno + ", deptName=" + deptName
				+ ", operationCode=" + operationCode
				+ ", operationDescription=" + operationDescription
				+ ", pathways=" + pathways + ", dateOfJoining=" + dateOfJoining
				+ ", active=" + active + ", address=" + address + ", site="
				+ site + ", city=" + city + ", contactNo=" + contactNo
				+ ", empType=" + empType + ", projectid=" + projectid
				+ ", registerStatus=" + registerStatus + ", employeeID="
				+ employeeID + "]";
	}


	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getKeyPin() {
		return keyPin;
	}

	public void setKeyPin(String keyPin) {
		this.keyPin = keyPin;
	}

	public String getPickUpTime() {
		return pickUpTime;
	}

	public void setPickUpTime(String pickUpTime) {
		this.pickUpTime = pickUpTime;
	}

	public String getProjectUnit() {
		return projectUnit;
	}

	public void setProjectUnit(String projectUnit) {
		this.projectUnit = projectUnit;
	}

	public String getRegisterStatus() {
		return registerStatus;
	}

	public void setRegisterStatus(String registerStatus) {
		this.registerStatus = registerStatus;
	}

	public String getManagerName() {
		return managerName;
	}

	public void setManagerName(String managerName) {
		this.managerName = managerName;
	}

	public String getSpocName() {
		return spocName;
	}

	public void setSpocName(String spocName) {
		this.spocName = spocName;
	}

	public String getDesignationName() {
		return DesignationName;
	}

	public void setDesignationName(String designationName) {
		DesignationName = designationName;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getLattitude() {
		return lattitude;
	}

	public void setLattitude(String lattitude) {
		this.lattitude = lattitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getInroute() {
		return inroute;
	}

	public void setInroute(String inroute) {
		this.inroute = inroute;
	}
	private String zip;
	public String getZip() {
		return zip;
	}

	public void setZip(String zip) {
		this.zip = zip;
	}


}
