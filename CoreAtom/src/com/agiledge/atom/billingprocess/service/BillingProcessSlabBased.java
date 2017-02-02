package com.agiledge.atom.billingprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingprocess.dao.BillingProcessSlabBasedDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessSlabBasedDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingtype.config.dto.BillingSlabDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class BillingProcessSlabBased implements BillingProcess {

	BillingProcessSlabBasedDao dao = new BillingProcessSlabBasedDaoImpl();
	SlabBasedBillingConfigDto dto;
	 

	@Override
	public boolean getValues(String refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		boolean returnFlag = false;
		try {
		 dto = dao.getSlabBillingConfigFromBillingId(refId, vehicleTypeId);
		 if(dto!=null) {
			 returnFlag = true;
		 }
		  
	}catch(Exception e) {
			  
		 }
		return returnFlag;
	}
	
	
	private float getTripRate(TripDetailsDto tripDto ) {
		
		float tripRate = Float.parseFloat( dto.getTripRate());
		ArrayList<BillingSlabDto> slabs = dto.getSlabList();
		 String shiftTime = tripDto.getTrip_time();
		
		 for(BillingSlabDto slab : slabs) {
			 if(shiftTime.compareTo(slab.getStartTime())>=0 && shiftTime.compareTo(slab.getEndTime())<=0 ) {
				 tripRate = Float.parseFloat(slab.getRate());
				 break;
			 }
		 }
		return  tripRate;
	}


	@Override
	public Map<String, String> calculateValues(TripDetailsDto tripDto) {
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			float totalRate = getTripRate(tripDto);
			float escortRate = 0f;
			 
			 
			  
			 
			if(tripDto.getEscort().equalsIgnoreCase("YES")) {
				if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.FLAT)) {
					escortRate =(float) ( Float.parseFloat(dto.getEscortRate()));
					 
				} else {
					escortRate =(float) ( Float.parseFloat(dto.getEscortRate()) * totalRate / 100);
				}
			}
			 
			String type="slab";
		 	map.put(type+"TripBillingId", dto.getId());
			map.put(type+"TripRate", String.valueOf( totalRate));
			map.put(type+"EscortRate", String.valueOf(escortRate));
		}catch(Exception e) {
			System.out.println("Error in BiillingProcessSlabBased : "+ e);
		}
		return map;
		 

	}

}
