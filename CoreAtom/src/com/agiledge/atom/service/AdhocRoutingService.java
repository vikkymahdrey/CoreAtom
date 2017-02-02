package com.agiledge.atom.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiledge.atom.billingprocess.dao.BillingProcessDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingprocess.service.BillingProcessFactory;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.AdhocDao;
import com.agiledge.atom.dao.AdhocRoutingDao;
import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.dto.VendorTripDto;
import com.agiledge.atom.hibernate.dto.AdhocTripHBMDto;
import com.agiledge.atom.transporttype.dto.TransportTypeConfig;

public class AdhocRoutingService {
	public int doAdhocRouting(String siteId, String date) {
		date = OtherFunctions.changeDateFromatToIso(date);
		ArrayList<AdhocDto> bookingList = new AdhocDao()
				.getBookingDetailsForRouting(siteId, date);
		VehicleTypeDao vehicleTypedao = new VehicleTypeDao();
		SiteDao siteDao = new SiteDao();
		for (AdhocDto adhocDto : bookingList) {
			adhocDto.setVehicleType(vehicleTypedao
					.getMinimumCapacityVehicle(siteId));
			adhocDto.setEscort(siteDao.setEscort(siteId,
					adhocDto.getEmployeeId(), adhocDto.getStartTime(),
					adhocDto.getEndTime()));
		}

		int retVal = new AdhocRoutingDao().insertToTrip(bookingList);
		System.out.println("return Val" + retVal);
		return retVal;
	}

	public ArrayList<TripDetailsDto> getAdhocRouting(String siteId, String date) {
		date = OtherFunctions.changeDateFromatToIso(date);
		return new AdhocRoutingDao().getTripDetails(siteId, date);
	}
	
	
	public ArrayList<TripDetailsDto> getSavedTripDetails(String siteId, String date, String approvalStatus[]) {
		date = OtherFunctions.changeDateFromatToIso(date);
		TripDetailsDto pdto = new TripDetailsDto();
		pdto.setSiteId(siteId);
		pdto.setTrip_date(date);
		return new AdhocRoutingDao().getSavedTripDetails(pdto,  approvalStatus);
	}
	
	public  TripDetailsDto  getAdhocRoutingTrip(String tripId) {
		 
		return new AdhocRoutingDao().getTripDetailsTrip(tripId);
	}

	public ArrayList< TripDetailsDto>  getAdhocRoutingTrip(TripDetailsDto dto) {
		  
		return new AdhocRoutingDao().getTripDetailsTrip(dto);
	}
	public ArrayList< TripDetailsDto>  getOtherAdhocRoutingTripForVendor(TripDetailsDto dto) {
		  
		return new AdhocRoutingDao().getOtherAdhocRoutingTripForVendor(dto);
	}

	public int SaveTrip(String[] tripids) {
		return new AdhocRoutingDao().SaveTrip(tripids);
	}

	public int approveTrip(ArrayList<TripDetailsDto> tripList) {
		// return
		int retVal = 0;
		retVal = new AdhocRoutingDao().approveTrip(tripList);
		HashMap<String, TripDetailsDto> mapDto = new HashMap<String, TripDetailsDto>();
		try {
			for (TripDetailsDto dto : tripList) {
				mapDto.put(dto.getId(), dto);
			}
			saveTripRateArgs(mapDto, SettingsConstant.ADHOC);

		} catch (Exception e) {

			System.out.println("error in approve adhoc " + e);
		}
		return retVal;
	}


public void saveTripRateArgs (HashMap<String, TripDetailsDto> trips,
			
			String user) throws SQLException {
		
	
			Set <String> keys =trips.keySet();
			for(String key : keys) {
				
				try{
				TripDetailsDto localTrip = trips.get(key);
				TripDetailsDto proxyTrip = getAdhocRoutingTrip(key);
				 
				
				proxyTrip.setTripBasedDistance(localTrip.getTripBasedDistance());
				   System.out.println("VEHICLE TYPE OF THE TRIP : " + proxyTrip.getVehicle());
				   VendorDto vendorDto = getVendorAdhocRoutingTrip(key);
					List<BillingProcess> bpList= BillingProcessFactory.getProcessInstances(proxyTrip, String.valueOf(TransportTypeConfig.ADHOC),proxyTrip.getVehicleTypeId(), vendorDto );
				 
				System.out.println("_+++++++++++++++++++++++-___");
				Map<String, String> tot = new HashMap<String, String>(); 
				for(BillingProcess bp: bpList ) {
					Map<String, String> params = bp.calculateValues(proxyTrip);
					tot.putAll( params);
					
				}
				BillingProcessDao billingProcessDao = new BillingProcessDaoImpl();
				billingProcessDao.save(proxyTrip, tot, "adhoc_td_billing_args");

				}catch(Exception e) {
					System.out.println("Error in report gen : "+e);
					System.out.println("SKipped the report gen of " + key);
				}
			}
	 
		
	}


	private VendorDto getVendorAdhocRoutingTrip(String tripId) {
	// TODO Auto-generated method stub
	return new AdhocDao().getVendorAdhocRoutingTrip( tripId);
}

	public int rejectTrip(String[] tripids) {
		return new AdhocRoutingDao().rejectTrip(tripids);
	}

	public  ArrayList<VendorDto> getMasterVendorList(ArrayList<TripDetailsDto> trips){
		// TODO Auto-generated method stub
		return new AdhocRoutingDao() . getMasterVendorList(trips );
		 
	}
	
	
	public  ArrayList<VendorDto> getAssignedMasterVendorList(ArrayList<TripDetailsDto> trips){
		// TODO Auto-generated method stub
		return new AdhocRoutingDao() . getAssignedMasterVendorList(trips );
		 
	}
	
	public int assignVendorToAdhocTrip(VendorTripDto dto) {
		int returnInt=0;
		if(dto.getSelectedtrip()!=null&&dto.getSelectedtrip().length>0) {
			ArrayList<AdhocTripHBMDto> hdtoList = new ArrayList<AdhocTripHBMDto>();
			System.out.println(" Selected trip : " + dto.getSelectedtrip().length);
			for(String tripId: dto.getSelectedtrip() ) {
				AdhocTripHBMDto hdto = new AdhocTripHBMDto();
				hdto.setTripId(Long.parseLong( tripId));
				hdto.setVendorId(dto.getVendor());
				hdtoList.add(hdto);
				System.out.println(hdto);
			}
			return new AdhocRoutingDao().assignVendor(hdtoList);
		}
		return returnInt;
	}
}
