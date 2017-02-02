package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.service.PageService;

/**
 * Servlet implementation class SetRolesHomePage
 */
 
public class SetRolesHomePage extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetRolesHomePage() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
			doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//  
		
		JSONObject json = new JSONObject();
		 try {
			 	
			 	String userType = request.getParameter("userType");
			 	String urlId =  request.getParameter("urlId");
			   	 System.out.println("usertype : "+ userType + " urlId " + urlId);
			 	  ViewManagementDto dto = new ViewManagementDto();
			 	  dto.setRoleName(userType);
			 	  dto.setViewUrlId(urlId);
			 	  dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
			 	  
			 	  
			 			
			 	
			 	 PageService service = new PageService();
			 	int returnInt =0;
			 	  returnInt = service.setHomePage(dto);
			 	if(returnInt>0) {
			 		json.put("result", "true");
			 		json.put("message",  "Home page assignment successfull.");
			 	} else {
			 		json.put("result", "false");
			 		json.put("message",  "Home page assignment failed.");
			 	}
			  
			 
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

 
