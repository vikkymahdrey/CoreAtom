package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.TripGenChooseDto;
import com.agiledge.atom.hibernate.dao.TripRegnDao;

public class TripRegenService {
	ArrayList<TripDetailsChildDto> remEmpList = null;
public String empToTravelStatus;
	public ArrayList<TripDetailsDto> purifyTrips(
			ArrayList<TripDetailsDto> tripSheetList) {

		ArrayList<TripDetailsDto> purifiedTripList = new ArrayList<>();
		int iidx = 0;
		// for(TripDetailsDto dto : tripSheetList) {
		for (; iidx < tripSheetList.size(); iidx++) {
			TripDetailsDto dto = tripSheetList.get(iidx);
			ArrayList<TripDetailsChildDto> purifiedList = new ArrayList<>();
			for (int i = 0; i < dto.getTripDetailsChildDtoList().size(); i++) {
				String delStatus = dto.getTripDetailsChildDtoList().get(i)
						.getStatus();
				if (delStatus != null && delStatus.equalsIgnoreCase("empty")) {
					;
				} else {
					purifiedList.add(dto.getTripDetailsChildDtoList().get(i));
				}
			}
			if (purifiedList.size() > 0) {
				dto.setTripDetailsChildDtoList(purifiedList);
				purifiedTripList.add(dto);
			}

		}
		return purifiedTripList;
	}

