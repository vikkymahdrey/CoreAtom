package com.agiledge.atom.spring.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.jboss.logging.annotations.Param;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import com.agiledge.atom.accessRight.dto.AccessRightConstants;

import com.agiledge.atom.accessRight.service.AccessRightService;
import com.agiledge.atom.harddelete.service.HardDeleteService;

@Controller
public class HardDeleteController {

 HardDeleteService service =  new HardDeleteService();
 AccessRightService accessService = new AccessRightService();
	@RequestMapping(value="hardDeleteSchedule", method=RequestMethod.GET)
	 public void hardDeleteSchedule(  
			    @Param String site,
			    HttpSession session,
			     HttpServletResponse response
			    ) { 
		 
			try {
				
				AccessRightService accessService = new AccessRightService();
				if(accessService.hasRight(AccessRightConstants.ADMIN, session.getAttribute("role").toString())) {
				response.getWriter().print( String.valueOf(service.hardDeleteSchedule(site)));
				}
				else {
					response.getWriter().print("No access !");
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			 }  
	

  


@RequestMapping(value="hardDeleteSubscription", method=RequestMethod.GET)
public void hardDeleteSubscription(  
		    @Param String site,
		    HttpSession session,
		     HttpServletResponse response
		    ) { 
	 
		try {
			
			if(accessService.hasRight(AccessRightConstants.ADMIN, session.getAttribute("role").toString())) {
				response.getWriter().print( String.valueOf(service.hardDeleteSubscription(site)));
			}
			else {
				response.getWriter().print("No access !");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }  

 

@RequestMapping(value="hardDeleteTrip", method=RequestMethod.GET)
public void hardDeleteTrip(  
		    @Param String site,
		    HttpSession session,
		     HttpServletResponse response
		    ) { 
	 
		try {
			

			
			if(accessService.hasRight(AccessRightConstants.ADMIN, session.getAttribute("role").toString())) {
				response.getWriter().print( String.valueOf(service.hardDeleteTripRelatedTables(site)));
			}
			else {
				response.getWriter().print("No access !");
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		 }  


}
