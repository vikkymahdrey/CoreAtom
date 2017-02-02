package com.agiledge.atom.servlets;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.agiledge.atom.commons.OtherFunctions;

/**
 * Servlet Filter implementation class PreServeltFilter
 */
 
public class PreServeltFilter implements Filter {

    /**
     * Default constructor. 
     */
    public PreServeltFilter() {
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see Filter#destroy()
	 */
	public void destroy() {
		// TODO Auto-generated method stub
	}

	/**
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
		// place your code here
		HttpServletRequest httpRequest=((HttpServletRequest) request);
		HttpServletResponse httpResponse=((HttpServletResponse) response);
		HttpSession session = httpRequest.getSession();

		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) 
		{
			
			 
			httpResponse.sendRedirect("index.jsp?message=Invalid user");
		}else
		{
		
		//httpRequest.ge 	
		chain.doFilter(request, response);
		}
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
