package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.PasswordGenerator;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.service.EmployeeService;
import com.agiledge.atom.service.SettingsService;

/**
 * Servlet implementation class ResetPassword
 */
public class ResetPassword extends HttpServlet {
	/**
	 * @see HttpServlet#HttpServlet()
	 */
	public ResetPassword() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		HttpSession session = request.getSession();
		String empid = request.getParameter("pwdempid");
		String email = request.getParameter("email");

		String password = new PasswordGenerator().randomString(10);
		String subject = "Password Reset";
		if (email == null) {
			int result = new EmployeeService().UpdatePassword(
					Integer.parseInt(empid), password);
			if (result == 1) {
				EmployeeDto dto = new EmployeeDao().getEmployeeAccurate(empid);
				dto.setPassword(password);
				String message = SettingsService.getPasswordResetMessage(dto);
				SendMail mail = SendMailFactory.getMailInstance();
				mail.send(dto.getEmailAddress(), subject, message);
				session.setAttribute("status",
						"<div class='success'>Password Reset Successfull</div");
			} else {
				session.setAttribute("status",
						"<div class='failure'>Operation Failed</div");
			}
			response.sendRedirect("viewallExternal.jsp");
		} else {
			try {
				int result = new EmployeeService().UpdatePassword(email,
						password);
				if (result == 1) {
					EmployeeDto dto = new EmployeeDao()
							.getEmployeeByEmail(email);
					dto.setPassword(password);
					String message = SettingsService
							.getPasswordResetMessage(dto);
					SendMail mail = SendMailFactory.getMailInstance();

					mail.send_invokedByRun(dto.getEmailAddress(), subject,
							message);

					session.setAttribute("status",
							"<div class='success'>New Password has been sent to your Email</div");
				} else {
					session.setAttribute("status",
							"<div class='failure'>Password Reset Failed</div");
				}
			} catch (Exception e) {
				session.setAttribute("status",
						"<div class='failure'>Password Reset Failed</div");
			}
			response.sendRedirect("index.jsp");

		}

	}
}
