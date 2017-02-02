package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.xmlbeans.impl.xb.xsdschema.Public;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.ShuttleSocketDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.service.EmployeeSubscriptionService;
import com.agiledge.atom.service.ShuttleSocketService;

public class ShuttleSubscribe extends HttpServlet {

	/**
	 *                                                                                                                                               
	 */
	private static final long serialVersionUID = 1L;

	// Only for Keonics
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		if(SettingsConstant.comp.equalsIgnoreCase("keo")){
			if(req.getParameter("interest")!=null){
				doNewKeo(req, resp);
			}else{
			doKeo(req, resp);
			}
		}else{
			dovisa(req, resp);
		}
				
			
		
	}
	protected void doKeo(HttpServletRequest req, HttpServletResponse resp){
		try {
		HttpSession session = req.getSession();
		EmployeeDto dto = new EmployeeDto();
		
			PrintWriter out = resp.getWriter();
		
		String home = req.getParameter("homelatlong");
		home = home.substring(1, home.length() - 1);
		String suburl="";
		String lat = home.split(",")[0];
		String ln = home.split(", ")[1];
		int flag=0;
		dto.setEmployeeID(req.getParameter("empid"));
		dto.setEmployeeFirstName(req.getParameter("fname"));
		dto.setEmployeeLastName(req.getParameter("lname"));
		dto.setDisplayName(req.getParameter("fname")+" "+req.getParameter("lname"));
		dto.setLogin(req.getParameter("inTime"));
		dto.setLogout(req.getParameter("outTime"));
		dto.setinroute(req.getParameter("inRoute"));
		dto.setOutroute(req.getParameter("outRoute"));
		dto.setPickup(req.getParameter("inlatlong"));
		dto.setGender(req.getParameter("gender"));
		dto.setAddress(req.getParameter("addr")); 
		dto.setCity(req.getParameter("city"));
		dto.setZip(req.getParameter("zip"));
		dto.setState(req.getParameter("state"));
		dto.setEmailAddress(req.getParameter("email"));
		dto.setContactNo(req.getParameter("mob"));
		dto.setPersonnelNo(req.getParameter("rollno"));
		dto.setLattitude(lat);
		dto.setLongitude(ln);
		if (req.getParameter("points").equalsIgnoreCase("same")) {
			dto.setDrop(req.getParameter("inlatlong"));
		} else {
			dto.setDrop(req.getParameter("outlatlong"));
		}
		if (!req.getParameter("samedrop").equalsIgnoreCase("ignorecase")) {
			new ShuttleSocketService().setShuttlePickUpDrop(dto);
		} else {
			new ShuttleSocketService().removeShuttlePickUpDrop(dto);
		}
		int res = new EmployeeSubscriptionService()
				.setShuttleEmpSubscriptionDetails(dto);
		if (res > 0) {
			/*String rid="",panel="",u="";
			if(session.getAttribute("responsecode")!=""){
				rid="&RId="+session.getAttribute("responsecode");
			}
			
			if(session.getAttribute("panel")!=""){
				panel="&P="+session.getAttribute("panel");
			}

			u=(String)session.getAttribute("U");
			if(u.equalsIgnoreCase("1")){
				resp.sendRedirect("https://intranet.tools.siemens.co.in/TravelSurvey/Pages/Everyday.aspx?F=1&U=1"+rid+panel);
			}else if( u.equalsIgnoreCase("0")){
				resp.sendRedirect("https://intranet.tools.siemens.co.in/TravelSurvey/Pages/Occasionally.aspx?F=1&U=2"+rid+panel);
			}else{
*/				session.setAttribute("status",
						"<div class=\"success\"  >Registration Successful </div>");

				resp.sendRedirect("employee_home.jsp");
			/*}*/
		} else {
			session.setAttribute("status",
					"<div class=\"failure\" >Registration failed !</div>");
			resp.sendRedirect("employee_home.jsp");
			
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
protected void dovisa(HttpServletRequest req, HttpServletResponse resp){
		
	try {
		HttpSession session = req.getSession();
		EmployeeDto dto = new EmployeeDto();
		
			PrintWriter out = resp.getWriter();
		
      String home = req.getParameter("homelatlong");
		home = home.substring(1, home.length() - 1);
		String lat = home.split(",")[0];
		String lng = home.split(", ")[1];
				dto.setLattitude(lat);
		dto.setLongitude(lng);               

		dto.setEmployeeID(req.getParameter("empid"));
		dto.setEmployeeFirstName(req.getParameter("fname"));
		dto.setEmployeeLastName(req.getParameter("lname"));
		dto.setDisplayName(req.getParameter("fname")+" "+req.getParameter("lname"));
		dto.setGender(req.getParameter("gender"));
		dto.setAddress(req.getParameter("addr")); 
		dto.setCity(req.getParameter("city"));
		dto.setZip(req.getParameter("zip"));
		dto.setState(req.getParameter("state"));
		dto.setEmailAddress(req.getParameter("email"));
		dto.setContactNo(req.getParameter("mob"));
		dto.setPersonnelNo(req.getParameter("rollno"));
		dto.setLineManager(req.getParameter("supervisorID1"));//Manager id
		dto.setProject(req.getParameter("project"));		
		dto.setProjectUnit(req.getParameter("projectunit"));
		/*
		 currently shift information is not captured
    	dto.setLogin(req.getParameter("inTime"));
		dto.setLogout(req.getParameter("outTime"));
		new ShuttleSocketService().setShuttlePickUpDrop(dto);*/
		
		

		int res = new EmployeeSubscriptionService().setEmpRegisterDetails(dto);

		System.out.println(res);
		if (res > 0) {
			session.setAttribute("status",
					"<div class=\"Success\" > Thank you for entering your details !</div>");
			resp.sendRedirect("employee_Register.jsp");
			
			
		} else {
			session.setAttribute("status",
					"<div class=\"failure\" > Failed to enter the details !</div>");
			resp.sendRedirect("employee_Register.jsp");
			
		}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
protected void doNewKeo(HttpServletRequest req, HttpServletResponse resp){
	try {
	HttpSession session = req.getSession();
	EmployeeDto dto = new EmployeeDto();
	
		PrintWriter out = resp.getWriter();
	
	String home = req.getParameter("homelatlong");
	home = home.substring(1, home.length() - 1);
	String suburl="";
	String lat = home.split(",")[0];
	String ln = home.split(", ")[1];
	int flag=0;
	dto.setEmployeeID(req.getParameter("empid"));
	dto.setEmployeeFirstName(req.getParameter("fname"));
	dto.setEmployeeLastName(req.getParameter("lname"));
	dto.setLogin(req.getParameter("inTime"));
	dto.setLogout(req.getParameter("outTime"));
	dto.setGender(req.getParameter("gender"));
	dto.setAddress(req.getParameter("addr")); 
	dto.setCity(req.getParameter("city"));
	dto.setZip(req.getParameter("zip"));
	dto.setState(req.getParameter("state"));
	dto.setEmailAddress(req.getParameter("email"));
	dto.setContactNo(req.getParameter("mob"));
	dto.setPersonnelNo(req.getParameter("rollno"));
	dto.setLattitude(lat);
	dto.setLongitude(ln);
	dto.setSite(req.getParameter("site"));
		new ShuttleSocketDao().setShuttlePickUpDrop1(dto,req.getParameter("interest"));
	
	int res = new EmployeeSubscriptionService()
			.setShuttleEmpSubscriptionDetails(dto);
	if (res > 0) {

				session.setAttribute("status",
					"<div class=\"success\"  >Registration Successful </div>");

			resp.sendRedirect("employee_home.jsp");
		
	} else {
		session.setAttribute("status",
				"<div class=\"failure\" >Registration failed !</div>");
		resp.sendRedirect("employee_home.jsp");
		
	}
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
}

}
