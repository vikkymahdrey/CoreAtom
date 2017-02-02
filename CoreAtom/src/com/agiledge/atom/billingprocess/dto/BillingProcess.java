package com.agiledge.atom.billingprocess.dto;

import java.util.Map;

import com.agiledge.atom.dto.TripDetailsDto;

public interface BillingProcess {

	 
	 
	boolean getValues(String refId, String vehicleTypeId);
	Map<String, String> calculateValues(TripDetailsDto dto);
}
