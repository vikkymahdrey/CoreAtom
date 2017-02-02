/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dto.RoutingDto;
import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.service.RoutingService;
import com.agiledge.atom.service.TripDetailsService;
import com.agiledge.atom.service.VehicleService;

/**
 * 
 * @author muhammad
 */
public class Routing extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();			
			HttpSession session1 = request.getSession(true);
			 session1.setAttribute("routeStatus", "started"); 	 
			String doneBy = session1.getAttribute("user").toString();			 
			String check = request.getParameter("check");
			String siteIdstr = request.getParameter("siteId");
			int siteid = Integer.parseInt(siteIdstr);
			//System.out.println("siteId In routing serv" + check);
			String date = OtherFunctions.changeDateFromatToIso(request
					.getParameter("for_date"));
			request.getSession().setAttribute("date", request
					.getParameter("for_date"));
			String logTime = request.getParameter("logTime");
			String log = request.getParameter("log");
			String overwrite = request.getParameter("isOverwrite");
			RoutingDto routingDto = new RoutingDto();
			routingDto = new RoutingDto();
			routingDto.setDoneBy(doneBy);
			routingDto.setDate(date);
			routingDto.setTime(logTime);
			//System.out.println("logtime" + log);
			routingDto.setTravelMode(log);
			String status = "";
			String retDat = "Please wait for 10 min and go to view trip ";
			int statusForDisplyStyleofVehicleCount=0;
			if(OtherFunctions.isEmpty(doneBy)==false) {							 
					if (check == null  ) {
					//	System.out.println("Action");			
					//	System.out.println(request.getParameterValues("vehicleType"));
						String[] vehicleTypes = request
								.getParameterValues("vehicleType");						
						ArrayList<VehicleTypeDto> vehicleType = new ArrayList<VehicleTypeDto>();
						ArrayList<VehicleTypeDto> vehicleTypeGiven=null;
						VehicleTypeDto dto = null;
						try {
						String[] givenCounts = new String[vehicleTypes.length];
						boolean flag=false;
						//System.out.println("length          "+vehicleTypes.length);
						for (int i = 0; i < vehicleTypes.length; i++) {
							try
							{			 	
							//	Integer.parseInt(request.getParameter("count" + vehicleTypes[i]));	
							dto = new VehicleTypeDto();							
								givenCounts[i] = request.getParameter("count"
										+ vehicleTypes[i]);							 							
							dto.setId(Integer.parseInt(vehicleTypes[i]));
						//	System.out.println("given coutn " + 	givenCounts[i] );
							dto.setCount(Integer.parseInt(givenCounts[i]));					
							vehicleType.add(dto);
							flag=true;
							statusForDisplyStyleofVehicleCount=1;
							}
							catch(Exception ee)
							{
							System.out.println("some vehicle null error "+ee);	
							}							
						}
						if(!flag)
						{
							vehicleType=new VehicleService().setVehicleTypeCount(siteIdstr); 	
						}							
							if (overwrite != null) {
								vehicleTypeGiven=new ArrayList<VehicleTypeDto>(vehicleType);
								status = new RoutingService().routeProcess(routingDto,
										siteid, overwrite, doneBy, vehicleType);
							} else {
								vehicleTypeGiven=new ArrayList<VehicleTypeDto>(vehicleType);
						//		System.out.println("In Else");
								status = new RoutingService().routeProcess(routingDto,
										siteid, null, doneBy, vehicleType);
							}		
		//System.out.println("Status"+status);		
						//	 response.sendRedirect("view_routing.jsp");
							if (status.equals("success")) {
								retDat = "Routing Successful|";					
								retDat+= new TripDetailsService().routingSummaryHTMLTable(siteIdstr,date, log, logTime,vehicleTypeGiven,statusForDisplyStyleofVehicleCount);																											
							} else if (status.equals("noEmps")) {
								retDat = "No employee is scheduled or Allocation already Done";
								// response.sendRedirect("view_routing.jsp");
							}		
						} catch (Exception ex) {		
							retDat = " Routing failed !";
							// TODO Auto-generated catch block
							ex.printStackTrace();
							// response.sendRedirect("view_routing.jsp");
						}
						out.print(retDat);
						session1.setAttribute("routingData", retDat);
						// response.sendRedirect("automatic_routing.jsp");
						session1.setAttribute("routeStatus", "finished");
					}		
					else {		
						int rsv = new RoutingService().checkTripExist(routingDto,
								siteid);
						System.out.println("In ajax" + rsv);		
						if (rsv > 0) {
							out.print("true");
						} else {
							out.print("false");
						}
					}				 			
			} else {
				out.print("Session Expired. Please login...");
			}
	}
	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>
}
