package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.ShuttleRoutingDao;
import com.agiledge.atom.dto.GeneralShiftDTO;

public class ShuttleRoutingService {
	
	public int shttleRouting(String siteId,String date, String inOut, String inOutTime) {
		if(new ShuttleRoutingDao().checkTripExist(siteId,date,inOut, inOutTime)!=0)
		{
		return -5;	
		}
		return new ShuttleRoutingDao().createTrip(siteId,date,inOut, inOutTime);
			
	}
	
	public ArrayList<GeneralShiftDTO> getShuttleTrip(String siteId,String date, String inOut, String inOutTime) {
	return new ShuttleRoutingDao().getShuttleTrip(siteId,date, inOut, inOutTime);
	}
}
