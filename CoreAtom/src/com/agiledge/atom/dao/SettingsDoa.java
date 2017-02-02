/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.SettingsDTO;

/**
 * 
 * @author 123
 */
public class SettingsDoa {

	public double getTransportationCost() {
		double amount = -1;
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		try {
			String query = "select transportationCost from settings";

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				amount = rs.getDouble(1);
			}
		} catch (Exception e) {
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);
		}

		return amount;
	}

	public String getAccountingDate() {
		String date = "";
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			String query = "select accountingDate from settings";

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				date = "" + rs.getInt(1);
				System.out.println("Accounting date : " + date);

			}
			

		} catch (Exception e) {
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}

		return date;
	}

	// accounting date
	// get accounting date of month and year
	public String getAccountingDate(String month, String year) {
		String date = "";
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			String query = "select payrollDate from payroll where payrollDate between '"
					+ month
					+ "/1/"
					+ year
					+ "' and (select dateadd(s,-1, dateadd(mm, datediff(m,0,getdate()) + 1,0))) ";
			String query1 = "select accountingDate from settings";

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				date = "" + rs.getInt(1);
				// System.out.println("Accounting date : " + date);
			} else {
				ResultSet rs1 = st.executeQuery(query1);
				if (rs1.next()) {
					date = "" + rs1.getInt(1);
					// System.out.println("Accounting date : " + date);

				} else {
					date = "";
				}
				DbConnect.closeResultSet(rs1);
			}

			

		} catch (Exception e) {
			date = "";
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}

		return date;
	}

	public String getSourceEmailAddress() throws Exception {

		String email = "";
		DbConnect ob = DbConnect.getInstance();
		// System.out.println("In getSourceEmailAddress in SettingsDao");
		Connection con = ob.connectDB();
		Statement st =null;
		ResultSet rs = null;
		try {
			String query = "select sourceMail from settings";

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				email = "" + rs.getString(1);
				// System.out.println("Source Mail : " + email);
			}

			

		} catch (Exception e) {
			email = "";
			DbConnect.closeConnection(con);
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);

			throw e;
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}

		return email;

	}

	public static Date getUtilityLastUpdatedDate() {

		java.util.Date returnDate = null;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		try {
			String query = "select utilityLastUpdatedDate from settings";

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				// System.out.println("dATE iN DB"+rs.getDate(1));
				returnDate = rs.getDate(1);

			}

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		} catch (Exception e) {
			returnDate = null;
			DbConnect.closeConnection(con);
			System.out.println("Error in SettingDao getTransportationCost : e "
					+ e);

		}

		return returnDate;

	}

	public String[] getUtilityRunningTime() {
		String returnTime[] = null;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		try {
			String query = "select utilityRunningTime from settings";

			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery(query);
			if (rs.next()) {
				returnTime = rs.getString(1).split(":");

			}

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		} catch (Exception e) {
			returnTime = null;
			DbConnect.closeConnection(con);
			System.out.println("Error in SettingDao getUtilityRunningTime : e "
					+ e);

		}

		return returnTime;

	}

	public int setUtilityLastUpdatedDate(String curDate) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		try {
			String query = "update settings set utilityLastUpdatedDate='"
					+ curDate + "'";

			Statement st = con.createStatement();
			returnInt = st.executeUpdate(query);

			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		} catch (Exception e) {
			returnInt = 0;
			DbConnect.closeConnection(con);
			System.out
					.println("Error in SettingDao setUtilityLastUpdatedDate : e "
							+ e);

		}

		return returnInt;

	}

	public String getDomainName() {
		String data = "";
		DbConnect ob = DbConnect.getInstance();

		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			String query = "select domainName from settings";

			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				data = "" + rs.getString(1);
				// System.out.println(" DomainName   : " + data);

			}

		} catch (Exception e) {
			System.out.println("Error in  getDomainName   : e " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return data;
	}
	
	public int addSettings(SettingsDTO dto)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement disableSt = null;
		PreparedStatement deleteSt = null;
		
	 	ResultSet rs = null;
		int returnInt=0;
		String id = null;
		try{
			String type ="SINGLE";
			if(dto.getProjectid()>0 || OtherFunctions.isEmpty( dto.getSite ())==false) {
				type = "MULTIPLE";
				
			}
			 System.out.println("type : "+ type);
			con=ob.connectDB();
			con.setAutoCommit(false);
			SettingsDTO curDto = new SettingsDTO();
			curDto.setProperty(dto.getProperty());
			curDto.setSite(dto.getSite());
			curDto.setProjectid(dto.getProjectid());
			System.out.println("her 111");
			curDto = getSettingsValue(curDto);
			System.out.println("her 222");
			if(curDto!=null) {
				System.out.println("Update.....");
				disableSt = con.prepareStatement("update SETTINGS_TABLE set todate=? where id = ?");
				System.out.println("update SETTINGS_TABLE set todate='" + dto.getEffectivedate().getTime() + "' where id =" + curDto.getId());
				disableSt.setDate(1, new java.sql.Date(dto.getEffectivedate().getTime()));
				disableSt.setLong(2, curDto.getId());
				disableSt.executeUpdate();
				
				
			}
			
			java.sql.Date effectiveDate = new java.sql.Date(dto.getEffectivedate().getTime());
			System.out.println("...............");
			deleteFutureSettings(dto, con);
			System.out.println("...............");
			st=con.prepareStatement("INSERT INTO SETTINGS_TABLE ( FROMDATE,TODATE,PROPERTY,VALUE, MODULE, TYPE) VALUES(  ?,?,?,?,?,?)",Statement.RETURN_GENERATED_KEYS);
			int loc=0;
			
			java.sql.Date toDate = null;
			if(dto.getTodate()!=null) {
				toDate = new java.sql.Date(dto.getTodate().getTime());
			}
		 	st.setDate(++loc,   effectiveDate);
			st.setDate(++loc,  toDate);
			st.setString(++loc, dto.getProperty());
			st.setString(++loc,dto.getValue());
			st.setString(++loc, dto.getModule());
			 
				st.setString(++loc, type );
			 
		    returnInt=st.executeUpdate();
		    if(returnInt>0) {
		    	rs = st.getGeneratedKeys();
		    	rs.next();
		    	id = rs.getString(1);		
		    }
			if(OtherFunctions.isEmpty( dto.getSite ())==false ) {
				System.out.println("insert into site_settings (site, settingsId) values ("+ dto.getSite() + ","+id+") " );
				DbConnect.closeStatement(st);
				st = con.prepareStatement("insert into site_settings (site, settingsId) values ("+ dto.getSite() + ","+id+") ");
				returnInt =st.executeUpdate();
			}
			if(dto.getProjectid()>0 && OtherFunctions.isEmpty( dto.getSite ())==false) {
				DbConnect.closeStatement(st);
				st = con.prepareStatement("insert into project_settings (projectId, settingsId) values ("+ dto.getProjectid()  + ","+id+") ");
				returnInt =st.executeUpdate();
			}
			
			con.commit();
			con.setAutoCommit(true);
		}catch (Exception e) {
			System.out.println("Exception : "+ e);
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st, disableSt, deleteSt);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}
	
	private int deleteFutureSettings(SettingsDTO dto , Connection con) {
		PreparedStatement pre = null;
		PreparedStatement deleteSt = null;
		ResultSet rs = null;
		try {
			String type ="SINGLE";
			if(dto.getProjectid()>0 || OtherFunctions.isEmpty( dto.getSite ())==false) {
				type = "MULTIPLE";
				
			}
			java.sql.Date effectiveDate = new java.sql.Date(dto.getEffectivedate().getTime());
			String siteQuery = " and ss.site is null";
			if(OtherFunctions.isEmpty(dto.getSite())==false) {
				siteQuery = " and ss.site=" + dto.getSite();
			}
			pre = con.prepareStatement("select *  from SETTINGS_TABLE st left join site_settings ss on ss.settingsId=st.id where st.fromDate>=? and st.property=? and st.module=? and st.type=? " + siteQuery);
			System.out.println("select *  from SETTINGS_TABLE st left join site_settings ss on ss.settingsId=st.id where st.fromDate>='"+dto.getEffectivedate()+"' and st.property='"+dto.getProperty()+"' and st.module='"+dto.getModule()+"' and st.type='"+type+"' " + siteQuery);
			pre.setDate(1, effectiveDate);
			pre.setString(2, dto.getProperty());
			pre.setString(3, dto.getModule());
			pre.setString(4, type);
			
			rs = pre.executeQuery();
			
			
			 
			
			int val=0;
			while(rs.next()) {
				long id = rs.getLong("id");
				System.out.println("id : " + id);
				deleteSt = con.prepareStatement("delete from SETTINGS_TABLE where id = ?");
				deleteSt.setLong(1, id);
				val +=deleteSt.executeUpdate();
				System.out.println("val : " + val);
			}
			 
			
			return val;
			
			
		}catch(Exception e) {
			return 0;
		}
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pre, deleteSt);
			 

		}
		
	}
	
	public ArrayList<SettingsDTO> getSettings(String property)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		ArrayList<SettingsDTO> returndto= new ArrayList<SettingsDTO>();
		String query="Select * from settings_table where property='"+property+"'";
		try{
			if(property.equals("All"))
		      {
				query="Select * from settings_table";
			  }
			con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				SettingsDTO dto=new SettingsDTO();
				dto.setId(rs.getInt("id"));
				//dto.setSiteid(rs.getInt("site"));
				//dto.setProjectid(rs.getInt("project"));
				dto.setEffectivedate(rs.getDate("fromDate") );
				dto.setTodate(rs.getDate("toDate") );
				dto.setProperty(rs.getString("property"));
				dto.setAmount(rs.getString("value"));
				dto.setValue(rs.getString("value"));
				returndto.add(dto);
			}
			
			
		}catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returndto;
	}

	
	
	public boolean checkDate(String fromDate,String toDate,int projectid,int siteid)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		boolean flag=false;
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String query="select * from settings_table where ((toDate between '"+OtherFunctions.changeDateFromatToIso(fromDate)+"' and '"+OtherFunctions.changeDateFromatToIso(toDate)+"') or (fromDate between '"+OtherFunctions.changeDateFromatToIso(fromDate)+"' and '"+OtherFunctions.changeDateFromatToIso(toDate)+"')) and (project="+projectid+" or project=0) and site="+siteid;
			rs=st.executeQuery(query);
			if(rs.next())
			{
				flag=true;
			}
		  }catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return flag;
	}
	
	
	
	public int deleteSetting(int deleteId)
	{
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		int result=0;
		try{
			con=ob.connectDB();
			st=con.createStatement();
			result=st.executeUpdate("Delete from settings_table where id="+deleteId);
			}catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	public String getSiteSetting(String siteId, String property) {
		 
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		String returnString =null;
		 
		String query="select * from  settings_table s join site_settings st on s.id=st.settingsId where site="+siteId+" and s.property='"+property+"';";
		try{
		 	con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			if(rs.next())
			{
				returnString = rs.getString("value");
				 
		 	}
			
			
		}catch (Exception e) {
		 		 System.out.println("Errror in getSetting site wise "+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnString;

	}


	
	public ArrayList<SettingsDTO> getSettingsValues(SettingsDTO pDto)
	{
		DbConnect ob = DbConnect.getInstance();
		
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		String value="";
		 ArrayList<SettingsDTO> dtoList  = new ArrayList<SettingsDTO>();
		String fromDateQuery = "";
		String toDateQuery = " ";
		String siteQuery=" and ss.site is null ";
		 
		if(pDto.getEffectivedate()!=null&&pDto.getEffectivedate().equals("")==false) {
			fromDateQuery = " and s.fromDate<="+ OtherFunctions.changeDateFromat(  pDto.getEffectivedate());
		}
		if(pDto.getTodate()!=null&&pDto.getTodate().equals("")==false) {
			toDateQuery = " and s.fromDate>"+OtherFunctions.changeDateFromat(  pDto.getTodate());
		}
		if(pDto.getSiteid()>0) {
			siteQuery = " and ss.site=" + pDto.getSiteid();  
				 
		}
		String moduleQuery="";
		if(OtherFunctions.isEmpty(pDto.getModule())==false) {
			moduleQuery = " and s.module='" + pDto.getModule() + "' ";
		}
		String query="Select s.*, ss.site, st.site_name from settings_table s left join site_settings ss on s.id=ss.settingsId left join site st on ss.site = st.id where s.property='"+pDto.getProperty()+"' " + fromDateQuery + " " + toDateQuery + " " +
		siteQuery + " " + moduleQuery + "  order by s.id desc  ";
		System.out.println(query);
		try{
			 
			con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			while(rs.next())
			{
				 
				SettingsDTO	dto=new SettingsDTO();
					
					dto.setId(rs.getInt("id"));
					try {
					dto.setSiteid(rs.getInt("site"));
					dto.setSite(rs.getString("site_name"));
					}catch(Exception e) {
						dto.setSiteid(-1);
						dto.setSite("");
					}
			//		dto.setProjectid(rs.getInt("project"));
					System.out.println(".......");
					System.out.println(" from date : " + rs.getDate("fromDate"));
					System.out.println(" to date : " + rs.getDate("toDate"));
					dto.setEffectivedate(rs.getDate("fromDate") );
						 
					dto.setTodate(rs.getDate("toDate") );
				
					dto.setProperty(rs.getString("property"));
				 
					dto.setAmount(rs.getString("value"));
				 
					dto.setValue(rs.getString("value"));
					
				 dtoList.add(dto);
			
				 
			}
			
			
		}catch (Exception e) {
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}
	
	public SettingsDTO getSettingsValue(SettingsDTO pDto)
	{
		DbConnect ob = DbConnect.getInstance();
		SettingsDTO dto=null;
		Connection con = null;
		Statement st = null;
		ResultSet rs=null;
		String value="";
		 
		String fromDateQuery = "";
		String toDateQuery = " and s.toDate is null ";
		String siteQuery=" and ss.site is null ";
		if(pDto.getEffectivedate()!=null&&pDto.getEffectivedate().equals("")==false) {
			fromDateQuery = " and s.fromDate<="+ OtherFunctions.changeDateFromat(  pDto.getEffectivedate());
		}
		if(pDto.getTodate()!=null&&pDto.getTodate().equals("")==false) {
			toDateQuery = " and s.fromDate>"+OtherFunctions.changeDateFromat(  pDto.getTodate());
		}
		if(pDto.getSiteid()>0) {
			siteQuery = " and ss.site=" + pDto.getSiteid();  
				 
		}
		String moduleQuery="";
		if(OtherFunctions.isEmpty(pDto.getModule())==false) {
			moduleQuery = " and s.module='" + pDto.getModule() + "' ";
		}
		String query="Select s.*, ss.site from settings_table s left join site_settings ss on s.id=ss.settingsId where s.property='"+pDto.getProperty()+"' " + fromDateQuery + " " + toDateQuery + " " +
		siteQuery + " " + moduleQuery + "  order by s.id desc  ";
		try{
			 
			con=ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(query);
			if(rs.next())
			{
				 	dto=new SettingsDTO();
					
					dto.setId(rs.getInt("id"));
					try {
					dto.setSiteid(rs.getInt("site"));
					}catch(Exception e) {
						dto.setSiteid(-1);
					}
					dto.setEffectivedate(rs.getDate("fromDate") );
						 
					dto.setTodate(rs.getDate("toDate") );
				
					dto.setProperty(rs.getString("property"));
				 
					dto.setAmount(rs.getString("value"));
				 
					dto.setValue(rs.getString("value"));
				 
			
				 
			}
			
			
		}catch (Exception e) {
			System.out.println("IN CATCH"+e);

		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dto;
	}


	public int forceUpdateSettings(SettingsDTO pDto) {
		// TODO Auto-generated method stub
		DbConnect ob = DbConnect.getInstance();
		SettingsDTO dto=null;
		Connection con = null;
		Statement st = null;
		int returnInt =0;
		String value="";
		 
		String fromDateQuery = "";
		String toDateQuery = " and s.toDate is null ";
		String siteQuery=" and ss.site is null ";
		if(pDto.getEffectivedate()!=null&&pDto.getEffectivedate().equals("")==false) {
			fromDateQuery = " and s.fromDate<="+pDto.getEffectivedate();
		}
		if(pDto.getTodate()!=null&&pDto.getTodate().equals("")==false) {
			toDateQuery = " and s.fromDate>"+pDto.getTodate();
		}
		if(pDto.getSiteid()>0) {
			siteQuery = " and ss.site=" + pDto.getSiteid();  
				 
		}
		String query=" update settings_table s left join site_settings ss on s.id=ss.settingsId   set value='OFF'  where s.property='"+pDto.getProperty()+"' " + fromDateQuery + " " + toDateQuery + " " +
		siteQuery + " ";
		System.out.println(query);
		try{
			 
			con=ob.connectDB();
			
			con.setAutoCommit(false);
			con.rollback();
			st=con.createStatement();
			System.out.println(" ........ ");
			returnInt=st.executeUpdate(query); 
			con.commit();
			System.out.println("++++++++++++++++++++++++++++++++++++++++++++++++++++");
		}catch (Exception e) {
			System.out.println("Error in forceupdate : " + e);
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			 
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
 
		return returnInt;
	}





	public ArrayList<SettingsDTO> getSettingsStrings(String module,
			String subModule) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		con=ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<SettingsDTO> returnDtoList = new ArrayList<SettingsDTO>();
		String query = "Select * from settingsString   where module='" + module
				+ "' and submodule='" + subModule + "'";
		 System.out.println(query);
		try {
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				SettingsDTO dto = new SettingsDTO();
				dto.setId(rs.getInt("id"));
				dto.setKeyValue(rs.getString("keyVal"));
				dto.setValue(rs.getString("Val"));
				dto.setModule(rs.getString("module"));
				dto.setSubModule(rs.getString("subModule"));
				dto.setStatus(rs.getString("status"));
				returnDtoList.add(dto);
			}

		} catch (Exception e) {						
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return returnDtoList;
	
}

public ArrayList<SettingsDTO> getSettingsStrings(String module,
		String subModule, String siteId, String projectUnit) {
	DbConnect ob = DbConnect.getInstance();
	Connection con = null;
	con = ob.connectDB();
	PreparedStatement pst = null;
	ResultSet rs = null;
	ArrayList<SettingsDTO> returnDtoList = new ArrayList<SettingsDTO>();
	String query = "Select ss.*,at.id as adhocId,at.pickdrop as pickDrop from settingsString  ss left outer join adhoctypes at on ss.keyVal=at.type and at.site="
			+ siteId
			+ " and (at.projectUnit='all' or at.projectUnit='"
			+ projectUnit
			+ "') where module='"
			+ module
			+ "' and submodule='" + subModule + "'";
	try {
		System.out.println(query);
		pst = con.prepareStatement(query);
		rs = pst.executeQuery();
		while (rs.next()) {
			SettingsDTO dto = new SettingsDTO();
			dto.setId(rs.getInt("id"));
			dto.setKeyValue(rs.getString("keyVal"));
			dto.setValue(rs.getString("Val"));
			dto.setModule(rs.getString("module"));
			dto.setSubModule(rs.getString("subModule"));
			dto.setStatus(rs.getString("status"));
			dto.setModuleId(rs.getString("adhocId"));
			dto.setPickDrop(rs.getString("pickDrop"));
			returnDtoList.add(dto);
		}

	} catch (Exception e) {
		e.printStackTrace();
	} finally {
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);

	}
	return returnDtoList;
}
}
