/*

 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.service.SchedulingService;


/**
 * 
 * @author Administrator
 */
public class ScheduleEmployee extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		
		try {
			String scheduledBy = session.getAttribute("user").toString();
			String source = request.getParameter("source");
			String subscriptionIds[] = request.getParameterValues("subscriptionId");
			SchedulingDto schedulingDtoObj = null;
			SchedulingService schedulingServiceObj = new SchedulingService();
			ArrayList<SchedulingDto> schedulingEmpList = new ArrayList<SchedulingDto>();
			for (int i = 0; i < subscriptionIds.length; i++) {
				schedulingDtoObj = new SchedulingDto();
				schedulingDtoObj.setSubscriptionId(subscriptionIds[i]);
				String project =request.getParameter("project" + subscriptionIds[i]);										
				schedulingDtoObj.setProject(project);								
				schedulingDtoObj.setSchedulingFromDate(request
						.getParameter("from_date" + subscriptionIds[i]));
				schedulingDtoObj.setSchedulingToDate(request
						.getParameter("to_date" + subscriptionIds[i]));
				schedulingDtoObj.setWeeklyOff(request
						.getParameter("weeklyoff" + subscriptionIds[i]));
				schedulingDtoObj.setLoginTime(request.getParameter("logintime"
						+ subscriptionIds[i]));
				schedulingDtoObj.setLogoutTime(request
						.getParameter("logouttime" + subscriptionIds[i]));
				schedulingDtoObj.setScheduledBy(scheduledBy);
				schedulingEmpList.add(schedulingDtoObj);				
			}
			int resultRows = schedulingServiceObj
					.setScheduleEmployees(schedulingEmpList);
			if (resultRows > 0) {
				session.setAttribute("status",
						"<div class=\"success\" >  Scheduling successful </div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Scheduling failed </div>");
			}
			if (source == null)
				response.sendRedirect("scheduled_employee.jsp");
			else
				response.sendRedirect("transadmin_schedule_employee.jsp");
		}catch(Exception e)		
		{
			System.out.println("error"+e);
			response.sendRedirect("employee_home.jsp");
		}
		
		
	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">

	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
