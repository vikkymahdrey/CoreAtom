package com.agiledge.atom.usermanagement.controller;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
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
 * Servlet implementation class EditView
 */
 
public class EditView extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditView() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		ViewManagementDto dto = new ViewManagementDto();
		dto.setViewId(Integer.parseInt(request.getParameter("viewId")));
		dto.setViewName(request.getParameter("viewName"));
		System.out.println("view key :" + request.getParameter("viewKey"));
		dto.setViewKey(request.getParameter("viewKey"));
		dto.setViewUrlId(request.getParameter("url"));
		dto.setViewShowOrder(Integer.parseInt(request.getParameter("showorder")));
		ViewManagementService service =  new ViewManagementService();
		int status = service.UpdateView(dto);
		if (status >0) {
			EmployeeDto employeeDto = (EmployeeDto) request.getSession().getAttribute("userDto");
			 
			service.resetView(dto.getViewKey(), request.getSession().getServletContext());
			session.setAttribute("status",
					"<div class='success'>View Updated Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>View Updation Failed: "+service.getMessage()+"</div");
		}
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/editSubview.jsp");
		request.setAttribute("closeFlag", "true");

	}

}
