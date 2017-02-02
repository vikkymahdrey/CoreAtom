package com.agiledge.atom.servlets;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.TripMirrorDao;
import com.agiledge.atom.dto.TripDetailsDto;

public class GenerateFixedRoute extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String siteid = req.getParameter("siteId");
		String tripDate = req.getParameter("tripDate");
		String tripLog = req.getParameter("tripLog");
		String tripTime = req.getParameter("tripTime");
		HttpSession session = req.getSession();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		Date date;
		try {
			date = df.parse(tripDate);
			String travelDate = OtherFunctions
					.changeDateFromatToSqlFormat(date);
			Boolean flag = new TripMirrorDao().checkTripWithDate(travelDate,
					tripLog, tripTime);
			if (flag) {
				session.setAttribute("status",
						"<div class='failure'>Trips Already Generated</div");
				resp.sendRedirect("generateFixedTrips.jsp");
			} else {
				int result = new TripMirrorDao().generatefixedRouteTrips(travelDate, tripLog, tripTime, siteid);
				if (result > 0) {

					session.setAttribute("status",
							"<div class='success'>Trip Generated successfully</div");
					resp.sendRedirect("view_routing.jsp");
				} else {
					session.setAttribute("status",
							"<div class='failure'>Failed</div");
					resp.sendRedirect("generateFixedTrips.jsp");
				}
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
