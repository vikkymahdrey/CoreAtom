package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.service.VehicleService;


/**
 * Servlet implementation class AddTripRate
 */

public class AddTripRate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddTripRate() {
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
		String siteId=request.getParameter("siteId")==null?"":request.getParameter("siteId");
		try{
			
			VehicleTypeDto dto = new VehicleTypeDto();
			dto.setDoneBy(request.getSession().getAttribute("user").toString());
			dto.setSite(request.getParameter("siteId"));
			dto.setType(request.getParameter("vehicleTypeId"));
			dto.setFromDate(request.getParameter("fromDate"));
			try{
			dto.setRatePerTrip(Float.parseFloat(request.getParameter("rate")));
			}catch(NumberFormatException nu)
			{
				dto.setRatePerTrip(0);
			}
			
			VehicleService service = new VehicleService();
			if(service.validateTripRateData(dto)==false)
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >Validation Failure:!</div>"+service.getErrorMessage());
			}else
			{
			int val=service.addRateForVehicleType(dto);
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > New Rate Set Up Successfully</div>");
			
			
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > Set Up Failure!</div>");
			}
			}
		}catch(Exception e)
		{
			System.out.println("Exception in AddTripRate : "+ e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Error In Server!</div>");
	
			
		}
		response.sendRedirect("TripCostCalandar.jsp?siteId="+siteId);
	}

}
