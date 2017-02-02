package com.agiledge.atom.spring.controller;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agiledge.atom.reports.dto.DelegationReportDto;
import com.agiledge.atom.spring.dao.DelegateReportDAO;
@Controller
public class DelegateController {
	
	@Autowired
	DelegateReportDAO drd;

	
	@RequestMapping(value="/display_delegationReport2.do", method={RequestMethod.GET, RequestMethod.POST})	
	public String empDelegationHandler(ModelMap modelMap,HttpServletRequest request)
	{
		
		String from_Date=request.getParameter("fromDate");
		String to_Date=request.getParameter("toDate");
		List<DelegationReportDto> delegateList = null;
		
		if(from_Date!=null&&to_Date!=null)
		{
			delegateList=drd.getDelegationReport(from_Date,to_Date);
		}
		
		modelMap.addAttribute("empDelegateList",delegateList);
		  
		return "/display_delegationReport2";
	}
}

