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
 * Servlet implementation class DeleteView
 */
 
public class DeleteView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteView() {
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
		//  
		int viewId = Integer.parseInt(request.getParameter("deleteId"));
		HttpSession session = request.getSession();
		int result = new ViewManagementService().DeleteView(viewId);
		if (result == 1) {
			EmployeeDto employeeDto = (EmployeeDto) request.getSession().getAttribute("userDto");
			 
			ViewManagementService service =  new ViewManagementService();
			service.resetView("", request.getSession().getServletContext());
			session.setAttribute("status",
					"<div class='success'>View Deleted Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>View Deletion Failure</div");
		}
		response.sendRedirect("AllView.jsp");

	}

}
