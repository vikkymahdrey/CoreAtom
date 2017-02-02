/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dto;

/**
 * 
 * @author 123
 */
public class ScheduleMailDto extends EmployeeSubscriptionDto {
	MailDto mail = new MailDto();

	public MailDto getMail() {
		return mail;
	}

	public void setMail(MailDto mail) {
		this.mail = mail;
	}

}
