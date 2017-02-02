package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.service.VendorService;


/**
 * Servlet implementation class deleteVendor
 */
 
public class deleteVendor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public deleteVendor() {
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
		 
	try{
		HttpSession session = request.getSession(true);
		String doneBy = session.getAttribute("user").toString();

			System.out.println("in deleteVendor servlet");
			VendorDto dto = new VendorDto();
 			dto.setId(request.getParameter("deleteid"));
 			dto.setDoneBy(doneBy);
 			
 			
			int value=0;
			value= new VendorService().deleteVendor(dto);
			System.out.println(" value : "+ value);
			if(value>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Vendor deleted successfully</div>");
			
				
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Subscription deletion failure</div>");
			
			}
			
		}catch(Exception e)
		{
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Vendor deletion failure </div>");
		
			System.out.println("Exception in AddVendor servlet "+ e);
		}
		
		
		response.sendRedirect("transadmin_vendorSetUp.jsp");

	}

}
