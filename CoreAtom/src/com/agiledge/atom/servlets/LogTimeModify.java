package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.LogTimeDto;


/**
 * Servlet implementation class LogTimeModify
 */
public class LogTimeModify extends HttpServlet {

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();
		int id = 0;
		String logTime = "";
		try {
			id = Integer.parseInt(request.getParameter("id"));
			logTime = request.getParameter("logTime");
			LogTimeDto dto = new LogTimeDto();
			dto.setDoneBy(doneBy);
			dto.setId(id);
			dto.setLogTime(logTime);
			new LogTimeDao().updateLogtime(dto);
			response.sendRedirect("logtime_list.jsp");
		} catch (Exception e) {
			System.out.println("ERROR" + e);
			response.sendRedirect("logtime_list.jsp");
		}
	}

}
