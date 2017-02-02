package com.agiledge.atom.billingprocess.dao;

import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;


public interface BillingProcessKmBasedTemplateDao {
 

	 KmBasedTemplateTripBillingConfigDto  getKmBasedTemplateBillingConfig(
			String refId, String vehicleType);

}
