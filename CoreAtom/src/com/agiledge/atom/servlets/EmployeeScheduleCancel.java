package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ScheduleAlterDao;
import com.agiledge.atom.dto.ScheduleAlterDto;


/**
 * Servlet implementation class EmployeeScheduleCancel
 */
public class EmployeeScheduleCancel extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String scheduleIdDates[]=null;
	String splittedScheduleIdDates[]=new String[2];
	String scheduleId="";
	String date="";
	ScheduleAlterDto scheduleAlterDto=null;
	String user=request.getSession().getAttribute("user").toString();
	ArrayList<ScheduleAlterDto> scheduleAlterDtos=null;
	try{
		scheduleIdDates=request.getParameterValues("scheduleIdDate");
		
		scheduleAlterDtos=new ArrayList<ScheduleAlterDto>();
	for (String scheduleIdDate:scheduleIdDates)
	{
		System.out.println("IN Loop "+scheduleIdDate);	
	
		splittedScheduleIdDates=scheduleIdDate.split("@");
		System.out.println("splitted"+splittedScheduleIdDates[0]);
		scheduleId=splittedScheduleIdDates[0];
		date=splittedScheduleIdDates[1];
	
	
	scheduleAlterDto=new ScheduleAlterDto();
	scheduleAlterDto.setScheduleId(scheduleId);
	scheduleAlterDto.setUpdatedById(user);
	scheduleAlterDto.setScheduledBy(user);
	scheduleAlterDto.setDate(date);
	scheduleAlterDto.setLoginTime(request.getParameter("loginTime"+scheduleIdDate));
	scheduleAlterDto.setLogoutTime(request.getParameter("logoutTime"+scheduleIdDate));
	scheduleAlterDto.setScheduleStates(request.getParameter("status"+scheduleIdDate));
	scheduleAlterDtos.add(scheduleAlterDto);
	}
	int value =new ScheduleAlterDao().scheduleAlterInsert(scheduleAlterDtos);
	if (value > 0) {
		request.getSession()
				.setAttribute("status",
						"<div class=\"success\" >  Scheduling Modification Successful </div>");
	} else {
		request.getSession()
				.setAttribute("status",
						"<div class=\"failure\" > Scheduling Modification Failed </div>");
	}

} catch (Exception e) {
	System.out.println("Erro in servlet" + e);
	request.getSession()
			.setAttribute("status",
					"<div class=\"failure\" > Scheduling Modification Failed </div>");
}
	response.sendRedirect("emp_subscriptionHistory.jsp");
	
	}

}
