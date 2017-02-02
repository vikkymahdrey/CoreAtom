package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.BillingSlabDto;
import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.SlabBasedBillingTypeConfigService;


/**
 * Servlet implementation class UpdateSlabBasedBillingConfig
 */
 
public class UpdateSlabBasedBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateSlabBasedBillingConfig() {
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
		try{
			
			 SlabBasedBillingConfigDto dto = new SlabBasedBillingConfigDto();
			 dto.setRefId(refId);
			  
			 dto.setId(request.getParameter("id" ));
			 dto.setVehicleType(request.getParameter("selectedVehicleType"));
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setEscortRateType(request.getParameter("escortRateType"));
			 dto.setEscortRate(request.getParameter("escortRate"));
			 dto.setTripRate(request.getParameter("tripRate"));
			 String slabIds[] = request.getParameterValues("slabId");
			 if( slabIds!=null && slabIds.length >0 ) {
				 ArrayList<BillingSlabDto> slabRateList = new ArrayList<BillingSlabDto>();   
				 for(String slabId : slabIds) {
				 String slabRate = request.getParameter("slabRate_" + slabId );
				 
				 BillingSlabDto slabDto = new BillingSlabDto();
				 slabDto.setId(slabId);
				 slabDto.setSlabId(slabId);
				 slabDto.setRate(slabRate);
				 slabRateList.add(slabDto);
				 }
				 dto.setSlabList(slabRateList);
			 }
			 
			 	 
			 SlabBasedBillingTypeConfigService service = new SlabBasedBillingTypeConfigService();
				 
			if(service.validateSlabBasedBillingTypeConfig(dto)==false)
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >Validation Failure:!</div>"+service.getMessage());
			}else
			{
			int val=service.updateSlabBasedConfig(dto); 
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" >Slab Rate Updated Successfully</div>");
			
			
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
		response.sendRedirect("slabBasedBillingConfig.jsp?refId="+refId);
	}


	//UpdateSlabBasedBillingConfig

}
