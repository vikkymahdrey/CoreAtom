/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.SiteDto;


/**
 * 
 * @author 123
 */
public class SiteService {

	SiteDao dao = new SiteDao();

	public List<SiteDto> getSites() {
		return new SiteDao().getSites();
	}
	
	public ArrayList<SiteDto> getSites(String branchId)
	{
		return new SiteDao().getSites(branchId);
		
	}

	public int addSite(SiteDto dto) {
		return new SiteDao().addSite(dto);
	}
	

	public int updateSite(SiteDto dto) {
		return new SiteDao().updateSite(dto);
	}

	public SiteDto getSite(String siteId)
	{
		return new SiteDao().getSite(siteId);
	}

	public int addVendorToSite(String siteId, String[] vendors) {
		return new SiteDao().addVendorToSite(siteId,vendors);
	}

	public int deleteVendorsFromSite(String siteId) {
 
		return new SiteDao().deleteVendorsFromSite(siteId);
	}

}
