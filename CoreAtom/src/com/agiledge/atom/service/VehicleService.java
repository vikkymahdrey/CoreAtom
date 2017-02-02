package com.agiledge.atom.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.DriverDAO;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.DriverVehicleDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.dto.VehicleTypeDto;



public class VehicleService {
	private String errorMessage;
	public ArrayList<VehicleTypeDto> getAllVehicleTypeBySite(String siteId) {
		
		return new VehicleTypeDao().getAllVehicleTypeBySite(Integer.parseInt(siteId));
		
		 
	}
	public ArrayList<VehicleTypeDto> setVehicleTypeCount(String siteId)
	{
		ArrayList<VehicleTypeDto> dtos=getAllVehicleTypeBySite(siteId);	
		for(VehicleTypeDto typeDto:dtos)
		{
			typeDto.setCount(999);
		}
	return dtos;	
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
	public void AssignDriverToVehicle(ArrayList<VehicleTypeDto> dtos)
	{
		
	}
	public ArrayList<DriverVehicleDto> getVehicleDetails(String vehicleId)
	{
	return new VehicleDao().getVehicle(vehicleId);	
	}
	public ArrayList<DriverDto> getDrivers(String vehicleId)
	{
	return new DriverDAO().getDrivers(vehicleId);	
	}
	public int addDriverVehicle(String vehicle, String[] vehicleDrivers) {
		return new VehicleDao().addDriverVehicle(vehicle,vehicleDrivers);
		
	}
	public ArrayList<VehicleDto> getVehicleTrackInInterval(String tripId) {
		return new VehicleDao().getVehicleTrackInInterval(tripId);
	}
	
	public int UpdateVehicleStatus(String id,String status) {
		return new VehicleDao().UpdateVehicleStatus(id, status);
	}
	//sandesh
	public ArrayList<VehicleDto> getAllVehicle() {
		return new VehicleDao().getAllVehicle();
	}
	//sandesh
	public int mapVehicleWithShift(String siteId, String vehicle, String[] inT,
			String[] outT, String routeIn, String routeOut) {
		return new VehicleDao().mapVehicleWithShift(siteId,vehicle,inT,outT,routeIn,routeOut);
	}
}
