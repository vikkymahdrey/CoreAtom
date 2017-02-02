package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Calendar;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dto.PayrollDto;
import com.agiledge.atom.service.PayrollService;

/**
 * Servlet implementation class UpdatePayrollAmount
 */
 
public class UpdatePayrollAmount extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdatePayrollAmount() {
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
		PrintWriter out = response.getWriter();
		try{
			String id = request.getParameter("id");
			String amount = request.getParameter("amount");
			String reason = request.getParameter("reason");
			reason = reason==null ? "" : reason;
			id=id==null?"":id;
			amount = amount==null ? "" : amount;
			PayrollService service = new PayrollService();
			if((id.trim().equals("")||amount.trim().equals(""))==false) {
				PayrollDto dto = new PayrollDto();
				dto.setId(id);
				dto.setAmount(Double.parseDouble(amount));
				dto.setReason(reason);
				dto.setUpdatedBy(request.getSession().getAttribute("user").toString());
				int val = service.updatePayrollAmount(dto);
				if(val>0) {
					JSONObject json= new JSONObject();
					json.put("result", "true");
					json.put("id", dto.getId());
					json.put("displayName", new EmployeeDao().getEmployeeAccurate(dto.getUpdatedBy()).getDisplayName() );
					json.put("reason", dto.getReason());
					Calendar cal = Calendar.getInstance();
					
					json.put("date",OtherFunctions. changeDateFromatToNormalWithTime(  cal.getTime()));
					json.put("personnelNo", dto.getEmployee().getPersonnelNo());
					out.print(json.toString());
				} else {
					JSONObject json= new JSONObject();
					json.put("result", "false");
					out.print(json.toString());
					 
				}
				
			}
		}catch(Exception e)
		{
			JSONObject json= new JSONObject();
			try {
				json.put("result", "false");
			} catch (JSONException e1) {
				// TODO Auto-generated catch block
				System.out.println("Error in Update PayrollAmount in Catch blok json "+e);
			}
			out.print(json.toString());
			
		}
	
	}
	
	

}
