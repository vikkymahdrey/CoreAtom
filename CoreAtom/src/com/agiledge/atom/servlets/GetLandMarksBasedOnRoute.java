package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dto.APLDto;

public class GetLandMarksBasedOnRoute extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		String route= req.getParameter("route");
		PrintWriter out = resp.getWriter();
		ArrayList<APLDto> apllist= new APLDao().getAPLBasedOnRoute(route);
		String apl="";
		for (APLDto aplDto : apllist) {
			apl=apl+"<option value=\""+aplDto.getLandMarkID()+"\">"+aplDto.getArea()+"->"+aplDto.getPlace()+"->"+aplDto.getLandMark()+"</option>";
		}
		out.print(apl);
	}

}
