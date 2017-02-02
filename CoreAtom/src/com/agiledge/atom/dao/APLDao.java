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
import java.util.HashMap;

import org.apache.poi.hssf.record.formula.functions.Db;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.AplTree;
import com.agiledge.atom.dto.AuditLogDTO;


/**
 * 
 * @author 123
 */
public class APLDao {

	private String message;


	public ArrayList<APLDto> getAreas() {
		ArrayList<APLDto> area = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery("select id, area from area order by area.area");
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setAreaID(rs.getString("id"));
				dto.setArea(rs.getString("area"));
				area.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL : getAreas " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return area;
	}

	
	public ArrayList<APLDto> getAreas(String location) {
		ArrayList<APLDto> area = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery("select id, area from area where area.location="+location+" order by area.area");
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setAreaID(rs.getString("id"));
				dto.setArea(rs.getString("area"));
				area.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL : getAreas " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return area;
	}

	public ArrayList<APLDto> getPlaces() {
		ArrayList<APLDto> place = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(" select id, place from place order by place.place");
			while (rs.next()) {

				APLDto dto = new APLDto();
				dto.setPlaceID(rs.getString("id"));
				dto.setPlace(rs.getString("place"));
				// System.out.println(dto.getPlace());
				place.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return place;
	}

	public ArrayList<APLDto> getLandMarks() {
		ArrayList<APLDto> landMark = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(" select id, landMark from landMark order by landmark.landmark");
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landMark"));
				landMark.add(dto);
			}

			
		} catch (Exception e) {			
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return landMark;
	}

