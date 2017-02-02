package com.agiledge.atom.spring.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.commons.UploadedFile;
import com.agiledge.atom.dao.OtherDao;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.SchedulingService;
import com.agiledge.atom.service.SiteService;

@Controller
public class ScheduleController {
	
	 
	
	@RequestMapping(value="uploadSchedule", method=RequestMethod.GET)
	 public ModelAndView showUploadSchedule(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,  
			   BindingResult result) { 
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("scheduleUpload");
			  
			  model.addObject("sites", sites);
			  return model;
			 }  
	

	
	@RequestMapping(value="uploadSchedule", method=RequestMethod.POST)
	 public ModelAndView uploadSchedule(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			   HttpSession session 
			    ) throws IOException { 
		System.out.println("chcek");
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("scheduleUpload");
			  MultipartFile file = uploadedFile.getFile();
			  String fileName = file.getOriginalFilename();
			  System.out.println("file name: "+ fileName);
			  try{
				  SchedulingService schService = new SchedulingService();
			     int val= schService.uploadSchedule(file.getInputStream(),uploadedFile.getSite(), session.getAttribute("user").toString());
			     if(val>0) {
			    	 
			    	 session.setAttribute("status",
								"<div class=\"success\" width=\"100%\" > " + schService.getMessage()+ "</div>");
			     } else {
			    	 session.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > " + schService.getMessage()+ "</div>");
			     }
			     String reportHtml="<div> <h1>Status Report </h1><div><br/>";
			     if(schService.getMessageList()!=null && schService.getMessageList().size()>0) {
			    	 reportHtml+="<Div>";
			    	 for(String message : schService.getMessageList()) {
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
	
	@RequestMapping(value="downloadHorizontalScheduleTemplate", method=RequestMethod.GET)
	public ModelAndView getDownloadHorizontalSchedulePage(
			@ModelAttribute("uploadedFile") UploadedFile uploadedFile 
			) {
		List<SiteDto> sites = new SiteService().getSites();
		ModelAndView model = new ModelAndView("scheduleHorizontalUpload");
		model.addObject("sites", sites);
		return model;
	}
	
	@RequestMapping(value="downloadHXLS", method=RequestMethod.GET)
	 public void downloadHorizontalSchedule(  
			  
			 @Param String fromDate,
			 @Param String toDate,
			 @Param String site,
			   HttpSession session,
			   HttpServletResponse response
			    ) throws IOException { 
		 
		 
		 
			    
			  try{
				  System.out.println(" from Date : " + fromDate + " & to date : " + toDate);
				  SchedulingService schService = new SchedulingService();
				  //fromDate = OtherFunctions.changeTimeFormat(fromDate, "dd/MM/yyyy", "")
					// fromDate ="01/09/2014";
					 // toDate = "11/09/2014";
					   
					  
					  
					  System.out.println(".........DOnwloading.........");
				  XSSFWorkbook wb = schService.downloadHorizantalScheduleTemplate(fromDate, toDate, site);
/*
					try   
			        {  
			            FileOutputStream out =  
			            new FileOutputStream  
			                (new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));  
			            wb.write(out);  
			            out.close();  
			            System.out.println  
			                ("Excel written successfully..");
			            
			             
			    	// 	SchedulingService.uploadScheduleHorizoantalFile(in);
			        }   
			        catch (FileNotFoundException e)   
			        {  
			            e.printStackTrace();  
			        }
			        
*/				 
				  String filename = "home.xlsx";   
				      
				  response.setContentType("APPLICATION/OCTET-STREAM");   
				  response.setHeader("Content-Disposition","attachment; filename=\"" + filename + "\"");   
				  wb.write(response.getOutputStream());
			      
			      
			  }catch(Exception e) {
				  
				  System.out.println("Error in schedule Upload controller :" +e);
				  
				  
			  }
			   			 
	}
	
	
	@RequestMapping(value="uploadHXLSchedule", method=RequestMethod.POST)
	 public ModelAndView uploadHorizontalSchedule(  
			   @ModelAttribute("uploadedFile") UploadedFile uploadedFile,
			   HttpSession session 
			    ) throws IOException { 
		System.out.println("chcek");
		List<SiteDto> sites = new SiteService().getSites();
			  ModelAndView model = new ModelAndView("scheduleHorizontalUpload");
			  MultipartFile file = uploadedFile.getFile();
			  String fileName = file.getOriginalFilename();
			  System.out.println("file name: "+ fileName);
			  try{
				  SchedulingService schService = new SchedulingService();
			     int val= schService.uploadHorizontalSchedule(file.getInputStream(),uploadedFile.getSite(), session.getAttribute("user").toString());
			     System.out.println("value :" + val);
			     if(val>0) {
			    	 
			    	 session.setAttribute("status",
								"<div class=\"success\" width=\"100%\" > " + schService.getMessage()+ "</div>");
			     } else {
			    	 session.setAttribute("status",
								"<div class=\"failure\" width=\"100%\" > " + schService.getMessage()+ "</div>");
			     }
			     String reportHtml="<div> <h1>Status Report </h1><div><br/>";
			     if(schService.getMessageList()!=null && schService.getMessageList().size()>0) {
			    	 reportHtml+="<Div>";
			    	 for(String message : schService.getMessageList()) {
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
	

	

 
	
	@RequestMapping(value = "/ScheduleSelf", method = RequestMethod.GET)
	public ModelAndView showsubscriptionDetailsForSchedule(HttpSession session,HttpServletResponse response) {
		ModelAndView mav = new ModelAndView("selfSchedule");
		String employeeId = session.getAttribute("user").toString();
		OtherDao ob=OtherDao.getInstance();
		SchedulingDto schdulingEmployeeDto=new SchedulingService().getDetailsForSchedule(employeeId);
		if(schdulingEmployeeDto==null)
		{
			session.setAttribute("status",
					"<div class=\"failure\" > You are Not Subscribed! </div>");
			mav= new ModelAndView("redirect:/SubscriptionSelector");	
		}
		else
		{
		schdulingEmployeeDto.setScheduledBy(employeeId);
		mav.addObject("SubscriptionEffective", OtherFunctions.changeDateFromatddmmyy(schdulingEmployeeDto.getSubscriptionDate()));
		mav.addObject("SchedulededPerson",ob.getUserName(Long.parseLong(schdulingEmployeeDto.getScheduledBy())));
		mav.addObject("SelfScheduling",schdulingEmployeeDto);
		}
		return mav;
	}

	@RequestMapping(value = "/SelfScheduleSubmit", method = RequestMethod.POST)
	public void bookSHeduleForSelf(@ModelAttribute("SelfScheduling") SchedulingDto dto,HttpSession session,HttpServletResponse response) throws IOException {
		String employeeId = session.getAttribute("user").toString();
		dto.setScheduledBy(employeeId);
	long status = new SchedulingService().scheduleSelf(dto);
	if(status>0)
	{
		session.setAttribute("status",
				"<div class=\"success\" >  Scheduling successful </div>");
	} else {
		session.setAttribute("status",
				"<div class=\"failure\" > Scheduling failed </div>");
	}
	
	response.sendRedirect("emp_schedulingHistory.jsp?subid="+dto.getSubscriptionId());
	}
	
	private Object getMap(ArrayList<LogTimeDto> loginTimeDto) {
HashMap<String,String> logtimes=new HashMap<String,String>();
for (LogTimeDto dto : loginTimeDto) {
	logtimes.put(dto.getLogTime(), dto.getLogTime());
	}
		return logtimes;
	}
}
