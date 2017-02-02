package com.agiledge.atom.spring.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.http.HttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.payroll.service.PayrollConfigService;
import com.agiledge.atom.payroll.service.PayrollConfigServiceImpl;
import com.agiledge.atom.payrollsettings.dto.PayrollConfigConstants;
import com.agiledge.atom.payrollsettings.dto.PayrollConfigDto;
import com.agiledge.atom.service.PayrollService;
import com.agiledge.atom.service.SiteService;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;
import com.agiledge.atom.transporttype.service.TransportTypeService;

@Controller
public class PayrollConfigController {
	PayrollConfigService service = new PayrollConfigServiceImpl();
	@RequestMapping(value = "/addPayrollConfig", method = RequestMethod.GET)
	public ModelAndView showPayrollConfigSetup(HttpServletRequest request, HttpSession session) {
		ModelAndView mav = new ModelAndView("payrollSettingsConfig");
 		String site = session.getAttribute("site").toString();
 		ArrayList<SiteDto> sites = (ArrayList<SiteDto>) new SiteService().getSites();
 		ArrayList<TransportTypeDto> transportTypes = new TransportTypeService().getTransportTypes();
 		ArrayList<PayrollConfigDto> list = service.getPayrollConfigs();
 		Map<String, String> payrollTypes = new HashMap<String, String>();
 		payrollTypes.put(PayrollConfigConstants.flatrate, "Flat Rate");
 		payrollTypes.put(PayrollConfigConstants.prorata, "Prorata");
 		payrollTypes.put(PayrollConfigConstants.oneway, "Oneway");
 		PayrollConfigDto dto = new PayrollConfigDto();
 		dto.setSite(site);
 			mav.addObject("list",list);
			mav.addObject("sites", sites);
			mav.addObject("transportTypes", transportTypes);
			   
			mav.addObject("payrollTypes", payrollTypes);
			mav.addObject("site", site);
			  
			mav.addObject("payrollEntry", new PayrollConfigDto());
			 
 
		return mav;
	}
	
	
	@RequestMapping(value = "/addPayrollConfig", method = RequestMethod.POST)
	public ModelAndView updatePayrollConfig(@ModelAttribute("payrollEntry") PayrollConfigDto  dto, HttpSession session, HttpServletRequest request) {
		ModelAndView mav = new ModelAndView("payrollSettingsConfig");
		System.out.println(" in update ..........");
		dto.setFromDate(new Date());
		service.updatePayrollConfig(dto);
		
		return showPayrollConfigSetup(request, session);

 
	}

}
