package com.agiledge.atom.transporttype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.transporttype.dto.TransportTypeDto;
import com.agiledge.atom.transporttype.service.TransportTypeService;


/**
 * Servlet implementation class GetTransportTypes
 */
 
public class GetTransportTypes extends HttpServlet {
	private static final long serialVersionUID = 1L;
  
    public GetTransportTypes() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		 
		 doProcess(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  
		doProcess(request, response);
	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		
		JSONObject json = new JSONObject();
		 try {
			 String siteId = request.getParameter("siteId");
			 String vendorId = request.getParameter("vendorId");
			 TransportTypeDto dto = new TransportTypeDto();
				dto.setSiteId(siteId);
				dto.setVendorId(vendorId);
				dto.setStatus("present");
			System.out.println("reached here");
			 	json = new TransportTypeService().getTransportTypeMappingsInJSON(dto);
		
			 
		 } catch(Exception e) {
			 try {
		 			json.put("result", "false");
		 		 
					json.put("message",  "Error :"+ e);
				 
			 } catch(org.json.JSONException jsonException) {
				 response.getWriter().write("{result:\"true\", message:\"Error :" + jsonException + "\" }");
			 }
		 }
		 
		 response.getWriter().write(json.toString());
			 
		  
		
		
	}

}
