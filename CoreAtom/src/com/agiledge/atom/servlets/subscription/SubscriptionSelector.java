/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.servlets.subscription;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.service.EmployeeSubscriptionService;


/**
 * 
 * @author 123
 */
public class SubscriptionSelector extends HttpServlet {

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
		response.setContentType("text/html;charset=UTF-8");

		String location = "";
		try {
			/*
			 * TODO output your page here out.println("<html>");
			 * out.println("<head>");
			 * out.println("<title>Servlet SubscriptionSelector</title>");
			 * out.println("</head>"); out.println("<body>");
			 * out.println("<h1>Servlet SubscriptionSelector at " +
			 * request.getContextPath () + "</h1>"); out.println("</body>");
			 * out.println("</html>");
			 */
			HttpSession session = request.getSession();
			// System.out.println("user " + session.getAttribute("user"));

			if (session.getAttribute("user") != null) {

				if (new EmployeeSubscriptionService().isSubscribed(request
						.getSession().getAttribute("user").toString())) {
					location = "emp_showSubscription.jsp";
				} else if (new EmployeeSubscriptionService()
						.isSubscriptionRequestMade(request.getSession()
								.getAttribute("user").toString())) {
					location = "emp_showSubscription.jsp";
				} else {
					location = "emp_subscription.jsp";
				}
			} else {
				location = "index.jsp";
			}

		} catch (Exception e) {
			location = "index.jsp";
			System.out.println("Exception in SubscriptionSelector Servlet : "
					+ e);
		}

		// response.sendRedirect(location);
		 System.out.println("LOC"+location);
		RequestDispatcher rd = getServletContext().getRequestDispatcher(
				"/" + location);
		rd.forward(request, response);
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
