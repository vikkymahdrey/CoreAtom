package com.agiledge.atom.billingtype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.billingtype.service.BillingTypeService;


/**
 * Servlet implementation class DeleteBillingTypeMapping
 */
 
public class DeleteBillingTypeMapping extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteBillingTypeMapping() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
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

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//  
 
 
		try {
			System.out.println("IN DeleteBillingTypemapping");
			String id = request.getParameter("id");
			System.out.println("ID : " + id);
			BillingTypeService service = new BillingTypeService();
			BillingTypeDto dto = service.getBillingTypeMapping(id);
			int value = service.deleteBillingTypeMapping(dto);
			System.out.println("IN DeleteBillingTypemapping");
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
			System.out.println("Error in DeleteBillingTypeMapping : "+ e);
		}
			response.sendRedirect("billingtype_mapping.jsp");	
			 
	}

}
