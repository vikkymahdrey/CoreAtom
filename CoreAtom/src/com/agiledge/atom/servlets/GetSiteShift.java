package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.LogTimeDto;
import com.agiledge.atom.dto.SiteDto;


/**
 * Servlet implementation class GetSiteShift
 */
public class GetSiteShift extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		int siteId = Integer.parseInt(request.getParameter("siteId"));
		String source = request.getParameter("src");
		ArrayList<LogTimeDto> routeLogTimeDtos=null;
		String retString = "|";
		String remainPart="";
		if(source==null)
		{
			SiteDto dto=new SiteDao().getSite(request.getParameter("siteId"));	
		 routeLogTimeDtos = new LogTimeDao().getAllRouteLogtime(siteId);
		 remainPart=dto.getWeekoffcombain()+"|";
		}
		else
		{
			routeLogTimeDtos = new LogTimeDao().getAllLogtime(siteId);			
		}		
		retString += "<select name='shift' id='shift' multiple='multiple'>";
		for (LogTimeDto logTimeDto : routeLogTimeDtos) {
			if (logTimeDto.getStatus().equals("primary")) {
				retString += "<option value='" + logTimeDto.getId() + "'>"
						+ logTimeDto.getLogTime() + " : "
						+ logTimeDto.getLogtype() + "</option>";
			}
		}
		retString +="</select>";
		retString += "|<select name='selectedshift' id='selectedshift' multiple='multiple'>";		
		for (LogTimeDto logTimeDto : routeLogTimeDtos) {
			if (logTimeDto.getStatus().equals("combained")) {
				retString += "<option value='" + logTimeDto.getId() + "'>"
						+ logTimeDto.getLogTime() + " : "
						+ logTimeDto.getLogtype() + "</option>";
			}				
		}
		retString +="</select>";
		
		retString += " | "+remainPart;		
		
		out.write(retString);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
