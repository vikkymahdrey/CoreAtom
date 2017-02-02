package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.service.EmployeeService;

/**
 * Servlet implementation class UpdateEmpGeocode
 */
public class UpdateEmpGeocode extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateEmpGeocode() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}
	
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String empid=request.getParameter("emp_code");
		String latitude=request.getParameter("emp_lat");
		String longitude=request.getParameter("emp_long");
		int status=new EmployeeService().UpdateEmpLatLong(empid, latitude, longitude);
		if (status == 1) {
			request.getSession().setAttribute("status",
					"<div class=\"success\" > Data Saved Successfully!</div>");
			
		}

		else {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" > Operation Failure!</div>");

		}
		response.sendRedirect("emplatlong.jsp");
	}

}
