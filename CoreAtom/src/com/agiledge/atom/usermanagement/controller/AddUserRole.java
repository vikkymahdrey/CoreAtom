package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.usermanagement.dto.UserManagementDto;
import com.agiledge.atom.usermanagement.service.UserManagementService;

/**
 * Servlet implementation class AddUserRole
 */
 
public class AddUserRole extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUserRole() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		PrintWriter out  =  response.getWriter();
		try {
			UserManagementDto dto = new UserManagementDto();
			
			dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
			dto.setName(request.getParameter("name"));
			dto.setDescription(request.getParameter("description"));
			dto.setUserType( request.getParameter("userType")); 
			
			UserManagementService service = new UserManagementService();
			if(service.validate(dto)) {
				
				int val=service.addUserRole(dto);
				if(val>0) {
					request.getSession().setAttribute("status",
							"<div class=\"success\" width=\"100%\" >Success: New role added.</div>");
				} else {
					request.getSession().setAttribute("status",
							"<div class=\"failure\" width=\"100%\" >Failure: New role is not added.</div>");
				}
			} else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Validation Error :"+service.getMessage() + "</div>");
			}
			 
			
		}catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Error :"+e.getMessage() + "</div>");
		}
		
		response.sendRedirect("userRoleSetUp.jsp");
	}

}
