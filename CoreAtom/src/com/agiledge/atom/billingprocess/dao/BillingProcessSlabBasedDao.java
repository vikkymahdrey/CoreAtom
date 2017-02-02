package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;


public interface BillingProcessSlabBasedDao {
	 
	SlabBasedBillingConfigDto getSlabBillingConfigFromBillingId(String refId,
			String vehicleTypeId);

	 
}
