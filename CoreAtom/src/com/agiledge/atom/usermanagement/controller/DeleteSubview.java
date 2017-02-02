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
 * Servlet implementation class DeleteSubview
 */

public class DeleteSubview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DeleteSubview() {
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
		int subviewId = Integer.parseInt(request.getParameter("deleteId"));
		int viewId = Integer.parseInt(request.getParameter("deleteviewid"));
		String viewName = request.getParameter("deleteviewname");
		HttpSession session = request.getSession();
		ViewManagementService service =  new ViewManagementService();
		int result = service.deleteSubview(subviewId);
		if (result >0) {
			 
			service.resetView("", request.getSession().getServletContext());
			session.setAttribute("status",
					"<div class='success'>Subview Deleted Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Subview Deletion Failure</div");
		}
		response.sendRedirect("ViewSubview.jsp?viewId=" + viewId + "&viewName="
				+ viewName);
	}

}
