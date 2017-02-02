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
 * Servlet implementation class EditSubview
 */
public class EditSubview extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditSubview() {
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
		 System.out.println("In edit sub view servlet");
		HttpSession session = request.getSession();
		ViewManagementDto dto = new ViewManagementDto();
		dto.setSubViewId(Integer.parseInt(request.getParameter("subviewId")));
		dto.setParentId(Integer.parseInt(request.getParameter("parentId")));
		dto.setSubViewName(request.getParameter("subviewName"));
		dto.setSubViewKey(request.getParameter("subviewKey"));
		dto.setSubViewURL(request.getParameter("url"));
		dto.setSubViewShowOrder(Integer.parseInt(request
				.getParameter("showorder")));
		System.out.println("SHow order : "+ request
				.getParameter("showorder"));
		ViewManagementService service = new ViewManagementService();
		int status = service.UpdateSubView(dto);
		if (status >0) {
			 
			service.resetView(dto.getViewKey(), request.getSession().getServletContext());
			session.setAttribute("status",
					"<div class='success'>Subview Updated Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Subview Updation Failed" + service.getMessage() + "</div");
		}
		 
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/editSubview.jsp");
		request.setAttribute("closeFlag", "true");
		rd.forward(request, response);
		

	}

}
