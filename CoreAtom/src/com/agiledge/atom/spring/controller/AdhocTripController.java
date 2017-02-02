package com.agiledge.atom.spring.controller;

 
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import net.sf.json.JSONObject;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.dto.VendorTripDto;
import com.agiledge.atom.service.AdhocRoutingService;
import com.agiledge.atom.service.VendorService;
import com.agiledge.atom.trip.dto.TripForm;

@Controller
public class AdhocTripController {

	AdhocRoutingService service = new AdhocRoutingService();
	@RequestMapping(value="AdhocTripExists", method=RequestMethod.GET, produces=MediaType.APPLICATION_JSON_VALUE)
	public @ResponseBody String checkAdhocTripExists(@RequestParam(value="tripDate") String tripDate){
		TripDetailsDto dto = new TripDetailsDto();
		dto.setTrip_date(tripDate);
		dto.setStatus("saved");
		System.out.println(" hai ");
		String returnString ="{\"result\":\"false\"}";
		try {
			
		ArrayList<TripDetailsDto> savedList =service.getAdhocRoutingTrip(dto);
		dto.setStatus("routed");
		ArrayList<TripDetailsDto> routedList =service.getAdhocRoutingTrip(dto);
		JSONObject json = new JSONObject();
		if(routedList!=null&&routedList.size()>0) {
			json.put("routed", "true");
			json.put("routedNo", routedList.size());
		} else {
			json.put("routed", "false");
		}
		if(savedList!=null&&savedList.size()>0) {
			json.put("saved", "true");
			json.put("savedNo", savedList.size());
		} else {
			json.put("saved", "false");
		}
		json.put("result", "true");
		returnString = json.toString();
		}catch(Exception e) {
			System.out.println(e.toString());
		}
		System.out.println(" "+returnString);
		return returnString;
	}
	
	@RequestMapping(value="adhocVendorAssign", method=RequestMethod.GET)
	public ModelAndView adhocVendorTripAssign(
			@ModelAttribute(value="tripVendor") VendorTripDto vdto
			 
			) {
		ModelAndView model = new ModelAndView("assignVendorForAdhocTrips");
		  
		
		 
		 AdhocDto dto= new AdhocDto();
		 dto.getAdhocTypeString();
		 TripDetailsDto paramDto = new TripDetailsDto();
		  
		  
		 
		 
		ArrayList<TripDetailsDto> vendorAssignTrip =null;
		ArrayList<TripDetailsDto> vendorAssignedTrip = null;
		if(OtherFunctions.isEmpty( vdto.getTripDate())==false && OtherFunctions.isEmpty( vdto.getSiteId())==false) {
			paramDto.setTrip_date( vdto.getTripDate() );
			paramDto.setSiteId(vdto.getSiteId());
			vendorAssignTrip = new AdhocRoutingService().getAdhocRoutingTrip(paramDto);
		} else {
			if(vdto!=null&& OtherFunctions.isEmpty( vdto.getSiteId())==false &&OtherFunctions.isEmpty(vdto.getVendor()) 
					&& OtherFunctions.isEmpty(vdto.getTripDate())==false) {
		 TripDetailsDto paramDto1 = new TripDetailsDto();
		 paramDto1.setSiteId(vdto.getSiteId());
		 paramDto1.setTrip_date(vdto.getTripDate()); 
		 paramDto1.setVendorId(vdto.getVendor() );
		 vendorAssignedTrip = new AdhocRoutingService().getAdhocRoutingTrip(paramDto1);
			}
		
		}
		model.addObject( "vendorAssignTrip",vendorAssignTrip);
		model.addObject( "vendorAssignedTrip",vendorAssignedTrip);
		 
		ArrayList<VendorDto> masterVendorList = new VendorService() .getMasterVendorlist();
		 System.out.println(" master vendor list : " + masterVendorList.size());
		  
		model.addObject( "masterVendorList",masterVendorList);
		 
		return model;
	}
	
	//addAdhocVendorTrip

