package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.DriverDto;

/**
 * Servlet implementation class DeleteDriver
 */

public class DeleteDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteDriver() {
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
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		DriverDto driverDtoObj = new DriverDto();
		
		
		
		try {
 
			String driverId = request.getParameter("driverId");
			
 			driverDtoObj.setDriverId(Integer.parseInt(driverId));
 			int val=0;
 					//val=new DriverDAO().DeleteDriver(driverDtoObj);
			if(val>0) {
				session.setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Driver Deleted successful</div>");
			}else {
				session.setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Driver Deletion Failed</div>");
			}
			response.sendRedirect("add_driver.jsp");
		} catch (Exception ex) {
			System.out.println("Error in Dao" + ex);
			session.setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Error :"+ex+"</div>");
			response.sendRedirect("add_driver.jsp");
		}


	}

}
