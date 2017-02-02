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

/**
 * 
 * @author Administrator
 */
public class ChangeUser extends HttpServlet {

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
		String role = "" + session.getAttribute("role");
		String changeType = "" + request.getParameter("changeType");
		// System.out.println("Role : " + role);
		if (role.equals("hrm")) {
			session.setAttribute("role", "hrme");
			response.sendRedirect("employee_home.jsp");
		}
		if (role.equals("tmhr")) {
			session.setAttribute("role", "tme");
			response.sendRedirect("employee_home.jsp");
		}
		if (role.equals("tm")) {
			if (changeType.equals("1")) {
				session.setAttribute("role", "tmhr");
				response.sendRedirect("manager_home.jsp");
			} else {
				session.setAttribute("role", "tme");
				response.sendRedirect("employee_home.jsp");
			}
		}
		if (role.equals("tahr")) {
			session.setAttribute("role", "tae");
			response.sendRedirect("employee_home.jsp");
		}
		if (role.equals("ta")) {
			if (changeType.equals("1")) {
				session.setAttribute("role", "tahr");
				response.sendRedirect("manager_home.jsp");
			} else {
				session.setAttribute("role", "tae");
				response.sendRedirect("employee_home.jsp");
			}
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
