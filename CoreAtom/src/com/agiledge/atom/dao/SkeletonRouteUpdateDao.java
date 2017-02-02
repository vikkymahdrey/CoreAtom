package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map.Entry;
import java.util.TreeMap;

import com.agiledge.atom.dbConnect.DbConnect;


/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 * 
 * @author Shahid
 */
public class SkeletonRouteUpdateDao {

	public static final int R = 6371;

	public void UpdateRoute(int numberofRoute) {
		ArrayList<Integer> nodes = new ArrayList<Integer>();
		TreeMap<Float, Integer> nodeinord = new TreeMap<Float, Integer>();
		ArrayList<Integer> ordrelist = new ArrayList<Integer>();
		float d = (float) 0.0;
		Statement st = null;// con.createStatement();
		Statement st1 = null;// con.createStatement();
		ResultSet rs = null;
		ResultSet rs1 = null;
		Connection con = null;
		try {
			DbConnect ob = DbConnect.getInstance();
			con = ob.connectDB();
			st = con.createStatement();
			st1 = con.createStatement();
			float lat1 = (float) 0.0;
			float lon1 = (float) 0.0;
			float lat2 = (float) 0.0;
			float lon2 = (float) 0.0;
			float sitelat = (float) 0.0;
			float sitelon = (float) 0.0;
			for (int i = 1; i <= numberofRoute; i++) {
				int cupos = 0;
				rs = st.executeQuery("SELECT id,latitude,longitude FROM landmark where id=(select landmark from site)");
				if (rs.next()) {
					sitelat = rs.getFloat(2);
					sitelon = rs.getFloat(3);
				}
				rs = st.executeQuery("SELECT l.id,l.latitude,l.longitude,rc.position FROM landmark l,routeChild rc WHERE rc.routeid="
						+ i
						+ " and l.id =rc.landmarkId ORDER BY rc.position DESC");
				while (rs.next()) {
					nodes.add(rs.getInt(1));
				}
				rs = st.executeQuery("SELECT id,latitude,longitude FROM landmark,routeChildSample rcs WHERE rcs.routeid ="
						+ i
						+ " and landmark.id =rcs.landmarkId ORDER BY rcs.position DESC");
				while (rs.next()) {
					cupos++;
					lat1 = rs.getFloat(2);
					lon1 = rs.getFloat(3);
					nodeinord.put(getDistance(sitelat, sitelon, lat1, lon1),
							rs.getInt(1));
					rs1 = st1
							.executeQuery("SELECT id,latitude,longitude FROM landmark WHERE id NOT IN "
									+ "(SELECT id FROM landmark,routeSample WHERE landmark.id =routeSample.landmarkId) order by id");
					while (rs1.next()) {
						lat2 = rs1.getFloat(2);
						lon2 = rs1.getFloat(3);
						d = getDistance(lat1, lon1, lat2, lon2);
						if (d < 1) {
							if (!nodes.contains(rs1.getInt(1))) {
								nodes.add(cupos, rs1.getInt(1));
								nodeinord.put(
										getDistance(sitelat, sitelon, lat2,
												lon2), rs1.getInt(1));
								cupos++;
							}
						}

						// System.out.println("distance" + d);
					}
				}
				for (Iterator<Entry<Float, Integer>> it = nodeinord.entrySet()
						.iterator(); it.hasNext();) {
					Entry<Float, Integer> entry = it.next();
					Object dist = entry.getKey();
					Object value = entry.getValue();
					ordrelist.add((Integer) value);
				}
				System.out.println(nodes);
				System.out.println(ordrelist);
				// st.executeUpdate("truncate interroute");
				PreparedStatement pst = con
						.prepareStatement("insert into routeChild(routeId,position,landmarkId) values (?,?,?)");
				for (int t = 0; t < ordrelist.size(); t++) {
					pst.setInt(1, i);
					pst.setInt(2, t);
					pst.setInt(3, Integer.parseInt(ordrelist.get(t).toString()));
					pst.addBatch();
				}
				pst.executeBatch();
				nodes.clear();
				ordrelist.clear();
				nodeinord.clear();
			}
		} catch (Exception e) {
			System.out.println("Exception On Connection : " + e);
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}
	}

	float getDistance(float lat1, float lon1, float lat2, float lon2) {
		float dLat = (float) ((lat2 - lat1) * (3.18 / 180));
		float dLon = (float) ((lon2 - lon1) * (3.18 / 180));
		lat1 = (float) (lat1 * (3.14 / 180));
		lat2 = (float) (lat2 * (3.14 / 180));

		float a = (float) (Math.sin(dLat / 2) * Math.sin(dLat / 2) + Math
				.sin(dLon / 2)
				* Math.sin(dLon / 2)
				* Math.cos(lat1)
				* Math.cos(lat2));
		float c = (float) (2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a)));
		float d = R * c;
		return (d);
	}
}
