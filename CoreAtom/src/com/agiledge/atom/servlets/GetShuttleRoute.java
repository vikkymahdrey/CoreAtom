package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.RouteDao;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.ShuttleServcie;

/**
 * Servlet implementation class GetShuttleRoute
 */

public class GetShuttleRoute extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetShuttleRoute() {
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
		String retString = "";
		try {
			String routeId = request.getParameter("routeId");
			String loginTime = request.getParameter("loginTime");
			String logoutTime = request.getParameter("logoutTime");
			ArrayList<RouteDto> list = null;	
			int seats=0;
			if (loginTime != null) {
				seats=new RouteDao().getShuttleSeat(routeId, loginTime)- new ShuttleServcie().getTotalbookings(routeId, loginTime);
				retString=getRetString(seats);
			} else if (logoutTime != null) {
				seats=new RouteDao().getShuttleSeat(routeId, logoutTime)- new ShuttleServcie().getTotalbookings(routeId, logoutTime);
				retString=getRetString(seats);
			} else {
				list = new ShuttleServcie().getShuttleRouteDetails(Integer
						.parseInt(routeId));
				retString = "<table>";
				for (RouteDto dto : list) {
					retString += "<tr><td>" + dto.getArea() + "->"
							+ dto.getPlace() + "->" + dto.getLandmark()
							+ "</td></tr>";
				}
				retString += "</table>";	
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		}		
		PrintWriter out = response.getWriter();
		out.write(retString);
	}
	public String getRetString(int seats)
	{
		String retString="";
		if(seats<=0)
		{
			seats=(seats-1)*(-1);
			retString=" Waiting List"+seats;	
		}
		else
		{
			retString=seats+" Seats Avialable";
		}
		return retString;
	}

}
