package com.agiledge.atom.servlets.apl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.service.APLService;


/**
 * Servlet implementation class GetPlace
 */
public class GetPlace extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	
		private void processRequest(HttpServletRequest request,
				HttpServletResponse response) throws IOException {
		int areaId = Integer.parseInt(request.getParameter("area"));
System.out.println("Here inside GetPlace");
		PrintWriter out = response.getWriter();
		String retText = "";
		try {
			ArrayList<APLDto> placeDtos = new APLService()
					.getPlacesByAreaId(areaId);
			retText = "<select name='place' id='place' onchange='showLandmark(this.value)'>";
			retText += "<option value='0' >Select</option>";
			for (APLDto placeDto : placeDtos) {
				retText += "<option value='" + placeDto.getPlaceID() + "' >"
						+ placeDto.getPlace() + "</option>";
			}
			retText += "</select>";
			System.out.println("Result : " + retText);
			out.write(retText);

		} finally {
			out.close();
		}
	}
		protected void doGet(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			processRequest(request, response);
			
		}
		protected void doPost(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			processRequest(request, response);
			
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

}
