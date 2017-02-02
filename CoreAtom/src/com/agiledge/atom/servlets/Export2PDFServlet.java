package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.printutil.Export2PDF;
import com.agiledge.atom.service.TripDetailsService;


/**
 * Servlet implementation class Export2PDFServlet
 */

public class Export2PDFServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Export2PDFServlet() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
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

	public void processRequest(HttpServletRequest request,
			HttpServletResponse response) {
		response.setContentType("application/pdf");

		String tripDate = request.getParameter("tripDate");
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		ArrayList<TripDetailsDto> tripSheetSaved = new TripDetailsService().getTripSheetSaved(tripDate, tripMode, siteId, tripTime);
		try {
			ServletOutputStream sos = response.getOutputStream();
			Export2PDF xpdf = new Export2PDF(sos, tripSheetSaved);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
