package com.agiledge.atom.billingprocess.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.billingtype.service.BillingTypeService;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VendorDto;

public class BillingProcessFactory {
	
	public static ArrayList<BillingProcess>  getProcessInstances(TripDetailsDto tripDto, String transportTypeId, String vehicleTypeId, VendorDto vendorDto ) throws SQLException {
		TripDetailsDao tripDao = new TripDetailsDao();
		String tripId = tripDto.getId();
	
		System.out.println("VENDOR DTO : "+vendorDto);
	//	TripDetailsDto tripDto =  tripDao.getTripSheetByTrip(tripId);
		System.out.println("TRIP DTO : "+tripDto);
		
		ArrayList<BillingTypeDto> billingTypeList = new BillingTypeService().getCurrentBillingTypeMappings(tripDto.getSiteId(),  vendorDto.getId(), transportTypeId);
		//ArrayList<BillingTypeDto> billingTypeList = new BillingTypeService().getCurrentBillingTypeMappings(tripDto.getSiteId(),  vendorDto.getId(), transportTypeId);
		ArrayList<BillingProcess> bps = new ArrayList<BillingProcess>();
		System.out.println(".........Bill Process Size : " + billingTypeList.size());
		int i=0;
		for(BillingTypeDto dto : billingTypeList) {
			
			BillingProcess kmCl = new BillingProcessKmBasedClassic();
			 if(kmCl.getValues(dto.getId(), vehicleTypeId )) {
				  bps.add(kmCl);
			 }
			 BillingProcess kmTm = new BillingProcessKmBasedTemplate();
			 if(kmTm.getValues(dto.getId(), vehicleTypeId)) {
				  bps.add(kmTm);
			 }
			 BillingProcess kmMp = new BillingProcessKmBasedMap();
			 if(kmMp.getValues(dto.getId(), vehicleTypeId)) {
				  bps.add(kmMp);
			 }
			 BillingProcess trip = new BillingProcessTripBased();
			 if(trip.getValues(dto.getId(), vehicleTypeId)) {
				  bps.add(trip);
			 }
			 BillingProcess slab = new BillingProcessSlabBased();
			 if(slab.getValues(dto.getId(), vehicleTypeId)) {
				  
				  bps.add(slab);
			 }
			  
		 	 
		}
		 
		
		return bps;
	}

}
