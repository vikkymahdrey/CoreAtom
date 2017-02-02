package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.SpocService;

/**
 * Servlet implementation class RemoveEmployee
 */
public class RemoveEmployee extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public RemoveEmployee() {
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
		HttpSession session=request.getSession();
		String id=request.getParameter("empid");
		String spoc_name=request.getParameter("namespoc");
		String spocid=request.getParameter("idspoc");
		String url="viewspocemp.jsp?spoc_id="+spocid+"&spoc_name="+spoc_name;
		int result=new SpocService().removeEmployee(id);
		if(result==1)
		{
			session.setAttribute("status",
					"<div class='success'>Removed Successfully</div");
		}
		else
		{
			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		}
		response.sendRedirect(url);
		
	}

}
