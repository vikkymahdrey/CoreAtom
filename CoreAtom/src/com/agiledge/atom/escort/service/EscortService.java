package com.agiledge.atom.escort.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.EscortDao;
import com.agiledge.atom.dto.EscortDto;

public class EscortService {

	private String errorMessage = "";
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
	public boolean validateAddEscort(EscortDto dto) {
		boolean flag=true;
		try{ 
			System.out.println(""+dto.getName());
			System.out.println(""+dto.getId());
			System.out.println(""+dto.getEscortClock());
			System.out.println(""+dto.getEmail());
			System.out.println(""+dto.getPhone());
			System.out.println(""+dto.getAddress());
			System.out.println(""+dto.getPassword());
			System.out.println(""+dto.getSite());
		if(dto.getName().equals("")) {
			errorMessage="Name field is empty";
			flag=false;
		}
		else if(dto.getEscortClock().equals("")) {
			errorMessage="Escort# field is empty";
			flag=false;
		}
		else if(dto.getAddress().equals("")) {
			errorMessage="Address field is empty";
			flag=false;
		}
		
		else if(dto.getEmail().equals("")) {
			errorMessage="Email field is empty";
			flag=false;
		}
		else if(dto.getSite().equals("")) {
			errorMessage="Site field is empty";
			flag=false;
		}
		}catch(Exception ex) {
			System.out.println("error in invalidate");
			errorMessage= ex.getMessage();
			flag=false;
		}
		return flag;
	}
	public int addEscort(EscortDto escortDtoObj, String changedBy) {
		// TODO Auto-generated method stub
		EscortDao escortDao = new EscortDao();
		int val=0;
		val = escortDao.addEscort(escortDtoObj, changedBy);
		errorMessage = escortDao.getMessage();
		return val;
		
	}
	public int updateEscort(EscortDto escortDtoObj) {
		// TODO Auto-generated method stub
		return  new EscortDao().updateEscort(escortDtoObj);
	}
	
	public ArrayList<EscortDto> getAllEscorts(String site) {
		return new EscortDao().getAllEscorts(site);
	}

	public int  assignTripEscort(ArrayList<EscortDto> escortList) {
		// TODO Auto-generated method stub
			return new EscortDao().assignTripEscort(escortList);
	}
	public int alterEscortStatus(String tripId,String status)
	{
		return new  EscortDao().alterEscortStatus(tripId, status);
	}
	

	
}
