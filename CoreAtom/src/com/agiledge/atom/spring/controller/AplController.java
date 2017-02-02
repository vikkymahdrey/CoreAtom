package com.agiledge.atom.spring.controller;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.APLUploadFileValidtor;
import com.agiledge.atom.commons.UploadedFile;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.RoutingTypeDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.APLService;
import com.agiledge.atom.service.SiteService;

@Controller
public class AplController {
	APLUploadFileValidtor  fileValidator= new APLUploadFileValidtor();
	@RequestMapping(value="aplUpload", method = RequestMethod.GET)
	 public ModelAndView getAplUploadForm(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
			   BindingResult result) {
		System.out.println("Get Brach Id : "+ uploadedFile.getBranchId());
		uploadedFile.setPlaceDupe(true);
		uploadedFile.setLandmarkDupe(true);
			  return new ModelAndView("aplUpload");  
			 }  
	
	 
	 @RequestMapping(value = "/aplUpload", method=RequestMethod.POST)  
	 public ModelAndView aplFileUploaded(  
	   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
	   BindingResult result, HttpSession session) {  
	  InputStream inputStream = null;  
	  OutputStream outputStream = null;  
	  System.out.println(" uploading .............");

	  try {
		  System.out.println("Brach Id : "+ uploadedFile.getBranchId());
	  MultipartFile file = uploadedFile.getFile();  
	   fileValidator.validate(uploadedFile, result);  
	  
	  String fileName = file.getOriginalFilename();  
	  
		  int val =new APLService().uploadAPL(file.getInputStream(), uploadedFile.isAreaDupe(), uploadedFile.isPlaceDupe(), uploadedFile.isLandmarkDupe(), uploadedFile.getBranchId());
		  if(val>0) {
			  session.setAttribute("status",
						"<div class=\"success\" > Uploaded successfully!</div>");
		  }else {
			  session.setAttribute("status",
						"<div class=\"failure\" > Uploading failed !</div>");
		  }
	  }catch(Exception e) {
		  ;
	  }
	  if (result.hasErrors()) {  
	   return new ModelAndView("aplUpload");  
	  }  
	  
	   
	  return new ModelAndView("aplUpload");  
	 }
	 
	 
	 
	 // route upload show 
	 @RequestMapping(value="routeUpload", method = RequestMethod.GET)
	 public ModelAndView getRouteUploadForm(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
			   BindingResult result) {
		uploadedFile.setPlaceDupe(true);
		uploadedFile.setLandmarkDupe(true);
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("routeUpload");
			  List<RoutingTypeDto> types = new ArrayList<RoutingTypeDto>();
			  types.add(new RoutingTypeDto("Primary", "p"));
			  types.add(new RoutingTypeDto("Combined", "c"));
			  types.add(new RoutingTypeDto("Full Combined", "f"));
			  model.addObject("types", types);
			 
			  model.addObject("sites", sites);
			  return model;
			 }  
	
	
	 
	 @RequestMapping(value = "/routeUpload", method=RequestMethod.POST)  
	 public ModelAndView routeFileUploaded(  
	   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
	   BindingResult result, HttpSession session) {  
	  InputStream inputStream = null;  
	  OutputStream outputStream = null;  
	  System.out.println(" uploading .............");
	  ModelAndView model = new ModelAndView("routeUpload");
	  try {
	  MultipartFile file = uploadedFile.getFile();
	    
	  fileValidator.setUserType( session.getAttribute("role").toString());
	   fileValidator.validate(uploadedFile, result);
	   System.out.println("result.hasErrors() : "+ result.hasErrors());
	   System.out.println("result.hasFieldErrors() : "+ result.hasFieldErrors());
	   System.out.println("result.hasGlobalErrors() : "+ result.hasGlobalErrors());
	    
	  String fileName = file.getOriginalFilename();  
	  List<SiteDto> sites = new SiteService().getSites();
	  List<RoutingTypeDto> types = new ArrayList<RoutingTypeDto>();
	  types.add(new RoutingTypeDto("Primary", "p"));
	  types.add(new RoutingTypeDto("Combined", "c"));
	  types.add(new RoutingTypeDto("Full Combined", "f"));
	  model.addObject("types", types);
	  
	  model.addObject("sites", sites);
	  if(result.hasErrors()==false) {
	  APLService aplService = new APLService();
		  int val =aplService.uploadRoute(file.getInputStream(), uploadedFile.getSite(), uploadedFile.getType(), uploadedFile.isTruncate() );
		  
		  if(val>0) {
			  session.setAttribute("status",
						"<div class=\"success\" > Uploaded successfully!</div>");
		  }else {
			  session.setAttribute("status",
						"<div class=\"failure\" > Uploading failed !</div>");
		  }
	    }else {
	    	  session.setAttribute("status",
						"<div class=\"failure\" > Uploading failed !</div>");
	    }
	  }catch(Exception e) {
		  ;
	  }
	  if (result.hasErrors()) {  
	   return model;  
	  }  
	  
	   
	  return model;  
	 }
	 
	 
	
	  
	
}
