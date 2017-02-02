package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.LogTimeDao;
import com.agiledge.atom.dto.LogTimeDto;

/**
 * Servlet implementation class GetExtensionShift
 */
public class GetExtensionShift extends HttpServlet {
	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */

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
		String pickDrop = request.getParameter("pickdrop");
		ArrayList<LogTimeDto> dtos = new LogTimeDao().getAdhocShift(pickDrop);
		String retString = "<option>Select </option>";
		PrintWriter out = response.getWriter();
		for (LogTimeDto timeDto : dtos) {
			retString += "<option value='" + timeDto.getLogTime() + "'>"
					+ timeDto.getLogTime() + "</option> ";
		}
		out.write(retString);

	}

}
