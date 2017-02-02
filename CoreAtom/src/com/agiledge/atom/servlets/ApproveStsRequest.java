package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.service.StsService;

public class ApproveStsRequest extends HttpServlet{
	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		PrintWriter out = resp.getWriter();
		String responce="Failed";
		String ststripid =req.getParameter("id");
		String status=req.getParameter("status");
		String approvedby=req.getParameter("approvedby");
		int result=new StsService().approveStsRequest(ststripid, status,approvedby);
		if(result>0){
			responce=status;
		}
		out.print(responce);
	}
}
