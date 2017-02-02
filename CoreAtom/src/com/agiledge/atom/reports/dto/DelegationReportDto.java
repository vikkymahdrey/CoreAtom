package com.agiledge.atom.reports.dto;

import java.sql.Date;

public class DelegationReportDto {
	
	public int id,delegatedEmployeeId,delegate_empID;
	Date from_date, to_date;
	public String displayname,personnelno,delegatedEmpDisplayname,delegatedPersonnelno;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getDelegatedEmployeeId() {
		return delegatedEmployeeId;
	}
	public void setDelegatedEmployeeId(int delegatedEmployeeId) {
		this.delegatedEmployeeId = delegatedEmployeeId;
	}
	public int getDelegate_empID() {
		return delegate_empID;
	}
	public void setDelegate_empID(int delegate_empID) {
		this.delegate_empID = delegate_empID;
	}
	public Date getFrom_date() {
		return from_date;
	}
	public void setFrom_date(Date from_date) {
		this.from_date = from_date;
	}
	public Date getTo_date() {
		return to_date;
	}
	public void setTo_date(Date to_date) {
		this.to_date = to_date;
	}
	public String getDisplayname() {
		return displayname;
	}
	public void setDisplayname(String displayname) {
		this.displayname = displayname;
	}
	public String getPersonnelno() {
		return personnelno;
	}
	public void setPersonnelno(String personnelno) {
		this.personnelno = personnelno;
	}
	public String getDelegatedEmpDisplayname() {
		return delegatedEmpDisplayname;
	}
	public void setDelegatedEmpDisplayname(String delegatedEmpDisplayname) {
		this.delegatedEmpDisplayname = delegatedEmpDisplayname;
	}
	public String getDelegatedPersonnelno() {
		return delegatedPersonnelno;
	}
	public void setDelegatedPersonnelno(String delegatedPersonnelno) {
		this.delegatedPersonnelno = delegatedPersonnelno;
	}
	
}
