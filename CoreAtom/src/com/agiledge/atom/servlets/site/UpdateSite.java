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
 * Servlet implementation class UpdateSite
 */

public class UpdateSite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UpdateSite() {
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
		 
		HttpSession session = request.getSession();
		SiteDto dto = new SiteDto();
		
		String companyId=request.getParameter("companyId");
		String branchId=request.getParameter("branchId");
		HttpSession session1 = request.getSession(true);
		String doneBy = session1.getAttribute("user").toString();
		
		System.out.println("..................................");
		System.out.print("Comapny ID : " + companyId + " Branch Id : " + branchId);
		System.out.println("..................................");
		dto.setId(request.getParameter("id"));
		dto.setName(request.getParameter("siteName"));
		dto.setLandmark(request.getParameter("landMarkID"));
		dto.setNight_shift_start(request.getParameter("night_shift_start"));
		dto.setNight_shift_end(request.getParameter("night_shift_end"));
		dto.setLady_securiy(request.getParameter("lady_security"));
		dto.setDoneBy(doneBy);
System.out.println(" .. : "+ dto.getLady_securiy() + " " + dto.getNight_shift_start() + " ");
System.out.println(" .. : "+ dto.getNight_shift_end() + " " + dto.getLandmark() + " ");
System.out.println(" .. : "+ dto.getName() + "   ");
		if (new SiteService().updateSite(dto) > 0) {
			session.setAttribute("status",
					"<div class='success' >Site Modified Successfully </div>");
		} else {
			session.setAttribute("status",
					"<div class='failure' >Site Modification Failed ! </div>");
		}

		response.sendRedirect("site.jsp?companyId=" +companyId+ "&branchId=" + branchId);

	}

}
