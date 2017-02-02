package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.Servlet;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.tasks.service.Scheduler;


/**
 * Servlet implementation class StartupServlet
 */
public class Startup extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public Startup() {
		super();
	}

	/**
	 * @see Servlet#init(ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
		try {
			// System.out.println("In Startup servlet");			
			if(SettingsConstant.utiltiyRun.equalsIgnoreCase("yes"))
			{
			Scheduler.getInstance().startTasks();
			// System.out.println("===================INIT Method====================");
		}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void destroy() {
		try {
			Scheduler.getInstance().endTasks();
			// System.out.println("===================Destroy Method====================");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	protected void service(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
	}
}
