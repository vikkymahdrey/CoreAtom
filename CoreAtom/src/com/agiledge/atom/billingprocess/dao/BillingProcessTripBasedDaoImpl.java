package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dao.TripBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;
 
public class BillingProcessTripBasedDaoImpl implements BillingProcessTripBasedDao{
	
	@Override
	public   TripBasedBillingConfigDto getKmBasedClassicBillingConfigFromBillingId(String  billingId, String vehicleId ) {
		// TODO Auto-generated method stub
		System.out.println(" INSIDE BillingProcessTripBasedDaoImpl ");
		return new TripBasedBillingTypeConfigDao().getTripBasedBillingConfig (billingId, vehicleId);
 	}
	


}
