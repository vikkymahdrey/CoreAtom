package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.StsService;

public class StsRegister extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		HttpSession session = req.getSession();
		String fname=req.getParameter("fname");
		String lname= req.getParameter("lname");
		String personnelno =req.getParameter("personnelno");
		String gender= req.getParameter("gender");
		String mob = req.getParameter("mob");
		String email= req.getParameter("email");
		String pwd = req.getParameter("pwd");
		EmployeeDto dto = new EmployeeDto();
		dto.setEmployeeFirstName(fname);
		dto.setEmployeeLastName(lname);
		dto.setPersonnelNo(personnelno);
		dto.setGender(gender);
		dto.setContactNo(mob);
		dto.setEmailAddress(email);
		dto.setPassword(pwd);
		int status=new StsService().addStsEmployee(dto);
		if (status > 0) {
			session.setAttribute("status",
					"<div class='success'>Registered Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Failed</div");
		}
		resp.sendRedirect("index.jsp");
	}

}
