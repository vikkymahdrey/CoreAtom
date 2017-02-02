package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedClassicTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedClassicBillingTypeConfigService;


/**
 * Servlet implementation class AddTripBasedBillingConfig
 */
 
public class UpdateKmBasedClassicBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateKmBasedClassicBillingConfig() {
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
		  
		String id = request.getParameter("classicId")==null ?"": request.getParameter("classicId");
		String kmBillingType=request.getParameter("kmBillingTypeHidden")==null?"":request.getParameter("kmBillingTypeHidden");
		String selectedVehicleType = request.getParameter("selectedVehicleType")==null ?"": request.getParameter("selectedVehicleType");
		String classicTripRate = request.getParameter("classicTripRate")==null ?"": request.getParameter("classicTripRate");
		String acYes = request.getParameter("classicAcYes") ==null ? "" : request.getParameter("classicAcYes");
		String escortRateType =request.getParameter("classicEscortRateType") ==null ? "" : request.getParameter("classicEscortRateType");
		String escortRate =  request.getParameter("classicEscortRate") ==null ? "" : request.getParameter("classicEscortRate");
		  
		
		try{
			
			 KmBasedClassicTripBillingConfigDto dto = new KmBasedClassicTripBillingConfigDto();
		
			 dto.setAcYes(acYes);
			 dto.setEscortRate(escortRate);
			 dto.setEscortRateType(escortRateType);
		 
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setRate(classicTripRate);
			 dto.setId(id);
			 dto.setKmBillingType(kmBillingType);
			 dto.setVehicleTypeId(selectedVehicleType);
			 if(dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
				 String [] fromHour = request.getParameterValues("classicFromHour");
				 String [] fromMinute = request.getParameterValues("classicFromMinute");
				  
				 
				 String [] toHour = request.getParameterValues("classicToHour");
				 String [] toMinute = request.getParameterValues("classicToMinute");
				  
				 
				 String [] acRates = request.getParameterValues("classicAcRate");
				 System.out.println("------------------");
				 ArrayList<AcConstraintDto> acList= new ArrayList<AcConstraintDto>();
				  for(int i=0; i<fromHour.length; i++) {
					  System.out.println("trackk --- " + i);
					  System.out.println("FROM "+fromHour[i]+":"+fromMinute[i]);
					  System.out.println("TO "+toHour[i]+":"+toMinute[i]);
					  AcConstraintDto accDto = new AcConstraintDto();
					  accDto.setFromTime(fromHour[i]+":"+fromMinute[i]);
					  accDto.setToTime(toHour[i]+":"+toMinute[i]);
					  accDto.setRate(acRates[i]);
					  acList.add(accDto);
				  }
			 
			  dto.setAcList(acList);
			
			 } 
			  	 
			 KmBasedClassicBillingTypeConfigService service = new KmBasedClassicBillingTypeConfigService ();
				 
			if(service.validateKmBasedClassicBillingTypeConfigForEdit(dto) ==false)
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >Validation Failure:!</div>"+service.getMessage());
			}else
			{
			int val=service.updateKmBasedClassicConfig(dto);
			if(val>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Configuration updated successfully</div>");
			
			
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > Setup Failure!</div>");
			}
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
