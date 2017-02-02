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
 * Servlet implementation class ModifyRoute
 */
public class ModifyRoute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ModifyRoute() {
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
		HttpSession session1 = request.getSession(true);
		String doneBy = session1.getAttribute("user").toString();
		// System.out.println("In servlet");
		String[] routeAPL = request.getParameterValues("routeApl");
		int routeId = Integer.parseInt(request.getParameter("routeId"));
		HttpSession session = request.getSession();
		ArrayList<RouteDto> routeLandmarks = new ArrayList<RouteDto>();
		RouteDto routeDto = null;
		for (String landmark : routeAPL) {
			routeDto = new RouteDto();
			routeDto.setDoneBy(doneBy);
			routeDto.setLandmarkId(landmark);
			routeLandmarks.add(routeDto);
		}
		int status = new RouteService().modifyRoute(routeLandmarks, routeId);
		if (status > 0) {
			session.setAttribute("status",
					"<div class='success'>Route Modification Successful</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Route Modification Failed</div");
		}
		response.sendRedirect("route_list.jsp");
	}

}
