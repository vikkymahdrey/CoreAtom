package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.DriverDAO;
import com.agiledge.atom.dto.DriverDto;


/**
 * Servlet implementation class AddVehicle
 */
public class AddDriver extends HttpServlet {


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
		DriverDto driverDtoObj = new DriverDto();
		
		
		
		try {
			String driverName = request.getParameter("name");
			
			String address1 = request.getParameter("address1");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String vendor=request.getParameter("vendor");
			String contactNo=request.getParameter("contactNo");
			
			driverDtoObj.setDriverName(driverName);
			driverDtoObj.setAddress1(address1);
			driverDtoObj.setUsername(username);
			driverDtoObj.setPassword(password);
			driverDtoObj.setDoneBy(doneBy);
			driverDtoObj.setVendor(vendor);
			driverDtoObj.setContactNo(contactNo);
			DriverDAO driverDao = new DriverDAO();
			int val  = driverDao.addDriver(driverDtoObj, doneBy);
			 
			if(val>0) {
				session.setAttribute("status",
						"<div class=\"success\" width=\"100%\" > " + driverDao.getMessage() +  "</div>");
				
			} else {
				session.setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > " + driverDao.getMessage() +  "</div>");
				
		 }
			 
			response.sendRedirect("add_driver.jsp");
		} catch (Exception ex) {
			System.out.println("Error in Dao" + ex);
			session.setAttribute("status","<div class=\"failure\" width=\"100%\" > Error :" + ex +  "</div>");
			response.sendRedirect("add_driver.jsp");
		}

	}

}
