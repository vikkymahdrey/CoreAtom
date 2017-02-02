/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import javax.mail.internet.AddressException;

/**
 * 
 * @author 123
 */
public class Test {

	public static void main(String args[]) throws AddressException {
		MailService service = new MailService();
		service.sentEmailNearToBookingExpiry("11/14/2012");

	}

}
