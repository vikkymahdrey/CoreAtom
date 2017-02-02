package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.TimeSloatDao;
import com.agiledge.atom.dto.TimeSloatDto;


/**
 * Servlet implementation class TimeSloat
 */
public class TimeSloat extends HttpServlet {
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
			HttpServletResponse response) {
		try {
			String[] traffic = request.getParameterValues("traffic");
			System.out.println("tarck");
			String[] timeStartHour = request
					.getParameterValues("time_start_hr");
			System.out.println("tarck");
			String[] timeStartMnt = request
					.getParameterValues("time_start_mnt");
			System.out.println("tarck");
			String[] timeEndHour = request.getParameterValues("time_end_hr");
			System.out.println("tarck");
			String[] timeEndMnt = request.getParameterValues("time_end_mnt");
			System.out.println("tarck");
			String[] speed = request.getParameterValues("speed");
			System.out.println("tarck");

			ArrayList<TimeSloatDto> timeSloats = new ArrayList<TimeSloatDto>();
			TimeSloatDto timeSloat = null;
			for (int i = 0; i < traffic.length; i++) {
				timeSloat = new TimeSloatDto();
				timeSloat.setTraffic(traffic[i]);
				timeSloat
						.setTimeStart(timeStartHour[i] + ":" + timeStartMnt[i]);
				timeSloat.setTimeEnd(timeEndHour[i] + ":" + timeEndMnt[i]);
				timeSloat.setSpeed(speed[i]);
				timeSloats.add(timeSloat);
			}
			int retVal = new TimeSloatDao().addTimeSloat(timeSloats);
			response.sendRedirect("showTimeSloat.jsp");
		} catch (Exception e) {
			System.out.println("Error" + e);
		}
	}

}
