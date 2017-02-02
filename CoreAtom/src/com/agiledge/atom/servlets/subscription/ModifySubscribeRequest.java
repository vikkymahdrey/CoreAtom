/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.service.EmployeeSubscriptionService;


/**
 * 
 * @author 123
 */
public class ModifySubscribeRequest extends HttpServlet {

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
		try {
			// System.out.println("In ModifySubscribeRequest Servlet");
			HttpSession session1 = request.getSession(true);
			String doneBy = session1.getAttribute("user").toString();
			EmployeeSubscriptionDto dto = new EmployeeSubscriptionDto();
			dto.setSupervisor1(request.getParameter("supervisor1"));
			dto.setSupervisor2(request.getParameter("supervisor2"));
			dto.setLandMark(request.getParameter("landMark"));
			dto.setContactNo(request.getParameter("contactNo"));
			dto.setContactNo(request.getParameter("addressn"));
			dto.setDoneBy(doneBy);
			HttpSession session = request.getSession();
			dto.setEmployeeID(session.getAttribute("user").toString());
			String date = new Date().toString();
			Calendar cal = Calendar.getInstance();
			date = " " + cal.get(cal.MONTH) + "/" + cal.get(cal.DATE) + "/"
					+ cal.get(cal.YEAR);
			// System.out.println(" date  " + date);
			dto.setSubscriptionDate(date);
			// System.out.println(" Employee ID : " + dto.getEmployeeID());
			EmployeeSubscriptionService service = new EmployeeSubscriptionService();
			if (validateForm(dto)) {
				
				int value = service.modifySubscribeRequest(dto);
				if (value > 0)
					session.setAttribute("status",
							"<div class=\"success\" > Subscription successfull</div>");
				else
					session.setAttribute("status",
							"<div class=\"failure\" > Subscription failed !</div>");
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" > Subscription failed !</div>");
			}
		} catch (Exception e) {
			System.out.println("Error in Servliet subscribe : " + e);
		}

		response.sendRedirect("SubscriptionSelector");
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

	private boolean validateForm(EmployeeSubscriptionDto dto) {
		boolean flag = true;
		if (dto.getEmployeeID().equals("")) {
			flag = false;

		} else if (dto.getSupervisor1().equals("")) {
			flag = false;
		} else if (dto.getSupervisor2().equals("")) {
			flag = false;
		} else if (dto.getLandMark().equals("")) {
			flag = false;
		}
		return flag;
	}

}
