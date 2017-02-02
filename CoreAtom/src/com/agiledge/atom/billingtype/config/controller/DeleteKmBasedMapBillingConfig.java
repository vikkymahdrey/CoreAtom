package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedMapBillingTypeConfigService;


/**
 * Servlet implementation class AddTripBasedBillingConfig
 */
 
public class DeleteKmBasedMapBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteKmBasedMapBillingConfig() {
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
		
		 
		String id = request.getParameter("mapId")==null ?"": request.getParameter("mapId");
		String refId = request.getParameter("refId")==null ?"": request.getParameter("refId");
		System.out.println("ID : " + id);
		try{
			
			 KmBasedMapTripBillingConfigDto dto = new KmBasedMapTripBillingConfigDto();
			  
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			  
			 dto.setId(id);
			  	 
			 KmBasedMapBillingTypeConfigService service = new KmBasedMapBillingTypeConfigService ();
				 
			 
			int val=service.deleteKmBasedMapBillingConfig(dto);  
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
