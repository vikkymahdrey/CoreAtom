package com.agiledge.atom.reports.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ProjectwiseNoShowServlet
 */
public class ProjectwiseNoShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ProjectwiseNoShowServlet() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ProjectwiseNoShow obj=new ProjectwiseNoShow();
	    Calendar cal =Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH),current=0,acurrent=0,bcurrent=0,ccurrent=0,dcurrent=0;
	    int year=cal.get(Calendar.YEAR);
	    if(month==12)
	    {
	    	current=obj.getProjectwise(1, year,1);
    		acurrent=obj.getProjectwise(1, year,2);
    		bcurrent=obj.getProjectwise(1, year,3);
    		ccurrent=obj.getProjectwise(1, year,4);
    		dcurrent=obj.getProjectwise(1, year,5);
	    }
	    else
	    {
	    	current=obj.getProjectwise(month+1, 2013,1);
    		acurrent=obj.getProjectwise(month+1, year,2);
    		bcurrent=obj.getProjectwise(month+1, year,3);
    		ccurrent=obj.getProjectwise(month+1, year,4);
    		dcurrent=obj.getProjectwise(month+1, year,5);
	    }
		JSONObject data = new JSONObject();
		data.put("current", current);
		data.put("acurrent", acurrent);
		data.put("bcurrent", bcurrent);
		data.put("ccurrent", ccurrent);
		data.put("dcurrent", dcurrent);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	}

}
