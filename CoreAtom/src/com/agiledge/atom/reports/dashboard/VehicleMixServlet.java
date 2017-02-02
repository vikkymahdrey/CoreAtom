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
 * Servlet implementation class VehicleMixServlet
 */
public class VehicleMixServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VehicleMixServlet() {
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
		VehicleMix obj=new VehicleMix();
	    Calendar cal =Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH),current=0,pre1=0,pre2=0,pre3=0,pre4=0,pre5=0;
	    int scurrent=0,spre1=0,spre2=0,spre3=0,spre4=0,spre5=0;
	    int year=cal.get(Calendar.YEAR);
	    if(month<=4)
	    {
	    	if(month==4)
	    	{
	    		pre1=obj.getVehicleMix(month, year,1);
	    		current=obj.getVehicleMix(month+1, year,1);
	    		pre2=obj.getVehicleMix(3, year,1);
	    		pre3=obj.getVehicleMix(2, year,1);
	    		pre4=obj.getVehicleMix(1, year,1);
	    		pre5=obj.getVehicleMix(12, year-1,1);
	    		spre1=obj.getVehicleMix(month, year,2);
	    		scurrent=obj.getVehicleMix(month+1, year,2);
	    		spre2=obj.getVehicleMix(3, year,2);
	    		spre3=obj.getVehicleMix(2, year,2);
	    		spre4=obj.getVehicleMix(1, year,2);
	    		spre5=obj.getVehicleMix(12, year-1,2);
	    		
	    	}else if(month==3)
	    	{
	    		pre1=obj.getVehicleMix(month, year,1);
	    		current=obj.getVehicleMix(month+1, year,1);
	    		pre2=obj.getVehicleMix(2, year,1);
	    		pre3=obj.getVehicleMix(1, year,1);
	    		pre4=obj.getVehicleMix(12, year-1,1);
	    		pre5=obj.getVehicleMix(11, year-1,1);
	    		spre1=obj.getVehicleMix(month, year,2);
	    		scurrent=obj.getVehicleMix(month+1, year,2);
	    		spre2=obj.getVehicleMix(2, year,2);
	    		spre3=obj.getVehicleMix(1, year,2);
	    		spre4=obj.getVehicleMix(12, year-1,2);
	    		spre5=obj.getVehicleMix(11, year-1,2);
	    		
	    	}else if(month==2)
	    	{
	    		pre1=obj.getVehicleMix(month, year,1);
	    		current=obj.getVehicleMix(month+1, year,1);
	    		pre2=obj.getVehicleMix(1, year,1);
	    		pre3=obj.getVehicleMix(12, year-1,1);
	    		pre4=obj.getVehicleMix(11, year-1,1);
	    		pre5=obj.getVehicleMix(10, year-1,1);
	    		spre1=obj.getVehicleMix(month, year,2);
	    		scurrent=obj.getVehicleMix(month+1, year,2);
	    		spre2=obj.getVehicleMix(1, year,2);
	    		spre3=obj.getVehicleMix(12, year-1,2);
	    		spre4=obj.getVehicleMix(11, year-1,2);
	    		spre5=obj.getVehicleMix(10, year-1,2);
	 
	    	}else if(month==1)
	    	{
	    		pre1=obj.getVehicleMix(month, year,1);
	    		current=obj.getVehicleMix(month+1, year,1);
	    		pre2=obj.getVehicleMix(12, year-1,1);
	    		pre3=obj.getVehicleMix(11, year-1,1);
	    		pre4=obj.getVehicleMix(10, year-1,1);
	    		pre5=obj.getVehicleMix(9, year-1,1);
	    		spre1=obj.getVehicleMix(month, year,2);
	    		scurrent=obj.getVehicleMix(month+1, year,2);
	    		spre2=obj.getVehicleMix(12, year-1,2);
	    		spre3=obj.getVehicleMix(11, year-1,2);
	    		spre4=obj.getVehicleMix(10, year-1,2);
	    		spre5=obj.getVehicleMix(9, year-1,2);
	    	}else if(month==0)
	    	{
	    		pre1=obj.getVehicleMix(12, year-1,1);
	    		current=obj.getVehicleMix(month+1, year,1);
	    		pre2=obj.getVehicleMix(11, year-1,1);
	    		pre3=obj.getVehicleMix(10, year-1,1);
	    		pre4=obj.getVehicleMix(9, year-1,1);
	    		pre5=obj.getVehicleMix(8, year-1,1);
	    		spre1=obj.getVehicleMix(12, year-1,2);
	    		scurrent=obj.getVehicleMix(month+1, year,2);
	    		spre2=obj.getVehicleMix(11, year-1,2);
	    		spre3=obj.getVehicleMix(10, year-1,2);
	    		spre4=obj.getVehicleMix(9, year-1,2);
	    		spre5=obj.getVehicleMix(8, year-1,2);
	    	}
	    }
	    else
	    {
	    	pre1=obj.getVehicleMix(month, year,1);
    		current=obj.getVehicleMix(month+1, year,1);
    		pre2=obj.getVehicleMix(month-1, year,1);
    		pre3=obj.getVehicleMix(month-2, year,1);
    		pre4=obj.getVehicleMix(month-3, year,1);
    		pre5=obj.getVehicleMix(month-4, year,1);
    		spre1=obj.getVehicleMix(month, year,2);
    		scurrent=obj.getVehicleMix(month+1, year,2);
    		spre2=obj.getVehicleMix(month-1, year,2);
    		spre3=obj.getVehicleMix(month-2, year,2);
    		spre4=obj.getVehicleMix(month-3, year,2);
    		spre5=obj.getVehicleMix(month-4, year,2);
	    }
		JSONObject data = new JSONObject();
		data.put("current", current);
		data.put("pre1", pre1);
		data.put("pre2", pre2);
		data.put("pre3", pre3);
		data.put("pre4", pre4);
		data.put("pre5", pre5);
		data.put("scurrent", scurrent);
		data.put("spre1", spre1);
		data.put("spre2", spre2);
		data.put("spre3", spre3);
		data.put("spre4", spre4);
		data.put("spre5", spre5);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	}
	}

