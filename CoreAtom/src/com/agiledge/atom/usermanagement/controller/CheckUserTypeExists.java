package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.agiledge.atom.usermanagement.dto.UserManagementDto;
import com.agiledge.atom.usermanagement.service.UserManagementService;

/**
 * Servlet implementation class CheckExists
 */
 
public class CheckUserTypeExists extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckUserTypeExists() {
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
		 
		PrintWriter out  =  response.getWriter();
		JSONObject json = new JSONObject();
		try {
			System.out.println("Inside CheckExists");
			UserManagementDto dto = new UserManagementDto();
			
			dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
			dto.setName(request.getParameter("name"));
			dto.setName( dto.getName()==null?"":dto.getName());

			dto.setUserType( request.getParameter("userType")); 
			dto.setUserType( dto.getUserType()==null?"":dto.getUserType());
			System.out.println("name :"+ dto.getName() + " user type : "+ dto.getUserType());
			
			UserManagementService service = new UserManagementService();
			
			if(service.checkUserTypeExists(dto)) {
				 
				json.put("result", "true");
				json.put("message", "Duplicate");
			}
			else {
				System.out.println("false");
				json.put("result", "false");
				json.put("message", "");
			}
			 
			
		}catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Error :"+e.getMessage() + "</div>");
		}
		out.write(json.toString());
		 
	 
	}

}
