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
 * Servlet implementation class AddVendor
 */

public class AddVendor extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public AddVendor() {
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
		 String errorMessage="Vendor adding failure";
		 HttpSession session = request.getSession(true);
		 String doneBy = session.getAttribute("user").toString();

		try{			
			VendorDto dto = new VendorDto();
			dto.setName(request.getParameter("name"));
			dto.setAddress(request.getParameter("address"));
			dto.setEmail(request.getParameter("email"));
			dto.setContactNumber(request.getParameter("contactNumber"));
			dto.setLoginId(request.getParameter("loginId"));
			dto.setPassword(request.getParameter("password"));
			dto.setCompanyId(request.getParameter("company"));
			dto.setDoneBy(doneBy);			
			int value=0;
			value= new VendorService().addVendor(dto);			
			if(value>0)
			{
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" > Vendor addition successful</div>");						
			}else
			{
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" > Vendor addition failure </div>");			
			}			
		}catch(Exception e)
		{
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" > Vendor addition failure </div>");		
			System.out.println("Exception in AddVendor servlet "+ e);
		}
		response.sendRedirect("transadmin_vendorSetUp.jsp");
	}

}
