package com.agiledge.atom.service;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.agiledge.atom.billingprocess.dao.BillingProcessDao;
import com.agiledge.atom.billingprocess.dao.BillingProcessDaoImpl;
import com.agiledge.atom.billingprocess.dto.BillingProcess;
import com.agiledge.atom.billingprocess.service.BillingProcessFactory;
import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleTypeDto;
import com.agiledge.atom.dto.VendorDto;
import com.agiledge.atom.transporttype.dto.TransportTypeConfig;

public class TripDetailsService {
	BillingProcessDao billingProcessDao = new BillingProcessDaoImpl();
	public ArrayList<TripDetailsDto> getEmployeeTripSheet(String employeeId) {
		return new TripDetailsDao().getEmployeeTripSheet(employeeId);
	}

	public int trackTripByVendor(ArrayList<TripDetailsChildDto> dtoList) {

		return new TripDetailsDao().trackTripByVendor(dtoList);
	}

	public int unCheckTripByVendor(
			ArrayList<TripDetailsChildDto> unCheckedDtoList) {
		return new TripDetailsDao().unCheckByVendor(unCheckedDtoList);
	}

	public int updateVendorTrip(HashMap<String, TripDetailsDto> trips,
			String user) {
		return new TripDetailsDao().updateVendorTrip(trips, user);
	}

	public ArrayList<TripDetailsDto> getEmployeeTripSheetForVendorTracking(
			String tripDate, String tripMode, String siteId, String tripTime[],
			String personnelNo, String vendor) {
		ArrayList<TripDetailsDto> tripSheetList = null;
		// ArrayList<TripDetailsDto> tripSheetSaved =null;

		tripSheetList = new TripDetailsDao().getTripSheetSaved(tripDate,
				tripMode, siteId, tripTime);

		/*
		 * if (tripSheetList != null && tripSheetList.size() > 0) {
		 * 
		 * if ((vendor != null && !vendor.equals("")) || (personnelNo != null &&
		 * !personnelNo.equals(""))) { tripSheetList = new
		 * TripDetailsDao().getTripSheetSaved_Vendor( tripDate, tripMode,
		 * siteId, tripTime, personnelNo, vendor); } }
		 */

		return tripSheetList;
	}
	
	public  TripDetailsDto  getTripSheetByTrip(String tripId) throws SQLException {
		TripDetailsDto dto=new TripDetailsDao().getTripSheetByTrip(tripId);
			updateDistanceAndTime(dto);
			return dto;
	}

	public ArrayList<TripDetailsDto> getTrackedTripSheet(String tripDate,
			String tripMode, String siteId, String tripTime[],
			String personnelNo, String vendor, String approvalStatus[]) {
		ArrayList<TripDetailsDto> tripSheetList = null;
		// ArrayList<TripDetailsDto> tripSheetSaved =null;
		try {
		/*	tripSheetList = new TripDetailsDao()
			.getTrackedTripSheetBasedOnStatus(tripDate, tripMode,
					siteId, tripTime, approvalStatus);*/
			tripSheetList = new TripDetailsDao()
					.getTrackedTripSheetBasedOnStatus(tripDate, tripMode,
							siteId, tripTime, approvalStatus, personnelNo, vendor);
/*
			if (tripSheetList != null && tripSheetList.size() > 0) {

				if ((vendor != null && !vendor.equals(""))
						|| (personnelNo != null && !personnelNo.equals(""))) {
					System.out.println("Personnel no : " + personnelNo);
					tripSheetList = new TripDetailsDao()
							.getTrackedTripSheetBasedOnStatus_Search(tripDate,
									tripMode, siteId, tripTime, personnelNo,
									vendor, approvalStatus);
				}
			}
*/
		} catch (Exception e) {
			System.out.println("Error in " + e);
		}
		return tripSheetList;
	}
	
	 
	public ArrayList<TripDetailsDto> viewTrackedTrip(String tripId) {
		ArrayList<TripDetailsDto> tripSheetList = null;
		// ArrayList<TripDetailsDto> tripSheetSaved =null;

		tripSheetList = new TripDetailsDao().viewTrackedTrip(tripId);
		return tripSheetList;
	}

