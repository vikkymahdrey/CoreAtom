package com.agiledge.atom.servlets;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.dao.DistanceListDao;
import com.agiledge.atom.dto.DistanceChartDto;


/**
 * Servlet implementation class GetSrcDestLandmark
 */

public class GetSrcDestLandmark extends HttpServlet {

	/**
	 * @see HttpServlet#HttpServlet()
	 */

	/**
	 * @see Servlet#init(ServletConfig)
	 */

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		String srcid = request.getParameter("srcid");
		if (srcid != null) {
			String destid = request.getParameter("destid");
			String dist = "" + request.getParameter("dist");
			System.out.println("source="+srcid+"  Dest="+destid+"   Dista"+dist);
			new DistanceListDao().storeDistance(srcid, destid, dist);

		} else {
			HttpSession session = request.getSession(true);
			int srcId = 1;
			int destId = 0;
			if (session.getAttribute("distchartsrc") != null) {
				srcId = Integer.parseInt(session.getAttribute("distchartsrc")
						.toString());
				destId = Integer.parseInt(session.getAttribute("distchartdest")
						.toString());
			}
			else
			{
			srcId=DistanceListDao.lastId;	
			}
			try {
				ArrayList<DistanceChartDto> distanceChartDtos = new DistanceListDao()
						.getsourceAndDestination(srcId, destId);
			
				PrintWriter out = response.getWriter();
				String retVal = "";

				for (DistanceChartDto distanceChartDto : distanceChartDtos) {
					retVal += ":" + distanceChartDto.getSourceLandmarkId();
					retVal += ":" + distanceChartDto.getSourceLattitude();
					retVal += ":" + distanceChartDto.getSourceLongitude();
					retVal += ":" + distanceChartDto.getDestinationLandmarkId();
					retVal += ":" + distanceChartDto.getDestinationLattitude();
					retVal += ":" + distanceChartDto.getDestinationLongitude()
							+ "$";
				}
				if(distanceChartDtos!=null&&distanceChartDtos.size()>0)
				{
				session.setAttribute("distchartsrc", Integer
						.parseInt(distanceChartDtos.get(
								distanceChartDtos.size() - 1)
								.getSourceLandmarkId()));
				session.setAttribute("distchartdest", Integer
						.parseInt(distanceChartDtos.get(
								distanceChartDtos.size() - 1)
								.getDestinationLandmarkId()));
				}
				out.write(retVal);
			} catch (Exception e) {
				System.out.println("Error" + e);
			}

		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

}
