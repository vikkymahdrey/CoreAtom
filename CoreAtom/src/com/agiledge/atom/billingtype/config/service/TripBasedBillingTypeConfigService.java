package com.agiledge.atom.billingtype.config.service;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dao.TripBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto;
import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;

public class TripBasedBillingTypeConfigService {

	private String message;
	private TripBasedBillingTypeConfigDao dao= new TripBasedBillingTypeConfigDao();
	public boolean validateTripBasedBillingTypeConfig(TripBasedBillingConfigDto dto) {
		boolean flag=true;
		
		if( OtherFunctions.isEmpty( dto.getFlatTripRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getVehicleTypeId() )) {
			setMessage("Vehicle type is not selected");
			flag = false;
			
		} else if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getDcYes())) {
			setMessage("Distance constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 for(AcConstraintDto adto: dto.getAcList()) {
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = false;
						 break;
						 
					 } else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = false;
						 break;
					 }
				 }
			 }
		} else if (dto.getDcYes().equalsIgnoreCase(BillingTypeConfigConstants.DC_YES)) {
			 ArrayList<DistanceConstraintDto> dcList = dto.getDcList();
			 if(dcList==null ||dcList.size()<=0) {
				 setMessage("No value in distance constraints");
				 flag = false;
			 } else {
				 for(DistanceConstraintDto adto: dto.getDcList()) {
					 if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("Distance Constraint rate is blank.");
						 flag = false;
						 break;
					 }
				 }
			 }
		}
		
		return flag;
		
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int addTripBasedConfig(TripBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return  dao.addTripBasedBillingConfig(dto);
		 
	}
	
	public ArrayList<TripBasedBillingConfigDto> getTripBasedBillingConfig(String  refId){
		return dao.getTripBasedBillingConfig(refId);
	}
	public int updateTripBasedConfig(TripBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return dao.updateTripBasedConfig(dto);
	}
	public int deleteTripBasedConfig(TripBasedBillingConfigDto dto) {
		return dao.deleteTripBasedConfig(dto);
	}
	
}
