package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import com.agiledge.atom.service.SettingsService;

/**
 * Servlet implementation class CheckData
 */

public class CheckData extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public CheckData() {
        super();
        // TODO Auto-generated constructor stub
    }
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
    String fromDate=request.getParameter("fromDate");
    String toDate=request.getParameter("toDate");
    System.out.println("projectid"+request.getParameter("projectid"));
    System.out.println("siteid"+request.getParameter("site"));
    int projectid=Integer.parseInt(request.getParameter("projectid"));
    int siteid=Integer.parseInt(request.getParameter("site"));
    String result="";
    boolean Dateflag=new SettingsService().checkDate(fromDate, toDate,projectid,siteid);
    if(Dateflag==true)
    {
    	result="true";
    }
    else
    {
    	result="false";
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
