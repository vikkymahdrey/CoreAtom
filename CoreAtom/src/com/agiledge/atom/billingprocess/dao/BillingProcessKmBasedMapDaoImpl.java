package com.agiledge.atom.billingprocess.dao;

import java.util.ArrayList;

import com.agiledge.atom.billingtype.config.dao.KmBasedMapBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;


public class BillingProcessKmBasedMapDaoImpl implements
		BillingProcessKmBasedMapDao {

	@Override
	public ArrayList<KmBasedMapTripBillingConfigDto> getKmBasedMapBillingConfig(String refId, String vehicleTypeId) {
		System.out.println(" INSIDE BillingProcessKmBasedMapDao");
		return new KmBasedMapBillingTypeConfigDao().getKmBasedMapBillingConfig(refId, vehicleTypeId);
	}

}
