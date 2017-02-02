package com.agiledge.atom.spring.controller;

import java.io.IOException;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.UploadedFile;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.SiteService;
@Controller
public class SubscriptionController {

 

	@RequestMapping(value="uploadSubscription", method=RequestMethod.GET)
	 public ModelAndView showUploadSchedule(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
			   BindingResult result) { 
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("subscriptionUpload");
			  
			  model.addObject("sites", sites);
			  return model;
			 }  
	

	
	@RequestMapping(value="uploadSubscription", method=RequestMethod.POST)
	 public ModelAndView uploadSchedule(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			   HttpSession session 
			    ) throws IOException { 
		System.out.println("chcek");
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("subscriptionUpload");
			  MultipartFile file = uploadedFile.getFile();
			  String fileName = file.getOriginalFilename();
			  System.out.println("file name: "+ fileName);
			  try{
				  EmployeeSubscriptionService sService = new EmployeeSubscriptionService();
				   
			     int val= sService.uploadSubscription(file.getInputStream(),uploadedFile.getSite(), session.getAttribute("user").toString());
			     if(val>0) {
			    	 
			    	 session.setAttribute("status",
								"<div class=\"success\" width=\"100%\" >Upload successfull</div>");
			     } else {
			    	 session.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" >Upload failed !</div>");
			     }
			     String reportHtml="<div> <h1>Status Report </h1><div><br/>";
			     if(sService.getMessageList()!=null && sService.getMessageList().size()>0) {
			    	 reportHtml+="<Div>";
			    	 for(String message : sService.getMessageList()) {
			    		  reportHtml+=message+"<br/>";
			    	 }
			    	reportHtml+="</Div>";
			     }
			     model.addObject("statusReport", reportHtml);
			  System.out.println("file name: "+ fileName);
			  }catch(Exception e) {
				  System.out.println("Error in schedule Upload controller :" +e);
			  }
			  model.addObject("sites", sites);
			  
			  return model;
			 }  
	


}
