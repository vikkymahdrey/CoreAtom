package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.UnitMasterDTO;
import com.agiledge.atom.service.UnitService;

/**
 * Servlet implementation class AddUnit
 */
public class AddUnit extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddUnit() {
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
		// TODO Auto-generated method stub
		HttpSession session=request.getSession();
		String unitcode=request.getParameter("adducode");
		String unitname=request.getParameter("adduname");
		String locid=request.getParameter("addlocid");
		String status=request.getParameter("addstatus");
		UnitMasterDTO dto=new UnitMasterDTO();
		dto.setUnitcode(unitcode);
		dto.setUnitname(unitname);
		dto.setLocation_id(locid);
		dto.setStatus(status);
		int returnInt=new UnitService().insertUnitData(dto);
		 if(returnInt==1)
			{
				session.setAttribute("status",
					"<div class='success'>Unit Data Inserted Successfully</div");
		    }
		   else
		   {

			session.setAttribute("status",
					"<div class='failure'>Operation Failed</div");
		   }
		   response.sendRedirect("viewUnit.jsp");
		
	}

}
