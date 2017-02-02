package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.service.ViewManagementService;

/**
 * Servlet implementation class AddView
 */
 
public class AddView extends HttpServlet {
 

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddView() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		ViewManagementDto dto = new ViewManagementDto();
		dto.setViewName(request.getParameter("viewName"));
		dto.setViewShowOrder(Integer.parseInt(request.getParameter("showorder")));
		dto.setViewUrlId(request.getParameter("url"));
		dto.setViewKey(request.getParameter("viewKey"));
		
		dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
		ViewManagementService service =		new  ViewManagementService();
		
		int status = service.addView(dto);
		
		if (status == 1) {
			
			 
			service.resetView("", request.getSession().getServletContext());
		
			session.setAttribute("status",
					"<div class='success'>View Created Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>View Creation Failed: "+ service.getMessage()+ " </div");
		}
		response.sendRedirect("AllView.jsp");
	}

}
