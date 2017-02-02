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

import com.agiledge.atom.constants.AuditLogConstants;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.AuditLogDTO;
import com.agiledge.atom.dto.ModifyTripDto;
import com.agiledge.atom.trip.dto.AplDistanceDto;

/**
 * 
 * @author muhammad
 */
public class ModifyTripDao {
	int siteLandmarkId;
	private boolean distanceInDb = true;
	private ArrayList<AplDistanceDto> aplDistanceList;
	private ArrayList<AplDistanceDto> landmarkDistanceList;
	// public boolean testCase;
	private ArrayList<String> errorMessageList = new ArrayList<String>();

	public ArrayList<AplDistanceDto> getAplDistanceList() {
		return aplDistanceList;
	}

	public void setAplDistanceList(ArrayList<AplDistanceDto> aplDistanceList) {
		this.aplDistanceList = aplDistanceList;
	}

	public int modifyTrip(ArrayList<ModifyTripDto> modifyTripDetails,
			String tripid, String doneBy, String tripMode, String siteId,
			float[] distances, float[] times, String tripDate, String tripTime) {
		int autoincNumber = 0, changedBy = 0;
		changedBy = Integer.parseInt(doneBy);
		DbConnect ob = null;
		int returnInt = -1;
		Connection con = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			int order = 1;
			String vehicleType = null;
			String escort=null;
			if(modifyTripDetails!=null && modifyTripDetails.size()>0)
			{
				vehicleType=modifyTripDetails.get(0).getVehicleType();
				escort= modifyTripDetails.get(0).getEscort();
			}
			pst = con
					.prepareStatement("select * from trip_details where id =?");
			pst.setString(1, tripid);
			rs = pst.executeQuery();
			if (!rs.next()) {								
				pst1 = con
						.prepareStatement(
								"insert into trip_details (trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle_type,status,security_status,distance,travelTime,routingType) values (?,?,?,?,?,?,?,?,?,?,?,?) ",
								Statement.RETURN_GENERATED_KEYS);
				

				int j = 1;
				pst1.setString(j, tripid);
				j++;
				pst1.setString(j, siteId);
				j++;
				pst1.setString(j, tripDate);
				j++;
				pst1.setString(j, tripTime);
				j++;
				pst1.setString(j, tripMode);
				j++;
				pst1.setString(j, "0");
				j++;
				pst1.setString(j, vehicleType);
				j++;
				pst1.setString(j, "added");
				j++;
				pst1.setString(j, escort);
				j++;
				pst1.setFloat(j, totalDistance);
				j++;
				pst1.setFloat(j, totalTime);
				j++;
				pst1.setString(j, "o");
				j++;
				pst1.executeUpdate();
				System.out.println("track...3");
				ResultSet keys = pst1.getGeneratedKeys();
				if (keys.next()) {
					autoincNumber = Integer.parseInt(keys.getString(1));
					auditLogEntryforedittrip(
							autoincNumber,
							AuditLogConstants.TRIPSHEET_MODULE,
							changedBy,
							AuditLogConstants.WORKFLOW_STATE_TRIPSHEET_GENERATED,
							AuditLogConstants.WORKFLOW_STATE_TRIPS_ADDED,
							AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
					tripid = keys.getString(1);
				}
				System.out.println("track...3"+tripid);

			}
			
				pst = null;
			pst1 = con
						.prepareStatement("update trip_details set status=?,distance=?,travelTime=?,security_status=?,vehicle_type=? where id=?");

			
					pst1.setString(1, "modified");			
				pst1.setString(2, "" + totalDistance);
				pst1.setString(3, "" + totalTime);
				pst1.setString(4, escort);
				pst1.setString(5, vehicleType);
				pst1.setString(6, tripid);
				returnInt += pst1.executeUpdate();
				System.out.println("Track...5"+tripid);
				autoincNumber = Integer.parseInt((String) tripid);
				auditLogEntryformodifytrip(autoincNumber,
						AuditLogConstants.TRIPSHEET_MODULE, changedBy,
						AuditLogConstants.WORKFLOW_STATE_TRIPSHEET_GENERATED,
						AuditLogConstants.WORKFLOW_STATE_TRIPS_MODIFIED,
						AuditLogConstants.AUDIT_LOG_EDIT_ACTION);
				if (modifyTripDetails != null && !(modifyTripDetails.isEmpty())) {
					pst = con
							.prepareStatement("insert into trip_details_child(tripid,employeeId,landmarkId,routedOrder,scheduleId,dist,time) values(?,?,?,?,?,?,?)");
					int i = 0;
					for (ModifyTripDto modifyTripDto : modifyTripDetails) {

						pst.setString(1, tripid);
						pst.setString(2, modifyTripDto.getEmployeId());
						pst.setString(3, modifyTripDto.getLandmarkId());
						pst.setInt(4, order);
						pst.setString(5, modifyTripDto.getScheduleId());
						if (distances != null && distances.length > i) {
							pst.setFloat(6, distances[i]);
							pst.setFloat(7, times[i]);
						} else {
							pst.setString(6, null);
							pst.setString(7, null);
						}

						returnInt += pst.executeUpdate();
						System.out.println("Track...6"+tripid);
						order++;
						i++;
					}

				}			
		} catch (Exception e) {
			System.out.println("Error should not" + e);		
		}finally{
				DbConnect.closeResultSet(rs);
				DbConnect.closeStatement(pst);
				DbConnect.closeConnection(con);

			

		}

		return returnInt;
	}

