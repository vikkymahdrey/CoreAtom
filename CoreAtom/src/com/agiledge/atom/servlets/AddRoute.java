package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.RouteService;

/**
 * Servlet implementation class AddRoute
 */

public class AddRoute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

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
		HttpSession session1 = request.getSession(true);
		String doneBy = session1.getAttribute("user").toString();
		String[] routeAPL = request.getParameterValues("selectedapl");
		String routeName = request.getParameter("routeName");
		String site = request.getParameter("routeSite");
		String inOut = request.getParameter("inOut");
		String routeType = request.getParameter("routeType");
		HttpSession session = request.getSession();
		ArrayList<RouteDto> routeLandmarks = new ArrayList<RouteDto>();
		RouteDto routeDto = null;
			for (String landmark : routeAPL) {
				routeDto = new RouteDto();
				
				routeDto.setSiteId(site);
				routeDto.setRouteType(routeType);
				routeDto.setRouteName(routeName);
				routeDto.setLandmarkId(landmark);
				routeDto.setInOut(inOut);
				routeDto.setDoneBy(doneBy);
				routeLandmarks.add(routeDto);
			}		
		int status = 0;
		if (routeType != null)
			status = new RouteService().insertRoute(routeLandmarks);
		else
			status = new RouteService().insertShuttleRoute(routeLandmarks);
		if (status > 0) {
			session.setAttribute("status",
					"<div class='success'>Route Created Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Route Creation Failed</div");
		}
		if (routeType != null)
			response.sendRedirect("route_list.jsp");
		else
			response.sendRedirect("routeListShuttle.jsp");
	}

}
