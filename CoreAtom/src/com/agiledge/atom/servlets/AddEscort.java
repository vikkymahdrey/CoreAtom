package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.escort.service.EscortService;


/**
 * Servlet implementation class AddVehicle
 */
public class AddEscort extends HttpServlet {


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
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		EscortDto escortDtoObj = new EscortDto();
		
		
		
		try {
			String escortName = request.getParameter("name");
			
			String address = request.getParameter("address");
			String escortClock = request.getParameter("escortClock");
			String password = request.getParameter("password");
			String email=request.getParameter("email");
			String phone=request.getParameter("phone");
			String site=request.getParameter("site");
			
			 escortDtoObj.setName(escortName);
			 escortDtoObj.setAddress(address);
			 escortDtoObj.setEscortClock(escortClock);
			 escortDtoObj.setPassword(password);
			 escortDtoObj.setEmail(email);
			 escortDtoObj.setPhone(phone);
			 escortDtoObj.setSite(site);
			 EscortService service = new EscortService();
			 if(service.validateAddEscort(escortDtoObj) ) {
				  
				int val= service.addEscort(escortDtoObj, doneBy);
				if(val>0) {
					session.setAttribute("status",
							"<div class=\"success\" width=\"100%\" > " + service.getErrorMessage() +  "</div>");
					
				} else {
					session.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" > " + service.getErrorMessage() +  "</div>");
					
			 }
			 }else {
					session.setAttribute("status",
							"<div class=\"failure\" width=\"100%\" > Validation Error :"+service.getErrorMessage()+"</div>");
					
			 }
			 
			
			response.sendRedirect("escortSetUp.jsp");
		} catch (Exception ex) {
			System.out.println("Error in Dao" + ex);
			response.sendRedirect("escortSetUp.jsp");
		}

	}

}
