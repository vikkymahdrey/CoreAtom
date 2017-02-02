package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.EmployeeService;


/**
 * Servlet implementation class SetSpecial
 */

public class SetSpecial extends HttpServlet {
	
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		HttpSession session = request.getSession(false);
		PrintWriter out = response.getWriter();
		try {
			String setattrib = request.getParameter("setattrib");
			String empids[] = request.getParameterValues("employeeIdCheckBox");
			if (new EmployeeService().UpdateSetSpecial(empids,setattrib) > 0) {
				session.setAttribute("status",
						"<div class='success' >Updation Successful  </div> ");

			} else {
				session.setAttribute("status",
						"<div class='failure' > Updation Failed !</div> ");
			}

			response.sendRedirect("setSpecial.jsp");

		} catch (Exception e) {
			System.out.println("Error" + e);

		} finally {
			out.close();

		}
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
