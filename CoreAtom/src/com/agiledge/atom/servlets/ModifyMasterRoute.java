package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.RouteService;


/**
 * Servlet implementation class SetRouteRule
 */
public class ModifyMasterRoute extends HttpServlet {

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	int routeId=Integer.parseInt(request.getParameter("routeId"));
	String routeName=request.getParameter("routeName");
	String routeType=request.getParameter("routeType");
	HttpSession session = request.getSession(true);
	String doneBy = session.getAttribute("user").toString();
	RouteDto routeDto=new RouteDto();
	routeDto.setRouteId(routeId);
	routeDto.setRouteName(routeName);
	routeDto.setRouteType(routeType);
	routeDto.setDoneBy(doneBy);
	
	try {
		int value=new RouteService().updateMasterRoute(routeDto);
		if (value > 0) {		
			request.getSession()
					.setAttribute("status",
							"<div class=\"success\" >  Route Type Modification Successful </div>");
		} else {
			request.getSession()
					.setAttribute("status",
							"<div class=\"failure\" > Route Type Modification Failed </div>");
		}

	} catch (Exception e) {
		request.getSession()
		.setAttribute("status",	"<div class=\"failure\" > Route Type Modification Failed </div>");
		e.printStackTrace();
	}
	response.sendRedirect("route_list.jsp");
	}

}
