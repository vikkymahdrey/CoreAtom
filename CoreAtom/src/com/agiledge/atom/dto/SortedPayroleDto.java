package com.agiledge.atom.dto;

 

public class SortedPayroleDto extends PayrollReportDto implements Comparable<PayrollReportDto> {

	@Override
	public int compareTo(PayrollReportDto dto) {
		// TODO Auto-generated method stub
		
		System.out.println("amount1 :" + this.getTotalAmount() + " amount two " + dto.getTotalAmount());
		return this.getTotalAmount()>dto.getTotalAmount()?1:this.getTotalAmount()==dto.getTotalAmount()?0:-1;
	}

	 
}
