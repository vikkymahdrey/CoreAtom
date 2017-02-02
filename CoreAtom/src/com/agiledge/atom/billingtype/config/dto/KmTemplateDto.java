package com.agiledge.atom.billingtype.config.dto;

import java.util.ArrayList;

import com.agiledge.atom.dto.APLDto;

public class KmTemplateDto {

	private String templateName;
	private String doneBy;
	private String site;
	private String billingRefId;
	public String getSite() {
		return site;
	}


	public void setSite(String site) {
		this.site = site;
	}


	private String fileName;
	private ArrayList<KmTemplateChildDto> childList = new ArrayList<KmTemplateChildDto>();
	
	public String getTemplateName() {
		return templateName;
	}


	public void setTemplateName(String templateName) {
		this.templateName = templateName;
	}


	public long getTemplateId() {
		return templateId;
	}


	public void setTemplateId(long templateId) {
		this.templateId = templateId;
	}


	public String getFileName() {
		return fileName;
	}


	public void setFileName(String fileName) {
		this.fileName = fileName;
	}


	public ArrayList<KmTemplateChildDto> getChildList() {
		return childList;
	}


	public void setChildList(ArrayList<KmTemplateChildDto> childList) {
		this.childList = childList;
	}


	public String getDoneBy() {
		return doneBy;
	}


	public void setDoneBy(String doneBy) {
		this.doneBy = doneBy;
	}


	public String getBillingRefId() {
		return billingRefId;
	}


	public void setBillingRefId(String billingRefId) {
		this.billingRefId = billingRefId;
	}


	private long templateId;
	
	
	public class KmTemplateChildDto {
		private long id;

		private  long landmark;
		private String landmarkName;
		private float distance;
		private APLDto aplDto;
		
		public  KmTemplateChildDto() {
			aplDto = new APLDto(); 
		}
		
		public APLDto getAplDto() {
			return aplDto;
		}
		public void setAplDto(APLDto aplDto) {
			this.aplDto = aplDto;
		}
		public long getId() {
			return id;
		}
		public void setId(long id) {
			this.id = id;
		}
		public long getLandmark() {
			return landmark;
		}
		public void setLandmark(long landmark) {
			this.landmark = landmark;
		}
		public String getLandmarkName() {
			return landmarkName;
		}
		public void setLandmarkName(String landmarkName) {
			this.landmarkName = landmarkName;
		}
		public float getDistance() {
			return distance;
		}
		public void setDistance(float distance) {
			this.distance = distance;
		}
		 
	}
}
