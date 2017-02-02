package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.SiteService;


/**
 * Servlet implementation class AddVendorToSite
 */

public class AddVendorToSite extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddVendorToSite() {
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
		 String siteId="";
		try{
			HttpSession session = request.getSession(true);
			String doneBy = session.getAttribute("user").toString();
			 String vendors[]=request.getParameterValues("vendorsInSite");
			 siteId=request.getParameter("siteId");
			 
			int value=0;
			System.out.println("..first.");
			if(vendors==null||vendors.length==0)
			{
				System.out.println("..delete.");
				value=new SiteService().deleteVendorsFromSite(siteId);
			}else
			{
				
				System.out.println("..add.");
			value=	new SiteService().addVendorToSite(siteId, vendors);
			
			 
			}
			
			if(value>0)
			{
			
				request.getSession().setAttribute("status",
						"<div class=\"success\" > Vendor assigned successfully</div>");
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" > Vendor assignment failure</div>");
			}
		}catch(Exception e)
		{
			System.out.println(" Error in AddVendorToSite: " + e);
		}
		
		response.sendRedirect("vendor_site.jsp?siteId=" + siteId);
	}

}
