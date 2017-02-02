package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedClassicBillingTypeConfigService;


/**
 * Servlet implementation class AddTripBasedBillingConfig
 */
 
public class DeleteKmBasedClassicBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteKmBasedClassicBillingConfig() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// 
		processRequest(request, response);
	}

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		String refId=request.getParameter("refId")==null?"":request.getParameter("refId");
		String classicTripRate = request.getParameter("classicTripRate")==null ?"": request.getParameter("classicTripRate");
		String id = request.getParameter("classicId")==null ?"": request.getParameter("classicId");
		System.out.println("ID : " + id);
		try{
			
			 KmBasedClassicTripBillingConfigDto dto = new KmBasedClassicTripBillingConfigDto();
			  
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setRate(classicTripRate);
			 dto.setId(id);
			  	 
			 KmBasedClassicBillingTypeConfigService service = new KmBasedClassicBillingTypeConfigService ();
				 
			 
			int val=service.deleteKmBasedClassicConfig(dto);
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Configuration updated successfully</div>");
			
			
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
