/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.service.BackToBackRoutingService;



/**
 * 
 * @author muhammad
 */
public class BackToBackRouting extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession();
		String site=request.getParameter("siteId");
		String date = OtherFunctions.changeDateFromatToIso(request.getParameter("for_date"));
		System.out.println("site"+site+date);
		int result=new BackToBackRoutingService().BackToBackRouting(date, site);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class=\"success\" > Operation Successful</div>");
		}
		else
		{
			session.setAttribute(
					"status",
					"<div class=\"failure\" > Operation Failed</div>");
			
		}
		response.sendRedirect("viewbacktoback.jsp");
		/*String doneBy = session.getAttribute("user").toString();
		String check = request.getParameter("check");
		int siteid = Integer.parseInt(request.getParameter("siteId"));
		String date = OtherFunctions.changeDateFromatToIso(request
				.getParameter("for_date"));
		String logTime = request.getParameter("logTime");
		String log = request.getParameter("log");
		String overwrite = request.getParameter("isOverwrite");
		RoutingDto routingDto = new RoutingDto();
		routingDto.setDoneBy(doneBy);
		routingDto.setSiteId(siteid);
		routingDto.setDate(date);
		routingDto.setTime(logTime);
		System.out.println("log"+log);
		routingDto.setTravelMode(log);
		String status = "";
		if (check == null) {			
			try {
				if (overwrite != null) {
					status = new BackToBackRoutingService().routeProcess(
							routingDto, overwrite);
				} else {
					System.out.println("In Else");
					status = new BackToBackRoutingService().routeProcess(
							routingDto, null);
				}

				// System.out.println("Step 8 :close the Site");

				// response.sendRedirect("view_routing.jsp");
				if (status.equals("success")) {
					session.setAttribute("status",
							"<div class=\"success\" > Routing Successful</div>");
				} else if (status.equals("noEmps")) {
					session.setAttribute(
							"status",
							"<div class=\"failure\" > No employee is scheduled or Allocation already Done</div>");
					// response.sendRedirect("view_routing.jsp");
				}

			} catch (Exception ex) {
				session.setAttribute("status",
						"<div class=\"failure\" > Routing failed !</div>");
				// TODO Auto-generated catch block
				ex.printStackTrace();
				// response.sendRedirect("view_routing.jsp");
			}
			response.sendRedirect("backtoback_routing.jsp");
		}

		else {
			PrintWriter out = response.getWriter();
			try {
				System.out.println("In ajax");
				if (new BackToBackRoutingService().checkTripExist(routingDto) > 0) {
					out.print("true");
				} else {
					out.print("false");
				}
			} finally {
				out.close();
			}
		}*/

	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}




































