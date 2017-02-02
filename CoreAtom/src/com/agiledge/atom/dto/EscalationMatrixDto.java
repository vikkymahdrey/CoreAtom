package com.agiledge.atom.dto;

public class EscalationMatrixDto {
private String level;
private String group;
private String personId;
private String personCode;
private String personName;
private String contact;
private String email;
private String timeSlot;
public String getLevel() {
	return level;
}
public void setLevel(String level) {
	this.level = level;
}
public String getGroup() {
	return group;
}
public void setGroup(String group) {
	this.group = group;
}
public String getPersonId() {
	return personId;
}
public void setPersonId(String personId) {
	this.personId = personId;
}
public String getPersonCode() {
	return personCode;
}
public void setPersonCode(String personCode) {
	this.personCode = personCode;
}
public String getPersonName() {
	return personName;
}
public void setPersonName(String personName) {
	this.personName = personName;
}
public String getTimeSlot() {
	return timeSlot;
}
public void setTimeSlot(String timeSlot) {
	this.timeSlot = timeSlot;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getContact() {
	return contact;
}
public void setContact(String contact) {
	this.contact = contact;
}
}
