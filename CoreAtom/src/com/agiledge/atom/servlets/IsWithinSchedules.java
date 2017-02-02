
package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.SchedulingService;



/**
 * Servlet implementation class IsWithinSchedules
 */

public class IsWithinSchedules extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public IsWithinSchedules() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		 
		 
		 
		String fromDate = request.getParameter("fromDate");
		
		String toDate = request.getParameter("toDate");
		
		fromDate=OtherFunctions.changeDateFromatToIso(fromDate);
		toDate=OtherFunctions.changeDateFromatToIso(toDate);
		
				
		String subscriptionID = request.getParameter("subscriptionID");
		
		System.out.println("From Date "+ fromDate);
		System.out.println("to Date " + toDate );
		System.out.println("subscription ID " + subscriptionID );
		
		
		if(new SchedulingService().isWithinSchedules(subscriptionID,fromDate,toDate))
		{
			response.getWriter().write("true");
			System.out.println(" true ");
		}else
		{
			response.getWriter().write("false");
			System.out.println(" false");
		}

	}
 
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

 
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