	@RequestMapping(value="addAdhocVendorTrip", method=RequestMethod.POST)
	public ModelAndView addAdhocVendorTripAssign( 
			@ModelAttribute(value="tripVendor") VendorTripDto vdto,
			  HttpSession session
			 
			) {
		ModelAndView model = new ModelAndView("assignVendorForAdhocTrips");
		  
		int val=	 new AdhocRoutingService().assignVendorToAdhocTrip(vdto);
		if (val <= 0) {
			session.setAttribute("status",
					"<div class=\"failure\" > Vendor Assignment Failed!</div>");
		} else {
			session.setAttribute("status",
					"<div class=\"success\" > Vendor Assignment Successfull !</div>");
		}

		 
		 AdhocDto dto= new AdhocDto();
		 dto.getAdhocTypeString();
		 TripDetailsDto paramDto = new TripDetailsDto();
		  
		  
		 
		 
		ArrayList<TripDetailsDto> vendorAssignTrip =null;
		ArrayList<TripDetailsDto> vendorAssignedTrip = null;
		if(vdto!=null &&  OtherFunctions.isEmpty( vdto.getTripDate())==false && OtherFunctions.isEmpty( vdto.getSiteId())==false) {
			paramDto.setTrip_date( vdto.getTripDate() );
			paramDto.setSiteId(vdto.getSiteId());
			vendorAssignTrip = new AdhocRoutingService().getAdhocRoutingTrip(paramDto);
	 
			if( OtherFunctions.isEmpty(vdto.getVendor())==false 
				 ) {
		 TripDetailsDto paramDto1 = new TripDetailsDto();
		 paramDto1.setSiteId(vdto.getSiteId());
		 paramDto1.setTrip_date(vdto.getTripDate()); 
		 paramDto1.setVendorId(vdto.getVendor() );
		 
		 vendorAssignedTrip = new AdhocRoutingService().getAdhocRoutingTrip(paramDto1);
		  	vendorAssignTrip = new AdhocRoutingService().getOtherAdhocRoutingTripForVendor(paramDto1);
	 
			}
		
		}
		model.addObject( "vendorAssignTrip",vendorAssignTrip);
		model.addObject( "vendorUnAssignedTrip",vendorAssignedTrip);
		 
		ArrayList<VendorDto> masterVendorList = new VendorService() .getMasterVendorlist();
		 System.out.println(" master vendor list : " + masterVendorList.size());
		  
		model.addObject( "masterVendorList",masterVendorList);
		 
		return model;
	}
		
	@RequestMapping(value="vendorAdhocTripApproval", method=RequestMethod.GET)
	public ModelAndView adhocTripApproval( 
			@ModelAttribute(value="tripVendor") TripDetailsDto vdto,
			  HttpSession session
			 
			) {
		ModelAndView model = new ModelAndView("vendor_adhocTripApproval");
		List<SiteDto> siteDtoList = new SiteDao().getSites();
		String approvalStatus[] = {"Open" };
		ArrayList<TripDetailsDto> tripList = new AdhocRoutingService(). 
				getSavedTripDetails(vdto.getSiteId(), vdto.getTrip_date(), approvalStatus);
		TripForm tf= new TripForm();
		tf.setTripList(tripList);
	 
		model.addObject("sites", siteDtoList);
		 
		model.addObject("tripDetailsDto", vdto);
		model.addObject("tripForm", tf);
		 
  
		return model;
	}

	
	@RequestMapping(value="vendorAdhocTripApproval", method=RequestMethod.POST)
	public ModelAndView trackAndApproveAdhocTrip( 
			@ModelAttribute(value="tripForm") TripForm vdto,
			  HttpSession session
			 
			) {
		// code to track and approve ..
		// ..............
		ModelAndView model = new ModelAndView("vendor_adhocTripApproval");
		List<SiteDto> siteDtoList = new SiteDao().getSites();
		String approvalStatus[] = {"Open" };
		ArrayList<TripDetailsDto> tripList = new AdhocRoutingService(). 
				getSavedTripDetails(vdto.getSiteId(), vdto.getTrip_date(), approvalStatus);
		TripForm tf= new TripForm();
		tf.setTripList(tripList);
		model.addObject("sites", siteDtoList);
		 
		model.addObject("tripDetailsDto", vdto);
		model.addObject("tripForm", tf);
		 
  
		return model;
	}

}
