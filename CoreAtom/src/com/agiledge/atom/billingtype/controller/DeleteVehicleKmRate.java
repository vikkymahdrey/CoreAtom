package com.agiledge.atom.billingtype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.service.BillingTypeService;
import com.agiledge.atom.dto.VehicleTypeDto;

/**
 * Servlet implementation class DeleteVehicleKmRate
 */
@WebServlet("/DeleteVehicleKmRate")
public class DeleteVehicleKmRate extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteVehicleKmRate() {
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
		//  
		String  refId= request.getParameter("refId");
		try {
			System.out.println("IN DeleteVehicleKmRate");
			String id = request.getParameter("id");
			 
			BillingTypeService service = new BillingTypeService();
			//BillingTypeDto dto = service.getBillingTypeMapping(id);
			VehicleTypeDto dto = new VehicleTypeDto();
			dto.setConditionId(id);
			dto.setRefId(refId);
			 
			int value = service.deleteVehicleKmRate(dto);
			 
			if(value > 0) {
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" >Deletion successful  </div>");
			} else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" >Deletion failure  </div>");
			}
			
 			
		} catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" >Deletion failure "+e+" </div>");
			System.out.println("Error in DeleteBillingSetupCondition : "+ e);
		}
 
			response.sendRedirect("kmBasedBillingSetupCondition.jsp?refId="+refId);

			 

		
	}
	
	

}
