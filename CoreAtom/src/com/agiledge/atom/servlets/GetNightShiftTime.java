package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.SiteDto;

public class GetNightShiftTime extends HttpServlet{
@Override
protected void doPost(HttpServletRequest req, HttpServletResponse resp)
		throws ServletException, IOException {
	PrintWriter out =resp.getWriter();
	String site = req.getParameter("site");
	SiteDao dao= new SiteDao();
	SiteDto dto=dao.getSite(site);
	String time= dto.getNight_shift_start()+"#"+dto.getNight_shift_end();
	out.print(time);
}
}
