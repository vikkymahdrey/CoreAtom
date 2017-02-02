package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.SpocService;

/**
 * Servlet implementation class AddMultiSpoc
 */
public class AddMultiSpoc extends HttpServlet {
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try {
			HttpSession session = request.getSession();
			String empids[]={};
			String man_id=request.getParameter("man_id");
			String from_date="";
			String to_date="";
			String selector=request.getParameter("mainselector");
			if(selector.equals("all"))
			{
				empids= request.getParameterValues("employeeIdCheckBox");
				from_date=request.getParameter("multifrom_date");
				to_date=request.getParameter("multito_date");

			}
			else
			{
				empids= request.getParameterValues("views");
				from_date=request.getParameter("selfrom_date");
				to_date=request.getParameter("selto_date");
			}
			if(new SpocService().insertSpoc(empids, man_id,from_date,to_date)>0){
				session.setAttribute("status",
						"<div class='success' > Assigned successfully  </div> ");

			} else {
				session.setAttribute("status",
						"<div class='failure' > Operation Failed !</div> ");
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		response.sendRedirect("viewspocs.jsp");
		
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
		processRequest(request, response);
	}

}
