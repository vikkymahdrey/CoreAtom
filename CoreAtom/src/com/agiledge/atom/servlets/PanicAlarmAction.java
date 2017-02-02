package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.PanicDao;
import com.agiledge.atom.dto.PanicDto;


/**
 * Servlet implementation class PanicAlarmAction
 */

public class PanicAlarmAction extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public PanicAlarmAction() {
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
	 * @throws IOException 
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession();
		String takenBy = session.getAttribute("user").toString();
		String tripId = request.getParameter("tripId");
		try{
		if(request.getParameter("source")!=null)
		{
			int status = new PanicDao().StopSound(tripId,takenBy);	
		}
		else if(request.getParameter("stop")==null)
		{		
		String causeOfalarm = request.getParameter("causeOfalarm");
		String actionDesc = request.getParameter("actionDesc");
		PanicDto panicDto = new PanicDto();
		panicDto.setAlarmCause(causeOfalarm);
		panicDto.setTripId(tripId);
		panicDto.setPrimaryActiontakenBy(takenBy);
		panicDto.setPrimaryAction(actionDesc);
		int status = new PanicDao().TakePanicAction(panicDto);
		if (status >= 0) {
			session.setAttribute("status",
					"<div class='success'>Primary Action Taken</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Primary Action Taken Fialed</div");
		}
		response.sendRedirect("panicAlarmTrip.jsp?tripId=" + tripId);
		}
		else
		{
			
			String panicId = request.getParameter("panicid");
			System.out.println("IN Action"+panicId);			
			int status = new PanicDao().PanicStopAction(panicId,tripId,takenBy);			
			if (status >= 1) {				
				session.setAttribute("status",
						"<div class='success'>Panic Stopped</div");
			} else {
				session.setAttribute("status",
						"<div class='failure'>Panic Stop Filed</div");
			}
			response.sendRedirect("trackVehicle.jsp");		
		}
		}catch(Exception e)
		{
		System.out.println("error in stop action servlet "+e);	
		response.sendRedirect("trackVehicle.jsp");
		}
					
	}
	}

