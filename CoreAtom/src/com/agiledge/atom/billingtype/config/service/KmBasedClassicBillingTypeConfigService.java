package com.agiledge.atom.billingtype.config.service;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dao.KmBasedClassicBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;

public class KmBasedClassicBillingTypeConfigService {

	private String message;
	private KmBasedClassicBillingTypeConfigDao dao= new KmBasedClassicBillingTypeConfigDao();
	public boolean validateKmBasedClassicBillingTypeConfig(KmBasedClassicTripBillingConfigDto dto) {
		boolean flag=true;
		 
		if( OtherFunctions.isEmpty( dto.getKmBillingType() )) {
			setMessage("Please update billing type first");
			flag = false;
			
		} else if( checkDuplication(dto) ) {
			setMessage("This entry already exists");
			flag = false;
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getVehicleTypeId() )) {
			setMessage("Vehicle type is not selected");
			flag = false;
			
		}  else	if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getEscortRateType() )) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if(OtherFunctions.isEmpty (dto.getEscortRate()) || !OtherFunctions.isDouble(dto.getEscortRate()) ) {
			setMessage("Escort rate should be a valid rate");
			 flag = false;
		}else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
		 
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 String lastToTime=" ";
				 for(AcConstraintDto adto: dto.getAcList()) {
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = true;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = true;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
					 }else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = true;
						 break;
					 }
					 lastToTime=adto.getToTime();
				 }
			 }
		}
		return flag;
		
	}
		
	 
	
	public boolean checkDuplication(KmBasedClassicTripBillingConfigDto dto) {
		return dao.checkDuplication(dto);
	}
	
	public boolean validateKmBasedClassicBillingTypeConfigForEdit(KmBasedClassicTripBillingConfigDto dto) {
		boolean flag=true;
		 
		if( OtherFunctions.isEmpty( dto.getId() )) {
			setMessage("Unable to update");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		} else	if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getEscortRateType() )) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if(OtherFunctions.isEmpty (dto.getEscortRate()) || !OtherFunctions.isDouble(dto.getEscortRate()) ) {
			setMessage("Escort rate should be a valid rate");
			 flag = false;
		}else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
		 
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 String lastToTime=" ";
				 for(AcConstraintDto adto: dto.getAcList()) {
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = true;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = true;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
					 }else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = true;
						 break;
					 }
					 lastToTime=adto.getToTime();
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
	public int addKmBasedClassicConfig(KmBasedClassicTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return  dao.addKmBasedClassicBillingConfig(dto);
		 
	}
	
	public ArrayList<KmBasedClassicTripBillingConfigDto> getKmBasedClassicBillingConfig(String  refId ){
		return dao.getKmBasedClassicBillingConfig( refId );
	}

	public int updateKmBasedClassicConfig(KmBasedClassicTripBillingConfigDto dto) {
		return dao.updateKmBasedClassicConfig( dto) ;
	}

	public int deleteKmBasedClassicConfig(KmBasedClassicTripBillingConfigDto dto) {
 
			return dao.deleteKmBasedClassicConfig(dto);
		 
	}
	
}
