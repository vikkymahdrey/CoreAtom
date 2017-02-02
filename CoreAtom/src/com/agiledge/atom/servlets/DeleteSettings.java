package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.SettingsService;


/**
 * Servlet implementation class DeleteSettings
 */
public class DeleteSettings extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteSettings() {
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
		int deleteid=Integer.parseInt(request.getParameter("deleteId"));
		HttpSession session = request.getSession();
		int result=new SettingsService().deletesetting(deleteid);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Setting Deleted Successfully</div");
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Setting Deletion Failure</div");
		}
		response.sendRedirect("viewSettings.jsp?property=All");
	}

}
