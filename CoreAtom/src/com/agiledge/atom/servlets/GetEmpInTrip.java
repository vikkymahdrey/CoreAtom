package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;




import com.agiledge.atom.dashboard.dao.LiveTrackingDao;
import com.agiledge.atom.dto.TripDetailsChildDto;

public class GetEmpInTrip extends HttpServlet{

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
			PrintWriter out=resp.getWriter();
			String tripid=req.getParameter("tripId");
			List<TripDetailsChildDto> list=new LiveTrackingDao().getEmployeeInTrip(tripid);
			String ret="";
			for(TripDetailsChildDto dto : list){
				ret+=dto.getPersonnelNo()+"@"+dto.getEmployeeName()+"@"+dto.getInTime()+"@"+dto.getOutTime()+"@"+dto.getIsCorrectPos()+"#";
			}
			out.print(ret);
	}

}
