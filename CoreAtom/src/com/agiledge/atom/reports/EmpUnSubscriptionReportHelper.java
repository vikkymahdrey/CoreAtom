package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.EmpUnsubscription;


public class EmpUnSubscriptionReportHelper {

	public List<EmpUnsubscription> getAllUnSubscriptionDetails(String fromDate,
			String toDate, String spoc,String empId, String siteId) {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
		System.out.println("from Date " + fromDate);
		System.out.println("to Date " + toDate);
		DbConnect dbConn = DbConnect.getInstance();
		siteId=siteId==null?"":siteId;
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		String fromDateQuery = "";
		String toDateQuery = "";
		String spocQuery="";
		String empQuery="";
		String siteQuery="";
		if (fromDate != null && !fromDate.trim().equals("")) {
			fromDateQuery = "   and eunsub.fromDate>=? ";
		}

		if (toDate != null && !toDate.trim().equals("")) {
			toDateQuery = "   and eunsub.fromDate<=? ";
		}		
		if (spoc!=null&& !spoc.trim().equals(""))
		{
			spocQuery="   and ( spoc.id="+spoc+" or sup.id="+spoc+" ) ";
		}
		if (empId!=null&& !empId.trim().equals(""))
		{
			empQuery="   and e.id="+empId+"";
		}
		if(siteId.trim().equals("")==false)
		{
			  siteQuery=" and site.id="+ siteId;
		}
		List<EmpUnsubscription> unsubscriptionList = new ArrayList<EmpUnsubscription>();
		try {

			String query = " select site.site_name,  ifnull(date_format(es.scheduledUpTo,'%d/%m%y'),'')  scheduledUpTo,  e.PersonnelNo as EmployeeID,  CONCAT(e.EmployeeFirstName,' ', e.employeeLastName)  as EmployeeName, eunsub.fromDate, eunsub.unsubscriptionDate, sup.displayname as Manager,spoc.displayname as SPOC,a.area,p.place,l.landmark from employee_unsubscription eunsub, ( select * from employee_subscription s left join ( select  ifnull(cancel_date,to_date) scheduledUpTo,subscription_id from employee_schedule where id in (	select max(id) from employee_schedule    group by subscription_id ) and ifnull(cancel_date,to_date) >=from_date)eh on eh.subscription_id=s.subscriptionID) es, employee e,employee sup,employee spoc,area a,place p, landmark l, site  where eunsub.subscriptionID=es.subscriptionID and site.id=e.site  and es.empID=e.id and es.supervisor=sup.id and es.spoc= spoc.id and es.landMark=l.id and a.id=p.area and l.place=p.id    "
					+ fromDateQuery + toDateQuery + spocQuery+empQuery + siteQuery;

			System.out.println("Query: " + query);
			// "SELECT e.id,e.PersonnelNo,e.EmployeeFirstName,e.EmployeeLastName,e.Gender,a.area,p.place,l.id as landmarkId ,l.landmark  FROM employee e,employee_subscription es,area a,place p,landmark l where e.id=es.empID and (es.subscriptionStatus='subscribed' or es.subscriptionStatus='pending') and es.fromDate >=?  and es.fromDate<= ? and es.landMark=l.id and l.place=p.id and p.area=a.id";

			con = dbConn.connectDB();
			stmt = con.prepareStatement(query);
			// stmt.setString(1, fromDate);
			// stmt.setString(2, toDate);
			int index = 0;
			if (fromDate != null && !fromDate.trim().equals("")) {
				stmt.setDate(++index, new java.sql.Date(sdf.parse(fromDate)
						.getTime()));
			}
			if (toDate != null && !toDate.trim().equals("")) {
				stmt.setDate(++index, new java.sql.Date(sdf.parse(toDate)
						.getTime()));
			}

			rs = stmt.executeQuery();

			while (rs.next()) {
				System.out.println(rs.getString("EmployeeID"));
				EmpUnsubscription empSub = new EmpUnsubscription();
				empSub.setEmpID(rs.getString("EmployeeID"));
				empSub.setEmpName(rs.getString("EmployeeName"));
				empSub.setEffectiveDate(new java.util.Date(rs.getDate(
						"fromDate").getTime()));
				empSub.setScheduledUpTo (rs.getString("scheduledUpTo") );
				
				empSub.setUnsubscriptionDate(new java.util.Date(rs.getDate(
						"unsubscriptionDate").getTime()));
				empSub.setManager(rs.getString("Manager"));
				empSub.setSpoc(rs.getString("SPOC"));
				empSub.setArea(rs.getString("area"));
				empSub.setPlace(rs.getString("place"));
				empSub.setLandmark(rs.getString("landmark"));
				empSub.setSite(rs.getString("site_name"));
				unsubscriptionList.add(empSub);
			}
			return unsubscriptionList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}

}
