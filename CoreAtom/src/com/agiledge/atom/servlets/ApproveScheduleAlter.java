package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ScheduleAlterDao;

public class ApproveScheduleAlter extends HttpServlet{
 @Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	String scheduleId= req.getParameter("scheduleId");
	String date= req.getParameter("date");
	String status= req.getParameter("status");
	String ApprovedBy= req.getParameter("ApprovedBy");
	String login=req.getParameter("login");
	String logout=req.getParameter("logout");
	int count=new ScheduleAlterDao().approveScheduleAlter(scheduleId,ApprovedBy,date,status,login,logout);
	PrintWriter out = resp.getWriter();
	out.print(count);
}
}
