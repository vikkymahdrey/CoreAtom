package com.agiledge.atom.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.VehicleTypeDto;

public class VehicleTypeService {
	private String errorMessage;
	public ArrayList<VehicleTypeDto> getAllVehicleTypeBySite(String siteId) throws SQLException {
		
		return new VehicleTypeDao().getAllVehicleTypeBySite(Integer.parseInt(siteId));
		
		 
	}
	public int addRateForVehicleType(VehicleTypeDto dto) throws SQLException {
		return new VehicleTypeDao().addRateForVehicleType(dto);
		 
	}
	public ArrayList<VehicleTypeDto> getVehicleTypeRateHistory( )
	{
		
		return new VehicleTypeDao().getVehicleTypeRateHistory( );
	}
	
	public boolean validateTripRateData(VehicleTypeDto dto)
	{
		boolean flag=true;
		if(dto.getSite()==null||dto.getSite().equals(""))
		{
			setErrorMessage("Site Is Invalid");
			flag=false;
		}else if(dto.getType()==null||dto.getType().equals(""))
		{
			flag=false;
			setErrorMessage("Vehicle Type Is Invalid");
		}else if(dto.getFromDate()==null||dto.getFromDate().equalsIgnoreCase("")||OtherFunctions.isNormalDate(dto.getFromDate())==false)
		{
			setErrorMessage("Effective Date Is Invalid");
			flag=false;
		}
		
		else if( new VehicleTypeDao().checkTripRateDateOverlapping(dto))
		{
			setErrorMessage("Date Overlapping With Previouse dates");
			flag=false;
		}
		
		else if( dto.getRatePerTrip()==0)
		{
			setErrorMessage("Rate Per Trip Is Invalid");
			flag=false;
		}
		
		return flag;
	}
	public String getErrorMessage() {
		return errorMessage;
	}
	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}
}
