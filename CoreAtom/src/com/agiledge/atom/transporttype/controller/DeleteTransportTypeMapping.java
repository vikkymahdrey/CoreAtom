package com.agiledge.atom.transporttype.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.transporttype.dto.TransportTypeDto;
import com.agiledge.atom.transporttype.service.TransportTypeService;


/**
 * Servlet implementation class DeleteTransportTypeMapping
 */
 
public class DeleteTransportTypeMapping extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public DeleteTransportTypeMapping() {
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

	private void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws IOException {
		//  
 
 
		try {
			System.out.println("IN DeleteTransportTypemapping");
			String id = request.getParameter("id");
			TransportTypeService service = new TransportTypeService();
			TransportTypeDto dto = service.getTransportTypeMapping(id);
			int value = service.deleteTransportTypeMapping(dto);
			System.out.println("IN DeleteTransportTypemapping");
			if(value > 0) {
				request.getSession().setAttribute("status",
						"<div class=\"success\" width=\"100%\" >Deletion successful  </div>");
			} else {
				request.getSession().setAttribute("status",
						"<div class=\"failure\" width=\"100%\" >Deletion failure  </div>");
			}
			
 			
		} catch(Exception e) {
			request.getSession().setAttribute("status",
					"<div class=\"failure\" width=\"100%\" >Deletion failure "+e+" </div>");
			System.out.println("Error in DeleteTransportTypeMapping : "+ e);
		}
			response.sendRedirect("transporttype_mapping.jsp");	
			System.out.println("Happend");
	}

}
