package com.agiledge.atom.sms;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dto.EmergencyDto;

public class getMessages {
	static String messageFactoryVal = "";
	static String authType = "";
	
	static {
		messageFactoryVal = SendSMSFactory.smsFlag;
		authType=SettingsConstant.empAuthinticationForComet;
	}

	
	public static String getPinSMS( String empName,String empCode,
			String tripDate, String vehicleNo, String driverName,
			String driverContact, String pickUpTime, String pin,
			String inOrOut, String tripTime) {
		
		if(authType.equalsIgnoreCase("empId"))
		{
		pin="UrEmpid";	
		}		
		String message = "";
		if (messageFactoryVal.equalsIgnoreCase("gss")) {
			if(inOrOut.equalsIgnoreCase("IN"))
			{
			message = "Dear "+ empName +",Pickup details for "+OtherFunctions.changeDateFromat(tripDate)+" . Cab:"+ vehicleNo+" Driver AT("+driverContact+"), pickup Time:" + pickUpTime + " . PIN # "+pin;
			}
			else
			{
				message = "Dear "+ empName +",Drop details for "+OtherFunctions.changeDateFromat(tripDate)+" . Cab:"+ vehicleNo+" Driver AT("+driverContact+"), Departure Time:" + tripTime + " . PIN # "+pin;	
			}
		
		}
		else if(messageFactoryVal.equalsIgnoreCase("cd"))
		{
			if(inOrOut.equalsIgnoreCase("IN"))
			{
			message=	"Date: "+tripDate+", Login time "+tripTime+" Cab #:"+vehicleNo+", Driver name "+driverName+", Driver mob # "+driverContact+" . Pick-up time +/- 10 min  from "+pickUpTime+", PIN # "+pin+". Regds, Transport Team. Helpline #s 7760995236 or 08040343496";
			//message = "Date:"+vehicleNo+", Driver name "+driverName+" Driver Ctc "+driverContact+". Pick-up time:, Authentication PIN "+pin+". Regds, Transport Team";		
			}
			else
			{				
				message="Date:"+tripDate+", Logout time "+tripTime+" Cab #: "+vehicleNo+",Driver name "+driverName+", Driver mob # "+driverContact+", Departure time:"+tripTime+", PIN # "+pin+". Regds, Transport Team. Helpline #s 7760995236 or 08040343496";
			//message = "Cab Details: No:"+vehicleNo+", Driver name "+driverName+",Driver Ctc "+driverContact+". Pick-up time:"+tripTime+", Authentication PIN "+pin+". Regds, Transport Team";	
			}
		}
		else if(messageFactoryVal.equalsIgnoreCase("demo"))
		{
			
			if(inOrOut.equalsIgnoreCase("IN"))
			{
				message="Date:"+tripDate+", Login time "+tripTime+", Cab #:"+vehicleNo+", Driver name: "+driverName+", Pick-up "+pickUpTime+", PIN#"+pin+". Regds, Transport Team.";
			//message = "Transport Details:for "+tripTime+"-IN Log-in is, Driver name ,driver ctc "+driverContact +". Pick-up time:"+pickUpTime+"Your authentication PIN is "+pin+". Regds, Transport Team";		
			}
			else
			{
				message = "Date:"+tripDate+", Logout time"+tripTime+", Cab #:"+vehicleNo+", Driver name: "+driverName+", Departure time "+pickUpTime+", PIN#"+pin+". Regds, Transport Team.";
				//message = "Transport Details:for "+tripTime+"-OUT Log-in is, Driver name ,driver ctc "+driverContact +". Pick-up time:"+tripTime+"Your authentication PIN is "+pin+". Regds, Transport Team";	
			}
		}
		else if(messageFactoryVal.equalsIgnoreCase("cdt"))
		{
			//if(inOrOut.equalsIgnoreCase("IN"))
			//{
			message=	"Cab Details for Xite -  Veh No:"+vehicleNo+", Driver name "+driverName+", Driver conctact No "+driverContact+" Driver will remain same for both pickup & Drop. Request you to be ready at least one hour in advance. For any emergency kindly contact Pradeep @ 9740255755 Regds, Transport Team – 7760995236, 9880116303, 9880846040, and 7760995237.";
			//message = "Date:"+vehicleNo+", Driver name "+driverName+" Driver Ctc "+driverContact+". Pick-up time:, Authentication PIN "+pin+". Regds, Transport Team";		
			//}
			//else
			//{				
			//	message=	"Cab Details for Xite -  Veh No:"+vehicleNo+",”, Driver name "+driverName+", Driver conctact No "+driverContact+" Driver will remain same for both pickup & Drop. Request you to be ready at least one hour in advance. For any emergency kindly contact Pradeep @ 9740255755 Regds, Transport Team – 7760995236, 9880116303, 9880846040, and 7760995237.";
			//message = "Cab Details: No:"+vehicleNo+", Driver name "+driverName+",Driver Ctc "+driverContact+". Pick-up time:"+tripTime+", Authentication PIN "+pin+". Regds, Transport Team";	
			//}
		}
		System.out.println("In get messagessssssssssssssssssssss"+message);
		return message;
	}
	public static String getDriverSMS(String tripdate,String InOut, String tripTime, String firstPickUpTime, String DriverUsername, String driverPassword, String isEscort,String tripcode)
	{
		String message="";
		if(messageFactoryVal.equalsIgnoreCase("cd")||messageFactoryVal.equalsIgnoreCase("cdt"))
		{
		if(InOut.equalsIgnoreCase("IN"))
		{	
		message ="Date:"+tripdate+",T.Code:"+tripcode+", Login time "+tripTime+", First point time "+firstPickUpTime+", PIN # "+driverPassword+", Escort "+isEscort+". Regds, Transport Team";
		}
		else
		{
			message= "Date:"+tripdate+",T.Code:"+tripcode+", Logout time "+tripTime+", PIN # "+driverPassword+", Escort "+isEscort+". Regds, Transport Team";
		}
		System.out.println(message);
		}
		else if(messageFactoryVal.equalsIgnoreCase("demo"))
		{
			if(InOut.equalsIgnoreCase("IN"))
			{	
				//message = "TripID:" +tripdate+ ".Time:" +InOut+"-"+tripTime+ "UserName:" +DriverUsername+ "PIN:" +driverPassword+ ". Regds, Transport Team";
			    message = "TripDate:"+tripdate+ ", Time " +InOut+"-"+tripTime+", UserName:"+DriverUsername+ ", PIN#"+driverPassword+  ". Regds, Transport Team.";
			}
			else
			{
				message = "TripDate:"+tripdate+ ", Time " +InOut+"-"+tripTime+", UserName:"+DriverUsername+ ", PIN#"+driverPassword+  ". Regds, Transport Team.";
				
			}
			System.out.println(message);
		}
		else if(messageFactoryVal.equalsIgnoreCase("gss"))
		{
			if(InOut.equalsIgnoreCase("IN"))
			{	
			    message = "Date:"+tripdate+", Login time "+tripTime+", First point time "+firstPickUpTime+", PIN # "+driverPassword+", Escort "+isEscort+". Regds, Transport Team";
			}
			else
			{
				message = "Date:"+tripdate+", Logout time "+tripTime+", PIN # "+driverPassword+", Escort "+isEscort+". Regds, Transport Team";
				
			}
		}
		return message;
		
	}
	public static String getEscortMesssage(String tripCode, String inOut, String tripTime, String vehNo, String driverName, String driverContact, String escortPswd) {
		String message="";		
		if(messageFactoryVal.equalsIgnoreCase("gss"))
		{
		message="TripID:"+tripCode+".Time:"+inOut+"-"+tripTime+", Cab #: "+vehNo+", Driver mob # "+driverContact+", PIN:"+escortPswd+". Regds";
		}
		else
		{
		message="TripID:"+tripCode+".Time:"+inOut+"-"+tripTime+", Cab #: "+vehNo+",Driver "+driverName+", Driver mob # "+driverContact+", PIN:"+escortPswd+". Regds, Transport Team. Helpline #s 9880116303 or 08040343434";
		}
		return message;
		
		
	}
	
	public static String getEmergencyDriverSMS(EmergencyDto dto,String driverpswd)
	{
		String message="";
		
		message ="Date:"+dto.getTravelDate()+", Trip time "+dto.getStartTime()+", First point time "+dto.getStartTime()+", PIN # "+driverpswd+". Regds, Transport Team";
		
		System.out.println(message);
		
		
		return message;
		
	}
	
	public static String getEmergencyEmployeeSMS(EmergencyDto dto,String driverContact,String empPin)
	{
		String message="";
		
		
		message = "Dear "+ dto.getBookingforName() +",Emergency Booking Cab details for "+OtherFunctions.changeDateFromat(dto.getTravelDate())+" . Cab:"+ dto.getVehicleNo()+" Driver AT("+driverContact+"), Time:" + dto.getStartTime() + " . PIN #"+empPin+". Regds, Transport Team. Helpline # 7760995236 or 08040343496" ;
		
		System.out.println(message);
		
		
		return message;
		
	}
}
