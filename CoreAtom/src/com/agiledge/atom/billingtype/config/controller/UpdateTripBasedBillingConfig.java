package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.DistanceConstraintDto;
import com.agiledge.atom.billingtype.config.dto.TripBasedBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.TripBasedBillingTypeConfigService;
import com.agiledge.atom.commons.OtherFunctions;


/**
 * Servlet implementation class AddTripBasedBillingConfig
 */
 
public class UpdateTripBasedBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateTripBasedBillingConfig() {
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
		 
		String id = request.getParameter("id")==null?"":request.getParameter("id");
		try{
			
			 TripBasedBillingConfigDto dto = new TripBasedBillingConfigDto();
			 dto.setId(id);
			 dto.setBillingRefId(refId);
			 dto.setVehicleTypeId(request.getParameter("selectedVehicleType"));
			 dto.setAcYes(request.getParameter("acYes"));
			 dto.setDcYes(request.getParameter("dcYes"));
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setEscortRateType(request.getParameter("escortRateType"));
			 dto.setEscortRate(request.getParameter("escortRate"));
			 dto.setFlatTripRate(request.getParameter("tripRate"));
			  
			 if(dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
				 String [] fromHour = request.getParameterValues("fromHour");
				 String [] fromMinute = request.getParameterValues("fromMinute");
				  
				 
				 String [] toHour = request.getParameterValues("toHour");
				 String [] toMinute = request.getParameterValues("toMinute");
				  
				 
				 String [] acRates = request.getParameterValues("acRate");
				 
				 ArrayList<AcConstraintDto> acList= new ArrayList<AcConstraintDto>();
				  for(int i=0; i<fromHour.length; i++) {
					  AcConstraintDto accDto = new AcConstraintDto();
					  accDto.setFromTime(fromHour[i]+":"+fromMinute[i]);
					  accDto.setToTime(toHour[i]+":"+toMinute[i]);
					  accDto.setRate(acRates[i]);
					  acList.add(accDto);
				  }
			  dto.setAcList(acList);
			 }
			 
			 if(dto.getDcYes().equalsIgnoreCase(BillingTypeConfigConstants.DC_YES)) {
				 String [] fromKms = request.getParameterValues("fromKm");
				 String [] toKms = request.getParameterValues("toKm");
				 String [] dcRates = request.getParameterValues("dcRate");
				 String [] dcAcRates = request.getParameterValues("dcAcRate");
				 ArrayList<DistanceConstraintDto> dcList= new ArrayList<DistanceConstraintDto>();
				  for(int i=0; i<fromKms.length; i++) {
					  System.out.println("KMs " + i + " " + fromKms[i]);
					  DistanceConstraintDto dccDto = new DistanceConstraintDto();
					  dccDto.setFromKm(fromKms[i]);
					  dccDto.setToKm(toKms[i]);
					  dccDto.setRate(dcRates[i]);
					  if(OtherFunctions.isEmpty(dcAcRates[i])==false) {
						dccDto.setDcAcRate(dcAcRates[i]);  
					  }else {
						  dccDto.setDcAcRate("0");
					  }
					  dcList.add(dccDto);
				  }
				  dto.setDcList(dcList);
			 }
				 
			 TripBasedBillingTypeConfigService service = new TripBasedBillingTypeConfigService();
				 
			if(service.validateTripBasedBillingTypeConfig(dto)==false)
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >Validation Failure:!</div>"+service.getMessage());
			}else
			{
			int val=service.updateTripBasedConfig(dto);
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
		response.sendRedirect("tripBasedBillingConfig.jsp?refId="+refId);
	}

		
	

}
