package com.agiledge.atom.servlets;

import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.ScheduledEmpDao;
import com.agiledge.atom.dao.SchedulingDao;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduledEmpDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.service.SchedulingAlterService;
import com.agiledge.atom.service.SchedulingService;

/**
 * Servlet implementation class SelectedEmpSchedule
 */

public class SelectedEmpSchedule extends HttpServlet 
{
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    
        // TODO Auto-generated constructor stub
        protected void processRequest(HttpServletRequest request,HttpServletResponse response) throws ServletException, IOException {
            
        	try {
        	HttpSession session = request.getSession(true);
    		String scheduledBy = session.getAttribute("user").toString();
    		long empid = Long.parseLong(scheduledBy);
    		long actEmpId=empid;
    		String employeeId = scheduledBy;
    		if(session.getAttribute("delegatedId")!=null)
    			{
    			employeeId=session.getAttribute("delegatedId").toString();
    			empid = Long.parseLong(employeeId);
    			}
    		int val =0;
    		
    		String source = request.getParameter("source");
    		String site = request.getParameter("site");
    		//String subscriptionIds[] = request.getParameterValues("subscriptionid");
    		String empcount = request.getParameter("employeeCount");
    		String date = request.getParameter("datecount");
    		int datecount = Integer.parseInt(date);
    		int count = Integer.parseInt(empcount);
    		
    		SchedulingDto schedulingDtoObj = null;
    		ScheduleAlterDto schedulingAldto = null;
    		ScheduledEmpDao ScheduledDaoObj = new ScheduledEmpDao();
    		SchedulingService schedulingServiceObj = new SchedulingService();
    		ArrayList<SchedulingDto> schedulingEmpListparent = new ArrayList<SchedulingDto>();
    		
    		String fdate,ldate =null;
    		for(int i=1; i<= count ; i++)
    		{
    		
    		String subscriptionid = request.getParameter("subscriptionid"+i);
    		
    		String personnelId = request.getParameter("personnelno"+i);
    		String eid = request.getParameter("eid"+i);
    		
    		
    			//for (int i = 0; i < subscriptionIds.length; i++) {
    				schedulingDtoObj = new SchedulingDto();
    				//schedulingDtoObj.setSubscriptionId(subscriptionIds[i]);
    				schedulingDtoObj.setSubscriptionId(subscriptionid);
    				//schedulingdto.setSubscriptionId(subscriptionid);
    				String project =request.getParameter("project"+i);
    				
    				
    						
    				schedulingDtoObj.setEmployeeId(personnelId);
    				schedulingDtoObj.setPersonnelNo(personnelId);
    				schedulingDtoObj.setProject(project);
    				//schedulingdto.setProject(project);
    				
    				schedulingDtoObj.setSchedulingFromDate(request
    						.getParameter("fromdate"));
    				
					
    				schedulingDtoObj.setSchedulingToDate(request
    						.getParameter("todate"));
    				
    				/*schedulingDtoObj.setWeeklyOff(request
    						.getParameter("weeklyoff" + subscriptionIds[i]));*/
    			    String parentLogin = request.getParameter("logintime"+schedulingDtoObj.getPersonnelNo()+1);
    			    String parentLogout = request.getParameter("logouttime"+schedulingDtoObj.getPersonnelNo()+1);
    			    if(parentLogin == null || parentLogin == "")
    			    {
    			    	parentLogin = "none";
    			    }
    			    if(parentLogout == null || parentLogout == "")
    			    {
    			    	parentLogout = "none";
    			    }
    				schedulingDtoObj.setLoginTime(parentLogin);
    				schedulingDtoObj.setLogoutTime(parentLogout);
    				
    				
    				
    				
    				
    				schedulingDtoObj.setScheduledBy(scheduledBy);
    				schedulingEmpListparent.add(schedulingDtoObj);
    				//System.out.println(schedulingEmpListparent.size() + "schedulinglist");
    			
    		}		
    			/*int resultRows = schedulingServiceObj
    					.setScheduleEmployees(schedulingEmpList);
    			System.out.println("here again");*/
    			/*if (resultRows > 0) {
    				session.setAttribute("status",
    						"<div class=\"success\" >  Scheduling successful </div>");
    			} else {
    				session.setAttribute("status",
    						"<div class=\"failure\" > Scheduling failed </div>");
    			}*/
    			
    			for(int i=1; i<= count ; i++)
    			{
    				SchedulingDto curDto = schedulingEmpListparent.get(i-1);
    				
    				ArrayList<ScheduleAlterDto> schedulingEmpListchild = new ArrayList<ScheduleAlterDto>();
    	    		 
    	    				//schedulingdto.setProject(project);
    	    				for(int j=1 ; j<=datecount; j++)
    	    				{
    	    					//ArrayList<ScheduleAlterDto> schedulingEmpListchild = new ArrayList<ScheduleAlterDto>();
    	    					
    	    					schedulingAldto = new ScheduleAlterDto();
    	    					//schedulingdto.setSubscriptionId(subscriptionid);
    	    					//schedulingdto.setProject(project);	
    	    				String currdate = request.getParameter("date"+j);
    	    				String login = request.getParameter("logintime"+curDto.getPersonnelNo()+j);
    	    				String logout = request.getParameter("logouttime"+curDto.getPersonnelNo()+j);
    	    				if(login == null || login == "")
    	    					login ="none";
    	    				if(logout == null || logout == "" )
    	    					logout ="none";
    	    				DateFormat formatter ; 
    					
    	    				schedulingAldto.setDate(currdate);
    	    				schedulingAldto.setScheduledBy(scheduledBy);
    	    				//schedulingAldto.set
    	    				schedulingAldto.setLoginTime(login);
    	    				schedulingAldto.setLogoutTime(logout);
    	    				schedulingEmpListchild.add(schedulingAldto);
    	    			
    	        			
    	    				//schedulingEmpListchild.clear();
    	    				}
    	    				
    	    				curDto.setAlterList(schedulingEmpListchild);
    	    				
    				 
				
    			}
    			 
    			
    			
    		/*	for(SchedulingDto schdto : schedulingEmpListparent)
    			{
    				
    					schdto.setAlterList(schedulingEmpListchild);
    				
    			}*/
    			
    		/*	for(SchedulingDto dto : schedulingEmpListparent) {
    				System.out.println(" child size : " + dto.getAlterList().size());
    				for(ScheduleAlterDto adto : dto.getAlterList()) {
    					System.out.println(" Alter " + adto);
    				}
    			}*/
    			
    			SchedulingService sch = new SchedulingService();
    			val = sch.uploadSchedule(schedulingEmpListparent,scheduledBy );
    			if(val >0)
    			{
    				
    				session.setAttribute("status",
    						"<div class=\"success\" >  Scheduling successful </div>");
    				
    			}
    			
    			else
    			{
    				System.out.println("not working");
    				session.setAttribute("status",
    						"<div class=\"failure\" > Scheduling failed </div>");
    			
    			}
    			
    			if (source == null)
				response.sendRedirect("scheduled_employee.jsp");
			else
				response.sendRedirect("transadmin_schedule_employee.jsp");
    		 }
        catch(Exception e)
        {
        	System.out.println("error in select schedule" + e);
        }
        finally {

    		}
        }

        
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
