package com.agiledge.atom.dao;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.RouteDto;

public class RouteUpload {
	public ArrayList<RouteDto> routeDtos = new ArrayList<RouteDto>();
	public int count = 1;
	public String type;
	public String site;
	int truncateStatus = 0;
	int siteId = 0;
	private boolean truncate;

	public ArrayList<RouteDto> getRoutesFromExcel(String sheetName,
			String sourecDriver, int truncateStatus, int siteId)

	throws ClassNotFoundException, SQLException {
		this.truncateStatus = truncateStatus;
		this.siteId = siteId;
		Class.forName("sun.jdbc.odbc.JdbcOdbcDriver");
		Connection conExcel = null;
		PreparedStatement pstExcel = null;
		ResultSet rsExcel = null;
		RouteDto dtoExcel = null;
		try {
			conExcel = DriverManager.getConnection("jdbc:odbc:" + sourecDriver
					+ "");

			String excelQuery = "select * from [" + sheetName + "$]";
			pstExcel = conExcel.prepareStatement(excelQuery);
			rsExcel = pstExcel.executeQuery();

			while (rsExcel.next()) {
				dtoExcel = new RouteDto();
				dtoExcel.setRouteId((rsExcel.getInt(1)));
				// System.out.println("Route Id"+dtoExcel.getRouteId());
				dtoExcel.setArea(rsExcel.getString(2));
				dtoExcel.setPlace(rsExcel.getString(3));
				dtoExcel.setLandmark(rsExcel.getString(4));
				routeDtos.add(dtoExcel);
				if (dtoExcel.getRouteId() == 0) {
					count++;
				}
			}
		} catch (Exception e) {
			System.out.println("ERROR " + e);
			// e.printStackTrace();
		} finally {
			rsExcel.close();
			pstExcel.close();
			conExcel.close();
		}
		System.out.println("Taking Data from Excel Completed");
		return routeDtos;

	}

	public void printAll() {
		for (RouteDto dto : routeDtos) {
			System.out.println(dto.getArea() + "  " + dto.getPlace() + "  "
					+ dto.getLandmark());

		}
		insertRoutesToDB();
	}
	public int insertRoutesToDB() {
		DbConnect ob = DbConnect.getInstance();
		Connection con = ob.connectDB();

		PreparedStatement pstDeleteParent = null;
		PreparedStatement pstDeleteChild = null;
		PreparedStatement pst = null;
		PreparedStatement pst1 = null;
		ResultSet keys = null;
		int index = 0;
		int position = 0;
		try {
			
			con.setAutoCommit(false);
		 	if (truncate) {
		 
		 		System.out.println("delete from routeChild where routeId  in (select id from route where siteId=" + Integer.parseInt(site) + " and type='" + type + "' )");
		 		System.out.println(" delete from route where siteId=" +  Integer.parseInt(site) + " and type='" + type + "' ");
		 		pstDeleteChild = con
						.prepareStatement("delete from routeChild where routeId in (select id from route where siteId="
						+ Integer.parseInt(site) + " and type='" + type + "' )");
				int childCount=pstDeleteChild.executeUpdate();
				if(childCount>0) {
					System.out.println("" + childCount + " childs deleted");
				}
				
				pstDeleteParent = con.prepareStatement("delete from route where siteId="
						+ Integer.parseInt(site) + " and type='" + type + "' ");
				int parentCount =  pstDeleteParent.executeUpdate();
				if(parentCount>0) {
					System.out.println("" + parentCount + " parentss deleted");
				}
				
				 
				DbConnect.closeStatement(pstDeleteChild,pstDeleteParent);
			}
		 	
 
			pst = con.prepareStatement(
					"insert into route(routeName,siteId,type) values (?,?,?) ",
					Statement.RETURN_GENERATED_KEYS);
			index = 0;
			System.out.println("count :"+ count);
			for (int i = 1; i <= count; i++) {
				pst.setString(1, "" + i);
				pst.setInt(2, Integer.parseInt(site));
				pst.setString(3, type);
				pst.executeUpdate();
System.out.println("P." + i);
				keys = pst.getGeneratedKeys();
				pst1 = con
						.prepareStatement("insert into routeChild(routeId,position,landmarkId) (select ?,?,l.id  FROM area a,place p,landmark l where a.area=?   and p.place=? and l.landmark=?   and l.place=p.id and p.area=a.id) ");

				if (keys.next()) {
					position = 0;
					while (routeDtos.size() > index) {
						RouteDto dto = routeDtos.get(index);
						// System.out.println("Landmark"+dto.getLandmark());
						System.out.print(". "+index);
						index++;
						if (dto.getRouteId() == 0) {
							break;
						} else {
							System.out.println("insert ing ;"+dto.getLandmark());
							pst1.setInt(1, keys.getInt(1));
							pst1.setInt(2, position);
							pst1.setString(3, dto.getArea());
							pst1.setString(4, dto.getPlace());
							pst1.setString(5, dto.getLandmark());
							pst1.executeUpdate();
							position++;
						}
					}
				}
			}
			con.commit();
			System.out.println("commited .. ");
			pst1.close();
			pst.close();
			keys.close();
			con.close();
			return 1;
		} catch (Exception e) {
			System.out.println("ERROR IN Insert " + e);
			e.printStackTrace();
			try {
				pst1.close();
				pst.close();
				keys.close();
				con.close();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
				return 0;
			}

			return 0;

		}
	}

	public boolean isTruncate() {
		return truncate;
	}

	public void setTruncate(boolean truncate) {
		this.truncate = truncate;
	}
}