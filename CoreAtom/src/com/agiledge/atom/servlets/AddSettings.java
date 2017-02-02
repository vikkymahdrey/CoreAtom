package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.SettingsDTO;


/**
 * Servlet implementation class AddSettings
 */

public class AddSettings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddSettings() {
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
		HttpSession session=request.getSession();
		String property=request.getParameter("property");
		String module = request.getParameter("module");
		String effectivedate=request.getParameter("fromDate");
		String todate=request.getParameter("toDate");
		String value=request.getParameter("amount");
		//int siteid=Integer.parseInt(request.getParameter("siteId"));
		String site=request.getParameter("siteId");
		System.out.println(request.getParameter("condition"));
		int project;
		if(request.getParameter("condition").equalsIgnoreCase("all")){
			project=0;
		}else
		{
			project=Integer.parseInt(request.getParameter("project"));
		}
		SettingsDTO dto=new SettingsDTO();
		dto.setProperty(property);
		Calendar calFrom = Calendar.getInstance();
		String fromDatePart [] = effectivedate.split("/");
		calFrom.set(Calendar.DATE, Integer.parseInt(fromDatePart[0]));
		calFrom.set(Calendar.MONTH, Integer.parseInt(fromDatePart[1]));
		calFrom.set(Calendar.YEAR, Integer.parseInt(fromDatePart[2]));
		Calendar calTo = Calendar.getInstance();
		String toDatePart [] = todate.split("/");
		calTo.set(Calendar.DATE, Integer.parseInt(toDatePart[0]));
		calTo.set(Calendar.MONTH, Integer.parseInt(toDatePart[1]));
		calTo.set(Calendar.YEAR, Integer.parseInt(toDatePart[2]));
		dto.setEffectivedate( calFrom.getTime());
		dto.setTodate(calTo.getTime());
		dto.setValue(value);
		//dto.setSiteid(siteid);
		dto.setSite(site);
		dto.setProjectid(project);
		dto.setModule(module);
		int result= new SettingsDoa().addSettings(dto);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Data Added Successfully</div");
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		}
		response.sendRedirect("payroll_setup.jsp");
		
				}

}
