/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.ScheduleModifyCancelDao;
import com.agiledge.atom.dto.ScheduleModifyCancelDto;


/**
 * 
 * @author muhammad
 */
public class ScheduleModify extends HttpServlet {

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
		String bookModify = request.getParameter("modify");
		HttpSession session=request.getSession(true);
		ScheduleModifyCancelDao scheduleModifyCancelDao = new ScheduleModifyCancelDao();
		if (bookModify != null) {
			// System.out.println("Modify");
			try {
				/*
				 * HttpSession session=request.getSession(true); String
				 * scheduledBy =session.getAttribute("user").toString();
				 */
				String scheduledBy =session.getAttribute("user").toString();
				String scheduleId = request.getParameter("scheduleId");
				ScheduleModifyCancelDto scheduleModifyCancelDtoObj = new ScheduleModifyCancelDto();
				scheduleModifyCancelDtoObj.setScheduleId(scheduleId);
				scheduleModifyCancelDtoObj.setProject(request
						.getParameter("project"));
				scheduleModifyCancelDtoObj.setLoginTime(""
						+ request.getParameter("logintime"));
				scheduleModifyCancelDtoObj.setLogoutTime(""
						+ request.getParameter("logouttime"));
				scheduleModifyCancelDtoObj.setScheduledBy(scheduledBy);
				// System.out.println("log out time"+scheduleModifyCancelDtoObj.getLogoutTime());
				// System.out.println("schedule Id"+scheduleId);
				int status = scheduleModifyCancelDao
						.scheduleModifySingle(scheduleModifyCancelDtoObj);
				if (status > 0) {
					request.getSession()
							.setAttribute("status",
									"<div class='success'>Schedule Modified Successfully</div");
				} else {
					request.getSession()
							.setAttribute("status",
									"<div class='failure'>Schedule Modification Failed</div");
				}
				response.sendRedirect("scheduled_employee.jsp");
			} catch (Exception e) {
				request.getSession()
						.setAttribute("status",
								"<div class='failure'>Schedule Modification Failed</div");
				System.out.println("ERRORin Serlet" + e);
				response.sendRedirect("scheduled_employee.jsp");
			}
		} else {
			ScheduleModifyCancelDto scheduleModifyCancelDtoObj = new ScheduleModifyCancelDto();
			String[] scheduleIds = request.getParameterValues("scheduleId");
			scheduleModifyCancelDao.scheduleCancel(scheduleIds,scheduleModifyCancelDtoObj);
			String scheduledBy =session.getAttribute("user").toString();
			scheduleModifyCancelDtoObj.setScheduledBy(scheduledBy);
			scheduleModifyCancelDao.scheduleCancel(scheduleIds,scheduleModifyCancelDtoObj);
			response.sendRedirect("scheduled_employee.jsp");
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
