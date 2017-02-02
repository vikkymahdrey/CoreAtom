package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dao.VendorDao;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.dto.VendorContractDto;
import com.agiledge.atom.dto.VendorDto;

public class VendorService {

	private VendorDao dao = new VendorDao();
	private String erroMessage = "";

	public ArrayList<VendorDto> getVendorList() {
		ArrayList<VendorDto> list = dao.getVendorList();
		return list;
	}

	public int addVendor(VendorDto dto) {

		return dao.addVendor(dto);
	}

	public int updateVendor(VendorDto dto) {
		System.out.println(" ....................");
		return dao.updateVendor(dto);
	}
	
	public int updateVendorMaster(VendorDto dto) {
		return dao.updateVendorMaster(dto);
	}

	public int deleteVendor(VendorDto dto) {
		//
		return dao.deleteVendor(dto);
	}

	public boolean validateVendorContract(VendorContractDto dto) {

		boolean flag = true;
		try {
			if (dto.getDescription() == null
					|| dto.getDescription().trim().equals("")) {
				setErroMessage("Contract description should not be blank");
				flag = false;
			} else if (dto.getRate() == null || dto.getRate().trim().equals("")) {
				setErroMessage("Rate should not blank");
				flag = false;
			}
			try {
				double val = Double.parseDouble(dto.getRate().trim());

			} catch (NumberFormatException e) {
				flag = false;
				setErroMessage("Contract rate is invalid");
			}
		} catch (Exception e) {
			flag = false;
			setErroMessage(e.toString());
		}
		return flag;
	}

	public String getErroMessage() {
		return erroMessage;
	}

	public void setErroMessage(String erroMessage) {
		this.erroMessage = erroMessage;
	}

	public int doVendorContract(VendorContractDto dto) {

		return dao.doVendorContract(dto);
	}

	public ArrayList<VendorDto> getVendorInSite(String siteId) {
		return dao.getVendorInSite(siteId);
	}

	public ArrayList<VendorDto> getVendorNotInSite(String siteId) {
		return dao.getVendorNotInSite(siteId);
	}
	public int addVendorMaster(VendorDto dto) {
		 int val=0;
		 val = dao.addVendorMaster(dto);
		return val;
	}

	public int deleteVendorMaster(VendorDto dto) {
		// TODO Auto-generated method stub
		return dao.deleteVendorMaster(dto);
	}
	public ArrayList<VendorDto> getMasterVendorlist() {
		return dao.getMasterVendorList();
	}

	public int assignVendorTrip(String vendorCompanyIdId, String[] trips) {
		return dao.assignVendorTrip(vendorCompanyIdId, trips);
	}

	public ArrayList<TripDetailsDto> getAssignedTrip(String vendorId,
			String siteId, String tripDate, String tripTime[], String tripLog,
			String src) {
		return new TripDetailsDao().getVendorAssignedTrip(vendorId, siteId,
				tripDate, tripTime, tripLog, src);
	}
	
	public ArrayList<TripDetailsDto> getAssignedTrip1(String siteId, String tripDate,
			String tripTime[], String tripLog, String src) {
		return new TripDetailsDao().getVendorAssignedTrip1( siteId,
				tripDate, tripTime, tripLog, src);
	}

	Map<String, ArrayList<VehicleDto>> vendorVehicle = null;

	public void loadVehiclesVendor(String vendorId) {
		String vehicletype = "";
		vendorVehicle = new HashMap<String, ArrayList<VehicleDto>>();
		ArrayList<VehicleDto> dtos = new VendorDao()
				.getVendorVehicles(vendorId);
		ArrayList<VehicleDto> typeVehicle = null;
		for (VehicleDto dto : dtos) {
			if (!vehicletype.equals(dto.getVehicleType())) {
				
				vehicletype = dto.getVehicleType();
				typeVehicle = new ArrayList<VehicleDto>();
				typeVehicle.add(dto);
			//	System.out.println(dto.getVehicleType()+"  "+typeVehicle.size());
				vendorVehicle.put(dto.getVehicleType(), typeVehicle);

			} else {

				typeVehicle.add(dto);
			}
		}
	}

	
	public void loadVehicles() {
		String vehicletype = "";
		vendorVehicle = new HashMap<String, ArrayList<VehicleDto>>();
		ArrayList<VehicleDto> dtos = new VehicleDao().getAllVehicle();
		ArrayList<VehicleDto> typeVehicle = null;
		for (VehicleDto dto : dtos) {
			if (!vehicletype.equals(dto.getVehicleType())) {
				
				vehicletype = dto.getVehicleType();
				typeVehicle = new ArrayList<VehicleDto>();
				typeVehicle.add(dto);
			//	System.out.println(dto.getVehicleType()+"  "+typeVehicle.size());
				vendorVehicle.put(dto.getVehicleType(), typeVehicle);

			} else {

				typeVehicle.add(dto);
			}
		}
	}
	
	
	public ArrayList<VehicleDto> getVendorVehicles(String vehicleType) {
		try {		
			if(SettingsConstant.felxibleVehicleType.equalsIgnoreCase("true"))
			{
				ArrayList<VehicleDto> veh=new ArrayList<VehicleDto>();
				for(Map.Entry<String, ArrayList<VehicleDto>> vehicles:vendorVehicle.entrySet())
				{
				veh.addAll(vehicles.getValue());	
				}
				return veh;
						
				
			}
			else
			{
			return vendorVehicle.get(vehicleType);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
			return null;
		}
	}

	public int assaginTripVehicle(ArrayList<VehicleDto> tripVehicles) {
		
	    int returnVal=new TripDetailsDao().updateTravelTime(tripVehicles);
		int returnvalue= new TripDetailsDao().assaginTripVehicle(tripVehicles);
		if (returnvalue>0 && SettingsConstant.TRACKING_METHOD.equalsIgnoreCase("DOUBLE_APPROVAL"))
		{
			int statusreturn=new TripDetailsDao().updateApprovalStatus(tripVehicles);
		}
		
		return returnvalue;
	}

}
