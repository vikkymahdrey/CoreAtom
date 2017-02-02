package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.service.VendorService;

/**
 * Servlet implementation class DeleteVendorMaster
 */
 
public class DeleteVendorMaster extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteVendorMaster() {
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
		doProcess(request, response);
	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		 

		 
		try {
			System.out.println("IN DeleteVendorMaster");
			String id = request.getParameter("deleteid");
			 
			VendorService service = new VendorService();
			//BillingTypeDto dto = service.getBillingTypeMapping(id);
			VendorDto dto = new VendorDto();
			 dto.setId(id);
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			int value = service.deleteVendorMaster(dto);
			 
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
			System.out.println("Error in DeleteVendorMaster: "+ e);
		}
 
			response.sendRedirect("vendorMasterSetUp.jsp" );


		
	}

}
