/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ProjectDao;
import com.agiledge.atom.dto.ProjectDto;



/**
 * 
 * @author muhammad
 */
public class GetProject extends HttpServlet {

	/**
	 * Processes requests for both HTTP <code>GET</code> and <code>POST</code>
	 * methods.
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
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String type=request.getParameter("type");
		String projectCode = "" + request.getParameter("projectCode");
		String projectName = "" + request.getParameter("projectName");				
		String retValue = "";
		ArrayList<ProjectDto> projectList = new ArrayList<ProjectDto>();
		ProjectDao projectDaoObj = new ProjectDao();
		if (projectCode.equals("null")||projectCode.equals("")) {
			projectList = projectDaoObj.getProjectsByName(projectName);
		} else {
			projectList = projectDaoObj.getProjectsByCode(projectCode);
		}
		for (ProjectDto projectDtoObj : projectList) {
			if(type.equalsIgnoreCase("project"))
			{
			retValue += "<tr><td>"+projectDtoObj.getProject() +"</td>" +
					"<td id='"+projectDtoObj.getId()+"projecttd'>"+projectDtoObj.getDescription() +"</td>" +
					"<td><a href='#' onclick='closeWindow(\""+projectDtoObj.getId()+"\",\""+projectDtoObj.getProject()+"\")'>Select</a></td></tr>";
			}
			else
			{
				retValue += "<tr><td>"+projectDtoObj.getProject() +"</td>" +
						"<td id='"+projectDtoObj.getId()+"projecttd'>"+projectDtoObj.getDescription() +"</td>" +
						"<td><a href='#' onclick='closeWindow(\""+projectDtoObj.getId()+"\",\""+projectDtoObj.getProject()+"\")'>Select</a></td></tr>";	
			}
		}
		out.write(retValue);
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
