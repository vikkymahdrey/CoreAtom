package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dto.UnitMasterDTO;

/**
 * Servlet implementation class GetUnits
 */
public class GetUnits extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		PrintWriter out = response.getWriter();
		String unitCode = request.getParameter("unitCode");
		String unitName = request.getParameter("unitName");
		String retValue = "";
		//UnitMasterDTO dto = new UnitMasterDTO();
		ArrayList<UnitMasterDTO> list = new ArrayList<UnitMasterDTO>();
		if(unitCode==null || unitCode=="")
		{
		//	list=new DAO.UnitMasterDAO().getUnitbyName(unitName);	
		}else
		{
		//	list=new DAO.UnitMasterDAO().getUnitbyCode(unitCode);
		}
		for(UnitMasterDTO dto: list )
		{
			retValue += "<tr><td>"+dto.getUnitcode() +"</td>" +
					"<td id='"+dto.getUnitid()+"projecttd'>"+dto.getUnitname() +"</td>" +
					"<td><a href='#' onclick='closeWindow(\""+ dto.getUnitid()+"\",\"unit\")'>Select</a></td></tr>";
		}
System.out.println(retValue);
		out.write(retValue);
		
	}
		
    public GetUnits() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
