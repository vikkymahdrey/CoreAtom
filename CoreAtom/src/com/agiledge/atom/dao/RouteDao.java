package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.GeoTagDto;
import com.agiledge.atom.dto.RouteDto;

public class RouteDao {

	public int insertRoute(ArrayList<RouteDto> routeDtos) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int position = 0;
		int routeId = 0;
		int status = 0;
		int changedBy = 0, autoincNumber;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet keys = null;
		try {
			pst = con.prepareStatement(
					"insert into route(routeName,siteId,type) values (?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);
			pst1 = con
					.prepareStatement("insert into routeChild(routeId,position,landmarkId) values(?,?,?)");
			for (RouteDto routeDto : routeDtos) {
				if (position == 0) {
					pst.setString(1, routeDto.getRouteName());
					pst.setString(2, routeDto.getSiteId());
					pst.setString(3, routeDto.getRouteType());
					int returnvalue = pst.executeUpdate();
					keys = pst.getGeneratedKeys();
					if (keys.next()) {
						routeId = keys.getInt(1);
					}
					autoincNumber = routeId;
					changedBy = Integer.parseInt(routeDto.getDoneBy());
					if (returnvalue == 1) {
						auditLogEntryforinsertroute(autoincNumber,
								AuditLogConstants.ROUTE_MODULE, changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}
					DbConnect.closeStatement(pst);
				}
				pst1.setInt(1, routeId);
				pst1.setInt(2, position);
				pst1.setString(3, routeDto.getLandmarkId());
				pst1.executeUpdate();
				position++;
			}

			status = 1;
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeResultSet(keys);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return status;
	}

	public int insertShuttleRoute(ArrayList<RouteDto> routeDtos, String inOut) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int position = 0;
		int routeId = 0;
		int status = 0;
		int changedBy = 0, autoincNumber;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet keys = null;
		try {
			pst = con
					.prepareStatement(
							"insert into routeShuttle(routeName,siteId,in_out) values(?,?,?)",
							Statement.RETURN_GENERATED_KEYS);
			pst1 = con
					.prepareStatement("insert into routeChildShuttle(routeId,position,landmarkId) values(?,?,?)");
			for (RouteDto routeDto : routeDtos) {
				if (position == 0) {
					pst.setString(1, routeDto.getRouteName() + "-" + inOut);
					pst.setString(2, routeDto.getSiteId());
					pst.setString(3, inOut);
					int returnvalue = pst.executeUpdate();
					keys = pst.getGeneratedKeys();
					if (keys.next()) {
						routeId = keys.getInt(1);
					}
					autoincNumber = routeId;
					changedBy = Integer.parseInt(routeDto.getDoneBy());
					if (returnvalue == 1) {
						auditLogEntryforinsertroute(autoincNumber,
								AuditLogConstants.ROUTE_MODULE, changedBy,
								AuditLogConstants.WORKFLOW_STATE_EMPTY,
								AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
								AuditLogConstants.AUDIT_LOG_CREATE_ACTION);
					}
					DbConnect.closeStatement(pst);
				}
				pst1.setInt(1, routeId);
				pst1.setInt(2, position);
				pst1.setString(3, routeDto.getLandmarkId());
				pst1.executeUpdate();
				position++;
			}

			status = 1;
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeResultSet(keys);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return status;
	}

	private void auditLogEntryforinsertroute(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public ArrayList<RouteDto> getAllRoutes() {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,rt.type,rt.typeDesc,s.site_name FROM route r,site s,routeTypes rt where s.id=r.siteId and rt.type=r.type order by r.id";
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setRouteType(rs.getString("type"));
				routeDto.setRouteTypeDesc(rs.getString("type"));
				routeDto.setSiteName(rs.getString("site_name"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	public ArrayList<RouteDto> getAllRoutes(String site) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,rt.type,rt.typeDesc,s.site_name FROM route r,site s,routeTypes rt where r.siteId="
					+ site
					+ " and s.id=r.siteId and rt.type=r.type order by r.id desc";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setRouteType(rs.getString("type"));
				routeDto.setRouteTypeDesc(rs.getString("type"));
				routeDto.setSiteName(rs.getString("site_name"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error          a" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	public ArrayList<RouteDto> getAllShuttleRoutes(String site) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,s.site_name,sum(vt.sit_cap*sv.count) seats,sum(sb.id) as bookings ,r.in_out  FROM site s,routeShuttle r left outer join  shuttleVehicle sv on r.id=sv.routeId left outer join vehicle_type vt on sv.vehicleType=vt.id left outer join shuttleBooking sb on r.id=sb.routeId and sb.status='a' where r.siteId="
					+ site
					+ " and s.id=r.siteId group by r.id,r.routeName,r.siteId,s.site_name order by r.id";
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setSiteName(rs.getString("site_name"));
				routeDto.setSeats(rs.getString("seats"));
				routeDto.setBookingCount(rs.getString("bookings"));
				routeDto.setInOut(rs.getString("in_out"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error1111111" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	public ArrayList<RouteDto> getInOutShuttleRoutes(String site, String inOut) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,s.site_name,sum(vt.sit_cap*sv.count) seats,sum(sb.id) as bookings   FROM site s,routeShuttle r left outer join  shuttleVehicle sv on r.id=sv.routeId left outer join vehicle_type vt on sv.vehicleType=vt.id left outer join shuttleBooking sb on r.id=sb.routeId and sb.status='a' where r.siteId="
					+ site
					+ " and r.in_out='"
					+ inOut
					+ "' and s.id=r.siteId group by r.id,r.routeName,r.siteId,s.site_name order by r.id";
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setSiteName(rs.getString("site_name"));
				routeDto.setSeats(rs.getString("seats"));
				routeDto.setBookingCount(rs.getString("bookings"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	public ArrayList<RouteDto> getRouteDetails(int routeId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "SELECT distinct rc.routeId,a.area,p.place,l.id as landmarkId,l.landmark,l.latitude,l.longitude  FROM area a,place p,landmark l,routeChild rc WHERE rc.routeId ="
					+ routeId
					+ " and rc.landmarkId=l.id and  p.id=l.place and  a.id=p.area  order by rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("routeId"));
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmarkId(rs.getString("landmarkId"));
				routeDto.setLandmark(rs.getString("landmark"));
				routeDto.setLattitude(rs.getString("latitude"));
				routeDto.setLongitude(rs.getString("longitude"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;
	}

	/********************* only for ordering the route *************/
	public ArrayList<RouteDto> getRouteDetailsInRevers(int routeId, Object con) {
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;
		try {
			routedtos = new ArrayList<RouteDto>();
			st = ((Connection) con).createStatement();
			String nuquery = "SELECT rc.routeId,a.area,p.place,l.id as landmarkId,l.landmark,l.latitude,l.longitude  FROM area a,place p,landmark l,routeChild rc WHERE rc.routeId ="
					+ routeId
					+ " and rc.landmarkId=l.id and  p.id=l.place and  a.id=p.area  order by rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("routeId"));
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmarkId(rs.getString("landmarkId"));
				routeDto.setLandmark(rs.getString("landmark"));
				routeDto.setLattitude(rs.getString("latitude"));
				routeDto.setLongitude(rs.getString("longitude"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
		}
		return routedtos;
	}

	/*********************************************************/

	public ArrayList<RouteDto> getShuttleRouteDetails(int routeId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "SELECT rc.routeId,a.area,p.place,l.id as landmarkId,l.landmark  FROM area a,placeShuttle p,landmarkShuttle l,routeChildShuttle rc WHERE rc.routeId ="
					+ routeId
					+ " and rc.landmarkId=l.id and  p.id=l.place and  a.id=p.area order by rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("routeId"));
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmarkId(rs.getString("landmarkId"));
				routeDto.setLandmark(rs.getString("landmark"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;
	}

	public int modifyRoute(int routeId) {
		return routeId;

	}

	public RouteDto getMasterRouteDetails(int routeId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st = con.createStatement();
			String nuquery = "select * from route where id=" + routeId + "";
			rs = st.executeQuery(nuquery);
			if (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setRouteType(rs.getString("type"));
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return routeDto;
	}

	public RouteDto getShuttleMasterRouteDetails(int routeId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st = con.createStatement();
			String nuquery = "select * from routeShuttle where id=" + routeId
					+ "";
			rs = st.executeQuery(nuquery);
			if (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));

			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return routeDto;
	}

	public int modifyRoute(ArrayList<RouteDto> routeLandmarks, int routeId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int position = 0, changedBy = 0, autoincNumber;
		int status = 0;
		PreparedStatement pst = null;
		int returnvalue = 0;
		try {
			pst = con
					.prepareStatement("delete from routeChild where routeId=?");
			pst.setInt(1, routeId);
			int returnvalue1 = pst.executeUpdate();
			autoincNumber = routeId;
			if (returnvalue1 == 1) {
				auditLogEntryformodifyroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
			}
			pst = con
					.prepareStatement("insert into routeChild(routeId,position,landmarkId) values(?,?,?)");
			for (RouteDto routeDto : routeLandmarks) {
				changedBy = Integer.parseInt(routeDto.getDoneBy());
				pst.setInt(1, routeId);
				pst.setInt(2, position);
				pst.setString(3, routeDto.getLandmarkId());
				returnvalue = pst.executeUpdate();
				position++;
			}
			if (returnvalue == 1) {
				auditLogEntryformodifyroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

			status = 1;
		} catch (Exception e) {
			System.out.println("ERROR in modify" + e);
		} finally {
			DbConnect.closeStatement(pst);
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return status;

	}

	public int modifyShuttleRoute(ArrayList<RouteDto> routeLandmarks,
			int routeId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int position = 0, changedBy = 0, autoincNumber;
		int status = 0;
		PreparedStatement pst = null;
		int returnvalue = 0;
		try {
			pst = con
					.prepareStatement("delete from routeChildShuttle where routeId=?");
			pst.setInt(1, routeId);
			int returnvalue1 = pst.executeUpdate();
			autoincNumber = routeId;
			if (returnvalue1 == 1) {
				auditLogEntryformodifyroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_DELETE_ACTION);
			}
			pst = con
					.prepareStatement("insert into routeChildShuttle(routeId,position,landmarkId) values(?,?,?)");
			for (RouteDto routeDto : routeLandmarks) {
				changedBy = Integer.parseInt(routeDto.getDoneBy());
				pst.setInt(1, routeId);
				pst.setInt(2, position);
				pst.setString(3, routeDto.getLandmarkId());
				returnvalue = pst.executeUpdate();
				position++;
			}
			if (returnvalue == 1) {
				auditLogEntryformodifyroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}

			status = 1;
		} catch (Exception e) {
			System.out.println("ERROR in Shuttle route modify" + e);
		} finally {
			DbConnect.closeStatement(pst);
			try {
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}

		}
		return status;

	}

	private void auditLogEntryformodifyroute(int autoincNumber, String Module,
			int changedBy, String preworkflowState, String curworkflowState,
			String auditLogAction) {
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

	public ArrayList<RouteDto> getRoutetypes() {
		DbConnect ob = DbConnect.getInstance();
		ArrayList<RouteDto> routeTypedtos = new ArrayList<RouteDto>();
		RouteDto routeTypedto = null;
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		try {
			String query = "select * from routeTypes order by priority";

			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeTypedto = new RouteDto();
				routeTypedto.setRouteType(rs.getString(1));
				routeTypedto.setRouteTypeDesc(rs.getString(2));
				routeTypedtos.add(routeTypedto);
			}

		} catch (Exception e) {
			System.out.println("Error in  getDomainName   : e " + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}

		return routeTypedtos;

	}

	public int updateShuttleMasterRoute(RouteDto routeDto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0, changedBy = 0, autoincNumber;
		try {
			String query = "update routeShuttle set routeName=? where id=?";

			pst = con.prepareStatement(query);
			pst.setString(1, routeDto.getRouteName());
			pst.setInt(2, routeDto.getRouteId());
			autoincNumber = routeDto.getRouteId();
			changedBy = Integer.parseInt(routeDto.getDoneBy());
			retVal = pst.executeUpdate();
			if (retVal == 1) {
				auditLogEntryformodifymasterroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}
		} catch (Exception e) {
			System.out.println("Error in  getDomainName   : e " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	public int updateMasterRoute(RouteDto routeDto) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0, changedBy = 0, autoincNumber;
		try {
			String query = "update route set routeName=? ,type=? where id=?";

			pst = con.prepareStatement(query);
			pst.setString(1, routeDto.getRouteName());
			pst.setString(2, routeDto.getRouteType());
			pst.setInt(3, routeDto.getRouteId());
			autoincNumber = routeDto.getRouteId();
			changedBy = Integer.parseInt(routeDto.getDoneBy());
			retVal = pst.executeUpdate();
			if (retVal == 1) {
				auditLogEntryformodifymasterroute(autoincNumber,
						AuditLogConstants.ROUTE_MODULE, changedBy,
						AuditLogConstants.WORK_FLOW_STATE_ADD_ROUTE,
						AuditLogConstants.WORK_FLOW_STATE_MODIFY_ROUTE,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
			}
		} catch (Exception e) {
			System.out.println("Error in  getDomainName   : e " + e);
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}

		return retVal;
	}

	private void auditLogEntryformodifymasterroute(int autoincNumber,
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

	public ArrayList<RouteDto> getRoutes(String site) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "select r.id,r.routeName,a.area,p.place,l.landmark,r.type from route r,routeChild rc,area a,place p,landmark l where r.siteId="
					+ site
					+ " and r.id=rc.routeId and  rc.landmarkId=l.id and l.place=p.id and p.area=a.id order by rc.routeId,rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmark(rs.getString("landmark"));
				routeDto.setRouteTypeDesc(rs.getString("type"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return routedtos;
	}

	public int assignShuttleVehicle(ArrayList<RouteDto> list) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int returnvalue = 0;
		PreparedStatement pst = null, pst1 = null;
		ResultSet rs = null;
		try {

			pst = con
					.prepareStatement("select * from shuttleVehicle where routeid=? and vehicleType=? and inOutTime=?");

			for (RouteDto routeDto : list) {
				pst.setInt(1, routeDto.getRouteId());
				pst.setString(2, routeDto.getVehicleType());
				pst.setString(3, routeDto.getInOut());
				rs = pst.executeQuery();
				if (rs.next()) {
					pst1 = con
							.prepareStatement("update shuttleVehicle set count="
									+ routeDto.getVehicleCount()
									+ " where routeId=? and vehicleType=? and inOutTime=?");
					// update shuttleVehicle set
					// availableSeat=("+routeDto.getSeats()+"+availableSeat-("+routeDto.getSeat()+"*count)),
					// count="+routeDto.getVehicleCount()+" where routeId=? and
					// vehicleType=? and inOutTime=?

					pst1.setInt(1, routeDto.getRouteId());
					pst1.setString(2, routeDto.getVehicleType());
					pst1.setString(3, routeDto.getInOut());
					returnvalue += pst1.executeUpdate();
				} else {
					pst1 = con
							.prepareStatement("insert into shuttleVehicle(routeId,vehicleType,count,inOutTime,availableSeat) values(?,?,?,?,?)");
					pst1.setInt(1, routeDto.getRouteId());
					pst1.setString(2, routeDto.getVehicleType());
					pst1.setString(3, routeDto.getVehicleCount());
					pst1.setString(4, routeDto.getInOut());
					pst1.setString(5, routeDto.getSeats());
					returnvalue += pst1.executeUpdate();
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeStatement(pst1);
			DbConnect.closeConnection(con);
		}
		return returnvalue;
	}

	public ArrayList<RouteDto> getTripRouteDetails(int tripId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "SELECT area,place,landmarkId,landmark,latitude,longitude from (select a.area,p.place,l.id as landmarkId,l.landmark,l.latitude,l.longitude,0  as routedOrder  FROM area a,place p,landmark l,site s,trip_details t WHERE t.Id="
					+ tripId
					+ " and t.siteid=s.id  and s.landmark=l.id and  p.id=l.place and  a.id=p.area and t.trip_log='OUT' union SELECT a.area,p.place,l.id as landmarkId,l.landmark,l.latitude,l.longitude,tdc.routedOrder  FROM area a,place p,landmark l,trip_details_child tdc WHERE tdc.tripId="
					+ tripId
					+ " and tdc.landmarkId=l.id and  p.id=l.place and  a.id=p.area union select a.area,p.place,l.id as landmarkId,l.landmark,l.latitude,l.longitude,100  as routedOrder  FROM area a,place p,landmark l,site s,trip_details t WHERE t.Id="
					+ tripId
					+ " and t.siteid=s.id  and s.landmark=l.id and  p.id=l.place and  a.id=p.area and t.trip_log='IN' ) as direction order by routedOrder";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmarkId(rs.getString("landmarkId"));
				routeDto.setLandmark(rs.getString("landmark"));
				routeDto.setLattitude(rs.getString("latitude"));
				routeDto.setLongitude(rs.getString("longitude"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		}
		return routedtos;

	}

	public ArrayList<RouteDto> getRoutes() {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "select r.id,r.routeName,a.area,p.place,l.landmark from route r,routeChild rc,area a,place p,landmark l where r.id=rc.routeId and  rc.landmarkId=l.id and l.place=p.id and p.area=a.id order by rc.routeId,rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("routeId"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setArea(rs.getString("area"));
				routeDto.setPlace(rs.getString("place"));
				routeDto.setLandmark(rs.getString("landmark"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error" + e);
		} finally {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				st.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			try {
				con.close();
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}
		return routedtos;
	}

	public int getShuttleSeat(String routeId, String inOutTime) {

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		int retVal = 0;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		String query = "select ifnull(sum(vt.sit_cap*sv.count),0) as seats from vehicle_type vt,shuttleVehicle sv where sv.vehicleType=vt.id and sv.routeId="
				+ routeId + " and sv.inOutTime='" + inOutTime + "'";
		System.out.println(query);

		try {

			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if (rs.next()) {
				retVal = rs.getInt(1);
			}

		} catch (Exception e) {
			System.out.println("Exception " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public int deleteAllRoute(String site, String type) {
		//

		DbConnect ob = DbConnect.getInstance();
		PreparedStatement pst = null;
		PreparedStatement pstMain = null;
		PreparedStatement pstChild = null;
		int retVal = 0;
		ResultSet rs = null;
		Connection con = ob.connectDB();

		String query = "select * from route where siteId = ? and type = ?";
		String queryChild = "delete from routeChild where routeId = ?";
		String queryMain = "delete from route where siteId = ? and type = ?";
		System.out.println(query);

		try {

			con.setAutoCommit(false);

			pst = con.prepareStatement(query);
			pst.setString(1, site);
			pst.setString(2, type);

			rs = pst.executeQuery();

			while (rs.next()) {

				String id = rs.getString("id");

				pstChild = con.prepareStatement(queryChild);
				pstChild.setString(1, id);
				pstChild.executeUpdate();

			}

			pstMain = con.prepareStatement(queryMain);
			pstMain.setString(1, site);
			pstMain.setString(2, type);
			retVal = pstMain.executeUpdate();

			con.commit();

		} catch (Exception e) {
			System.out.println("Exception " + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;

	}

	public int orderTheRoute(Object[] landmarksInOrder, int routeId) {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();
		int status = 0;
		int changedBy = 0, autoincNumber;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet keys = null;
		int retVal = 0;
		try {
			pst = con
					.prepareStatement("update routeChild set position=? where landmarkid=? and routeId=?");
			for (int i = 0; i < landmarksInOrder.length; i++) {

				pst.setInt(1, landmarksInOrder.length - i - 1);
				pst.setString(2, landmarksInOrder[i].toString());
				pst.setInt(3, routeId);
				retVal += pst.executeUpdate();
			}
		} catch (Exception e) {
			System.out.println("ERROR" + e);
		} finally {
			DbConnect.closeResultSet(keys);
			DbConnect.closeStatement(pst, pst1);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	// Only for Keonics
	public ArrayList<RouteDto> getAllRoutesWithlog(String site, String log) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;

		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,rt.type,rt.typeDesc,s.site_name FROM route r,site s,routeTypes rt where r.siteId="
					+ site
					+ " and r.logType in ('"
					+ log
					+ "', 'IN/OUT')and s.id=r.siteId and rt.type=r.type  ORDER BY SUBSTR(r.routename FROM 1 FOR 2), CAST(SUBSTR(r.routename FROM 3) AS UNSIGNED), SUBSTR(r.routename FROM 3)";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setRouteType(rs.getString("type"));
				routeDto.setRouteTypeDesc(rs.getString("type"));
				routeDto.setSiteName(rs.getString("site_name"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error@getAllRoutesWithlog" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	// only for keonics
	public int createRouteWithoutAPL(String routename, String logType,
			String[] landmarks, String[] landmarkName) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement st1 = null, st2 = null;
		Statement st = null;
		ResultSet keys = null, key1 = null;
		int result = 0;
		int routeId = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String qry = "insert into route(routeName,siteId,type,logType) values('"
					+ routename + "','1','P','" + logType + "')";
			st1 = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
			result = st1.executeUpdate();
			keys = st1.getGeneratedKeys();
			if (keys.next()) {
				routeId = keys.getInt(1);
			}
			if (routeId > 0) {
				int order = 0;
				for (int z = 0; z < landmarks.length; z++) {
					String lat = landmarks[z].split("-")[1].split(":")[0];
					String lng = landmarks[z].split("-")[1].split(":")[1];
					String insertlandmark = "INSERT INTO landmark (landmark, latitude, longitude,place) VALUES ('"
							+ landmarkName[z]
							+ "', '"
							+ lat
							+ "', '"
							+ lng
							+ "','0');";
					st2 = con.prepareStatement(insertlandmark,
							Statement.RETURN_GENERATED_KEYS);
					int landmark = 0;
					st2.executeUpdate();
					key1 = st2.getGeneratedKeys();
					if (key1.next()) {
						landmark = key1.getInt(1);
					}

					String insertqry = "insert into routechild(routeId, position, landmarkId) values("
							+ routeId + "," + order + ",'" + landmark + "')";
					st = con.createStatement();
					st.executeUpdate(insertqry);
					order++;
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@createRouteWithoutAPL" + e);
		} finally {
			DbConnect.closeResultSet(keys, key1);
			DbConnect.closeStatement(st, st1, st2);
			DbConnect.closeConnection(con);
		}
		return result;
	}

	public ArrayList<RouteDto> getRouteDetailsWithoutAPL(int routeId) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			st = con.createStatement();
			String nuquery = "SELECT distinct rc.routeId,l.id as landmarkId,l.landmark,l.latitude,l.longitude  FROM landmark l,routeChild rc WHERE rc.routeId ="
					+ routeId + " and rc.landmarkId=l.id  order by rc.position";
			rs = st.executeQuery(nuquery);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("routeId"));
				routeDto.setLandmarkId(rs.getString("landmarkId"));
				routeDto.setLandmark(rs.getString("landmark"));
				routeDto.setLattitude(rs.getString("latitude"));
				routeDto.setLongitude(rs.getString("longitude"));
				routedtos.add(routeDto);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getRouteDetailsWithoutAPL" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;
	}

	public ArrayList<ArrayList<RouteDto>> getAllRouteDetailsWithoutAPL(
			String[] routeids) {
		ArrayList<ArrayList<RouteDto>> list = new ArrayList<ArrayList<RouteDto>>();
		if (routeids[0].equalsIgnoreCase("ALL")) {

		} else {
			for (int s = 0; s < routeids.length; s++) {
				ArrayList<RouteDto> route = getRouteDetailsWithoutAPL(Integer
						.parseInt(routeids[s]));
				list.add(route);
			}
		}
		return list;
	}

	// Only for Keonics
	public ArrayList<RouteDto> getAllRoutesWithNoSites(String log) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto routeDto = null;
		ArrayList<RouteDto> routedtos = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			routedtos = new ArrayList<RouteDto>();
			String query = "SELECT r.id,r.routeName,r.siteId,rt.type,rt.typeDesc FROM route r,routeTypes rt where r.siteId='0' and r.logType in ('"
					+ log
					+ "', 'IN/OUT') and rt.type=r.type  ORDER BY SUBSTR(r.routename FROM 1 FOR 2), CAST(SUBSTR(r.routename FROM 3) AS UNSIGNED), SUBSTR(r.routename FROM 3)";
			System.out.println(query);
			st = con.createStatement();
			rs = st.executeQuery(query);
			while (rs.next()) {
				routeDto = new RouteDto();
				routeDto.setRouteId(rs.getInt("id"));
				routeDto.setRouteName(rs.getString("routeName"));
				routeDto.setSiteId(rs.getString("siteId"));
				routeDto.setRouteType(rs.getString("type"));
				routeDto.setRouteTypeDesc(rs.getString("type"));
				routedtos.add(routeDto);
			}

		} catch (Exception e) {

			System.out.println("Error@getAllRoutesWithNoSites" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return routedtos;

	}

	//only for keonics
	public int createAutoRoute(String routename, String logType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement st1 = null, st2 = null;
		Statement st = null,st3=null;
		ResultSet keys = null, key1 = null;
		int result = 0;
		int routeId = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String qry = "insert into route(routeName,siteId,type,logType,routeType,vehicle) values('"
					+ routename + "','1','P','" + logType + "','auto','"+vehiceType+"')";
			st1 = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
			result = st1.executeUpdate();
			keys = st1.getGeneratedKeys();
			
			if (keys.next()) {
				routeId = keys.getInt(1);
			}
			if (routeId > 0) {
				int order = 0;
				for (int z = 0; z < points.length; z++) {
					String lat = points[z].replace("*", "/").split("/")[0];
					String lng = points[z].replace("*", "/").split("/")[1];;
					String insertlandmark = "INSERT INTO landmark (landmark, latitude, longitude,place) VALUES ('"
							+ landmarkName[z]
							+ "', '"
							+ lat
							+ "', '"
							+ lng
							+ "','0');";
					st2 = con.prepareStatement(insertlandmark,
							Statement.RETURN_GENERATED_KEYS);
					int landmark = 0;
					st2.executeUpdate();
					key1 = st2.getGeneratedKeys();
					if (key1.next()) {
						landmark = key1.getInt(1);
					}

					String insertqry = "insert into routechild(routeId, position, landmarkId) values("
							+ routeId + "," + order + ",'" + landmark + "')";
					st = con.createStatement();
					st.executeUpdate(insertqry);
					order++;
				}
				if(empids.length>0){
				//updating employee
				st3=con.createStatement();
				String subqry1="";
				for(int s=0;s<empids.length;s++){
					subqry1+=empids[s]+"";
					if(s<empids.length-1){
						subqry1+=",";
					}
				}
				String empqry="update employee set adminroute='"+routeId+"' where id in ("+subqry1+")";
				System.out.println(empqry);
				st3.executeUpdate(empqry);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@createAutoRoute" + e);
		} finally {
			DbConnect.closeResultSet(keys, key1);
			DbConnect.closeStatement(st, st1, st2,st3);
			DbConnect.closeConnection(con);
		}
		return result;
	}

	public ArrayList<RouteDto> getAutogeneratedRoutelist() {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st2=null;
		ResultSet rs = null,rs1=null;
		ArrayList<RouteDto> list = new ArrayList<RouteDto>();
		try{
			String qry="select id,routename,logtype,vehicle from route where routeType='auto'";
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			st2=con.createStatement();
			rs=st.executeQuery(qry);
			while(rs.next()){
				String qry2="select count(id) as count from employee where adminroute="+rs.getInt("id");
				rs1=st2.executeQuery(qry2);
				if(rs1.next()){
				RouteDto dto=new RouteDto();
				dto.setRouteId(rs.getInt("id"));
				dto.setRouteName(rs.getString("routename"));
				dto.setVehicleType(rs.getString("vehicle"));
				dto.setInOut(rs.getString("logtype"));
				dto.setVehicleCount(rs1.getString("count"));
				list.add(dto);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getAutogeneratedRoutelist" + e);
		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st2);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	public RouteDto getAutogeneratedRoute(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto dto=new RouteDto();
		try{
			String qry="select id,routename,logtype,vehicle from route where id="+routeid;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(qry);
			while(rs.next()){

				dto.setRouteId(rs.getInt("id"));
				dto.setRouteName(rs.getString("routename"));
				dto.setVehicleType(rs.getString("vehicle"));
				dto.setInOut(rs.getString("logtype"));
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getAutogeneratedRoute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}

	public ArrayList<GeoTagDto> getEmployeeFromRoute(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<GeoTagDto> list=new ArrayList<GeoTagDto>();
		try{
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			String qry="select id,displayname,personnelno,emp_lat,emp_long from employee where adminroute="+routeid;
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayname"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				list.add(dto);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getEmployeeFromRoute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return list;
	}

	public int updateAutoRoute(String routeid,String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		int result=0;
		try{
			String qry="UPDATE route SET siteId='0', routeType='no' WHERE id="+routeid;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			result=st.executeUpdate(qry);
			if(result>0){
				result=createAutoRoute(routename, routeType, points,
						landmarkName, vehiceType, empids);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@updateAutoRoute" + e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return result;
	}

	public int createAutoRoutewithLog(String routename, String logType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement st1 = null, st2 = null;
		Statement st = null,st3=null;
		ResultSet keys = null, key1 = null;
		int result = 0;
		int routeId = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String qry = "insert into route(routeName,siteId,type,logType,routeType,vehicle,time) values('"
					+ routename + "','1','P','" + logType + "','auto','"+vehiceType+"','"+time+"')";
			st1 = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
			result = st1.executeUpdate();
			keys = st1.getGeneratedKeys();
			
			if (keys.next()) {
				routeId = keys.getInt(1);
			}
			if (routeId > 0) {
				int order = 0;
				for (int z = 0; z < points.length; z++) {
					String lat = points[z].replace("*", "/").split("/")[0];
					String lng = points[z].replace("*", "/").split("/")[1];;
					String insertlandmark = "INSERT INTO landmark (landmark, latitude, longitude,place) VALUES ('"
							+ landmarkName[z]
							+ "', '"
							+ lat
							+ "', '"
							+ lng
							+ "','0');";
					st2 = con.prepareStatement(insertlandmark,
							Statement.RETURN_GENERATED_KEYS);
					int landmark = 0;
					st2.executeUpdate();
					key1 = st2.getGeneratedKeys();
					if (key1.next()) {
						landmark = key1.getInt(1);
					}

					String insertqry = "insert into routechild(routeId, position, landmarkId) values("
							+ routeId + "," + order + ",'" + landmark + "')";
					st = con.createStatement();
					st.executeUpdate(insertqry);
					order++;
				}
				if(empids.length>0){
				//updating employee
				st3=con.createStatement();
				String subqry1="";
				for(int s=0;s<empids.length;s++){
					subqry1+=empids[s]+"";
					if(s<empids.length-1){
						subqry1+=",";
					}
				}
				String empqry="update employee set adminroute='"+routeId+"' where id in ("+subqry1+")";
				System.out.println(empqry);
				st3.executeUpdate(empqry);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@createAutoRoutewithLog" + e);
		} finally {
			DbConnect.closeResultSet(keys, key1);
			DbConnect.closeStatement(st, st1, st2,st3);
			DbConnect.closeConnection(con);
		}
		return result;

	}
	public ArrayList<RouteDto> getAutogeneratedRoutelistWithLog() {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st2=null;
		ResultSet rs = null,rs1=null;
		ArrayList<RouteDto> list = new ArrayList<RouteDto>();
		try{
			String qry="select id,routename,logtype,vehicle,time from route where routeType='auto'";
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			st2=con.createStatement();
			rs=st.executeQuery(qry);
			while(rs.next()){
				String qry2="select count(id) as count from employee where adminroute="+rs.getInt("id");
				rs1=st2.executeQuery(qry2);
				if(rs1.next()){
				RouteDto dto=new RouteDto();
				dto.setRouteId(rs.getInt("id"));
				dto.setRouteName(rs.getString("routename"));
				dto.setVehicleType(rs.getString("vehicle"));
				dto.setInOut(rs.getString("logtype"));
				dto.setVehicleCount(rs1.getString("count"));
				dto.setTime(rs.getString("time"));
				list.add(dto);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getAutogeneratedRoutelistWithLog" + e);
		} finally {
			DbConnect.closeResultSet(rs,rs1);
			DbConnect.closeStatement(st,st2);
			DbConnect.closeConnection(con);
		}
		return list;
	}
	public RouteDto getAutogeneratedRouteWithLog(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto dto=new RouteDto();
		try{
			String qry="select id,routename,logtype,vehicle,time from route where id="+routeid;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(qry);
			while(rs.next()){

				dto.setRouteId(rs.getInt("id"));
				dto.setRouteName(rs.getString("routename"));
				dto.setVehicleType(rs.getString("vehicle"));
				dto.setInOut(rs.getString("logtype"));
				dto.setTime(rs.getString("time"));
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getAutogeneratedRouteWithLog" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	
	//Autoroutes
	public int updateAutoRouteWithLog(String routeid,String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids,String time) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		int result=0;
		try{
			String qry="UPDATE route SET siteId='0', routeType='no' WHERE id="+routeid;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			result=st.executeUpdate(qry);
			if(result>0){
				result=createAutoRoutewithLog(routename, routeType, points,
						landmarkName, vehiceType, empids,time);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@updateAutoRouteWithLog" + e);
		} finally {
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return result;
	}

	public int createShuttleRoute(String routename, String logType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time,String[] compDist,String[] noodleDist) {
		
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement st1 = null, st2 = null;
		Statement st = null,st3=null,st4=null,st5=null;
		ResultSet keys = null, key1 = null,rs=null;
		int result = 0;
		int routeId = 0;
		System.out.println(noodleDist[0]+"NOOdle Dist "+noodleDist.length);
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String qry = "insert into route(routeName,siteId,type,logType,routeType,vehicle,time) values('"
					+ routename + "','1','P','" + logType + "','auto','"+vehiceType+"','"+time+"')";
			st1 = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
			result = st1.executeUpdate();
			keys = st1.getGeneratedKeys();
			
			if (keys.next()) {
				routeId = keys.getInt(1);
			}
			if (routeId > 0) {
				int order = 0;
				for (int z = 0; z < points.length; z++) {
					String lat = points[z].replace("*", "/").split("/")[0];
					String lng = points[z].replace("*", "/").split("/")[1];
					String checkqry="select id from landmark where latitude='"+lat+"' and longitude='"+lng+"'";
					st4=con.createStatement();
					rs=st4.executeQuery(checkqry);
					int landmark = 0;
					if(rs.next()){
						landmark=rs.getInt("id");
					}else{
					String insertlandmark = "INSERT INTO landmark (landmark, latitude, longitude,place) VALUES ('"
							+ landmarkName[z]
							+ "', '"
							+ lat
							+ "', '"
							+ lng
							+ "','0');";
					st2 = con.prepareStatement(insertlandmark,
							Statement.RETURN_GENERATED_KEYS);
					
					st2.executeUpdate();
					key1 = st2.getGeneratedKeys();
					if (key1.next()) {
						landmark = key1.getInt(1);
					}
					}
					String insertqry = "insert into routechild(routeId, position, landmarkId) values("
							+ routeId + "," + order + ",'" + landmark + "')";
					st = con.createStatement();
					st.executeUpdate(insertqry);
					order++;
				}
				if(empids.length>0){
				//updating employee
					String subqry1="";
					
				for(int s=0;s<empids.length;s++){
					st3=con.createStatement();
					String mapqry="INSERT INTO emp_route_map (empid, routeid, position, noodledist, compdist) VALUES ("+empids[s]+", '"+routeId+"', '"+(s+1)+"', '"+noodleDist[s]+"', '"+compDist[s]+"')";
					System.out.println("emp Map"+mapqry);
					st3.executeUpdate(mapqry);
					subqry1+=empids[s]+"";
					if(s<empids.length-1){
						subqry1+=",";
					}
				}
				String sub2="";
				if(logType.equalsIgnoreCase("IN")){
					sub2=" routeIn= '"+routeId+"'";
				}else{
					sub2=" routeOut= '"+routeId+"'";
				}
				String empqry="UPDATE employee_tripping SET "+sub2+" where empid in ("+subqry1+")";
				System.out.println(empqry);
				st5=con.createStatement();
				st5.executeUpdate(empqry);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@createShuttleRoute" + e);
		} finally {
			DbConnect.closeResultSet(keys, key1,rs);
			DbConnect.closeStatement(st, st1, st2,st3,st4,st5);
			DbConnect.closeConnection(con);
		}
		return result;

		
	}

	public ArrayList<RouteDto> getShuttleRoutes() {
		// TODO Auto-generated method stub
				DbConnect ob = null;
				Connection con = null;
				Statement st = null,st2=null;
				ResultSet rs = null,rs1=null;
				ArrayList<RouteDto> list = new ArrayList<RouteDto>();
				try{
					String qry="select id,routename,logtype,vehicle,time from route where routeType='auto'";
					ob = DbConnect.getInstance();
					con = ob.connectDB();
					st=con.createStatement();
					st2=con.createStatement();
					rs=st.executeQuery(qry);
					while(rs.next()){
						String qry2="select count(id) as count from emp_route_map where routeid="+rs.getInt("id");
						rs1=st2.executeQuery(qry2);
						if(rs1.next()){
						RouteDto dto=new RouteDto();
						dto.setRouteId(rs.getInt("id"));
						dto.setRouteName(rs.getString("routename"));
						dto.setVehicleType(rs.getString("vehicle"));
						dto.setInOut(rs.getString("logtype"));
						dto.setVehicleCount(rs1.getString("count"));
						dto.setTime(rs.getString("time"));
						list.add(dto);
						}
					}
				} catch (Exception e) {

					System.out.println("Error RouteDao@getShuttleRoutes" + e);
				} finally {
					DbConnect.closeResultSet(rs,rs1);
					DbConnect.closeStatement(st,st2);
					DbConnect.closeConnection(con);
				}
				return list;
	}

	public ArrayList<GeoTagDto> getEmployeeFromShuttleRoute(String routeid) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<GeoTagDto> list=new ArrayList<GeoTagDto>();
		try{
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			String qry="select e.id,e.displayname,e.personnelno,e.emp_lat,emp_long,em.noodledist,em.compdist from employee e, emp_route_map em where e.id=em.empid and em.routeid="+routeid;
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayname"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setDistanceperadmin(Double.parseDouble( rs.getString("noodledist")));
				dto.setDistanceperadminout(Double.parseDouble(rs.getString("compdist")));
				list.add(dto);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getEmployeeFromShuttleRoute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return list;


	}

	public int createShuttleRoute1(String routename, String logType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time, String[] compDist,
			String[] noodleDist, String filter,String TravelTime,String siteid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement st1 = null, st2 = null;
		Statement st = null,st3=null,st6=null;
		ResultSet keys = null, key1 = null;
		int result = 0;
		int routeId = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String qry = "insert into route(routeName,siteId,type,logType,routeType,vehicle,time,filter,traveltime) values('"
					+ routename + "','"+siteid+"','P','" + logType + "','auto','"+vehiceType+"','"+time+"','"+filter+"','"+TravelTime+"')";
			st1 = con.prepareStatement(qry, Statement.RETURN_GENERATED_KEYS);
			result = st1.executeUpdate();
			keys = st1.getGeneratedKeys();
			
			if (keys.next()) {
				routeId = keys.getInt(1);
			}
			if (routeId > 0) {
				int order = 0;
				for (int z = 0; z < points.length; z++) {
					String lat = points[z].replace("*", "/").split("/")[0];
					String lng = points[z].replace("*", "/").split("/")[1];;
					String insertlandmark = "INSERT INTO landmark (landmark, latitude, longitude,place) VALUES ('"
							+ landmarkName[z]
							+ "', '"
							+ lat
							+ "', '"
							+ lng
							+ "','0');";
					st2 = con.prepareStatement(insertlandmark,
							Statement.RETURN_GENERATED_KEYS);
					int landmark = 0;
					st2.executeUpdate();
					key1 = st2.getGeneratedKeys();
					if (key1.next()) {
						landmark = key1.getInt(1);
					}

					String insertqry = "insert into routechild(routeId, position, landmarkId) values("
							+ routeId + "," + order + ",'" + landmark + "')";
					st = con.createStatement();
					st.executeUpdate(insertqry);
					order++;
				}
				if(empids.length>0){
				//updating employee
				st3=con.createStatement();
				String subqry1="";
				for(int s=0;s<empids.length;s++){
						st6=con.createStatement();
						String mapqry="INSERT INTO emp_route_map (empid, routeid, position, noodledist, compdist) VALUES ("+empids[s]+", '"+routeId+"', '"+(s+1)+"', '"+noodleDist[s]+"', '"+compDist[s]+"')";
						System.out.println("emp Map"+mapqry);
						st6.executeUpdate(mapqry);
					
					subqry1+=empids[s]+"";
					if(s<empids.length-1){
						subqry1+=",";
					}
				}
				String empqry="";
				if(logType.equalsIgnoreCase("IN")){
					empqry="update employee set adminroute1='"+routeId+"' where id in ("+subqry1+")";
				}else{
					empqry="update employee set adminoutRoute='"+routeId+"' where id in ("+subqry1+")";
				}
				
				System.out.println(empqry);
				st3.executeUpdate(empqry);
				}
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@createShuttleRoute1" + e);
		} finally {
			DbConnect.closeResultSet(keys, key1);
			DbConnect.closeStatement(st, st1, st2,st3,st6);
			DbConnect.closeConnection(con);
		}
		return result;


	}
	public RouteDto getAutogeneratedRouteWithFilter(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		RouteDto dto=new RouteDto();
		try{
			String qry="select id,routename,logtype,vehicle,time,filter,siteId,traveltime from route where id="+routeid;
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			rs=st.executeQuery(qry);
			while(rs.next()){

				dto.setRouteId(rs.getInt("id"));
				dto.setRouteName(rs.getString("routename"));
				dto.setVehicleType(rs.getString("vehicle"));
				dto.setInOut(rs.getString("logtype"));
				dto.setTime(rs.getString("time"));
				dto.setSiteId(rs.getString("siteId"));
				dto.setDistance(rs.getString("traveltime"));
				dto.setPosition(rs.getString("filter")==null?"home":rs.getString("filter"));
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getAutogeneratedRouteWithFilter" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return dto;
	}
	public ArrayList<GeoTagDto> getEmployeeFromShuttleRoute1(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<GeoTagDto> list=new ArrayList<GeoTagDto>();
		try{
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			String qry="select id,displayname,personnelno,emp_lat,emp_long from employee where adminroute="+routeid;
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayname"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				list.add(dto);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getEmployeeFromShuttleRoute1" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return list;
	}

	public int disableAutoRoute(String routeid) {
		// TODO Auto-generated method stub
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null,st2=null;
		ResultSet rs=null;
		int result=0;
		try{
			String log="IN";
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			st2=con.createStatement();
			
			String getqry="select logtype from route  where id="+routeid;
			rs=st2.executeQuery(getqry);
			if(rs.next()){
				log=rs.getString("logtype");
			}
			String disableqry="update route set siteid=0,routeType='no'  where id="+routeid;
			result=st.executeUpdate(disableqry);
			if(result>0){
				String updateEmp="";
				if(log.equalsIgnoreCase("IN")){
					updateEmp="update employee set adminRoute1=0 where id in(select empid from emp_route_map where routeid="+routeid+")";
				}else{
					updateEmp="update employee set adminoutRoute=0 where id in(select empid from emp_route_map where routeid="+routeid+")";
				}
				System.out.println(updateEmp);
				st1=con.createStatement();
				st1.executeUpdate(updateEmp);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@disableAutoRoute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st,st1,st2);
			DbConnect.closeConnection(con);
		}
		return result;
	}
	public ArrayList<GeoTagDto> getEmployeeFromFilteredRoute(String routeid) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null;
		ResultSet rs = null;
		ArrayList<GeoTagDto> list=new ArrayList<GeoTagDto>();
		try{
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			String qry="select e.id,e.displayname,e.personnelno,e.emp_lat,emp_long,em.noodledist,em.compdist from employee e, emp_route_map em where e.id=em.empid and em.routeid="+routeid;
			
			rs=st.executeQuery(qry);
			while(rs.next()){
				GeoTagDto dto = new GeoTagDto();
				dto.setEmpid(rs.getString("id"));
				dto.setEmpName(rs.getString("displayname"));
				dto.setHomelat(rs.getString("emp_lat"));
				dto.setHomelong(rs.getString("emp_long"));
				dto.setPersonnelNo(rs.getString("personnelno"));
				dto.setDistanceperadmin(Double.parseDouble( rs.getString("noodledist")));
				dto.setDistanceperadminout(Double.parseDouble(rs.getString("compdist")));
				list.add(dto);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@getEmployeeFromFilteredRoute" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		
		return list;


	}

	public int UpdateAutoRouteWithFilter(String siteid,String routeid,String routename, String routeType,
			String[] points, String[] landmarkName, String vehiceType,
			String[] empids, String time, String[] compDist,
			String[] noodleDist, String filter,String TravelTime) {
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st1=null;
		int result=0;
		try{
			String qry="UPDATE route SET siteId='0', routeType='no' WHERE id="+routeid;
			String empqry="update employee set adminroute1='0' where adminroute1='"+routeid+"'";
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			st=con.createStatement();
			result=st.executeUpdate(qry);
			st1=con.createStatement();
			st1.executeUpdate(empqry);
			if(result>0){
				removeEmployeeFromRoute(routeid);
				result=createShuttleRoute1(routename, routeType, points,
						landmarkName, vehiceType, empids,time,compDist,noodleDist,filter,TravelTime,siteid);
			}
		} catch (Exception e) {

			System.out.println("Error RouteDao@UpdateAutoRouteWithFilter" + e);
		} finally {
			DbConnect.closeStatement(st,st1);
			DbConnect.closeConnection(con);
		}
		
		return result;
	}

	public int removeEmployeeFromRoute(String routeid){
		DbConnect ob = null;
		Connection con = null;
		Statement st = null,st2=null,st3=null;
		ResultSet rs=null;
		int result=0;
		try{
		
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			String log="";
			String getqry="select logtype from route  where id="+routeid;
			st2=con.createStatement();
			rs=st2.executeQuery(getqry);
			if(rs.next()){
				log=rs.getString("logtype");
			}
			
			String updateEmp="";
			if(log.equalsIgnoreCase("IN")){
				updateEmp="update employee set adminRoute1=0 where id in(select empid from emp_route_map where routeid="+routeid+")";
			}else{
				updateEmp="update employee set adminoutRoute=0 where id in(select empid from emp_route_map where routeid="+routeid+")";
			}
			System.out.println(updateEmp);
			st3=con.createStatement();
			st3.executeUpdate(updateEmp);
			
			String qry="delete from emp_route_map where routeid="+routeid;
			
			st=con.createStatement();
			result=st.executeUpdate(qry);
			
			
		} catch (Exception e) {

			System.out.println("Error RouteDao@removeEmployeeFromRoute" + e);
		} finally {
			DbConnect.closeStatement(st,st2,st3);
			DbConnect.closeConnection(con);
		}
		
		return result;

	}
}
