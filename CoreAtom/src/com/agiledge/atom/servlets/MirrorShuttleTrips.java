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

public class MirrorShuttleTrips extends HttpServlet {

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		DateFormat df = new SimpleDateFormat("dd/MM/yyyy");
		String siteId = req.getParameter("siteId");
		String sourcedate = req.getParameter("tripDate");
		String fromdate = req.getParameter("fromDate");
		String todate = req.getParameter("toDate");
		Date date, date1, date2;
		try {
			date = df.parse(sourcedate);
			sourcedate = OtherFunctions.changeDateFromatToSqlFormat(date);
			date1 = df.parse(fromdate);
			fromdate = OtherFunctions.changeDateFromatToSqlFormat(date1);
			date2 = df.parse(todate);
			todate = OtherFunctions.changeDateFromatToSqlFormat(date2);
			Boolean flag = new TripMirrorDao().checkTripWithDate(sourcedate,
					"ALL", "ALL");
			if (flag) {
				Boolean flag1 = new TripMirrorDao().checkTripWithDateRange(fromdate, todate);
				if (flag1) {
					session.setAttribute("status",
							"<div class='failure'>Trips exist on this date range</div");
					resp.sendRedirect("mirrorShuttleTrip.jsp?siteId="
							+ req.getParameter("siteId") + "&tripDate="
							+ req.getParameter("tripDate") + "&fromDate="
							+ req.getParameter("fromDate") + "&toDate="
							+ req.getParameter("toDate"));
				} else {
					int result = new TripMirrorDao().mirrorFixedTrips(siteId,sourcedate, fromdate, todate);
					if (result > 0) {
						session.setAttribute("status",
								"<div class='success'>Trip Generated successfully</div");
						resp.sendRedirect("mirrorShuttleTrip.jsp");
					} else {
						session.setAttribute("status",
								"<div class='failure'>Trip Mirroring Failed</div");
						resp.sendRedirect("mirrorShuttleTrip.jsp");
					}
				}
			} else {
				session.setAttribute("status",
						"<div class='failure'>Trips not Found on source date</div");
				resp.sendRedirect("mirrorShuttleTrip.jsp?siteId="
						+ req.getParameter("siteId") + "&tripDate="
						+ req.getParameter("tripDate") + "&fromDate="
						+ req.getParameter("fromDate") + "&toDate="
						+ req.getParameter("toDate"));
			}
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
