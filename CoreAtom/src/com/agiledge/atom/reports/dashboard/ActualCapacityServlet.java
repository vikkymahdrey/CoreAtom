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
 * Servlet implementation class ActualCapacityServlet
 */
public class ActualCapacityServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ActualCapacityServlet() {
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
		ActualCapacity obj=new ActualCapacity();
	    Calendar cal =Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH),current=0,pre1=0,pre2=0,pre3=0,pre4=0,pre5=0;
	    int year=cal.get(Calendar.YEAR);
	    if(month<=4)
	    {
	    	if(month==4)
	    	{
	    		pre1=obj.getActual(month, year);
	    		current=obj.getActual(month+1, year);
	    		pre2=obj.getActual(3, year);
	    		pre3=obj.getActual(2, year);
	    		pre4=obj.getActual(1, year);
	    		pre5=obj.getActual(12, year-1);
	    		
	    	}else if(month==3)
	    	{
	    		pre1=obj.getActual(month, year);
	    		current=obj.getActual(month+1, year);
	    		pre2=obj.getActual(2, year);
	    		pre3=obj.getActual(1, year);
	    		pre4=obj.getActual(12, year-1);
	    		pre5=obj.getActual(11, year-1);
	    	}else if(month==2)
	    	{
	    		pre1=obj.getActual(month, year);
	    		current=obj.getActual(month+1, year);
	    		pre2=obj.getActual(1, year);
	    		pre3=obj.getActual(12, year-1);
	    		pre4=obj.getActual(11, year-1);
	    		pre5=obj.getActual(10, year-1);
	    	}else if(month==1)
	    	{
	    		pre1=obj.getActual(month, year);
	    		current=obj.getActual(month+1, year);
	    		pre2=obj.getActual(12, year-1);
	    		pre3=obj.getActual(11, year-1);
	    		pre4=obj.getActual(10, year-1);
	    		pre5=obj.getActual(9, year-1);
	    	}else if(month==0)
	    	{
	    		pre1=obj.getActual(12, year-1);
	    		current=obj.getActual(month+1, year);
	    		pre2=obj.getActual(11, year-1);
	    		pre3=obj.getActual(10, year-1);
	    		pre4=obj.getActual(9, year-1);
	    		pre5=obj.getActual(8, year-1);
	    	}
	    }
	    else
	    {
	    	pre1=obj.getActual(month, year);
    		current=obj.getActual(month+1, year);
    		pre2=obj.getActual(month-1, year);
    		pre3=obj.getActual(month-2, year);
    		pre4=obj.getActual(month-3, year);
    		pre5=obj.getActual(month-4, year);
	    }
		JSONObject data = new JSONObject();
		data.put("Current", current);
		data.put("pre1", pre1);
		data.put("pre2", pre2);
		data.put("pre3", pre3);
		data.put("pre4", pre4);
		data.put("pre5", pre5);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	}

}
