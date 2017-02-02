package com.agiledge.atom.usermanagement.dto;

import java.util.ArrayList;


public class ViewManagementDto {

	private long id;
	private int roleId;
	private String viewKey;
	private String roleName;
	private String roleDescription;
	private int viewId;
	private String viewName;
	private String viewURL;
	private int viewShowOrder;
	private int subViewId;
	private String subViewName;
	private int parentId;
	private String subViewURL;
	private String subViewURLId;
	private int subViewShowOrder;
	private int roleViewAssociationId;
	private String updatedBy;
	private String viewType;
	private String viewUrlId;
	private String subViewKey;
	public String getSubViewKey() {
		return subViewKey;
	}

	public void setSubViewKey(String subViewKey) {
		this.subViewKey = subViewKey;
	}

	private ArrayList<String > roles;

	public ArrayList<String> getRoles() {
		return roles;
	}

	public void setRoles(ArrayList<String> roles) {
		this.roles = roles;
	}

	public String getViewUrlId() {
		return viewUrlId;
	}

	public void setViewUrlId(String viewUrlId) {
		this.viewUrlId = viewUrlId;
	}

	public String getViewType() {
		return viewType;
	}

	public void setViewType(String viewType) {
		this.viewType = viewType;
	}

	public String getUpdatedBy() {
		return updatedBy;
	}

	public void setUpdatedBy(String updatedBy) {
		this.updatedBy = updatedBy;
	}

	public int getRoleViewAssociationId() {
		return roleViewAssociationId;
	}

	public void setRoleViewAssociationId(int roleViewAssociationId) {
		this.roleViewAssociationId = roleViewAssociationId;
	}

	public int getViewShowOrder() {
		return viewShowOrder;
	}

	public void setViewShowOrder(int viewShowOrder) {
		this.viewShowOrder = viewShowOrder;
	}

	public int getSubViewShowOrder() {
		return subViewShowOrder;
	}

	public void setSubViewShowOrder(int subViewShowOrder) {
		this.subViewShowOrder = subViewShowOrder;
	}

	public String getSubViewName() {
		return subViewName;
	}

	public void setSubViewName(String subViewName) {
		this.subViewName = subViewName;
	}

	public int getRoleId() {
		return roleId;
	}

	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getRoleDescription() {
		return roleDescription;
	}

	public void setRoleDescription(String roleDescription) {
		this.roleDescription = roleDescription;
	}

	public int getViewId() {
		return viewId;
	}

	public void setViewId(int viewId) {
		this.viewId = viewId;
	}

	public String getViewName() {
		return viewName;
	}

	public void setViewName(String viewName) {
		this.viewName = viewName;
	}

	public String getViewURL() {
		return viewURL;
	}

	public void setViewURL(String viewURL) {
		this.viewURL = viewURL;
	}

	public int getSubViewId() {
		return subViewId;
	}

	public void setSubViewId(int subViewId) {
		this.subViewId = subViewId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getSubViewURL() {
		return subViewURL;
	}

	public void setSubViewURL(String subViewURL) {
		this.subViewURL = subViewURL;
	}

	public String getSubViewURLId() {
		return subViewURLId;
	}

	public void setSubViewURLId(String subViewURLId) {
		this.subViewURLId = subViewURLId;
	}
	
	public String getViewKey() {
		return viewKey;
	}

	public void setViewKey(String viewKey) {
		this.viewKey = viewKey;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



}
