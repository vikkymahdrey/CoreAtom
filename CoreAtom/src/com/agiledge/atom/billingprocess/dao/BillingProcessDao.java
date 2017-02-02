package com.agiledge.atom.billingprocess.dao;

import java.util.Map;

import com.agiledge.atom.dto.TripDetailsDto;

public interface BillingProcessDao {

 
 
	int save(TripDetailsDto tripDto, Map<String, String> params, String table);
}
