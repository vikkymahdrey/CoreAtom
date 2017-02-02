package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.SpocService;

/**
 * Servlet implementation class AddEmployeeSpoc
 */
public class AddEmployeeSpoc extends HttpServlet {
       
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String url="";
		try {
			HttpSession session = request.getSession();
			String empids[]={};
			String spoc_id=request.getParameter("spocid");
			String spoc_name=request.getParameter("spocname");
			url="viewspocemp.jsp?spoc_id="+spoc_id+"&spoc_name="+spoc_name;
			String selector=request.getParameter("mainselector");
			if(selector.equals("all"))
			{
				empids= request.getParameterValues("employeeIdCheckBox");

			}
			else
			{
				empids= request.getParameterValues("views");
			}
			if(new SpocService().insertEmployee(empids, spoc_id)>0){
				session.setAttribute("status",
						"<div class='success' > Added successfully  </div> ");

			} else {
				session.setAttribute("status",
						"<div class='failure' > Operation Failed !</div> ");
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
		response.sendRedirect(url);
		
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
