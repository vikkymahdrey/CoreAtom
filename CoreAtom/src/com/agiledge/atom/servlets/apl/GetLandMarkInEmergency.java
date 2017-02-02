package com.agiledge.atom.servlets.apl;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dto.APLDto;

public class GetLandMarkInEmergency extends HttpServlet{
	
		protected void processRequest(HttpServletRequest request,
				HttpServletResponse response) throws ServletException, IOException {
			response.setContentType("text/html;charset=UTF-8");
			
			PrintWriter out = response.getWriter();
			try {
				/*
				 * TODO output your page here out.println("<html>");
				 * out.println("<head>");
				 * out.println("<title>Servlet GetLandMark</title>");
				 * out.println("</head>"); out.println("<body>");
				 * out.println("<h1>Servlet GetLandMark at " +
				 * request.getContextPath () + "</h1>"); out.println("</body>");
				 * out.println("</html>");
				 */

				// System.out.println(" "
				// + "track . in GetEmployee Servlet");
				APLDao dao = new APLDao();

				String landMarkName = request.getParameter("landMarkText");
				String location = request.getParameter("location");
				String site = request.getParameter("site");
				
				String transportType = request.getParameter("transportType");
				List<APLDto> landMarkList=null;			
				 if(transportType!=null && transportType.equals(SettingsConstant.SHUTTLE))
				{
					 landMarkList = dao.getShuttleLandMarks(landMarkName,location,site);	
				}
				 else
				 {
					 landMarkList = dao.getLandMarks(landMarkName,location,site); 
				 }
				 
					
				String landMarkPipe = "";
				for (APLDto lnd : landMarkList) {
					landMarkPipe += lnd.getLandMarkID() + ":" + lnd.getLandMark()
							+ "|";
				}

				// System.out.println("List : " + landMarkPipe);
				out.write(landMarkPipe);

			} finally {
				out.close();
			}
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
