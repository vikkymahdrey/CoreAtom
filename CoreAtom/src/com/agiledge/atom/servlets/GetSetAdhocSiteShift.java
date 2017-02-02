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
 * Servlet implementation class GetSetAdhocSiteShift
 */

public class GetSetAdhocSiteShift extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public GetSetAdhocSiteShift() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out=response.getWriter();
		int siteId = Integer.parseInt(request.getParameter("siteId"));
		String source = request.getParameter("src");
		if(source!=null&&source.equalsIgnoreCase("get"))
		{
			ArrayList<LogTimeDto> genLogTimeDtos=null;
			ArrayList<LogTimeDto> adhocLogTimeDtos=null;
			String retString = "|";
			String remainPart="";
			SiteDto dto=new SiteDao().getSite(request.getParameter("siteId"));	
			genLogTimeDtos = new LogTimeDao().getNotAdhocLogtime(siteId);
			adhocLogTimeDtos=new LogTimeDao().getShiftAdhoc(siteId);
		 remainPart=dto.getWeekoffcombain()+"|";
		
		retString += "<select name='shift' id='shift' multiple='multiple'>";
		for (LogTimeDto logTimeDto : genLogTimeDtos) {
				retString += "<option value='" + logTimeDto.getId() + "'>"
						+ logTimeDto.getLogTime() + " : "
						+ logTimeDto.getLogtype() + "</option>";			
		}
		retString +="</select>";
		retString += "|<select name='selectedshift' id='selectedshift' multiple='multiple'>";		
		for (LogTimeDto logTimeDto : adhocLogTimeDtos) {
				retString += "<option value='" + logTimeDto.getId() + "'>"
						+ logTimeDto.getLogTime() + " : "
						+ logTimeDto.getLogtype() + "</option>";			
		}
		retString +="</select>";
		
		retString += " | "+remainPart;				
		out.write(retString);

	}
		else
		{
			String[] shifts=request.getParameterValues("selectedshift");
			try {
				int value=new LogTimeDao().updateAdhocSiteShift(shifts,request.getParameter("siteId"));
				if (value > 0) {		
					request.getSession()
							.setAttribute("status",
									"<div class=\"success\" >  Adhoc Shift Updation Successful </div>");
				} else {
					request.getSession()
							.setAttribute("status",
									"<div class=\"failure\" > Adhoc Shift Updation Failed </div>");
				}

			} catch (Exception e) {
				request.getSession()
				.setAttribute("status",	"<div class=\"failure\" > Adhoc Shift Updation Failed </div>");
				e.printStackTrace();
			}
			response.sendRedirect("adhoc_shift.jsp");
	
		}
	}

}
