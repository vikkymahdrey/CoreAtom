package com.agiledge.atom.billingprocess.service;

import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedClassicDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedClassicDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class BillingProcessKmBasedClassic implements BillingProcess {

	BillingProcessKmBasedClassicDao dao = new BillingProcessKmBasedClassicDaoImpl();
	KmBasedClassicTripBillingConfigDto dto;
	@Override
	public boolean getValues(String refId, String vehicleTypeId) {
		boolean returnFlag = false;
		try {
			System.out.println(" INSIDE BillingProcessKmBasedClassic  vehicleTypeId " +vehicleTypeId );
		 dto = dao.getKmBasedClassicBillingConfigFromBillingId(refId, vehicleTypeId);
		 if(dto!=null) {
			 returnFlag = true;
		 }
		  
	}catch(Exception e) {
			  
		 }
		return returnFlag;
	}
	

	private float getTripRate(String shiftTime) {
		 
		float tripRate = Float.parseFloat( dto.getRate());
		 for(AcConstraintDto acDto : dto.getAcList()) {
			 
			  
			 if(shiftTime.compareTo(acDto.getFromTime())>=0 && shiftTime.compareTo(acDto.getToTime())<=0 ) {
				 tripRate = Float.parseFloat(acDto.getRate());
				 	 
				 break;
			 }
		 }
		return tripRate;
	}
	
	

	@Override
	public Map<String, String> calculateValues(TripDetailsDto tripDto) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		try {
		System.out.println("TripBased Calcul : distance "+tripDto );
		float distance = Float.parseFloat( tripDto.getDistance());
		
		float ratePerKm = getTripRate(tripDto.getTrip_time());
		float totalRate = distance * ratePerKm;
		float escortRate = 0f;  
		  
		if(tripDto.getEscort().equalsIgnoreCase("YES")) {
			if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.FLAT)) {
				escortRate = Float.parseFloat(dto.getEscortRate()); 
			} else {
				escortRate = (Float.parseFloat(dto.getEscortRate())*totalRate)/100;
			}
		}
		
		map.put("classicTripBillingId", dto.getId() );
		map.put("classicTripDistance", tripDto.getDistance());
		map.put("classicTripRate", String.valueOf( totalRate));
		map.put("classicEscortRate", String.valueOf(escortRate));
		} catch(Exception e) {
			System.out.println("Error in BillingProcessKmBasedClassic :"+ e);
		}
		return map;
	}

}
