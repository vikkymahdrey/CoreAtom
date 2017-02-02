package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.service.TripDetailsService;

/**
 * Servlet implementation class TripWiseDetails
 */

public class TripWiseDetails extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public TripWiseDetails() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String tripId = request.getParameter("tripId");
		String projectId = request.getParameter("projectId");
		PrintWriter out=response.getWriter();
		if(tripId!=null)
		{
			ArrayList<TripDetailsChildDto> childDtos = new TripDetailsService().liveTripStatusByEmps(tripId,projectId);
			String retString="";
			for(TripDetailsChildDto childDto:childDtos)
			{
			retString+="<tr><td>"+childDto.getEmployeeName()+"</td>";
			retString+="<td>"+childDto.getArea()+" "+childDto.getPlace()+" "+childDto.getLandmark()+"</td>";
			retString+="<td>"+childDto.getShowStatus()+"</td>";
			retString+="<td>"+childDto.getStatus()+"</td></tr>";
			}
			out.write(retString);
		}
		else
		{	
		String tripDate = request.getParameter("tripDate");
		String tripLog = request.getParameter("tripLog");
		String tripTime = request.getParameter("tripTime");
		ArrayList<TripDetailsDto> dtos = new TripDetailsService()
				.liveTripStatusByTrips(tripDate, tripLog, tripTime,projectId);
		
		String retString="";
		try
		{
		for(TripDetailsDto detailsDto: dtos)
		{
			retString+="<tr><td><a href='#' onclick=\"showTripEmp('"+detailsDto.getId()+"')\">"+detailsDto.getTrip_code()+"</td>";
			retString+="<td>"+detailsDto.getTrip_date()+"</td>";
			retString+="<td>"+detailsDto.getTrip_log();
			retString+=detailsDto.getTrip_time()+"</td>";
			retString+="<td>"+detailsDto.getEmpInCount()+"</td>" ;
			retString+="<td>"+detailsDto.getReachedempCount()+"</td>";			
			//retString+="<td>"+detailsDto.getDistance()+"</td>";
			retString+="<td>"+detailsDto.getTravelTime()+"</td>";
			retString+="<td>"+detailsDto.getExpectedArrivalTime()+"</td></tr>";
			
		}
		}catch(Exception e)
		{
			System.out.println("ERRor"+e);
		}
		out.write(retString);
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
