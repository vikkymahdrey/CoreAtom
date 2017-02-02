package com.agiledge.atom.harddelete.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.agiledge.atom.dbConnect.DbConnect;

public class HardDeleteDao {

	public boolean hardDeleteTripRelatedTables(String site) {
		System.out.println("In hard delete trip");
		boolean val = false;
		Connection con=null;
		PreparedStatement deletTripDetails = null;
		PreparedStatement deletTripDetailsChild = null;
		PreparedStatement deletTripRoute = null;
		PreparedStatement deletTripComment = null;
		PreparedStatement deletTripVendor = null;
		PreparedStatement deletTripDetailsRemoved = null; 
		PreparedStatement deletTripModified = null;
		try {
			// initialise 
			con= DbConnect.getInstance().connectDB();
			con.setAutoCommit(false);
			 
			 deletTripDetails = null;
				String deletTripDetailsChildQuery="delete  a from trip_details_child a, trip_details d where d.siteId=?";
				String deletTripRouteQuery="delete a from triproute a, trip_details d where d.siteId=?";
				String deletTripCommentQuery="delete a from tripcomments a, trip_details d where d.siteId=?";
				String deletTripVendorQuery="delete a from tripvendorassign a, trip_details d where d.siteId=?";
				String deletTripDetailsRemovedQuery="delete a from trip_details_removed a, trip_details d where d.siteId=?"; 
				String deletTripModifiedQuery="delete a from trip_details_modified a, trip_details d where d.siteId=?";
				String deletTripDetailsQuery="delete d from trip_details d where d.siteId=?";
				
				/*delete  a from trip_details_child a, trip_details d where d.siteId=?
				delete a from triproute a, trip_details d where d.siteId=?
				delete a from tripcomments a, trip_details d where d.siteId=?
				delete a from trip_details_removed a, trip_details d where d.siteId=?
				delete a from trip_details_modified a, trip_details d where d.siteId=?
				delete d from trip_details d where d.siteId=?
				*/				
				deletTripDetailsChild=con.prepareStatement(deletTripDetailsChildQuery);
				deletTripDetailsChild.setString(1, site);
				deletTripRoute=con.prepareStatement(deletTripRouteQuery);
				deletTripRoute.setString(1, site);
				deletTripComment=con.prepareStatement(deletTripCommentQuery);
				deletTripComment.setString(1, site);
				deletTripVendor=con.prepareStatement(deletTripVendorQuery);
				deletTripVendor.setString(1, site);
				deletTripDetailsRemoved=con.prepareStatement(deletTripDetailsRemovedQuery);
				deletTripDetailsRemoved.setString(1, site);
				deletTripModified=con.prepareStatement(deletTripModifiedQuery);
				deletTripModified.setString(1, site);
				
				deletTripDetails=con.prepareStatement(deletTripDetailsQuery);
				deletTripDetails.setString(1, site);
				
				deletTripDetailsChild.executeUpdate();
				deletTripRoute.executeUpdate();
				deletTripComment.executeUpdate();
				deletTripVendor.executeUpdate();
				deletTripDetailsRemoved.executeUpdate(); 
				deletTripModified.executeUpdate();
				deletTripDetails.executeUpdate();
				
				con.commit();
				val=true;
		}catch(Exception e) {
			val=false;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(" Error : " + e);
		}
		return val;
	}
	
	public boolean hardDeleteSchedule(String site) {
		System.out.println("In hard delete schedule");
		boolean val = false;
		Connection con=null;
		PreparedStatement deletScheduleAlter = null;
		PreparedStatement deletSchedule = null;
		
		try {
			// initialise 
			con= DbConnect.getInstance().connectDB();
			con.setAutoCommit(false);
			  



				String deletScheduleAlterQuery="delete aa from employee_schedule_alter aa, employee_schedule a, employee d where aa.scheduleId=a.id and a.employee_id=d.id and d.site=?";
				String deletScheduleQuery="delete a from employee_schedule a, employee d where a.employee_id=d.id and d.site=?";
		 
				
				deletScheduleAlter=con.prepareStatement(deletScheduleAlterQuery);
				deletScheduleAlter.setString(1, site);
				deletScheduleAlter.executeUpdate();
				deletSchedule=con.prepareStatement(deletScheduleQuery);
				deletSchedule.setString(1, site);
				deletSchedule.executeUpdate();
				  
				con.commit();
				val=true;
		}catch(Exception e) {
			val=false;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(" Error : " + e);
		}
		return val;
	}


public boolean hardDeleteSubscription(String site) {
	System.out.println("In hard delete subscription");
		boolean val = false;
		Connection con=null;
		PreparedStatement deletUnsubscription = null;
		PreparedStatement deletSchedule = null;
		
		try {
			// initialise 
			con= DbConnect.getInstance().connectDB();
			con.setAutoCommit(false);
			  

			
					

				String deletUnsubscriptionQuery="delete ua from  employee_unsubscription ua, employee_subscription a, employee d where a.subscriptionId=ua.subscriptionId and a.empId=d.id and d.site=?";
				String deletSubscriptionQuery="delete a from employee_subscription a, employee d where a.empId=d.id and d.site=?";
		 
				
				deletUnsubscription=con.prepareStatement(deletUnsubscriptionQuery);
				deletUnsubscription.setString(1, site);
				deletUnsubscription.executeUpdate();
				deletSchedule=con.prepareStatement(deletSubscriptionQuery);
				deletSchedule.setString(1,site);
				deletSchedule.executeUpdate();
				con.commit();
				val=true;
		}catch(Exception e) {
			val=false;
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			System.out.println(" Error : " + e);
		}
		return val;
	}

	

}

