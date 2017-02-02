/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.ModifyTripDao;
import com.agiledge.atom.dto.ModifyTripDto;
import com.agiledge.atom.service.ModifyTripService;


/**
 * 
 * @author muhammad
 */
public class UpdateTrip extends HttpServlet {

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
			HttpServletResponse response) throws ServletException, IOException,
			SQLException {
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		String tripDate = request.getParameter("tripDate");
		String siteId = request.getParameter("siteId");
		String tripMode = "" + request.getParameter("tripMode");
		String tripTime = "" + request.getParameter("tripTime");
		String tripIds[] = request.getParameterValues("tripIds");
		try {
			ModifyTripDto modifyTripDto = null;
			ArrayList<ModifyTripDto> modifyTripDtos = null;
			ModifyTripService modifyTrip = new ModifyTripService();
			LinkedHashMap<String, ArrayList<ModifyTripDto>> modifiedList = new LinkedHashMap<String, ArrayList<ModifyTripDto>>();
			int status=0;
			String vehicleType;
			String escort ;
			for(int i=0;i<4;i++)
			{							

			String tripEmps[] = request.getParameterValues("trip"+i);
			if(tripEmps!=null&&tripEmps.length>0)
			{		
			vehicleType = request.getParameter("vehicleType"+i).split(":")[0];
			escort = request.getParameter("escort"+i);			
															
			modifyTripDtos = new ArrayList<ModifyTripDto>();
			for (String empId : tripEmps) {
			String empidlandmark[] = empId.split(":");
			modifyTripDto = new ModifyTripDto();
			modifyTripDto.setEmployeId(empidlandmark[0]);
			modifyTripDto.setLandmarkId(empidlandmark[1]);
			modifyTripDto.setScheduleId(empidlandmark[2]);
			modifyTripDto.setTripStatus("modified");
			modifyTripDto.setVehicleType(vehicleType);
			modifyTripDto.setEscort(escort);
			modifyTripDtos.add(modifyTripDto);
			if(i<tripIds.length)
			{
				
				modifiedList.put(tripIds[i], modifyTripDtos);
				
			}
			else
			{			
				modifiedList.put("ADD"+i, modifyTripDtos);
			}
				}					
			}
			}
			for(Iterator it=modifiedList.entrySet().iterator();it.hasNext();)
			{
			Entry entry=(Entry) it.next();
			System.out.println(entry.getKey());
			ArrayList<ModifyTripDto> emplist=(ArrayList<ModifyTripDto>) entry.getValue();
			for(ModifyTripDto dto:emplist)
			{
			System.out.println("EMPID "+dto.getEmployeId());
			System.out.println("LAndmArkId "+dto.getLandmarkId());
			System.out.println("ScheduleId "+dto.getScheduleId());
			System.out.println("Status "+dto.getTripStatus());
			}
			
			}
			for(String tripid: tripIds)
			{
				System.out.println(tripid);
			}
			
			status = modifyTrip.modifyTrip(modifiedList, tripIds,doneBy,tripMode,siteId,tripTime,tripDate);
				
			
			String removedEmpids[] = request.getParameterValues("empids");
			System.out.println(removedEmpids);
			ArrayList<ModifyTripDto> addList = null;
			if (removedEmpids != null) {
				addList=new ArrayList<ModifyTripDto>();
				for (String removedEmpId : removedEmpids) {
					// System.out.println("Removed Employee" + removedEmpId);
					String empidlandmark[] = removedEmpId.split(":");
					modifyTripDto = new ModifyTripDto();
					modifyTripDto.setTripId(tripIds[0]);
					modifyTripDto.setEmployeId(empidlandmark[0]);
					modifyTripDto.setLandmarkId(empidlandmark[1]);
					modifyTripDto.setScheduleId(empidlandmark[2]);
					modifyTripDto.setTripStatus("modified");
					addList.add(modifyTripDto);
				}
				
					status=modifyTrip.addNewtrip(addList,doneBy,tripMode,siteId,tripTime,tripDate);				
			}			
			if (status >= 0) {
				request.getSession().setAttribute("status",
						"<div class='success'>Tripsheet Modififed</div");
			} else {
				request.getSession().setAttribute("status",
						"<div class='failure'>Tripsheet Not Modififed</div");
			}

		} catch (Exception e) {
			System.out.println("ERROR" + e);
			request.getSession().setAttribute("status",
					"<div class='failure'>Tripsheet Not Modififed</div");
		}
		response.sendRedirect("trip_details_modify.jsp?siteId=" + siteId
				+ "&tripDate=" + tripDate + "&tripTime=" + tripTime
				+ "&tripMode=" + tripMode + "");
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
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(UpdateTrip.class.getName()).log(Level.SEVERE,
					null, ex);
		}
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
		try {
			processRequest(request, response);
		} catch (SQLException ex) {
			Logger.getLogger(UpdateTrip.class.getName()).log(Level.SEVERE,
					null, ex);
		}
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