	public ArrayList<TripDetailsDto> viewActualTrip(String tripId) {
		ArrayList<TripDetailsDto> tripSheetList = null;
		// ArrayList<TripDetailsDto> tripSheetSaved =null;

		tripSheetList = new TripDetailsDao().viewTrackedTrip(tripId);
		return tripSheetList;
	}

	public int setApproved(HashMap<String, TripDetailsDto> trips, String user) {
		int val = 0;
		if (trips != null && trips.size() > 0) {
			val = new TripDetailsDao().setApproved(trips, user);
		}
		return val;

	}

	public int disapproveTripsHavingNoEmployee(
			HashMap<String, TripDetailsDto> trips, String user) {
		int val = 0;
		if (trips != null && trips.size() > 0) {
			val = new TripDetailsDao().disapproveTripsHavingNoEmployee(trips,
					user);
		}
		return val;
	}

	public int transAdmin_ApproveTrackedTrips(String tripId[], String status,
			String[] comments, String doneBy) {
		String tripIds = "";
		int val = 0;
		if (tripId != null && tripId.length > 0) {

			val = new TripDetailsDao().transAdmin_ApproveTrackedTrips(tripId,
					status, comments, doneBy);

		}
		return val;
	}

	public boolean trackByVendor(HashMap<String, TripDetailsDto> trips,
			ArrayList<TripDetailsChildDto> checkedDtoList,
			ArrayList<TripDetailsChildDto> unCheckedDtoList,
			HashMap<String, String> NoTrips, String isApproval, String user) {

		boolean flag = false;
 
		try {
			if (trips != null && trips.size() > 0
					&& updateVendorTrip(trips, user) > 0) {
				   saveTripRateArgs(trips, user);
				System.out.println(" trip parents  updated ..");
				flag = true;
			}

			if (flag == true && checkedDtoList != null
					&& checkedDtoList.size() > 0
					&& trackTripByVendor(checkedDtoList) > 0) {
				System.out.println(" track children updated (approval)..");
				flag = true;

			}

			if (flag == true && unCheckedDtoList != null
					&& unCheckedDtoList.size() > 0
					&& unCheckTripByVendor(unCheckedDtoList) > 0) {
				System.out.println(" track children updated (disapproval)..");
				flag = true;
			}

		} catch (Exception e) {
			System.out.println("Ex : " + e);
			flag = false;
		}
		return flag;

	}

	private int deleteNoTrips(HashMap<String, String> noTrips) {

		return new TripDetailsDao().deleteNoTrips(noTrips);
	}


	public ArrayList<EmployeeDto> getemployeeContactNo(int tripid)
	{
	return new TripDetailsDao().getemployeeContactNo(tripid);
	}

	public ArrayList<TripDetailsDto> getTripToAssignVendor(String date,
			String log, String siteId, String time) {
		ArrayList<TripDetailsDto> dtos = new TripDetailsDao()
		.getTripSheetSavedNotVendorAssigned(date, log, siteId, time);		
		
		for (int i = 0; i < dtos.size(); i++) {

			if (dtos.get(i).getTrip_log().equals("IN")) {
				dtos.get(i).setTripDetailsChildDtoObj(
						dtos.get(i).getTripDetailsChildDtoList().get(0));
			} else {
				dtos.get(i).setTripDetailsChildDtoObj(
						dtos.get(i)
								.getTripDetailsChildDtoList()
								.get(dtos.get(i).getTripDetailsChildDtoList()
										.size() - 1));
			}
		}
		return dtos;

	}

