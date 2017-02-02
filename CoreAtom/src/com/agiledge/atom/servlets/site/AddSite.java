/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.site;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.SiteDto;
import com.agiledge.atom.service.SiteService;



/**
 * 
 * @author 123
 */
public class AddSite extends HttpServlet {

	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		String doneBy = session.getAttribute("user").toString();
		SiteDto dto = new SiteDto();
		String companyId=request.getParameter("companyId");
		String branchId=request.getParameter("branchId");
		dto.setBranch(branchId);
		dto.setName(request.getParameter("siteName"));
		dto.setLandmark(request.getParameter("landMarkID"));
		dto.setNight_shift_start(request.getParameter("night_shift_start"));
		dto.setNight_shift_end(request.getParameter("night_shift_end"));
		dto.setLady_securiy(request.getParameter("lady_security"));
		dto.setDoneBy(doneBy);
		if (new SiteService().addSite(dto) > 0) {
			session.setAttribute("status",
					"<div class='success' >Site Added Successfully </div>");
		} else {
			session.setAttribute("status",
					"<div class='failure' >Site Insertion Failed ! </div>");
		}

		response.sendRedirect("site.jsp?companyId=" +companyId+ "&branchId=" + branchId);


	}

	// <editor-fold defaultstate="collapsed"
	// desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
	/**
	 * Handles the HTTP <code>GET</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Handles the HTTP <code>POST</code> method.
	 * 
	 * @param request
	 *            servlet request
	 * @param response
	 *            servlet response
	 * @throws ServletException
	 *             if a servlet-specific error occurs
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		processRequest(request, response);
	}

	/**
	 * Returns a short description of the servlet.
	 * 
	 * @return a String containing servlet description
	 */
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>

}
