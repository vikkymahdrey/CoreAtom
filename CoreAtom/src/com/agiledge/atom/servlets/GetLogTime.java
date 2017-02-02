package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.GeneralShiftDAO;
import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.dto.LogTimeDto;


/**
 * Servlet implementation class GetLogTime
 */
public class GetLogTime extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public GetLogTime() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @throws IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		String logtype = request.getParameter("logtype");
		String type = request.getParameter("type");
		String site = request.getParameter("site");
		String isShuttle = request.getParameter("shuttle");
		String returnString = "";
		PrintWriter out = response.getWriter();
		ArrayList<LogTimeDto> logTimeDtos = null;
		try {
			if(isShuttle!=null)
			{
			ArrayList<GeneralShiftDTO> shutleList=new GeneralShiftDAO().getActiveLogData(logtype);
			for(GeneralShiftDTO dto : shutleList)
			{
				returnString += "<option value=" + dto.getLogtime() + ">"
						+ dto.getLogtime() + "</option>";	
			}
			}
		else{
			if (logtype != null) {
				if (type != null && type.equals("disabled")) {
					logTimeDtos = new LogTimeDao().getAllInactiveLogtime(
							logtype, site);
				} else {
					logTimeDtos = new LogTimeDao().getAllLogtime(logtype, site);
				}
				returnString += "";
				for (LogTimeDto dto : logTimeDtos) {

					returnString += "<option value=" + dto.getLogTime() + ">"
							+ dto.getLogTime() + "</option>";
				}
			} else {
				ArrayList<LogTimeDto> loginTimeDtos = new LogTimeDao()
						.getAllLogtime("IN", site);
				ArrayList<LogTimeDto> logoutTimeDtos = new LogTimeDao()
						.getAllLogtime("OUT", site);
				returnString += "";
				for (LogTimeDto dto : loginTimeDtos) {

					returnString += "<option value=" + dto.getLogTime() + ">"
							+ dto.getLogTime() + "</option>";
				}
				returnString +="|";
				for (LogTimeDto dto : logoutTimeDtos) {

					returnString += "<option value=" + dto.getLogTime() + ">"
							+ dto.getLogTime() + "</option>";
				}

				
			}
		}
			// System.out.println(returnString);
		} catch (Exception e) {
			System.out.println("ERRO" + e);
		}
		//System.out.println("return string"+returnString);
		out.write(returnString);

	}
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
processRequest(request, response);
		}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

}
