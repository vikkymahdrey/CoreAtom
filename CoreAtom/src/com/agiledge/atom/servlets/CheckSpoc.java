package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.service.SpocService;

/**
 * Servlet implementation class CheckSpoc
 */
public class CheckSpoc extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckSpoc() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    	String from_Date=OtherFunctions.changeDateFromatToIso(request.getParameter("from_date"));
    	String result="false";
        String to_Date=OtherFunctions.changeDateFromatToIso(request.getParameter("to_date"));
        Long empid=Long.parseLong(request.getParameter("empid"));
        int intresult=new SpocService().checkSpoc(empid,from_Date,to_Date);
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
