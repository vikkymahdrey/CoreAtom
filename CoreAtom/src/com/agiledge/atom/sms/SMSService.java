package com.agiledge.atom.sms;

import java.util.ArrayList;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.DriverDAO;
import com.agiledge.atom.dao.EscalationMatrixDao;
import com.agiledge.atom.dao.EscortDao;
import com.agiledge.atom.dao.TripDetailsDao;
import com.agiledge.atom.dao.VehicleDao;
import com.agiledge.atom.dto.DriverDto;
import com.agiledge.atom.dto.EmergencyDto;
import com.agiledge.atom.dto.EscalationMatrixDto;
import com.agiledge.atom.dto.EscortDto;
import com.agiledge.atom.dto.TripDetailsChildDto;
import com.agiledge.atom.dto.TripDetailsDto;
import com.agiledge.atom.dto.VehicleDto;
import com.agiledge.atom.service.TripDetailsService;

public class SMSService {
	public int sendSMSOnPanic(String imei, String time) {
		ArrayList<EscalationMatrixDto> escalationMatrixDtos = new EscalationMatrixDao()
				.getEscalationContactWithTimeSlot();
		String[] contacts = new String[escalationMatrixDtos.size()];
		String[] timeSlots = new String[escalationMatrixDtos.size()];
		String[] emails = new String[escalationMatrixDtos.size()];
		VehicleDto vehicleDto = new VehicleDao().getPanicAlarmVehicle(imei);
		String message = "Panic Alarm has been pressed in the vehicle No "
				+ vehicleDto.getVehicleNo() + "at " + time
				+ " Please take immediate action";

		int i = 0;
		for (EscalationMatrixDto dto : escalationMatrixDtos) {
			contacts[i] = dto.getContact();
			timeSlots[i] = dto.getTimeSlot();
			emails[i] = dto.getEmail();
			i++;
		}
		System.out.println("here");

		new PanicSMS().sendRepeated(contacts, emails, message, timeSlots,
				vehicleDto.getTripId());
		System.out.println("One more");
		return 1;
	}

	public int sendPinSMS(TripDetailsDto tripDto) {
		String mobileNos[] = null;
		String messages[] = null;

		try {
			ArrayList<TripDetailsChildDto> tripDtos = tripDto
					.getTripDetailsChildDtoList();
			mobileNos = new String[tripDtos.size()];
			messages = new String[tripDtos.size()];
			String inOut = tripDto.getTrip_log();
			String[] cabNo = tripDto.getVehicleNo().trim().split(" ");
			tripDto.setVehicleNo(cabNo[cabNo.length - 1]);
			for (int i = 0; i < tripDtos.size(); i++) {
				mobileNos[i] = tripDtos.get(i).getContactNumber();
				messages[i] = getMessages
						.getPinSMS(tripDtos.get(i).getEmployeeName(), tripDtos
								.get(i).getPersonnelNo(), OtherFunctions
								.changeDateFromat(tripDto.getTrip_date()),
								tripDto.getVehicleNo(),
								tripDto.getDriverName(), tripDto
										.getDriverContact(), tripDtos.get(i)
										.getTime(),
								tripDtos.get(i).getKeyPin(), inOut, tripDto
										.getTrip_time());
			}

		} catch (Exception e) {
			System.out.println("error in setting mobno and message" + e);
		}
		SendSMS sendSMS = SendSMSFactory.getSMSInstance();
		sendSMS.send(mobileNos, messages);
		return 1;
	}

