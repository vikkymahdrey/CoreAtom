package com.agiledge.atom.transporttype.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;
import com.agiledge.atom.transporttype.dao.TransportTypeDao;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;


public class TransportTypeService {
	private String message;
	TransportTypeDao dao = new  TransportTypeDao();
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	
	public ArrayList<TransportTypeDto> getTransportTypes() {
		return dao.getTransportTypes();
		
	}
	
	
	
	public boolean  checkAnyFutureEntry(TransportTypeDto dto) {
		return dao.checkAnyFutureEntry(dto);
	}
	
	public int mapTransportType(TransportTypeDto dto) {
		int returnInt=0;
		try {
			dao.Connect();
			dao.setAutoCommit(false);
			if(dao.checkAnyFutureEntry(dto)) {
				this.setMessage("Mapping Failed :Please Delete Existing Entry");
				
			}else {
			returnInt = dao.mapTransportType(dto);
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
	
	public ArrayList<TransportTypeDto> getTransportTypeMappings(String param) {
		return dao.getTransportTypeMappings(param);
	}
	
	
	public boolean checkIsFutureEntry(TransportTypeDto dto) {
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
	public int deleteTransportTypeMapping(TransportTypeDto dto) {
		 
		int value=0;
			try {
				if(dto==null) {
					setMessage("Error : Invalid Entry");
				} else {
					if(checkIsFutureEntry(dto)) {
						value = dao.deleteTransportTypeMapping(dto);
					}
				}
			} catch(NullPointerException nu) {
				setMessage("Error :" + nu);
			}
		return value;
	}
	public TransportTypeDto getTransportTypeMapping(String id) {
		 return dao.getTransportTypeMappingById(id);
	}
	
	public ArrayList<TransportTypeDto> getTransportTypeMappings(TransportTypeDto dto) {
		return dao.getTransportTypeMappings(dto);
	}
	
	public JSONObject getTransportTypeMappingsInJSON(TransportTypeDto dto) throws JSONException {
		ArrayList<TransportTypeDto>  dtoList = dao.getTransportTypeMappings(dto);
		dto.setStatus("future");
		dtoList.addAll( dao.getTransportTypeMappings(dto ));
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
	
}
