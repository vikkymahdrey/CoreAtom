package com.agiledge.atom.billingtype.config.service;

import com.agiledge.atom.billingtype.config.dao.KmBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;

public class KmBasedBillingTypeConfigService {

	private String message;
	private KmBasedBillingTypeConfigDao dao= new KmBasedBillingTypeConfigDao();
	public boolean validateKmBasedBillingTypeConfig(KmBasedBillingConfigDto dto) {
		boolean flag=true;
		 
		if( OtherFunctions.isEmpty( dto.getBillingRefId() )) {
			setMessage("Invalid billing type");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getKmBillingType() )) {
			setMessage("Km billing type is not chosen");
			flag = false;
			
		}  	
		return flag;
		
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public int addKmBasedConfig(KmBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return  dao.addKmBasedBillingConfig(dto);
		 
	}
	
	public  KmBasedBillingConfigDto getKmBasedBillingConfig(String  refId){
		return dao.getKmBasedBillingConfig(refId);
	}
	
	 
	
}