	/*
	 * Send Generated pin to Drivers
	 */
	public int sendPasswordToDriver(String tripId) {
		int retval = 0;
		try {

			TripDetailsDto tripDto = new TripDetailsDao()
					.getTripSheetByTrip(tripId);
			tripDto.setTrip_date(OtherFunctions.changeDateFromat(tripDto
					.getTrip_date()));
			DriverDto driverDto = new DriverDAO().getDriver(tripDto
					.getDriverId());
			String mobileNo = null;
			String message = null;
			String firstPickUpTime = OtherFunctions.addTime(
					tripDto.getTrip_time(), tripDto
							.getTripDetailsChildDtoList().get(0).getTime());
			String time = OtherFunctions.changeTimeFormat(
					tripDto.getTrip_time(), "HH:mm", "hh:mm a");

			int i = 0;
			if (driverDto != null) {
				mobileNo = driverDto.getContactNo() == null ? "" : driverDto
						.getContactNo();
				/*
				 * message = String .format(
				 * "Hi %s\n----------\nTrip: %s\nTime: %s %s\nUserName: %s\nPassword: %s\nVehicle: %s\nVehicle No: %s\n--------\nATOm.. "
				 * , driverDto.getDriverName(), tripDto.getTrip_code(),
				 * tripDto.getTrip_time(), tripDto.getTrip_log(),
				 * driverDto.getUsername(), tripDto.getDriverPassword(),
				 * tripDto.getVehicle_type(), tripDto.getVehicleNo());
				 */
				// message
				// ="TripID:"+tripDto.getTrip_code()+".Time:"+tripDto.getTrip_log()+"-"+time+" UserName:"+driverDto.getUsername()+" PIN:"+tripDto.getDriverPassword()+". Regds, Transport Team";
				message = getMessages.getDriverSMS(tripDto.getTrip_date(),
						tripDto.getTrip_log(), time, firstPickUpTime,
						driverDto.getUsername(), tripDto.getDriverPassword(),
						tripDto.getIsSecurity(), tripDto.getTrip_code());
				i++;
			}
			SendSMS sendSMS = SendSMSFactory.getSMSInstance();
			sendSMS.send(mobileNo, message);
			retval = 1;
		} catch (Exception e) {
			System.out.println(" Error in SmsService sendPasswordToDriver");
			retval = 0;
		}
		return retval;

	}

	/*
	 * 
	 */
	public int sendPasswordToEscort(String tripId, String password) {
		int retval = 0;
		try {

			TripDetailsDto tripDto = new TripDetailsDao()
					.getTripSheetByTrip(tripId);

			EscortDto escortDto = new EscortDao().getEscortById(tripDto
					.getEscortId());
			String mobileNo = null;
			String message = null;
			int i = 0;
			if (escortDto != null) {
				mobileNo = escortDto.getPhone() == null ? "" : escortDto
						.getPhone();
				/*
				 * message = String .format(
				 * "Hi %s\n----------\nTrip: %s\nTime: %s %s\nUserName: %s\nPassword: %s\nVehicle: %s\nVehicle No: %s\n--------\nATOm.. "
				 * , driverDto.getDriverName(), tripDto.getTrip_code(),
				 * tripDto.getTrip_time(), tripDto.getTrip_log(),
				 * driverDto.getUsername(), tripDto.getDriverPassword(),
				 * tripDto.getVehicle_type(), tripDto.getVehicleNo());
				 */
				message = getMessages
						.getEscortMesssage(tripDto.getTrip_code(),
								tripDto.getTrip_log(), tripDto.getTrip_time(),
								tripDto.getVehicleNo(),
								tripDto.getDriverName(),
								tripDto.getDriverContact(),
								tripDto.getEscortPassword());
				i++;
			}
			System.out.println("here");

			SendSMS sendSMS = SendSMSFactory.getSMSInstance();
			sendSMS.send(mobileNo, message);
			retval = 1;
		} catch (Exception e) {
			System.out.println(" Error in SmsService sendPasswordToDriver");
			retval = 0;
		}
		return retval;

	}

	/*
	 * this function is same but for ajax called servelt and returns message
	 * sending status
	 */
	public String sendPasswordToDriverGetAck(String tripId) {
		int retval = 0;
		String smsStatus = "";
		try {

			TripDetailsDto tripDto = new TripDetailsDao()
					.getTripSheetByTrip(tripId);

			DriverDto driverDto = new DriverDAO().getDriver(tripDto
					.getDriverId());

			String time = OtherFunctions.changeTimeFormat(
					tripDto.getTrip_time(), "HH:mm", "hh:mm a");
			String firstPickUpTime = OtherFunctions.addTime(
					tripDto.getTrip_time(), tripDto
							.getTripDetailsChildDtoList().get(0).getTime());
			String mobileNo = null;
			String message = null;
			int i = 0;
			if (driverDto != null) {

				mobileNo = driverDto.getContactNo() == null ? "" : driverDto
						.getContactNo();
				message = getMessages.getDriverSMS(tripDto.getTrip_date(),
						tripDto.getTrip_log(), time, firstPickUpTime,
						driverDto.getUsername(), tripDto.getDriverPassword(),
						tripDto.getIsSecurity(), tripDto.getTrip_code());

				i++;
			}
			SendSMS sendSMS = SendSMSFactory.getSMSInstance();
			sendSMS.send(mobileNo, message);
			smsStatus = "Sent success (UAT-Sms Disabled)";
		} catch (Exception e) {
			System.out.println(" Error in SmsService sendPasswordToDriver");
			retval = 0;
		}
		return smsStatus;

	}

