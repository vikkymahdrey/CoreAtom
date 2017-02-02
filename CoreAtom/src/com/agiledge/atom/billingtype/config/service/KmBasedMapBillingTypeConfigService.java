package com.agiledge.atom.billingtype.config.service;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dao.KmBasedMapBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;

public class KmBasedMapBillingTypeConfigService {

	private String message;
	private KmBasedMapBillingTypeConfigDao dao= new KmBasedMapBillingTypeConfigDao();
	public boolean validateKmBasedMapBillingTypeConfig(KmBasedMapTripBillingConfigDto dto) {
		boolean flag=true;
		 
		if( OtherFunctions.isEmpty( dto.getKmBillingType() )) {
			setMessage("Please update billing type first");
			flag = false;
			
		} else if(checkDuplication(dto)) {
			
			setMessage("Already exists");
			flag = false;
		} else if( OtherFunctions.isEmpty( dto.getTripRate() )) {
			setMessage("Trip Rate / Km is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getVehicleTypeId() )) {
			setMessage("Vehicle type is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getDistanceType() )) {
			setMessage("Distance type is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getCalculationType() )) {
			setMessage("Calculation type is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Swing value is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Swing rate is not selected");
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
						 flag = false;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = false;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 setMessage("A/C time is not in order");
						 flag = false;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = false;
						 break;
					 }else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = false;
						 break;
					 }
					 lastToTime=adto.getToTime();
				 }
			 }
		}
		return flag;
		
	}
	
	public boolean checkDuplication(KmBasedMapTripBillingConfigDto dto) {
		return dao.checkDuplication(dto);
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int addKmBasedMapConfig(KmBasedMapTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return  dao.addKmBasedMapBillingConfig(dto);
		 
	}
	
	public int updateKmBasedClassicConfig(KmBasedMapTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return  dao.updateKmBasedMapBillingConfig(dto);
		 
	}
	
	public ArrayList<KmBasedMapTripBillingConfigDto> getKmBasedMapBillingConfig(String  refId){
		return dao.getKmBasedMapBillingConfig(refId);
	}
	public boolean validateKmBasedClassicBillingTypeConfigForEdit(
			KmBasedMapTripBillingConfigDto dto) {
		boolean flag=true;
		 

		 if( OtherFunctions.isEmpty( dto.getId() )) {
			setMessage("Unable to update");
			flag = false;
			
		}  else if( OtherFunctions.isEmpty( dto.getTripRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		}  else if( OtherFunctions.isEmpty( dto.getDistanceType() )) {
			setMessage("Distance type is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Swing value is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getCalculationType() )) {
			setMessage("Calculation type is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getRate() )) {
			setMessage("Swing rate is not selected");
			flag = false;
			
		}   else	if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getEscortRateType() )) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if(OtherFunctions.isEmpty (dto.getEscortRate()) || !OtherFunctions.isDouble(dto.getEscortRate()) ) {
			setMessage("Escort rate should be a valid rate");
			 flag = false;
		} else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
		 
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 String lastToTime=" ";
				 for(AcConstraintDto adto: dto.getAcList()) {
					 System.out.println("last to: " + lastToTime + " now from" + adto.getFromTime() +
							 "Compare Result :" + lastToTime.compareTo(adto.getFromTime()) );
					 System.out.println("now From: " + adto.getFromTime() + " now to " + adto.getToTime() +
							 "Compare Result :" +  adto.getFromTime().compareTo(adto.getToTime()) );
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = false;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = false;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 System.out.println("from > to");
						 setMessage("A/C time is not in order");
						 flag = false;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = false;
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
	
	public int deleteKmBasedMapBillingConfig(KmBasedMapTripBillingConfigDto dto) {
		return dao.deleteKmBasedMapBillingConfig( dto); 
	}
	
}