	private void auditLogEntryformodifytrip(int autoincNumber, String Module,
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

	public int addToRemovedtrip(ArrayList<ModifyTripDto> modifyTripDtos,
			String doneBy, String tripMode, String siteId, String tripDate,
			String tripTime) {
		DbConnect ob = null;
		Connection con = null;
		PreparedStatement pst = null;
		Statement st = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		int retVal = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			//pst = con.prepareStatement("truncate table trip_details_removed");
			//pst.executeUpdate();
			String query = "INSERT INTO trip_details_removed(siteId,trip_date,trip_time,in_out,employeeId,landmarkId,scheduleId,status) values (?,?,?,?,?,?,?,?)";
			System.out.println(query);
			pst = con.prepareStatement(query);
			System.out.println(modifyTripDtos.size());
			for (ModifyTripDto modifyTripDto : modifyTripDtos) {
				pst.setString(1, siteId);
				pst.setString(2, tripDate);
				pst.setString(3, tripTime);
				pst.setString(4, tripMode);
				pst.setString(5, modifyTripDto.getEmployeId());
				pst.setString(6, modifyTripDto.getLandmarkId());
				pst.setString(7, modifyTripDto.getScheduleId());
				pst.setString(8, "removed");
				retVal += pst.executeUpdate();
			}
			DbConnect.closeStatement(pst);

		} catch (Exception e) {
			System.out.println("Error in Add to Removed dao" + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(pst, st);
			DbConnect.closeConnection(con);

		}
		return retVal;
	}

	private void auditLogEntryforedittrip(int autoincNumber, String Module,
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

	public float[] getDistances(int[] landmarks, String inOut) {
		setDistanceInDb(true);
		PreparedStatement pst = null;
		ResultSet rs = null;
		float totalDistance = (float) 0.0;
		float distance[] = new float[landmarks.length];
		aplDistanceList = new ArrayList<AplDistanceDto>();
		landmarkDistanceList = new ArrayList<AplDistanceDto>();

		Connection con = null;
		DbConnect ob = null;

		String query = "select distance from distchart where (srcId=? and destId=?) or (destId=? and srcId=?)";
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst = con.prepareStatement(query);
			int srcId = 0;
			int destId = 0;
			
			if (inOut.equalsIgnoreCase("OUT")) {
				
				for (int i = 0; i < landmarks.length; i++) {

					if (i == 0) {
						srcId = siteLandmarkId;		
						AplDistanceDto aplDto1 = new AplDistanceDto();
						aplDto1.setSourceId(String.valueOf(srcId));
						aplDto1.setDistance(0); 
						landmarkDistanceList.add(aplDto1);

					} else {
						srcId = landmarks[i-1];
						// destId = landmarks[i-1];
					}
					destId = landmarks[i];
					
					pst.setInt(1, srcId);
					pst.setInt(2, destId);
					pst.setInt(3, srcId);
					pst.setInt(4, destId);

					rs = pst.executeQuery();
					if (rs.next()) {
						distance[i] = rs.getFloat(1);
						System.out.println("OUT           "+"Sorce Id"+srcId+"Dest ID"+destId+"I="+i+"  distance[i]"+distance[i]);
						totalDistance += rs.getFloat(1);
					} else {

						if (srcId != destId) {
							System.out.println("distance in DB False");
							setDistanceInDb(false);
						}
					}
					AplDistanceDto aplDto1 = new AplDistanceDto();
					aplDto1.setSourceId(String.valueOf(destId));
					aplDto1.setDistance(distance[i]); 
					landmarkDistanceList.add(aplDto1);
					/* src, dest setup for db insert */
					AplDistanceDto aplDDto = new AplDistanceDto();
					aplDDto.setSourceId(String.valueOf(srcId));
					aplDDto.setTargetId(String.valueOf(destId));
					aplDDto.setDistance(distance[i]);
					aplDistanceList.add(aplDDto);

				}

			} else if (inOut.equalsIgnoreCase("IN")) {
				for (int i = 0; i <=landmarks.length ; i++) {

					if (i == 0) {

						/*
						 * srcId = landmarks[landmarks.length-1];
						 * destId=siteLandmarkId;
						 */
						distance[i] = 0;
						srcId = landmarks[i];
						 AplDistanceDto aplDto1 = new AplDistanceDto();
						 aplDto1.setSourceId(String.valueOf(landmarks[i]));
						 aplDto1.setDistance(0);
						 landmarkDistanceList.add(aplDto1);						

					} else	if (i == landmarks.length) {
						float tempDistance=0.0f;
						System.out.println(i + " -> " + siteLandmarkId);
						srcId = landmarks[i-1];
						destId = siteLandmarkId;
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
						rs = pst.executeQuery();
						if (rs.next()) {						
							
							tempDistance = rs.getFloat(1);
							totalDistance += tempDistance;
							System.out.println("IN           "+"Sorce Id"+srcId+"Dest ID"+destId+"I="+i+"  distance[i]"+tempDistance);						
					} else {

						if (srcId != destId) {
							setDistanceInDb(false);
						}

					}																		

						/* adding the site landmarks in queue */
					
					AplDistanceDto aplDto2 = new AplDistanceDto();
					aplDto2.setSourceId(String.valueOf(destId));
					aplDto2.setDistance(tempDistance); 
					landmarkDistanceList.add(aplDto2);
					/* src, dest setup for db insert */
					AplDistanceDto aplDDto = new AplDistanceDto();
					aplDDto.setSourceId( String.valueOf(srcId) );
					aplDDto.setTargetId(String.valueOf(destId));
					aplDDto.setDistance(tempDistance);
					aplDistanceList.add(aplDDto);


										
					} else {
						System.out.println(i + " -> " + landmarks[i]);
						srcId = landmarks[i - 1];
						destId = landmarks[i];
											
						pst.setInt(1, srcId);
						pst.setInt(2, destId);
						pst.setInt(3, srcId);
						pst.setInt(4, destId);
						rs = pst.executeQuery();
						//float distanceTemp = 0f;
						if (rs.next()) {						
							distance[i] = rs.getFloat(1);
							totalDistance += rs.getFloat(1);
							System.out.println("IN           "+"Sorce Id"+srcId+"Dest ID"+destId+"I="+i+"  distance[i]"+distance[i]);						
					} else {

						if (srcId != destId) {
							setDistanceInDb(false);
						}

					}																		

						/* adding the site landmarks in queue */

					AplDistanceDto aplDto1 = new AplDistanceDto();
					aplDto1.setSourceId(String.valueOf(destId));
					aplDto1.setDistance(distance[i]); 
					landmarkDistanceList.add(aplDto1);
					
					/* src, dest setup for db insert */
					AplDistanceDto aplDDto = new AplDistanceDto();
					aplDDto.setSourceId( String.valueOf(srcId) );
					aplDDto.setTargetId(String.valueOf(destId));
					aplDDto.setDistance(distance[i]);
					aplDistanceList.add(aplDDto);
					}
				}

			}

			/*
			 * for(int i=1;i<landmarkDistanceList.size();i++) { // src, dest
			 * setup for db insert AplDistanceDto aplDDto = new
			 * AplDistanceDto();
			 * aplDDto.setSourceId(String.valueOf(landmarkDistanceList
			 * .get(i-1).getSourceId()));
			 * aplDDto.setTargetId(String.valueOf(landmarkDistanceList
			 * .get(i).getSourceId()));
			 * aplDDto.setDistance(landmarkDistanceList.get(i).getDistance() );
			 * aplDistanceList.add(aplDDto); }
			 */

		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		System.out.println("totalDistance"+totalDistance);
		setTotalDistance(totalDistance);
		return distance;
	}

	public float[] getTime(String inOut, float[] distance, String tripTime) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		float time[] = new float[distance.length];
		float totalMinutes = (float) 0.0;
		float currentDistance = (float) 0.0;
		String query = "select ROUND( ?/speedpkm*60,0)as time from timeSloat where startTime<=? and endTime>?";
		System.out.println(query + distance.length);
		Connection con = null;
		DbConnect ob = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst = con.prepareStatement(query);
			if (inOut.equals("IN")) {

				for (int i = 0; i < distance.length; i++) {
					if (i == 0) {
						currentDistance = totalDistance;
					}
					currentDistance -= distance[i];
					pst.setFloat(1, currentDistance);
					pst.setString(2, tripTime);
					pst.setString(3, tripTime);
					System.out.println(currentDistance + tripTime);
					rs = pst.executeQuery();
					if (rs.next()) {
						time[i] = rs.getFloat("time");
						if (i == 0)
							totalMinutes = time[i];
					}
				}
				/*
				 * for (int i = distance.length - 1; i >= 0; i--) { if (i == 0)
				 * { pst.setFloat(1, routingDto.getDistance() - sumOfistances);
				 * } else { sumOfistances += distance[i]; pst.setFloat(1,
				 * distance[i]); } pst.setString(2, routingDto.getTime());
				 * pst.setString(3, routingDto.getTime());
				 * 
				 * rs = pst.executeQuery(); if (rs.next()) { time[i] =
				 * rs.getFloat("time"); totalMinutes += time[i]; } }
				 */
			} else {
				for (int i = 0; i < distance.length; i++) {
					pst.setFloat(1, distance[i]);
					pst.setString(2, tripTime);
					pst.setString(3, tripTime);
					rs = pst.executeQuery();
					if (rs.next()) {
						time[i] = rs.getFloat("time");
						totalMinutes += time[i];
					}
				}
			}
			setTotalTime(totalMinutes);
		} catch (Exception e) {
			e.printStackTrace();
		}
		finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		return time;

	}

