package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.OtherDao;
import com.agiledge.atom.dto.SiteDto;


/**
 * Servlet implementation class SetGetRolePage
 */
public class SetGetRolePage extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
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
		PrintWriter out = response.getWriter();
		String usertype = request.getParameter("usertype");
		String[] userpages = request.getParameterValues("selectedpage");
		String display = request.getParameter("display");
		
		OtherDao dao = OtherDao.getInstance();
		if(userpages==null)
		{
		String returnString = "";
		String returnString1 = "";
		
		ArrayList<SiteDto> pages = dao.selectPagesNoAccess(usertype);
		ArrayList<SiteDto> selectedpages = dao.selectPagesAccess(usertype);
		for (SiteDto page : pages) {
			returnString += "<option value=" + page.getId() + ">"
					+ page.getName() + "</option>";
		}
		for (SiteDto selectedpage : selectedpages) {
			returnString1 += "<option value=" + selectedpage.getId() + ">"
					+ selectedpage.getName() + "</option>";
		}
		System.out.println("User type" + usertype + "    " + returnString + "|"
				+ returnString1);
		out.write(returnString + "|" + returnString1);
		}
		else
		{
		dao.insertRolePages(usertype, userpages,display);
		response.sendRedirect("rolepage.jsp");	
		}

	}

}
