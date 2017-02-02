package com.agiledge.atom.dto;

import java.util.ArrayList;
import java.util.Date;

public class PayrollReportDto {

	/**
	 * @param args
	 */

	// TODO Auto-generated method stub
	private String id;
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	private String employeeID;
	private String employeeName;
	private String roll;
	private ArrayList<PayrollReportDto.DetailClass> details;
	private double totalAmount;

	public String getEmployeeID() {
		return employeeID;
	}

	public void setEmployeeID(String employeeID) {
		this.employeeID = employeeID;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public String getRoll() {
		return roll;
	}

	public void setRoll(String roll) {
		this.roll = roll;
	}

	 

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}
	
	public ArrayList<PayrollReportDto.DetailClass> getDetails() {
		return details;
	}

	public void setDetails(ArrayList<PayrollReportDto.DetailClass> details) {
		this.details = details;
	}

	public class DetailClass {
		private double amount;
		private String id;
		private String month;
		private String status;
		private String updatedBy;
		private Date updateDate;
		private String reason;
		public String getUpdatedBy() {
			return updatedBy;
		}
		public void setUpdatedBy(String updatedBy) {
			this.updatedBy = updatedBy;
		}
	 
		public String getReason() {
			return reason;
		}
		public void setReason(String reason) {
			this.reason = reason;
		}
		public double getAmount() {
			return amount;
		}
		public void setAmount(double amount) {
			this.amount = amount;
		}
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}
		public String getMonth() {
			return month;
		}
		public void setMonth(String month) {
			this.month = month;
		}
		public String getStatus() {
			return status;
		}
		public void setStatus(String status) {
			this.status = status;
		}
		public Date getUpdateDate() {
			return updateDate;
		}
		public void setUpdateDate(Date updateDate) {
			this.updateDate = updateDate;
		}
	}

}