	public void getSitelandmark(String siteId)

	{

		PreparedStatement pst = null;

		Connection con = null;
		DbConnect ob = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
			pst = con.prepareStatement("select landmark from site where id="
					+ siteId);
			ResultSet rs = pst.executeQuery();
			if (rs.next()) {
				siteLandmarkId = rs.getInt(1);
			}

		} catch (Exception e) {
			System.out.println("error in update" + e);
		}finally{
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
	}

	public boolean isDistanceInDb() {
		return distanceInDb;
	}

	public void setDistanceInDb(boolean distanceInDb) {
		this.distanceInDb = distanceInDb;
	}

	public ArrayList<AplDistanceDto> getLandmarkDistanceList() {
		return landmarkDistanceList;
	}

	public ArrayList<String> getErrorMessageList() {
		return errorMessageList;
	}

	public void setErrorMessageList(ArrayList<String> errorMessageList) {
		this.errorMessageList = errorMessageList;
	}

	public float getTotalDistance() {
		return totalDistance;
	}

	public void setTotalDistance(float totalDistance) {
		this.totalDistance = totalDistance;
	}

	public float getTotalTime() {
		return totalTime;
	}

	public void setTotalTime(float totalTime) {
		this.totalTime = totalTime;
	}

	private float totalDistance;
	private float totalTime;

