package com.agiledge.atom.usermanagement.service;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.ServletContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.agiledge.atom.dao.OtherDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.SettingsDTO;
import com.agiledge.atom.service.SettingsService;
import com.agiledge.atom.settings.dto.SettingStatus;
import com.agiledge.atom.usermanagement.dao.PageDao;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementStatus;


public class PageService {

	public PageDto addPage(PageDto dto) {
		//  
		return new PageDao().addPage(dto);
	}

	public PageDto updatePage(PageDto dto) {
		// TODO Auto-generated method stub
		return new PageDao().updatePage(dto);
	}

	public ArrayList<PageDto> getAssignedPages( PageDto dto) {
		//  
		return new PageDao().getAssignedPages(dto);
		
	}
	
	public JSONArray getAssignedPagesJson( PageDto dto) throws JSONException {
		//  
		ArrayList<PageDto> dtoList = new PageDao().getAssignedPages(dto);
		JSONArray array = new JSONArray();
		for (PageDto item : dtoList) {
			JSONObject obj = new JSONObject();
			obj.put("id" , item.getId());
			obj.put("url" , item.getUrl());
			System.out.println(" item (" + item.getId() + " ) : " + item.getUrl());
			array.put(obj);
			
		}
		return array;
	}

	public int groupAssignedUrls(ArrayList<PageDto> assignedList) {
		//  
		
		return new PageDao().groupAssignedUrls(assignedList);
	}
	
	public int groupAssignedUrl( PageDto pagDto) {
		//  
		
		return new PageDao().groupAssignedUrl(pagDto);
	}
	
	public int groupAssignedUrlItself( PageDto pagDto) {
		//  
		
		return new PageDao().groupAssignedUrlItself(pagDto);
	}
	
	
	public ArrayList<String> getListOfFiles(String path) {
	//	System.out.println("Path : " + path);
		ArrayList<String> files = new ArrayList<String>();
		File dir = new File(path);
		for(File child: dir.listFiles()) {
			if(child.isFile()) {
				 String parts[]=(child.getName()).split("\\.");
				  
				 if(parts!=null&&parts.length>1&&parts[1].equals("class"))
				 {
					 String name = parts[0];
					  
					 String filters [] = {
							 "Service", "Dao","Dto", "service", "dao", "dto",  "SERVICE",
							 "DAO", "DTO"
					 };
					 boolean isNonServlet=false;
					 for(int i=0; i< filters.length; i ++) {
						 
						 if(name.indexOf(filters[i])>=0&&name.indexOf(filters[i])==(name.length()-filters[i].length()))
						 {
								isNonServlet = true;
								if(parts[0].equalsIgnoreCase("logout" )) {
									System.out.println("hahah logut "+ name.length());
									
								}
								break;
								
						 }
					 }
					 
					 if(isNonServlet==false) {
						 files.add( parts[0]);
					 }
						
						
					
				 }
			
			 
			} else if(child.isDirectory()) {
				files.addAll( getListOfFiles(child.getPath()));
			}
		}
		 
		return files;
	}

	public int setHomePage(ViewManagementDto dto) {
		return new PageDao().setHomePage(dto);
	}

	public  PageDto getPageFromUrlString(String url) {
		// TODO Auto-generated method stub
		return new PageDao().getPageFromUrlString( url);
	}
	
	public void loadPagesToDb(ServletContext application) {
		System.out.println(" App path : " + application.getRealPath("/"));
		SettingsService sService = new SettingsService();
		 
		 SettingsDTO sDto= sService.getSettingValue(SettingStatus.PROPERTY_AUTOMATIC_PAGE_ADD );
		 System.out.println("SDto " + sDto==null?"NULL" :sDto.getValue());
		String prompt = sDto.getValue();
		System.out.println(" Settings : PROPERTY_AUTOMATIC_PAGE_ADD :" + sDto.getValue());
		if(prompt!=null&&prompt.trim().equalsIgnoreCase("YES")) {
	
		try {
			OtherDao ob = OtherDao.getInstance();
			ArrayList<String> pages = new ArrayList<String>();
		
				File folder = new File(application.getRealPath("/"));
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile())
						pages.add(listOfFiles[i].getName());
				}
				ob.insertPageToDatabase(pages,ViewManagementStatus.PAGE);
			} catch (Exception e) {

				System.out.println("error" + e);
			}
		
		try {
			OtherDao ob = OtherDao.getInstance();
			ArrayList<String> pages = new ArrayList<String>();
		
				File folder = new File(application.getRealPath("/"));
				File[] listOfFiles = folder.listFiles();

				for (int i = 0; i < listOfFiles.length; i++) {
					if (listOfFiles[i].isFile())
						pages.add(listOfFiles[i].getName());
				}
				ob.insertPageToDatabase(pages,ViewManagementStatus.PAGE);
				
				
				
			} catch (Exception e) {

				System.out.println("error" + e);
			}

		// list of servlet is next
							try {
								OtherDao ob1 = OtherDao.getInstance();
			ArrayList<String>  servletList = new PageService().getListOfFiles(application.getRealPath("/WEB-INF/"));
			 ob1.insertPageToDatabase(servletList,ViewManagementStatus.SERVLET);
			 
				
				
			} catch (Exception e) {

				System.out.println("error" + e);
			}
		}

	}

	public PageDto getHomePageUrl(EmployeeDto dto)
	{
	return new PageDao().getHomePageUrl(dto);
	}

	 
	
	public boolean isWellPage(String url, String role) {
		return new PageDao().isWellPage(url, role);
	}
}
