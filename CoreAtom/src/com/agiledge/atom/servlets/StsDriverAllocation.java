package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.service.StsService;

public class StsDriverAllocation extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		int result = 0;

		String tripid = req.getParameter("tripid");
		String approvedby = req.getParameter("empid");
		if (req.getParameter("details").equalsIgnoreCase("registered")) {

			String drivername = req.getParameter("drivername");
			String vehicle = req.getParameter("vehicletype");
			String regno = req.getParameter("regno");
			TripDetailsDto dto = new TripDetailsDto();
			dto.setId(tripid);
			dto.setVehicle_type(vehicle);
			dto.setVehicleNo(regno);
			dto.setDriverName(drivername);
			dto.setDoneBy(approvedby);
			result = new StsService().stsTripDriverAssaign(dto);
		} else {
			result = new StsService().thirdPartyAssaign(tripid, approvedby,
					req.getParameter("details"));
		}
		if (result > 0) {
			session.setAttribute("status",
					"<div class='success'>Registered Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Failed</div");
		}
		resp.sendRedirect("employee_home.jsp");
	}
}
