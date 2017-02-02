package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.TimeSloatDto;


public class TimeSloatDao {

	public int addTimeSloat(ArrayList<TimeSloatDto> timeSloats)
			throws Exception {
		DbConnect ob = null;// DbConnect.getInstance();
		Connection con = null;// ob.connectDB();
		PreparedStatement pst = null;
		int retVal = 0;
		try {
			ob = DbConnect.getInstance();
			con = ob.connectDB();

			pst = con.prepareStatement("truncate table timeSloat");
			pst.executeUpdate();
			pst = con
					.prepareStatement("insert into timeSloat(startTime,endTime,traffic,speedpkm) values (?,?,?,?)");
			for (TimeSloatDto dto : timeSloats) {
				pst.setString(1, dto.getTimeStart());
				pst.setString(2, dto.getTimeEnd());
				pst.setString(3, dto.getTraffic());
				pst.setString(4, dto.getSpeed());
				retVal += pst.executeUpdate();
			}
		} finally {
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return retVal;
	}

	public ArrayList<TimeSloatDto> getTimeSloats() {
		DbConnect ob = null;// DbConnect.getInstance();
		Connection con = null;// ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		ArrayList<TimeSloatDto> timeSloatDtos = new ArrayList<TimeSloatDto>();
		TimeSloatDto dto = new TimeSloatDto();
		ob = DbConnect.getInstance();
		con = ob.connectDB();
		try {
			pst = con.prepareStatement("select * from timeSloat");

			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new TimeSloatDto();
				dto.setTraffic(rs.getString("traffic"));
				dto.setTimeStart(rs.getString("startTime"));
				dto.setTimeEnd(rs.getString("endTime"));
				dto.setSpeed(rs.getString("speedpkm"));
				timeSloatDtos.add(dto);
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return timeSloatDtos;
	}

}
