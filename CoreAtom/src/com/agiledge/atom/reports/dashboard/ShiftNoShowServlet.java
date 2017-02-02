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
 * Servlet implementation class ShiftNoShowServlet
 */
public class ShiftNoShowServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShiftNoShowServlet() {
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
		ShiftNoShow obj=new ShiftNoShow();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal =Calendar.getInstance();
	    int shift[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},shift2[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},j=2;
	    shift[1]=obj.getNoShow(dateFormat.format(cal.getTime()).toString(),1);
	    shift2[1]=obj.getNoShow(dateFormat.format(cal.getTime()).toString(),2);
	    for (int i=1;i<=14;i++)
	    {
		cal=Calendar.getInstance();
	    cal.add(Calendar.DATE,-(i));
	    shift[j]=obj.getNoShow(dateFormat.format(cal.getTime()).toString(),1);
	    shift2[j]=obj.getNoShow(dateFormat.format(cal.getTime()).toString(),2);
	    j++;
	    }
	    JSONObject data = new JSONObject();
		data.put("Current",shift[1]);
		data.put("pre1",shift[2]);
		data.put("pre2", shift[3]);
		data.put("pre3", shift[4]);
		data.put("pre4", shift[5]);
		data.put("pre5", shift[6]);
		data.put("pre6", shift[7]);
		data.put("pre7", shift[8]);
		data.put("pre8", shift[9]);
		data.put("pre9", shift[10]);
		data.put("pre10", shift[11]);
		data.put("pre11", shift[12]);
		data.put("pre12", shift[13]);
		data.put("pre13", shift[14]);
		data.put("pre14", shift[15]);
		data.put("cCurrent",shift2[1]);
		data.put("cpre1",shift2[2]);
		data.put("cpre2", shift2[3]);
		data.put("cpre3", shift2[4]);
		data.put("cpre4", shift2[5]);
		data.put("cpre5", shift2[6]);
		data.put("cpre6", shift2[7]);
		data.put("cpre7", shift2[8]);
		data.put("cpre8", shift2[9]);
		data.put("cpre9", shift2[10]);
		data.put("cpre10", shift2[11]);
		data.put("cpre11", shift2[12]);
		data.put("cpre12", shift2[13]);
		data.put("cpre13", shift2[14]);
		data.put("cpre14", shift2[15]);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
		
	}

}
