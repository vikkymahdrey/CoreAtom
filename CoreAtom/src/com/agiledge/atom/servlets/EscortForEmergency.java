package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.escort.service.EscortService;


public class EscortForEmergency extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EscortForEmergency() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	String siteId = request.getParameter("siteid");
	EscortService esc=new EscortService();
	String name = "";
	ArrayList<EscortDto> escorts =esc.getAllEscorts(siteId);
	for (EscortDto escortDto : escorts) {
		name=name+"<option value='" + escortDto.getId()  + "'>" + escortDto.getName()+ "</option>#";
	}

	PrintWriter out = response.getWriter();
	out.print(name);
	
	}

}
