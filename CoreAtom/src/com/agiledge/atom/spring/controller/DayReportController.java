package com.agiledge.atom.spring.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;




import com.agiledge.atom.dto.DayReportDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.DayReportService;
import com.agiledge.atom.service.SiteService;
@Controller
public class DayReportController {
	@RequestMapping(value="/dayReport", method = { RequestMethod.GET, RequestMethod.POST })	
public ModelAndView setDayReportFields(@ModelAttribute("reportDto") DayReportDto dayreportDto)
{
		
	List<SiteDto> sites = new SiteService().getSites();
	  ModelAndView model = new ModelAndView("dayReport");
	  
	  dayreportDto=new DayReportService().getDayReport(dayreportDto);
	  
	  model.addObject("sites", sites);
	  model.addObject("empList", dayreportDto.getEmpScheduleList());
	  model.addObject("reportDto",dayreportDto);
	  
	  return model;
}


}
