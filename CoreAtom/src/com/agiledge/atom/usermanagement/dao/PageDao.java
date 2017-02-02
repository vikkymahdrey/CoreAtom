package com.agiledge.atom.usermanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.PageManagementStatus;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementStatus;
 

public class PageDao {

	public PageDto addPage(PageDto dto) {
		
		  
				DbConnect ob = DbConnect.getInstance();
				Connection con = null;
				PreparedStatement st = null;
				PageDto rDto = null;
				int returnInt = 0;
				try {
					con = ob.connectDB();
					con.setAutoCommit(false);
					st = con.prepareStatement("INSERT INTO page (url, type) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
					System.out.println(" in addpage pagedao");
					st.setString(1, dto.getUrl());
					st.setString(2, dto.getUrlType());
					returnInt = st.executeUpdate();
					if(returnInt>0) {
						ResultSet genkeys= null;
						genkeys = st.getGeneratedKeys();
						if(genkeys.next()) {
							int key = genkeys.getInt(1);
							System.out.println(" ke y " + key);
							new AuditLogDAO().auditLogEntry(key, AuditLogConstants.VIEW_MANAGEMENT,Integer.parseInt(dto.getUpdatedBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY,  AuditLogConstants.WORK_FLOW_STATE_PAGE_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
							rDto = new PageDto();
							rDto.setId(String.valueOf(key));		
						}
						
						DbConnect.closeResultSet(genkeys);
					}
					
					con.commit();

				} catch (Exception e) {
					try {
						con.rollback();
						returnInt=0;
						
					} catch (Exception ignore) {

					}
					e.printStackTrace();
				} finally {
					DbConnect.closeStatement(st);
					DbConnect.closeConnection(con);

				}
				return rDto;
			 
		 
	}

	public PageDto updatePage(PageDto dto) {
		
		  
				DbConnect ob = DbConnect.getInstance();
				Connection con = null;
				PreparedStatement st = null;
				PageDto rDto = null; 
				int returnInt = 0;
				try {
					con = ob.connectDB();
					con.setAutoCommit(false);
					//System.out.println(" query : update page set url='"+dto.getUrl()+"'  where id=" + dto.getId() +"" );
					st = con.prepareStatement("update page set url=?  where id=?",Statement.RETURN_GENERATED_KEYS);
					st.setString(1, dto.getUrl());
					st.setString(2, dto.getId());
					returnInt = st.executeUpdate();
					 
					if(returnInt>0) {
					 		 
							
							new AuditLogDAO().auditLogEntry(Integer.parseInt(dto.getId()), AuditLogConstants.VIEW_MANAGEMENT,Integer.parseInt(dto.getUpdatedBy()), "",  AuditLogConstants.WORK_FLOW_STATE_PAGE_UPDATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
							rDto = new PageDto();
							rDto.setId(String.valueOf(dto.getId()));		
							
					  
					}  
					
					con.commit();

				} catch (Exception e) {
					try {
						con.rollback();
						System.out.println("Error in updatePage " +e);
						
					} catch (Exception ignore) {

					}
					e.printStackTrace();
				} finally {
					DbConnect.closeStatement(st);
					DbConnect.closeConnection(con);

				}
				return rDto;
			 
		 
	}

	public ArrayList<PageDto> getAssignedPages(PageDto dto) {
		 
		DbConnect ob = DbConnect.getInstance();
		String query=null;
		String typeQuery = "";
		if(dto!= null && dto.getId().trim().equalsIgnoreCase("")==false) {
		
			if(dto.getUrlType()!=null&&dto.getUrlType().trim().equals("")==false) {
				typeQuery = " and m.type='" + dto.getUrlType() + "'";
			}
			query = "select p.id, p.url from menu_url m join page p on m.url = p.id where m.menuKey=?" + typeQuery;
		}  
		  
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		 
		ArrayList<PageDto> dtoList = new ArrayList<PageDto>();
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			st.setString(1, dto.getId());
		 
			rs = st.executeQuery();
			while (rs.next()) {
				 
				PageDto pdto = new PageDto();
				
				pdto.setId(rs.getString("id"));
				pdto.setUrl(rs.getString("url"));
				 			 
				dtoList.add(pdto);
			}
			
		} catch (Exception e) {
		 
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		System.out.println("List Size : " + dtoList.size());
		return dtoList;

	}

	public int groupAssignedUrls(ArrayList<PageDto> assignedList) {
		 

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
  
		int returnInt = 0;
		String mainUrlId="";
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			
			if(assignedList!= null && assignedList.size() > 0) {
				mainUrlId = assignedList.get(0).getParentPageId();
				st = con.prepareStatement("insert into menu_url (menukey, url, type ) values (?,?,? ) ");
				int val = deleteGroupAssigmentWithMainUrl(mainUrlId);
				st.setString(1, mainUrlId);
				st.setString(2, mainUrlId);
				st.setString(3, PageManagementStatus.MAIN);
				 
				returnInt= st.executeUpdate();
				for(PageDto dto : assignedList) {
					 if(dto.getId()!=null&&dto.getId().trim().equals("")==false) {
							st.setString(1, mainUrlId);
							st.setString(2, dto.getId());
							st.setString(3, PageManagementStatus.SUB);
							returnInt = returnInt + st.executeUpdate();
					 }
					
				}
 
			}
			
			 
			
			 
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error in updatePage " +e);
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}

		
		return returnInt;
	}
	
	
	
	public int groupAssignedUrl( PageDto  dto) {
		 

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement checkSt = null;
		PreparedStatement insertSt = null;
		ResultSet checkRs = null;
		int returnInt = 0;
		 
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
		 
			 	checkSt = con.prepareStatement("select pageId from views where pageId =" + dto.getUrl());
			 	 
			 	insertSt = con.prepareStatement("insert into role_page (role,page) values ('" + dto.getUserType() + "', " + dto.getUrl() + ") " );
			 	 
				st = con.prepareStatement("insert into menu_url (menukey, url, type ) values (?,?,? ) ");
				 
				checkRs = checkSt.executeQuery();
				if(checkRs.next() ) {
					 
					insertSt.executeUpdate();
					st.setString(1, dto.getUrl());
					st.setString(2, dto.getUrl());
					st.setString(3, PageManagementStatus.MAIN);
					returnInt = st.executeUpdate();
				
				} else {
					
				 

					st.setString(1, dto.getParentPageId());
					st.setString(2, dto.getUrl());
					st.setString(3, dto.getUrlType());
					 
					returnInt= st.executeUpdate();
				
					
				}
				
			   
			 
			
			 
			
			 
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error in PageDao groupAssignedUrl  " +e);
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(checkRs);
			DbConnect.closeStatement(st,checkSt,insertSt);
			DbConnect.closeConnection(con);

		}

		
		return returnInt;
	}

	


	public int groupAssignedUrlItself( PageDto  dto) {
		 

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement checkSt = null;
		PreparedStatement insertSt = null;
		ResultSet checkRs = null;
		int returnInt = 0;
		 
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
		 
			 	checkSt = con.prepareStatement("select pageId from views where pageId =" + dto.getParentPageId());
			 	 
			 	insertSt = con.prepareStatement("insert into role_page (role,page) values ('" + dto.getUserType() + "', " + dto.getParentPageId() + ") " );
			 	 
				st = con.prepareStatement("insert into menu_url (menukey, url, type ) values (?,?,? ) ");
				 
				checkRs = checkSt.executeQuery();
				if(checkRs.next() ) {
					 
					insertSt.executeUpdate();
					st.setString(1, dto.getParentPageId());
					st.setString(2, dto.getUrl());
					st.setString(3, PageManagementStatus.MAIN);
					returnInt = st.executeUpdate();
				
				}  				
			   
			 
			
			 
			
			 
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error in PageDao groupAssignedUrl  " +e);
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(checkRs);
			DbConnect.closeStatement(st,checkSt,insertSt);
			DbConnect.closeConnection(con);

		}

		
		return returnInt;
	}

	

	 

	private int deleteGroupAssigmentWithMainUrl(String mainUrlId) {
		//  
		

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PageDto rDto = null; 
		int returnInt = 0;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			//System.out.println(" query : update page set url='"+dto.getUrl()+"'  where id=" + dto.getId() +"" );
			st = con.prepareStatement("delete from menu_url  where menuKey=?" );
			st.setString(1, mainUrlId);
			 
			returnInt = st.executeUpdate();
			  
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
				System.out.println("Error in updatePage " +e);
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}
	 
	public PageDto getHomePageUrl(EmployeeDto dto) {
		String url=ViewManagementStatus.WELCOME_PAGE;

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PageDto rDto = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			  
			st = con.prepareStatement(" select h.id,h.url urlId, h.role usertype, p.url from user_home_page h join page  p on h.url=p.id where h.role='" + dto.getUserType() + "' ");
			 rs = st.executeQuery();
			 if(rs.next()) {
				 rDto = new PageDto();
				 rDto.setUrl(rs.getString("url"));
				 rDto.setId(rs.getString("urlId"));
				 
			 } else {
				 rDto = new PageDto();
				 rDto.setUrl(url);
				 rDto.setId("-1");
				 System.out.println("No Home Page Set");
			 }
			  	
			 

		} catch (Exception e) {
			try {
				con.rollback();
			 
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return rDto;
	 

		 
		
 
	}
	
	public int setHomePage(ViewManagementDto  dto) {
		
		   
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement dst = null;
		PreparedStatement authst= null;
		PageDto rDto = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			dst =con.prepareStatement(" delete from user_home_page where role='" + dto.getRoleName() + "' ");
			dst.executeUpdate();
			st = con.prepareStatement("INSERT INTO user_home_page  (url, role) VALUES(?,?)",Statement.RETURN_GENERATED_KEYS);
			System.out.println(" in addpage pagedao");
			st.setString(1, dto.getViewUrlId());
			st.setString(2, dto.getRoleName());
			returnInt = st.executeUpdate();
			if(returnInt>0) {
				authst = con.prepareStatement("insert into role_page (role, page ) values (?,?) " );
				authst.setString(1, dto.getRoleName());
				authst.setString(2, dto.getViewUrlId());
				 authst.executeUpdate();
				ResultSet genkeys= null;
				genkeys = st.getGeneratedKeys();
				if(genkeys.next()) {
					int key = genkeys.getInt(1);
					
					new AuditLogDAO().auditLogEntry(key, AuditLogConstants.VIEW_MANAGEMENT,Integer.parseInt(dto.getUpdatedBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY,  AuditLogConstants.WORK_FLOW_HOME_PAGE_UPDATE, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					rDto = new PageDto();
					rDto.setId(String.valueOf(key));		
				}
				
				DbConnect.closeResultSet(genkeys);
			}
			
			con.commit();

		} catch (Exception e) {
			try {
				con.rollback();
				returnInt=0;
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	 
 
}

	public PageDto getPageFromUrlString(String url) {
		 
	 

		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PageDto rDto = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			 
			st = con.prepareStatement(" select  * from page where url='"+url+"' ");
			 rs = st.executeQuery();
			 if(rs.next()) {
				 rDto = new PageDto();
				 rDto.setUrl(rs.getString("url"));
				 rDto.setId(rs.getString("id"));
				 rDto.setUrlType(rs.getString("type"));
			 } else {
				 rDto = null;
				  
			 }
			  	
			 

		} catch (Exception e) {
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
		return rDto;
	 


	}

	public boolean isWellPage(String url, String role) {
		//  
		String wellPageQuery= "select * from page p join  role_page r on p.id=r.page where  p.url='" + url + "' and r.role='" + role + "'";

		boolean flag = false;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		PageDto rDto = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			 
			st = con.prepareStatement(wellPageQuery);
			 rs = st.executeQuery();
			 if(rs.next()) {
				  flag = true;
			 } 
			  	
			 

		} catch (Exception e) {
			try {
				con.rollback();
			 
				
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return flag;
	 



		
	}


}
