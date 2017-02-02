package com.agiledge.atom.usermanagement.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.UserManagementDAO;
import com.agiledge.atom.dto.UserManagementDTO;
import com.agiledge.atom.usermanagement.dao.UserManagementDao;
import com.agiledge.atom.usermanagement.dto.UserManagementDto;


public class UserManagementService {

	private String message;
	public ArrayList <UserManagementDto> getSystemUsers() {
		return new UserManagementDao().getSystemUsers();
	}
	public ArrayList <UserManagementDto> getSystemUsers(String roleid) {
		return new UserManagementDao().getSystemUsers(roleid);
	}
	public ArrayList<UserManagementDTO> GetAllRoleList() {
	return new UserManagementDAO().GetAllRoleList();
	}
	public boolean validate(UserManagementDto dto) {
		boolean flag=true;
		try {
			if(dto.getName()==null||dto.getName().trim().equals("")) {
				flag = false;
				setMessage("Name is empty");
			} else if (dto.getDescription()==null || dto.getDescription().trim().equals("")) {
				flag = false;
				setMessage("Description is empty");
			}  else if (dto.getUserType()==null || dto.getUserType().trim().equals("")) {
				flag = false;
				setMessage("Key is empty");
			}
			
		} catch (Exception e) {
			message=e.getMessage();
			flag=false;
		}
		
		return flag;
		
	}
	
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}

	public int addUserRole(UserManagementDto dto) {
		// TODO Auto-generated method stub
		dto.setType(UserManagementDao.SECONDARY_TYPE);
		int val= new UserManagementDao().addUserRole(dto);
		return  val;
	}
	
	public int updateUserRole(UserManagementDto dto) {
		 
		int val= new UserManagementDao().updateUserRole(dto);
		return  val;
	}
	
	
	
	public boolean checkUserTypeExists(UserManagementDto dto ) {
		return new UserManagementDao().checkUserTypeExists(dto);
	}

	public boolean checkUserNameExists(UserManagementDto dto) {
		// TODO Auto-generated method stub
		return new UserManagementDao().checkUserNameExists(dto);
	}
	
	public int getRoleId(String userType) {
		ArrayList<UserManagementDto> dtoList  = getSystemUsers();
		int roleId=-1;
		for(UserManagementDto dto : dtoList) {
			if(dto.getUserType().equals(userType) ) {
				roleId=dto.getId();
				break;
			}
		}
		return roleId;
	}
	
}
