package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.mobile.service.TripService;
import com.agiledge.atom.reports.TripUtilisationReportHelper;
import com.agiledge.atom.service.VehicleService;

/**
 * Servlet implementation class GetVehiclePosition
 */
public class GetVehiclePosition extends HttpServlet {
	private static final long serialVersionUID = 1L;

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		PrintWriter out = response.getWriter();
		String retString = "";
		String tripId = request.getParameter("tripId");
		String tripInterval = request.getParameter("tripInterval");

		if (tripId == null) {
			String branch = request.getParameter("branch");
			String filshift=request.getParameter("shift");
			String vehno=request.getParameter("regno");
			if(filshift==null)
			{
				filshift="ALL";
			}
			if(vehno==null)
			{
				vehno="ALL";
			}
			System.out.println("vehno"+vehno);
		/*	ArrayList<TripDetailsDto> dtos = new TripDetailsDao()
					.getTripsWithVehicle(branch,filshift,vehno);*/
			ArrayList<TripDetailsDto> dtos = new TripUtilisationReportHelper()
			.getTripsWithVehicle(branch,filshift,vehno);
			dtos.addAll(new TripService().getStationaryVehiclePosition(branch));
			
			for (TripDetailsDto dto : dtos) {
				retString += dto.getId() + "~" + dto.getVehicleNo() + "~"
						+ dto.getLatitude() + "~" + dto.getLongitude() + "~"
						+ dto.getStatus() + "~" + dto.getLadyInCount() + "~"
						+ dto.getEmpInCount() + "~" + dto.getIsSecurity() + "~"
						+ dto.getPanicAck() + "~" + dto.getTrip_code() +"~"
						+ dto.getEmpCount() + "~" + dto.getTrip_date() +"~"
						+dto.getTrip_log()+"|";
				if(dto.getTripDetailsChildDtoList()!=null&&dto.getTripDetailsChildDtoList().size()>0)
				for (TripDetailsChildDto childDto : dto
						.getTripDetailsChildDtoList()) {
					retString += childDto.getLatitude() + ":"
							+ dto.getLongitude() + "~";
				}
				retString += "#";
			}

			out.write(retString);
		}
		else if(tripInterval!=null) {
			ArrayList<VehicleDto> dtos = new VehicleService().getVehicleTrackInInterval(tripId);

			for (VehicleDto dto : dtos) {
				retString += dto.getDateTime() + "~" + dto.getLattitude()
						+ "~" + dto.getLongitude() + "|";
			}
			//System.out.println("vehicle Path"+retString);
			out.write(retString);
		}
		else {
			ArrayList<VehicleDto> dtos = new VehicleDao().vehicleTrack(tripId);

			for (VehicleDto dto : dtos) {
				retString += dto.getVehicleNo() + "~" + dto.getLattitude()
						+ "~" + dto.getLongitude() + "|";
			}
			//System.out.println("vehicle Path"+retString);
			out.write(retString);
		}

	}

	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
