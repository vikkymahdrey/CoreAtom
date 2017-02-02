package com.agiledge.atom.spring.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;


import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dao.VehicleTypeDao;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.TripGenChooseDto;
import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.service.SiteService;
import com.agiledge.atom.service.TripRegenService;
import com.agiledge.atom.trip.dto.TripForm;

@Controller
public   class TripGenController {
	
	TripRegenService service = new TripRegenService();

	@RequestMapping(value="gettripgen", method=RequestMethod.GET )
	public ModelAndView getTripRegenPage() {
		System.out.println(" in gettripgen ");
		ArrayList<SiteDto> sites = (ArrayList<SiteDto>) new SiteService().getSites();
		 Map<String,String> siteMap = new HashMap<String,String>();
			for (SiteDto i : sites) {
				 
				siteMap.put(String.valueOf( i.getId()) ,i.getName() );
			}
		
		ModelAndView mv= new ModelAndView("tripRegen");
		mv.addObject("sites" , siteMap);
		mv.addObject("tripRegen",new TripGenChooseDto());
		mv.addObject("tripForm", new TripForm());
		return mv;
	}

	@RequestMapping(value="gettripgenaccept", method=RequestMethod.POST )
	public ModelAndView getTripRegenPage(@ModelAttribute("tripRegen") TripGenChooseDto tgcDto,HttpServletResponse response,HttpSession session) {

		if(service==null) {
			service = new TripRegenService();
		}
		ModelAndView mv= new ModelAndView("tripRegen");
//		mv.addObject("tripDetails",new Arr );
		System.out.println(" FromDate : " + tgcDto.getFromDate());
		System.out.println(" ToDate : " + tgcDto.getToDate());
		 System.out.println(" site : " + tgcDto.getSiteId());
		 System.out.println(" formtime :" + tgcDto.getFromTime());
		 System.out.println(" totime :" + tgcDto.getToTime());
		 tgcDto.setFromDate(OtherFunctions.changeDateFromatToIso(tgcDto.getFromDate()));
		 tgcDto.setToDate(OtherFunctions.changeDateFromatToIso(tgcDto.getToDate()));
		 ArrayList<TripDetailsDto> tripSheetList= service.getTripForRegen(tgcDto);
		 tgcDto.setFromDate(OtherFunctions.changeDateFromatddmmyy(tgcDto.getFromDate()));
		 tgcDto.setToDate(OtherFunctions.changeDateFromatddmmyy(tgcDto.getToDate())); 
		System.out.println("size :"+ tripSheetList.size());
		ArrayList<VehicleTypeDto> vehicleTypeList =  new VehicleTypeDao().getAllVehicleTypeBySite(Integer.parseInt( tgcDto.getSiteId()));
		VehicleTypeDto blankVehicle = new VehicleTypeDto();
		blankVehicle.setId(-1);
		blankVehicle.setType("-- Select --");
		vehicleTypeList.add(blankVehicle);
//		ArrayList< TripDetailsDto > tripList = new TripDetailsService().getTripSheetActual(tgcDto.getFromDate(), tgcDto.getFromLog(), "1", tgcDto.getFromTime());
		System.out.println(tripSheetList.size());
		TripForm tf= new TripForm();
		tf.setTrip_date(tgcDto.getToDate());
		tf.setTrip_time(tgcDto.getToTime());
		tf.setTrip_log(tgcDto.getToLog());
		tf.setSiteId(tgcDto.getSiteId());
		tf.setTripList(tripSheetList);
		
		ArrayList< TripDetailsChildDto> empList = service.getPoolEmps();
		  
		 ArrayList<SiteDto> sites = (ArrayList<SiteDto>) new SiteService().getSites();
		 Map<String,String> siteMap = new HashMap<String,String>();
			for (SiteDto i : sites) {
				 
				siteMap.put(String.valueOf( i.getId()) ,i.getName() );
			}
			ArrayList<LogTimeDto> logTimeDtos = new LogTimeDao().getAllLogtime(tgcDto.getFromLog(), tgcDto.getSiteId());
		Map<String,String> fromTimeMap = new LinkedHashMap<String,String>();
			for (LogTimeDto i : logTimeDtos) {
				 
				fromTimeMap.put(String.valueOf( i.getLogTime()) ,i.getLogTime() );
			}
		 
		 
			 logTimeDtos = new LogTimeDao().getAllLogtime(tgcDto.getToLog(), tgcDto.getSiteId());
			Map<String,String> toTimeMap = new LinkedHashMap<String,String>();
				for (LogTimeDto i : logTimeDtos) {
				
					toTimeMap.put(String.valueOf( i.getLogTime()) ,i.getLogTime() );
				}
			Map<String, String > securityLabels = new HashMap<>();
			securityLabels.put("YES" , "YES");
			securityLabels.put("NO",  "NO");
		mv.addObject("securityLabels", securityLabels);
		mv.addObject("sites" , siteMap);
		mv.addObject("fromTimeMap", fromTimeMap);		 
		mv.addObject("toTimeMap", toTimeMap);
		mv.addObject("tripForm", tf);
		mv.addObject("vehicleTypeList", vehicleTypeList);
		System.out.println(" List size : " + tf.getTripList().size());
		 
		Map<String,String> map = new HashMap<String,String>();
		for (VehicleTypeDto i : vehicleTypeList) {
			System.out.println("id : " + i.getId() + " value : " + i.getType());
			String key =String.valueOf( i.getId());
			if(i.getId()<0) key="";
			map.put( key ,i.getType());
		}
		System.out.println("............");
		mv.addObject("vehicleTypeMap", map);
		mv.addObject("employeePool", empList);
		mv.addObject("tripRegen", tgcDto);
		if(service.empToTravelStatus!=null&&(service.empToTravelStatus.equalsIgnoreCase("No employee To Travel")||service.empToTravelStatus.equalsIgnoreCase("Routing Done")))
		{
			session.setAttribute("status",
					"<div class=\"failure\" > "+service.empToTravelStatus+" </div>");
			mv= new ModelAndView("redirect:/message.jsp");	
		}
		return mv;

	}
	
	 	
	@RequestMapping(value="tripgensubmit", method=RequestMethod.POST)
	public ModelAndView submitTripRegenPage(
			@ModelAttribute("tripRegen") TripForm tripForm,
			HttpSession session
			) {
		if(service==null) {
			service = new TripRegenService();
		}
		System.out.println("Trip Date :" + tripForm.getTrip_date());
		if(tripForm.getTripList()==null || tripForm.getTripList().size()==0 ) {
			session.setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > No Trips</div>");
		}else {
			ArrayList<TripDetailsDto> tripSheetList  = tripForm.getTripList();
			System.out.println(" Size of tripSheetList : " + tripForm.getTripList().size());
			for(TripDetailsDto ndto:tripSheetList)
 
			{
			System.out.println(" status"+ndto.getStatus()+ndto.getTrip_log()+" "+ndto.getTrip_time());	
 
			for(TripDetailsChildDto pDto:ndto.getTripDetailsChildDtoList())
			{
			System.out.println(pDto.getEmployeeId());
			 
			}
			}
			ArrayList<TripDetailsDto> purifiedTripList  = service.purifyTrips(tripSheetList);
			
			System.out.println(" Purified List size " + purifiedTripList.size());
 			for(TripDetailsDto ndto:purifiedTripList)
			{
			System.out.println(" status"+ndto.getStatus()+ndto.getTrip_log()+" "+ndto.getTrip_time());		
			for(TripDetailsChildDto pDto:ndto.getTripDetailsChildDtoList())
			{
			System.out.println(pDto.getEmployeeId());	
			}
			}
			
			
	 		if(service.InsertTrip(purifiedTripList) > 0)  {
  
				session.setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Saved successfully</div>");
			} else {
				session.setAttribute("failure",
						"<div class=\"failure\" width=\"100%\" > Saved successfully</div>");
			}
		}
		 return getTripRegenPage();
	}
	

	
}
