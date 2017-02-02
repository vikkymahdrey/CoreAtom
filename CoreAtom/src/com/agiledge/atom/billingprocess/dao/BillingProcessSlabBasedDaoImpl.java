package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dao.SlabBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;


public class BillingProcessSlabBasedDaoImpl implements
		BillingProcessSlabBasedDao {

	 
	@Override
	public SlabBasedBillingConfigDto getSlabBillingConfigFromBillingId(
			String refId, String vehicleTypeId) {
		// TODO Auto-generated method stub
		System.out.println(" INSIDE BillingProcessSlabBasedDaoImpl ");
		SlabBasedBillingTypeConfigDao dao= new SlabBasedBillingTypeConfigDao();
		return dao.getSlabBasedBillingConfig(refId, vehicleTypeId);
	}

}
