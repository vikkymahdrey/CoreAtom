package com.agiledge.atom.usermanagement.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dao.AuditLogDAO;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.usermanagement.dto.ViewManagementDto;
import com.agiledge.atom.usermanagement.dto.ViewManagementStatus;


public class ViewManagementDao {

	private String message="";

	public int AddRole(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("INSERT INTO ROLES (NAME,DESCRIPTION) VALUES(?,?)");
			st.setString(1, dto.getRoleName());
			st.setString(2, dto.getRoleDescription());
			returnInt = st.executeUpdate();

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
		return returnInt;
	}

	public int UpdateRole(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("UPDATE ROLES SET NAME=?,DESCRIPTION=? WHERE ID=?");
			st.setString(1, dto.getRoleName());
			st.setString(2, dto.getRoleDescription());
			st.setInt(3, dto.getRoleId());
			returnInt = st.executeUpdate();

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
		return returnInt;
	}

	public int DeleteRole(int roleId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("DELETE FROM ROLES WHERE ID=?");
			st.setInt(1, roleId);
			returnInt = st.executeUpdate();

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
		return returnInt;
	}

	public int addView(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		
		ResultSet rs=null;
		Statement selectSt=null;
		int returnInt = 0;
		 
		boolean isIdSet=false;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			selectSt = con.createStatement();
			String selectQuery = "select * from views where viewKey='"+dto.getViewKey()+"'";
			rs = selectSt.executeQuery(selectQuery);
			if(rs.next() )  {
				 returnInt = 0;
				 this.message ="Identifying name already exists";
			} else {
			System.out.println(" selecet query : " + selectQuery);
			System.out.println(" is Id set : " + isIdSet);
			 
				st = con.prepareStatement("INSERT INTO VIEWS (VIEW_NAME,pageId,SHOW_ORDER,TYPE, viewKey) VALUES(?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				st.setString(1, dto.getViewName());
				st.setString(2, dto.getViewUrlId());
				st.setInt(3, dto.getViewShowOrder());
				st.setString(4, ViewManagementStatus.ROOT);
				st.setString(5, dto.getViewKey());
				returnInt = st.executeUpdate();
				ResultSet genkeys= null;
				genkeys = st.getGeneratedKeys();
				if(genkeys.next()) {
					int key = genkeys.getInt(1);
					new AuditLogDAO().auditLogEntry(key, AuditLogConstants.VIEW_MANAGEMENT,Integer.parseInt(dto.getUpdatedBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY,  AuditLogConstants.WORK_FLOW_STATE_VIEW_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
							
				}
				DbConnect.closeResultSet(genkeys);
				con.commit();
			}

		} catch (Exception e) {
			this.message="" + e;
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int UpdateView(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement chek  = null;
		ResultSet rs = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			chek = con.prepareStatement("select * from views where id!=? and viewkey=?");
			chek.setInt(1,dto.getViewId() );
			chek.setString(2,dto.getViewKey() );
			rs = chek.executeQuery();
			if(rs.next()) { 
				System.out.println("select * from views where id!="+dto.getViewId()+" and viewkey='"+dto.getViewKey()+"'");
				this.message = "Identifying name already exists";
				returnInt = 0;
			} else {
				
				st = con.prepareStatement("UPDATE VIEWS SET VIEW_NAME=?,pageId=?,SHOW_ORDER=?, viewKey=? WHERE ID=?");
				st.setString(1, dto.getViewName());
				st.setString(2, dto.getViewUrlId());
				st.setInt(3, dto.getViewShowOrder());
				System.out.println(dto.getViewKey());
				st.setString(4, dto.getViewKey());
				st.setInt(5, dto.getViewId());
				returnInt = st.executeUpdate();
				con.commit();
			}
		} catch (Exception e) {
			this.message = e.getMessage();
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st,chek);
			DbConnect.closeResultSet(rs);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}

	public int AddSubView(ViewManagementDto dto) {
		
		/*
		 st = con.prepareStatement("INSERT INTO  VIEWS (PARENT_ID,VIEW_NAME,URL,SHOW_ORDER, TYPE) VALUES(?,?,?,?,?)");
			st.setInt(1, dto.getParentId());
			st.setString(2, dto.getSubViewName());
			st.setString(3, dto.getSubViewURL());
			st.setInt(4, dto.getSubViewShowOrder());
			st.setString(5, ViewManagementStatus.CHILD);
			returnInt = st.executeUpdate();
		 */
		
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement check = null;
		ResultSet genkeys= null;
		ResultSet rs = null; 
		 
		int returnInt = 0;
		int id=0;
		boolean isIdSet=false;
		try {
			System.out.println("In Add Sub View ");
			con = ob.connectDB();
			con.setAutoCommit(false);
 			check = con.prepareStatement("select * from views where viewKey=?");
 			check.setString(1, dto.getSubViewKey());
			 rs =  check.executeQuery();
			 if(rs.next()) {
				 System.out.println("Already Exist");
				 this.message = "The Indentifying name already exists";
				 returnInt = 0;
			 }else 
			 {
			System.out.println("In Add Sub View 01");
				st = con.prepareStatement("INSERT INTO VIEWS (VIEW_NAME,pageId,SHOW_ORDER,TYPE, PARENT_ID, viewKey) VALUES(?,?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS);
				st.setString(1, dto.getSubViewName());
				st.setString(2, dto.getSubViewURL());
				st.setInt(3, dto.getSubViewShowOrder());
				st.setString(4, ViewManagementStatus.CHILD);
				st.setInt(5, dto.getParentId());
				st.setString(6, dto.getSubViewKey());
			
				System.out.println("In Add Sub View 02");
				returnInt = st.executeUpdate();
				genkeys = st.getGeneratedKeys();
				
				if(genkeys.next()) {
					int key = genkeys.getInt(1);
					System.out.println("In Add Sub View 03");
					new AuditLogDAO().auditLogEntry(key, AuditLogConstants.VIEW_MANAGEMENT,Integer.parseInt(dto.getUpdatedBy()), AuditLogConstants.WORKFLOW_STATE_EMPTY,  AuditLogConstants.WORK_FLOW_STATE_VIEW_CREATED, AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
							
				}
				System.out.println("In Add Sub View 04");
				con.commit();
			 }

		} catch (Exception e) {
			System.out.println("Error in AddSubView " + e);
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}

	public int UpdateSubView(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement checkSt = null;
		ResultSet rs = null;
		int returnInt = 0;
		try {
			System.out.println(".........IN UpdateSubView ");
			con = ob.connectDB();
			con.setAutoCommit(false);
			
			checkSt = con.prepareStatement("select * from views where id!=? and viewkey=?");
			/*checkSt.setInt(1,dto.getViewId() );
			checkSt.setString(2,dto.getSubViewKey() );
			rs = checkSt.executeQuery();*/
			System.out.println("select * from views where id!="+dto.getViewId()+" and viewkey='"+dto.getViewKey()+"'");
			if(false) { 
				
				this.message = "Identifying name already exists";
				returnInt = 0;
			} else {
				
			
			st = con.prepareStatement("UPDATE  VIEWS SET PARENT_ID=?,VIEW_NAME=?,pageId=?,SHOW_ORDER=?, viewKey=? WHERE ID=? and TYPE=?");
			
			
			st.setInt(1, dto.getParentId());
			st.setString(2, dto.getSubViewName());
			st.setString(3, dto.getSubViewURL());
			st.setInt(4, dto.getSubViewShowOrder());
			st.setString(5, dto.getSubViewKey());
			st.setInt(6, dto.getSubViewId());
			st.setString(7, ViewManagementStatus.CHILD);
			returnInt = st.executeUpdate();
			con.commit();
			System.out.println("Update committed..");
			}
		} catch (Exception e) {
			this.message = e.getMessage();
			try {
				con.rollback();
			} catch (Exception ignore) {

			}
			e.printStackTrace();
		} finally {
			DbConnect.closeStatement(st, checkSt);
			DbConnect.closeResultSet(rs);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}

	public int deleteSubview(int subviewId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		String query = "DELETE FROM VIEWS WHERE type='"+ViewManagementStatus.CHILD+"' and id=" + subviewId;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			returnInt = st.executeUpdate(query);
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
		return returnInt;

	}
	
	
	  	
	public int AddRoleViewAssociation(String viewId[], int roleId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement drlv = null;
		PreparedStatement st = null;
		PreparedStatement drvst = null;
		PreparedStatement irvst = null;
		
		int returnInt = 0;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			drlv= con.prepareStatement("DELETE FROM ROLES_VIEWS_ASSOCIATION WHERE role_id=? ");
			drvst = con.prepareStatement("delete from role_page  where role_page.page in (   select distinct pId from  (select  r.usertype, r.id roleId, p.id pId from views v join page p on v.pageId=p.id join ROLES_VIEWS_ASSOCIATION rv on rv.view_id=v.id join  roles r on rv.role_id=r.id  union  select  r.usertype, r.id roleId, p.id pId  from  views v join views v1 on v1.parent_id=v.id join page p on v1.pageId=p.id  join ROLES_VIEWS_ASSOCIATION rv on rv.view_id=v.id join  roles r on rv.role_id=r.id   ) s where s.usertype=role_page.role and roleId=?  ) ");
			st = con.prepareStatement("INSERT INTO ROLES_VIEWS_ASSOCIATION (ROLE_ID,VIEW_ID) VALUES(?,?)");
			irvst = con.prepareStatement(" insert into role_page (page,role)  select distinct pId, usertype from  (select  r.usertype, r.id roleId, p.id pId from views v join page p on v.pageId=p.id join ROLES_VIEWS_ASSOCIATION rv on rv.view_id=v.id join  roles r on rv.role_id=r.id  union  select  r.usertype, r.id roleId, p.id pId  from  views v join views v1 on v1.parent_id=v.id join page p on v1.pageId=p.id  join ROLES_VIEWS_ASSOCIATION rv on rv.view_id=v.id join  roles r on rv.role_id=r.id   ) s  where roleId=?");
			System.out.println("LENGTH : " + viewId.length);
			   
				drlv.setInt(1, roleId);
				drlv.executeUpdate();
				drvst.setInt(1, roleId);
				drvst.executeUpdate();
				for (int i = 0; i < viewId.length; i++) {
					System.out.println("inside loop :" +i);
					System.out.println(" viewID ["+i+"] : " + viewId[i]);
					st.setInt(1, roleId);
					st.setInt(2, Integer.parseInt(viewId[i]));
					returnInt += st.executeUpdate();
					
				}
				irvst.setInt(1, roleId);
				irvst.executeUpdate();
			
			if(returnInt==viewId.length) {
				con.commit();
			}else {
				con.rollback();
				returnInt = 0;
			}
		} catch (Exception e) {
			System.out.println("Error : "+ e);
			returnInt =0;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			;
			}
			 System.out.println("Error in ViewManagementDao AddRoleViewAssociation "+e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;

	}
	
	 
	

	public int DeleteRoleViewAssociation(String actualId, int roleId, int viewId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		PreparedStatement st1 = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			con.setAutoCommit(false);
			st1 = con.prepareStatement("delete role_page where role in (select usertype from roles where id="+roleId+" ) and page not in  (select pageId from views v join  ROLES_VIEWS_ASSOCIATION ra on v.id=ra.view_id where role_id="+roleId+")");
			
			st = con.prepareStatement("DELETE FROM ROLES_VIEWS_ASSOCIATION WHERE role_id=? AND view_id=? and id=?");
			st.setInt(1, roleId);
			st.setInt(2, viewId);
			st.setString(3, actualId);
			
			returnInt = st.executeUpdate();
			st1.executeUpdate();
			con.commit();
		} catch (Exception e) {
			try {
				con.rollback();
			} catch ( Exception ignor) {
				// TODO Auto-generated catch block
				;
			}
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public ArrayList<ViewManagementDto> GetAllRoleList() {
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT id,name,description FROM ROLES";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setRoleId(rs.getInt("id"));
				dto.setRoleName(rs.getString("name"));
				dto.setRoleDescription(rs.getString("description"));
				dtoList.add(dto);
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
		return dtoList;
	}

	public boolean isRoleNameexists(String RoleName) {
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT * FROM ROLES WHERE NAME='" + RoleName + "'";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		boolean result = false;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			if (rs.next()) {
				result = true;
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
		return result;
	}

	public ArrayList<ViewManagementDto> getViewsbyRole(int roleId) {
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
		/*String query = "SELECT v.*, p.url FROM VIEWS v join page p on v.pageid=p.id  WHERE v.id IN (SELECT view_id FROM roles_views_association WHERE role_id="
				+ roleId + ") and v.type='" + ViewManagementStatus.ROOT + "' ORDER BY show_order";*/
		String query = "SELECT distinct rva.id rvaid, v.*, p.url FROM VIEWS v join page p on v.pageid=p.id join roles_views_association  rva on rva.view_id=v.id  WHERE v.type='" + ViewManagementStatus.ROOT + "'  and rva.role_id=" + roleId
				+ " ORDER BY rva.id ";
		
		System.out.println(query);
	 
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setViewId(rs.getInt("id"));
				dto.setId(rs.getLong("rvaid"));
				dto.setViewName(rs.getString("view_name"));
				dto.setViewKey(rs.getString("viewKey"));
				dto.setViewURL(rs.getString("url"));
				dto.setViewShowOrder(rs.getInt("show_order"));
				dtoList.add(dto);
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
		return dtoList;
	}

	public ArrayList<ViewManagementDto> getSubviewsbyView(int viewId) {
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT v.*, p.url FROM  VIEWS v join page p on v.pageId=p.id WHERE parent_id=" + viewId
				+ " ORDER BY show_order*1";
		 
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setSubViewId(rs.getInt("id"));
				dto.setParentId(rs.getInt("parent_id"));
				dto.setSubViewName(rs.getString("view_name"));
				dto.setSubViewURL(rs.getString("url"));
				dto.setSubViewURLId(rs.getString("pageId"));
				dto.setSubViewKey(rs.getString("viewKey"));
				
				dto.setSubViewShowOrder(rs.getInt("show_order"));
				dto.setViewUrlId(rs.getString("pageId"));
				dtoList.add(dto);
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
		return dtoList;
	}

	public ArrayList<ViewManagementDto> getAllRootViewList() {
		ArrayList<ViewManagementDto> viewList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT v.*, p.url FROM VIEWS v join page p on v.pageId=p.id where v.type='ROOT'";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setViewId(rs.getInt("id"));
				dto.setViewName(rs.getString("view_name"));
				dto.setViewURL(rs.getString("url"));
				dto.setViewShowOrder(rs.getInt("show_order"));
				dto.setViewKey(rs.getString("viewKey"));
				dto.setViewUrlId(rs.getString("pageId"));
				viewList.add(dto);
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
		return viewList;
	}

	public int DeleteView(int viewId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		String query = "DELETE FROM VIEWS WHERE id=" + viewId;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			returnInt = st.executeUpdate(query);
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
		return returnInt;

	}

	public ArrayList<ViewManagementDto> roleViewExisting(int roleId) {
		ArrayList<ViewManagementDto> viewList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
	/*	String query = "SELECT v.*, p.url FROM VIEWS v join page p on v.pageId=p.id where view_name not in(SELECT view_name FROM VIEWS WHERE id IN (SELECT view_id FROM roles_views_association WHERE role_id="
				+ roleId + "))";*/
		String query = "SELECT v.*, p.url FROM VIEWS v join page p on v.pageId=p.id where viewKey not in(SELECT viewKey FROM VIEWS WHERE id IN (SELECT view_id FROM roles_views_association WHERE role_id="
				+ roleId + ")) and v.type='" + ViewManagementStatus.ROOT + "'";
		 
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setViewId(rs.getInt("id"));
				dto.setViewName(rs.getString("view_name"));
				dto.setViewURL(rs.getString("url"));
				dto.setViewShowOrder(rs.getInt("show_order"));
				dto.setViewKey(rs.getString("viewKey"));
				viewList.add(dto);
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
		return viewList;
	}

	public int getRoleId(long empid) {
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT role_id FROM EMPLOYEE WHERE ID=" + empid;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		int role_id = 0;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				role_id = rs.getInt("role_id");
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
		return role_id;
	}

	public String getRoleNamebyid(int roleId) {
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT name FROM ROLES WHERE ID=" + roleId;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String role_name = null;
		try {
			System.out.println(query);
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				role_name = rs.getString("name");
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
		return role_name;
	}
	
	public String getRoleNamebyUserType (String key) {
		DbConnect ob = DbConnect.getInstance();
	
		String query = "SELECT name FROM ROLES WHERE usertype='" + key +"'";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String role_name = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				role_name = rs.getString("name");
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
		return role_name;
	}
	public String getRoleNameById(String key) {
		DbConnect ob = DbConnect.getInstance();
		String query = "select r.name from employee e,roles r where e.role_Id=r.id and e.id=" + key;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		String role_name = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				role_name = rs.getString("name");
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
		return role_name;
	}

	
	
	public ArrayList<ViewManagementDto>  getServletOnly() {
		ViewManagementDto dto = new ViewManagementDto();
		dto.setViewType(ViewManagementStatus.SERVLET);
		return getPages(dto);
		
	}
	
	public ArrayList<ViewManagementDto>  getAllPages() {
	 
		return getPages(null);
		
	}
	
	public ArrayList<ViewManagementDto>  getPagesOnly() {
		ViewManagementDto dto = new ViewManagementDto();
		dto.setViewType(ViewManagementStatus.PAGE);
		return getPages(dto);
		
	}

	
	public ArrayList<ViewManagementDto> getPages(ViewManagementDto dto) {
		DbConnect ob = DbConnect.getInstance();
		String query;
		if(dto!= null && dto.getViewType().trim().equalsIgnoreCase("")==false) {
			query = "SELECT id, url, type FROM page WHERE type='"+dto.getViewType()+"'";
		} else {
			query = "SELECT id, url, type FROM page  ";
		}
		  
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		 
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				 
				ViewManagementDto pdto = new ViewManagementDto();
				
				pdto.setViewURL(rs.getString("url"));
				pdto.setViewUrlId(rs.getString("id"));
				pdto.setViewType(rs.getString("type"));
			 
				dtoList.add(pdto);
			}
			
		} catch (Exception e) {
		 
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

	public ArrayList<ViewManagementDto> getViewPages() {
		
			
		DbConnect ob = DbConnect.getInstance();
		String query;
 
			query = "select p.id, p.url, p.type from page p join views v on p.id=  v.pageId ";
 
		  
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		 
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				 
				ViewManagementDto pdto = new ViewManagementDto();
				
				pdto.setViewURL(rs.getString("url"));
				pdto.setViewUrlId(rs.getString("id"));
				pdto.setViewType(rs.getString("type"));
			 
				dtoList.add(pdto);
			}
			
		} catch (Exception e) {
		 
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dtoList;

 
 
	}

	public ArrayList<ViewManagementDto> getEmployeeHeirarchy() {
		ArrayList<ViewManagementDto> dtoList = new ArrayList<ViewManagementDto>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT id,uniqkey,role FROM empHeirarchy ";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				ViewManagementDto dto = new ViewManagementDto();
				dto.setRoleId(rs.getInt("id"));
				dto.setViewKey(rs.getString("uniqkey"));
				dto.setRoleName(rs.getString("role"));				
				dtoList.add(dto);
			}

		} catch (Exception e) {		
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

}
