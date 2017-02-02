package com.agiledge.atom.usermanagement.service;

import java.util.ArrayList;

import javax.servlet.ServletContext;

import com.agiledge.atom.dao.UserManagementDAO;
import com.agiledge.atom.dto.UserManagementDTO;
import com.agiledge.atom.usermanagement.dao.ViewManagementDao;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;


public class ViewManagementService {
	private ViewManagementDao dao = new ViewManagementDao();
	private String message="";
	public ArrayList<ViewManagementDto> getRoleList() {
		ArrayList<ViewManagementDto> list = dao.GetAllRoleList();
		return list;
	}
	public ArrayList<ViewManagementDto> getEmployeeHeirarchy() {
		ArrayList<ViewManagementDto> list = dao.getEmployeeHeirarchy();
		return list;
	}
	
	public String AddRole(ViewManagementDto dto) {
		boolean result;
		String returnString;
		result = dao.isRoleNameexists(dto.getRoleName());
		if (result == false) {
			int resultdao = dao.AddRole(dto);
			if (resultdao == 1)
				returnString = "Role Created Successfully";
			else
				returnString = "Role Creation Failed";
		} else
			returnString = "Role Name Already Exists";
		return returnString;
	}

	public ArrayList<ViewManagementDto> getViewsbyRole(int roleId) {
		ArrayList<ViewManagementDto> dtoList = dao.getViewsbyRole(roleId);
		return dtoList;
	}

	public ArrayList<ViewManagementDto> getSubviewsbyView(int viewId) {
		ArrayList<ViewManagementDto> dtoList = dao.getSubviewsbyView(viewId);
		return dtoList;
	}

	public int UpdateRole(ViewManagementDto dto) {
		int result = dao.UpdateRole(dto);
		return result;
	}

	public int DeleteRole(int roleId) {
		int result = dao.DeleteRole(roleId);
		return result;
	}

	public int DeleteRoleViewAssociation(String actualId,int roleId, int viewId) {
		//int resultDelete = dao.deleteViewAccessRole(String.valueOf(viewId), roleId);
		int result = dao.DeleteRoleViewAssociation(actualId, roleId, viewId);
		if(result>0) {
			
		}
		return result;
	}

	public int deleteSubview(int subviewId) {
		int result = dao.deleteSubview(subviewId);
		return result;
	}

	public int UpdateSubView(ViewManagementDto dto) {
		int result = dao.UpdateSubView(dto);
		this.setMessage(dao.getMessage());
		return result;
	}

	public int AddSubView(ViewManagementDto dto) {
		int result = dao.AddSubView(dto);
		this.message = dao.getMessage();
		return result;
	}

	public ArrayList<ViewManagementDto> getAllRootViewList() {
		ArrayList<ViewManagementDto> viewList = new ArrayList<ViewManagementDto>();
		viewList = dao.getAllRootViewList();
		return viewList;
	}

	public int DeleteView(int viewId) {
		int result = dao.DeleteView(viewId);
		return result;
	}

	public int UpdateView(ViewManagementDto dto) {
		int result = dao.UpdateView(dto);
		this.message = dao.getMessage();
		return result;
	}

	public int addView(ViewManagementDto dto) {
		int result = dao.addView(dto);
		this.message = dao.getMessage();
		return result;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public ArrayList<ViewManagementDto> roleViewExisting(int roleId) {
		ArrayList<ViewManagementDto> viewList = new ArrayList<ViewManagementDto>();
		viewList = dao.roleViewExisting(roleId);
		return viewList;
	}

	public int AddRoleViewAssociation(String viewId[], int roleId) {
		int result = dao.AddRoleViewAssociation(viewId, roleId);
		return result;
	}
	
	public ArrayList<ViewManagementDto> getAllPages() {
		return new ViewManagementDao().getAllPages();
	}

	public ArrayList<ViewManagementDto> getPagesOnly() {
		return new ViewManagementDao().getPagesOnly();
	}

	public ArrayList<ViewManagementDto> getServletsOnly() {
		return new ViewManagementDao().getServletOnly();
	}

	public ArrayList<ViewManagementDto> getViewPages() {
		return new ViewManagementDao().getViewPages();
	}
 
	public boolean resetView(String viewKey, ServletContext servletContext) {
		boolean val=false;
		try {
		ArrayList<UserManagementDTO> dtoList = new UserManagementDAO().GetAllRoleList();
		for(UserManagementDTO roleDto : dtoList) {
			String menuName = String.valueOf(roleDto.getRoleId()).concat("-menu");
			servletContext.removeAttribute(menuName);
		}
		val=true;
		} catch(Exception e) {
			System.out.println("Error in resetting view :"+ e);
		}
		return val;
		
	} 


}