	/*
	 * send sms to escort
	 */
	public String sendPasswordToEscortGetAck(String tripId) {
		int retval = 0;
		String smsStatus = "";
		try {

			TripDetailsDto tripDto = new TripDetailsDao()
					.getTripSheetByTrip(tripId);

			EscortDto escortDto = new EscortDao().getEscortByTripId(tripId);
			String mobileNo = null;
			String message = null;
			int i = 0;
			if (escortDto != null) {
				mobileNo = escortDto.getPhone() == null ? "" : escortDto
						.getPhone();
				message = getMessages
						.getEscortMesssage(tripDto.getTrip_code(),
								tripDto.getTrip_log(), tripDto.getTrip_time(),
								tripDto.getVehicleNo(),
								tripDto.getDriverName(),
								tripDto.getDriverContact(),
								tripDto.getEscortPassword());
				i++;
			}
			System.out.println("here");
			SendSMS sendSMS = SendSMSFactory.getSMSInstance();
			sendSMS.send(mobileNo, message);

		} catch (Exception e) {
			System.out.println(" Error in SmsService sendPasswordToEscort");
			retval = 0;
		}
		return smsStatus;

	}

	public void sendSMSMessage(String ShiftTime, String pickUpTime,
			String vehicleNo, String toContactNo, String driverContact,
			String siteId) {
		String message = "Dear Member,Pls note your Cab details for "
				+ ShiftTime + " ,pick up time : " + pickUpTime + " veh#"
				+ vehicleNo + " contact# " + driverContact;
		message += OtherFunctions.siteWiseSMSContent(siteId);
		SendSMS sendSMS = SendSMSFactory.getSMSInstance();
		sendSMS.send(toContactNo, message);
	}

	public String sendPinSMSForTrip(String tripId) {
		TripDetailsDto dto = new TripDetailsDto();

		String inOut = dto.getTrip_log();
		String[] mobileNos = null;
		String[] messages = null;
		try {
			dto = new TripDetailsService().getTripSheetByTrip(tripId);
			ArrayList<TripDetailsChildDto> tripDtos = dto
					.getTripDetailsChildDtoList();
			mobileNos = new String[tripDtos.size()];
			messages = new String[tripDtos.size()];
			for (int i = 0; i < tripDtos.size(); i++) {
				mobileNos[i] = tripDtos.get(i).getContactNumber();
				messages[i] = getMessages.getPinSMS(tripDtos.get(i)
						.getEmployeeName(), tripDtos.get(i).getEmployeeId(),
						OtherFunctions.changeDateFromat(dto.getTrip_date()),
						dto.getVehicleNo(), dto.getDriverName(), dto
								.getDriverContact(), tripDtos.get(i)
								.getPickUpTime(), tripDtos.get(i).getKeyPin(),
						inOut, dto.getTrip_time());
				/*
				 * if (inOut.equalsIgnoreCase("IN")) { messages[i] =
				 * "Cab Details: No:"
				 * +dto.getVehicleNo()+", Driver name "+dto.getDriverName
				 * ()+", Driver conctact No "
				 * +dto.getDriverContact()+" Driver Ctc . Pick-up time:"
				 * +tripDtos.get(i).getTime()+
				 * ", Authentication PIN UrEmpId . Regds, Transport Team"; }
				 * else { messages[i] =
				 * "Cab Details: No:"+dto.getVehicleNo()+", Driver name "
				 * +dto.getDriverName
				 * ()+", Driver conctact No "+dto.getDriverContact
				 * ()+" Driver Ctc . Pick-up time:"+dto.getTrip_time()+
				 * ", Authentication PIN UrEmpId. Regds, Transport Team"; }
				 */
			}
		} catch (Exception e) {
			System.out.println("error in setting mobno and message");
		}
		SendSMS sendSMS = SendSMSFactory.getSMSInstance();
		sendSMS.send(mobileNos, messages);
		return "";

	}

