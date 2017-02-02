package com.agiledge.atom.reports;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.reports.dto.AgileBillDto;
import com.agiledge.atom.reports.dto.CapacityUtilizationReportDto;
import com.agiledge.atom.reports.dto.OnTimeTripCountDto;
import com.agiledge.atom.reports.dto.OtaOtdDto;
import com.agiledge.atom.reports.dto.ProjectWiseTripBillDto;
import com.agiledge.atom.reports.dto.TripBillDto;
import com.agiledge.atom.reports.dto.TripSheetNoShowCountReportDto;
import com.itextpdf.text.log.SysoLogger;



public class TripSheetNoShowCountReportHelper {

	public List<TripSheetNoShowCountReportDto> getNoShowCountGroupBy(
			String month, String year, String groupBy, String siteId) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<TripSheetNoShowCountReportDto> dtoList = new ArrayList<TripSheetNoShowCountReportDto>();
		try {

			String siteQuery="";
			String monthQuery = "";
			String yearQuery = "";
			String attrQuery = "";
			String groupByQuery = "";
			String groupByLabel = "";
			String groupById="";
			
			if(siteId!=null&&siteId.trim().equals("")==false)
			{
				siteQuery=" and siteId=" + siteId;
			}
			if (month!=null&&!month.trim().equals("")) {
				monthQuery = " and month(td.trip_date) =" + month;
				  
				System.out.println(" month query : " + monthQuery);
			}
			if (year!=null&&!year.trim().equals("")) {
				yearQuery = " and  year(td.trip_date) =" + year;
			}
			String bodyQuery=" select  e1.id employeeId, e1.displayName employeeName, proj.description projectName, proj.id projectId, es.scheduledBy, td.trip_date, td.trip_time, td.trip_log, vtp.noShowReason, scheduledBy.displayName scheduledByName  from vendor_trip_sheet vtp, trip_details td, employee_subscription esub, employee_schedule es, trip_details_child tdc, employee e1, atsconnect proj, employee scheduledBy  where vtp.showStatus='No Show' and vtp.tripId = td.id  and vtp.employeeId=esub.empID and es.subscription_id=esub.subscriptionID and tdc.scheduleId=es.id and ( tdc.tripId=td.id and vtp.employeeId=tdc.employeeId and ( td.status='saved' or td.status='saveedit' or td.status='addsave' )  )and e1.id=vtp.employeeId and (proj.id = es.project and proj.projecttype = es.type ) and es.scheduledBy = scheduledBy.id "+ monthQuery+ siteQuery + yearQuery;
			
			if (groupBy.equals("Project")) {
				attrQuery = " projectName, projectId, ";
				groupByQuery = " projectName, projectId ";
				groupByLabel = "projectName";
				groupById="projectId";
				
			}
			if (groupBy.equals("User")) {
				attrQuery = "  employeeName, employeeId, ";
				groupByQuery = "  employeeName, employeeId ";
				groupByLabel = "employeeName";
				groupById="employeeId";
				
			}
			
			
			if (groupBy.equals("Manager")||groupBy.equals("SPOC")) {
				
				attrQuery =    " spocname, spocId, ";
				groupByQuery = " spocname, spocId ";
				groupByLabel = "spocname";
				groupById="spocId";
//				or spoc.id=esub.supervisor
				bodyQuery=" select  e1.PersonnelNo, e1.displayName employeeName, e1.id employeeId, proj.description projectName, proj.id projectId, spoc.displayName spocName, spoc.id spocId, es.scheduledBy, td.trip_date, td.trip_time, td.trip_log, vtp.noShowReason, scheduledBy.displayName scheduledByName  from vendor_trip_sheet vtp, trip_details td, employee_subscription esub, employee_schedule es, ( select *, 'saved' status1,'saved' status2 from trip_details_child   	union   select * , 'savedit' status1,'addsave' status2    from trip_details_modified     ) tdc, employee e1, atsconnect proj, employee scheduledBy, employee spoc  where vtp.showStatus='No Show' and vtp.tripId = td.id  and vtp.employeeId=esub.empID and es.subscription_id=esub.subscriptionID and tdc.scheduleId=es.id and ( tdc.tripId=td.id and vtp.employeeId=tdc.employeeId and ( td.status=tdc.status1 or td.status=tdc.status2 ) ) and e1.id=vtp.employeeId and (proj.id = es.project and proj.projecttype = es.type )  and es.scheduledBy = scheduledBy.id and (  spoc.id=esub.supervisor ) "+ monthQuery+ siteQuery + yearQuery; 
						 
			}
if ( groupBy.equals("SPOC")) {
				
				attrQuery =    " spocname, spocId, ";
				groupByQuery = " spocname, spocId ";
				groupByLabel = "spocname";
				groupById="spocId";
//				or spoc.id=esub.supervisor
				bodyQuery=" select  e1.PersonnelNo, e1.displayName employeeName, e1.id employeeId, proj.description projectName, proj.id projectId, spoc.displayName spocName, spoc.id spocId, es.scheduledBy, td.trip_date, td.trip_time, td.trip_log, vtp.noShowReason, scheduledBy.displayName scheduledByName  from vendor_trip_sheet vtp, trip_details td, employee_subscription esub, employee_schedule es, ( select *, 'saved' status1,'saved' status2 from trip_details_child   	union   select * , 'savedit' status1,'addsave' status2    from trip_details_modified     ) tdc, employee e1, atsconnect proj, employee scheduledBy, employee spoc  where vtp.showStatus='No Show' and vtp.tripId = td.id  and vtp.employeeId=esub.empID and es.subscription_id=esub.subscriptionID and tdc.scheduleId=es.id and ( tdc.tripId=td.id and vtp.employeeId=tdc.employeeId and ( td.status=tdc.status1 or td.status=tdc.status2 ) ) and e1.id=vtp.employeeId and (proj.id = es.project and proj.projecttype = es.type )  and es.scheduledBy = scheduledBy.id and (spoc.id=esub.spoc  ) "+ monthQuery+ siteQuery + yearQuery; 
						 
			}
			String openingQuery="select monthname(trip_date) month, year(trip_date) year, "+attrQuery+" count(*) count from (";
			String closingQuery=") ed    group by year(trip_date), monthname( trip_date), " + groupByQuery;
			
			String query = openingQuery+bodyQuery+closingQuery;

			System.out.println("Query: " + query);

			con = dbConn.connectDB();
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				TripSheetNoShowCountReportDto dto = new TripSheetNoShowCountReportDto();
				dto.setMonth(rs.getString("month"));
				dto.setYear(rs.getString("year"));
				dto.setMonthLabel(dto.getMonth()+ " " + dto.getYear());
				dto.setNoShowCount(rs.getInt("count"));
				dto.setGroupByLabel(groupByLabel);
				dto.setGroupBy(rs.getString(groupByLabel));
				dto.setGroupById(rs.getString(groupById));
				dtoList.add(dto);

			}
			return dtoList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}
	
	 
	public List<TripSheetNoShowCountReportDto> getNoShowCountEmployees(
			String month, String year,String Site,String Project,String SPOC,String Manager, String User) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<TripSheetNoShowCountReportDto> dtoList = new ArrayList<TripSheetNoShowCountReportDto>();
		try {

			
			String subQuery="  and monthname( td.trip_date)='" + month + "' and year( td.trip_date) = " + year;
			String query = "select e1.id , e1.PersonnelNo, e1.displayName employeeName, proj.description projectName, proj.id projectId, es.scheduledBy, td.trip_date, td.trip_time, td.trip_log, vtp.noShowReason, scheduledBy.displayName scheduledByName from vendor_trip_sheet vtp, trip_details td, employee_subscription esub, employee_schedule es,  trip_details_child tdc, employee e1, atsconnect proj, employee scheduledBy where vtp.showStatus='No Show' and vtp.tripId = td.id  and vtp.employeeId=esub.empID and es.subscription_id=esub.subscriptionID and tdc.scheduleId=es.id and ( tdc.tripId=td.id and vtp.employeeId=tdc.employeeId and  ( td.status='saved' or td.status='saveedit' or td.status='addsave' )  )and e1.id=vtp.employeeId and (proj.id = es.project and proj.projecttype = es.type ) and es.scheduledBy = scheduledBy.id ";
			
			/*if (groupBy.equals("Site")) {
				attrQuery = " site.site_name SiteName, site.id siteId, ";
				groupByQuery = "  site.id, site.site_name    ";
				groupByLabel = "SiteName";
				groupById="siteId";

			}*/
			if(!Project.equals(""))
			{
				subQuery+=" and proj.id=" + Project;
			}
			if(!SPOC.equals(""))
			{
				subQuery+=" and spoc.id=" + SPOC;
				query="select e1.id , e1.PersonnelNo, e1.displayName employeeName, e1.id employeeId, proj.description projectName, proj.id projectId, spoc.displayName spocName, spoc.id spocId, es.scheduledBy, td.trip_date, td.trip_time, td.trip_log, vtp.noShowReason, scheduledBy.displayName scheduledByName from vendor_trip_sheet vtp, trip_details td, employee_subscription esub, employee_schedule es, ( select *, 'saved' status1,'saved' status2 from trip_details_child	union select * , 'savedit' status1,'addsave' status2    from trip_details_modified ) tdc, employee e1, atsconnect proj, employee scheduledBy, employee spoc where vtp.showStatus='No Show' and vtp.tripId = td.id  and vtp.employeeId=esub.empID and es.subscription_id=esub.subscriptionID and tdc.scheduleId=es.id and ( tdc.tripId=td.id and vtp.employeeId=tdc.employeeId and ( td.status=tdc.status1 or td.status=tdc.status2 ) ) and e1.id=vtp.employeeId and (proj.id = es.project and proj.projecttype = es.type ) and es.scheduledBy = scheduledBy.id and (spoc.id=esub.spoc or spoc.id=esub.supervisor )";
			}
			 
			if(!User.equals(""))
			{
				subQuery+=" and e1.id=" + User;
			}
			 
			 
			
			//if(Pro)
			  
			query = query + subQuery;
			System.out.println("Query: " + query);

			con = dbConn.connectDB();
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				TripSheetNoShowCountReportDto dto = new TripSheetNoShowCountReportDto();
				dto.setEmployeeId(rs.getString("personnelNo"));
				dto.setEmployeeName(rs.getString("EmployeeName"));
				dto.setShiftTime( rs.getString("trip_time"));
				dto.setTripLog(rs.getString("trip_log"));
				dto.setTripDate(rs.getString("trip_date"));
				dto.setProject(rs.getString("ProjectName"));
				dto.setScheduledBy(rs.getString("scheduledByName"));
				dto.setReason(rs.getString("noShowReason"));
	 
				dtoList.add(dto);

			}
			return dtoList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}


	public List<OnTimeTripCountDto> getTripOnTimeCount(String month,
			String year, String groupBy) {
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<OnTimeTripCountDto> dtoList = new ArrayList<OnTimeTripCountDto>();
		try {

			String monthQuery = "";
			String yearQuery = "";

			if (month != null && !month.trim().equals("")) {
				monthQuery = " and month(td.trip_date) =" + month;
			}
			if (year != null && !year.trim().equals("")) {
				yearQuery = " and year(td.trip_date) =" + year;
			}
			String qryOnTimeMainAttr = "";
			String qryOnTimeAttr = "";
			String qryOnTimeFrom = "";
			String qryOnTimeCondition = "";
			String qryOnTimeGroupBy = "";

			String qryOffTimeAttr = "";
			String qryOffTimeFrom = "";
			String qryOffTimeCondition = "";
			String qryOffTimeGroupBy = "";
			String qryOnTimeOffTimeJoin = "";
			if (groupBy.equals("Vehicle Type")) {
				qryOnTimeMainAttr = ", onTime.type ";
				qryOnTimeAttr = ", vehicle_type.type type ";
				qryOnTimeFrom = "  , vehicle_type  ";
				qryOnTimeCondition = " and vehicle_type.id=td.vehicle_type ";
				qryOnTimeGroupBy = " , vehicle_type.type ";

				qryOffTimeAttr = ", vehicle_type.type type ";
				qryOffTimeFrom = "  , vehicle_type  ";
				qryOffTimeCondition = " and vehicle_type.id=td.vehicle_type ";
				qryOffTimeGroupBy = " , vehicle_type.type ";
				qryOnTimeOffTimeJoin = " and onTime.type=offTime.type 	";
			}

			String query = " select onTime.monthNo, onTime.month, ontime.site,onTime.logType, ifnull(onTime.onTimeCount ,0)  onTimeCount, ifnull( offTime.offTimeCount,0) offTimeCount, ifnull(onTimeCount,0)+ ifnull(offTimeCount,0) total"
					+ qryOnTimeMainAttr
					+ " from ( select month(td.trip_date) monthNo, concat(monthname(td.trip_date) , ' ' ,cast(year(td.trip_date) as char))  month, site.site_name site,   count(vtp.tripId) onTimeCount, td.trip_log logType "
					+ qryOnTimeAttr
					+ " from vendor_trip_sheet_parent vtp,   trip_details td, site "
					+ qryOnTimeFrom
					+ " where  vtp.tripId=td.id and onTimeStatus='On Time' and site.id=td.siteId "
					+ qryOnTimeCondition
					+ " "
					+ monthQuery
					+ yearQuery
					+ " 	group by month(td.trip_date),concat(monthname(td.trip_date) , ' ' ,cast(year(td.trip_date) as char)),site.site_name, td.trip_log "
					+ qryOnTimeGroupBy
					+ "	) as onTime 	left join ( select  concat(monthname(td.trip_date) , ' ' ,cast(year(td.trip_date) as char))  month, site.site_name site, count(vtp.tripId) offTimeCount, td.trip_log logType  "
					+ qryOffTimeAttr
					+ "  from vendor_trip_sheet_parent vtp,   trip_details td, site  "
					+ qryOffTimeFrom
					+ "  where  vtp.tripId=td.id   and  onTimeStatus='Not On Time'  and site.id=td.siteId    "
					+ qryOffTimeCondition
					+ " "
					+ monthQuery
					+ yearQuery
					+ "	group by concat(monthname(td.trip_date) , ' ' ,cast(year(td.trip_date) as char)) ,site.site_name, td.trip_log "
					+ qryOffTimeGroupBy
					+ "	) as offTime on onTime.month=offTime.month and onTime.logType=offTime.logType and onTime.site=offTime.site  "
					+ qryOnTimeOffTimeJoin;

			System.out.println("Query: " + query);

			con = dbConn.connectDB();
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				OnTimeTripCountDto dto = new OnTimeTripCountDto();
				dto.setMonthNo(rs.getInt("monthNo"));
				dto.setMonth(rs.getString("month"));
				dto.setSite(rs.getString("site"));
				dto.setLogType(rs.getString("logType"));
				dto.setOnTimeCount(rs.getInt("onTimeCount"));
				dto.setOffTimeCount(rs.getInt("offTimeCount"));
				dto.setTotalCount(rs.getInt("total"));

				if (groupBy.equals("Vehicle Type")) {
					dto.setVehicleType(rs.getString("type"));
				}
				dtoList.add(dto);

			}
			return dtoList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}

	public List<OtaOtdDto> getOtaOtdReportDetails(String month, String site,
			String logType, String vehicleType, String onTimeStatus,
			String fromDate, String toDate) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<OtaOtdDto> dtoList = new ArrayList<OtaOtdDto>();
		String monthQuery = "";
		String siteQuery = "";
		String vehicleTypeQuery = "";
		String logTypeQuery = "";
		String onTimeStatusQuery = "";
		String fromDateQuery = "";
		String toDateQuery = "";
		fromDate = fromDate == null ? "" : fromDate;
		toDate = toDate == null ? "" : toDate;
		fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);
		if (fromDate.equals("") == false) {
			fromDateQuery = "  and td.trip_date>='" + fromDate + "' ";
		}
		if (toDate.equals("") == false) {
			toDateQuery = " and td.trip_date<='" + toDate + "'  ";
		}
		System.out.println(" To Date : " + toDateQuery);
		if (onTimeStatus != null && onTimeStatus.equals("") == false) {
			onTimeStatusQuery = "  and vtp.onTimeStatus='Not On Time' ";
		}
		if (month != null && !month.equals("")) {
			monthQuery = "  and DATE_FORMAT(td.trip_date,'%M %Y')='" + month
					+ "' ";
		}
		if (site != null && !site.equals("")) {
			siteQuery = " and site.site_name='" + site + "'";
		}
		if (logType != null && !logType.equals("null") && !logType.equals("")) {

			logTypeQuery = " and td.trip_log='" + logType + "'";
		}
		if (vehicleType != null && !vehicleType.equals("")) {
			vehicleTypeQuery = " and vt.type='" + vehicleType + "'";
		}
		try {
			System.out.println(monthQuery);
			// String query =
			// "select   td.trip_date, td.id tripId,  td.trip_code tripCode, td.trip_log Shift, vtp.vehicleNo Cab#, vt.type cabType, td.trip_time plannedTime,vtp.logTime actualTime, case when vtp.onTimeStatus='Not On Time' then'Delayed'else'On Time' end onTimeStatus,   site.site_name site from vendor_trip_sheet_parent vtp, vehicle_type vt,  trip_details td, site  where   vtp.tripId=td.id  and site.id=td.siteId  	  and vt.id=td.vehicle_type "
			String query = "select   td.trip_date, td.id tripId,  td.trip_code tripCode, td.trip_log Shift, vtp.vehicleNo CabNo, vt.type cabType, td.trip_time plannedTime,vtp.logTime actualTime, case when vtp.onTimeStatus='Not On Time' then'Delayed'else'On Time' end onTimeStatus, site.site_name site,(select count(vtc.tripId) from vendor_trip_sheet vtc where vtc.tripId=td.id and vtc.showStatus='Show' ) showCount, (select count(vtc.tripId) from vendor_trip_sheet vtc where vtc.tripId=td.id and vtc.showStatus='No Show' ) noShowCount from vendor_trip_sheet_parent vtp, vehicle_type vt,  trip_details td, site  where   vtp.tripId=td.id  and site.id=td.siteId  	  and vt.id=td.vehicle_type "
					+ monthQuery
					+ siteQuery
					+ vehicleTypeQuery
					+ logTypeQuery
					+ onTimeStatusQuery + fromDateQuery + toDateQuery;
			System.out.println("Query: " + query);

			con = dbConn.connectDB();
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				OtaOtdDto dto = new OtaOtdDto();
				dto.setTrip_date(rs.getDate("trip_date"));
				dto.setTripCode(rs.getString("tripCode"));
				dto.setTripId(rs.getString("tripId"));
				dto.setShift(rs.getString("Shift"));
				dto.setCabNo(rs.getString("CabNo"));
				dto.setCabType(rs.getString("cabType"));
				dto.setPlannedTime(rs.getString("plannedTime"));
				dto.setActualTime(rs.getString("actualTime"));
				dto.setOnTimeStatus(rs.getString("onTimeStatus"));
				dto.setSite(rs.getString("site"));
				dto.setNoShowCount(rs.getInt("noShowCount"));
				dto.setShowCount(rs.getInt("showcount"));
				dtoList.add(dto);

			}
			return dtoList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}

	public List<ProjectWiseTripBillDto> getProjectWiseBillingReport(
			String fromDate, String toDate, String siteId, String project) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		PreparedStatement stmt = null;
		ResultSet rs = null;
		List<ProjectWiseTripBillDto> dtoList = new ArrayList<ProjectWiseTripBillDto>();

		siteId = siteId == null ? "" : siteId;
		fromDate = fromDate == null ? "" : fromDate;
		toDate = toDate == null ? "" : toDate;

		fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);

		try {

			if ((fromDate + toDate + siteId).trim().equals("") == false) {
				// Error may be in the
				// query**************************************************************************
				// String
				// query="select alldata.cost/alldata.totalEmployee PERINSTANCE, alldata.cost/alldata.totalEmployee*2 twoway,  (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))  AS PROJECTCOST,PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode,project_emp.projectdescription  from  ( select sum(totals.totalNoOfTrips) as trips,sum(totals.total) cost, (select  count(tdc.employeeId) as emps from trip_details_child tdc,trip_details td where td.id=tdc.tripId and  td.trip_date between '"+fromDate+"' and '"+toDate+"'  and td.siteId="+siteId+") as totalEmployee  from ( select  count(*) totalNoOfTrips, td.trip_date,  (vht.ratePerTrip*(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status,vht.ratePerTrip cost  from trip_details td, vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between '"+fromDate+"' and '"+toDate+"' and td.siteId="+siteId+"  group by siteid, td.trip_date,vehicle_type,security_status,vht.ratePerTrip,td.trip_time,td.trip_log) as totals  ) as alldata,  (  select count( *) empCount, sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and  pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between '"+fromDate+"' and '"+toDate+"')    group by sch.project,td.trip_date,pr.project,pr.description) as project_emp ";
				// String
				// query="select alldata.cost/alldata.totalEmployee PERINSTANCE,  alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))  AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode,  project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from    ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee,  totals.trip_time,  totals.trip_log from  (select  count(tdc.employeeId) as emps, td.trip_time, td.trip_log from trip_details_child tdc, trip_details td where td.id=tdc.tripId and  td.trip_date between '"+fromDate+"' and '"+toDate+"'  and td.siteId="+siteId+" group by td.trip_time, td.trip_log) as totalEmployee, ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,  (vht.ratePerTrip*(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status,vht.ratePerTrip cost  from trip_details td, vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between '"+fromDate+"' and '"+toDate+"' and td.siteId="+siteId+"  group by siteid, td.trip_date,vehicle_type,security_status,vht.ratePerTrip,td.trip_time,td.trip_log ) as totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount, sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time, td.trip_log from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and  pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between '"+fromDate+"' and '"+
				// toDate+"') and td.siteId="+siteId+"    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where   alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log";
				String query = " select alldata.cost/alldata.totalEmployee PERINSTANCE,  alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))  AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode,  project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from    ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee,  totals.trip_time,  totals.trip_log from  (select  count(tdc.employeeId) as emps, td.trip_time, td.trip_log from trip_details_child tdc, trip_details td where td.id=tdc.tripId and  td.trip_date between '"+fromDate+"' and '"+toDate+"'  and td.siteId="+siteId+" group by td.trip_time, td.trip_log) as totalEmployee, ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,  ( (select ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()) limit 1) *(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status, (select ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()) limit 1)  cost  from trip_details td, vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between '"+fromDate+"' and '"+toDate+"' and td.siteId="+siteId+"  group by siteid, td.trip_date,vehicle_type,security_status,   td.trip_time,td.trip_log ) as totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount, sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time, td.trip_log from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and  pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between '"+fromDate+"' and '"+toDate+"') and td.siteId="+siteId+"    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where   alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log   ";
				System.out.println(query);
/*				
				String query1 = "declare @site int declare '"+fromDate+"' datetime declare @toDate datetime set    '"+fromDate+"'='"
						+ fromDate
						+ "'  set @toDate='"
						+ toDate
						+ "' set @site="
						+ siteId
						+ "      select * from ((             select alldata.cost/alldata.totalEmployee PERINSTANCE,      alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))   	 AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode, 	   project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from     	   ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee, 	     totals.trip_time,  totals.trip_log from    (select  count(tdc.employeeId) as emps, td.trip_time, td.trip_log from trip_details_child tdc, trip_details td    where td.id=tdc.tripId  and (td.status='saved') and  td.trip_date between @fromDate and @toDate  and td.siteId=@site group by td.trip_time, td.trip_log) as totalEmployee,    ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,     ( (select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and     vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate())) 	*(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status, 	(select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type	 and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()))  cost  from trip_details td,	  vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between @fromDate and @toDate and 	  td.siteId=@site  group by siteid, td.trip_date,vehicle_type,security_status,   td.trip_time,td.trip_log ) as 	  totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by 	  totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount, 	  sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time, 	  td.trip_log from trip_details_child tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and (td.status='saved')	  and pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between @fromDate and @toDate) and td.siteId=@site   	    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where  		 alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log) 		 union 		 ( select alldata.cost/alldata.totalEmployee PERINSTANCE,      alldata.cost/alldata.totalEmployee*2 twoway,   (PROJECT_Emp.empCount*(alldata.cost/alldata.totalEmployee))   	 AS PROJECTCOST, PROJECT_Emp.id projectId,  PROJECT_Emp.empCount,PROJECT_Emp.trip_date,  PROJECT_Emp.projectCode, 	   project_emp.projectdescription, alldata.trip_log, alldata.trip_time  from     	   ( select sum(totals.totalNoOfTrips) as trips,   sum(totals.total) cost, totalEmployee.emps as totalEmployee, 	     totals.trip_time,  totals.trip_log from    (select  count(tdc.empid) as emps, td.trip_time, td.trip_log from trip_details_modified tdc, trip_details td    where td.id=tdc.tripId  and (td.status='addsave' or td.status='saveedit') and  td.trip_date between @fromDate and @toDate  and td.siteId=@site group by td.trip_time, td.trip_log) as totalEmployee,    ( select  count(*) totalNoOfTrips, td.trip_date, td.trip_time, td.trip_log,     ( (select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and     vehicle_type=td.vehicle_type and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()))  	*(case when td.security_status='YES' then  2 else 1 end))*count(*) as total, vehicle_type,security_status,   	(select top 1 ratePerTrip from site_vehicle_rate_calander where site=td.siteId and vehicle_type=td.vehicle_type 	 and td.trip_date >=fromDate and td.trip_date<ifnull(toDate,curdate()))  cost  from trip_details td, 	  vehicle_type vht where vht.id=td.vehicle_type and td.trip_date between @fromDate and @toDate and  	  td.siteId=@site  group by siteid, td.trip_date,vehicle_type,security_status,   td.trip_time,td.trip_log ) as  	  totals  where totalEmployee.trip_log=totals.trip_log and totalEmployee.trip_time=totals.trip_time group by  	  totalEmployee.emps, totals.trip_time,  totals.trip_log  ) as alldata,  (  select count( *) empCount,  	  sch.project,td.trip_date,pr.project as projectCode, pr.description as projectdescription, pr.id, td.trip_time,  	  td.trip_log from trip_details_modified tdc ,trip_details td,   employee_schedule sch   ,atsconnect pr where td.id=tdc.tripId and (td.status='addsave' or td.status='saveedit') 	  and pr.id=sch.project and tdc.scheduleId=sch.id and     (td.trip_date between @fromDate and @toDate) and td.siteId=@site    	    group by sch.project,td.trip_date,pr.project,pr.description, pr.id, td.trip_time, td.trip_log ) as project_emp  where   		 alldata.trip_time=project_emp.trip_time and alldata.trip_log=project_emp.trip_log))as tb    ";
						*/
				con = dbConn.connectDB();
				if (project.equals("") == false) {
					query = query + " and  PROJECT_Emp.id=? ";

				}
				stmt = con.prepareStatement(query);
				if (project.equals("") == false) {

					stmt.setString(1, project);
				}
				System.out.println("Query: " + query);

				rs = stmt.executeQuery();

				while (rs.next()) {
					ProjectWiseTripBillDto dto = new ProjectWiseTripBillDto();
					dto.setTripDate(OtherFunctions
							.changeDateFromatToddmmyyyy(rs
									.getString("trip_date")));
					dto.setProject(rs.getString("projectdescription"));
					dto.setProjectcode(rs.getString("projectCode"));
					dto.setTotalEmployees(rs.getInt("empCount"));
					dto.setPlannedTotalcost(rs.getDouble("PROJECTCOST"));
					dto.setPlannedPerInstanceCost(rs.getDouble("PERINSTANCE"));
					dto.setPlannedCostPerEmployees(rs.getDouble("twoway"));
					dto.setTripTime(rs.getString("trip_time"));
					dto.setTripLog(rs.getString("trip_log"));

					if (dto.getTripLog().equals("IN")) {
						dto.setTripLog("Pick Up");
					}
					if (dto.getTripLog().equals("OUT")) {
						dto.setTripLog("Drop");
					}

					dtoList.add(dto);

				}

				return dtoList;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}

	public List<ProjectWiseTripBillDto> getProjectWiseBillingReportForManager(
			String fromDate, String toDate, String siteId, String manager) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<ProjectWiseTripBillDto> dtoList = new ArrayList<ProjectWiseTripBillDto>();

		siteId = siteId == null ? "" : siteId;
		fromDate = fromDate == null ? "" : fromDate;
		toDate = toDate == null ? "" : toDate;
		manager = manager == null ? "" : manager;
		fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);

		try {

			if ((fromDate + toDate + siteId + manager).trim().equals("") == false) {
				// Error in the query****************************************8
				// String
				// query="select site.site_name,site.id, convert(varchar, PlannedTrip.trip_date,103) trip_date,prj.description project, prj.project projectCode , PlannedTrip.totalEmployees,PlannedTrip.totalCost plannedTotalCost, PlannedTrip.perInstanceCost plannedPerInstanceCost,PlannedTrip.perInstanceCost*2 plannedCostPerEmployees, ifnull(ActualTrip.totalCost,0) actualTotalCost,ifnull(ActualTrip.perInstanceCost,0)  actualPerInstanceCost,ActualTrip.perInstanceCost*2 actualCostPerEmployees  from atsconnect prj, site,  ( select distinct totalEmployee.siteId, totalEmployee.trip_date, totalEmployee.totalEmployees, totalCost.totalCost,totalCost.totalCost/ totalEmployee.totalEmployees perInstanceCost,totalEmployee.project from (select td.siteId, count(*) totalEmployees, td.trip_date, esh.project from trip_details_child tdc, trip_details td, employee_schedule esh where td.id=tdc.tripId and tdc.scheduleId=esh.id   group by td.siteId,td.trip_date, esh.project) totalEmployee,(select td.siteId, count(td.id) noOfTrips, td.vehicle_type, td.security_status, vht.type, vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) cost, count(td.id)*vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) totalCost, td.trip_date   from trip_details td, vehicle_type vht where  vht.id=td.vehicle_type group by td.siteId, td.vehicle_type, td.security_status,vht.type,   vht.ratePerTrip , td.trip_date ) noOfTrips, ( select siteId, sum(totalCost) totalCost,trip_date from( select td.siteId, count(td.id) noOfTrips, td.vehicle_type, td.security_status,    vht.type, vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) cost,    count(td.id)*vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) totalCost,    td.trip_date  from trip_details td, vehicle_type vht where   vht.id=td.vehicle_type group by td.siteId, td.vehicle_type,    td.security_status,vht.type, vht.ratePerTrip , td.trip_date    ) s group by siteId, trip_date)totalCost 	where       totalEmployee.trip_date=noOfTrips.trip_date and totalEmployee.trip_date=totalCost.trip_date and totalEmployee.siteId=   noOfTrips.siteId  ) PlannedTrip    left join (    select distinct  totalEmployee.siteId,  totalEmployee.trip_date, totalEmployee.totalEmployees, totalCost.totalCost,totalCost.totalCost/ totalEmployee.totalEmployees   perInstanceCost,totalEmployee.project from     (select td.siteId, count(*) totalEmployees, td.trip_date, esh.project from trip_details_child tdc, trip_details td, employee_schedule esh   where td.id=tdc.tripId and tdc.scheduleId=esh.id   group by td.siteId, td.trip_date, esh.project) totalEmployee,      (select td.siteId, count(td.id) noOfTrips, td.vehicle_type, vtp.escort, vht.type, vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) cost, count(td.id)*vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end)    totalCost, td.trip_date  from trip_details td, vehicle_type vht, vendor_trip_sheet_parent vtp where     vht.id=td.vehicle_type and vtp.tripId=td.id  group by td.siteId, td.vehicle_type, vtp.escort,vht.type, vht.ratePerTrip ,    td.trip_date  ) noOfTrips,( select siteId, sum(totalCost) totalCost,trip_date from(select td.siteId, count(td.id) noOfTrips, td.vehicle_type,    vtp.escort, vht.type, vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) cost,    count(td.id)*vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) totalCost, td.trip_date     from trip_details td, vehicle_type vht, vendor_trip_sheet_parent vtp where  vht.id=td.vehicle_type and vtp.tripId=td.id     group by td.siteId, td.vehicle_type, vtp.escort,vht.type, vht.ratePerTrip , td.trip_date  )    s group by siteId, trip_date)totalCost    where     totalEmployee.trip_date=noOfTrips.trip_date and totalEmployee.trip_date=totalCost.trip_date  and totalEmployee.siteId=   noOfTrips.siteId   ) ActualTrip    on ActualTrip.trip_date=PlannedTrip.trip_date and ActualTrip.project=PlannedTrip.project and    PlannedTrip.siteId=ActualTrip.siteId   where prj.id=PlannedTrip.project and site.id=PlannedTrip.siteId and PlannedTrip.trip_date between '"+fromDate+"' and '"+toDate+"' and PlannedTrip.siteId="
				// +siteId;
				String query = "select * from ( select site.site_name,site.id,  PlannedTrip.trip_date trip_date,prj.description project, prj.project projectCode , PlannedTrip.totalEmployees,PlannedTrip.totalCost plannedTotalCost, PlannedTrip.perInstanceCost plannedPerInstanceCost,PlannedTrip.perInstanceCost*2 plannedCostPerEmployees, ifnull(ActualTrip.totalCost,0) actualTotalCost,ifnull(ActualTrip.perInstanceCost,0)  actualPerInstanceCost,ActualTrip.perInstanceCost*2 actualCostPerEmployees  from atsconnect prj, site,  ( select distinct totalEmployee.siteId, totalEmployee.trip_date, totalEmployee.totalEmployees, totalCost.totalCost,totalCost.totalCost/ totalEmployee.totalEmployees perInstanceCost,totalEmployee.project from (select td.siteId, count(*) totalEmployees, td.trip_date, esh.project from trip_details_child tdc, trip_details td, employee_schedule esh where td.id=tdc.tripId and tdc.scheduleId=esh.id   group by td.siteId,td.trip_date, esh.project) totalEmployee,(select td.siteId, count(td.id) noOfTrips, td.vehicle_type, td.security_status, vht.type, vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) cost, count(td.id)*vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) totalCost, td.trip_date   from trip_details td, vehicle_type vht where  vht.id=td.vehicle_type group by td.siteId, td.vehicle_type, td.security_status,vht.type,   vht.ratePerTrip , td.trip_date ) noOfTrips, ( select siteId, sum(totalCost) totalCost,trip_date from( select td.siteId, count(td.id) noOfTrips, td.vehicle_type, td.security_status,    vht.type, vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) cost,    count(td.id)*vht.ratePerTrip * (case when td.security_status='YES' then 2 else 1 end) totalCost,    td.trip_date  from trip_details td, vehicle_type vht where   vht.id=td.vehicle_type group by td.siteId, td.vehicle_type,    td.security_status,vht.type, vht.ratePerTrip , td.trip_date    ) s group by siteId, trip_date)totalCost 	where       totalEmployee.trip_date=noOfTrips.trip_date and totalEmployee.trip_date=totalCost.trip_date and totalEmployee.siteId=   noOfTrips.siteId  ) PlannedTrip    left join (    select distinct  totalEmployee.siteId,  totalEmployee.trip_date, totalEmployee.totalEmployees, totalCost.totalCost,totalCost.totalCost/ totalEmployee.totalEmployees   perInstanceCost,totalEmployee.project from     (select td.siteId, count(*) totalEmployees, td.trip_date, esh.project from trip_details_child tdc, trip_details td, employee_schedule esh   where td.id=tdc.tripId and tdc.scheduleId=esh.id   group by td.siteId, td.trip_date, esh.project) totalEmployee,      (select td.siteId, count(td.id) noOfTrips, td.vehicle_type, vtp.escort, vht.type, vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) cost, count(td.id)*vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end)    totalCost, td.trip_date  from trip_details td, vehicle_type vht, vendor_trip_sheet_parent vtp where     vht.id=td.vehicle_type and vtp.tripId=td.id  group by td.siteId, td.vehicle_type, vtp.escort,vht.type, vht.ratePerTrip ,    td.trip_date  ) noOfTrips,( select siteId, sum(totalCost) totalCost,trip_date from(select td.siteId, count(td.id) noOfTrips, td.vehicle_type,    vtp.escort, vht.type, vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) cost,    count(td.id)*vht.ratePerTrip * (case when vtp.escort='YES' then 2 else 1 end) totalCost, td.trip_date     from trip_details td, vehicle_type vht, vendor_trip_sheet_parent vtp where  vht.id=td.vehicle_type and vtp.tripId=td.id     group by td.siteId, td.vehicle_type, vtp.escort,vht.type, vht.ratePerTrip , td.trip_date  )    s group by siteId, trip_date)totalCost    where     totalEmployee.trip_date=noOfTrips.trip_date and totalEmployee.trip_date=totalCost.trip_date  and totalEmployee.siteId=   noOfTrips.siteId   ) ActualTrip    on ActualTrip.trip_date=PlannedTrip.trip_date and ActualTrip.project=PlannedTrip.project and    PlannedTrip.siteId=ActualTrip.siteId   where prj.id=PlannedTrip.project and site.id=PlannedTrip.siteId and PlannedTrip.trip_date between '"
						+ fromDate
						+ "' and '"
						+ toDate
						+ "' and PlannedTrip.siteId="
						+ siteId
						+ " ) pr join ( select distinct p.project from atsconnect p , employee e, ( select * from employee_subscription sb, employee_schedule sh where sb.subscriptionID=sh.subscription_id and sh.from_date<='"
						+ toDate
						+ "' and ifnull(sh.cancel_date,sh.to_date) >='"
						+ fromDate
						+ "' and  '"
						+ fromDate
						+ "' <='"
						+ toDate
						+ "' and  (sb.spoc="
						+ manager
						+ " or sb.supervisor="
						+ manager
						+ " ) ) sub where (e.id=sub.spoc or e.id=sub.supervisor) and p.id=sub.project and e.site="
						+ siteId + " ) ppr on  pr.projectCode=ppr.project ";

				System.out.println("Query: " + query);

				con = dbConn.connectDB();
				stmt = con.createStatement();

				rs = stmt.executeQuery(query);

				while (rs.next()) {
					ProjectWiseTripBillDto dto = new ProjectWiseTripBillDto();
					dto.setTripDate(rs.getString("trip_date"));
					dto.setProject(rs.getString("project"));
					dto.setProjectcode(rs.getString("projectCode"));
					dto.setTotalEmployees(rs.getInt("totalEmployees"));
					dto.setPlannedTotalcost(rs.getInt("plannedTotalCost"));
					dto.setPlannedPerInstanceCost(rs
							.getInt("plannedPerInstanceCost"));
					dto.setPlannedCostPerEmployees(rs
							.getInt("plannedCostPerEmployees"));
					dto.setActualTotalCost(rs.getInt("actualTotalCost"));
					dto.setActualPerInstanceCost(rs
							.getInt("actualPerInstanceCost"));
					dto.setActualCostPerEmployees(rs
							.getInt("actualCostPerEmployees"));

					dtoList.add(dto);

				}

				return dtoList;
			} else {
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}

	public List<TripBillDto> getBillingReportDetails(String month, String year,
			String site) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<TripBillDto> dtoList = new ArrayList<TripBillDto>();
		String monthQuery = "";
		String yearQuery = "";
		String siteQuery = "";

		if (site != null && !site.equals("")) {
			siteQuery = " where siteId=" + site;
		}

		if (month != null && !month.equals("")) {

			monthQuery = " and monthNo1=" + month + " ";
		}
		if (year != null && !year.equals("")) {

			yearQuery = " and  year=" + year + " ";
		}
		/*
		 * if (site != null && !site.equals("")) { siteQuery =
		 * " and td.siteId='" + site + "'"; }
		 */

		try {

			// String
			// query="select * from (    select                                         datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART  (year, td.trip_date)) as month,count (*) actualTrips,vt.type, 'With Escort' escortType, site.site_name, site.id siteId, vt.ratePerTrip*2 perTripCost1  from trip_details td, vehicle_type vt, site  where  vehicle_type=vt.id and site.id=td.siteId  "+
			// monthQuery+yearQuery+"  group by                             datename(month,trip_date) + ' ' +convert(varchar,   year(trip_date)), site.site_name, site.id   , vt.type, vt.ratePerTrip   ) as actualTrip   left join   ( select  									   datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) as   month, count(*) submittedTrips, vt.type, site.site_name from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site where   td.id=vtp.tripId and   vehicle_type=vt.id  and site.id=td.siteId     "
			// + monthQuery + yearQuery +
			// "   group by                              datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)), site.site_name    , vt.type   ) submittedTrip    on actualTrip.month=submittedTrip.month and   actualTrip.type=submittedTrip.type and actualTrip.site_name=submittedTrip.site_name    left join  (     select  								        datename(month,td.trip_date) + ' ' +   convert(varchar, DATEPART(year,td.trip_date)) as month , count(*) approvedTrips, vt.type,   2*count(*)* vt.ratePerTrip as TotalCost, vt.ratePerTrip*2 perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site where vtp.escort='YES' and  (vtp.approvalStatus='Approved by Transport Admin' or   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and vehicle_type=vt.id   and site.id=td.siteId    "
			// + monthQuery + yearQuery +
			// "  group by  							 datename(month,  td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) , site.site_name , vt.type,  vt.ratePerTrip   ) approvedTrip    on    actualTrip.month=approvedTrip.month and   actualTrip.type=approvedTrip.type  and actualTrip.site_name=approvedTrip.site_name   UNION    select * from (    select  										datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART  (year, td.trip_date)) as month,count (*) actualTrips,vt.type, 'Without Escort' escortType , site.site_name, site.id siteId, vt.ratePerTrip perTripCost1  from trip_details td, vehicle_type vt, site  where  vehicle_type=vt.id and site.id=td.siteId   "
			// + monthQuery + yearQuery +
			// "  group by                            datename(month,trip_date) + ' ' +convert(varchar,   year(trip_date)), site.site_name, site.id   , vt.type , vt.ratePerTrip  ) as actualTrip   left join     ( select  										datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) as   month, count(*) submittedTrips, vt.type, site.site_name from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site where   td.id=vtp.tripId and   vehicle_type=vt.id  and site.id=td.siteId    "
			// + monthQuery + yearQuery +
			// "   group by                              datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)), site.site_name    , vt.type   ) submittedTrip    on actualTrip.month=submittedTrip.month and   actualTrip.type=submittedTrip.type and actualTrip.site_name=submittedTrip.site_name    left join  (     select  									  datename(month,td.trip_date) + ' ' +   convert(varchar, DATEPART(year,td.trip_date)) as month , count(*) approvedTrips, vt.type,  count(*)* vt.ratePerTrip as TotalCost, vt.ratePerTrip perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site where vtp.escort='NO' and  (vtp.approvalStatus='Approved by Transport Admin' or   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and vehicle_type=vt.id   and site.id=td.siteId    "
			// + monthQuery + yearQuery +
			// "     group by  							 datename(month,  td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) , site.site_name , vt.type,  vt.ratePerTrip   ) approvedTrip    on    actualTrip.month=approvedTrip.month and   actualTrip.type=approvedTrip.type  and actualTrip.site_name=approvedTrip.site_name "
			// + siteQuery;
			String query = "select fromDate1, toDate1,monthNo1,  month1,year, actualTrips, submittedTrips, type1, escortType, site_name1,siteId, perTripCost1,approvedTrips,TotalCost from (   select * from (     select  rate.fromDate1, rate.toDate toDate1, month(td.trip_date) monthNo1,  year(td.trip_date) year,date_format(td.trip_date,'%M %Y')  as month1, count(*) actualTrips,vt.type type1, 'With Escort' escortType, site.site_name site_name1, site.id siteId, rate.ratePerTrip *2 perTripCost1  from trip_details td, vehicle_type vt, site, site_vehicle sv  , (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate fromDate1, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate  where td.trip_date >= rate.fromDate1 and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type   and vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and td.vehicle_type=vt.id and site.id=td.siteId and td.security_status='YES'  and ( td.status='saved' or td.status='addsave' or td.status='saveedit' )     group by year(td.trip_date), month(trip_date),  date_format(trip_date,'%M %Y') , site.site_name, site.id   , vt.type,  rate.fromDate1, rate.toDate ,rate.ratePerTrip     ) as actualTrip   left join   	( select rate.fromDate2, month(td.trip_date) monthNo2, date_format(td.trip_date,'%M %Y') as   month2, count(*) submittedTrips, vt.type type2, site.site_name site_name2 from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site, site_vehicle sv 	, (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate fromDate2, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate 	where  td.trip_date >= rate.fromDate2 and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type 	and  vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and td.id=vtp.tripId and   td.vehicle_type=vt.id  and site.id=td.siteId  and td.security_status='YES' and (vtp.approvalStatus='Approved by Transport Manager' or vtp.approvalStatus='Sent for TM approval' or vtp.approvalStatus='Rejected by Transport Manager' or vtp.approvalStatus='Rejected by Transport Co-ordinator'  or vtp.approvalStatus='Sent for TC approval'  )       group by month(trip_date), date_format(td.trip_date,'%M %Y') , site.site_name    , vt.type  ,rate.fromDate2, rate.ratePerTrip 	) submittedTrip   	 on actualTrip.month1=submittedTrip.month2 and    actualTrip.type1=submittedTrip.type2 and actualTrip.site_name1=submittedTrip.site_name2  and actualTrip.fromDate1=submittedTrip.fromDate2   	 left join  (     	 select rate.fromDate,  month(td.trip_date) monthNo,date_format(td.trip_date,'%M %Y') as month , count(*) approvedTrips, vt.type,   2*count(*)* rate.ratePerTrip as TotalCost, rate.ratePerTrip*2 perTripCost, site.site_name site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site, site_vehicle sv 	 , (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate	 where   td.trip_date >= rate.fromDate and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type	 and vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and vtp.escort='YES' and  (  vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and td.vehicle_type=vt.id   and site.id=td.siteId     group by month(trip_date), date_format(td.trip_date,'%M %Y') , site.site_name , vt.type, rate.fromDate,  rate.ratePerTrip   ) approvedTrip    on    actualTrip.month1=approvedTrip.month and   actualTrip.type1=approvedTrip.type  and actualTrip.site_name1=approvedTrip.site_name and approvedTrip.fromDate=actualTrip.fromDate1   	 UNION    	select * from (    	select rate.fromDate1, rate.toDate toDate1, month(td.trip_date) monthNo1,  year(td.trip_date) year, date_format(td.trip_date,'%M %Y') as month1,count(*) actualTrips,vt.type type1, 'Without Escort' escortType , site.site_name site_name1, site.id siteId, rate.ratePerTrip perTripCost1  from trip_details td, vehicle_type vt, site, site_vehicle sv	, (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate fromDate1, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate	where td.trip_date >= rate.fromDate1 and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type	 and vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and td.vehicle_type=vt.id and site.id=td.siteId   and td.security_status='NO'  and ( td.status='saved' or td.status='addsave' or td.status='saveedit' )     group by year(td.trip_date),month(trip_date),date_format(trip_date,'%M %Y'), site.site_name, site.id   , vt.type , rate.fromDate1, rate.toDate, rate.ratePerTrip  	) as actualTrip   left join     	( select rate.fromDate2, month(td.trip_date) monthNo2, date_format(td.trip_date,'%M %Y') as   month2, count(*) submittedTrips, vt.type type2, site.site_name site_name2 from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site, site_vehicle sv 	, (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate fromDate2, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate	where  td.trip_date >= rate.fromDate2 and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type	and     vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and td.id=vtp.tripId and  td.vehicle_type=vt.id  and site.id=td.siteId   and td.security_status='NO' and (vtp.approvalStatus='Approved by Transport Manager' or vtp.approvalStatus='Sent for TM approval' or vtp.approvalStatus='Rejected by Transport Manager' or vtp.approvalStatus='Rejected by Transport Co-ordinator'  or vtp.approvalStatus='Sent for TC approval'  )       group by  month(trip_date), date_format(td.trip_date,'%M %Y'), site.site_name    , vt.type, rate.fromDate2   	) submittedTrip    on actualTrip.month1=submittedTrip.month2 and   actualTrip.type1=submittedTrip.type2 and actualTrip.site_name1=submittedTrip.site_name2 and actualTrip.fromDate1=submittedTrip.fromDate2   	 left join  	 ( 	 select rate.fromDate, month(td.trip_date) monthNo, date_format(td.trip_date,'%M %Y') as month , count(*) approvedTrips, vt.type,  count(*)* rate.ratePerTrip as TotalCost, rate.ratePerTrip perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site, site_vehicle sv 	  , (select   site,vehicle_type vehicle_type, ratePerTrip, fromDate, ifnull(toDate,curdate()) toDate from site_vehicle_rate_calander  ) rate	 where   td.trip_date >= rate.fromDate and td.trip_date <ifnull(rate.toDate,curdate()) and td.siteId=rate.site and td.vehicle_type=rate.vehicle_type	 and   vt.id=sv.vehicleTypeId and sv.siteId=td.siteId and vtp.escort='NO' and  (   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and td.vehicle_type=vt.id   and site.id=td.siteId      group by  month(trip_date), date_format(td.trip_date,'%M %Y'), site.site_name , vt.type, rate.fromDate, rate.ratePerTrip   	 ) approvedTrip    on    actualTrip.month1=approvedTrip.month and   actualTrip.type1=approvedTrip.type  and actualTrip.site_name1=approvedTrip.site_name and actualTrip.fromDate1=approvedTrip.fromDate   	    ) s "
					+ siteQuery
					+ yearQuery
					+ monthQuery
					+ " order by siteId,    monthNo1 asc, type1, escortType asc ";
			// String query =
			// " select * from (   select  datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART  (year, td.trip_date)) as month,count (*) actualTrips,vt.type, site.site_name, site.id siteId  from trip_details td, vehicle_type vt, site  where  vehicle_type=vt.id and site.id=td.siteId  "
			// + monthQuery + yearQuery +
			// "  group by  datename(month,trip_date) + ' ' +convert(varchar,   year(trip_date)), site.site_name, site.id   , vt.type   ) as actualTrip left join   ( select  datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) as   month, count(*) submittedTrips, vt.type, site.site_name from trip_details td,   vendor_trip_sheet_parent vtp, vehicle_type vt, site where   td.id=vtp.tripId and   vehicle_type=vt.id  and site.id=td.siteId   "
			// + monthQuery + yearQuery +
			// "  group by    datename(month,td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)), site.site_name    , vt.type   ) submittedTrip  on actualTrip.month=submittedTrip.month and   actualTrip.type=submittedTrip.type left join  "
			// +
			// "(  select  datename(month,td.trip_date) + ' ' +   convert(varchar, DATEPART(year,td.trip_date)) as month , count(*) approvedTrips, vt.type,   2*count(*)* vt.ratePerTrip as TotalCost, vt.ratePerTrip*2 perTripCost, site.site_name from   trip_details td, vendor_trip_sheet_parent vtp, vehicle_type vt, site where vtp.escort='YES' and  (vtp.approvalStatus='Approved by Transport Admin' or   vtp.approvalStatus='Approved by Transport Manager' )  and td.id=vtp.tripId and vehicle_type=vt.id   and site.id=td.siteId   "
			// + monthQuery + yearQuery +
			// "    group by  datename(month,  td.trip_date) + ' ' +convert(varchar, DATEPART(year,td.trip_date)) , site.site_name , vt.type,  vt.ratePerTrip   "
			// +
			//
			// ") approvedTrip on    actualTrip.month=approvedTrip.month and   actualTrip.type=approvedTrip.type  ";
			System.out.println("Query: " + query);

			con = dbConn.connectDB();
			stmt = con.createStatement();

			rs = stmt.executeQuery(query);

			while (rs.next()) {
				TripBillDto dto = new TripBillDto();
				dto.setMonthNo(rs.getInt("monthNo1"));
				dto.setMonth(rs.getString("month1"));
				dto.setActualTrips(rs.getInt("actualTrips"));
				dto.setType(rs.getString("type1"));
				dto.setSite(rs.getString("site_name1"));
				dto.setSubmittedTrips(rs.getInt("submittedTrips"));
				// dto.setApprovedTripsWithEscort(rs.getInt("approvedTripsWithEscort"));
				// dto.setApprovedTripsWithoutEscort(rs.getInt("approvedTripsWithoutEscort"));
				dto.setApprovedTrips(rs.getInt("approvedTrips"));
				dto.setType(dto.getType() + " " + rs.getString("escortType"));

				// dto.setTotalCostWithEscort(rs.getInt("TotalCostWithEscort"));
				// dto.setTotalCostWithoutEscort(rs.getInt("TotalCostWithoutEscort"));
				dto.setTotalCost(rs.getInt("TotalCost"));

				// dto.setPerTripCostWithEscort(rs.getInt("perTripCostWithEscort"));
				// dto.setPerTripCostWithoutEscort(rs.getInt("perTripCostWithoutEscort"));
				dto.setPerTripCost(rs.getInt("perTripCost1"));
				dtoList.add(dto);

			}
			return dtoList;

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;

	}

	public List<CapacityUtilizationReportDto> getCapacityUtilizationReportAvailableVsPlanned(
			String fromDate, String toDate, String siteId, String logInTime,
			String logOutTime, String vehicleTypeId) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<CapacityUtilizationReportDto> dtoList = new ArrayList<CapacityUtilizationReportDto>();

		siteId = siteId == null ? "" : siteId;
		fromDate = fromDate == null ? "" : fromDate;
		toDate = toDate == null ? "" : toDate;
		vehicleTypeId = vehicleTypeId == null ? "" : vehicleTypeId;
		fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);

		String tripTimeQuery = "";
		String tripTimeQueryIn = "";
		String tripTimeQueryOut = "";
		String tripDateQuery = "";
		String vehicleTypeQuery = "";
		if (logInTime != null && logInTime.trim().equals("") == false) {
			tripTimeQueryIn = "   a.trip_time='" + logInTime
					+ "' and a.trip_log='IN' ";
		}

		if (logOutTime != null && logOutTime.trim().equals("") == false) {
			tripTimeQueryOut = "   a.trip_time='" + logOutTime
					+ "' and a.trip_log='OUT' ";
		}

		if (tripTimeQueryIn.equals("") == false
				&& tripTimeQueryOut.equals("") == false) {
			tripTimeQuery = " and ( ( " + tripTimeQueryIn + " ) or ( "
					+ tripTimeQueryOut + ") ) ";
		} else if (tripTimeQueryIn.equals("") == false) {
			tripTimeQuery = " and " + tripTimeQueryIn;
		} else if (tripTimeQueryOut.equals("") == false) {
			tripTimeQuery = " and " + tripTimeQueryOut;
		}
		if (fromDate.equals("") == false) {
			tripDateQuery = " and a.trip_date>='" + fromDate + "' ";
		}
		if (toDate.equals("") == false) {
			tripDateQuery += " and a.trip_date<='" + toDate + "' ";
		}
		if (vehicleTypeId.equals("") == false) {
			vehicleTypeQuery = " and a.vehicle_type=" + vehicleTypeId;
		}

		String siteQuery = "   a.site=" + siteId;
		String subQuery = "where " + siteQuery + tripTimeQuery + tripDateQuery
				+ vehicleTypeQuery;

		try {

			if ((fromDate + toDate + siteId).trim().equals("") == false) {
				String query = "select a.site, site.site_name, a.trip_date, a.trip_time, a.trip_log, a.vehicle_type, vt.type vehicleTypeName, a.vehicleCount, a.availableCapacity,p.plannedCapacity, ifnull( p1.actualCapacity,0) actualCapacity  from ( select td.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type , count(*) vehicleCount,(select sit_cap from vehicle_type where id=td.vehicle_type) * count(*) availableCapacity from ( select distinct td.*, tc.site from trip_details td join  ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)   tc on td.id=tc.tripId   ) td where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   )  group by td.site, td.trip_date, td.trip_time, td.trip_log, td.vehicle_type   ) a join ( select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  , count(*) plannedCapacity from trip_details td join   ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)  tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  ) p on a.site=p.site and  a.trip_date=p.trip_date and a.trip_time=p.trip_time and a.trip_log=p.trip_log and a.vehicle_type=p.vehicle_type   join vehicle_type vt on p.vehicle_type=vt.id join site  on p.site=site.id  left join (select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type, count(*) actualCapacity from (select vtd.approvalStatus,td.* from vendor_trip_sheet_parent vtd join trip_details td on vtd.tripId=td.id  )td join ( select tc.*,e.site  from vendor_trip_sheet tc join employee e on tc.employeeId=e.id)    tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) and tc.showStatus='show' and td.approvalStatus='Approved by Transport Manager' group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  )   p1 on a.site=p1.site and a.trip_date=p1.trip_date and a.trip_time=p1.trip_time and a.trip_log=p1.trip_log and a.vehicle_type=p1.vehicle_type     "
						+ subQuery;

				System.out.println("Query: " + query);

				con = dbConn.connectDB();
				stmt = con.createStatement();

				rs = stmt.executeQuery(query);

				while (rs.next()) {
					CapacityUtilizationReportDto dto = new CapacityUtilizationReportDto();

					dto.setSiteId(rs.getString("site"));
					dto.setSiteName(rs.getString("site_name"));
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
					dto.setVehicleTypeName(rs.getString("vehicleTypeName"));
					dto.setTripDate(rs.getDate("trip_date"));
					dto.setAvaiableCapacity(rs.getInt("availableCapacity"));
					dto.setPlannedCapacity(rs.getInt("plannedCapacity"));
					dto.setActualCapcity(rs.getInt("actualCapacity"));
					dto.setTripTime(rs.getString("trip_time"));
					dto.setTripLog(rs.getString("trip_log"));
					dto.setAvaliableVsPlanned(dto.getPlannedCapacity() * 100
							/ dto.getAvaiableCapacity());
					dto.setPlannedVsActual(dto.getActualCapcity() * 100
							/ dto.getPlannedCapacity());

					dtoList.add(dto);

				}

				return dtoList;
			} else {
				System.out.println("No parameter selected");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}

	public List<CapacityUtilizationReportDto> getCapacityUtilizationReportAvailableVsPlannedSummary(
			String fromDate, String toDate, String siteId, String logInTime,
			String logOutTime) {

		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<CapacityUtilizationReportDto> dtoList = new ArrayList<CapacityUtilizationReportDto>();

		siteId = siteId == null ? "" : siteId;
		fromDate = fromDate == null ? "" : fromDate;
		toDate = toDate == null ? "" : toDate;

		fromDate = OtherFunctions.changeDateFromatToIso(fromDate);
		toDate = OtherFunctions.changeDateFromatToIso(toDate);

		String tripTimeQuery = "";
		String tripTimeQueryIn = "";
		String tripTimeQueryOut = "";
		String tripDateQuery = "";
		if (logInTime != null && logInTime.trim().equals("") == false) {
			tripTimeQueryIn = "   a.trip_time='" + logInTime
					+ "' and a.trip_log='IN' ";
		}

		if (logOutTime != null && logOutTime.trim().equals("") == false) {
			tripTimeQueryOut = "   a.trip_time='" + logOutTime
					+ "' and a.trip_log='OUT' ";
		}

		if (tripTimeQueryIn.equals("") == false
				&& tripTimeQueryOut.equals("") == false) {
			tripTimeQuery = " and ( ( " + tripTimeQueryIn + " ) or ( "
					+ tripTimeQueryOut + ") ) ";
		} else if (tripTimeQueryIn.equals("") == false) {
			tripTimeQuery = " and " + tripTimeQueryIn;
		} else if (tripTimeQueryOut.equals("") == false) {
			tripTimeQuery = " and " + tripTimeQueryOut;
		}
		if (fromDate.equals("") == false) {
			tripDateQuery = " and a.trip_date>='" + fromDate + "' ";
		}
		if (toDate.equals("") == false) {
			tripDateQuery += " and a.trip_date<='" + toDate + "' ";
		}

		String siteQuery = "   a.site=" + siteId;
		String subQuery = "where " + siteQuery + tripTimeQuery + tripDateQuery;

		try {

			if ((fromDate + toDate + siteId).trim().equals("") == false) {
				String query = " select site site, site_name, trip_date, vehicle_type, vehicleTypeName, sum(availableCapacity) availableCapacity, sum(plannedCapacity) plannedCapacity, sum(actualCapacity) actualCapacity  from  ("
						+ " select a.site, site.site_name, a.trip_date, a.trip_time, a.trip_log, a.vehicle_type, vt.type vehicleTypeName, a.vehicleCount, a.availableCapacity,p.plannedCapacity, ifnull( p1.actualCapacity,0) actualCapacity  from ( select td.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type , count(*) vehicleCount,(select sit_cap from vehicle_type where id=td.vehicle_type) * count(*) availableCapacity from ( select distinct td.*, tc.site from trip_details td join  ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)   tc on td.id=tc.tripId   ) td where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   )  group by td.site, td.trip_date, td.trip_time, td.trip_log, td.vehicle_type   ) a join ( select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  , count(*) plannedCapacity from trip_details td join   ( select tc.*,e.site  from trip_details_child tc join employee e on tc.employeeId=e.id)  tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  ) p on a.site=p.site and  a.trip_date=p.trip_date and a.trip_time=p.trip_time and a.trip_log=p.trip_log and a.vehicle_type=p.vehicle_type   join vehicle_type vt on p.vehicle_type=vt.id join site  on p.site=site.id  left join (select  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type, count(*) actualCapacity from (select vtd.approvalStatus,td.*  from vendor_trip_sheet_parent vtd join trip_details td on vtd.tripId=td.id  )td join ( select tc.*,e.site  from vendor_trip_sheet tc join employee e on tc.employeeId=e.id)    tc on td.id=tc.tripId where ( td.status='saved' or td.status='addsave' or td.status='saveedit'   ) and tc.showStatus='show' and td.approvalStatus='Approved by Transport Manager' group by  tc.site,  td.trip_date, td.trip_time, td.trip_log, td.vehicle_type  )   p1 on a.site=p1.site and a.trip_date=p1.trip_date and a.trip_time=p1.trip_time and a.trip_log=p1.trip_log and a.vehicle_type=p1.vehicle_type     "
						+ subQuery
						+ " )summary group by site, site_name, trip_date, vehicle_type, vehicleTypeName ";

				System.out.println("Query: " + query);

				con = dbConn.connectDB();
				stmt = con.createStatement();

				rs = stmt.executeQuery(query);

				while (rs.next()) {
					CapacityUtilizationReportDto dto = new CapacityUtilizationReportDto();

					dto.setSiteId(rs.getString("site"));
					dto.setSiteName(rs.getString("site_name"));
					dto.setVehicleTypeId(rs.getString("vehicle_type"));
					dto.setVehicleTypeName(rs.getString("vehicleTypeName"));
					dto.setTripDate(rs.getDate("trip_date"));
					dto.setAvaiableCapacity(rs.getInt("availableCapacity"));
					dto.setPlannedCapacity(rs.getInt("plannedCapacity"));
					dto.setActualCapcity(rs.getInt("actualCapacity"));

					dto.setAvaliableVsPlanned(dto.getPlannedCapacity() * 100
							/ dto.getAvaiableCapacity());
					dto.setPlannedVsActual(dto.getActualCapcity() * 100
							/ dto.getPlannedCapacity());

					dtoList.add(dto);

				}

				return dtoList;
			} else {
				System.out.println("No parameter selected");
				return null;
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(stmt);
			DbConnect.closeConnection(con);
		}

		return null;
	}

	public List<AgileBillDto> getAgileBill(int month, int year,String site) {
		month--;
		DbConnect dbConn = DbConnect.getInstance();
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		List<AgileBillDto> dtoList = new ArrayList<AgileBillDto>();
		Calendar cal1 = Calendar.getInstance();
		cal1.setTime(new Date());
		int startMonth = 0;
		int endMonth = cal1.get(Calendar.MONTH);
		int monthCounter = month;

		try {
			if (month >= 0 && month <= 11) {
				startMonth = month;
				endMonth = month;
			}

			con = dbConn.connectDB();
			stmt = con.createStatement();

			for (monthCounter = startMonth; monthCounter <= endMonth; monthCounter++) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(new Date());
				System.out.println("Date : " + cal.get(Calendar.MONTH) + " "
						+ cal.get(Calendar.YEAR));
				int lastDayOfMonth = OtherFunctions.getLastDateOfMonth(year,
						monthCounter).get(Calendar.DAY_OF_MONTH);

				cal.set(Calendar.YEAR, year);
				cal.set(Calendar.MONTH, monthCounter);
				cal.set(Calendar.DAY_OF_MONTH, 1);
				String isoDate = OtherFunctions.changeDateFromatToIso(cal
						.getTime());
/*
				String query = "select f.year, f.month, f.monthName, f.firstCount, m.middleCount, l.lastCount, (f.firstCount+ m.middleCount+ l.lastCount)/3 avgCount,(select pricePerSubscription from settings) pricePerSubscription,((f.firstCount+ m.middleCount+ l.lastCount)/3)* (select pricePerSubscription from settings) totalAmount  from ( select year("
						+ isoDate
						+ ") year , month("
						+ isoDate
						+ " ) month, month("
						+ isoDate
						+ ") monthName,  count(*) firstCount from employee_subscription s where s.subscriptionID not in (select subscriptionID   from employee_unsubscription where fromDate<=  date_add( '" 
						+ isoDate
						+ "')+1,  '"
						+ isoDate
						+ "') ) and s.fromDate<=    dateadd(dd,-day('"
						+ isoDate
						+ "')+1,  '"
						+ isoDate
						+ "')  ) f  join ( select year(  '"
						+ isoDate
						+ "' ) year , month( '"
						+ isoDate
						+ "' ) month,  monthname('"
						+ isoDate
						+ "') monthName,  count(*) middleCount from employee_subscription s where s.subscriptionID not in (select subscriptionID   from employee_unsubscription where fromDate<= dateadd(dd,-day('"
						+ isoDate
						+ "')+15,  '"
						+ isoDate
						+ "') ) and s.fromDate<=   dateadd(dd,-day('"
						+ isoDate
						+ "')+15,  '"
						+ isoDate
						+ "')  ) m on f.month=m.month and f.year=m.year  join   ( select  year( '"
						+ isoDate
						+ "' ) year , month( '"
						+ isoDate
						+ "' ) month,  monthname('"
						+ isoDate
						+ "') monthName,  count(*) lastCount from employee_subscription s where s.subscriptionID not in (select subscriptionID   from employee_unsubscription where fromDate<= dateadd(dd,-day('"
						+ isoDate
						+ "'),dateadd(month,1, '"
						+ isoDate
						+ "')) ) and s.fromDate<=dateadd(dd,-day('"
						+ isoDate
						+ "'),dateadd(month,1, '"
						+ isoDate
						+ "'))  ) l on  f.month=l.month and f.year=l.year ";
						*/
				String query="select year('"+isoDate+"') year ,  monthname(date_add('"+isoDate+"',interval -1 day )) monthName,  count(*) totCount from employee_subscription s where s.subscriptionID not in (select subscriptionID   from employee_unsubscription where fromDate<= '"+isoDate+"' ) and s.fromDate<='"+isoDate+"' and s.empID in (select id from employee where site="+site+")"; 
				
				System.out.println("Query: " + query);
				rs = stmt.executeQuery(query);
				if (rs.next()) {
					AgileBillDto dto = new AgileBillDto();
					dto.setYear(rs.getInt("year"));
					dto.setMonth(rs.getInt("month"));
					dto.setMonthName(rs.getString("monthName"));
					dto.setFirstCount(rs.getInt("firstCount"));
					dto.setMiddleCount(rs.getInt("middleCount"));
					dto.setLastCount(rs.getInt("lastCount"));
					dto.setAvgCount(rs.getInt("avgCount"));
					dto.setPricePerSubscription(rs
							.getDouble("pricePerSubscription"));
					dto.setTotalAmount(rs.getDouble("totalAmount"));

					dtoList.add(dto);

				}

			}

			return dtoList;

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
