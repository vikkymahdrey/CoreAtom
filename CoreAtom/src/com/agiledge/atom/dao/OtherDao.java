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

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.dto.SiteDto;



/**
 * 
 * @author Shahid
 */
public class OtherDao {

	DbConnect ob = DbConnect.getInstance();

	private OtherDao() {
	}

	public static OtherDao getInstance() {
		return OtherFunctionsHolder.INSTANCE;
	}

	private static class OtherFunctionsHolder {

		private static final OtherDao INSTANCE = new OtherDao();
	}

	public void closeconnection(Connection con) throws SQLException {
		if (con != null) {
			con.close();
		}
	}

	public String getUserName(long uid) {
		Statement st = null;
		ResultSet rs = null;
		String username = "";
		Connection con = ob.connectDB();
		try {

			st = con.createStatement();
			rs = st.executeQuery("select displayname  from employee where id="
					+ uid + "");
			if (rs.next()) {
				username = rs.getString(1);
			}
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return username;
	}

	public String getEmployeesNotSccheduledforTomorrow(String date) {
		Statement st = null;
		ResultSet rs = null;
		String returnData = "";
		Connection con = ob.connectDB();
		try {
			st = con.createStatement();
			// rs =
			// st.executeQuery("select distinct displayname,PersonnelNo,'schedule cancelled' as status   from employee where id in (select es.employee_id from employee_schedule es	where  ( es.cancel_date is  null or es.cancel_date>'"
			// + date +
			// "' ) and  es.id in (select ea.scheduleId from employee_schedule_alter ea where  ea.date='"
			// + date +
			// "' and (ea.status='c' or ea.login_time='' or ea.login_time='none'))  )	union	select distinct displayname, PersonnelNo,'weekly off' from employee where id in ((select es.employee_id from employee_schedule es where  ( es.cancel_date is  null or es.cancel_date>'"
			// + date +
			// "' ) and  es.id in  (select ea.scheduleId from employee_schedule_alter ea where  ea.date='"
			// + date +
			// "' and  (ea.login_time='weekly off')))) union	select distinct displayname, PersonnelNo,'no schdule for the day' from employee where id in ((select employee_subscription.empID from employee_subscription where  subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
			// + date +
			// "') and empID not  in(select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'"
			// + date + "' ) and es1.from_date<='" + date +
			// "'  and es1.to_date>='" + date +
			// "'))) union select distinct displayname, PersonnelNo,'no schdule for the day' from employee where id in ( select employee_subscription.empID from employee_subscription where  ( subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
			// + date +
			// "') ) and empID  in (select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'"
			// + date +
			// "') and (login_time='none' or login_time='') and ( logout_time='none' or logout_time='' ) and  es1.id not in ( select scheduleId from employee_schedule_alter where date='"
			// + date + "' )  and es1.from_date<='" + date +
			// "'  and es1.to_date>='" + date +
			// "' ) ) union select  distinct displayname, PersonnelNo,'landmark not in route' from employee where id in ((select employee_subscription.empID from employee_subscription 	where  (   subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
			// + date +
			// "') )	 and landmark not 	in(select routeChild.landmarkId from routeChild)))");
			// modified on 06-02-2013 -> taking subscriptions at that date only

			rs = st.executeQuery("select distinct id staffId ,displayname,PersonnelNo,'schedule cancelled' as currentStatus from employee where id  in (select es.employee_id from employee_schedule es	where  ( es.cancel_date is  null or es.cancel_date>'"
					+ date
					+ "' ) and  es.id in (select ea.scheduleId from employee_schedule_alter ea where  ea.date='"
					+ date
					+ "' and (ea.status='c' or ea.login_time='' or ea.login_time='none'))  )	union	select distinct id  staffId, displayname, PersonnelNo,'weekly off' from employee where id in ((select es.employee_id from employee_schedule es where  ( es.cancel_date is  null or es.cancel_date>'"
					+ date
					+ "' ) and  es.id in  (select ea.scheduleId from employee_schedule_alter ea where  ea.date='"
					+ date
					+ "' and  (ea.login_time='weekly off')))) union	select distinct id  staffId, displayname, PersonnelNo,'no schdule for the day' from employee where id in ((select employee_subscription.empID from employee_subscription where fromDate<='"
					+ date
					+ "' and  subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
					+ date
					+ "') and empID not  in(select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'"
					+ date
					+ "' ) and es1.from_date<='"
					+ date
					+ "'  and es1.to_date>='"
					+ date
					+ "'))) union select distinct id  staffId, displayname, PersonnelNo,'no schdule for the day' from employee where id in ( select employee_subscription.empID from employee_subscription where fromDate<='"
					+ date
					+ "' and  ( subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
					+ date
					+ "') ) and empID  in (select es1.employee_id from employee_schedule es1 where  ( es1.cancel_date is  null or es1.cancel_date>'"
					+ date
					+ "') and (login_time='none' or login_time='') and ( logout_time='none' or logout_time='' ) and  es1.id not in ( select scheduleId from employee_schedule_alter where date='"
					+ date
					+ "' )  and es1.from_date<='"
					+ date
					+ "'  and es1.to_date>='"
					+ date
					+ "' ) ) union select distinct id  staffId, displayname, PersonnelNo,'landmark not in route' from employee where id in ((select employee_subscription.empID from employee_subscription 	where fromDate<='"
					+ date
					+ "' and   (   subscriptionID not in ( select subscriptionID from employee_unsubscription u where u.fromDate<='"
					+ date
					+ "') )	 and landmark not 	in(select routeChild.landmarkId from routeChild)))");
			returnData = "<table border='1'>"
					+ "<thead><tr><th>Date</th><th>Name</th><th>ID</th><th>status</th></tr>";
			while (rs.next()) {
				returnData += "<tr><td>"
						+ OtherFunctions.changeDateFromatToddmmyyyy(date)
						+ "</td><td>" + rs.getString(1) + "</td>" + "<td>"
						+ rs.getString(3) + "</td><td></td></tr>";
			}
		} catch (Exception e) {
			System.out.println("Error" + e);

		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		}
		return returnData;
	}

	public String getEmpRoute(long uid) {

		return "";
	}

	public String getEmployeeDet(long uid) {
		String employeeAddress = "";
		String addresscontrol = "";
		String empType;
		String returnString = "<h3>My Details</h3><hr/>" + "<div  >" +

		"<table id='empdet' style=\"width=40%;\"   >";
		Connection con = ob.connectDB();
		Statement st = null;
		ResultSet rs = null;
		Statement st1 = null;
		ResultSet rs1 = null;
		try {
			st = con.createStatement();
			st1 = con.createStatement();
			rs = st.executeQuery("select employee.id,loginId,displayname as  EmployeeName , EmailAddress,PersonnelNo,LineManager,StaffManager,DeptNo,OperationCode,  case when gender='m' then 'Male' else 'Female' end gender,case when employee_subscription.subscriptionStatus is null then 'Not Subscribed' when employee_subscription.subscriptionStatus ='subscribed' then 'Subscribed' when employee_subscription.subscriptionStatus ='pending' then 'Pending'  end subscriptionStatus,address,  contactNumber1  contactNumber,addressChangeDate,isContractEmployee as emptype," +
					"case when employee.registerStatus is null or employee.registerStatus!='a' then 'Not Registered'  else 'Registered'   end registerStatus" +
					"" +
					" from employee left outer join employee_subscription on employee_subscription.empID=employee.id  where employee.id="
					+ uid
					+ " order by employee_subscription.subscriptionDate desc limit 1");					
			if (rs.next()) {
				/*
				 * returnString +=
				 * "<tr><td><ul class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>LoginID:</span></li></ul></td>"
				 * + "<td><ul class='sb_menu'><li><span class='style39'>" +
				 * rs.getString(1) + "</span></li></ul></td>" +
				 * "<td><ul class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>E-Mail</td><td><td><ul class='sb_menu'><li><span class='style39'>"
				 * + rs.getString(2) + "</span></li></ul></td></tr>";
				 * returnString +=
				 * "<tr><td><ul class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>Line Manager</span></li></ul></td>"
				 * + "<td><ul class='sb_menu'><li><span class='style39'>" +
				 * rs.getString(3) + "</span></li></ul></td>" +
				 * "<td><ul class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>Staff Manager</td><td><td><ul class='sb_menu'><li><span class='style39'>"
				 * + rs.getString(4) + "</span></li></ul></td></tr>" +
				 * "<tr><td><ulwidth='30%' class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>Department</span></li></ul></td>"
				 * + "<td><ul class='sb_menu'><li><span class='style39'>" +
				 * rs.getString(5) + "</span></li></ul></td>" +
				 * "<td><ul class='sb_menu'><li>&nbsp;&nbsp;&nbsp;<span class='style39'>Operation</td><td><td><ul class='sb_menu'><li><span class='style39'>"
				 * + rs.getString(6) + "</span></li></ul></td></tr>";
				 */
				returnString += " <tr> " + "<td align='right' > "
						+ "<b>Name </b>" + "" + "</td> " + "<td  > "
						+ "<b>: </b>" + "</td> "
						+ " <td  width='20%' align='left'> "
						+ rs.getString("EmployeeName") + "</td> "
						+ " <td  align='right'> <b> Status</b>"
						+ " </td> " + "<td  > " + "<b>: </b>" + "</td> " +

						" <td  align='left'> "
						+ rs.getString("registerStatus") + " </td> "
						+ "</tr>";
				returnString += " <tr> "
						+ "<td  align='right'> <b> My ID </b> </td> "
						+ "<td  > " + "<b>: </b>" + "</td> "
						+ " <td  width='20%' align='left'> "
						+ rs.getString("PersonnelNo") + " </td> "
						+ " <td  align='right'> <b> Gender</b>" + " </td> "
						+ "<td  > " + "<b>: </b>" + "</td> " +

						" <td  align='left'> " + rs.getString("gender")
						+ " </td> " +

						"</tr>";

				employeeAddress = "" + rs.getString("address");
				empType = rs.getString("emptype");
				String afterthirtydayDate = OtherFunctions
						.getafterthirtydayDateInSqlFormat();
				if ( ((rs.getDate("addressChangeDate") == null) || ((rs
								.getDate("addressChangeDate").toString())
								.compareTo(afterthirtydayDate) > 0))) {
					employeeAddress = employeeAddress.replaceAll(",", "\n");
					addresscontrol = employeeAddress;
				} else {
					employeeAddress = employeeAddress.replaceAll(",", "\n");
					addresscontrol = employeeAddress;
				}
			}
			rs1 = st1
					.executeQuery("select date_format(from_date,'%d-%m-%Y') from_date,date_format(to_date,'%d-%m-%Y') to_date from employee_schedule where employee_id="
							+ uid
							+ " and to_date>=curdate() and status='a'");
			while (rs1.next()) {

				returnString += " <tr> "
						+ "<td  align='right'> <b> Schedule From(dd/mm/yyyy) </b> </td> "
						+ "<td  > "
						+ "<b>: </b>"
						+ "</td> "
						+ " <td  width='20%' align='left'> "
						+ rs1.getString("from_date")
						+ " </td> "
						+ " <td  align='right'> <b> Schedule To(dd/mm/yyyy)</b>"
						+ " </td> " + "<td  > " + "<b>: </b>" + "</td> " +

						" <td  align='left'> " + rs1.getString("to_date")
						+ " </td> " + "</tr>";

			}
			returnString += "</table>" + "</div>"
					+ "<div style='display:none' id='address'>"
					+ addresscontrol + "</div>";
			// System.out.println("BAcked data" + returnString);
		} catch (Exception e) {
			System.out.println("error In Get" + e);
			// e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs, rs1);
			DbConnect.closeStatement(st, st1);
			DbConnect.closeConnection(con);
		}

		return returnString;
	}

	/*
	 * public ResultSet getApl(String searchval) { Connection con =
	 * ob.connectDB(); PreparedStatement pst = null; ResultSet rs = null;
	 * searchval = searchval + "%"; try {
	 * 
	 * String query =
	 * "SELECT landmark.id,area.area,place.place,landmark.landmark FROM AREA,place,landmark WHERE (area.area LIKE ? OR place.place LIKE ? OR landmark.landmark LIKE ?) AND landmark.place=place.id AND place.area=area.id"
	 * ; pst = con.prepareStatement(query); pst.setString(1, searchval);
	 * pst.setString(2, searchval); pst.setString(3, searchval); rs =
	 * pst.executeQuery(); } catch (Exception e) {
	 * System.out.println("Exception" + e); } return rs; }
	 */
	public String Location(int landmark) {
		String location = "";
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		try {

			String query = "SELECT area.area,place.place,landmark.landmark FROM area,place,landmark WHERE landmark.id=? and landmark.place=place.id AND place.area=area.id";
			pst = con.prepareStatement(query);
			pst.setInt(1, landmark);

			rs = pst.executeQuery();
			if (rs.next()) {
				// location += rs.getString(1) + "--" + rs.getString(2) + "--" +
				// rs.getString(3);
				location += rs.getString(1) + "--" + rs.getString(3);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return location;
	}

	/*
	 * public ResultSet getLogTime() { Connection con = ob.connectDB();
	 * PreparedStatement pst = null; ResultSet rs = null; try {
	 * 
	 * String query = "SELECT * from logtime"; pst = con.prepareStatement(query,
	 * ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY); rs =
	 * pst.executeQuery(); } catch (Exception e) {
	 * System.out.println("Exception" + e); } return rs; }
	 */
	public int insertPageToDatabase(ArrayList<String> pages, String type) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int returnData = 0;
		Connection con = ob.connectDB();
		ArrayList<String> existingpages = new ArrayList<String>();
		try {
			pst = con.prepareStatement("select url from page");
			rs = pst.executeQuery();
			while (rs.next()) {
				existingpages.add(rs.getString(1));
			}
			pst = con.prepareStatement("insert into page(url, type) values(?,?)");
			for (String page : pages) {
				if (!existingpages.contains(page)) {
					//System.out.println("page: " + page);
					pst.setString(1, page);
					pst.setString(2, type);
					returnData += pst.executeUpdate();
				}

			}

		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnData;
	}


	public int insertRolePages(String usertype, String[] pages,String display) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		int returnData = 0;
		Connection con = ob.connectDB();
		String[] users = null;
		ArrayList<String> existingpages = new ArrayList<String>();
		try {
			switch (usertype) {
			case "emp":
				users = new String[4];
				users[0] = usertype;
				users[1] = "tae";
				users[2] = "tme";
				users[3] = "hrme";
				break;
			case "hrm":
				users = new String[3];
				users[0] = usertype;
				users[1] = "tmhr";
				users[2] = "tahr";
				break;
			default:
				users = new String[1];
				users[0] = usertype;
			}
			pst = con.prepareStatement("delete from role_page where role=?");
			for (String user : users) {
				pst.setString(1, user);
				pst.executeUpdate();
			}
			pst = con
					.prepareStatement("insert into role_page(role,page ) values(?,?)");
			for (String user : users) {
				for (String page : pages) {
					if (!existingpages.contains(page)) {
						pst.setString(1, user);
						pst.setString(2, page);
				 
						returnData += pst.executeUpdate();
					}
				}
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return returnData;
	}

	public ArrayList<SiteDto> selectPagesNoAccess(String usertype) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<SiteDto> existingpages = new ArrayList<SiteDto>();
		SiteDto dto = null;
		try {
			pst = con
					.prepareStatement("select id,url from page where id not in(select page from role_page where role=?)");
			pst.setString(1, usertype);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new SiteDto();
				dto.setId(rs.getString(1));
				dto.setName(rs.getString(2));
				existingpages.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error ooooooo" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return existingpages;
	}

	public ArrayList<SiteDto> selectPagesAccess(String userType) {
		PreparedStatement pst = null;
		ResultSet rs = null;
		Connection con = ob.connectDB();
		ArrayList<SiteDto> existingpages = new ArrayList<SiteDto>();
		SiteDto dto = null;
		try {
			pst = con
					.prepareStatement("select id,url from page where id in(select page from role_page where role=?)");
			pst.setString(1, userType);
			rs = pst.executeQuery();
			while (rs.next()) {
				dto = new SiteDto();
				dto.setId(rs.getString(1));
				dto.setName(rs.getString(2));
				existingpages.add(dto);
			}
		} catch (Exception e) {
			System.out.println("Error" + e);
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);
		}
		return existingpages;
	}
	public String[] getCity(String site) {
		 
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		ResultSet rs = null;
		String[] city=new String[2];		
		try {

			String priorityQuery = "";
			if(OtherFunctions.isEmpty(site)) {
				priorityQuery= ", 0 as cnt";
			} else {
				priorityQuery= ", if(s.id="+site+",1,0) cnt ";
			}
			String query = "SELECT l.latitude,l.longitude "+priorityQuery+" from landmark l,site s where s.landmark=l.id order by cnt desc";
			System.out.println(query);
			pst = con.prepareStatement(query);
			rs = pst.executeQuery();
			if(rs.next())
			{
				city[0]=rs.getString(1);
				city[1]=rs.getString(2);
			}
		} catch (Exception e) {
			System.out.println("Exception" + e);
		}
		finally
		{
		DbConnect.closeResultSet(rs);
		DbConnect.closeStatement(pst);
		DbConnect.closeConnection(con);
		}
		return city;		
	}
	public boolean isRegisterd(String employeeId) {

		ResultSet rs = null;
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		boolean returnVal = false;

		try {
			pst = con.prepareStatement("select * from employee where id="
					+ employeeId + " and registerStatus='"
					+ SettingsConstant.ACTIVE_STATUS + "'");
			rs=pst.executeQuery();
			if (rs.next()) {
				returnVal = true;

			}
			

		} catch (Exception e) {
			System.out.println("ERRORR" + e);
		}
		finally
		{
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst);
			DbConnect.closeConnection(con);	
		}
		return returnVal;
	}
	
	

}
