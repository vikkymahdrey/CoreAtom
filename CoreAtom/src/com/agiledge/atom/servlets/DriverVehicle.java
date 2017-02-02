package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.DriverVehicleDto;
import com.agiledge.atom.service.VehicleService;

/**
 * Servlet implementation class DriverVehicle
 */
public class DriverVehicle extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public DriverVehicle() {
		super();
		// TODO Auto-generated constructor stub
	}

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
			HttpServletResponse response) throws ServletException, IOException {
		String vehicle = request.getParameter("vehicle");
		String submitAction = request.getParameter("source");
		HttpSession session=request.getSession();
		if(submitAction==null)
		{		
		VehicleService service=new VehicleService();
		ArrayList<DriverVehicleDto> dtos = service.getVehicleDetails(vehicle);
		ArrayList<DriverDto> drivers=service.getDrivers(vehicle);
		String returnString="<table border='1'><th>DriverName</th><th>Address</th><th>Contact</th><th>Veh#</th><th>Vehicle type</th><th>Vendor</th></tr>";
		String returnStringSecondPart="";
		PrintWriter out = response.getWriter();
		if(dtos!=null && dtos.size()>0)
		{
		for(DriverVehicleDto dto:dtos)
		{
			returnString+="<tr><td>"+dto.getDriverDto().getDriverName()+"</td>";
			returnString+="<td>"+dto.getDriverDto().getAddress1()+"</td>";
			returnString+="<td>"+dto.getDriverDto().getContactNo()+"</td>";
			returnString+="<td>"+dto.getVehicleDto().getVehicleNo()+"</td>";
			returnString+="<td>"+dto.getVehicleDto().getVehicleType()+"</td>";
			returnString+="<td>"+dto.getDriverDto().getVendor()+"</td></tr>";
			returnStringSecondPart+="<option value='"+dto.getDriverDto().getDriverId()+"'>"+dto.getDriverDto().getDriverName()+" : "+dto.getDriverDto().getVendor()+"</option>";
		}
		
		

	}
		else
		{
			returnString+="<tr><td>No driver Associated with the vehicle</td></tr>";	
		}
		returnStringSecondPart+=" | ";
		for(DriverDto dto: drivers)
		returnStringSecondPart+=" <option value='"+dto.getDriverId()+"'>"+dto.getDriverName()+" : "+dto.getVendor()+"</option>";	
		returnString+="</table>"+" | "+returnStringSecondPart;
		System.out.println("Current Details |"+returnString);
		out.write("Current Details |"+returnString);
		
		}else
		{
		System.out.println("In else");	
			String vehicleDrivers[]=request.getParameterValues("vehicleDrivers");
			int retVal=new VehicleService().addDriverVehicle(vehicle,vehicleDrivers);
			if(retVal>0)
			{
				session.setAttribute("status", "Driver Vehicle Association Successful");	
			}
			else
			{
				session.setAttribute("status", "Driver Vehicle Association Failure");	
			}
		response.sendRedirect("driver_vehicle.jsp");	
			
		}
		}
	

}
