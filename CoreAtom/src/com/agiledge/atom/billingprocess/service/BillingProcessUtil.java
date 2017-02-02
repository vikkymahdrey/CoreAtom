package com.agiledge.atom.billingprocess.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.agiledge.atom.billingtype.dto.BillingTypeConstant;
import com.agiledge.atom.billingtype.service.BillingTypeService;
import com.agiledge.atom.dao.VendorDao;
import com.agiledge.atom.dto.VendorDto;

public class BillingProcessUtil {

	public static Map<String, String > getBillingPages(String site , Integer transportType) {
		  
System.out.println("site : " + site + "  transport type : " + transportType);
        		ArrayList<VendorDto> vendorList = new VendorDao().getVendorMasterList();
        		Map<String,String> map = new HashMap<String,String>();
        		for(VendorDto vdto : vendorList) {
						ArrayList<String > str = new BillingTypeService().getCurrentBillingTypeMappingsKeys(site, vdto.getId(),  String.valueOf( transportType));
						 
						
						// MultiMap map = new MultiHashMap(); 
					 
						 for(String st : str) {
					 System.out.println(" ST : " + st);
							  if(st.equalsIgnoreCase(BillingTypeConstant.TRIP_BASED_BILLING_TYPESTRING)){ 
								 map.put("Trip Based","displayTripBasedBilling4GeneralShift.jsp");
							} else   if(st.equalsIgnoreCase(BillingTypeConstant.SLAB_BASED_BILLING_TYPESTRING)){ 
								 map.put("Slab Based","displaySlabBasedBilling4GeneralShift.jsp");
							} else   if(st.equalsIgnoreCase(BillingTypeConstant.KM_BASED_CLASSIC_BILLING_TYPESTRING )){ 
								 map.put("Classic Km","displayKmBasedClassicBilling4GeneralShift.jsp");
							} else   if(st.equalsIgnoreCase(BillingTypeConstant.KM_BASED_MAP_BILLING_TYPESTRING )){ 
								 map.put("Map Km","displayKmBasedMapBilling4GeneralShift.jsp");
							} else  if(st.equalsIgnoreCase(BillingTypeConstant.KM_BASED_TEMPLATE_BILLING_TYPESTRING )){ 
								 map.put("Template Km","displayKmBasedTemplateBilling4GeneralShift.jsp");
							}
							  
						}
        		}
				return map; 
				 
			 

	}
	
}