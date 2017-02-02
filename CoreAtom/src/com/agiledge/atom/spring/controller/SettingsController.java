package com.agiledge.atom.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.service.SiteService;

@Controller
public class SettingsController {
	
	
	
	
	SettingsService service = new SettingsService();
	
	
	
	@RequestMapping(value="/deviceSettings", method=RequestMethod.GET) 
	public ModelAndView showDeviceParams() {
		System.out.println(" insde showDeviceParamSettings ");
		ModelAndView model = new ModelAndView("deviceConfiguration");
		ArrayList<SiteDto> sites = (ArrayList<SiteDto>) new SiteService().getSites();
		String trueFalseLabel[] ={"Yes","No" };
		String trueFalseValue[] ={"TRUE","FALSE" };
		model.addObject("trueFalseLabel",trueFalseLabel);
		model.addObject("trueFalseValue",trueFalseValue);
		model.addObject("sites",sites);
		model.addObject("showDeviceParamSettings", new SettingsDTO());
		
		return model;
	}
	
	@RequestMapping ( value="/updateSettings", method=RequestMethod.POST
			  )
	public  @ResponseBody String updateSettingts( HttpServletRequest request, HttpServletResponse response ) throws IOException, JSONException {
		System.out.println(" >>>>>>>>>>>  post");
		System.out.println(" Property " + request.getParameter("property"));
		System.out.println(" value " + request.getParameter("value"));
		System.out.println(" Effecctiive date  " + request.getParameter("fromDate"));
		SettingsDTO dto = new SettingsDTO(); 
			dto.setProperty(request.getParameter("property"));
			dto.setValue(request.getParameter("value"));
			dto.setSite(request.getParameter("site"));
			System.out.println("Site :"+ dto.getSite());
			 
			dto.setEffectivedate(  OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(request.getParameter("fromDate")));
			 
			dto.setModule(SettingsConstant.TRACKING_MODULE);
		 
		 
		  Calendar cal=Calendar.getInstance();
		  Date today  = new Date();
		  cal.setTime(today);
		 today=OtherFunctions.sqlFormatToDate( 
				 OtherFunctions.changeDateFromatToSqlFormat(today));
		// stringToDate(sqlFormat) 
		// today=cal.getTime();
		 
		 int val = 0;
		 String errorMessage = "";
		  System.out.println("today : "+ today);
		  System.out.println("effective : "+ dto.getEffectivedate() + " " + today.compareTo(dto.getEffectivedate()));
		  if(OtherFunctions.isEmpty(dto.getValue())) {
			  errorMessage = "Value is invalid";
		  } else if(today.compareTo(dto.getEffectivedate())>0) {
			 errorMessage = "Date is invalid";
		 } else {
			 
			 val = SettingsService.updateSettings(dto);
			  
		 if(val==0) {
			 errorMessage = "Update failed";
		 }
		 }
		 /*
		 response.getWriter().print("{result:true, message=\"SUccess\"}");
		 response.getWriter().flush();*/
		 //return  "{result:true, message:\"SUccess\"}";
		JSONObject retObject = new JSONObject();
		if(val>0) {
		retObject.put("result", true);
		retObject.put("message", "Updated succefully");
		} else {
			retObject.put("result", false);
			retObject.put("message", errorMessage);
		}

		return retObject.toString();
	}

	@RequestMapping ( value="/showSettings", method=RequestMethod.GET
			  )
	public  ModelAndView showPropertyHistory( HttpServletRequest request, HttpServletResponse response ) throws IOException, JSONException {
		 SettingsService service = new SettingsService();
		 SettingsDTO dto = new SettingsDTO();
		 dto.setModule(SettingsConstant.TRACKING_MODULE);
		 dto.setProperty(request.getParameter("property"));
		  try {
			  System.out.println("show settings");
		   dto.setSiteid(Integer.parseInt( request.getParameter("site")) );
		  }catch(Exception e) {
			  dto.setSiteid(-1);
		  }
		 ArrayList<SettingsDTO>  dtoList = service.getSettingsValues(dto);
		 
		ModelAndView model = new ModelAndView("showDeviceSettings");
		model.addObject("dtoList",dtoList);
		return model;
	}

	  
	

	 

	 

}
 