	public ArrayList<TripDetailsDto> getTripSheetModified(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripDetailsDtos = new TripDetailsDao()
				.getTripSheetModified(tripDate, tripLog, siteId, tripTime);
		updateDistanceAndTime(tripDetailsDtos);
		return tripDetailsDtos;
	}

	public ArrayList<TripDetailsDto> getTripSheetSaved(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripDetailsDtos = new TripDetailsDao()
				.getTripSheetSaved(tripDate, tripLog, siteId, tripTime);
		updateDistanceAndTime(tripDetailsDtos);
		return tripDetailsDtos;
	}

	public ArrayList<TripDetailsDto> getTripSheetActual(String tripDate,
			String tripLog, String siteId, String tripTime) {
		ArrayList<TripDetailsDto> tripDetailsDtos = new TripDetailsDao()
				.getTripSheetActual(tripDate, tripLog, siteId, tripTime);
		updateDistanceAndTime(tripDetailsDtos);
		return tripDetailsDtos;
	}

	public void updateDistanceAndTime(ArrayList<TripDetailsDto> tripDetailsDtos) {

		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();

		String logTime;
		for (TripDetailsDto detailsDto : tripDetailsDtos) {
			try {
				cal.setTime(OtherFunctions.sqlFormatToDate(detailsDto
						.getTrip_date()));
				logTime = detailsDto.getTrip_time();
				int hr = Integer.parseInt(logTime.split(":")[0]);
				int mnt = Integer.parseInt(logTime.split(":")[1]);
				float distance = (float) 0.0;
				cal.add(Calendar.HOUR, hr);
				cal.add(Calendar.MINUTE, mnt);
				cal1.setTime(cal.getTime());			
				int minusTime = 0;
				for (int i = 0; i < detailsDto.getTripDetailsChildDtoList()
						.size(); i++) {

					distance += detailsDto.getTripDetailsChildDtoList().get(i)
							.getDistance();
					if (detailsDto.getTrip_log().equals("IN")) {
						if (detailsDto.getTripDetailsChildDtoList().get(i)
								.getTime().equals("0.0")) {
						} else {
							cal.setTime(cal1.getTime());
							cal.add(Calendar.MINUTE, -(int) Float.parseFloat(detailsDto
									.getTripDetailsChildDtoList().get(i)
									.getTime()));
						}
					} else
						cal.add(Calendar.MINUTE,
								(int) Float.parseFloat(detailsDto
										.getTripDetailsChildDtoList().get(i)
										.getTime()));

					detailsDto.getTripDetailsChildDtoList().get(i)
							.setDistance(distance);
					detailsDto
							.getTripDetailsChildDtoList()
							.get(i)
							.setTime(
									OtherFunctions.getTimePartFromDate(cal
											.getTime()));
				}

			} catch (Exception e) {
				System.out.println("error in changing time and dist" + e);
			}

		}

	}

	
	public void updateDistanceAndTime(TripDetailsDto detailsDto) {

		Calendar cal = Calendar.getInstance();
		Calendar cal1 = Calendar.getInstance();

		String logTime;		
			try {
				cal.setTime(OtherFunctions.sqlFormatToDate(detailsDto
						.getTrip_date()));
				logTime = detailsDto.getTrip_time();
				int hr = Integer.parseInt(logTime.split(":")[0]);
				int mnt = Integer.parseInt(logTime.split(":")[1]);
				float distance = (float) 0.0;
				cal.add(Calendar.HOUR, hr);
				cal.add(Calendar.MINUTE, mnt);
				cal1.setTime(cal.getTime());			
				int minusTime = 0;
				for (int i = 0; i < detailsDto.getTripDetailsChildDtoList()
						.size(); i++) {

					distance += detailsDto.getTripDetailsChildDtoList().get(i)
							.getDistance();
					if (detailsDto.getTrip_log().equals("IN")) {
						if (detailsDto.getTripDetailsChildDtoList().get(i)
								.getTime().equals("0.0")) {
						} else {
							cal.setTime(cal1.getTime());
							cal.add(Calendar.MINUTE, -(int) Float.parseFloat(detailsDto
									.getTripDetailsChildDtoList().get(i)
									.getTime()));
						}
					} else
						cal.add(Calendar.MINUTE,
								(int) Float.parseFloat(detailsDto
										.getTripDetailsChildDtoList().get(i)
										.getTime()));

					detailsDto.getTripDetailsChildDtoList().get(i)
							.setDistance(distance);
					detailsDto
							.getTripDetailsChildDtoList()
							.get(i)
							.setTime(
									OtherFunctions.getTimePartFromDate(cal
											.getTime()));
				}

			} catch (Exception e) {
				System.out.println("error in changing time and dist" + e);
			}
		

	}

	
	public ArrayList<TripDetailsDto> liveTripStatus(String tripDate,String projectId) {
		return new TripDetailsDao().liveTripStatus("1", tripDate,projectId);
	}

