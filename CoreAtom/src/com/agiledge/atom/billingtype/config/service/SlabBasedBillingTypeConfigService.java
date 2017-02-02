package com.agiledge.atom.billingtype.config.service;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dao.SlabBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.BillingSlabDto;
import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.commons.OtherFunctions;

public class SlabBasedBillingTypeConfigService {

	SlabBasedBillingTypeConfigDao dao = new SlabBasedBillingTypeConfigDao();
	private String message;
	 
	public int addSlabBasedBillingConfig(SlabBasedBillingConfigDto dto) {
		return dao.addSlabBasedBillingConfig(dto);
		 
	}
	
	public int updateSlabBasedConfig( SlabBasedBillingConfigDto dto) {
		return dao.updateSlabBasedConfig(dto);
		 
	}
	
	public ArrayList<SlabBasedBillingConfigDto> getSlabBasedBillingConfig(String refId) {
		return dao.getSlabBasedBillingConfig(refId);
	}
	
	public  SlabBasedBillingConfigDto getSlabBasedBillingConfig(String refId, String vehicleId) {
		return dao.getSlabBasedBillingConfig(refId, vehicleId);
	}
	
	public ArrayList<BillingSlabDto> getTimeSlabs() {
		return dao.getTimeSlabs();
	}

	public boolean validateSlabBasedBillingTypeConfig(
			SlabBasedBillingConfigDto dto) {
		// TODO Auto-generated method stub
		boolean flag = true;
		if(OtherFunctions.isEmpty(    dto.getTripRate())) {
			message = "Flat Amount is not selected";
			flag = false;
		} else if ( OtherFunctions.isEmpty(    dto.getEscortRateType()) ) {
			flag = false;
			message = "Escort Rate Type is not selected";
		}  else if ( OtherFunctions.isEmpty(    dto.getEscortRate()) ) {
			flag = false;
			message = "Escort Rate is not selected";
		} else if (dto.getSlabList()==null||dto.getSlabList().size()<=0) {
			flag = false;
			message = "Slab Rates are not assigned";
		} else {
			for(BillingSlabDto slabDto : dto.getSlabList()) {
				if( OtherFunctions.isEmpty(  slabDto.getId() )||OtherFunctions.isEmpty(  slabDto.getRate() ) ) {
					flag = false;
					message = "Slab Rates are not assigned";
					
					break;
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

	public int deleteSlabBasedConfig(SlabBasedBillingConfigDto dto) {
		 
		return dao.deleteSlabBasedConfig(dto);
	}
}
