package com.agiledge.atom.servlets.apl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ShuttleSocketDao;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.GeoTagDto;
import com.agiledge.atom.service.APLService;


/**
 * Servlet implementation class GetLandmark
 */
public class GetShuttleLandmarks extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		System.out.println("Here inside GetLandmarks");
	
		PrintWriter out = response.getWriter();
		String retText = "";
	try{		
				List<GeoTagDto> landmarkDtos = new ShuttleSocketDao().getemployeeLocationDetails();
				retText = "";
				
	
				String search=request.getParameter("search");
				
				if(search.equalsIgnoreCase("Home")){
					for (GeoTagDto landmarkDto : landmarkDtos) {
						retText = retText + "$" + landmarkDto.getEmpName()+":"
								+landmarkDto.getHomelat()+":"
								+ landmarkDto.getHomelong() + ":"
								+ landmarkDto.getAddress();
					}
				}
				else if (search.equalsIgnoreCase("Pickup")){
					for (GeoTagDto landmarkDto : landmarkDtos) {
						retText = retText + "$" + landmarkDto.getEmpName()+":"
								+ landmarkDto.getPicklat() + ":"
								+ landmarkDto.getPicklong()+ ":"
								+ landmarkDto.getAddress();
					}	
				}
				else if (search.equalsIgnoreCase("Drop")){
					for (GeoTagDto landmarkDto : landmarkDtos) {
						retText = retText + "$" + landmarkDto.getEmpName()+":"
								+ landmarkDto.getDroplat() + ":"
								+ landmarkDto.getDreoplong()+ ":"
								+ landmarkDto.getAddress();
					}
				}
				
				
				
				
			
				out.write(retText);
			}
				catch(Exception e)
				{
				System.out.println("Error"+e);													
			} finally {
				out.close();
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
