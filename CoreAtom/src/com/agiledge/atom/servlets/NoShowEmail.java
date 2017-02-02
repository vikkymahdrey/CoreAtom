package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.EmployeeService;


public class NoShowEmail extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public NoShowEmail() {
        super();
        // TODO Auto-generated constructor stub
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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String type=request.getParameter("type");
		String count=request.getParameter("count");
		String days=request.getParameter("days");
		int returnInt=new EmployeeService().UpdateNoShowConfig(type, count, days);
		if(returnInt==1)
		{
			session.setAttribute("status",
					"<div class='success'>Config Data Updated Successfully</div");	
		}
		else
		{
			session.setAttribute("status",
					"<div class='success'>Config Data Updated Successfully</div");	
		}
		response.sendRedirect("NoShowMailConfig.jsp");
	}
}
