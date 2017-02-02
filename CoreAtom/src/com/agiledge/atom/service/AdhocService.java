package com.agiledge.atom.service;

import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.AdhocDao;
import com.agiledge.atom.dto.AdhocDto;
import com.agiledge.atom.dto.EmployeeDto;

public class AdhocService {
	private String approveRight = "no";

	public int insertSetup(AdhocDto dto) {
		return new AdhocDao().insertSetup(dto);
	}

	public int[] getAdhocNotificationCount(String employeeId, String roleId,String role) {
		return new AdhocDao().getAdhocNotificationCount(employeeId,roleId,role);
	}
	
	public AdhocDto getSetupDetails(String adhocType) {
		AdhocDao dao = new AdhocDao();
		AdhocDto dto = dao.getAdhoctypeDetails(adhocType);
		return dto;
	}

	public ArrayList<AdhocDto> getSetupDetails(String adhocType, String siteId,
			String projectUnit) {
        AdhocDao dao = new AdhocDao();
		ArrayList<AdhocDto> adhcocdetailslist = null;
		adhcocdetailslist = dao.getAdhoctypeDetails(null, adhocType, siteId,
				projectUnit);
		return adhcocdetailslist;
	}

	public int updateSetup(AdhocDto dto) {
		return new AdhocDao().updateSetup(dto);
	}

	public ArrayList<AdhocDto> getAdhocTypes() {
		return new AdhocDao().getAdhocTypes();
	}

	public ArrayList<AdhocDto> getAdhocTypes(String site) {
		return new AdhocDao().getAdhocTypes(site);
	}

	public String bookAdhocTravel(AdhocDto dto) {

		String retVal = "";
		try {
			dto.setTravelDate(OtherFunctions.changeDateFromatToIso(dto
					.getTravelDate()));
			AdhocDao dao = new AdhocDao();
		//	AdhocDto adto = new AdhocDto();
			String flag ="";
			ArrayList<AdhocDto> adhocdetails = new ArrayList<AdhocDto>();
			adhocdetails=	dao.getAdhoctypeDetails(dto, dto.getAdhocType(), dto.getSiteId(),
					dto.getProjectUnit());
			if(adhocdetails != null)
			{
				System.out.println("not null");
			}
			else
			{
				System.out.println("null");
			}
			if(dto.getPickupDrop() != null && dto.getPickupDrop().equalsIgnoreCase("OUT"))
			{
				 flag = "pick up";
			}
			else
			{
				 flag = "drop";
			}
			for(AdhocDto adhocdto : adhocdetails)
			{
				System.out.println(adhocdto.getAdhocType() + " " + dto.getAdhocType() + " "+ dto.getBookedBy() + " " +dto.getPickupDrop() + " "+ adhocdto.getPickupDrop());
				if(dto.getAdhocType().equalsIgnoreCase(SettingsConstant.SHIFT_EXTENSTION)&& adhocdto.getPickupDrop().equalsIgnoreCase(flag))
				{
					
					//adto=adhocdto;
					dto.setId(adhocdto.getId());
					//dto.setAdhocType(rs.getString("type"));
					//dto.setSiteId(rs.getString("site"));
					//dto.setProjectUnit(rs.getString("projectUnit"));
					dto.setRequester(adhocdto.getRequester());
					dto.setApproval(adhocdto.getApproval());
					dto.setRequestCutoff(adhocdto.getRequestCutoff());
					dto.setCancelCutoff(adhocdto.getCancelCutoff());
					dto.setCancelMode(adhocdto.getCancelMode());
					dto.setExistingCancelTime(adhocdto.getExistingCancelTime());
					dto.setMaxRequest(adhocdto.getMaxRequest());
					dto.setMaxPendingRequest(adhocdto.getMaxPendingRequest());
					
					/*dto.setPickupDrop(rs.getString("pickDrop"));*/
					dao.getValidateDetails(dto);
					//System.out.println("In lloop"+adhocdto.getApproval()+"  "+adhocdto.getMaxPendingRequest()+" "+adhocdto.getPendingBookedCount()+" "+adhocdto.getMaxRequest()+" "+adhocdto.getBookedDayBookingCount());
					
				}
				else 
				{
					
					//adto=adhocdto;
					dto.setId(adhocdto.getId());
					//dto.setAdhocType(rs.getString("type"));
					//dto.setSiteId(rs.getString("site"));
					//dto.setProjectUnit(rs.getString("projectUnit"));
					dto.setRequester(adhocdto.getRequester());
					dto.setApproval(adhocdto.getApproval());
					dto.setRequestCutoff(adhocdto.getRequestCutoff());
					dto.setCancelCutoff(adhocdto.getCancelCutoff());
					dto.setCancelMode(adhocdto.getCancelMode());
					dto.setExistingCancelTime(adhocdto.getExistingCancelTime());
					dto.setMaxRequest(adhocdto.getMaxRequest());
					dto.setMaxPendingRequest(adhocdto.getMaxPendingRequest());
					
					/*dto.setPickupDrop(rs.getString("pickDrop"));*/
					dao.getValidateDetails(dto);
				}
			
			}
			
			
			 //System.out.println("In adhoc booking servicce "+dto.getApproval()+"  "+dto.getMaxPendingRequest()+" "+dto.getPendingBookedCount()+" "+dto.getMaxRequest()+" "+dto.getBookedDayBookingCount());

			if (dto.getApproval().equalsIgnoreCase("yes")
					&& Integer.parseInt(dto.getMaxPendingRequest()) <= Integer
							.parseInt(dto.getPendingBookedCount())) {
				retVal = "<div class=\"failure\">Booking Failed!You have Maximum of non-approved Request</div>";
			} else if (dto.getMaxRequest()!=null &&Integer.parseInt(dto.getMaxRequest()) <= Integer
					.parseInt(dto.getBookedDayBookingCount())) {
				retVal = "<div class=\"failure\"> Booking Failed!You have Maximum Request for the Day</div>";
			}
			else 
			{
				int status=dao.bookAdhocTravel(dto);			
				if (status > 0) {
					new MailService().sendmailForAdhocBooking(dto);
				retVal = "<div class=\"success\" >Booking Successful</div>";
			} else if(status==-100) {
				retVal = "<div class=\"failure\">Booking Failed! No active Schedule/Subscription </div>";
			} else {
				retVal = "<div class=\"failure\">Booking Failed</div>";
			}
			}
		} catch (Exception e) {

			System.out.println(retVal + "  :Error in Service" + e);
		}
		return retVal;
	}

