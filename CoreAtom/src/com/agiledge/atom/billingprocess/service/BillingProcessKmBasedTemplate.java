package com.agiledge.atom.billingprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedTemplateDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessKmBasedTemplateDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingprocess.service.util.EmployeeBoardChecker;
import com.agiledge.atom.billingtype.config.dao.KmBasedTemplateBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto.KmTemplateChildDto;
import com.agiledge.atom.commons.collectionUtils.Checker;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.trip.dto.TripDetailsConstant;


public class BillingProcessKmBasedTemplate implements BillingProcess {

	BillingProcessKmBasedTemplateDao dao = new BillingProcessKmBasedTemplateDaoImpl();
	KmBasedTemplateTripBillingConfigDto dto = null;
	
	@Override
	public boolean getValues(String refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		boolean returnFlag = false;
		dto = dao.getKmBasedTemplateBillingConfig(refId, vehicleTypeId);
		try {
		if(dto!=null) {
			 returnFlag = true;
		 }
		
		}catch(Exception iginor) {
			;
		}
		
		return returnFlag;
	}


	private float getTripRate(TripDetailsDto tripDto) {
		String shiftTime = tripDto.getTrip_time();
		float tripRate = Float.parseFloat( dto.getTripRate() );
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
			float distance = getDistance(tripDto);
			System.out.println("flat distance : " + distance);
			float ratePerKm = getTripRate(tripDto );
			
			float swingDistance = getSwingDistance(tripDto, distance);
			System.out.println("swing distance : " + swingDistance);
			System.out.println("total distance : " + (distance + swingDistance) );
			System.out.println("Rate/Km : " + ratePerKm);
			float totalDistance = (distance + swingDistance);
			float totalRate = (distance + swingDistance) * ratePerKm;
			float escortRate=0f;
			System.out.println("Total without escort : " + totalRate);
			if(tripDto.getEscort().equalsIgnoreCase("YES")) {
				if(dto.getEscortRateType().equalsIgnoreCase(BillingTypeConfigConstants.FLAT)) {
					escortRate =(float) ( dto.getEscortRate()); 
				} else {
					escortRate =(float) ( ( dto.getEscortRate()*totalRate)/100);
				}
			}
			
			 
			
			
			System.out.println("Total without after escort : " + totalRate);
			
			
			
			String type="template";
		 	map.put(type+"TripBillingId", dto.getId());
			map.put(type+"TripDistance", String.valueOf(totalDistance));
			map.put(type+"TripRate", String.valueOf( totalRate));
			map.put(type+"SwingDistance", String.valueOf( swingDistance));
			map.put(type+"EscortRate", String.valueOf(escortRate));
		}catch(Exception e) {
			System.out.println("Error in calculateValues : "+ e);
		}
		 
		return map;
		 

	}

 
	private class DistanceChecker implements Checker<KmTemplateChildDto> {
		String landmarkId;
		@Override
		public boolean check(KmTemplateChildDto obj) {
			// TODO Auto-generated method stub
			if(obj.getAplDto().getLandMarkID().equals(landmarkId)) {
				return true;
			}
			else {
			return false;
			}
		}
		
	}
	
	private float getDistance(TripDetailsDto tripDto) {
		// TODO Auto-generated method stub
		float distance = 0f;
		if(tripDto.getTripDetailsChildDtoList().size()>0) {
		
			try {
			TripDetailsChildDto lastChild =tripDto.getTrip_log().equals(  TripDetailsConstant.IN) ?   tripDto.getTripDetailsChildDtoList().get(0) : getLastShowChild( tripDto.getTripDetailsChildDtoList()  );  
			KmTemplateDto templateDto = new KmBasedTemplateBillingTypeConfigDao().getKmTemplateDto(dto.getKmBillingRefId() );
			DistanceChecker chk = new DistanceChecker();
			chk.landmarkId = lastChild.getLandmarkId();
			try {
			 KmTemplateChildDto templateChildDto = ( (ArrayList<KmTemplateChildDto>) com.agiledge.atom.commons.collectionUtils.CollectionUtil.findAll(templateDto.getChildList(), chk)).get(0);
			 distance = templateChildDto.getDistance();
			} catch(ArrayStoreException e) {
				System.out.println("No enry " + e);
				 
			} catch (NullPointerException e) {
				// TODO: handle exception
				System.out.println("Error " + e);
			}
			
			}catch(NullPointerException igon){}
			
		}
		return distance;
	}
	
	 
	
	private TripDetailsChildDto getLastShowChild(ArrayList<TripDetailsChildDto> list) {
		TripDetailsChildDto returnDto = null;
		try {
		for(int i=list.size()-1; i>=0 ; i--) {
			TripDetailsChildDto dto = list.get(i);
			if( dto.getShowStatus()!=null&&dto.getShowStatus().equalsIgnoreCase(TripDetailsConstant.SHOW)) {
				returnDto = dto;
				break;
			}
			
		}
		}catch(Exception nu) {
			;
		}
		return returnDto;
		
	}
	
	
	private float getSwingDistance(TripDetailsDto tripDto, float flatDistance) {
		// TODO Auto-generated method stub
		float distance = 0f;
		int noShowCount = 0;
		if(tripDto.getTripDetailsChildDtoList().size()>0) {
			EmployeeBoardChecker chk = new EmployeeBoardChecker();
			chk.boardStatus=TripDetailsConstant.NO_SHOW;
			try {
			 noShowCount = ( (ArrayList<TripDetailsChildDto >) com.agiledge.atom.commons.collectionUtils.CollectionUtil.findAll(tripDto.getTripDetailsChildDtoList()
					 , chk)).size();
			} catch (Exception e) {
				System.out.println("Exception while geting noshow count :"+e);
			}
			int noOfEmployees = tripDto.getTripDetailsChildDtoList().size();
			if(tripDto.getTrip_log().equals(  TripDetailsConstant.OUT) ) {
				noOfEmployees = noOfEmployees - noShowCount;
			}
			
			if(dto.getSwingRateType().equalsIgnoreCase(KmBasedBillingConfigConstants.FLAT)) {
				distance = noOfEmployees * Float.parseFloat( dto.getSwingRate());
			} else {
				distance = noOfEmployees * (Float.parseFloat( dto.getSwingRate()) * flatDistance / 100);
			}
			 			
		}
		return distance;
	}


}