	public ArrayList<TripDetailsDto> getTripForRegen(TripGenChooseDto dto) {
		String remEMpString = "";

		// System.out.println("tripListModified         "+tripListModified.size());
		remEmpList = new ArrayList<TripDetailsChildDto>();
		TripRegnDao regenDao = new TripRegnDao();

		ArrayList<RoutingDto> list = regenDao.getAllroutingDtos(
				dto.getSiteId(), dto.getToDate(), dto.getToLog(),
				dto.getToTime());
		Map<String,RoutingDto> empsToTravel = new LinkedHashMap<String,RoutingDto>();
		ArrayList<TripDetailsDto> tripDtos = null;
		try {
			// for (RoutingDto routinDto : list) {
			tripDtos = new TripDetailsDao().getTripSheetSaved(dto.getFromDate(), dto.getFromLog(), dto.getSiteId(),dto.getFromTime());
			empsToTravel = regenDao.getEmployeesForTravel(Integer.parseInt(dto.getSiteId()), dto.getToDate(),dto.getToTime(), dto.getToLog());
			if(!(regenDao.empsTotravelStatus.equalsIgnoreCase("No employee To Travel")||regenDao.empsTotravelStatus.equalsIgnoreCase("Routing Done")))
			{	
			try{
			if (tripDtos != null && tripDtos.size() > 0) {		
				for (int j = 0; j < tripDtos.size(); j++) {
					ArrayList<TripDetailsChildDto> tripListModifiedSubList = new ArrayList<TripDetailsChildDto>();
					tripListModifiedSubList.addAll(tripDtos.get(j).getTripDetailsChildDtoList());
					boolean isChangeFlag = false;
					for (int i = 0; i < tripDtos.get(j).getTripDetailsChildDtoList().size(); i++) {
						if (empsToTravel != null&& empsToTravel.containsKey(tripDtos.get(j).getTripDetailsChildDtoList().get(i).getEmployeeId())) {
							String landmark=empsToTravel.get(tripDtos.get(j).getTripDetailsChildDtoList().get(i).getEmployeeId()).getEmployeeLandmark();
							if(tripDtos.get(j).getTripDetailsChildDtoList().get(i).getLandmarkId().equalsIgnoreCase(landmark))							
									{
								empsToTravel.remove(tripDtos.get(j).getTripDetailsChildDtoList().get(i).getEmployeeId());
									}
							else
									{
								tripListModifiedSubList.remove(tripDtos.get(j).getTripDetailsChildDtoList().get(i));
									}									
						} else {
							isChangeFlag = true;
							tripListModifiedSubList.remove(tripDtos.get(j).getTripDetailsChildDtoList().get(i));
						}

					}
					tripDtos.get(j).setTripDetailsChildDtoList(tripListModifiedSubList);
					if (!dto.getFromLog().equalsIgnoreCase(dto.getToLog())) {
					Collections.reverse(tripDtos.get(j).getTripDetailsChildDtoList());					
					//tripDtos.get(j).setTrip_log(dto.getToLog());
					}
					if (!dto.getFromLog().equalsIgnoreCase(dto.getToLog())) {
						if (isChangeFlag==false&& dto.getFromLog().equalsIgnoreCase("IN")&& dto.getToLog().equalsIgnoreCase("OUT")) {
							float remDist = (float) 0.0;
							for (int k = tripDtos.get(j).getTripDetailsChildDtoList().size()-1; k >= 0; k--) {
								if (k == 0) {
									tripDtos.get(j).getTripDetailsChildDtoList().get(k).setDistance(Float.parseFloat(tripDtos.get(j).getDistance())- remDist);
								} else {
									remDist += tripDtos.get(j).getTripDetailsChildDtoList().get(k - 1).getDistance();
									tripDtos.get(j).getTripDetailsChildDtoList().get(k).setDistance(tripDtos.get(j).getTripDetailsChildDtoList().get(k-1).getDistance());
								}
							}
						}
						if (isChangeFlag==false&& dto.getFromLog().equalsIgnoreCase("OUT")&& dto.getToLog().equalsIgnoreCase("IN")) {
							for (int k = tripDtos.get(j).getTripDetailsChildDtoList().size()-1; k >=0; k--) {
								if (k ==0) {
									tripDtos.get(j).getTripDetailsChildDtoList().get(k).setDistance(0);
								} else {
									tripDtos.get(j).getTripDetailsChildDtoList().get(k).setDistance(tripDtos.get(j).getTripDetailsChildDtoList().get(k - 1).getDistance());
								}
							}
						}
					//	tripDtos.get(j).setTrip_log(dto.getToLog());
					}
					if(isChangeFlag)
					{
						tripDtos.get(j).setStatus("modified");	
					}
					tripDtos.get(j).setTrip_log(dto.getToLog());
					tripDtos.get(j).setTrip_date(dto.getToDate());
					tripDtos.get(j).setTrip_time(dto.getToTime());
				}
			}
			}catch(Exception e)
			{
			System.out.println("ERror in for loop"+e);	
			}		
			if (empsToTravel != null && empsToTravel.size() > 0) {
				for (String remEMps : empsToTravel.keySet()) {
					remEMpString += "" + remEMps + ",";
				}
				remEMpString = remEMpString.substring(0,
						remEMpString.length() - 1);
				remEmpList = regenDao.getRemainingEmpList(dto.getToDate(),
						remEMpString);
			}
			for (TripDetailsDto ndto : tripDtos) {
				for (int i = ndto.getTripDetailsChildDtoList().size(); i < ndto
						.getSeat(); i++) {
					TripDetailsChildDto newChildDto = new TripDetailsChildDto();
					newChildDto.setEmployeeName("EMPTY");
					newChildDto.setArea("*");
					newChildDto.setPlace("*");
					newChildDto.setLandmark("*");
					newChildDto.setStatus("EMPTY");
					ndto.getTripDetailsChildDtoList().add(newChildDto);
				}
			}

			empToTravelStatus=""; 
			}
			else
			{
				empToTravelStatus=regenDao.empsTotravelStatus;
			}
		} catch (Exception e) {
			System.out.println("Eroro in reg service" + e);
		}	
		return tripDtos;

	}

	public ArrayList<TripDetailsChildDto> getPoolEmps() {
		return remEmpList;
	}

	public int InsertTrip(ArrayList<TripDetailsDto> tripDtoList) {
		TripRegnDao dao = new TripRegnDao();		
		for(TripDetailsDto tripDto:tripDtoList)
		 {
		//	if(tripDto.getStatus()==null||tripDto.getStatus().equalsIgnoreCase("null")||tripDto.getStatus().equalsIgnoreCase("modified"))						
			//		{
					dao.updateDsitances(tripDto);					
					dao.updateTime(tripDto);					
			//		}
		 }	
			return dao.insertTrip(tripDtoList);
	}
}
