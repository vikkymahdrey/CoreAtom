package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.service.ViewManagementService;

/**
 * Servlet implementation class AddSubview
 */

public class AddSubview extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public AddSubview() {
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
		dto.setSubViewName(request.getParameter("subviewName"));
		dto.setSubViewKey(request.getParameter("subviewKey"));
		dto.setParentId(Integer.parseInt(request.getParameter("parentId")));
		dto.setSubViewShowOrder(Integer.parseInt(request
				.getParameter("showorder")));
		System.out.println("SHOW ORDER : " + dto.getSubViewShowOrder());

		dto.setSubViewURL(request.getParameter("url"));
		String viewName = request.getParameter("viewName");
		dto.setUpdatedBy(session.getAttribute("user").toString());
		ViewManagementService service = new ViewManagementService();
		int status = service.AddSubView(dto);
		System.out.println("Status " + status);
		if (status > 0) {
			try { 
				
				service.resetView(dto.getViewKey(), request.getSession().getServletContext());
				
			}catch(Exception ex)  {
				System.out.println(" Exception : ex : " + ex);
			}
			System.out.println(" Reset view ..");
			session.setAttribute("status",
					"<div class='success'>Subview Created Successfully</div");
		} else {
			session.setAttribute(
					"status",
					"<div class='failure'>Subview Creation Failed"
							+ service.getMessage() + "</div");
		}
		System.out.println("ViewSubview.jsp?viewId="
				+ request.getParameter("parentId") + "&viewName=" + viewName);
		response.sendRedirect("ViewSubview.jsp?viewId="
				+ request.getParameter("parentId") + "&viewName=" + viewName);

	}

}
