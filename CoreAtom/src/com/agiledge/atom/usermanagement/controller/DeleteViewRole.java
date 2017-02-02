package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.usermanagement.service.ViewManagementService;


/**
 * Servlet implementation class DeleteViewRole
 */

public class DeleteViewRole extends HttpServlet {
	

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteViewRole() {
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
		int roleId = Integer.parseInt(request.getParameter("deleteroleId"));
		int viewId = Integer.parseInt(request.getParameter("deleteviewId"));
		String roleName = request.getParameter("deleteroleName");
		String actualId = request.getParameter("actualId");
		int result = new ViewManagementService().DeleteRoleViewAssociation(actualId,
				roleId, viewId);
		if (result >0) {
			EmployeeDto employeeDto = (EmployeeDto) request.getSession().getAttribute("userDto");
			OtherFunctions.resetViews(employeeDto.getRoleId(), request.getSession().getServletContext());
		
			session.setAttribute("status",
					"<div class='success'>View Deleted Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>View Deletion Failure</div");
		}
		response.sendRedirect("roleView.jsp?roleId=" + roleId + "&roleName="
				+ roleName);

	}

}