	public AdhocDto getBookingDetails(int bookingId) {
		return new AdhocDao().getBookingDetails(bookingId);
	}

	public ArrayList<AdhocDto> getAllBookingDetails(String empId) {
		return new AdhocDao().getAllBookingDetails(empId);
	}

	public ArrayList<AdhocDto> getBookingDetailsForManager(String managerId,
			String roleId) {
		return new AdhocDao().getBookingDetailsForManager(managerId, roleId);
	}
	public ArrayList<AdhocDto> getBookingDetailsForTransportManager() {
		return new AdhocDao().getBookingDetailsForTransportManager();
	}

	public int approveAdhocBooking(String adhocId, String approverId,
			String update) {
		
		int status = new AdhocDao().approveAdhocBooking(adhocId, approverId, update);
		try{
			
		
			if (status > 0) {
				
				new MailService().sendmailForAdhocBookingApproval(adhocId,approverId,update);
				
				
			} else {
				System.out.println("failure");
			}
		} 
		
		catch(Exception e) {

			System.out.println("Error in Service" + e);
		}
		
		return status;
	}

	public int modifyAdhocTravel(AdhocDto dto) {
					
			int status = new AdhocDao().modifyAdhocTravel(dto);
			
			try{
				
				
				if (status > 0) {
					
					new MailService().sendmailForAdhocBookingModification(dto);
					
					
				} else {
					System.out.println("failure");
				}
			} 
			
			catch(Exception e) {

				System.out.println("Error in Service" + e);
			}
			
			return status;
		
	}

	public String isAdhocBookingright(String empId, String adhocType,
			String bookingFor, String site, String projectUnit,String roleId) {
		{

			return new AdhocDao().isAdhocBookingright(empId, adhocType,
					bookingFor, site, projectUnit,roleId);
		}
	}

	public AdhocDto getBookingDetails(String bookingId) {
		return new AdhocDao().getBookingDetails(bookingId);
	}

	public boolean checkIsRightToModify(String empId, String adhocType,
			String bookingFor, String site, String projectUnit,String roleId) {
		boolean flag = false;
		String isRight = new AdhocDao().isAdhocBookingright(empId, adhocType,
				bookingFor, site, projectUnit,roleId);
		if (isRight.equalsIgnoreCase("self") || isRight.equalsIgnoreCase("all")) {
			flag = true;
		}
		return flag;
	}

	public boolean checkIsRightToApprove(String empId, String adhocType,
			String bookingFor, String site, String projectUnit,String roleId) {
		boolean flag = false;
		if (new AdhocDao().isApproveRight(empId, adhocType, bookingFor, site,
				projectUnit,roleId))
			flag = true;
		return flag;

	}

	public int cancelAdhocBooking(String adhocId, String isShiftExt,
			String alterId, String user) {
		int status = new AdhocDao().cancelAdhocBooking(adhocId, isShiftExt, alterId);
		try{
			
			
			if (status > 0) {
				
				new MailService().sendmailForAdhocBookingCancellation(adhocId, isShiftExt, alterId,user);
				
				
			} else {
				System.out.println("failure");
			}
		} 
		
		catch(Exception e) {

			System.out.println("Error in Service" + e);
		}
		
		return status;
	}

	public String validateAdhocBooking(AdhocDto dto) {
		return "";
	}

	public String bookAdhocTravel2(AdhocDto dto) {
		int result=0;
		String retVal = null;
		 result=new AdhocDao().bookAdhocTravel2(dto);
		 if(result>0)
		 {
			 retVal = "<div class=\"success\">Booked Successfully</div>";
		 }
		 else
		 {
			 retVal = "<div class=\"failure\">Booking Failed!</div>"; 
		 }
		return retVal;
	}
	public ArrayList<AdhocDto> getAdhocTrips(String site)
	{
	return new AdhocDao().getAdhocTrips(site);
	}
	
	public ArrayList<EmployeeDto> getAdhocPassengers(String bookingid)
	{
		return new AdhocDao().getAdhocPassengers(bookingid);
	}
	
	public int vendorAssign(String id[], String vendorid) 
	{
		return new AdhocDao().vendorAssign(id, vendorid);
	}
}
