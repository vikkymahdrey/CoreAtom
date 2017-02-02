package com.agiledge.atom.billingprocess.service;

import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingprocess.dao.BillingProcessTripBasedDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessTripBasedDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto;
import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;
import com.agiledge.atom.dto.TripDetailsDto;

public class BillingProcessTripBased implements BillingProcess {

	BillingProcessTripBasedDao dao = new BillingProcessTripBasedDaoImpl();
	TripBasedBillingConfigDto dto;
	 
	@Override
	public boolean getValues(String refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		
		boolean returnFlag = false;
		try {
		 dto = dao.getKmBasedClassicBillingConfigFromBillingId  (refId, vehicleTypeId);
		 if(dto!=null) {
			 returnFlag = true;
		 }
		}catch(Exception e) {
			System.out.println("getValue : " + e);
		}
		return returnFlag;
	}
 

	private float getTripRate( TripDetailsDto tripDto) {
		String shiftTime = tripDto.getTrip_time();
		float tripRate = Float.parseFloat( dto.getFlatTripRate());
		float dcTripRate = 0f;
		float acTripRate = 0f;
		float dcAcTripRate = 0f;
		boolean enteredInDc = false;
		boolean enteredInAc = false;
		if(dto.getDcYes()!=null&&dto.getDcYes().equals(BillingTypeConfigConstants.DC_YES)&&dto.getDcList()!=null&&dto.getDcList().size()>0) {
			for(DistanceConstraintDto dcDto : dto.getDcList()) {
				float distance = Float.parseFloat(tripDto.getDistance());
				float fromKm = Float.parseFloat(dcDto.getFromKm());
				float toKm = Float.parseFloat(tripDto.getToKm());
				if(distance<=toKm && distance >= fromKm) {
					dcTripRate = Float.parseFloat(dcDto.getRate());
					dcAcTripRate = Float.parseFloat(dcDto.getDcAcRate());
					enteredInDc = true;
					break;
				}
			}

		}
		if(dto.getAcYes()!=null&&dto.getAcYes().equals(BillingTypeConfigConstants.AC_YES)&&dto.getAcList()!=null&&dto.getAcList().size()>0) {
			 for(AcConstraintDto acDto : dto.getAcList()) {
				 if(shiftTime.compareTo(acDto.getFromTime())>=0 && shiftTime.compareTo(acDto.getToTime())<=0 ) {
					 acTripRate = Float.parseFloat(acDto.getRate());
					 enteredInAc = true;
					 break;
				 }
			 }
		}
		
		if(enteredInAc&&enteredInDc) {
			tripRate = dcAcTripRate;
		} else if(enteredInAc) {
			tripRate = acTripRate;
		} else if(enteredInDc) {
			tripRate = dcTripRate;
		}
		return tripRate;
	}

	@Override
	public Map<String, String> calculateValues(TripDetailsDto tripDto) {
 
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			float totalRate = getTripRate(tripDto);
			float escortRate = 0f;
			 
			 
			//tripDto.setEscort("YES");
			System.out.println("Trip Details : " + tripDto.toString());
			if(tripDto.getEscort().equalsIgnoreCase("YES")) {
				if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.FLAT)) {
					System.out.println("Escort type :");
					escortRate =(float) ( Float.parseFloat(dto.getEscortRate()));
					 
				} else {
					escortRate =(float) ( Float.parseFloat(dto.getEscortRate()) * totalRate / 100);
				}
			}
			
			String type="trip";
		 	map.put(type+"TripBillingId", dto.getId());
			map.put(type+"TripRate", String.valueOf( totalRate));
			map.put(type+"EscortRate", String.valueOf(escortRate));
		}catch(Exception e) {
			System.out.println("Error in BillingPRocesstripBased : "+ e);
		}
			  
		 
		return map;
		 

	}

}
