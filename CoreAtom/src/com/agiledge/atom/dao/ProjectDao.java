/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map.Entry;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.ProjectDto;
import com.agiledge.atom.task.dao.GetEmpsJSON;

/**
 * 
 * @author Administrator
 */
public class ProjectDao {

	private ArrayList<ProjectDto> projectActual;
	private ArrayList<ProjectDto> projectExist;
	HashMap<String, ProjectDto> projects = null;
	private ProjectDto projectDtoObj;
	DbConnect ob = null;// DbConnect.getInstance();
	Connection con = null;// ob.connectDB();

	public void UpdateProjectDBConnet() throws Exception {
		ob = DbConnect.getInstance();
		con = ob.connectDB();
	}

	public void setprojectLists(ArrayList<ProjectDto> projectActual,
			ArrayList<ProjectDto> projectExist, HashMap<String, ProjectDto> actualProjectJson) {
		this.projectActual = projectActual;
		this.projectExist = projectExist;
		this.projects=actualProjectJson;
	}

	public void closeConnection() throws SQLException {
		DbConnect.closeConnection(con);
	}

	public void insertProject(ProjectDto projectdto) {
		try{
		//	System.out.println("herereeeeeeeeeeeee");
		PreparedStatement pst = con
				.prepareStatement("insert into atsconnect(project,description,status,projecttype,projectunit) values (?,?,?,?,?)");
		pst.setString(1, projectdto.getProject());
		pst.setString(2, projectdto.getDescription());
		pst.setString(3, "a");
		pst.setString(4, "project");
		pst.setString(5, projectdto.getProjectUnit());
		pst.executeUpdate();
		}catch(Exception e)
		{
		System.out.println("Error "+e);	
		}
	}

	public ArrayList<ProjectDto> getProjects() {
		ArrayList<ProjectDto> existingProject = null;

		try {
			UpdateProjectDBConnet();
			Statement st = null;
			ResultSet rs = null;

			rs = null;
			st = null;
			existingProject = new ArrayList<ProjectDto>();
			st = con.createStatement();
			rs = st.executeQuery("select project,description from atsconnect");
			while (rs.next()) {
				projectDtoObj = new ProjectDto();
				projectDtoObj.setProject(rs.getString("project"));
				projectDtoObj.setDescription(rs.getString("description"));
				existingProject.add(projectDtoObj);
			}
			rs.close();
			st.close();
			closeConnection();
		} catch (Exception e) {
			System.out.println("ERRORIN DAO111111" + e);
		}

		return existingProject;
	}

	public ArrayList<ProjectDto> getProjectsByCode(String projectCode) {
		ArrayList<ProjectDto> projects = null;

		try {
			UpdateProjectDBConnet();
			PreparedStatement pst = null;
			ResultSet rs = null;
			projects = new ArrayList<ProjectDto>();
			String query = "select id,project,description from atsconnect where project=?";
			pst = con.prepareStatement(query);
			pst.setString(1, projectCode);
			rs = pst.executeQuery();
			while (rs.next()) {
				projectDtoObj = new ProjectDto();
				projectDtoObj.setId(rs.getString("Id"));
				projectDtoObj.setProject(rs.getString("project"));
				projectDtoObj.setDescription(rs.getString("description"));
				projects.add(projectDtoObj);
			}
			rs.close();
			pst.close();
			closeConnection();
		} catch (Exception e) {
			System.out.println("ERRORIN DAO111111" + e);
			try {
				closeConnection();
			} catch (SQLException ex) {
				System.out.println("ERROR IN DAO close connection" + ex);
			}
		}
		return projects;
	}

	public ArrayList<ProjectDto> getProjectsByName(String projectName) {
		// System.out.println("Calling by Name"+projectName);
		ArrayList<ProjectDto> projects = null;

		try {
			UpdateProjectDBConnet();
			PreparedStatement pst = null;
			ResultSet rs = null;
			projects = new ArrayList<ProjectDto>();
			String query = "select id,project,description from atsconnect where description like ?";
			// System.out.println("query"+query);
			pst = con.prepareStatement(query);
			pst.setString(1,"%"+ projectName + "%");
			rs = pst.executeQuery();
			while (rs.next()) {
				projectDtoObj = new ProjectDto();
				projectDtoObj.setId(rs.getString("id"));
				projectDtoObj.setProject(rs.getString("project"));
				projectDtoObj.setDescription(rs.getString("description"));
				projects.add(projectDtoObj);
			}
			rs.close();
			pst.close();
			closeConnection();
		} catch (Exception e) {
			System.out.println("ERRORIN DAO111111" + e);
			try {
				closeConnection();
			} catch (SQLException ex) {
				System.out.println("ERROR IN DAO close connection" + ex);
			}
		}
		return projects;
	}

