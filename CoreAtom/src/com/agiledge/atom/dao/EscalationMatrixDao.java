package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EscalationMatrixDto;


public class EscalationMatrixDao {

	public int setEscalationMatrix(
			ArrayList<EscalationMatrixDto> escalationMatrixDtos) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		try {
			pst=con.prepareStatement("truncate table escalationmatrix");
			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into escalationmatrix(level,escalationGroup,personId,timeSlot) values(?,?,?,?)");			
			for (EscalationMatrixDto dto : escalationMatrixDtos) {
				pst.setString(1, dto.getLevel());
				pst.setString(2, dto.getGroup());
				pst.setString(3, dto.getPersonId());
				pst.setString(4, dto.getTimeSlot());
				retVal += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}
	public ArrayList<EscalationMatrixDto> getEscalationMatrix() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs=null;
		Connection con = ob.connectDB();
		ArrayList<EscalationMatrixDto> escalationMatrixDtos=new ArrayList<EscalationMatrixDto>();
		EscalationMatrixDto dto=null;
		try {
			
			String query="select e.displayname as name,e.emailaddress as email,e.contactnumber1 as contact, em.personId,em.level,em.escalationGroup,em.timeSlot from escalationMatrix em,employee e where e.id=em.personId order by timeSlot";
			//query="select em.personId,innerTable.Name,innerTable.email,innerTable.contact,em.level,em.escalationGroup,em.timeSlot from escalationMatrix em,(select id as ID,displayname Name,EmailAddress as email,contactnumber1 as contact, 'admin' as escalationGroup  from employee union select id as ID,name as Name,email as email,contactnumber as contact,'vendor' as escalationGroup from vendor union select id as ID,name as Name,email as email,phone as contact,'security' as escalationGroup from escort ) as innerTable  where em.personId=innerTable.id  and em.escalationGroup=innerTable.escalationGroup order by level,escalationgroup";
			pst = con
					.prepareStatement(query);
			rs=pst.executeQuery();
			while(rs.next())
			{
				dto=new EscalationMatrixDto();
				dto.setPersonId(rs.getString("personId"));
				dto.setPersonName(rs.getString("Name"));
				dto.setContact(rs.getString("contact"));
				dto.setEmail(rs.getString("email"));
				dto.setLevel(rs.getString("level"));
				dto.setGroup(rs.getString("escalationGroup"));
				dto.setTimeSlot(rs.getString("timeSlot"));
				escalationMatrixDtos.add(dto);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return escalationMatrixDtos;
	}
	public ArrayList<EscalationMatrixDto> getEscalationContactWithTimeSlot() {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs=null;
		Connection con = ob.connectDB();
		ArrayList<EscalationMatrixDto> escalationMatrixDtos=new ArrayList<EscalationMatrixDto>();
		EscalationMatrixDto dto=null;
		try {
			pst = con
					.prepareStatement("select e.displayname as name,e.emailaddress as email,e.contactnumber1 as contact, em.personId,em.level,em.escalationGroup,em.timeSlot from escalationMatrix em,employee e where e.id=em.personId order by timeSlot");
			rs=pst.executeQuery();
			while(rs.next())
			{
				dto=new EscalationMatrixDto();
				dto.setPersonId(rs.getString("personId"));
				dto.setPersonName(rs.getString("Name"));
				dto.setContact(rs.getString("contact"));
				dto.setEmail(rs.getString("email"));
				dto.setLevel(rs.getString("level"));
				dto.setGroup(rs.getString("escalationGroup"));
				dto.setTimeSlot(rs.getString("timeSlot"));
				escalationMatrixDtos.add(dto);
			}
		} catch (Exception e) {
			System.out.println("error" + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return escalationMatrixDtos;
	}
	
}
