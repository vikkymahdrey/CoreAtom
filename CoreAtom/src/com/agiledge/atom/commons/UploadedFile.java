
package com.agiledge.atom.commons;

import org.springframework.web.multipart.MultipartFile;

public class UploadedFile {


	 private MultipartFile file;  
	 boolean areaDupe;
	 boolean placeDupe;
	 boolean landmarkDupe;
	 private String site;
	 private String type;
	 private String branchId;
	 private boolean truncate;
	  
	  
	 public boolean isAreaDupe() {
		return areaDupe;
	}

	public void setAreaDupe(boolean areaDupe) {
		this.areaDupe = areaDupe;
	}

	public boolean isPlaceDupe() {
		return placeDupe;
	}

	public void setPlaceDupe(boolean placeDupe) {
		this.placeDupe = placeDupe;
	}

	public boolean isLandmarkDupe() {
		return landmarkDupe;
	}

	public void setLandmarkDupe(boolean landmarkDupe) {
		this.landmarkDupe = landmarkDupe;
	}

	public MultipartFile getFile() {  
	  return file;  
	 }  
	  
	 public void setFile(MultipartFile file) {  
	  this.file = file;  
	 }

	public String getSite() {
		return site;
	}

	public void setSite(String site) {
		this.site = site;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

 
	public boolean isTruncate() {
		return truncate;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}

 
	public String getBranchId() {
		return branchId;
	}

	public void setBranchId(String branchId) {
		this.branchId = branchId;
	}  

}
