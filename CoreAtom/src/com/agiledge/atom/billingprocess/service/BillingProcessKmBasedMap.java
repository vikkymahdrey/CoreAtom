package com.agiledge.atom.billingprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedMapDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedMapDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingprocess.service.util.EmployeeBoardChecker;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;
import com.agiledge.atom.commons.collectionUtils.CollectionUtil;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.trip.dto.TripDetailsConstant;

public class BillingProcessKmBasedMap implements BillingProcess {

	BillingProcessKmBasedMapDao dao = new  BillingProcessKmBasedMapDaoImpl();
	
	KmBasedMapTripBillingConfigDto dto;
	@Override
	public boolean getValues(String refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		boolean returnFlag=false;
		ArrayList<KmBasedMapTripBillingConfigDto > dtoList = dao.getKmBasedMapBillingConfig(refId, vehicleTypeId);
 
		try {
			
			 if(dtoList.size()==1) {
				 dto = dtoList.get(0);
				 returnFlag = true;
			 } 
		 }catch(Exception e) {
			  
		 }
		return returnFlag;
	}

	private float getTripRate(String shiftTime) {
		 
		float tripRate = Float.parseFloat( dto.getTripRate());
		 for(AcConstraintDto acDto : dto.getAcList()) {
			 if(shiftTime.compareTo(acDto.getFromTime())>=0 && shiftTime.compareTo(acDto.getToTime())<=0 ) {
				 tripRate = Float.parseFloat(acDto.getRate());
				 break;
			 }
		 }
		return tripRate;
	}
	
	private float getDistance(TripDetailsDto  tripDto) {
		float distance = Float.parseFloat( tripDto.getDistance());
			if(dto.getDistanceType().equalsIgnoreCase( KmBasedBillingConfigConstants.DISTANCE_TYPE_MOBILE  ) ) {
				distance =    (float) tripDto.getDistanceCovered();
			}  
			return distance;
			
			
	}
	 
	
	private float getSwingDistance(TripDetailsDto tripDto, float flatDistance) {
		// TODO Auto-generated method stub
		float distance = 0f;
		int noShowCount = 0;
		if(tripDto.getTripDetailsChildDtoList().size()>0) {
			EmployeeBoardChecker chk = new EmployeeBoardChecker();
			chk.boardStatus=TripDetailsConstant.NO_SHOW;
			try {
			 noShowCount = ( (ArrayList<TripDetailsChildDto >)  CollectionUtil.findAll(tripDto.getTripDetailsChildDtoList(), chk)).size();
			} catch (Exception e) {
				System.out.println("Exception while geting noshow count :"+e);
			}
			int noOfEmployees = tripDto.getTripDetailsChildDtoList().size();
			if(tripDto.getTrip_log().equals(  TripDetailsConstant.OUT) ) {
				noOfEmployees = noOfEmployees - noShowCount;
			}
			
			if(dto.getCalculationType().equalsIgnoreCase(KmBasedBillingConfigConstants.FLAT)) {
				distance = noOfEmployees * Float.parseFloat( dto.getRate());
			} else {
				distance = noOfEmployees * (Float.parseFloat( dto.getRate()) * flatDistance / 100);
			}
			 			
		}
		return distance;
	}


 
	@Override
	public Map<String, String> calculateValues(TripDetailsDto tripDto) {
		// TODO Auto-generated method stub
		HashMap<String, String> map = new HashMap<String, String>();
		try {
			float distance = getDistance(tripDto);
			float swingDistance = getSwingDistance(tripDto, distance);
			float ratePerKm = getTripRate(tripDto.getTrip_time());
			 
			
			float totalRate = (distance + swingDistance) * ratePerKm;
			float escortRate = 0f;
			if(tripDto.getEscort().equalsIgnoreCase("YES")) {
				if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.FLAT)) {
					escortRate = Float.parseFloat(dto.getEscortRate()); 
				} else {
					escortRate = (Float.parseFloat(dto.getEscortRate())*totalRate)/100;
				}
			}
			
			 
			
			
			
			
			
			
			String type="map";
			if(dto.getDistanceType().equalsIgnoreCase( KmBasedBillingConfigConstants.DISTANCE_TYPE_MOBILE  )) {
				type="mobile";
			}
			map.put(type+"TripBillingId", dto.getId());
			map.put(type+"TripDistance", tripDto.getDistance());
			map.put(type+"TripRate", String.valueOf( totalRate));
			map.put(type+"EscortRate", String.valueOf(escortRate));
		}catch(Exception e) {
			System.out.println("Error in BillingProcessKmBasedMp :"+ e);
		}
		return map;
		 
	}
	
	
	

	private TripDetailsChildDto getLastShowChild(ArrayList<TripDetailsChildDto> list) {
		TripDetailsChildDto returnDto = null;
		try {
		for(int i=list.size()-1; i>=0 ; i--) {
			TripDetailsChildDto dto = list.get(i);
			if(i==list.size()-2 || dto.getShowStatus()!=null&&dto.getShowStatus().equalsIgnoreCase(TripDetailsConstant.SHOW)) {
				returnDto = dto;
				break;
			}
			
		}
		}catch(Exception nu) {
			;
		}
		return returnDto;
		
	}
	
	

}
