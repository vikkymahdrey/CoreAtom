package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedMapTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.KmBasedMapBillingTypeConfigService;


/**
 * Servlet implementation class AddTripBasedBillingConfig
 */
 
public class UpdateKmBasedMapBillingConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateKmBasedMapBillingConfig() {
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
		String kmBillingType=request.getParameter("kmBillingTypeHidden")==null?"":request.getParameter("kmBillingTypeHidden");
		String selectedVehicleType = request.getParameter("selectedVehicleType")==null ?"": request.getParameter("selectedVehicleType");
		String distanceType = request.getParameter("distanceType")==null ?"": request.getParameter("distanceType");
		String calculationType = request.getParameter("calculationType")==null ?"": request.getParameter("calculationType");
		String swingDistance = request.getParameter("swingDistance")==null ?"": request.getParameter("swingDistance");
		String id =  request.getParameter("mapId")==null ?"": request.getParameter("mapId");
		String acYes = request.getParameter("mapAcYes") ==null ? "" : request.getParameter("mapAcYes");
		String escortRateType =request.getParameter("mapEscortRateType") ==null ? "" : request.getParameter("mapEscortRateType");
		String escortRate =  request.getParameter("mapEscortRate") ==null ? "" : request.getParameter("mapEscortRate");
		String mapTripRate = request.getParameter("mapTripRate") ==null ? "" : request.getParameter("mapTripRate");
		
		System.out.println(" ID : " + id);
		try{
			
			 KmBasedMapTripBillingConfigDto dto = new KmBasedMapTripBillingConfigDto();
			 dto.setAcYes(acYes);
			 dto.setEscortRate(escortRate);
			 dto.setEscortRateType(escortRateType);
			 dto.setTripRate(mapTripRate);
		
			 dto.setDoneBy(request.getSession().getAttribute("user").toString());
			 dto.setKmBillingType(kmBillingType);
			 dto.setVehicleTypeId(selectedVehicleType);
			 dto.setDistanceType(distanceType);
			 dto.setCalculationType(calculationType);
			 dto.setRate(swingDistance); 
			 dto.setId(id);
			 if(dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
				 String [] fromHour = request.getParameterValues("mapFromHour");
				 String [] fromMinute = request.getParameterValues("mapFromMinute");
				  
				 
				 String [] toHour = request.getParameterValues("mapToHour");
				 String [] toMinute = request.getParameterValues("mapToMinute");
				  
				 
				 String [] acRates = request.getParameterValues("mapAcRate");
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
			 KmBasedMapBillingTypeConfigService service = new KmBasedMapBillingTypeConfigService();
				 
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
