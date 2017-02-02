package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dao.KmBasedTemplateBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;


public class BillingProcessKmBasedTemplateDaoImpl implements
		BillingProcessKmBasedTemplateDao {

	@Override
	public  KmBasedTemplateTripBillingConfigDto getKmBasedTemplateBillingConfig(String refId, String vehicleTypeId) {
		System.out.println(" INSIDE BillingProcessKmBasedTemplateDaoImpl ");
		return new KmBasedTemplateBillingTypeConfigDao().getKmBasedTemplateBillingConfig(refId, vehicleTypeId);
	}
}
