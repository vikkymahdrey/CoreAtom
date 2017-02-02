package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.ProjectService;


/**
 * Servlet implementation class AddProjectToTime
 */
 
public class AddProjectToTime extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */


    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
    	String DoneBy = session.getAttribute("user").toString();
    	String path = "logtime_list.jsp" ;
		
    		try{
    			
    			String project=request.getParameter("project");
    			String projectdesc=request.getParameter("projectdesc");
    			String timeId=request.getParameter("id");
    		//	path=request.getServletPath().replace("/", "");
    		//	path=request.getHeader("referer");
    		//	path=path.substring(path.lastIndexOf("/")+1);
    			path="log_time_modify.jsp?id=" + timeId;
    			System.out.println("...." + path);
    			
    			int value=new ProjectService().mapTimeAndProject(project,timeId,DoneBy);
    			//int value = 0;
    			if (value > 0)
					request.getSession().setAttribute("status",
							"<div class=\"success\" width=\"100%\" > Shift time assigned for the project " + projectdesc + " </div>");
				else{
					request.getSession().setAttribute("status",
							"<div class=\"failure\" > Shift time assignment failure !</div>");
				}
    			
    		}catch(Exception e)
    		{
    			;
    		}
    		
    		response.sendRedirect(path);
    
    }
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
		
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		processRequest(request, response);
		
	}

}
