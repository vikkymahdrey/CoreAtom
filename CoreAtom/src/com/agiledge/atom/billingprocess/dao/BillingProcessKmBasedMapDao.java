package com.agiledge.atom.billingprocess.dao;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;


public interface BillingProcessKmBasedMapDao {

 
	ArrayList<KmBasedMapTripBillingConfigDto> getKmBasedMapBillingConfig(
			String refId, String vehicleTypeId);

}