	public void keepOrginal(String[] tripIds) {
		DbConnect ob = null;
		int returnInt = -1;
		Connection con = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet rs = null;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();
		pst = con
				.prepareStatement("select * from trip_details_parent_modified where tripid =?");
		for(String tripid:tripIds)
		{
		pst.setString(1, tripid);
		rs = pst.executeQuery();		
		if (!rs.next()) {		
		pst1 = con
				.prepareStatement("insert into trip_details_parent_modified (tripid, trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle_type,status,security_status,distance,travelTime,destinationLandmark,preTripId,distanceFromPreTrip,vehicle,routingType,driverPswd,driverId,escortId,escortPswd)select id,trip_code,siteId,trip_date,trip_time,trip_log,routeId,vehicle_type,status,security_status,distance,travelTime,destinationLandmark,preTripId,distanceFromPreTrip,vehicle,routingType,driverPswd,driverId,escortId,escortPswd from trip_details where id="
						+ tripid+" and status='routed'");
		int retVal=pst1.executeUpdate();
		System.out.println("track...1"+tripid);
		if(retVal>0)
		{
		pst1 = con
				.prepareStatement("INSERT INTO trip_details_modified (tripid, empid, landmarkId, routedOrder,  scheduleId, dist,time, transportType)  SELECT tripId, employeeId,landmarkId,routedOrder, scheduleId,dist,time, transportType FROM trip_details_child where tripid="
						+ tripid);
		pst1.executeUpdate();		
		}
		}
		pst1 = con
				.prepareStatement("update  trip_details set status='removed'  where id="
						+ tripid);
		pst1.executeUpdate();
		pst1 = con
				.prepareStatement("delete from trip_details_child  where tripid="
						+ tripid);
		pst1.executeUpdate();
		}
		}catch(Exception e)
		{
		System.out.println("Error in keep orginal"+e);
		}
		finally{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);

		}
		}

}
