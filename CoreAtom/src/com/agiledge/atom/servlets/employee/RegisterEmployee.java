package com.agiledge.atom.servlets.employee;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;

/**
 * Servlet implementation class RegisterEmployee
 */
public class RegisterEmployee extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public RegisterEmployee() {
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
		
		HttpSession session = request.getSession();
		String loginId=request.getParameter("loginId");
		String employeeId=session.getAttribute("user").toString();
		/*
		ArrayList<SettingsDTO> transportType = new SettingsDoa()
		.getSettingsStrings(SettingsConstant.TRANSPORT,
				SettingsConstant.TYPE);
		ArrayList<TransportTypeDto> list=new ArrayList<TransportTypeDto>();
		String employeeId=session.getAttribute("user").toString();
		for(SettingsDTO dto:transportType)
		{
			
		if(request.getParameter(dto.getKeyValue())!=null)
		{
			TransportTypeDto tptDto=new TransportTypeDto();
			tptDto.setEmployeeId(employeeId);
			tptDto.setTransportType(request.getParameter(dto.getKeyValue()));
			tptDto.setLandmarkId(request.getParameter("landMarkID"+dto.getKeyValue()));
			list.add(tptDto);
		}
		}	
		
		int status=new EmployeeService().registerEmployee(list);
		*/
		int status=new EmployeeService().registerEmployee(employeeId);
		if(status>0)
		{
			session.setAttribute("status",
					"<div class=\"success\" width=\"100%\" > Registration successful</div>");
			response.sendRedirect("register_employee.jsp?loginId="+loginId);
		}
		else
		{
			session.setAttribute("status",
					"<div class=\"failure\" > Registration Failed</div>");
			response.sendRedirect("register_employee.jsp?loginId"+loginId);
			
		}
				

	}

}
