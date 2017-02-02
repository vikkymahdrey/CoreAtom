package com.agiledge.atom.servlets.filter;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;

public class VendorVehicleEntryFilter implements Filter {
	public VendorVehicleEntryFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		
		System.out.println("IN VendorVehicleEntryFilter-6");
		String logTime=request.getParameter("tripTime");
		String logDate=request.getParameter("tripDate");
		String logMode=request.getParameter("tripMode");
		String logTimeModifyVal[]=logTime.split(":");
		int hour = Integer.parseInt(logTimeModifyVal[0]);
	    int mins = Integer.parseInt(logTimeModifyVal[1]);
	    int hoursInMins = hour * 60;
	    int logTimeResultVal=hoursInMins+mins;
	    	
		if(SettingsConstant.vehicleAssignCutOff.equalsIgnoreCase("true")) {
 			try{
 				 				
 				String startTime = SettingsConstant.vehicleAssignCutOffDuration;
 				                                                //System.out.println("filter-6 getting vehicleAssignCutOffDuration"+startTime);
 				String startTimeModifyVal[]=startTime.split(":");
 				int hour1 = Integer.parseInt(startTimeModifyVal[0]);
 			    int mins1 = Integer.parseInt(startTimeModifyVal[1]);
 			    int hoursInMins1 = hour1 * 60;
 			    int startTimeResultVal1=hoursInMins1+mins1;
 			    							
 								
 				Calendar curDate = Calendar.getInstance();
 				DateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
 				Date date = new Date();
 				String cDate=dateFormat.format(date);
 				curDate.setTime(new Date());
 				String curD=OtherFunctions.changeDateFormat(curDate.getTime(),"HH:mm");
 				
 				String curDModifyVal[]=curD.split(":");
 				int hour2 = Integer.parseInt(curDModifyVal[0]);
 			    int mins2 = Integer.parseInt(curDModifyVal[1]);
 			    int hoursInMins2 = hour2 * 60;
 			    int curDResultVal2=hoursInMins2+mins2;
 			    boolean flag=false ;
 			    String msg = null;
 																
 		   if(cDate.equalsIgnoreCase(logDate))
 			   { 
 				  logTimeResultVal= logTimeResultVal;
 			   }
 			   else
			    {
 				      logTimeResultVal=logTimeResultVal+24*60;	
			    }
 		   int resultant= logTimeResultVal-curDResultVal2;
				   
 		   if(logMode.equalsIgnoreCase("IN"))
 		   {
			   if(resultant>=startTimeResultVal1){
	 				flag= false;
	 				
	 			   }
				else
				{
					flag = true;	
			        msg = "before";
				}

 		   }  
 		   else 
 		   {
 			  if(resultant>=-startTimeResultVal1){
	 				flag=false;
	 			   }
				else{
					flag = true;
 			  		msg = "after upto";
           }
 			  }
 		   
                  if(flag == true){

 		              try {
				HttpServletResponse httpResponse=((HttpServletResponse) response);
				HttpServletRequest httpRequest=((HttpServletRequest) request);
				httpRequest.getSession().setAttribute("status",
						"<div class=\"failure\" > Trip assign to Vehicle is open "+msg+" 45 min of current time </div>");
				httpResponse.sendRedirect("message.jsp");
				}catch(Exception e) {
					System.out.println("Error in response : "+ e);
				}
 			 
                  }	
                  
                  else{
                	  chain.doFilter(request, response);
                  }
			
 			}catch(Exception e) {
 				System.out.println("Exception in VendorVehicleEntryFilter-6 : "+ e);
 				chain.doFilter(request, response);
 			}
			
		} else {
			chain.doFilter(request, response);
		}
		 
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
