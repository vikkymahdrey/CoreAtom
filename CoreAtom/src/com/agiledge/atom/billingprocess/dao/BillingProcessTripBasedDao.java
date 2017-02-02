package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;


public interface BillingProcessTripBasedDao {
	 TripBasedBillingConfigDto getKmBasedClassicBillingConfigFromBillingId(
			String refId, String vehicleTypeId);

	 
}
