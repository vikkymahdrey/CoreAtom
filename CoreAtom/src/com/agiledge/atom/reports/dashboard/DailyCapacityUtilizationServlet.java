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
 * Servlet implementation class DailyCapacityUtilizationServlet
 */
public class DailyCapacityUtilizationServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DailyCapacityUtilizationServlet() {
        super();
        // TODO Auto-generated constructor stub
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
		DailyCapacityUtilization obj=new DailyCapacityUtilization();
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	    Calendar cal =Calendar.getInstance();
	    int cplanned[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},cactual[]={0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0},j=2;
	    cplanned[1]=obj.getPlanned(dateFormat.format(cal.getTime()).toString());
	    cactual[1]=obj.getActual(dateFormat.format(cal.getTime()).toString());
	    System.out.println("cu"+j);
	    System.out.println("cuda"+dateFormat.format(cal.getTime()).toString());
	   for (int i=1;i<=14;i++)
	    {
		cal=Calendar.getInstance();
	    cal.add(Calendar.DATE,-(i));
	    cplanned[j]=obj.getPlanned(dateFormat.format(cal.getTime()).toString());
	    cactual[j]=obj.getActual(dateFormat.format(cal.getTime()).toString());
	    System.out.println("infor"+j);
	    System.out.println("inforrrrr"+dateFormat.format(cal.getTime()).toString());
	    j++;
	    }
	    
	    JSONObject data = new JSONObject();
		data.put("Current",cplanned[1]);
		data.put("pre1",cplanned[2]);
		data.put("pre2", cplanned[3]);
		data.put("pre3", cplanned[4]);
		data.put("pre4", cplanned[5]);
		data.put("pre5", cplanned[6]);
		data.put("pre6", cplanned[7]);
		data.put("pre7", cplanned[8]);
		data.put("pre8", cplanned[9]);
		data.put("pre9", cplanned[10]);
		data.put("pre10", cplanned[11]);
		data.put("pre11", cplanned[12]);
		data.put("pre12", cplanned[13]);
		data.put("pre13", cplanned[14]);
		data.put("pre14", cplanned[15]);
		data.put("cCurrent",cactual[1]);
		data.put("cpre1",cactual[2]);
		data.put("cpre2", cactual[3]);
		data.put("cpre3", cactual[4]);
		data.put("cpre4", cactual[5]);
		data.put("cpre5", cactual[6]);
		data.put("cpre6", cactual[7]);
		data.put("cpre7", cactual[8]);
		data.put("cpre8", cactual[9]);
		data.put("cpre9", cactual[10]);
		data.put("cpre10", cactual[11]);
		data.put("cpre11", cactual[12]);
		data.put("cpre12", cactual[13]);
		data.put("cpre13", cactual[14]);
		data.put("cpre14", cactual[15]);
		response.setContentType("application/json");
		PrintWriter out = response.getWriter();
		
		out.write(data.toString());
		out.flush();
	}

}
