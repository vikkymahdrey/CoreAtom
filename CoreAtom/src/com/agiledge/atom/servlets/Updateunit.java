package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.UnitService;

/**
 * Servlet implementation class Updateunit
 */
public class Updateunit extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Updateunit() {
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
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
        int unitid=Integer.parseInt(request.getParameter("updid"));
        String unitstatus=request.getParameter("upstatus");
        String status="A";
        if(unitstatus.equalsIgnoreCase("Active"))
        {
        	status="C";
        }
        int returnInt= new UnitService().updateStatus(unitid, status);
        if(returnInt==1)
		{
			session.setAttribute("status",
				"<div class='success'>Status Updated Successfully</div");
	    }
	   else
	   {

		session.setAttribute("status",
				"<div class='failure'>Operation Failed</div");
	   }
	   response.sendRedirect("viewUnit.jsp");
        
	}

}
