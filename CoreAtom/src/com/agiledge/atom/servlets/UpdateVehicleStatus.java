package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.VehicleService;

/**
 * Servlet implementation class UpdateVehicleStatus
 */
public class UpdateVehicleStatus extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateVehicleStatus() {
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
		// TODO Auto-generated method stub

		HttpSession session=request.getSession();
		String id=request.getParameter("vehid");
		String status=request.getParameter("active");
		int result=new VehicleService().UpdateVehicleStatus(id,status);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Status Updated Successfully</div");
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Status Updation Failed</div");
		}
		response.sendRedirect("view_vehicle.jsp");
		
		
	
	}

}
