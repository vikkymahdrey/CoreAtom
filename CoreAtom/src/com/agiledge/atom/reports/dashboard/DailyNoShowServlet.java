package com.agiledge.atom.reports.dashboard;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

/**
 * Servlet implementation class DailyNoShowServlet
 */
public class DailyNoShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DailyNoShowServlet() {
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
		DailyNoShow obj=new DailyNoShow();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal =Calendar.getInstance();
	    int values[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0};
	    int j=2;
	    cal=Calendar.getInstance();
	    values[1]=obj.getNoShow(dateFormat.format(cal.getTime()).toString());
	    cal=Calendar.getInstance();
	    for (int i=1;i<=14;i++)
	    {
		cal=Calendar.getInstance();
	    cal.add(Calendar.DATE,-(i));
	    values[j]=obj.getNoShow(dateFormat.format(cal.getTime()).toString());
	    }
	    JSONObject data = new JSONObject();
		data.put("Current",values[1]);
		data.put("pre1",values[2]);
		data.put("pre2", values[3]);
		data.put("pre3", values[4]);
		data.put("pre4", values[5]);
		data.put("pre5", values[6]);
		data.put("pre6", values[7]);
		data.put("pre7", values[8]);
		data.put("pre8", values[9]);
		data.put("pre9", values[10]);
		data.put("pre10", values[11]);
		data.put("pre11", values[12]);
		data.put("pre12", values[13]);
		data.put("pre13", values[14]);
		data.put("pre14", values[15]);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	    
	}

}
