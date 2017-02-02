package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeService;

/**
 * Servlet implementation class EditEmployee
 */
public class EditEmployee extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	System.out.println("0");
	HttpSession session=request.getSession();
	String personnelNo=request.getParameter("personnelNo");
	String firstname=request.getParameter("fname");
	String middlename=request.getParameter("mname");
	String lastname=request.getParameter("lname");
	String displayname=request.getParameter("dname");
	String phno=request.getParameter("phno");
	String email=request.getParameter("eid");
	String address=request.getParameter("address");
	String gender=request.getParameter("gender");
	String usertype=request.getParameter("usertype");
	String authtype=request.getParameter("authtype");
	String project=request.getParameter("project");
	String lineManager=request.getParameter("supervisorID1");
	EmployeeDto dto=new EmployeeDto();
	dto.setPersonnelNo(personnelNo);
	dto.setEmployeeFirstName(firstname);
	dto.setEmployeeMiddleName(middlename);
	dto.setEmployeeLastName(lastname);
	dto.setDisplayName(displayname);
	dto.setContactNo(phno);
	dto.setEmailAddress(email);
	dto.setAddress(address);
	dto.setGender(gender);
	dto.setAuthtype(authtype);
	dto.setUserType(usertype);
	dto.setProject(project);
	dto.setLineManager(lineManager);
	
	int result=new EmployeeService().UpdateEmployee(dto);
	if (result==1) {
		session.setAttribute("status",
				"<div class='success'>Employee Data Updated Successfully</div");
	}
	else
	{

		session.setAttribute("status",
				"<div class='failure'>Operation Failed</div");
	}
	response.sendRedirect("viewallExternal.jsp");
}


	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
		
}
}