	public ArrayList<ProjectDto> getAllProjects() {
		ArrayList<ProjectDto> existingProject = null;

		Statement st = null;
		ResultSet rs = null;
		try {
			existingProject = new ArrayList<ProjectDto>();
			st = con.createStatement();
			rs = st.executeQuery("select distinct id,project,description from atsconnect");
			while (rs.next()) {
				projectDtoObj = new ProjectDto();
				// System.out.println("Proj"+rs.getString("project"));
				projectDtoObj.setId(rs.getString("id"));
				projectDtoObj.setProject(rs.getString("project"));
				projectDtoObj.setDescription(rs.getString("description"));
				existingProject.add(projectDtoObj);
			}
			rs.close();
			st.close();
		} catch (Exception e) {
			try {
				rs.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			try {
				st.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println("ERRORIN DAO111111" + e);
		}

		return existingProject;
	}

	public void updateProject() throws SQLException {
		boolean flag = false;
		for (ProjectDto projectActualDto : projectActual) {
			// System.out.println("Actual Login Id"+employeeActualDto.getLoginId());
			flag = false;
			for (ProjectDto projectExistDto : projectExist) {
				// System.out.println("Existing Login Id"+employeeExistDto.getLoginId());
				if (projectActualDto.getProject().equals(
						projectExistDto.getProject())) {
					flag = true;
					break;
				}
			}
			if (!flag) {
				// System.out.println("Inserting login ID" +
				// projectActualDto.getProject());
				insertProject(projectActualDto);
			}
		}
	}

	public void updateProjectBasedEmployee() throws SQLException {
		System.out.println("reached here11111111111111111111");
		
		if(projects==null)
		System.out.println(projects.size());
		for (Iterator it = projects.entrySet().iterator(); it.hasNext();) {
			boolean flag = false;
			Entry entry = (Entry) it.next();
			ProjectDto projectActualDto = (ProjectDto) entry.getValue();
			//System.out.println(projectActualDto.getDescription());
			for (ProjectDto projectExistDto : projectExist) {
			//	System.out.println(projectExistDto.getDescription());
				// System.out.println("Existing Login Id"+employeeExistDto.getLoginId());
				if (projectActualDto.getDescription().equals(
						projectExistDto.getDescription())) {
					flag = true;
					break;
				}
			}
			System.out.println(flag);
			if (!flag) {
				// System.out.println("Inserting login ID" +
				// projectActualDto.getProject());
				insertProject(projectActualDto);
			}
		}
	}

	public int mapTimeAndProject(String project, String timeId, String DoneBy) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = ob.connectDB();
			String query = "insert into project_shifttime (project,shifttime) values (?, ?)";
			st = con.prepareStatement(query);
			st.setString(1, project);
			st.setString(2, timeId);

			returnInt = st.executeUpdate();

		} catch (Exception e) {
			System.out.println("Exception in projectDao mapTimeAndProject : "
					+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	public ArrayList<ProjectDto> getProjectsInShitTime(int timeID) {
		ArrayList<ProjectDto> dtoList = new ArrayList<ProjectDto>();
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			String query = "select p.* from atsconnect p join project_shifttime ps on p.id=ps.project where ps.shifttime=?";
			st = con.prepareStatement(query);
			st.setInt(1, timeID);
			rs = st.executeQuery();
			while (rs.next()) {
				ProjectDto dto = new ProjectDto();
				dto.setId(rs.getString("id"));
				dto.setProject(rs.getString("project"));
				dto.setDescription(rs.getString("description"));

				dtoList.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Exception in projectDao mapTimeAndProject : "
					+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return dtoList;
	}

	public int removeMapTimeAndProject(String project, String timeId,
			String DoneBy) {
		int returnInt = 0, changedBy = 0, autoincNumber;
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		try {
			con = ob.connectDB();
			changedBy = Integer.parseInt(DoneBy);
			autoincNumber = Integer.parseInt(project);
			String query = "delete from project_shifttime where project=? and shifttime=?";
			st = con.prepareStatement(query);
			st.setString(1, project);
			st.setString(2, timeId);

			returnInt = st.executeUpdate();
			if (returnInt == 1) {
				auditLogEntryforremoveshifttime(autoincNumber,
						AuditLogConstants.PROJECT_SHIFTTIME_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_EMPTY,
						AuditLogConstants.WORK_FLOW_STATE_REMOVE_SHIFTTIME,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

			}

		} catch (Exception e) {
			System.out
					.println("Exception in projectDao removeMapTimeAndProject : "
							+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	private void auditLogEntryforremoveshifttime(int autoincNumber,
			String Module, int changedBy, String preworkflowState,
			String curworkflowState, String auditLogAction) {
		AuditLogDTO auditLog = new AuditLogDTO();

		auditLog.setRelatedNodeId(autoincNumber);
		auditLog.setModuleName(Module);
		auditLog.setChangedBy(changedBy);
		auditLog.setDateChanged(new Date());
		auditLog.setPreviousState(preworkflowState);
		auditLog.setCurrentState(curworkflowState);
		auditLog.setAction(auditLogAction);

		AuditLogDAO auditDAO = new AuditLogDAO();
		auditDAO.addAuditLogEntry(auditLog);

	}

	public String getprojectName(int projectid) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = null;
		String projectname = "";
		String query = "Select project from atsconnect where id=" + projectid;
		try {
			con = ob.connectDB();
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				projectname = rs.getString(1);
			}
		} catch (Exception e) {
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}

		return projectname;
	}

	public ArrayList<ProjectDto> getProjectUnit() {
		ArrayList<ProjectDto> dtoList = new ArrayList<ProjectDto>();
		DbConnect ob = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement st = null;
		ResultSet rs = null;
		try {
			con = ob.connectDB();
			String query = "select distinct projectUnit from atsconnect";
			st = con.prepareStatement(query);
			rs = st.executeQuery();
			while (rs.next()) {
				ProjectDto dto = new ProjectDto();
	
				dto.setProjectUnit(rs.getString("projectUnit"));
	
				dtoList.add(dto);
			}
	
		} catch (Exception e) {
			System.out.println("Exception in projectDao mapTimeAndProject : "
					+ e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
	
		}
		return dtoList;
	}
	
}