	public ArrayList<APLDto> getLandMarks(String landMarkName,String location,String site) {
		ArrayList<APLDto> landMark = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement st = null;
		ResultSet rs = null;
		String query="";
		String conditionVal="";
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		if(site==null)
		{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landMark  join place on landMark.place=place.id join area on place.area=area.id  where area.location=? and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=location;
		}		 
		else{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from site, landMark  join place on landMark.place=place.id join area on place.area=area.id  where  site.id=? and  area.location=site.branch and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=site;
		}
		
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.prepareStatement(query);
			st.setString(1, conditionVal);
			st.setString(2, "%" + landMarkName + "%");
			st.setString(3, "%" + landMarkName + "%");
			st.setString(4, "%" + landMarkName + "%");
			rs = st.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("area") + " ->"
						+ rs.getString("place") + " ->"
						+ rs.getString("landMark"));
				landMark.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return landMark;
	}
	public ArrayList<APLDto> getCDLandMarks(String landMarkName,String location,String site) {
		ArrayList<APLDto> landMark = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement st = null;
		ResultSet rs = null;
		String query="";
		String conditionVal="";
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		if(site==null)
		{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landMark  join place on landMark.place=place.id join area on place.area=area.id  where area.location=? and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=location;
		}		 
		else{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from site, landMark  join place on landMark.place=place.id join area on place.area=area.id  where  site.id=? and  area.location=site.branch and landMark.empstat='y' and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=site;
		}
		
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.prepareStatement(query);
			st.setString(1, conditionVal);
			st.setString(2, "%" + landMarkName + "%");
			st.setString(3, "%" + landMarkName + "%");
			st.setString(4, "%" + landMarkName + "%");
			rs = st.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("area") + " ->"
						+ rs.getString("place") + " ->"
						+ rs.getString("landMark"));
				landMark.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return landMark;
	}

	public ArrayList<APLDto> getShuttleLandMarks(String landMarkName,String location,String site) {
		ArrayList<APLDto> landMark = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement st = null;
		ResultSet rs = null;
		String query="";
		String conditionVal="";
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		if(site==null)
		{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landmarkShuttle landMark  join placeShuttle place on landMark.place=place.id join area on place.area=area.id  where area.location=? and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=location;
		}		 
		else if(location ==null)
		{
			query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from site, landmarkShuttle landMark  join placeShuttle place on landMark.place=place.id join area on place.area=area.id  where  site.id=? and  area.location=site.branch and (landMark.landMark like ? or place.place like ? or area.area like ?) order by area.area,place.place,landmark.landmark;";
			conditionVal=site;
		}
		
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.prepareStatement(query);
			st.setString(1, conditionVal);
			st.setString(2, "%" + landMarkName + "%");
			st.setString(3, "%" + landMarkName + "%");
			st.setString(4, "%" + landMarkName + "%");
			rs = st.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("area") + " ->"
						+ rs.getString("place") + " ->"
						+ rs.getString("landMark"));
				landMark.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return landMark;
	}
	public APLDto getLandMarkAccurate(String landMarkID) {
		APLDto dto = new APLDto();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;

		Connection con = ob.connectDB();
		String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landMark  join place on landMark.place=place.id join area on place.area=area.id  where landMark.id="
				+ landMarkID;
		// System.out.println("query : " + query);
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {

				dto.setLandMark(rs.getString("landmark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));

			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarkAccurate : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}		
		return dto;
	}

	public ArrayList<APLDto> getPlacesByAreaId(int areaId) {
		ArrayList<APLDto> place = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			rs = st.executeQuery(" select id, place,area from place where area="
					+ areaId + " order by place.place");
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setPlaceID(rs.getString("id"));
				dto.setPlace(rs.getString("place"));
				dto.setAreaID(rs.getString("area"));
				// System.out.println(dto.getPlace());
				place.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return place;
	}

	public ArrayList<APLDto> getLandmarksByPlaceId(int placeId) {
		ArrayList<APLDto> landmarks = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String query = "select landMark.id,landMark.landMark,landMark.place,landmark.latitude,landmark.longitude from landMark where landMark.place="
				+ placeId + " order by landmark.landmark";
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landmark"));
				dto.setLattitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				dto.setPlaceID(rs.getString("place"));
				landmarks.add(dto);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		return landmarks;
	}
public ArrayList<APLDto> getShuttleLandmarksByPlaceId(int placeId) {
		ArrayList<APLDto> landmarks = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String query = "select landMark.id,landMark.landMark,landMark.place from Landmarkshuttle landMark where landMark.place="
				+ placeId + " order by landmark.landmark";
		try {
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landmark"));
				dto.setPlaceID(rs.getString("place"));
				landmarks.add(dto);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		return landmarks;
	}

	public int insertArea(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null,autogenrs=null;
		int ChangedBy,autoincNumber;
		Connection con = ob.connectDB();
		String query = "select area from area where area=? and location=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getArea());
			pst.setString(2, APLDtoObj.getLocation());
			ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
			rs = pst.executeQuery();
			if (rs.next()) {
				returnInt = 100;
			} else {
				pst = con
						.prepareStatement("insert into area(area,location)  values (?,?)");
				pst.setString(1, APLDtoObj.getArea());
				pst.setString(2, APLDtoObj.getLocation());
				Statement st=con.createStatement();
				returnInt = pst.executeUpdate();
				autogenrs=st.executeQuery("Select id from area where area='"+APLDtoObj.getArea()+"' and location="+APLDtoObj.getLocation()+"");
				while(autogenrs.next())
				{
					autoincNumber=autogenrs.getInt(1);
					auditLogEntryforinsertarea(autoincNumber,AuditLogConstants.APL_MODULE
							, ChangedBy,
							AuditLogConstants.WORKFLOW_STATE_EMPTY,
							AuditLogConstants.WORK_FLOW_STATE_ADD_AREA,
							AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
				}
				
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}

	private void auditLogEntryforinsertarea(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public APLDto getAreaById(int areaId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		APLDto APLDtoObj = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			rs = st.executeQuery(" select id, area from area where id="
					+ areaId + "");
			while (rs.next()) {
				APLDtoObj = new APLDto();
				APLDtoObj.setAreaID(rs.getString("id"));
				APLDtoObj.setArea(rs.getString("area"));
			}
		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		
		return APLDtoObj;
	}

	public int updateArea(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		int ChangedBy=0,autoincNumber;
		
		Connection con = ob.connectDB();
		String query = "select area from area where area=? and location=?";
		try {
			ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getArea());
			pst.setString(2, APLDtoObj.getLocation());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con.prepareStatement("update area set area=? where id=? ");
				pst.setString(1, APLDtoObj.getArea());
				pst.setString(2, APLDtoObj.getAreaID());
				returnInt = pst.executeUpdate();
				if(returnInt==1)
				{
				autoincNumber=Integer.parseInt(APLDtoObj.getAreaID());
				auditLogEntryforupdatearea(autoincNumber,AuditLogConstants.APL_MODULE
						, ChangedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_AREA,
						AuditLogConstants.WORK_FLOW_STATE_EDIT_AREA,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}

	private void auditLogEntryforupdatearea(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public int insertPlace(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null,autogenrs=null;
		int changedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		String query = "select place from place where area=? and place=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getAreaID());
			pst.setString(2, APLDtoObj.getPlace());
			changedBy=Integer.parseInt(APLDtoObj.getDoneby());
			rs = pst.executeQuery();
			if (rs.next()) {
				returnInt = 100;
			} else {
				pst = con
						.prepareStatement("insert into place(place,area)  values (?,?)");
				pst.setString(1, APLDtoObj.getPlace());
				pst.setString(2, APLDtoObj.getAreaID());
				returnInt = pst.executeUpdate();
				if(returnInt==1)
				{
					Statement st=con.createStatement();
					autogenrs=st.executeQuery("Select id from place where place='"+APLDtoObj.getPlace()+"' and area="+APLDtoObj.getAreaID());
					while(autogenrs.next())
					{
						autoincNumber=autogenrs.getInt(1);
						auditLogEntryforinsertplace(autoincNumber,AuditLogConstants.APL_MODULE
								, changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_PLACE,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);

					}
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}

	private void auditLogEntryforinsertplace(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public APLDto getPlaceById(int placeId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		APLDto APLDtoObj = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			rs = st.executeQuery(" select id, place,area from place where id="
					+ placeId + "");
			if (rs.next()) {
				APLDtoObj = new APLDto();
				APLDtoObj.setPlaceID(rs.getString("id"));
				APLDtoObj.setPlace(rs.getString("place"));
				APLDtoObj.setAreaID(rs.getString("area"));

			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return APLDtoObj;
	}

	public APLDto getShuttlePlaceById(int placeId) {
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		APLDto APLDtoObj = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			rs = st.executeQuery(" select id, place,area from placeShuttle where id="
					+ placeId + "");
			if (rs.next()) {
				APLDtoObj = new APLDto();
				APLDtoObj.setPlaceID(rs.getString("id"));
				APLDtoObj.setPlace(rs.getString("place"));
				APLDtoObj.setAreaID(rs.getString("area"));

			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return APLDtoObj;
	}
	
	public int insertLandmark(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null,autogenrs=null;
		int ChangedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		String query = "select landmark from landmark where place=? and landmark=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getPlaceID());
			pst.setString(2, APLDtoObj.getLandMark());
			ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
			rs = pst.executeQuery();
			if (rs.next()) {
				returnInt = 100;
			} else {
				pst = con
						.prepareStatement("insert into landmark(landmark,place)  values (?,?)");
				pst.setString(1, APLDtoObj.getLandMark());
				pst.setString(2, APLDtoObj.getPlaceID());
				returnInt = pst.executeUpdate();
				if (returnInt==1)
				{
					Statement st=con.createStatement();
					autogenrs=st.executeQuery("Select id from landmark where landmark='"+APLDtoObj.getLandMark()+"' and place="+APLDtoObj.getPlaceID());
					while(autogenrs.next())
					{
						autoincNumber=autogenrs.getInt(1);
						auditLogEntryforinsertlandmark(autoincNumber,AuditLogConstants.APL_MODULE
								, ChangedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_LANDMARK,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}
	private void auditLogEntryforinsertlandmark(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public int updatePlace(APLDto APLDtoObj) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int changedBy=0,autoincNumber;
		String query = "select place from place where place=? and area=?";
		try {
			pst = con.prepareStatement(query);
			changedBy=Integer.parseInt(APLDtoObj.getDoneby());
			pst.setString(1, APLDtoObj.getPlace());
			pst.setString(2, APLDtoObj.getAreaID());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update place set place=? where id=?");
				pst.setString(1, APLDtoObj.getPlace());
				pst.setString(2, APLDtoObj.getPlaceID());
				returnInt = pst.executeUpdate();
				if(returnInt==1)
				{
					autoincNumber=Integer.parseInt(APLDtoObj.getPlaceID());
					auditLogEntryforupdateplace(autoincNumber,AuditLogConstants.APL_MODULE
							, changedBy,
							AuditLogConstants.WORK_FLOW_STATE_ADD_PLACE,
							AuditLogConstants.WORK_FLOW_STATE_EDIT_PLACE,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);

				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}

	private void auditLogEntryforupdateplace(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	public void updateLandmark(APLDto APLDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int ChangedBy=0,autoincNumber;
		String query = "select landmark from landmark where place=? and landmark=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getPlaceID());
			pst.setString(2, APLDtoObj.getLandMark());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update landmark set landmark=? where id=?");
				pst.setString(1, APLDtoObj.getLandMark());
				pst.setString(2, APLDtoObj.getLandMarkID());
				ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
				autoincNumber=Integer.parseInt(APLDtoObj.getLandMarkID());
				int returnvalue=pst.executeUpdate();
				if (returnvalue==1)
				{
					auditLogEntryforupdatelandmark(autoincNumber,AuditLogConstants.APL_MODULE
							, ChangedBy,
							AuditLogConstants.WORK_FLOW_STATE_ADD_LANDMARK,
							AuditLogConstants.WORK_FLOW_STATE_EDIT_LANDMARK,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
	}
	private void auditLogEntryforupdatelandmark(int autoincNumber, String Module,
			int changedBy, String preworkflowState,
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

	
	
	public ArrayList<APLDto> getAllAPLForSpecific(String LAP) {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		if(LAP!=null && !(LAP.equals("")))				
		LAP=" where "+LAP;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area,landmark.latitude,landmark.longitude from landMark  join place on landMark.place=place.id join area on place.area=area.id "+LAP+"  order by area.area,place.place,landmark.landmark ";
		System.out.println(query);
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				dto.setLattitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}

	
	/*
	 * Route Related modified by Muhammad on 20 th nov
	 */
	public ArrayList<APLDto> getAllAPL() {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area,landmark.latitude,landmark.longitude from landMark  join place on landMark.place=place.id join area on place.area=area.id order by area.area,place.place,landmark.landmark ";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				dto.setLattitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}
	
	public HashMap<String,APLDto> getAllAPL(int landmark[]) {
		HashMap<String,APLDto>  aplMap= new HashMap<String, APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
	 	System.out.println("Landmark size : " + landmark.length);
		
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation
	 
			String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area,landmark.latitude,landmark.longitude from landMark  join place on landMark.place=place.id join area on place.area=area.id and landmark.id=? order by area.area,place.place,landmark.landmark ";

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			for(int landmarkInt : landmark ) {
			//	System.out.println("Getting landmark : "+ query + " : " +String.valueOf(landmarkInt));
				pst.setInt(1, landmarkInt);
			rs = pst.executeQuery();
			if(rs.next()) {
				if(aplMap.get(rs.getString("id")) == null) {
					APLDto dto = new APLDto();
					dto.setLandMarkID(rs.getString("id"));
					dto.setLandMark(rs.getString("landMark"));
					dto.setPlace(rs.getString("place"));
					dto.setArea(rs.getString("area"));
					dto.setLattitude(rs.getString("latitude"));
					dto.setLongitude(rs.getString("longitude"));
				//	System.out.println("putting landmarkid "+ rs.getString("id"));
					aplMap.put(rs.getString("id"), dto);
				}
			}
			
			 
			
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return aplMap;

	}

	
	public ArrayList<APLDto> getAllShuttleAPL(String location) {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area from landMarkShuttle landMark   join placeShuttle place on landMark.place=place.id join area on place.area=area.id and area.location="+location+" order by area.area,place.place,landmark.landmark ";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}
	public ArrayList<APLDto> getAllShuttleAPLNotInRoute(int routeId) {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "SELECT a.area,p.place,l.id as landmarkId,l.landmark FROM area a,placeShuttle p,landmarkShuttle l WHERE l.id not in (select rc.landmarkId from routeChildShuttle rc where rc.routeId="
				+ routeId
				+ ") and  p.id=l.place and  a.id=p.area and a.location =(select branch from site where id=(select  routeShuttle.siteId from routeShuttle where id="+routeId+" limit 1)) order by a.area,p.place,l.landmark";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("landmarkId"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL apl not in route  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}
	
	
	public ArrayList<APLDto> getAllAPL(String location) {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "select landMark.id id, landMark.landMark landMark, place.place place, area.area area,landmark.latitude,landmark.longitude from landMark  join place on landMark.place=place.id join area on place.area=area.id and area.location="+location+" order by area.area,place.place,landmark.landmark ";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("id"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				dto.setLattitude(rs.getString("latitude"));
				dto.setLongitude(rs.getString("longitude"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getLandMarks  : " + e);

		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}

	public ArrayList<APLDto> getAllAPLNotInRoute(int routeId) {
		ArrayList<APLDto> APL = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		// String
		// query=" select id, landMark from landMark where landMark.landMark like ?";
		String query = "SELECT a.area,p.place,l.id as landmarkId,l.landmark FROM area a,place p,landmark l WHERE l.id not in (select rc.landmarkId from routeChild rc where rc.routeId="
				+ routeId
				+ ") and  p.id=l.place and  a.id=p.area and a.location =(select branch from site where id=(select  route.siteId from route where id="+routeId+" limit 1)) order by a.area,p.place,l.landmark";
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setLandMarkID(rs.getString("landmarkId"));
				dto.setLandMark(rs.getString("landMark"));
				dto.setPlace(rs.getString("place"));
				dto.setArea(rs.getString("area"));
				APL.add(dto);
			}


		} catch (Exception e) {
			System.out.println("Error in DAO-> APL apl not in route  : " + e);

		}
	finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return APL;

	}	

	public int updateLandmarkPostion(APLDto aPLDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int retVal = 0;
		// System.out.println("lat"+aPLDtoObj.getLattitude()+"lon"+aPLDtoObj.getLongitude());
		try {
			pst = con
					.prepareStatement("update landmark set latitude=?,longitude=? where id=?");
			pst.setString(1, aPLDtoObj.getLattitude());
			pst.setString(2, aPLDtoObj.getLongitude());
			pst.setString(3, aPLDtoObj.getLandMarkID());
			retVal = pst.executeUpdate();
			
			pst = con
					.prepareStatement("delete from distchart where (srcid=? or destid=?)");
			pst.setString(1, aPLDtoObj.getLandMarkID());
			pst.setString(2, aPLDtoObj.getLandMarkID());
			retVal = pst.executeUpdate();
			
		} catch (Exception e) {			
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}
	public ArrayList<APLDto> getShuttlePlacesByAreaId(int areaId) {
		ArrayList<APLDto> place = new ArrayList<APLDto>();
		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			rs = st.executeQuery(" select id, place,area from placeShuttle where area="
					+ areaId + " order by placeShuttle.place");
			while (rs.next()) {
				APLDto dto = new APLDto();
				dto.setPlaceID(rs.getString("id"));
				dto.setPlace(rs.getString("place"));
				dto.setAreaID(rs.getString("area"));
				// System.out.println(dto.getPlace());
				place.add(dto);
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);	
		}
		return place;
	}

	public int insertShuttlePlace(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null,autogenrs=null;
		int changedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		String query = "select place from placeShuttle where area=? and place=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getAreaID());
			pst.setString(2, APLDtoObj.getPlace());
			changedBy=Integer.parseInt(APLDtoObj.getDoneby());
			rs = pst.executeQuery();
			if (rs.next()) {
				returnInt = 100;
			} else {
				pst = con
						.prepareStatement("insert into placeShuttle(place,area)  values (?,?)");
				pst.setString(1, APLDtoObj.getPlace());
				pst.setString(2, APLDtoObj.getAreaID());
				returnInt = pst.executeUpdate();
				if(returnInt==1)
				{
					Statement st=con.createStatement();
					autogenrs=st.executeQuery("Select id from placeShuttle where place='"+APLDtoObj.getPlace()+"' and area="+APLDtoObj.getAreaID());
					while(autogenrs.next())
					{
						autoincNumber=autogenrs.getInt(1);
						auditLogEntryforinsertplace(autoincNumber,AuditLogConstants.APL_MODULE
								, changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_PLACE,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
						
					}
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return returnInt;
	}
	
	public int updateShuttlePlace(APLDto APLDtoObj) {
		int returnInt = 0;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int changedBy=0,autoincNumber;
		String query = "select place from placeShuttle where place=? and area=?";
		try {
			pst = con.prepareStatement(query);
			changedBy=Integer.parseInt(APLDtoObj.getDoneby());
			pst.setString(1, APLDtoObj.getPlace());
			pst.setString(2, APLDtoObj.getAreaID());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update placeShuttle set place=? where id=?");
				pst.setString(1, APLDtoObj.getPlace());
				pst.setString(2, APLDtoObj.getPlaceID());
				returnInt = pst.executeUpdate();
				if(returnInt==1)
				{
					autoincNumber=Integer.parseInt(APLDtoObj.getPlaceID());
					auditLogEntryforupdateplace(autoincNumber,AuditLogConstants.APL_MODULE
							, changedBy,
							AuditLogConstants.WORK_FLOW_STATE_ADD_PLACE,
							AuditLogConstants.WORK_FLOW_STATE_EDIT_PLACE,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnInt;
	}
	
	
	public int insertShuttleLandmark(APLDto APLDtoObj) {
		int returnInt = -1;
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null,autogenrs=null;
		int ChangedBy=0,autoincNumber;
		Connection con = ob.connectDB();
		String query = "select landmark from landmarkShuttle where place=? and landmark=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getPlaceID());
			pst.setString(2, APLDtoObj.getLandMark());
			ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
			rs = pst.executeQuery();
			if (rs.next()) {
				returnInt = 100;
			} else {
				pst = con
						.prepareStatement("insert into landmarkShuttle(landmark,place)  values (?,?)");
				pst.setString(1, APLDtoObj.getLandMark());
				pst.setString(2, APLDtoObj.getPlaceID());
				returnInt = pst.executeUpdate();
				if (returnInt==1)
				{
					Statement st=con.createStatement();
					autogenrs=st.executeQuery("Select id from landmarkShuttle where landmark='"+APLDtoObj.getLandMark()+"' and place="+APLDtoObj.getPlaceID());
					while(autogenrs.next())
					{
						autoincNumber=autogenrs.getInt(1);
						auditLogEntryforinsertlandmark(autoincNumber,AuditLogConstants.APL_MODULE
								, ChangedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_LANDMARK,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}
				}
			}

		} catch (Exception e) {

			System.out.println("Error in DAO-> APL getPlaces : " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return returnInt;
	}
	
	public void updateShuttleLandmark(APLDto APLDtoObj) {
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		int ChangedBy=0,autoincNumber;
		String query = "select landmark from landmarkShuttle where place=? and landmark=?";
		try {
			pst = con.prepareStatement(query);
			pst.setString(1, APLDtoObj.getPlaceID());
			pst.setString(2, APLDtoObj.getLandMark());
			rs = pst.executeQuery();
			if (rs.next()) {
			} else {
				pst = con
						.prepareStatement("update landmarkShuttle set landmark=? where id=?");
				pst.setString(1, APLDtoObj.getLandMark());
				pst.setString(2, APLDtoObj.getLandMarkID());
				ChangedBy=Integer.parseInt(APLDtoObj.getDoneby());
				autoincNumber=Integer.parseInt(APLDtoObj.getLandMarkID());
				int returnvalue=pst.executeUpdate();
				if (returnvalue==1)
				{
					auditLogEntryforupdatelandmark(autoincNumber,AuditLogConstants.APL_MODULE
							, ChangedBy,
							AuditLogConstants.WORK_FLOW_STATE_ADD_LANDMARK,
							AuditLogConstants.WORK_FLOW_STATE_EDIT_LANDMARK,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				}
			}

		} catch (Exception e) {
			System.out.println("Error in DAO-> APL getPlaces : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
	}
	
	
	public int  uploadAPL(AplTree aplTree, boolean areaDupe, boolean placeDupe, boolean landmarkDupe, String location) {
		
		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		PreparedStatement pst2 = null;
		PreparedStatement updatePst = null;
		
		ResultSet rs = null;
		ResultSet rs1 = null; 
		Connection con = ob.connectDB();
		
		int ChangedBy=0,autoincNumber;
		 int returnValue = 0;
		try {
			con.setAutoCommit(false);
			 	pst = con.prepareStatement(" insert into area (area, location) values (?,?) ", Statement.RETURN_GENERATED_KEYS);
				pst1 = con.prepareStatement(" insert into place (place, area) values (? , ?) ",Statement.RETURN_GENERATED_KEYS);
				pst2 = con.prepareStatement(" insert into landmark ( landmark, place, latitude, longitude) values (? , ?, ?, ?) ",Statement.RETURN_GENERATED_KEYS);
				updatePst = con.prepareStatement("update landmark set latitude=?, longitude=? where id=?");
				
			if(aplTree!=null && aplTree.getMap().size()>0) {
				
				for(String area : aplTree.getMap().keySet()) {

					if(area!=null) {
System.out.println("area :" + area);
						long areaId=0;
					 pst.setString(1, area);
					 pst.setString(2, location);
					 if(areaDupe ) {
						 System.out.println("area inserted " + area);
						 returnValue = pst.executeUpdate();
					 } else if((  areaId= checkDuplicateArea(con, area))==0){
						 System.out.println("area inserted " + area);
						 returnValue = pst.executeUpdate();
					 } else {
						 returnValue =1;
					 }
		 				if (returnValue>0)
				{
		 					if( areaId ==0) {
		 			//		System.out.println("Inserted : "+ area);
		 				 rs = pst.getGeneratedKeys();
		 				 rs.next();
		 				 areaId = rs.getLong(1);
		 					}

		 				 
		 				  
		 				AplTree placeTree = aplTree.getMap().get(area);
		 				if(placeTree!=null  && placeTree.getMap().size()>0) {
			 				for(String place : placeTree.getMap().keySet())	 {

			 					if(place!=null) {
	System.out.println("place " + place );
			 						boolean placeExecuted =false;
			 						long placeId = 0;
					 				pst1.setString(1, place);
									pst1.setLong(2, areaId);
									if((placeId=checkDuplicatePlaceArea(con, place, areaId))>0) {
									
										 returnValue = 1;
									 } else if(placeDupe ) {
										 placeExecuted= true;
										 System.out.println("1. place inserted " + place);
										 returnValue = pst1.executeUpdate();
									 } else if((placeId=checkDuplicatePlaceInDifferentArea(con, place, areaId))==0) {
										 placeExecuted= true;
										 System.out.println("2. place inserted " + place);
										 returnValue = pst1.executeUpdate();
									 } else  {
										 System.out.println("Place id : " + placeId);
										 returnValue = 1;
									 }
									if(returnValue>0) {
										if(placeExecuted == true) {
						
											rs1 = pst1.getGeneratedKeys();
											rs1.next();
											placeId = rs1.getLong(1);
		System.out.println("getting place id : "+ placeId);
											
										}
								 
										AplTree landmarkTree = placeTree.getMap().get(place);
										if(landmarkTree!=null  && landmarkTree.getMap().size()>0) {
							 				for(String landmark : landmarkTree.getMap().keySet())	 {
								 					
									 			if(landmark!=null) {
			System.out.println("landmark " + landmark);
													long landmarkId;
									 				boolean landmarkExecuted = false;
									 				boolean latLngUpdate =false;
									 				
													double lat=0;
													double lng =0;
													/* getting lat and long start*/
												try{
													AplTree latLng = landmarkTree.getMap().get(landmark);
													if(latLng!=null) {
														System.out
																.println("getting lat long..");
														
														lat = Double.parseDouble(latLng.getAplDto().getLattitude());
														lng = Double.parseDouble(latLng.getAplDto().getLongitude());
														 
														if(lat==0 && lng ==0) {
															latLngUpdate = false;
														}else {
															latLngUpdate = true;
														}
														System.out
																.println("lat long ("+ lat+","+ lng + " )");
													}
													
												}catch(Exception ignor) { 
													
													latLngUpdate = false;
												}
												/* getting lat and long start*/
												
												pst2.setString(1, landmark);
												pst2.setLong(2, placeId);
												pst2.setDouble(3, lat);
												pst2.setDouble(4, lng);
												
								 				
													if((landmarkId=checkDuplicateLandmarkPlaceArea(con, landmark, placeId, areaId))>0) {
														try {
															System.out
																	.println("landamark dupe :"+ landmark);
														
																if(compareLatLong(con, String.valueOf(landmarkId), lat, lng)==false && latLngUpdate == true) {
																	updatePst.setDouble(1, lat);
																	updatePst.setDouble(2, lng);
																	updatePst.setLong(3, landmarkId);
																	System.out
																			.println("updating lat long.");
																	System.out.println();
																	System.out
																			.println(String.format("update landmark set latitude=%s, longitude=%s where id=%s ",lat,lng,String.valueOf(landmarkId)));
																	if(updatePst.executeUpdate()>0) {
																		System.out
																				.println("Latlng updated for landmark :"+ landmark);
																	} else {
																		System.out
																				.println("lat long update failed");
																	}
																}
																
															 
																
														
														}catch(Exception ignor) {
															
															System.out
																	.println(ignor);
														}
														 returnValue = 1;
													 } else if(landmarkDupe ) {
														 landmarkExecuted = true;
														 System.out.println("1. landmark inserted " + landmark + " place id" + placeId );
														 returnValue = pst2.executeUpdate();
													 } else if((landmarkId=checkDuplicateLandmarkInDifferentPlaceOrArea(con, landmark, placeId, areaId))==0) {
														 landmarkExecuted = true;
														 System.out.println("2. landmark inserted " + landmark + " place id" + placeId );
														 returnValue = pst2.executeUpdate();
													 } else {
														 returnValue = 1;
													 }
		
												 	
									 			}
													  
													 
										
							 				}
										}
								}

								DbConnect.closeResultSet(rs1);
								 
			 				}
			 				}
		 				}
					 
				}
		 				DbConnect.closeResultSet(rs);
					}		 
		 				
				}
				
				con.commit();
				
				
			}
		} catch (Exception e) {
			returnValue=0;
			  
			System.out.println("Error in DAO-> APL uploadApL : " + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(pst,pst1,pst2, updatePst);
			DbConnect.closeConnection(con);	
		}

	return returnValue;	
	}
	
	
	public long checkDuplicateArea(Connection con, String area) {
		PreparedStatement st = null;
		ResultSet rs = null;
		long val =0;
		try {
			
			st  = con.prepareStatement("select * from area where area = ? " );
			st.setString(1, area);
			rs = st.executeQuery();
			if(rs.next()) {
				val = rs.getLong("id");
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			 
		}
		return val;
	}
	
	
	public long checkDuplicatePlaceArea(Connection con, String place, long areaId ) {
		PreparedStatement st = null;
		ResultSet rs = null;
		long val =0;
		try {
			
			st  = con.prepareStatement("select * from place where place = ? and area =? " );
			st.setString(1, place);
			st.setLong(2, areaId);
			rs = st.executeQuery();
			if(rs.next()) {
				val = rs.getLong("id");
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
		  
		}
		return val;
	}
	
	
	public long checkDuplicateLandmarkPlaceArea(Connection con, String landmark, long placeId, long areaId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		long val =0;
		try {
			
			st  = con.prepareStatement("select l.* from landmark l join place p on l.place=p.id where l.landmark = ? and l.place = ? and p.area = ? " );
			st.setString(1, landmark);
			st.setLong(2, placeId);
			st.setLong(3, areaId);
			rs = st.executeQuery();
			if(rs.next()) {
				val = rs.getLong("id");
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			 
		}
		return val;
	}
	
	
	public boolean compareLatLong(Connection con, String landmarkId, double lat, double lng) {
		PreparedStatement st = null;
		ResultSet rs = null;
		boolean retVal=false;
		 
		try {
			
			st  = con.prepareStatement("select  * from landmark where id= ? and latitude = ? and longitude = ?" );
			st.setString(1, landmarkId);
			st.setDouble(2, lat);
			st.setDouble(3, lng);
			rs = st.executeQuery();
			if(rs.next()) {
				retVal = true;
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			 
		}
		return retVal;
	}
	

	

	public long checkDuplicatePlaceInDifferentArea(Connection con, String place, long areaId ) {
		PreparedStatement st = null;
		ResultSet rs = null;
		long val =0;
		try {
			
			st  = con.prepareStatement("select * from place where place = ? and area !=? " );
			st.setString(1, place);
			st.setLong(2, areaId);
			rs = st.executeQuery();
			if(rs.next()) {
				val = rs.getLong("id");
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
		  
		}
		return val;
	}
	
	
	public long checkDuplicateLandmarkInDifferentPlaceOrArea(Connection con, String landmark, long placeId, long areaId) {
		PreparedStatement st = null;
		ResultSet rs = null;
		long val =0;
		try {
			
			st  = con.prepareStatement("select l.* from landmark l join place p on l.place=p.id where l.landmark = ? and ( l.place != ? and p.area != ? ) " );
			st.setString(1, landmark);
			st.setLong(2, placeId);
			st.setLong(3, areaId);
			rs = st.executeQuery();
			if(rs.next()) {
				val = rs.getLong("id");
			}
			
		}catch(Exception e) {
			System.out.println(" " +e);
		}
		
		finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			 
		}
		return val;
	}
	public ArrayList<APLDto> getAPLBasedOnRoute(String route){
		DbConnect ob = DbConnect.getInstance();
		Connection con=null;
		Statement st=null;
		ResultSet rs=null;
		ArrayList<APLDto> list=new ArrayList<APLDto>();
		try{
			con=ob.connectDB();
			st=con.createStatement();
			String aplqry="select a.area,p.place,l.landmark,l.id from routechild rc,area a,place p,landmark l where a.id=p.area and p.id=l.place and l.id=rc.landmarkId and rc.routeId ="+route;
			rs=st.executeQuery(aplqry);
			while(rs.next()){
				APLDto dto=new APLDto();
				dto.setArea(rs.getString("area"));
				dto.setPlace(rs.getString("place"));
				dto.setLandMark(rs.getString("landmark"));
				dto.setLandMarkID(rs.getString("id"));
				list.add(dto);
			}
		}catch(Exception e) {
			System.out.println("Error in APLDao@getAPLBasedOnRoute " +e);
		}
		
		finally {
			DbConnect.closeConnection(con);
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
		}
		return list;
	}

	}