	public ArrayList<TripDetailsDto> liveTripStatusByTrips(String tripDate,
			String tripLog, String tripTime,String ProjectId) {
		return new TripDetailsDao().liveTripStatusByTrips("1", tripDate,
				tripTime, tripLog,ProjectId);

	}

	public ArrayList<TripDetailsChildDto> liveTripStatusByEmps(String tripId,String projectId) {
		return new TripDetailsDao().liveTripStatusByEmps(tripId,projectId);
	}

	public ArrayList<TripDetailsDto> getRoutingSummary(String siteId,
			String tripDate, String tripLog, String tripTime) {
		return new TripDetailsDao().getRoutingSummary(siteId, tripDate,
				tripLog, tripTime);
	}

	public String routingSummaryHTMLTable(String siteId, String tripDate,
			String tripLog, String tripTime,
			ArrayList<VehicleTypeDto> vehicleTypeGiven,int statusForDisplyStyleofVehicleCount) {
		String retData = "";
		String lastVehiclePart = "";
		int totalTripCount = 0;
		int totalEmpCount = 0;
		int totalSecTripCount=0;

		ArrayList<TripDetailsDto> dtos = getRoutingSummary(siteId, tripDate,
				tripLog, tripTime);
		int vehicletypeCounts = dtos.get(0).getVehicleTypes().size();
		try {
			retData = "<tr><th>&nbsp;</th><th>Date</th><th>IN/OUT</th><th>Time</th><th>Trip#</th><th>Sec_Trip#</th><th>Emp#</th>";
			String[] vehicleTypesUsed = new String[vehicletypeCounts];
			int i = 0;
			for (String vehicletype : dtos.get(0).getVehicleTypes()) {
				retData += "<th>" + vehicletype + "</th>";
				vehicleTypesUsed[i] = vehicletype;
				i++;
			}

			retData += "</tr>";
			for (TripDetailsDto detailsDto : dtos) {
				retData += "<tr><td>&nbsp;</td><td>" + OtherFunctions.changeDateFromatToddmmyyyy(detailsDto.getTrip_date())
						+ "</td><td>" + detailsDto.getTrip_log() + "</td><td>"
						+ detailsDto.getTrip_time() + "</td><td>"
						+ detailsDto.getTripCount() + "</td><td>"
						+ detailsDto.getSecCount() + "</td><td>"
						+ detailsDto.getEmpInCount() + "</td>";
				totalTripCount += Integer.parseInt(detailsDto.getTripCount());
				totalSecTripCount += detailsDto.getSecCount();
				totalEmpCount += detailsDto.getEmpInCount();

				for (i = 0; i < detailsDto.getVehicles().size(); i++) {
					for (VehicleTypeDto dto1 : vehicleTypeGiven) {
						if (dto1.getId() == detailsDto.getVehicles().get(i)
								.getId()) {
							dto1.setUsedCount(dto1.getUsedCount()
									+ detailsDto.getVehicles().get(i)
											.getCount());
							dto1.setCount(dto1.getCount()
									+ detailsDto.getVehicles().get(i)
											.getCount());

						}
					}
				}
				for (int j = 0; j < vehicletypeCounts; j++) {
					boolean flag = false;
					for (VehicleTypeDto typeDto : detailsDto.getVehicles()) {
						if (vehicleTypesUsed[j].equalsIgnoreCase(typeDto
								.getType())) {
							retData += "<td>" + typeDto.getCount() + "</td>";
							flag = true;

						}
					}
					if (!flag) {

						retData += "<td>&nbsp;</td>";
					}
				}
			}

			for (String vehicletype : dtos.get(0).getVehicleTypes()) {
				for (VehicleTypeDto vehicleDto : vehicleTypeGiven) {
					if (vehicletype.equalsIgnoreCase(vehicleDto.getType())) {
						if(statusForDisplyStyleofVehicleCount==1){
						lastVehiclePart += "<td>"+vehicleDto.getUsedCount()+" used out of " + vehicleDto.getCount()+" </td>" ;
						}
						else
						{
							lastVehiclePart += "<td>" +vehicleDto.getUsedCount();	
						}
					}

				}
			}
			retData += "</tr><tr><td>Total</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td><td>"
					+ totalTripCount
					+ "</td><td>"
					+ totalSecTripCount
					+ "</td><td>"
					+ totalEmpCount
					+ "</td>"
					+ lastVehiclePart;

			retData += "</tr>";

		} catch (Exception e) {
			System.out.println("error in service" + e);
		}
		return retData;
	}
	
	
public void saveTripRateArgs (HashMap<String, TripDetailsDto> trips,
			
			String user) throws SQLException {
		
	
			Set <String> keys =trips.keySet();
			for(String key : keys) {
				
				try{
				TripDetailsDto localTrip = trips.get(key);
				TripDetailsDto proxyTrip = new TripDetailsDao().getTrackedTripSheetByTripId(key);
				 
				
				proxyTrip.setTripBasedDistance(localTrip.getTripBasedDistance());
				   System.out.println("VEHICLE TYPE OF THE TRIP : " + proxyTrip.getVehicle());
					VendorDto vendorDto = new TripDetailsDao().getVendorFromTripId(key);
				List<BillingProcess> bpList= BillingProcessFactory.getProcessInstances(proxyTrip, String.valueOf(TransportTypeConfig.GENERAL_SHIFT),proxyTrip.getVehicle(), vendorDto );
				 
				System.out.println("_+++++++++++++++++++++++-___");
				Map<String, String> tot = new HashMap<String, String>(); 
				for(BillingProcess bp: bpList ) {
					Map<String, String> params = bp.calculateValues(proxyTrip);
					tot.putAll( params);
					
				}
				billingProcessDao.save(proxyTrip, tot, "td_billing_args");

				}catch(Exception e) {
					System.out.println("Error in report gen : "+e);
					System.out.println("SKipped the report gen of " + key);
				}
			}
	 
		
	}

public void saveTrip(String tripDate, String tripLog, String siteId,
		String tripTime, String doneBy,String tripids) {
new TripDetailsDao().saveTrip(tripDate, tripLog, siteId, tripTime, doneBy,tripids);
	
}

public TripDetailsDto getTripRate(String tripId)
{
	return new TripDetailsDao().getTripAmount(tripId);
}

public int  updateTripAmount(String tripId,Double amount)
{
	return new TripDetailsDao().updateTripAmount(tripId, amount);
}
public ArrayList<TripDetailsDto> getTripSummary(String employeeId) {
	return new TripDetailsDao().getTripSummary(employeeId);
}




public ArrayList<TripDetailsDto> getAdhocTripSheetSaved(String tripDate, String siteId) {
	return new TripDetailsDao().getAdhocTripSheetSaved(tripDate, siteId);
}

}
