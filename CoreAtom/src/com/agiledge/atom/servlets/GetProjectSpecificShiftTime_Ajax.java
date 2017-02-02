package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.service.LogTimeService;


/**
 * Servlet implementation class GetProjectSpecificShiftTime_Ajax
 */

public class GetProjectSpecificShiftTime_Ajax extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetProjectSpecificShiftTime_Ajax() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			
			
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String data="";
		 
		try{
		//	System.out.println("project id "+ request.getParameter("projectId"));
			  data=	new LogTimeService().getAllTimeWithProjectSpecific_String(request.getParameter("projectId"),request.getParameter("site"));
			
			 // data="11:30-1&12:30-2&10:30-3_21:30-4&22:30-5&21:30-6";
			System.out.println("Time : " + data);
			
			
		}catch(Exception e)
		{
			System.out.println("Exception in GetProjectSpecificShiftTime "+ e);
		}
		response.getWriter().write(data);
	}

}
