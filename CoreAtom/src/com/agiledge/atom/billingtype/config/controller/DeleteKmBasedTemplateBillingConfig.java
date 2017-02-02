package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService;


/**
 * Servlet implementation class DeleteKmBasedTemplateBillingConfig
 */
 
public class DeleteKmBasedTemplateBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteKmBasedTemplateBillingConfig() {
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
		 processRequest(request, response);
	}
	

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		 
		String id = request.getParameter("templateKeyId")==null ?"": request.getParameter("templateKeyId");
		String refId = request.getParameter("refId")==null ?"": request.getParameter("refId");
		System.out.println("ID : " + id);
		try{
			
			 KmBasedTemplateTripBillingConfigDto dto = new KmBasedTemplateTripBillingConfigDto ();
			  
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			  
			 dto.setId(id);
			  	 
			 KmBasedTemplateBillingService service = new KmBasedTemplateBillingService ();
				 
			 
			int val=service.deleteKmBasedTemplateBillingConfig(dto);  
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Configuration deleted successfully</div>");
			
			
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > Deletion Failure!</div>");
			}
			 
		}catch(Exception e)
		{
			System.out.println("Exception in AddTripRate : "+ e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Error In Server!</div>");
	
			
		}
		response.sendRedirect("kmBasedTripBillingConfig.jsp?refId="+refId);
	}



}
