package com.agiledge.atom.billingtype.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.billingtype.config.dao.KmBasedBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.KmBasedBillingConfigDto;
import com.agiledge.atom.billingtype.dao.BillingTypeDao;
import com.agiledge.atom.billingtype.dto.BillingTypeConstant;
import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.dto.VehicleTypeDto;

public class BillingTypeService {
	private String message;
	BillingTypeDao dao = new  BillingTypeDao();
	
	public String getMessage() {
		return message;
	}
	
	public String getCondtionUrl(int param) {
		String url="";
		System.out.println(" param :  " + param);
		if(param==BillingTypeConstant.KM_BASED_BILLING_TYPE) {
			url="kmBasedTripBillingConfig.jsp";
		} else if(param==BillingTypeConstant.SLAB_BASED_BILLING_TYPE) {
			url="slabBasedBillingConfig.jsp";
		} else if(param==BillingTypeConstant.TRIP_BASED_BILLING_TYPE) {
			url="tripBasedBillingConfig.jsp";
		}  
		
		return url;
	}
	
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ArrayList<BillingTypeDto> getBillingTypes() {
		return dao.getBillingTypes();
		
	}
	
	public boolean  checkAnyFutureEntry(BillingTypeDto dto) {
		return dao.checkAnyFutureEntry(dto);
	}
	
	public int mapBillingType(BillingTypeDto dto) {
		int returnInt=0;
		try {
			dao.Connect();
			dao.setAutoCommit(false);
			if(dao.checkAnyFutureEntry(dto)) {
				this.setMessage("Mapping Failed :Please Delete Existing Entry");
				
			}else {
			returnInt = dao.mapBillingType(dto);
			if(returnInt<0) {
				this.setMessage("Mapping Failed : Unable to map");
				dao.rollback();
			} else {
			dao.commit();
			}
			}
		} catch(Exception e) {
			try {
				dao.rollback();
				this.setMessage("Mapping Failed : "+e);
			} catch (SQLException Ignor) {
				// TODO Auto-generated catch block
				 ;
			}
		}
		return  returnInt;
	}
	
	public ArrayList<BillingTypeDto> getBillingTypeMappings(String param) {
		ArrayList<BillingTypeDto>  dtoList = dao.getBillingTypeMappings(param);
		
		return dtoList;
	}
	
	
	public ArrayList<BillingTypeDto> getCurrentBillingTypeMappings(String siteId , String vendorId, String transportType) {
		ArrayList<BillingTypeDto>  dtoList = dao.getCurrentBillingTypeMappings(siteId, vendorId, transportType);
		
		return dtoList;
	}
	
	public ArrayList<String>  getCurrentBillingTypeMappingsKeys(String siteId , String vendorId, String transportType) {
		ArrayList<String> billingTypes = new ArrayList<String>();
		ArrayList<BillingTypeDto>  dtoList = dao.getCurrentBillingTypeMappings(siteId, vendorId, transportType);
		for ( BillingTypeDto dto : dtoList) {
			 System.out.println(dto.getKey());
				if(dto.getKey().equals(BillingTypeConstant.KM_BASED_BILLING_TYPESTRING) ) {
					try { KmBasedBillingConfigDto kmDto =new KmBasedBillingTypeConfigDao().getKmBasedBillingConfig(dto.getId());
					 
					billingTypes.add(kmDto.getKmBillingType());
					} catch(NullPointerException ignor) {
					 ;
					}
					
				} else {
					billingTypes.add(dto.getKey());
				}
		}
		return billingTypes;
		
	}
	
	public boolean checkIsFutureEntry(BillingTypeDto dto) {
		// V\
		boolean flag=false;
		try {
			Date toDay = new Date();
			if(dto.getFromDate().compareTo(toDay)>0) {
				flag = true;
			}
		} catch(Exception e) {
			;
		}
		return flag;
	}
	public int deleteBillingTypeMapping(BillingTypeDto dto) {
		 
		int value=0;
			try {
				if(dto==null) {
					setMessage("Error : Invalid Entry");
				} else {
					if(checkIsFutureEntry(dto)) {
						value = dao.deleteBillingTypeMapping(dto);
					}
				}
			} catch(NullPointerException nu) {
				setMessage("Error :" + nu);
			}
		return value;
	}
	public int deleteVehicleKmRate(VehicleTypeDto dto) {
		 
		int value=0;
			try {
				if(dto==null) {
					setMessage("Error : Invalid Entry");
				} else {
					BillingTypeDto bdto = this.getBillingTypeMapping(dto.getRefId());
					if(  checkIsFutureEntry(bdto) ) {
						value = dao.deleteVehicleKmRate(dto);
					}
				}
			} catch(NullPointerException nu) {
				setMessage("Error :" + nu);
			}
		return value;
	}
	 

	public BillingTypeDto getBillingTypeMapping(String id) {
		 return dao.getBillingTypeMappingById(id);
	}
	
	public ArrayList<BillingTypeDto> getBillingTypeMappings(BillingTypeDto dto) {
		return dao.getBillingTypeMappings(dto);
	}
	
	public JSONObject getBillingTypeMappingsInJSON(BillingTypeDto dto) throws JSONException {
		ArrayList<BillingTypeDto>  dtoList = dao.getBillingTypeMappings(dto);
		JSONObject json = new JSONObject();
		if(dtoList!=null&&dtoList.size()>0) {
			json.put("collection", dtoList);
			System.out.println("JSON : "+ json.toString());
			json.put("result", "true");
			json.put("message", "");
 
		} else {
			json.put("result", "false");
			json.put("message", "No Data");
		}
		return json;
	}
	
	public ArrayList<VehicleTypeDto> getVehicleRates(String refId) {
		System.out.println("REF ID : "+ refId);
		return dao.getVehicleRates( refId);
	}

	public int addVehicleRate(VehicleTypeDto dto) {
		// TODO Auto-generated method stub
		int val=0;
		try {
			if(dao.checkDupliationinVehicleRate(dto)) {
				setMessage("Error : Already Exists");
			}else {
			val =dao.addVehicleRate(dto);
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			setMessage("Error : Insertion failed ");
		}
		return val;
	}
	
}