	public void SendEmergencyTransportSMS(EmergencyDto dto, String driverpswd,
			String empPin) {
		String[] contact = new String[2];
		contact = new SMSDao().getMobileNosbyIds(dto.getDriverId(),
				dto.getBookingFor());
		String drivercontact = contact[0];
		String employeecontact = contact[1];

		SendSMS dSMS = SendSMSFactory.getSMSInstance();
		SendSMS eSMS = SendSMSFactory.getSMSInstance();
		String message = getMessages.getEmergencyDriverSMS(dto, driverpswd);
		String message1 = getMessages.getEmergencyEmployeeSMS(dto,
				drivercontact, empPin);
		dSMS.send(drivercontact, message);
		eSMS.send(employeecontact, message1);
	}
	

	public void sendOTPEmployeeMessage(String MobileNo, String OTP) {
		String message = "Dear Member,Your One Time Password(OTP) Is "
				+ OTP
				+ ".Please Use This OTP To Login To ATOm Android Application- Thanks ATOm Team";
		SendSMS sendSMS = SendSMSFactory.getSMSInstance();
		sendSMS.send(MobileNo, message);
	}

	public int sendSMSOnPanicEmployee(String empid, String tripid, String imei) {
		ArrayList<EscalationMatrixDto> escalationMatrixDtos = new EscalationMatrixDao()
				.getEscalationContactWithTimeSlot();
		String[] contacts = new String[escalationMatrixDtos.size()];
		String[] timeSlots = new String[escalationMatrixDtos.size()];
		String[] emails = new String[escalationMatrixDtos.size()];
		String message = new SMSDao().getEmployeePanicMsg(empid, tripid);

		int i = 0;
		for (EscalationMatrixDto dto : escalationMatrixDtos) {
			contacts[i] = dto.getContact();
			timeSlots[i] = dto.getTimeSlot();
			emails[i] = dto.getEmail();
			i++;
		}
		System.out.println("here");

		new PanicForEmployee().sendRepeated(contacts, emails, message,
				timeSlots, tripid, imei, empid);
		System.out.println("One more");
		return 1;
	}
	//keonics
	public int sendSMSOnPanicForShuttle(String regNo, String time, String deviceNo) {
		ArrayList<EscalationMatrixDto> escalationMatrixDtos = new EscalationMatrixDao()
				.getEscalationContactWithTimeSlot();
		String[] contacts = new String[escalationMatrixDtos.size()];
		String[] timeSlots = new String[escalationMatrixDtos.size()];
		String[] emails = new String[escalationMatrixDtos.size()];
		String message = "Panic Alarm has been pressed in the vehicle No "
				+ regNo + "at " + time
				+ " Please take immediate action";
		int i = 0;
		for (EscalationMatrixDto dto : escalationMatrixDtos) {
			contacts[i] = dto.getContact();
			timeSlots[i] = dto.getTimeSlot();
			emails[i] = dto.getEmail();
			i++;
		}
		//String tripid=SettingsConstant.deviceMap.get(deviceNo);
		//new PanicSMS().sendRepeated(contacts, emails, message, timeSlots,tripid);
		return 1;
	}

	
	public void sendTripStartSMS(String tripid) {
		
	SMSDao sd= new SMSDao();
	sd.sendTripStartSMS(tripid);
	
 
	}
	
	
	
	
	public String[] sendSMSOnPanicEmployeeIVR(String empid, String tripid) {
		ArrayList<EscalationMatrixDto> escalationMatrixDtos = new EscalationMatrixDao()
				.getEscalationContactWithTimeSlot();
		String[] contacts = new String[escalationMatrixDtos.size()];
		String[] timeSlots = new String[escalationMatrixDtos.size()];
		String[] emails = new String[escalationMatrixDtos.size()];
		String message = new SMSDao().getEmployeePanicMsg(empid, tripid);

		int i = 0;
		for (EscalationMatrixDto dto : escalationMatrixDtos) {
			contacts[i] = dto.getContact();
			timeSlots[i] = dto.getTimeSlot();
			emails[i] = dto.getEmail();
			i++;
		}
		System.out.println("here");

		System.out.println("One more");
		return contacts;
	}

	
	
	
	
	
	
	
}
