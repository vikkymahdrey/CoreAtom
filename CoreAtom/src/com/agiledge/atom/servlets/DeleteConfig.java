package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.GeneralShiftService;

/**
 * Servlet implementation class DeleteConfig
 */
public class DeleteConfig extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteConfig() {
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
		HttpSession session = request.getSession(true);
		String deleteId = request.getParameter("deleteid");
		int result=new GeneralShiftService().deleteConfig(deleteId);
		System.out.println("here");
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Configuration Deleted Successfully</div");
		} else {
			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		}
		response.sendRedirect("viewallconfig.jsp");
	}

}
