package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dao.KmBasedClassicBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;
 
public class BillingProcessKmBasedClassicDaoImpl implements BillingProcessKmBasedClassicDao{
	
	@Override
	public  KmBasedClassicTripBillingConfigDto getKmBasedClassicBillingConfigFromBillingId(String  billingId, String vehicleId ) {
		// TODO Auto-generated method stub
		return new KmBasedClassicBillingTypeConfigDao().getKmBasedClassicBillingConfig(billingId, vehicleId);
 	}
	


}
