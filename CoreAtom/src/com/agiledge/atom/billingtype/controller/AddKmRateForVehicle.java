package com.agiledge.atom.billingtype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.service.BillingTypeService;
import com.agiledge.atom.dto.VehicleTypeDto;

/**
 * Servlet implementation class AddKmRateForVehicle
 */
 
public class AddKmRateForVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddKmRateForVehicle() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}
	

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String refId=request.getParameter("conditionId");
		try {
			VehicleTypeDto dto = new VehicleTypeDto();
			dto.setId(Integer.parseInt(request.getParameter("vehicleType")));
			 
			dto.setRatePerKm(Float.parseFloat(request.getParameter("rate"))); 
			dto.setConditionId(request.getParameter("conditionId"));
			dto.setDoneBy(request.getSession().getAttribute("user").toString());
			BillingTypeService service = new BillingTypeService();
			int value =service.addVehicleRate(dto);
			if(value>0) {
				request.getSession().setAttribute("status",
						"<div class=\"success\" >Rate insertion Successful.</div>");
			}else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >"+service.getMessage()+"</div>");
			}
			
		}catch(NumberFormatException e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" >Validation Failure: invalid rate  !</div>");
			
		}
		catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" >Validation Failure:" +e +"  !</div>");
			
		} 
		response.sendRedirect("kmBasedBillingSetupCondition.jsp?refId=" + refId);		
	}
	
 


}
