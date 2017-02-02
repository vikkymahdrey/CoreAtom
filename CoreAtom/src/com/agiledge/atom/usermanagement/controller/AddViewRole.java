package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.usermanagement.service.ViewManagementService;


/**
 * Servlet implementation class AddViewRole
 */

public class AddViewRole extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddViewRole() {
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
		int roleId = Integer.parseInt(request.getParameter("roleId"));
		String viewId[] = request.getParameterValues("choosenviews");
		String roleName = request.getParameter("roleName");
		HttpSession session = request.getSession();
		int status = new ViewManagementService().AddRoleViewAssociation(viewId,
				roleId);
		if (status > 0) {
			 
			OtherFunctions.resetViews( roleId,  request.getSession().getServletContext());
			session.setAttribute("status",
					"<div class='success'>View Added Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>View Adding Failed</div");
		}
		response.sendRedirect("roleView.jsp?roleId=" + roleId + "&roleName="
				+ roleName);

	}

}
