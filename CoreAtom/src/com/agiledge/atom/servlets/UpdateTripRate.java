package com.agiledge.atom.servlets;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.TripDetailsService;

/**
 * Servlet implementation class UpdateTripRate
 */
public class UpdateTripRate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTripRate() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession(true);
    	try{
		String tripId=request.getParameter("updatetripid");
		Double oldrate=Double.parseDouble(request.getParameter("oldrate"));
		Double variation=Double.parseDouble(request.getParameter("updatevariation"));
		Double updaterate=0.0;
		int result=0;
		String operation=request.getParameter("updateoperation");
		if(operation.equalsIgnoreCase("add"))
		{
			updaterate=oldrate+variation;
		}
		else if(operation.equalsIgnoreCase("sub"))
		{
			updaterate=oldrate-variation;
		}
		if(updaterate>0)
		{
		result=new TripDetailsService().updateTripAmount(tripId, updaterate);
		}
		if(result==1)
		{
		session.setAttribute("status",
				"<div class=\"success\" > Updation success </div>");
	} else {
		session.setAttribute("status",
				"<div class=\"failure\" > Updation failed !</div>");
	}
	}catch (Exception ex) {
			session.setAttribute("status",
					"<div class=\"failure\" > Updation failed!</div>");
			System.out.println("Erro" + ex);
		}
    	String url=session.getAttribute("url").toString();
    	response.sendRedirect(url);
    	
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
