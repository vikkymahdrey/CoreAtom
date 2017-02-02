package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.EmployeeService;


public class EditProject extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditProject() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request,response);
	}
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		try{
			String empid=request.getParameter("eid");
			String project=request.getParameter("project");
			System.out.println("her"+empid+project);
			HttpSession session=request.getSession();
			int returnint=new EmployeeService().UpdateProject(empid, project);
			String result="";
			if(returnint==1)
			{
				result="Updated Successfully";
				session.setAttribute("status",
						"<div class='success'>Project Data Updated Successfully</div");
			}
			else
			{
				result="Updation Failed";
				session.setAttribute("status",
						"<div class='failure'>Operation Failed</div");
			}
			String html="<html><script type=\"text/javascript\"> function doaction() { window.close(); opener.location.reload();	} </script><link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\" /><body><center><h1>"+result+"</h1></center><div></div><center><input type=\"button\" class=\"formbutton\" onClick=\"doaction()\" value=\"Close\"/></center></body></html> ";
			PrintWriter out = response.getWriter();		
			out.write(html);
			out.flush();
		}catch(Exception e)
		{
			e.printStackTrace();
		}
}
}
