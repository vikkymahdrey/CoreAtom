package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ProjectDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.SettingsDTO;


/**
 * Servlet implementation class GetSettings
 */

public class GetSettings extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
	protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		response.setContentType("text/html;charset=UTF-8");
		PrintWriter out = response.getWriter();
		try {
			String property=request.getParameter("property");
			String data = "";
			SiteDao obj = new SiteDao();
			ProjectDao pobj=new ProjectDao();
			String projectname="";
			ArrayList<SettingsDTO> list=new SettingsDoa().getSettings(property);
			String html = "<table border='0'>" + " <thead>" + " <tr >"
					+ "<th align='center'>Id</th>  " + "   <th align='center'>Site</th>"
					+ "<th align='center'>Project</th>" + "<th align='center'>Settings For</th>"
					+ "     <th align='center'>Effective Date</th>" + "     <th align='center'>To Date</th>"
					+ "     <th align='center'>Value</th>"
					+"<th align='center'>Actions</th> </tr>" + "</thead>" + " <tbody>";
			if (list.size() == 0) {
				data = "<td colspan='7' > No Data Found ! </td>";
			}
			for(SettingsDTO dto:list)
			{
				projectname="All";
				if(dto.getProjectid()!=0)
				{
					projectname=pobj.getprojectName(dto.getProjectid());
				}
				data = data
						+ "<tr align='center'> "
						+ "<td>"+dto.getId()+"</td><td>"+obj.getsiteName(dto.getSiteid())+"</td><td>"+projectname
						+"</td><td>"+dto.getProperty()+"</td><td>"+dto.getEffectivedate()+"</td><td>"+dto.getTodate()
						+"</td><td>"+dto.getAmount()+"</td><td style='border-bottom: 0px solid #cE5; padding: 0px;'><img src='images/delete.png' class='deleteButton' onclick='deletesettings("+dto.getId()+")' id='deleteimage'title='Delete' /></td></tr>";
			}
			html = html + data + "</table>";
			out.write(html);
			
			} finally {
				out.close();
			}
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
	@Override
	public String getServletInfo() {
		return "Short description";
	}// </editor-fold>


}
