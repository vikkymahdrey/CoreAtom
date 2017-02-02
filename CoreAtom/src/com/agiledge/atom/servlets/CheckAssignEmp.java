package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.agiledge.atom.service.SpocService;

/**
 * Servlet implementation class CheckAssignEmp
 */
public class CheckAssignEmp extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckAssignEmp() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	Long empid=Long.parseLong(request.getParameter("empid"));
    	String result="false";
    	int intresult=new SpocService().checkAssignEmployee(empid);
    	 if(intresult==1)
         {
         	result="true";
         }
         JSONObject data = new JSONObject();
         data.put("result", result);
         response.setContentType("application/json");
     	PrintWriter out = response.getWriter();
     	out.write(data.toString());
     	out.flush();
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
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
