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

import com.agiledge.atom.dao.ScheduleAlterDao;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.service.SchedulingAlterService;


/**
 * 
 * @author Administrator
 */
public class ScheduleAlter extends HttpServlet {

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
		String source = request.getParameter("source");
		String[] dates = request.getParameterValues("date");
		String scheduleId = request.getParameter("scheduleid");
		String book = "" + request.getParameter("book");
		String status = "";
		String scheduledBy = session.getAttribute("user").toString();
		ScheduleAlterDto scheduleAlterDtoObj = null;
		ArrayList<ScheduleAlterDto> scheduleAlterDtoList = new ArrayList<ScheduleAlterDto>();
		ScheduleAlterDao scheduleAlterDaoObj = new ScheduleAlterDao();
String user = request.getSession().getAttribute("user").toString();
		
			String loginTime = "";
			String logoutTime = "";
			try {
				for (String date : dates) {
					loginTime =   request.getParameter("logintime" + date);
					logoutTime =   request.getParameter("logouttime" + date);
					status =   request.getParameter("status" + date);
					scheduleAlterDtoObj = new ScheduleAlterDto();
					scheduleAlterDtoObj.setScheduleId(scheduleId);
					scheduleAlterDtoObj.setDate(date);
					scheduleAlterDtoObj.setScheduledBy(scheduledBy);
					scheduleAlterDtoObj.setUpdatedById(user);					
					scheduleAlterDtoObj.setScheduleStates(new SchedulingAlterService().getScheduleDetailsByDate(scheduleId, date));
					if (book.equals("Update")) {
					scheduleAlterDtoObj.setLoginTime(loginTime);
					scheduleAlterDtoObj.setLogoutTime(logoutTime);
					}
					else
					{
						scheduleAlterDtoObj.setLoginTime("");
						scheduleAlterDtoObj.setLogoutTime("");	
						
					}
					//scheduleAlterDtoObj.setScheduleStates(status);
					scheduleAlterDtoList.add(scheduleAlterDtoObj);
				}
				int value = scheduleAlterDaoObj
						.scheduleAlterInsert(scheduleAlterDtoList);
				if (value > 0) {
					new SchedulingAlterService()
							.sendMessageForAlter(scheduleAlterDtoList,user);
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
		if (source == null) {
			response.sendRedirect("scheduled_employee.jsp");
		} else {
			response.sendRedirect("transadmin_scheduledemployee.jsp");
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
