package com.agiledge.atom.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.GeneralShiftDTO;

public class GeneralShiftDAO {
	public ArrayList<GeneralShiftDTO> getDeductionTypes()
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<GeneralShiftDTO> list=new ArrayList<GeneralShiftDTO>();
		String query="SELECT * FROM DEDUCTION WHERE STATUS='a'";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while(rs.next())
			{
				GeneralShiftDTO dto=new GeneralShiftDTO();
				dto.setDeductionid(rs.getString("id"));
				dto.setDeductiontype(rs.getString("type"));
				list.add(dto);
			}
		}catch (Exception e) {
			
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
			return list;
		}
	
	public int updateTodate(String from_date,String site_id)
	{
		Connection con = null;
		int result=0;
		String id="";
		PreparedStatement st=null,st1=null;
		ResultSet rs=null;
		String query="Update general_shift_config set to_date=? where id=? ";
		String checkquery="select id from general_shift_config where site_id=? and convert(varchar,from_date,103)<? order by from_date desc";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(checkquery);
			st.setString(1, site_id);
			st.setString(2, from_date);
			rs=st.executeQuery();
			if(rs.next())
			{
			id=rs.getString("id");
			from_date=OtherFunctions.changeDateFromatToIso(from_date);
			String next_date=OtherFunctions.getPreviousDay(from_date);
			next_date=OtherFunctions.changeDateFromatToIso(next_date);
			st1=con.prepareStatement(query);
			st1.setString(1,next_date);
			st1.setString(2, id);
			result=st1.executeUpdate();
			}
			}catch (Exception e) {
				
			} finally {
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(st,st1);
				DbConnect.closeConnection(con);
			}
		return result;
	}
	
	
	public int insertConfigData(GeneralShiftDTO dto,String[] shiftin,String[] shiftout)
	{
		Connection con = null;
		int result=0,config_id=0;
		PreparedStatement st=null,st1=null;
		ResultSet rs=null;
		String query="INSERT INTO GENERAL_SHIFT_CONFIG (APPROVAL_REQ,APPROVED_BY,CUTOFFDAYS,DEDUCTION,DEDUCTION_TYPE,DEDUCTION_AMT,WAITLIST_RECONF,CUTOFF_WAITLIST,CUTOFF_CANCEL,SITE_ID,STATUS,FROM_DATE) VALUES(?,?,?,?,?,?,?,?,?,?,?,?)";
		String shiftquery="INSERT INTO GENERAL_SHIFT (CONFIG_ID,LOGTIME,LOGTYPE,STATUS) VALUES(?,?,?,?)";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query,Statement.RETURN_GENERATED_KEYS);
		    st.setString(1,dto.getApproval_req());
		    st.setString(2,dto.getApproved_by());
		    st.setString(3,dto.getCutoffdays());
		    st.setString(4,dto.getDeduction());
		    st.setString(5,dto.getDeductionid());
		    st.setString(6,dto.getDeduction_amt());
		    st.setString(7,dto.getWaitlist_reconf());
		    st.setString(8, dto.getWaitlist_cutoffdays());
		    st.setString(9, dto.getCancelcutoff());
		    st.setLong(10,Long.parseLong(dto.getSite_id()));
		    st.setString(11, "a");
		    st.setString(12,OtherFunctions.changeDateFromatToIso(dto.getFrom_date()));
		    result=st.executeUpdate();
		    if(result>0)
		    {
		    	updateTodate(dto.getFrom_date(),dto.getSite_id());
		    }
		    rs=st.getGeneratedKeys();
		    while(rs.next())
		    {
		    	config_id=rs.getInt(1);
		    }
		    if(config_id>0)
		    {
		    st1=con.prepareStatement(shiftquery);
		    for(String in:shiftin)
		    {
		    st1.setInt(1,config_id);
		    st1.setString(2, in);
		    st1.setString(3, "IN");
		    st1.setString(4, "a");
		    result+=st1.executeUpdate();
		    }
		    for(String out:shiftout)
		    {
		    st1.setInt(1,config_id);
		    st1.setString(2, out);
		    st1.setString(3, "OUT");
		    st1.setString(4, "a");
		    result+=st1.executeUpdate();
		    }
		    }
		    
	}catch (Exception e) {
		
	} finally {
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(st,st1);
		DbConnect.closeConnection(con);
	}
		return result;
	}
		
	
	public String insertValidations(GeneralShiftDTO dto,String[] shiftin,String[] shiftout)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		String returnvalue="true";
		String query="select * from general_shift_config where convert(varchar,from_date,103)=? and site_id=?";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			st.setString(1, dto.getFrom_date());
			st.setString(2, dto.getSite_id());
			rs=st.executeQuery();
			if(rs.next())
			{
				returnvalue="From Date Already Exists";
			}
		if(dto.getSite_id().equals("")||dto.getSite_id().equals("select"))
		{
			returnvalue="Site Is Empty";
		}
		else if(shiftin.length==0&&shiftout.length==0)
		{
			returnvalue="Log Time Is Not Selected";
		}
		else if(dto.getFrom_date().equals(""))
		{
			returnvalue="From Date Is Empty";
		}
		else if(dto.getApproval_req().equals(""))
		{
			returnvalue="Approval Required Is Empty";
		}
		else if(dto.getCutoffdays().equals(""))
		{
			returnvalue="Cut-Off Time For Subscribing(In Days) Is Empty";
		}
		else if(dto.getCancelcutoff().equals(""))
		{
			returnvalue="Cut-Off Time For Cancelling(In Days) Is Empty";
		}
		else if(dto.getDeduction().equals(""))
		{
			returnvalue="Deduction Type Is Empty";
		}
		else if(dto.getWaitlist_reconf().equals(""))
				{
			returnvalue="Waiting List Re-Confirmation Required Is Empty";
				}}catch (Exception e) {
					
				} finally {
					DbConnect.closeResultSet(rs);
					DbConnect.closeStatement(st);
					DbConnect.closeConnection(con);
				}
		return returnvalue;
	}
	
	
	public ArrayList<GeneralShiftDTO> getallConfigurations()
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<GeneralShiftDTO> list=new ArrayList<GeneralShiftDTO>();
		String query="select id,approval_req,cutoffdays,cutoff_cancel,deduction,deduction_amt,waitlist_reconf,cutoff_waitlist,status,from_date,case when now()<from_date  then 'yes'  else  'no' end editable,(select type from deduction where id=deduction_type) as deductionname,(select name from roles where id=approved_by) as approval_name,(select site_name from site where id=site_id ) as site_name,DATE_FORMAT(from_date,'%d-%m-%Y') displaydate from general_shift_config order by from_date desc";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while(rs.next())
			{
				GeneralShiftDTO dto=new GeneralShiftDTO();
				dto.setConf_id(rs.getString("id"));
				dto.setApproval_req(rs.getString("approval_req"));
				dto.setCutoffdays(rs.getString("cutoffdays"));
				dto.setCancelcutoff(rs.getString("cutoff_cancel"));
				dto.setWaitlist_reconf(rs.getString("waitlist_reconf"));
				dto.setWaitlist_cutoffdays(rs.getString("cutoff_waitlist"));
				dto.setStatus(rs.getString("status"));
				dto.setFrom_date(rs.getString("displaydate"));
				dto.setDeduction(rs.getString("deduction"));
				dto.setDeductiontype(rs.getString("deductionname"));
				dto.setDeduction_amt(rs.getString("deduction_amt"));
				dto.setApproved_by(rs.getString("approval_name"));
				dto.setSite_id(rs.getString("site_name"));
				dto.setEditable(rs.getString("editable"));
				list.add(dto);
			}
		}catch (Exception e) {
			System.out.println("Error"+e);
			
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
			return list;
		}
	
	public GeneralShiftDTO getConfigurations(String IN_OUT,String time)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		GeneralShiftDTO dto=new GeneralShiftDTO();
		String query="select gsc.* from general_shift_config gsc,general_shift gs where gs.config_id=gsc.id and gs.logtime='"+time+"' and gs.logtype='"+IN_OUT+"' and gs.status='a' and gsc.status='a' group by gsc.id";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			if(rs.next())
			{
				
				dto.setConf_id(rs.getString("id"));
				dto.setApproval_req(rs.getString("approval_req"));
				dto.setApproved_by(rs.getString("approved_by"));
				dto.setCutoffdays(rs.getString("cutoffdays"));
				dto.setCancelcutoff(rs.getString("cutoff_cancel"));
				dto.setWaitlist_reconf(rs.getString("waitlist_reconf"));
				dto.setWaitlist_cutoffdays(rs.getString("cutoff_waitlist"));
				dto.setStatus(rs.getString("status"));				
			}
		}catch (Exception e) {
			System.out.println("Error"+e);
			
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
			return dto;
		}
	

	public ArrayList<GeneralShiftDTO> getActiveLogData(String IN_OUT)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<GeneralShiftDTO> list=new ArrayList<GeneralShiftDTO>();
		String query="Select * from general_shift where logtype='"+IN_OUT+"' and status='a'";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while(rs.next())
			{
				GeneralShiftDTO dto=new GeneralShiftDTO();
				dto.setLogtime(rs.getString("logtime"));
				list.add(dto);
			}
           }catch (Exception e) {
			System.out.println("Error"+e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
			return list;
	}
	
	public ArrayList<GeneralShiftDTO> getAssignedLogData(String id)
	{
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		ArrayList<GeneralShiftDTO> list=new ArrayList<GeneralShiftDTO>();
		String query="Select * from general_shift where config_id=?";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			st.setString(1, id);
			rs = st.executeQuery();
			while(rs.next())
			{
				GeneralShiftDTO dto=new GeneralShiftDTO();
				dto.setLogtime(rs.getString("logtime"));
				dto.setLogtype(rs.getString("logtype"));
				list.add(dto);
			}
           }catch (Exception e) {
			
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
			return list;
	}
	
	public int deleteConfig(String id)
	{
		Connection con = null;
		PreparedStatement st = null;
		int result=0;
		String query="Delete from general_shift_config where id=?";
		try {
			DbConnect  db = DbConnect.getInstance();
			con = db.connectDB();
			st = con.prepareStatement(query);
			st.setString(1, id);
			result = st.executeUpdate();
		}catch (Exception e) {
				
			} finally {
				DbConnect.closeStatement(st);
				DbConnect.closeConnection(con);
			}
				return result;
	}
}
