/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.dao.SiteDao;
import com.agiledge.atom.dto.SiteDto;


/**
 * 
 * @author muhammad
 */
public class SiteVehicleService {

	public int setSiteVehicle(ArrayList<SiteDto> siteDtoList) {

		return new SiteDao().setSiteVehicle(siteDtoList);
	}

	public ArrayList<SiteDto> getSiteVehicle(int siteId) {

		ArrayList<SiteDto> siteDtoList = new SiteDao().getSiteVehicle(siteId);
		return siteDtoList;
	}
}
