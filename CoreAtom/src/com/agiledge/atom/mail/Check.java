/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.mail;

/**
 *
 * @author 123
 */

/**
 * 
 * @author Shahid
 */
public class Check {
	public static void main(String[] args) {

		SendMail ob = SendMailFactory.getMailInstance();
		try {
			ob.send("ccnoufal@gmail.com", "NOhhhO", "asdfas");
		} catch (Exception ex) {
			System.out.println("Message" + ex);
		}
	}
}
