package com.agiledge.atom.billingtype.config.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.billingtype.config.dto.SlabBasedBillingConfigDto;
import com.agiledge.atom.billingtype.config.service.SlabBasedBillingTypeConfigService;


/**
 * Servlet implementation class GetSlabBasedBillingConfigTypes
 */
 
public class GetSlabBasedBillingConfigTypes extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSlabBasedBillingConfigTypes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doProcess(request, response);
	}
	
	private void doProcess(HttpServletRequest request, HttpServletResponse response) throws IOException {
		String jsonString ="{result:false;}";
		try {
			String refId = request.getParameter("refId");
			String vehicleTypeId = request.getParameter("vehicleTypeId");
			System.out.println("vehicle Tpe Id : "+ vehicleTypeId);
			SlabBasedBillingTypeConfigService service = new SlabBasedBillingTypeConfigService();
			SlabBasedBillingConfigDto slabDto = service.getSlabBasedBillingConfig(refId, vehicleTypeId);
			  
			if(slabDto!= null) {
				JSONObject json = new JSONObject(slabDto);
				  json.put("result", "true");
				  json.put("slabList", slabDto.getSlabList());
				   
				  jsonString = json.toString();
			}
			
		}catch(Exception e) {
			System.out.println("Error in GetSlabBasedBillingConfigTypes : "+ e);
			
		}
		response.getWriter().print(jsonString);
	}

}
