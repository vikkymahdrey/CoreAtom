package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;


public interface BillingProcessKmBasedClassicDao {
	 KmBasedClassicTripBillingConfigDto getKmBasedClassicBillingConfigFromBillingId(
			String refId, String vehicleTypeId);

	 
}
