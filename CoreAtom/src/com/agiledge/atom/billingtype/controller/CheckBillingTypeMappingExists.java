package com.agiledge.atom.billingtype.controller;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.billingtype.dto.BillingTypeDto;
import com.agiledge.atom.billingtype.service.BillingTypeService;


/**
 * Servlet implementation class CheckBillingTypeMappingExists
 */
 
public class CheckBillingTypeMappingExists extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckBillingTypeMappingExists() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doProcess(request,response);
	}

	private void doProcess(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		JSONObject json = new JSONObject();
		 try {
			 
			 
				BillingTypeDto dto = new BillingTypeDto();
				dto.setId( String.valueOf(request.getParameter("type")));
				dto.setSite(String.valueOf(Integer.parseInt(request.getParameter("site"))));
				dto.setVendor(String.valueOf(Integer.parseInt(request.getParameter("vendor"))));
				dto.setTransportType(String.valueOf(Integer.parseInt(request.getParameter("tran_type"))) );
				
				Calendar cal = Calendar.getInstance();
	 			cal.setTime(  new Date() );
	 			cal.add(Calendar.DATE, 1);
	 			dto.setFromDate(cal.getTime());
	 			 
				  
				
				 
				BillingTypeService service = new BillingTypeService();
				if(service.checkAnyFutureEntry(dto)) {
			 		json.put("result", "true");
			 		json.put("message",  "Please delete existing entry.");
			 	} else {
			 		json.put("result", "false");
			 		json.put("message",  "");
			 	}
			  
			 
		 } catch(Exception e) {
			 try {
		 			json.put("result", "true");
		 		 
					json.put("message",  "Error :"+ e);
				 
			 } catch(org.json.JSONException jsonException) {
				 response.getWriter().write("{result:\"true\", message:\"Error :" + jsonException + "\" }");
			 }
		 }
		 
		 response.getWriter().write(json.toString());

			 
			  
		
	}

}
