package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.UserManagementDTO;


public class UserManagementDAO {
	public int AddRole(UserManagementDTO dto) {
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

	public int UpdateRole(UserManagementDTO dto) {
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

	public int AddView(UserManagementDTO dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("INSERT INTO VIEWS (VIEW_NAME,URL,SHOW_ORDER) VALUES(?,?,?)");
			st.setString(1, dto.getViewName());
			st.setString(2, dto.getViewURL());
			st.setInt(3, dto.getViewShowOrder());
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

	public int UpdateView(UserManagementDTO dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("UPDATE VIEWS SET VIEW_NAME=?,URL=?,SHOW_ORDER=? WHERE ID=?");
			st.setString(1, dto.getViewName());
			st.setString(2, dto.getViewURL());
			st.setInt(3, dto.getViewShowOrder());
			st.setInt(4, dto.getViewId());
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

	public int AddSubView(UserManagementDTO dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("INSERT INTO SUB_VIEWS (PARENT_ID,VIEW_NAME,URL,SHOW_ORDER) VALUES(?,?,?,?)");
			st.setInt(1, dto.getParentId());
			st.setString(2, dto.getSubViewName());
			st.setString(3, dto.getSubViewURL());
			st.setInt(4, dto.getSubViewShowOrder());
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

	public int UpdateSubView(UserManagementDTO dto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("UPDATE SUB_VIEWS SET PARENT_ID=?,VIEW_NAME=?,URL=?,SHOW_ORDER=? WHERE ID=?");
			st.setInt(1, dto.getParentId());
			st.setString(2, dto.getSubViewName());
			st.setString(3, dto.getSubViewURL());
			st.setInt(4, dto.getSubViewShowOrder());
			st.setInt(5, dto.getSubViewId());
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

	public int DeleteSubview(int subviewId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		Statement st = null;
		String query = "DELETE FROM SUB_VIEWS WHERE id=" + subviewId;
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
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("INSERT INTO ROLES_VIEWS_ASSOCIATION (ROLE_ID,VIEW_ID) VALUES(?,?)");
			for (int i = 0; i <= viewId.length; i++) {
				st.setInt(1, roleId);
				st.setInt(2, Integer.parseInt(viewId[i]));
				returnInt = st.executeUpdate();
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
		return returnInt;

	}

	public int DeleteRoleViewAssociation(int roleId, int viewId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		int returnInt = 0;
		try {
			con = ob.connectDB();
			st = con.prepareStatement("DELETE FROM ROLES_VIEWS_ASSOCIATION WHERE role_id=? AND view_id=?");
			st.setInt(1, roleId);
			st.setInt(2, viewId);
			returnInt = st.executeUpdate();
		} catch (Exception e) {
			
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public ArrayList<UserManagementDTO> GetAllRoleList() {
		ArrayList<UserManagementDTO> dtoList = new ArrayList<UserManagementDTO>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT id,name,description,usertype FROM ROLES";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				UserManagementDTO dto = new UserManagementDTO();
				dto.setRoleId(rs.getInt("id"));
				dto.setRoleName(rs.getString("name"));
				dto.setRoleDescription(rs.getString("description"));
				dto.setUserType(rs.getString("usertype"));				
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

	public ArrayList<UserManagementDTO> getViewsbyRole(int roleId) {
		ArrayList<UserManagementDTO> dtoList = new ArrayList<UserManagementDTO>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT * FROM VIEWS WHERE id IN (SELECT view_id FROM roles_views_association WHERE role_id="
				+ roleId + ") ORDER BY show_order";
		System.out.println("Query"+query);
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				UserManagementDTO dto = new UserManagementDTO();
				dto.setViewId(rs.getInt("id"));
				dto.setViewName(rs.getString("view_name"));
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

	public ArrayList<UserManagementDTO> getSubviewsbyView(int viewId) {
		ArrayList<UserManagementDTO> dtoList = new ArrayList<UserManagementDTO>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT * FROM SUB_VIEWS WHERE parent_id=" + viewId
				+ " ORDER BY show_order";
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				UserManagementDTO dto = new UserManagementDTO();
				dto.setSubViewId(rs.getInt("id"));
				dto.setParentId(rs.getInt("parent_id"));
				dto.setSubViewName(rs.getString("view_name"));
				dto.setSubViewURL(rs.getString("url"));
				dto.setSubViewShowOrder(rs.getInt("show_order"));
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

	public ArrayList<UserManagementDTO> GetAllViewList() {
		ArrayList<UserManagementDTO> viewList = new ArrayList<UserManagementDTO>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT * FROM VIEWS ";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				UserManagementDTO dto = new UserManagementDTO();
				dto.setViewId(rs.getInt("id"));
				dto.setViewName(rs.getString("view_name"));
				dto.setViewURL(rs.getString("url"));
				dto.setViewShowOrder(rs.getInt("show_order"));
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

	public ArrayList<UserManagementDTO> roleViewExisting(int roleId) {
		ArrayList<UserManagementDTO> viewList = new ArrayList<UserManagementDTO>();
		DbConnect ob = DbConnect.getInstance();
		String query = "SELECT * FROM VIEWS where view_name not in(SELECT view_name FROM VIEWS WHERE id IN (SELECT view_id FROM roles_views_association WHERE role_id="
				+ roleId + "))";
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				UserManagementDTO dto = new UserManagementDTO();
				dto.setViewId(rs.getInt("id"));
				dto.setViewName(rs.getString("view_name"));
				dto.setViewURL(rs.getString("url"));
				dto.setViewShowOrder(rs.getInt("show_order"));
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

}
