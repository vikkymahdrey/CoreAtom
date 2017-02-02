package com.agiledge.atom.service;

import java.util.List;

import com.agiledge.atom.dao.StsDao;
import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class StsService {

	public int addStsEmployee(EmployeeDto dto) {
		// TODO Auto-generated method stub
		return new StsDao().addStsEmployee(dto);
	}
	public List<EmergencyDto> getStsApprovedEmployees(){
		
		return new StsDao().getStsApprovedEmployees();
	}
	public EmergencyDto getStsApprovedTrip(String id){
		return new StsDao().getStsApprovedTrip(id);
	}
	public int approveStsRequest(String id,String status,String approvedBy){
		return new StsDao().approveStsRequest(id,status,approvedBy);
	}
	public int stsTripDriverAssaign(TripDetailsDto dto){
		return new StsDao().stsTripDriverAssaign(dto);
	}
	public List<EmergencyDto> getStsEmployeesForApproval(){
		return new StsDao().getStsEmployeesForApproval();
	}
	public List<EmergencyDto> getStsEmployeesForApprovalUnderTL(String empid){
		return new StsDao().getStsEmployeesForApprovalUnderTL(empid);
	}
	public int thirdPartyAssaign(String tripid, String approvedby,String thirdParty) {
		return new StsDao().thirdPartyAssaign(tripid,approvedby,thirdParty);
	}
}
