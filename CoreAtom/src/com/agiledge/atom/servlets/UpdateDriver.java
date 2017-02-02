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
 * Servlet implementation class UpdateDriver
 */
 
public class UpdateDriver extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateDriver() {
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
			String driverName = request.getParameter("name");
			String driverId = request.getParameter("driverId");
			
			String address1 = request.getParameter("address1");
			String address2=request.getParameter("address2");
			String username = request.getParameter("username");
			String password = request.getParameter("password");
			String vendor=request.getParameter("vendor");
			String contactNo=request.getParameter("contactNo");
			driverDtoObj.setDriverId(Integer.parseInt(driverId));
			driverDtoObj.setDriverName(driverName);
			driverDtoObj.setAddress1(address1);
			driverDtoObj.setAddress2(address2);
			driverDtoObj.setUsername(username);
			driverDtoObj.setPassword(password);
			driverDtoObj.setDoneBy(doneBy);
			driverDtoObj.setVendor(vendor);
			driverDtoObj.setContactNo(contactNo);
			int val=new DriverDAO().UpdateDriver(driverDtoObj);
			if(val>0) {
				session.setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Driver Updated successful</div>");
			}else {
				session.setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Driver Update Failed</div>");
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
