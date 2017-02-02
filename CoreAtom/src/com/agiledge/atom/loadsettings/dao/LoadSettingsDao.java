package com.agiledge.atom.loadsettings.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementStatus;

public class LoadSettingsDao {
	
	
	public int loadPage_Default(ArrayList<PageDto> pageList) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			if(pageList!=null&&pageList.size()>0) {
				for(PageDto dto : pageList) {
			stmt = con.prepareStatement("insert into page (url, type ) values (?, ?)");
			stmt.setString(1, dto.getUrl());
			stmt.setString(2, dto.getUrlType());
		 	retVal+=stmt.executeUpdate();
			
		 	
			}
				con.commit();
			}
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in loadPage_Default in LoadSettingsDao : " +e);
		}
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}


	public int setHomePage_Default(ArrayList<PageDto> pageList) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			if(pageList!=null&&pageList.size()>0) {
				for(PageDto dto : pageList) {

			stmt = con.prepareStatement("insert into user_home_page ( url , role ) 	  select  id , ? role from page where url=? ");
			stmt.setString(1, dto.getUserType());
			stmt.setString(2, dto.getUrl());
		 	retVal+=stmt.executeUpdate();
			
		 	
			}
				con.commit();
			}
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in setHomePage_Default in LoadSettingsDao : " +e);
		}
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}

	

	
	public int loadViews_Default(ArrayList<ViewManagementDto> pageList) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			if(pageList!=null&&pageList.size()>0) {
				for(ViewManagementDto dto : pageList) {
				stmt = con.prepareStatement("insert into views (view_name, show_order, pageId, type, viewKey) 	 select ?, ?, id, ?, ? from page where url =? ");
				stmt.setString(1, dto.getViewName());
				stmt.setInt(2, dto.getViewShowOrder());
				stmt.setString(3, ViewManagementStatus.ROOT);
				stmt.setString(4, dto.getViewKey());
				stmt.setString(5, dto.getViewURL());
				
			 	retVal+=stmt.executeUpdate();
			 	if(dto.getRoles()!=null&& dto.getRoles().size()>0) {
			 		ArrayList<ViewManagementDto> dtoList  = new ArrayList<ViewManagementDto>();
			 		for(String role : dto.getRoles()) {
			 			ViewManagementDto viewDto = new ViewManagementDto ();
			 			viewDto.setRoleName(role);
			 			viewDto.setViewName(dto.getViewName());
			 			dtoList.add(viewDto);
			 		}
			 		loadRolesViewsAssociation_Default(dtoList);
			 		 
			 	}
				
			 	
				}
				con.commit();
				 
			}
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in loadSubViews_Default in LoadSettingsDao : " +e);
		}
		
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}
	
	
	public int loadSubViews_Default(ArrayList<ViewManagementDto> pageList) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			if(pageList!=null&&pageList.size()>0) {
				for(ViewManagementDto dto : pageList) {
			stmt = con.prepareStatement("insert into views ( view_name , show_order, parent_id, type, pageId, viewKey ) 	 " +
					"select ? view_name, ? show_order, pv.id parent_id, ? type, p.id pageId, ? from views pv, page p " +
					"where pv.view_name =? and p.url=?");
			stmt.setString(1, dto.getSubViewName());
			stmt.setInt(2, dto.getSubViewShowOrder());
			stmt.setString(3, ViewManagementStatus.CHILD);
			stmt.setString(4, dto.getSubViewKey());
			stmt.setString(5, dto.getViewName());
			stmt.setString(6, dto.getSubViewURL());
		 	retVal+=stmt.executeUpdate();
			
		 	
			}
				con.commit();
			}
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in loadSubViews in LoadSettingsDao : " +e);
		}
		
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}
	
	
	public int loadRolesViewsAssociation_Default(ArrayList<ViewManagementDto> viewList) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			if(viewList!=null&&viewList.size()>0) {
				for(ViewManagementDto dto : viewList) {
			stmt = con.prepareStatement(" insert into ROLES_VIEWS_ASSOCIATION (role_id, view_id,display )  select roles.id roles_id, views.id views_id, 0 display from roles, views where roles.usertype=? and views.viewKey=?");
			 
			 
			stmt.setString(1, dto.getRoleName());
			stmt.setString(2, dto.getViewName());
			 
 
			retVal+=stmt.executeUpdate();
			
		 	
			}
				con.commit();
			}
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in RolesViewsAssocialtion_Default in LoadSettingsDao : " +e);
		}
		
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}
	
	

	public int loadPageRole_Default( ) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false);
			 stmt = con.prepareStatement(" insert into  role_page(role, page)   select distinct roles.usertype, page.id from roles, page, views v, ROLES_VIEWS_ASSOCIATION rva where roles.id=rva.role_id and v.id = rva.view_id and v.pageId = page.id");
			 
			  retVal=stmt.executeUpdate();
 
			 	con.commit();
			
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in loadPageRole_Default in LoadSettingsDao : " +e);
		}
		
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}






	
	
	public int loadMenu_Url_Default( ) {
		int retVal=0;
		 
		DbConnect db = DbConnect.getInstance();
		
		Connection con = null;
		PreparedStatement stmt = null;
		 
		
		try {
			con=db.connectDB();
			
			con.setAutoCommit(false); 
			stmt = con.prepareStatement(" insert into menu_url ( menuKey, url, type )   select v1.pageId menuKey, v1.pageId url, 'MAIN' type from views v1  ");
			 
			 
  
			retVal=stmt.executeUpdate();
			
		 	 	con.commit();
			 
		} catch(Exception e) {
			try {
				con.rollback();
			} catch (SQLException ignor) {
			 
			}
			retVal=0;
			System.out.println("Error in loadMenu_Url_Default in LoadSettingsDao : " +e);
		}
		
		finally {
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}


		return retVal;
	}



public int truncateAllPageRoleTables() {
	String [] queries= {"truncate table page","truncate table user_home_page", "truncate table views", "truncate table ROLES_VIEWS_ASSOCIATION", "truncate table role_page", "truncate table menu_url" };
	int retVal=0;
	 
	DbConnect db = DbConnect.getInstance();
	
	Connection con = null;
	PreparedStatement stmt = null;
	 
	
	try {
		con=db.connectDB();
		
		con.setAutoCommit(false);
		
		for(int i = 0 ; i < queries.length; i ++ ) {
			stmt = con.prepareStatement(queries[i]);
			 stmt.executeUpdate();
			 retVal ++;
			System.out.println(queries[i]+"-- DONE.");
			
		}

			
				
	 	 	con.commit();
		 
	} catch(Exception e) {
		try {
			con.rollback();
		} catch (SQLException ignor) {
		 
		}
		retVal=0;
		System.out.println("Error in truncateAllPageRoleTables in LoadSettingsDao : " +e);
	}
	
	finally {
		DbConnect.closeStatement(stmt);
		DbConnect.closeConnection(con);
	}


 	return retVal;
}


}
