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
 * Servlet implementation class GetLandmark
 */
public class GetLandmarks extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("Here inside GetLandmarks");
		if (request.getParameter("placeforLandmark") != null) {
			int placeId = Integer.parseInt(request.getParameter("placeforLandmark"));
			PrintWriter out = response.getWriter();
			String retText = "";

			try {
				ArrayList<APLDto> landmarkDtos = new APLService()
						.getLandmarksByPlaceId(placeId);
				retText = "<select name='landmarkId' id='landmarkId'>";
			
				for (APLDto landmarkDto : landmarkDtos) {
					retText += "<option value='" + landmarkDto.getLandMarkID()
							+ "' >" + landmarkDto.getLandMark() + "</option>";
				}
				retText += "</select>";
				out.write(retText);

			} finally {
				out.close();
			}
		} else {
			PrintWriter out = response.getWriter();
			String retText = "";
			String place=request.getParameter("place");
			String area=request.getParameter("area");
			String location=request.getParameter("location");
			String LAP="";
			try {
				if(place!=null)
				{
					LAP="  place.id="+place;	
				}
				else if(area!=null)
				{
					LAP="  area.id="+area;
				}
				else if(location!=null)
				{
					LAP="  area.location="+location;
				}
				ArrayList<APLDto> landmarkDtos = new APLService().getAllAPLForSpecific(LAP);

				retText = "";
				for (APLDto landmarkDto : landmarkDtos) {
					retText = retText + "$" + landmarkDto.getArea() + ":"
							+ landmarkDto.getPlace() + ":"
							+ landmarkDto.getLandMarkID() + ":"
							+ landmarkDto.getLandMark() + ":"
							+ landmarkDto.getLattitude() + ":"
							+ landmarkDto.getLongitude();
				}
			//	System.out.println("APLS"+retText);
				out.write(retText);
			}
				catch(Exception e)
				{
				System.out.println("Error"+e);													
			} finally {
				out.close();
			}

		}

	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
