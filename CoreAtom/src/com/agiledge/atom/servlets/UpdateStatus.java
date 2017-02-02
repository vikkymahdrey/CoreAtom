package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.EmployeeService;

/**
 * Servlet implementation class UpdateStatus
 */
public class UpdateStatus extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateStatus() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session=request.getSession();
		String empid=request.getParameter("empid");
		int active=Integer.parseInt(request.getParameter("active"));
		int result=new EmployeeService().updateStatus(empid, active);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Status Updated Successfully</div");
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Status Updation Failed</div");
		}
		response.sendRedirect("viewallExternal.jsp");
		
		
	}

}
