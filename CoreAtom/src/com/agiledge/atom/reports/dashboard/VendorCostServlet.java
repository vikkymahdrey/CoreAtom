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
 * Servlet implementation class VendorCostServlet
 */
public class VendorCostServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public VendorCostServlet() {
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
		VendorCost obj=new VendorCost();
	    Calendar cal =Calendar.getInstance();
	    int month = cal.get(Calendar.MONTH),current=0,pre1=0,pre2=0;
	    int year=cal.get(Calendar.YEAR);
	   if(month<=1)
	    {
	    	if(month==1)
	    	{
	    		pre1=obj.getVendorCost(month, year);
	    		current=obj.getVendorCost(month+1, year);
	    		pre2=obj.getVendorCost(12, year-1);
	    		
	    	}else if(month==0)
	    	{
	    		pre1=obj.getVendorCost(12, year-1);
	    		current=obj.getVendorCost(month+1, year);
	    		pre2=obj.getVendorCost(11, year-1);
	    	
	    }
	    }
	    else
	    {
	    	pre1=obj.getVendorCost(month, year);
    		current=obj.getVendorCost(month+1, year);
    		pre2=obj.getVendorCost(month-1, year);
	    }
		JSONObject data = new JSONObject();
		data.put("Current", current);
		data.put("pre1", pre1);
		data.put("pre2", pre2);
		
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	}

	}

