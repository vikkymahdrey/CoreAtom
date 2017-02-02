package com.agiledge.atom.servlets.filter;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.http.HttpRequest;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;

/**
 * Servlet Filter implementation class ScheduleValidation
 */
 
public class ScheduleValidation implements Filter {

    /**
     * Default constructor. 
     */
    public ScheduleValidation() {
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
		// TODO Auto-generated method stub
		// place your code here

		// pass the request along the filter chain
		//HttpServletRequest req=(HttpServletRequest) request;
		//HttpSession session=req.getSession();
		System.out.println("IN Schedule Validation Filter");
		if(SettingsConstant.scheduleCutOff.equalsIgnoreCase("true")) {
 			try {
 				Calendar cal = Calendar.getInstance();
 				cal.setTime(new Date());
 				
 				int fromDay = OtherFunctions.getDayIndexFromStringWeekOfDay(SettingsConstant.scheduleCutOffStartDay);
 				int toDayOfWeek = fromDay;
 				String startTime = SettingsConstant.scheduleCutOffStartTime;
 				int hourStart=Integer.parseInt(startTime.split(":")[0] );
 				int minuteStart = Integer.parseInt(startTime.split(":")[1] );
 				
 				String allowedTime = SettingsConstant.scheduleCutOffDuration;	//hh:mm
 				int hour = hourStart + Integer.parseInt( allowedTime.split(":")[0] );
 				int min = minuteStart + Integer.parseInt( allowedTime.split(":")[1] );
 				//int startTime = Integer.parseInt(startTime );
 				Calendar fromTime = Calendar.getInstance();
 				Calendar toTime = Calendar.getInstance();
 				System.out.println("wednesday : " + toDayOfWeek);
 				fromTime.set(Calendar.DAY_OF_WEEK, toDayOfWeek);
 				fromTime.set(Calendar.HOUR_OF_DAY,hourStart);
 				fromTime.set(Calendar.MINUTE, minuteStart);
 				
 				toTime.setTime(fromTime.getTime());
 				toTime.set(Calendar.HOUR_OF_DAY, hour);
 				toTime.set(Calendar.MINUTE, min);
 				
 				Calendar curDate = Calendar.getInstance();
 				curDate.setTime(new Date());
 				 
 				
 				System.out.println ("fROM Date : " + OtherFunctions.changeDateFormat(fromTime.getTime(),"dd/MM/yyy HH:mm") );
 				System.out.println(" tO dATE : " + OtherFunctions.changeDateFormat(toTime.getTime(),"dd/MM/yyy HH:mm") );
 				System.out.println("cur Date : "+  OtherFunctions.changeDateFormat(curDate.getTime(),"dd/MM/yyy HH:mm") );
 				System.out.println("Cur Date compare from Date : "+curDate.compareTo(fromTime) );
 				System.out.println("Cur Date compare to Date : "+curDate.compareTo(toTime) );
 			
 				if( curDate.compareTo(fromTime)>=0 && curDate.compareTo(toTime)<0  ) {
				chain.doFilter(request, response);
			} else {
				try {
				HttpServletResponse httpResponse=((HttpServletResponse) response);
				HttpServletRequest httpRequest=((HttpServletRequest) request);
				httpRequest.getSession().setAttribute("status",
						"<div class=\"failure\" > Scheduling is opened from Wednesday 10 AM to Friday 4 PM </div>");
				httpResponse.sendRedirect("message.jsp");
				}catch(Exception e) {
					System.out.println("Error in response : "+ e);
				}
			}
			
 			}catch(Exception e) {
 				System.out.println("Exception in filterScheduleValidator : "+ e);
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
