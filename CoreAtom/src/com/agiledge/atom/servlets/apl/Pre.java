package com.agiledge.atom.servlets.apl;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

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
import com.agiledge.atom.dao.FilterSupportDao;
import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.loadsettings.service.LoadSettingsService;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.settings.dto.SettingStatus;

public class Pre implements Filter {
	protected FilterConfig filterConfig;
	
	public int i;
	public static  String hasAuthentication="undefined";
	public static String autherisation_insertion_mode="undefined";
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		this.filterConfig=null;
		
	}


	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain chain) throws IOException, ServletException {
		// TODO Auto-generated method stub
	System.out.println(">> FILTER <<");
		String indexPage="index.jsp";
		String forwardPage="index.jsp";
		String queryString="";
		HttpServletRequest httpRequest=((HttpServletRequest) request);
		HttpServletResponse httpResponse=((HttpServletResponse) response);
		HttpSession session = httpRequest.getSession();
		try{
		
			String role="";
			String preUrl = httpRequest.getServletPath();
			String actualPrePage=getURI(httpRequest);			
		String page=preUrl;
		System.out.println(page);
		
		queryString= getQueryString(httpRequest)==null?"": getQueryString(httpRequest);
		
		page=page==null?"":page;
		if(page.equals("")==false )
		{
			page=page.substring(1);
			if(page.contains("?"))
			{
				httpRequest.getSession().invalidate();
				page=page.substring(0,page.indexOf("?"));
			}
			queryString=queryString==""?"":"?"+queryString;
			forwardPage=page+queryString;
		}else
		{
			page=indexPage;
			forwardPage=page;
		}

		
		if(page.equals("index.jsp")||page.equals("forgotPassword.jsp") )
		{
			
		
			httpRequest.getRequestDispatcher("/"+forwardPage).forward(request, response);
			//httpResponse.sendRedirect(forwardPage);
		}
		else if (page.equals("UserValidation")) { 
			httpRequest.getRequestDispatcher("/"+page).forward(request, response);
		}
		
		else if(OtherFunctions.isEitherJspOrServlet(actualPrePage))
		{
			
		String employeeId = OtherFunctions.checkUser(session);
		if (employeeId == null || employeeId.equals("null")) 
		{
			System.out.println("NO employee Id page : "+ page);
			String oPage="index.jsp";
			if(actualPrePage.equals("Logout")) {
				httpResponse.sendRedirect(oPage);
			}else {
				System.out.println("Actual Prepage = "+ actualPrePage);
				if(actualPrePage.substring(actualPrePage.length()-3).equals(".do")==false)  {
					String param = actualPrePage + "___"
					+ httpRequest.getQueryString();
					oPage="index.jsp?prePage=" + param;
				}
			httpResponse.sendRedirect(oPage);
			}
		}else
		{
			System.out.println(" "+hasAuthentication+ " page : " + page);
			role=httpRequest.getSession().getAttribute("role").toString();
			
		
		role=role==null?"":role;
		
		
		
		 
		 
		if(hasAuthentication.equals("undefined"))
		{
			
			hasAuthentication=""+ new FilterSupportDao().hasAuthentication(  );
			
			System.out.println(" now hasAuthentication : " + hasAuthentication);
			SettingsDTO settingsDto = new SettingsDTO();
			settingsDto.setSiteid(-1);
			settingsDto.setProperty(SettingStatus.AUTHERISATION_INSERTION_MODE);
			settingsDto.setEffectivedate(null);
			settingsDto.setTodate(null);
			autherisation_insertion_mode = new SettingsService().getSettingValue(settingsDto).getValue().trim();
		}
		  if(hasAuthentication.equals("false"))
		{
		
			chain.doFilter(request, response);
		}else
		{
		
		
		
		boolean valid=false;
		
		
		
	 FilterSupportDao dao = new FilterSupportDao();
	//	valid=dao.authenticatePageRoleFromServeltContext((HttpServletRequest)request, role, page);
	 valid=false;
		System.out.println("Context validation  : " + valid);
		if(!valid) {
			valid = dao.authenticatePageRole((HttpServletRequest)request,   role, page, employeeId);
			System.out.println("Db validation :" + valid);
		}
		i++;		 		
		if(valid)
		{
			
			chain.doFilter(request, response);
		}else
		{
			session.invalidate();
			if(httpResponse!=null)
			{
			
				httpResponse.sendRedirect(indexPage);
				return;
			}else
			{
				httpRequest.getRequestDispatcher("/index.jsp").forward(request, response);
			}	
		}
		}
		}
		} else {
			httpRequest.getRequestDispatcher("/"+page).forward(request, response);
		}
		}catch(Exception e)
		{
			 
				
			 
			
			
			System.out.println("Error in pre"+e);
				httpRequest.getRequestDispatcher("/"+indexPage).forward(request, response);
			
			// httpResponse.sendRedirect(indexPage);
			 return;
			
			
		}
		
	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
		// TODO Auto-generated method stub
		try {
		this.filterConfig=arg0;
		i=0;
		SettingsDTO settingsDto = new SettingsDTO();
		settingsDto.setSiteid(-1);
		settingsDto.setProperty(SettingStatus.LOAD_PAGE_ROLE_DATA);
		settingsDto.setEffectivedate(null);
		settingsDto.setTodate(null);		
		if( new SettingsService().getSettingValue(settingsDto).getValue().trim().equalsIgnoreCase(SettingStatus.LOAD_PAGE_ROLE_DATA_ON)) {
			new LoadSettingsService().loadAllPageViewSettingsDefaults();
			settingsDto.setValue(SettingStatus.LOAD_PAGE_ROLE_DATA_OFF);
			new SettingsService().forceUpdateSettings(settingsDto
					);
			
		}
		}catch(Exception e) {
			;
		}
	}
	
	private String getQueryString( ServletRequest servletRequest  )
	{
		//Set<String> paramContainer = new HashSet<String>();
		StringBuilder queryString = new StringBuilder();
		  Map<String, String[]> params= servletRequest.getParameterMap();
		  Set<String> pset= params.keySet();
		  boolean isFirst=true;
		  if(pset!=null&&pset.size()>0) {
		  // System.out.println("iii");
			  for(String name:pset) {
				  // System.out.println("-->name :"+name);
				  
				  for(String value:params.get(name) ) {
					  // System.out.println("--> value :"+ value);
					     StringBuilder tocken=new StringBuilder();
	        		   tocken.append(name).append("=").append(value);
	        		   if(isFirst) {
	        			   queryString.append(tocken);
	        			   isFirst=false;
	        		   } else {
	        			   queryString.append("&").append(tocken);
	        		   }
	        		    
				  }
	      		 
			  }
		  }
		  return queryString.toString();
	}
	
	

	private String getURI(HttpServletRequest request ) {
		
		String returnString="";
		try {
			returnString = request.getAttribute("javax.servlet.forward.request_uri").toString();
			if(returnString.trim().equals("")) {
				returnString = request.getServletPath().substring(1);
			} else {
				returnString.substring(returnString.lastIndexOf("/")+1);
			}
		}catch(Exception ex) {
			returnString = request.getServletPath().substring(1); 
		}
		
		return returnString;
	}


}
