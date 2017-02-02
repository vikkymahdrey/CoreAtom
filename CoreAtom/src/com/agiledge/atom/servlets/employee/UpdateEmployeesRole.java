/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.employee;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.EmployeeService;


//import javax.servlet.annotation.WebServlet;

/**
 * 
 * @author 123
 */
public class UpdateEmployeesRole extends HttpServlet {

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
		HttpSession session1 = request.getSession(true);
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession();
		try {
			String doneBy = session1.getAttribute("user").toString();
			String role = request.getParameter("role");
			String empids[] = request.getParameterValues("employeeIdCheckBox");
		/*	String empid = request.getParameter("employeeID");
			if(empids==null) {
				empids = new String[1];
				
			}  
			//employeeID
			if(OtherFunctions.isEmpty(empid)==false) {
				empids[empids.length-1]= empid;
			}*/
			 
			if (new EmployeeService().updatemployeesRole(empids, role, doneBy) > 0) {
				session.setAttribute("status",
						"<div class='success' > Roles are updated successfully  </div> ");

			} else {
				session.setAttribute("status",
						"<div class='failure' > Roles are not updated !</div> ");
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		response.sendRedirect("ChangeRole.jsp");
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
