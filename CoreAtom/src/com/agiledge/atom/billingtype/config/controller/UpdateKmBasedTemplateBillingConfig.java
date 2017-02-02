package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedTemplateBillingService;


/**
 * Servlet implementation class UpdateKmBasedTemplateBillingConfig
 */
 
public class UpdateKmBasedTemplateBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateKmBasedTemplateBillingConfig() {
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
		 
		System.out.println(" refId : " + refId);
		String kmBillingType=request.getParameter("kmBillingTypeHidden")==null?"":request.getParameter("kmBillingTypeHidden");
		String selectedVehicleType = request.getParameter("selectedVehicleType")==null ?"": request.getParameter("selectedVehicleType");
		String swingRate =  request.getParameter("templateSwingRate")==null ?"": request.getParameter("templateSwingRate"); 
		String swingDistanceType = request.getParameter("templateSwingRateType")==null ?"": request.getParameter("templateSwingRateType");
		String templateId = request.getParameter("templateId") == null ? "" : request.getParameter("templateId");
		String id = request.getParameter("templateKeyId") == null ? "" : request.getParameter("templateKeyId");
		String templateTripRate = request.getParameter("templateTripRate") ==null ? "" : request.getParameter("templateTripRate");
		String acYes = request.getParameter("templateAcYes") ==null ? "" : request.getParameter("templateAcYes");
		String escortRateType =request.getParameter("templateEscortRateType") ==null ? "" : request.getParameter("templateEscortRateType");
		String escortRate =  request.getParameter("templateEscortRate") ==null ? "" : request.getParameter("templateEscortRate");
		 
		try{
			
			KmBasedTemplateTripBillingConfigDto dto = new KmBasedTemplateTripBillingConfigDto();
			 dto.setTripRate( templateTripRate );
			 dto.setEscortRate(Float.parseFloat(escortRate));
			 dto.setEscortRateType(escortRateType);
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setKmBillingType(kmBillingType);
			 dto.setKmBillingRefId(refId);
			 dto.setVehicleTypeId(selectedVehicleType);
			 dto.setSwingRateType(swingDistanceType);
			 dto.setSwingRate(swingRate);
			 dto.setId(id);
			 System.out.println("ID FOR UPDATE : "+ id);
			 //dto.setTemplateId(Integer.parseInt(templateId));
			 dto.setAcYes(acYes);
			 System.out.println("trackk 001");
			 if(dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
				 String [] fromHour = request.getParameterValues("templateFromHour");
				 String [] fromMinute = request.getParameterValues("templateFromMinute");
				  
				 
				 String [] toHour = request.getParameterValues("templateToHour");
				 String [] toMinute = request.getParameterValues("templateToMinute");
				  
				 
				 String [] acRates = request.getParameterValues("templateAcRate");
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
				  System.out.println("trackk --- " + fromHour.length);
			  dto.setAcList(acList);
			 
			 }
			 
			  
			 System.out.println("trackk 002");	 
			 KmBasedTemplateBillingService service = new KmBasedTemplateBillingService();
				 
			if(service.validateKmBasedTemplateBillingForEdit(dto) ==false)
			{
				
				request.getSession().setAttribute("status",
						"<div class=\"failure\" >Validation Failure:!</div>"+service.getMessage());
			}else
			{
			int val=service.updateKmBasedTemplateConfig(dto);
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
			System.out.println("Exception in AddTemplateBasedbillingConfig : "+ e);
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Error In Server!</div>");
	
			
		}
		response.sendRedirect("kmBasedTripBillingConfig.jsp?refId="+refId);
	}
		


}
