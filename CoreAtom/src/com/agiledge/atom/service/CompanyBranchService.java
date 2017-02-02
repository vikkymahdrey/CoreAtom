package com.agiledge.atom.service;

import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.dto.BranchDto;
import com.agiledge.atom.dto.CompanyDto;


public class CompanyBranchService {

	public int addCompany(CompanyDto dto) {
		// TODO Auto-generated method stub
		return new com.agiledge.atom.dao.CompanyDao().addCompany(dto);
	}
	public CompanyDto getCompany() throws SQLException {
		// TODO Auto-generated method stub
		return new com.agiledge.atom.dao.CompanyDao().getCompany();
	}
	public int modifyCompany(CompanyDto dto) {
		return new com.agiledge.atom.dao.CompanyDao().modifyCompany(dto);
	}
	public int addBranch(BranchDto dto) {
		// TODO Auto-generated method stub
		return new com.agiledge.atom.dao.BranchDao().addBranch(dto);
	}
	public ArrayList<BranchDto> getBranches(String companyId) throws SQLException {
		return new com.agiledge.atom.dao.BranchDao().getBranches(companyId);
	}
	public int modifyBranch(BranchDto dto) {
		return new com.agiledge.atom.dao.BranchDao().modifyBranch(dto);
	}

}

	