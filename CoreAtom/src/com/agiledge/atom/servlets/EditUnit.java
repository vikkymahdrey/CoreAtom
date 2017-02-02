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
 * Servlet implementation class EditUnit
 */
public class EditUnit extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EditUnit() {
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
		int unitid=Integer.parseInt(request.getParameter("unid"));
		String unitcode=request.getParameter("ucode");
		String unitname=request.getParameter("uname");
		String locid=request.getParameter("lid");
		UnitMasterDTO dto=new UnitMasterDTO();
		dto.setUnitid(unitid);
		dto.setUnitcode(unitcode);
		dto.setUnitname(unitname);
		dto.setLocation_id(locid);
		int returnInt=new UnitService().updateUnitData(dto);
		if(returnInt==1)
		{
			session.setAttribute("status",
				"<div class='success'>Unit Data Updated Successfully</div");
	    }
	   else
	   {

		session.setAttribute("status",
				"<div class='failure'>Operation Failed</div");
	   }
	   response.sendRedirect("viewUnit.jsp");
	}

}
