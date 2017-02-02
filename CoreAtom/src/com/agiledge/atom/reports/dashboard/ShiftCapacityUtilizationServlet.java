package com.agiledge.atom.reports.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class ShiftCapacityUtilizationServlet
 */
public class ShiftCapacityUtilizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShiftCapacityUtilizationServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		ShiftCapacityUtilization obj=new ShiftCapacityUtilization();
	    Calendar cal =Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH);
	    int year = cal.get(Calendar.YEAR),j=1;
	    String title="";
        ArrayList<String> shifts = new ArrayList();
	    ArrayList<String> actual = new ArrayList();
	    ArrayList<String> planned = new ArrayList();
	    shifts=obj.getShift(month, year);
	    actual=obj.getActual(month, year);
	    planned=obj.getPlanned(month, year);
	    JSONObject data = new JSONObject();
	    for (int i = 0; i < shifts.size(); i++) 
	    {
	    	title="shift"+j;
	    	data.put(title, shifts.get(i));
	    	j++;
	    }
	    j=1;
	    for ( int i = 0; i < actual.size(); i++) 
	    {
	    	title="actual"+j;
	    	data.put(title,Integer.parseInt(actual.get(i)));
	    	j++;
	    }
	    j=1;
	    for ( int i = 0; i < planned.size(); i++) 
	    {
	    	title="planned"+j;
	    	data.put(title,Integer.parseInt(planned.get(i)));
	    	j++;
	    }
	    data.put("cu",Integer.parseInt(planned.get(1)));
	    response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	    
	}

}
