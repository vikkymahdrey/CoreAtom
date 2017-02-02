package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.SlabBasedBillingTypeConfigService;


/**
 * Servlet implementation class DeleteSlabBasedBillingConfig
 */
 
public class DeleteSlabBasedBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteSlabBasedBillingConfig() {
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
		
		String refId=request.getParameter("refId")==null?"":request.getParameter("refId");
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		System.out.println("ID : " + id);
		try{
			
			 SlabBasedBillingConfigDto dto = new SlabBasedBillingConfigDto();
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			  dto.setId(id);	 
			 SlabBasedBillingTypeConfigService service = new SlabBasedBillingTypeConfigService();
				 
			 
			int val=service.deleteSlabBasedConfig(dto);
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > New Rate Set Up Successfully</div>");
			
			
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > Set Up Failure!</div>");
			}
			 
		}catch(Exception e)
		{
			System.out.println("Exception in AddSlabRate : "+ e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Error In Server!</div>");
	
			
		}
		response.sendRedirect("slabBasedBillingConfig.jsp?refId="+refId);
	}

		


}
