package com.agiledge.atom.dashboard.service;

import java.util.ArrayList;

import com.agiledge.atom.dashboard.dao.LiveTrackingDao;
import com.agiledge.atom.dashboard.dto.LiveTrackingDto;
import com.agiledge.atom.dto.LogTimeDto;

public class LiveTrackingService {
	public ArrayList<LogTimeDto> getLast3ShiftTimes()
	{
		return new LiveTrackingDao().getLast3ShiftTimes();
	}
	
	public String[] getEmployeeValues(String triptime,String log)
	{
		return new LiveTrackingDao().getEmployeeValues(triptime, log);
	}

	public double getPercentage(String total,String value)
	{
		double percentage=0.0;
		if(value!=null&&total!=null){
		percentage=Double.parseDouble(value)/Double.parseDouble(total);
		percentage=percentage*100;
		}
		return percentage;
	}
	
	public String[] getTripReport(String shift,String log)
	{
		return new LiveTrackingDao().getTripReport(shift, log);
	}
	public ArrayList<LiveTrackingDto> getNext3ShiftTimes()
	{
		return new LiveTrackingDao().getNext3ShiftTimes();
	}

	public LiveTrackingDto getAdhocDetails(String triptime,String log,String date)
	{
		return new LiveTrackingDao().getAdhocDetails(triptime,log,date);
	}
	
	public LiveTrackingDto currentShiftDetails()
	{
		return new LiveTrackingDao().currentShiftDetails();
	}
	public ArrayList<LiveTrackingDto> getLastInandOut()
	{
		return new LiveTrackingDao().getLastInandOut();
	}
	
	public LiveTrackingDto getLastInOutDetails(String log)
	{
		return new LiveTrackingDao().getLastInOutDetails(log);
	}
	public LiveTrackingDto getVehicleStatus(String triptime,String log,String date)
	{
		return new LiveTrackingDao().getVehicleStatus(triptime,log,date);
	}
	public ArrayList<LiveTrackingDto> getEmployeeStatus(String filter,String triptime,String log)
	{
		return new LiveTrackingDao().getEmployeeStatus(filter,triptime,log);
	}
	public ArrayList<LiveTrackingDto> getTripStatus(String date,String triptime,String log)
	{
		return new LiveTrackingDao().getTripStatus(date,triptime,log);
	}
	
}
