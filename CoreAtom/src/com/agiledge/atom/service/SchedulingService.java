/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFDataFormat;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.ScheduleAlterDao;
import com.agiledge.atom.dao.ScheduledEmpDao;
import com.agiledge.atom.dao.SchedulingDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.ScheduleAlterDto;
import com.agiledge.atom.dto.ScheduledEmpDto;
import com.agiledge.atom.dto.SchedulingDto;
import com.agiledge.atom.mail.SendMail;
import com.agiledge.atom.mail.SendMailFactory;



/**
 * 
 * @author 123
 */
public class SchedulingService {
	private String message="";
	private ArrayList<String> messageList= new ArrayList<String> ();
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public boolean isError() {
		return error;
	}

	public void setError(boolean error) {
		this.error = error;
	}

	private boolean error=false;
	public ScheduledEmpDto getSchedulingDetailBySchedule(long scheduleId) {
		ScheduledEmpDto schedulingDtoObj = new ScheduledEmpDto();
		schedulingDtoObj = new ScheduledEmpDao()
				.getScheduledEmpBySchedule(scheduleId);
		return schedulingDtoObj;
	}

	public long[] getSchedulingDetailByEmployee(long empId) {
		
		long[] schedudleIds = new ScheduledEmpDao().getScheduledEmpByEmployee(empId);
		return schedudleIds;
	}
	
	
	public boolean isScheduled(String subscriptionID) {
		return new ScheduledEmpDao().isScheduled(subscriptionID);
	}

	public int setScheduleEmployees(ArrayList<SchedulingDto> scheduleEmpList) {
		int retVal = new SchedulingDao().setScheduleEmployees(scheduleEmpList);
		int returnValue=insertEmployeesWeeklyOff(scheduleEmpList);
		for (SchedulingDto dto : scheduleEmpList) {
			if (dto.isIsUpdated()) {

				if(SettingsConstant.emailForScheduleEmployee.equalsIgnoreCase("yes")) {
					sendScheduledMessageToEmployee(dto);
				}

			}
		}
		return retVal;

	}

	public int insertEmployeesWeeklyOff(ArrayList<SchedulingDto> schedulingDtos) {
		
		
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		Calendar tempDate = Calendar.getInstance();
		ArrayList<ScheduleAlterDto> scheduleAlterDtos = new ArrayList<ScheduleAlterDto>();
		ScheduleAlterDto scheduleAlterDto = null;
		int retVal = 0;

		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			for (SchedulingDto schedulingDto : schedulingDtos) {				
				if (schedulingDto.getWeeklyOff().equalsIgnoreCase("on")
						&& schedulingDto.getScheduleId() > 0) {
					fromDate.setTime(formatter.parse(schedulingDto
							.getSchedulingFromDate()));
					toDate.setTime(formatter.parse(schedulingDto
							.getSchedulingToDate()));
					//System.out.println(fromDate.getTime()+"IN "+toDate.getTime());
					while (fromDate.getTimeInMillis() <= toDate
							.getTimeInMillis()) {
						tempDate = fromDate;
						if (tempDate.get(Calendar.DAY_OF_WEEK) == 1
								|| tempDate.get(Calendar.DAY_OF_WEEK) == 7) {
							scheduleAlterDto = new ScheduleAlterDto();
							scheduleAlterDto.setScheduleId(""
									+ schedulingDto.getScheduleId());
							scheduleAlterDto.setUpdatedById(schedulingDto.getScheduledBy());
							scheduleAlterDto.setScheduleStates("new");
							scheduleAlterDto.setDate(OtherFunctions
									.changeDateFromatToIso(tempDate.getTime()));
							scheduleAlterDto.setLoginTime("weekly off");
							scheduleAlterDto.setLogoutTime("weekly off");
							scheduleAlterDtos.add(scheduleAlterDto);
						}
						fromDate.add(Calendar.DATE, 1);
					}
				}
			}
			if (scheduleAlterDtos.size() > 0) {
				retVal = new ScheduleAlterDao()
						.scheduleAlterInsert(scheduleAlterDtos);
			}
		} catch (Exception e) {

		}
		return retVal;
	}

	public void addWeeklyOff( SchedulingDto schedulingDto) {
		
		
		Calendar fromDate = Calendar.getInstance();
		Calendar toDate = Calendar.getInstance();
		Calendar tempDate = Calendar.getInstance();
		ArrayList<ScheduleAlterDto> scheduleAlterDtos = new ArrayList<ScheduleAlterDto>();
		ScheduleAlterDto scheduleAlterDto = null;
		int retVal = 0;

		try {
			DateFormat formatter;
			formatter = new SimpleDateFormat("dd/MM/yyyy");
			 
				if (schedulingDto.getWeeklyOff().equalsIgnoreCase("on")
						 ) {
				 
					fromDate.setTime(formatter.parse(schedulingDto
							.getSchedulingFromDate()));
					toDate.setTime(formatter.parse(schedulingDto
							.getSchedulingToDate()));					
					
					while (fromDate.getTimeInMillis() <= toDate
							.getTimeInMillis()) {
						tempDate = fromDate;
			
						if (tempDate.get(Calendar.DAY_OF_WEEK) == 1
								|| tempDate.get(Calendar.DAY_OF_WEEK) == 7) {
							 
							scheduleAlterDto = new ScheduleAlterDto();
							scheduleAlterDto.setScheduleId(""
									+ schedulingDto.getScheduleId());
							scheduleAlterDto.setUpdatedById(schedulingDto.getScheduledBy());
							scheduleAlterDto.setScheduleStates("new");
							scheduleAlterDto.setDate(OtherFunctions
									.changeDateFromatToIso(tempDate.getTime()));
							scheduleAlterDto.setLoginTime("weekly off");
							scheduleAlterDto.setLogoutTime("weekly off");
							scheduleAlterDto.setStatus("a");
							scheduleAlterDtos.add(scheduleAlterDto);
						}
						fromDate.add(Calendar.DATE, 1);
					}
				}
			 
			if (scheduleAlterDtos.size() > 0) {
				 schedulingDto.getAlterList().addAll(scheduleAlterDtos);
			}
		} catch (Exception e) {
System.out.println(e.toString());
		}
		 
	}


	// method to send message to employee based on subscription id
	public void sendScheduledMessageToEmployee(SchedulingDto dto) {
		EmployeeDto empDto = new EmployeeDao()
				.getEmployeeDetailsBasedOnSubscriptionID(dto
						.getSubscriptionId());
		

		String emailAddress = "";
		int returnInt = 0;
		if (empDto != null) {

			emailAddress = empDto.getEmailAddress();

			SendMail mail = SendMailFactory.getMailInstance();
			String messageToEmployees = SettingsService
					.getScheduledMessageDescriptionForEmployee(dto);
			 
			String title = SettingsService
					.getScheduledMessageTitleDescriptionForEmployee();

			try {
				 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
					String bccs[] = new String[0];
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
					for(EmployeeDto tm: tms)
					{
					bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
							.getEmails(new EmployeeDao().getDelegatedEmployees(tm
									.getEmployeeID())));
					}
				
			 
				mail.setBccs(bccs);
				mail.send(emailAddress, title, messageToEmployees);
				System.out.println("message is"+messageToEmployees);
				returnInt = 1;
			} catch (Exception e) {
				System.out.println("Error In mail sending : " + e);
			}
		}
	}

	// method to send message to employee based on subscription id
	public void sendBookedMessage(String subscriptionID) {
		String message = "Your transportation has been scheduled from..";

		new SchedulingDao().getAllSubscribedEmployeeDetailsByManger(1000);

	}

	/*
	 * Created by Noufal on oct 22 2o12
	 */
	 
	public List<ScheduledEmpDto> getScheduledHistory(String subscriptionID,
			String fromDate, String toDate) {
		return new ScheduledEmpDao().getScheduledHistory(subscriptionID,
				fromDate, toDate);
	}

	public void sendEmailAboutScheduleExpiryToSupervisor(String curDate,
			int dayBefore) {
		// TODO Auto-generated method stub
	 
			List<EmployeeSubscriptionDto> dtoList = new EmployeeDao()
					.getSupervisorBasedScheduleEmployeesAndWhoAreAboutToExpirty(
							curDate, dayBefore);

			// HashMap<String, EmployeeDto> emp= new HashMap<String,
			// EmployeeDto>();
			ArrayList<EmployeeSubscriptionDto> emp = new ArrayList<EmployeeSubscriptionDto>();
			// / emp=(ArrayList<EmployeeSubscriptionDto>) new
			// EmployeeDao().getEmployeesWhoAreAboutScheduleExpiry(curDate,1);
			int i = 0;
			EmployeeSubscriptionDto preDto = null;

			if (dtoList != null && dtoList.size() > 0) {
				HashMap<String,String> employeesChak = new HashMap<String,String>();
				
				boolean flag = false;
				for (; i < dtoList.size(); i++) {
					EmployeeSubscriptionDto curDto = dtoList.get(i);
					HashMap<String,String> employees1 = new HashMap<String,String>();
					if (preDto != null
							&& !curDto
									.getSupervisor()
									.getEmailAddress()
									.equals(preDto.getSupervisor()
											.getEmailAddress())) {
						String msg = "Hi "
								 
								+ preDto.getSupervisor().getDisplayName()
								
								+ ",<br/><br/>Transport schedule will expire on " +
								OtherFunctions
								.changeDateFromatToddmmyyyy(preDto
										.getUnsubscriptionDate())

								+ " for below members. Please create new schedule to have uninterrupted transport facility.<br/>";
								 

						String submsg = "";
						int j = 0;
						while (j < emp.size()) {
							EmployeeDto tttt = new EmployeeDao()
									.getEmployeeAccurate(emp.get(j)
											.getEmployee().getEmployeeID());
							submsg += "Emp"+ (j+1) + " : "+ tttt.getDisplayName() + " " + " ("
									+ tttt.getPersonnelNo() + " )" + " <br/>";
							j++;
							OtherFunctions.insertIfNotExistsElseDelete(employees1, tttt.getEmailAddress(),employeesChak);
						}
						emp.clear();
						submsg += "<br/>  Please ignore this if schedule has already been created. Else, click on link below to create schedule. "
								+ "<br/><br/>" 
								+ "Regards,<br/>" +
								" <a href='"
								+ new SettingsDoa().getDomainName()
								+"'>" 
								+ "Transport Team."
								+"</a>"
								+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
						String address = preDto.getSupervisor()
								.getEmailAddress();
						String title = "Transport Schedule is expiring on " + OtherFunctions
								.changeDateFromatToddmmyyyy(preDto
										.getUnsubscriptionDate());

						 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
							String bccs[] = new String[0];
							bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
							for(EmployeeDto tm: tms)
							{
							bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
									.getEmails(new EmployeeDao().getDelegatedEmployees(tm
											.getEmployeeID())));
							}
						
						bccs=OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(new EmployeeDao().getDelegatedEmployees( preDto.getSupervisor().getEmployeeID() )));
						String ccs[]=OtherFunctions.mapToStringArray(employees1);
						SendMail mail=SendMailFactory.getMailInstance();
						if(bccs!=null&&bccs.length>0)
						{
							mail.setBccs(bccs);
						}
						if(ccs!=null&&ccs.length>0)
						{
							mail.setCcs(ccs);
						}
						
						mail.send(address, title, msg + submsg);
					}

					emp.add(curDto);

					preDto = curDto;

				}
				if (preDto != null) {
					HashMap<String,String> employees1 = new HashMap<String,String>();
					String msg = "Hi "
							 
							+ preDto.getSupervisor().getDisplayName()
							 
							+ ",<br/><br/>Transport schedule will expire on " +
							OtherFunctions
							.changeDateFromatToddmmyyyy(preDto
									.getUnsubscriptionDate())

							+ " for below members. Please create new schedule to have uninterrupted transport facility. <br/>";
							 


					String submsg = "";
					int j = 0;
					System.out
							.println("..........................AAAA..............");
					while (j < emp.size()) {
						EmployeeDto tttt = new EmployeeDao()
								.getEmployeeAccurate(emp.get(j).getEmployee()
										.getEmployeeID());
						submsg += "Emp"+ (j+1) + " : "+ tttt.getDisplayName() + " " + " ("
								+ tttt.getPersonnelNo() + " )" + " <br/>";
						j++;
						OtherFunctions.insertIfNotExistsElseDelete(employees1, tttt.getEmailAddress(),employeesChak);
					}
					emp.clear();
					submsg += "<br/>  Please ignore this if schedule has already been created. Else, click on link below to create schedule. "
							+ "<br/><br/>" 
							+ "Regards,<br/> " +
							" <a href='"
							+ new SettingsDoa().getDomainName()
							+"'>" 
							+ "Transport Team."
							+"</a>"
							+"<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";

					String address = preDto.getSupervisor().getEmailAddress();
					String title = "Transport Schedule is expiring on " + OtherFunctions
							.changeDateFromatToddmmyyyy(preDto
									.getUnsubscriptionDate());

					 ArrayList<EmployeeDto> tms = new EmployeeDao().getEmployeesByRole("ta");
						String bccs[] = new String[0];
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(tms));
						for(EmployeeDto tm: tms)
						{
						bccs = OtherFunctions.appendArray(bccs, new EmployeeService()
								.getEmails(new EmployeeDao().getDelegatedEmployees(tm
										.getEmployeeID())));
						}
					
					bccs=OtherFunctions.appendArray(bccs, new EmployeeService().getEmails(new EmployeeDao().getDelegatedEmployees( preDto.getSupervisor().getEmployeeID() )));
					String ccs[]=OtherFunctions.mapToStringArray(employees1);
					SendMail mail=SendMailFactory.getMailInstance();
					if(bccs!=null&&bccs.length>0)
					{
						mail.setBccs(bccs);
					}
					if(ccs!=null&&ccs.length>0)
					{
						mail.setCcs(ccs);
					}
					
					mail.send(address, title, msg + submsg);

				}

			}
			 
		 
	}

	
	// Creted by Noufal C C ends

	public boolean isWithinSchedules(String subscriptionID, String fromDate,
			String toDate) {
		return new ScheduledEmpDao().isWithinSchedules(subscriptionID,
				fromDate, toDate);
	}

	public int uploadSchedule(InputStream inputStream1, String site, String changedBy) {
		// TODO Auto-generated method stub
		int val=0;
		InputStream inputStream = null;
		ArrayList<SchedulingDto>  dtoList = null;
		/*String fileName= writeToFile(inputStream1);
	
		
		if(fileName==null) {		
			System.out.println("WT fName"+fileName);
		}else {
			try {
				inputStream = new FileInputStream(new File(fileName));
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				message = "File Operation Failed";
				error=true;
				
			}
		}
	*/	inputStream = inputStream1;
		if(!error) {
			 dtoList = uploadScheduleFile(inputStream);
		}
		if(error) {
			System.out.println("Error : "+message);
			 
		}else
		if(dtoList!=null && dtoList.size()>0) {
			SchedulingDao schDao = new SchedulingDao();
			val = schDao.uploadSchedule(dtoList, changedBy);
			setMessage(schDao.getMessage());
			setMessageList(schDao.getMessageList());
		//	printSchedule(dtoList);
		}
		return val;
	}
	
	public void printSchedule(ArrayList<SchedulingDto>  dtoList ) {
		if(dtoList!=null && dtoList.size()>0) {
			for(SchedulingDto dto : dtoList) {
				if(dto.getAlterList()!=null && dto.getAlterList().size()>0) {
					for(ScheduleAlterDto adto : dto.getAlterList()) {
					}
				}
			}
		}
	}
	
	public String writeToFile(InputStream inputStream) {
		 
		OutputStream outputStream = null;
		String fileName="schupload" + new Date().toString() + ".xlxs";
		try {
			// read this file into InputStream
			 
	 
			// write the inputStream to a FileOutputStream
			outputStream = 
	                    new FileOutputStream(new File(fileName));
	 
			int read = 0;
			byte[] bytes = new byte[1024];
	 
			while ((read = inputStream.read(bytes)) != -1) {
				outputStream.write(bytes, 0, read);
			}
	 
			System.out.println("Done!");
			
		} catch (IOException e) {
			e.printStackTrace();
			fileName=null;
		} finally {
			if (inputStream != null) {
				try {
					inputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (outputStream != null) {
				try {
					// outputStream.flush();
					outputStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
	 
			}
			
		}
		return fileName;
	}

	
	public ArrayList<SchedulingDto>  uploadScheduleFile( InputStream in) {
		int r=0;
		error=false;
		message="";
		int rowIndex=0;
		ArrayList<SchedulingDto> dtoList = new ArrayList<SchedulingDto>();
		try {
			
			boolean flag = false;
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			  
			Iterator<Row> rowIterator = sheet.iterator();
			  
			rowIterator.next();
			while (rowIterator.hasNext() && error==false) {
			
				
				rowIndex ++;
				if (flag)
					break;

				Row row = rowIterator.next();
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				SchedulingDto dto = new SchedulingDto();
				boolean isScheduleAlter = false;
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					
					
					System.out.println("col " + cell.getColumnIndex());
					r=cell.getColumnIndex();
					if (cell.getColumnIndex() < 7) {
						if(cell.getColumnIndex()!=3&&cell.getColumnIndex()!=4) { 
						cell.setCellType(Cell.CELL_TYPE_STRING);
						}
						
					} else {
						break;
					}
 
					if (cell.getColumnIndex() == 0) {
						 // personnel No
								String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

						dto.setEmployeeId( cell.getStringCellValue().trim());
						 
					 

					} else if (cell.getColumnIndex() == 1) {
						  
					 
					 try{
						Calendar cal = Calendar.getInstance();
						cal.set(1900, 0, 0, 0, 0);
					    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
						 dto.setSchedulingFromDate(OtherFunctions
									.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) ));
						
					 }catch(NumberFormatException ne) {
							dto.setSchedulingFromDate(cell.getStringCellValue());
						}
						 if(OtherFunctions.checkDateFormat(dto.getSchedulingFromDate(), "dd/MM/yyyy")==false){
							 if(OtherFunctions.checkDateFormat(dto.getSchedulingFromDate(), "yyyy-MM-dd")) {
								 
								 dto.setSchedulingFromDate(OtherFunctions.changeDateFromat(dto.getSchedulingFromDate()));
							 } else {
							 message=message + "|" + "From Date :incorrect format (row:" + rowIndex+" )";
							 error=true;
							 }
						 }
						 

					} else if (cell.getColumnIndex() == 2) {
					//	cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						try{
						Calendar cal = Calendar.getInstance();
						cal.set(1900, 0, 0, 0, 0);
					    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
						dto.setSchedulingToDate( OtherFunctions
								.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) ));
						}catch(NumberFormatException ne) {
							dto.setSchedulingToDate(cell.getStringCellValue());
						}
					System.out.println(	" To date :" + dto.getSchedulingToDate());
 
						 if(dto.getSchedulingToDate().equalsIgnoreCase("")) {
							 isScheduleAlter=true;
							  
						 } else if(OtherFunctions.checkDateFormat(dto.getSchedulingToDate(), "dd/MM/yyyy")==false){
							 if(OtherFunctions.checkDateFormat(dto.getSchedulingToDate(), "yyyy-MM-dd")) {
								 dto.setSchedulingToDate(OtherFunctions.changeDateFromat(dto.getSchedulingToDate()));
							 } else {
								 message=message + "|" + "To Date :incorrect format (row:" + rowIndex+" )";
								 error=true;
								 
							 }
						 }

					}  else if (cell.getColumnIndex() == 3) {
						// cell.setCellType(Cell.CELL_TYPE_NUMERIC);
						 // dto.setLoginTime(cell.getStringCellValue().trim());
	 
						  if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
						  System.out.println("login  time : " +cell.getDateCellValue());
							  Date date = cell.getDateCellValue();
  
						        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
 						        String dateStamp = formatTime.format(date);
 						        dto.setLoginTime(dateStamp);

						  System.out.println(" login time : " + dto.getLoginTime());
						  System.out.println("type : "+ cell.getCellType());
						  try {
							  String split[] = dto.getLoginTime().split(":");
							  if(split[0].length()<2) {
								  split[0]="0"+split[0];
							  }
							  if(split[1].length()<2) {
								  split[1]="0"+split[0];
							  }
							  dto.setLoginTime(split[0]+":" + split[1]);
						  }catch(Exception e) {
							  if(dto.getLoginTime()==null||dto.getLoginTime().trim().equalsIgnoreCase("")||dto.getLoginTime().trim().equalsIgnoreCase("none")||dto.getLoginTime().trim().equalsIgnoreCase("nill")||dto.getLoginTime().trim().equalsIgnoreCase("null")) {
									dto.setLoginTime("none");
								} else {
									  String time = OtherFunctions.TimeFormat24Hr(dto.getLoginTime());
									  if(time!=null) {
										  dto.setLoginTime(time);
									  } else {
										  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
										  error=true;
									  }
								   
								}
						  }
						  } else  {

							  if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
								    System.out.println("cell type : " + cell.getCellType());
								  dto.setLoginTime(cell.getStringCellValue());
								 System.out.println("Login time :"+ dto.getLoginTime());
								  if(dto.getLoginTime()==null||dto.getLoginTime().trim().equalsIgnoreCase("")||dto.getLoginTime().trim().equalsIgnoreCase("none")||dto.getLoginTime().trim().equalsIgnoreCase("nill")||dto.getLoginTime().trim().equalsIgnoreCase("null")||dto.getLoginTime().trim().equalsIgnoreCase("cancel")||dto.getLoginTime().trim().equalsIgnoreCase("off")) {
										dto.setLoginTime("none");
									}  
									
									 else {
										  String time = OtherFunctions.TimeFormat24Hr(dto.getLoginTime());
										  if(time!=null) {
											  dto.setLoginTime(time);
										  } else {
											  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
											  error=true;
										  }
									}
							  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
									dto.setLoginTime("none");
							  }
							   
						  }
					   
					} else if (cell.getColumnIndex() == 4) {
						 
						  if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
							 
							  Date date = cell.getDateCellValue();
							  
						        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
						        String dateStamp = formatTime.format(date);
						        dto.setLogoutTime(dateStamp);

							  System.out.println(" logout time : " + dto.getLogoutTime());
							  System.out.println("type : "+ cell.getCellType());
							try{
								String split[] = dto.getLogoutTime().split(":");
								  if(split[0].length()<2) {
									  split[0]="0"+split[0];
								  }
								  if(split[1].length()<2) {
									  split[1]="0"+split[0];
								  }
								  dto.setLogoutTime( split[0]+":" + split[1]);
							}catch(Exception e) {
								if(dto.getLogoutTime()==null||dto.getLogoutTime().trim().equalsIgnoreCase("")||dto.getLogoutTime().trim().equalsIgnoreCase("none")||dto.getLogoutTime().trim().equalsIgnoreCase("nill")||dto.getLogoutTime().trim().equalsIgnoreCase("null")||dto.getLogoutTime().trim().equalsIgnoreCase("cancel")||dto.getLogoutTime().trim().equalsIgnoreCase("off")) {
									dto.setLogoutTime("none");
								} else {
									 String time = OtherFunctions.TimeFormat24Hr(dto.getLogoutTime());
									  if(time!=null) {
										  dto.setLogoutTime(time);
									  } else {

										  message=message + "|" + "Logout Time :incorrect format (row:" + rowIndex+" )";
											 error=true;
									  }
								}
							}
						  } else {
							if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
							    System.out.println("cell type : " + cell.getCellType());
							  dto.setLogoutTime(cell.getStringCellValue());
							 System.out.println("Logout time :"+ dto.getLogoutTime());
							  if(dto.getLogoutTime()==null||dto.getLogoutTime().trim().equalsIgnoreCase("")||dto.getLogoutTime().trim().equalsIgnoreCase("none")||dto.getLogoutTime().trim().equalsIgnoreCase("nill")||dto.getLogoutTime().trim().equalsIgnoreCase("null")||dto.getLogoutTime().trim().equalsIgnoreCase("cancel")||dto.getLogoutTime().trim().equalsIgnoreCase("off")) {
									dto.setLogoutTime("none");
								} else {
									 String time = OtherFunctions.TimeFormat24Hr(dto.getLogoutTime());
									  if(time!=null) {
										  dto.setLogoutTime(time);
									  } else {

										  message=message + "|" + "Logout Time :incorrect format (row:" + rowIndex+" )";
											 error=true;
									  }

 								}
						  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
								dto.setLogoutTime("none");
						  }
						  }
						  
						  if(isScheduleAlter) {
								 SchedulingDto preDto = dtoList.get(dtoList.size()-1);
								 if(preDto!=null) {
									 System.out.println("schedule alter .. ");
									 ScheduleAlterDto adto = new ScheduleAlterDto();
									 adto.setDate(dto.getSchedulingFromDate());
									 if(OtherFunctions.isEmpty(adto.getStatus()) ) {
										 adto.setStatus("a");
									 }else {
									 adto.setStatus(dto.getStatus());
									 }
									 adto.setLoginTime(dto.getLoginTime());
									 adto.setLogoutTime(dto.getLogoutTime());
									 preDto.getAlterList().add(adto);
									 System.out.println("added");
								 }
								 
							 }
							
						
					} else if (cell.getColumnIndex() == 5) {
						 
						 
						dto.setProject( cell.getStringCellValue().trim());
						  System.out.println(" project : " + dto.getProject());
					} else if (cell.getColumnIndex() == 6) {
						 
						 
						dto.setStatus( cell.getStringCellValue().trim());
						System.out.println(" status : " + dto.getStatus());
						System.out.println("is alter : "+ isScheduleAlter);
						 if(isScheduleAlter==false && dto.getStatus().equalsIgnoreCase("weekly off")) {
								 System.out.println("adding to weekly off days");
								 dto.setWeeklyOff("on");
								 addWeeklyOff(dto);
								 
							 }
							 
						 }
					
					
					
				
				}
				
				if (!flag) {
					if(!isScheduleAlter) {
						dtoList.add(dto);
					}
				}
				

			}
 			//System.out.println("returnint" + returnint);
			if(error) {
				message="Validation error at line : "+ rowIndex + " "+ message;
			}
		//	file.close();
			in.close();
			
		}

		catch (FileNotFoundException fnfe) {
			dtoList = null;
			fnfe.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			dtoList = null;
			System.out.println("ERRRO on cell "+r+"  cause"+e);
			e.printStackTrace();
			
		}
		return dtoList;
	}

	public ArrayList<String> getMessageList() {
		return messageList;
	}

	public void setMessageList(ArrayList<String> messageList) {
		this.messageList = messageList;
	}
	
	
	public SchedulingDto getDetailsForSchedule(String employeeId) {

		return new SchedulingDao().getDetailsForSchedule(employeeId);
	}

	public long scheduleSelf(SchedulingDto dto) {
		long retVal = new SchedulingDao().scheduleSelf(dto);
		addWeeklyOff(dto);
		return retVal;
	}
	
	
	public ArrayList<SchedulingDto>  uploadScheduleHorizoantalFile( InputStream in)  {
		 
		///FileInputStream fis = new FileInputStream(new File("D:\\CrossDomain\\testExcel.xlsx"));
	       // FileInputStream fis = new FileInputStream(excel);
		ArrayList<SchedulingDto> dtoList = new ArrayList<SchedulingDto>();
		int rowNum=0;
		int  colNum=0;
		int i=0;
		int j=0;
		try {
	        XSSFWorkbook wb = new XSSFWorkbook(in);
		  //org.apache.poi.ss.usermodel.Workbook wb = WorkbookFactory.create(fis);
		  
	        //XSSFSheet ws = wb.getSheet(0);
	        XSSFSheet ws =  wb.getSheetAt(0);
		  //org.apache.poi.ss.usermodel.Sheet ws = wb.getSheetAt(0);
	        
	        rowNum = ws.getLastRowNum() + 1;
	        System.out.println(".................");
	        colNum = ws.getRow(0).getLastCellNum();
	        
	        String [][] data = new String [rowNum] [colNum];
	        
	        ArrayList<String> dates= new ArrayList<String>();
	        
	        SchedulingDto dto = null;  
	        ScheduleAlterDto adto = null;
	        //rowNum
	        
	        for( i = 0; i <rowNum; i++){
	            XSSFRow row = (XSSFRow) ws.getRow(i);
	        	//Row row = (XSSFRow) ws.getRow(i);
	             System.out.println("RownNum : " + i);
	             boolean invalidFlow=true;
	             boolean invalid=false;
	             boolean firstLogoutTime=false;
	             boolean sameLogTime =false;
	             //boolean get
	             boolean blank=false;
	             System.out.print("colnum :"+ colNum + " secondLimit : " + ( 5+ dates.size()*2));
	                for ( j = 0; (j < colNum&&i<=1) || (j<( 5+ dates.size()*2)&&i>1); j++){
	                	
	                	System.out.println(" colNum = " + colNum + " j=" + j);
	                    XSSFCell cell = row.getCell(j);
	                    System.out.println("j = " + j);
	                    
	                    if(j==0&&i>1) {
	                    	try {
	                    	System.out.println(" j==0 : cell type :  " + cell.getCellType());
	                		if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
	                			 System.out.println("BLANKK");
	                			blank=true;
	                			break;
	                		}
	                		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                			System.out.println(" cell value of first column: "+ cell.getStringCellValue());
	                			if(cell.getStringCellValue().equalsIgnoreCase("")) {
	                				System.out.println("BLANKK");
	                				blank=true;
	                				break;
	                			}
	                		}
	                    	}catch(Exception e) {
	                    		System.out.println("Error in firstCol : "+ e);
	                    		blank=true;
	                    		break;
	                    	}
	                	}  
	              //  	org.apache.poi.ss.usermodel.Cell cell =  row.getCell(j);
	                    
	                   // String value = cell.toString();
	                   // data[i][j] = value;
	                    // System.out.print ("("+i+", "+j+"):" + value +"\t");
	                    if(j>4 && i==0) {
	                   //System.out.print ("the value is("+i+", "+j+") " + value +"\t");
	                    	if(i==0  && j%2==1) {
	                    		
	                    		System.out.println(" inside date");
	    						String date="";
	    						if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC  && DateUtil.isCellDateFormatted(cell)) {
	    							
	    							 
 								 date =OtherFunctions
	    									.changeDateFromatToSqlFormat(  cell.getDateCellValue() );
 								
 							} else if(cell.getCellType()==Cell.CELL_TYPE_STRING){
	    						try{
	    							Calendar cal = Calendar.getInstance();
	    							cal.set(1900, 0, 0, 0, 0);
	    				
	    						    cal.add(Calendar.DATE,  Integer.parseInt( cell.getStringCellValue())-1);
	    							 date =OtherFunctions
	    									.changeDateFromatToSqlFormat( new Date(cal.getTimeInMillis()) );
	    							}catch(NumberFormatException ne) {
	    								date =cell.getStringCellValue();
	    							}
	    						//System.out.println(	" To date :" + dto.getSchedulingToDate());
	    							if(OtherFunctions.checkDateFormat(date, "dd/MM/yyyy")==false){
	    								 if(OtherFunctions.checkDateFormat(date, "yyyy-MM-dd")) {
	    									 date =OtherFunctions.changeDateFromat(date);
	    								 } else if (OtherFunctions.checkDateFormat(date, "yyyy-MMM-dd")) {
	    									 date = OtherFunctions.changeTimeFormat(date, "yyyy-MMM-dd", "yyyy-MM-dd");
	    								 } else if (OtherFunctions.checkDateFormat(date, "dd-MMM-yyyy")) {
	    									 date = OtherFunctions.changeTimeFormat(date, "dd-MMM-yyyy", "yyyy-MM-dd");
	    								 } else {
//	    									 message=message + "|" + "To Date :incorrect format (row:" + rowIndex+" )";
	    									 error=true;
	    									 System.out.println("date validation error");
	    									 getMessageList().add("Error (Row :"+(i+1) +", Col :"+ (j + 1) + ")  : Date Validation Error  ");
	    									 
	    								 }
	    							 }
	    							
 							}
	    						dates.add(date);	
 							} else if (i>1) {
 							//	System.out.print(String.format("(%2d, %2d ) : %9s",i,j,value));
 							}	

	                    	
	                    	
	                    } else if(i>1) {
	                    	if(j==0) {
	                    		System.out.println("Creating scheduling dto ");
	                    		dto = new SchedulingDto();
	                    		dtoList.add(dto);
	                    		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                    		dto.setEmployeeId(cell.getStringCellValue());
	                    		
	                    		} else {
	                    			try {
	                    			cell.setCellType(Cell.CELL_TYPE_STRING);
	                    			dto.setEmployeeId(cell.getStringCellValue());
	                    			} catch(Exception e) {
	                    				getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Employee No Field Empty  ", (i+1), (j +1) ));
	                    				dto.setEmployeeId("");
	                    			}
	                    			 
	                    		}
	                    		System.out.println(" employee Id :  " + dto.getEmployeeId());
	                    	} else if(j==1) {
	                    		 
	                    		 
	                    		if(cell.getCellType()==Cell.CELL_TYPE_STRING) {
	                    		dto.setEmployeeName(cell.getStringCellValue());
	                    		
	                    		} else {
	                    			try {
		                    			cell.setCellType(Cell.CELL_TYPE_STRING);
		                    			dto.setEmployeeName(cell.getStringCellValue());
	                    			}catch(Exception e) {
	                    				dto.setEmployeeName("");
	                    				getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Employee Name Field Empty ", (i+1), (j +1) ));
	                    			}
	                    			//getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Employee Name Field Empty ", (i+1), (j +1) ));
	                    		}
//	                    		System.out.println("Employee Name : "+ dto.getEmployeeName());
	                    	} else  if(j==2) {
	                    		  System.out.println("Prject getting");
	                    		  try {
	                    		 
	                    				cell.setCellType(Cell.CELL_TYPE_STRING);
	                        			dto.setProject(cell.getStringCellValue());
	                    		 
	                    		  
	                    		  } catch(Exception e) {
	                    			  getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Employee Project Field Empty  ", (i+1), (j +1) ));
	                    			  dto.setProject("error");
	                    		  }
	                    		
	                    		System.out.println("Project : "+ dto.getProject() );
	                    	} else if (j>4) {
	                    		System.out.println(" the tyme");
	                    		 
	                    		if(j%2==1) {
	                    			System.out.println(" in login ... (" + i + " , " + j + " )");
	                    			String loginTime= getLoginTime(cell);
	                    			if(loginTime.equalsIgnoreCase("error")) {
	                    				System.out.println("Error .... ");
	                    				getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Invalid Login Time", (i+1), (j +1) ));
	                    				System.out.println("");
	                    			}
	                    			if(loginTime.equalsIgnoreCase("invalid") ) {
	                    				 invalid=true;
	                    			}else {
	                    				

	                    				 invalid=false;
		                    			 
		                    					
	                    				 if(invalidFlow == true) {
	                    					 System.out.println(" whatt happen to " + dto );
	                    					 
			                    				firstLogoutTime=true; 
			                    					dto.setSchedulingFromDate( dates.get((j-5)/2));
			                    				 
			                    				dto.setLoginTime(loginTime);
			                    				invalidFlow = false;
			                    			} else {
			                    				String loginTime1=getLoginTime(cell);
			                    				if(loginTime1.equalsIgnoreCase("error")) {
			                    					getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Invalid Login Time", (i+1), (j +1) ));
			                    				}
			                    				if(loginTime.equals(dto.getLoginTime())) {
			                    					sameLogTime=true;
			                    				} else {
			                    					sameLogTime = false;
					                    			adto =new ScheduleAlterDto();
					                    			adto.setLoginTime( loginTime1  );
					                    			System.out.println(" Date Index :" + ( (j-5)/2));
					                    			adto.setDate( dates.get((j-5)/2));
					                    			dto.getAlterList().add(adto);
					                    			
			                    				}
			                    				dto.setScheduledToDate( dates.get((j-5)/2));
				                    			
			                    			}
				                    			
		                    			 
		                    			
		                    			}			
	                    			
	                    		} else if (j%2==0) {
	                    			System.out.println(" in logout ... ");
	                    			if(invalid == false) {
	                    				if(firstLogoutTime) {
	                    					String logoutTime = getLogoutTime(cell);
	                    					if(logoutTime.equalsIgnoreCase("error")) {
	                    						getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Invalid Logout Time", (i+1), (j +1) ));
	                    					}
	                    					dto.setLogoutTime(logoutTime);
	                    					firstLogoutTime = false;
	                    				} else {
	                    					 String logoutTime1 = getLogoutTime(cell);
	                    					 if(logoutTime1.equalsIgnoreCase("error" )) {
	                    						 getMessageList().add(String.format( "Error (Row :%5d, Col :%5d)  : Invalid Logout Time", (i+1), (j +1) ));
	                    					 }
	                    					 if(sameLogTime  ) {
	                    						 if(dto.getLogoutTime().equals(logoutTime1)==false) {
				                    					sameLogTime = false;
						                    			adto =new ScheduleAlterDto();
						                    			adto.setLoginTime( dto.getLoginTime()  );
						                    			adto.setLogoutTime(logoutTime1);
						                    			adto.setDate( dates.get((j-5)/2));
						                    			dto.getAlterList().add(adto);
						                    			System.out.println("IN : " + adto.getLoginTime()+" OUT :" + adto.getLogoutTime());
	                    						 }
	                    						 
	                    					 } else {
	                    					adto.setLogoutTime(logoutTime1);
	                    					System.out.println("IN : " + adto.getLoginTime()+" OUT :" + adto.getLogoutTime());
	                    					 }
	                    					
	                    				}
	                    			} else {
	                    				System.out.println("INVALID...");
	                    			}
	                    			
	                    		}
	                    		
	                    		
	                    		
	                    	}
	                    	
	                    	 System.out.println(" getting ...");
	                    	
	                    }
	                }
	                System.out.println("))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))))");
	        }
	        
	        System.out.println();
	        System.out.println("total rows : "+ rowNum);
	        System.out.println(" ___________________");
	        for(String date : dates) {
	        	System.out.println(" " + date);
	        }
	        System.out.println(" ___________________");
	        /*for (SchedulingDto sDto : dtoList) {
	        	System.out.println("Emp Name : "+ sDto.getEmployeeId() + "\t EmpName : "+ sDto.getEmployeeName() + "\t Login : " + sDto.getLoginTime() + "\t Logout : " + sDto.getLogoutTime() + "\t From Date :" + sDto.getSchedulingFromDate() + "\t To Date :" + sDto.getScheduledToDate());
	        	System.out.println("Alter list size : "+ sDto.getAlterList().size());
	        	for(ScheduleAlterDto sAdto : sDto.getAlterList()) {
	        		System.out.println("Date : "+ sAdto.getDate() + "\tLogin : "+ sAdto.getLoginTime() + "Logout : "+ sAdto.getLogoutTime() );
	        		 
	        	}
	        }*/
		}catch( Exception e ) {
			error=true;
			System.out.println("Exception :"+ e);
			getMessageList().add("Error (Row :"+(i+1) +", Col :"+ (j + 1) + ")  : Data Validation Error  ");
		}
		finally {
			try {
	         in.close();
	         System.gc();
			} catch(Exception e) {
				System.out.println(" Exception while closing in : " + e);
			}
		}
		return dtoList;
	}

	private  String getLoginTime( Cell cell) {
		String loginTime=null;
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			  System.out.println("login  time : " +cell.getDateCellValue());
				  Date date = cell.getDateCellValue();

			        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
			        String dateStamp = formatTime.format(date);
			        loginTime= dateStamp;

			  //System.out.println(" login time : " + dto.getLoginTime());
			  System.out.println("type : "+ cell.getCellType());
			  try {
				  String split[] = loginTime.split(":");
				  if(split[0].length()<2) {
					  split[0]="0"+split[0];
				  }
				  if(split[1].length()<2) {
					  split[1]="0"+split[0];
				  }
				  loginTime = split[0]+":" + split[1];
			  }catch(Exception e) {
				  if(loginTime ==null||loginTime.trim().equalsIgnoreCase("")||loginTime.trim().equalsIgnoreCase("none")||loginTime.trim().equalsIgnoreCase("nill")||loginTime.trim().equalsIgnoreCase("null")||loginTime.trim().equalsIgnoreCase("weekly off")) {
					  loginTime="none";
					} else {
						if(loginTime.equalsIgnoreCase("invalid"))
						{
							
						} else {
						  String time = OtherFunctions.TimeFormat24Hr(loginTime );
						  if(time!=null) {
							  loginTime = time;
						  } else {
		/*					  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
							  error=true;
							  */
							  System.out.println(" Error in login format");
							  loginTime="Error";
						  }
						}
					   
					}
			  }
			  } else  {

				  if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
					    System.out.println("cell type : " + cell.getCellType());
					    loginTime = cell.getStringCellValue();
					  
					  
					  if(loginTime ==null||loginTime.trim().equalsIgnoreCase("")||loginTime.trim().equalsIgnoreCase("none")||loginTime.trim().equalsIgnoreCase("nill")||loginTime .trim().equalsIgnoreCase("null")||loginTime .trim().equalsIgnoreCase("cancel")|| loginTime .trim().equalsIgnoreCase("off" ) ||loginTime.trim().equalsIgnoreCase("weekly off")) {
						  loginTime = "none";
						}  
						
						 else {
								if(loginTime.equalsIgnoreCase("invalid"))
								{
									
								} else {
									  String time = OtherFunctions.TimeFormat24Hr(loginTime );
									  if(time!=null) {
										  loginTime  = time;
									  } else {
										  /*message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
										  error=true;*/
										  System.out.println("Error in login format");
										  loginTime = "error";
									  }
								}
						}
				  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					  loginTime  = "none";
				  }
				   
			  }
		return loginTime;
		   

		
	}
	
	//-------------
	
	private String getLogoutTime( Cell cell) {
		String logoutTime=null;
		if(cell.getCellType()==Cell.CELL_TYPE_NUMERIC && DateUtil.isCellDateFormatted(cell)) {
			  System.out.println("login  time : " +cell.getDateCellValue());
				  Date date = cell.getDateCellValue();

			        SimpleDateFormat formatTime = new SimpleDateFormat("HH:mm");
			        String dateStamp = formatTime.format(date);
			        logoutTime= dateStamp;

			  //System.out.println(" login time : " + dto.getLogoutTime());
			  System.out.println("type : "+ cell.getCellType());
			  try {
				  String split[] = logoutTime.split(":");
				  if(split[0].length()<2) {
					  split[0]="0"+split[0];
				  }
				  if(split[1].length()<2) {
					  split[1]="0"+split[0];
				  }
				  logoutTime = split[0]+":" + split[1];
			  }catch(Exception e) {
				  if(logoutTime ==null||logoutTime.trim().equalsIgnoreCase("")||logoutTime.trim().equalsIgnoreCase("none")||logoutTime.trim().equalsIgnoreCase("nill")||logoutTime.trim().equalsIgnoreCase("null")||logoutTime.trim().equalsIgnoreCase("invalid") ||logoutTime.trim().equalsIgnoreCase("weekly off")) {
					  logoutTime="none";
					} else {
						if(logoutTime.equalsIgnoreCase("invalid"))
						{
							
						} else {
								  String time = OtherFunctions.TimeFormat24Hr(logoutTime );
								  if(time!=null) {
									  logoutTime = time;
								  } else {
				/*					  message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
									  error=true;
									  */
									  System.out.println(" Error in login format");
									  logoutTime="Error";
								  }
						}	   
					}
			  }
			  } else  {

				  if(cell.getCellType()==Cell.CELL_TYPE_STRING) {   
					    System.out.println("cell type : " + cell.getCellType());
					    logoutTime = cell.getStringCellValue();
					  
					  
					  if(logoutTime ==null||logoutTime.trim().equalsIgnoreCase("")||logoutTime.trim().equalsIgnoreCase("none")||logoutTime.trim().equalsIgnoreCase("nill")||logoutTime .trim().equalsIgnoreCase("null")||logoutTime .trim().equalsIgnoreCase("cancel")|| logoutTime .trim().equalsIgnoreCase("off")||logoutTime .equalsIgnoreCase("invalid")|| logoutTime.trim().equalsIgnoreCase("weekly off") ) {
						  logoutTime = "none";
						}  
						
						 else {
								if(logoutTime.equalsIgnoreCase("invalid"))
								{
									
								} else {
									  String time = OtherFunctions.TimeFormat24Hr(logoutTime );
									  if(time!=null) {
										  logoutTime  = time;
									  } else {
										  /*message=message + "|" + "Login Time :incorrect format (row:" + rowIndex+" )";
										  error=true;*/
										  System.out.println("Error in login format");
										  logoutTime = "error";
									  }
								}
						}
				  } else if(cell.getCellType()==Cell.CELL_TYPE_BLANK) {
					  logoutTime  = "none";
				  }
				   
			  }
		return logoutTime;
		   

		
	}
	
	
	public XSSFWorkbook downloadHorizantalScheduleTemplate(String fromDate, String toDate, String site) {
		System.out.println(" .................. ");
		
		LinkedHashMap<String, ArrayList<SchedulingDto>> dtoMap = new SchedulingDao().getScheduleOfEmpForDownload(fromDate,toDate , site);
		//System.out.println(" Dto List : " + dtoList.size());
		for(String key : dtoMap.keySet()) {
			ArrayList<SchedulingDto> ourDtoList = dtoMap.get(key);
			for( SchedulingDto sdto : ourDtoList ) {
			System.out.println("Schedule : " + sdto);
			System.out.println("\t.........");
			for(ScheduleAlterDto sadto :  sdto.getAlterList() ) {
				System.out.println("Child  : " + sadto);
			}
		}
		}
		ArrayList<SchedulingDto> dtoList =getAllInBetweenInAlterList(fromDate, toDate, dtoMap);
	/*	for(SchedulingDto dto : dtoList) {
			
		System.out.print("" + dto.getEmployeeId() + "\t" + dto.getEmployeeName() );
//		  
		for(ScheduleAlterDto dtoAlterDto : dto.getAlterList() ) {
			 System.out.print(dtoAlterDto.getDate() + " (" + dtoAlterDto.getLoginTime() + ", " + dtoAlterDto.getLogoutTime() + " ) ");
		} 
		  System.out.println(); 
		}*/
		System.out.println("having schedule : "+ dtoList.size());
		
		ArrayList<SchedulingDto> dtoListNoSchedule = new SchedulingDao().getEmployeesHavingNoSchedule (fromDate,toDate , site);
		System.out.println("having no schedule : "+ dtoListNoSchedule.size());
		dtoList.addAll(dtoListNoSchedule);
		return writeSchedulesToExcel(dtoList);

	}
	
	
	public XSSFWorkbook writeSchedulesToExcel (ArrayList<SchedulingDto> dtoList) {
		
		//HSSFWorkbook workbook = new HSSFWorkbook();
		XSSFWorkbook workbook = new XSSFWorkbook();
		//HSSFSheet sheet = workbook.createSheet();
		XSSFSheet sheet = workbook.createSheet();
		
		
		int rownum=0;
		if(dtoList!=null && dtoList.size() >0) {
			SchedulingDto dto = dtoList.get(rownum);
			int colnum =0;
			//HSSFRow headerRow = sheet.createRow(rownum);
			XSSFRow headerRow = sheet.createRow(rownum);
			 
			headerRow.setRowStyle(workbook.createCellStyle() );
			
			//headerRow.getRowStyle().setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
			//headerRow.getRowStyle().setFillBackgroundColor(HSSFColor.YELLOW.index);
			headerRow.getRowStyle().setAlignment(HSSFCellStyle.ALIGN_CENTER);
//	 		HSSFFont font = workbook.createFont();
	 		XSSFFont font = workbook.createFont();
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
			headerRow.getRowStyle().setFont(font); 
			
//			sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
			sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
//			HSSFCell cell1  = headerRow.createCell(colnum++);
			XSSFCell cell1  = headerRow.createCell(colnum++);
			
			cell1.setCellStyle(headerRow.getRowStyle() );
			cell1.setCellValue ("Emp#");
//			sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
			sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
//			HSSFCell cell2  = headerRow.createCell(colnum++);
			XSSFCell cell2  = headerRow.createCell(colnum++);
			
			cell2.setCellValue ("DisplayName");
			cell2.setCellStyle(headerRow.getRowStyle() );
//			sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
			sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum ));
			
//			HSSFCell cell3  = headerRow.createCell(colnum++);
			XSSFCell cell3  = headerRow.createCell(colnum++);
			
			cell3.setCellStyle(headerRow.getRowStyle() );
			cell3.setCellValue ("Project");
//			sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
			sheet.addMergedRegion( new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum));
			
//			HSSFCell cell4  = headerRow.createCell(colnum++);
			XSSFCell cell4 = headerRow.createCell(colnum++);
			
			cell4.setCellStyle(headerRow.getRowStyle() );
			cell4.setCellValue ("Subscription Effective Date");
			
//			sheet.addMergedRegion(new CellRangeAddress(rownum,rownum+1,colnum,colnum));
			sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum+1,colnum,colnum ));
//			HSSFCell cell5  = headerRow.createCell(colnum++);
			XSSFCell cell5  = headerRow.createCell(colnum++);
			 
			cell5.setCellStyle(headerRow.getRowStyle() );
			cell5.setCellValue ("End Date");
			 
//	System.out.println(dto.getAlterList().size() + " : size of first alter list ");
			
			for(ScheduleAlterDto adto : dto.getAlterList()) {
				
				
//				HSSFDataFormat dataFormat = workbook.createDataFormat();
				XSSFDataFormat dataFormat = workbook.createDataFormat();
//				HSSFCell cellDate  = headerRow.createCell(colnum++);
				XSSFCell cellDate  = headerRow.createCell(colnum++);
//				HSSFCellStyle cellStyle= workbook.createCellStyle();
				XSSFCellStyle cellStyle= workbook.createCellStyle();
				
				cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
				cellStyle.setDataFormat(dataFormat.getFormat("yyyy-mm-dd"));
				cellStyle.setFont(font);
				//cellStyle.setFillBackgroundColor(HSSFColor.YELLOW.index);
			//	cellStyle.setFillPattern(HSSFCellStyle.SOLID_FOREGROUND);
				
				cellDate.setCellValue ( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate( OtherFunctions.changeDateFromat( adto.getDate())));
				 
				//sheet.addMergedRegion(new CellRangeAddress(rownum,rownum,colnum-1,colnum));
				sheet.addMergedRegion(new org.apache.poi.ss.util.CellRangeAddress(rownum,rownum,colnum-1,colnum));
				colnum++;
				//cellDate.setCellStyle(headerRow.getRowStyle() );
				cellDate.setCellStyle(cellStyle);
				cellDate.setCellType(HSSFCell.CELL_TYPE_NUMERIC );
				
			}
			 
			rownum++;
			colnum=5;
//			 HSSFRow headerRow1 = sheet.createRow(rownum);
			XSSFRow headerRow1 = sheet.createRow(rownum);
			 headerRow1.setRowStyle( headerRow.getRowStyle());
			 for(ScheduleAlterDto adto : dto.getAlterList()) {
			
//				HSSFCell cell11  = headerRow1.createCell(colnum++);
				 XSSFCell cell11  = headerRow1.createCell(colnum++);
					cell11.setCellStyle(headerRow1.getRowStyle() );
				cell11.setCellValue ("Login");
				
//				HSSFCell cell12 = headerRow1.createCell(colnum++);
				XSSFCell cell12 = headerRow1.createCell(colnum++);
				cell12.setCellStyle(headerRow1.getRowStyle() );
				cell12.setCellValue ("Logout");
			
			}
			 
			 for(SchedulingDto sdto : dtoList) {
				 rownum++;
//				 HSSFRow dataRow = sheet.createRow(rownum);
				 XSSFRow dataRow = sheet.createRow(rownum);
				 colnum=0;
//				 HSSFCell dataCellEmployeeId = dataRow.createCell(colnum++);
				 XSSFCell dataCellEmployeeId = dataRow.createCell(colnum++);
				 dataCellEmployeeId.setCellValue(sdto.getEmployeeId());
				 
//				 HSSFCell dataCellEmployeeName = dataRow.createCell(colnum++);
				 XSSFCell dataCellEmployeeName = dataRow.createCell(colnum++);
				 dataCellEmployeeName.setCellValue(sdto.getEmployeeName() );
				 
//				 HSSFCell dataCellProject = dataRow.createCell(colnum++);
				 XSSFCell dataCellProject = dataRow.createCell(colnum++);
				 dataCellProject.setCellValue(sdto.getProject() );
				 
//				 HSSFCell dataCellSubscriptionEffectiveDate = dataRow.createCell(colnum++);
				 XSSFCell dataCellSubscriptionEffectiveDate = dataRow.createCell(colnum++);
				 dataCellSubscriptionEffectiveDate.setCellValue(sdto.getSubscriptionDate() );
				 
//				 HSSFCell dataCellSubscriptionToDate = dataRow.createCell(colnum++);
				 XSSFCell dataCellSubscriptionToDate = dataRow.createCell(colnum++);
				 dataCellSubscriptionToDate.setCellValue(sdto.getSubscriptionToDate() );
				 
//				 System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
				 for(ScheduleAlterDto adto:  sdto.getAlterList()) {
//					 System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
					 if(adto.getLogoutTime()==null) {
						 adto.setLogoutTime("none");
					 }
					 if(adto.getLoginTime()==null) {
						 adto.setLoginTime("none");
					 }
//					 HSSFDataFormat dataFormat = workbook.createDataFormat();
					 XSSFDataFormat dataFormat = workbook.createDataFormat();
//					 HSSFCellStyle cellStyle= workbook.createCellStyle();
					 XSSFCellStyle cellStyle= workbook.createCellStyle();
					 cellStyle.setAlignment(HSSFCellStyle.ALIGN_CENTER);
					 cellStyle.setDataFormat(dataFormat.getFormat("[h]:mm:ss;@"));
					  
					 
//					 HSSFCell dataCell = dataRow.createCell(colnum++);
					 XSSFCell dataCell = dataRow.createCell(colnum++);
					 dataCell.setCellValue(adto.getLoginTime());
//					 System.out.println( sdto.getEmployeeName() + " (" + rownum + ":" + colnum + ")......AAA.........." + adto.getLoginTime() + " " + adto.getLogoutTime()); 
					  if(adto.getLoginTime().equalsIgnoreCase("INVALID") ) {
						  
						 //CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum-1, colnum-1);
						   org.apache.poi.ss.util.CellRangeAddressList addressList = new org.apache.poi.ss.util.CellRangeAddressList(rownum, rownum, colnum-1, colnum-1); 
						// DVConstraint dvConstraint =  DVConstraint.createExplicitListConstraint( new String[] {""});
						   //CTDataValidation = CTDataValidationImpl
//						 CellReference cr =  new  CellReference(dataCell.getRowIndex(), dataCell.getColumnIndex());
//						 System.out.println(cr.toString());
						 
						 //DVConstraint dvConstraint =  DVConstraint.createCustomFormulaConstraint("(" + cr.formatAsString()  +"='l'");
						 
//						 HSSFDataValidation dv = new HSSFDataValidation( addressList, dvConstraint);
						 //XSSFDataValidation dv = new XSSFDataValidation( addressList, dvConstraint  );
						// sheet.addValidationData(dv);
					 }
					   
			 		  
//					 HSSFCell dataCel2 = dataRow.createCell(colnum++);
					  XSSFCell dataCel2 = dataRow.createCell(colnum++);
					 dataCel2.setCellValue(adto.getLogoutTime());
					 

			 		 if(adto.getLogoutTime().equalsIgnoreCase("INVALID") ) {
						  
						/* CellRangeAddressList addressList = new CellRangeAddressList(rownum, rownum, colnum-1, colnum-1);
						 DVConstraint dvConstraint =  DVConstraint.createExplicitListConstraint( new String[] {""});
						 HSSFDataValidation dv = new HSSFDataValidation( addressList, dvConstraint);
						 sheet.addValidationData(dv);*/
					 }
			 	 }
			//	 System.err.println();
			 }
			 /*
			try   
	        {  
	            FileOutputStream out =  
	            new FileOutputStream  
	                (new File("D:\\CrossDomain\\ScheduleTemplateGeneated.xlsx"));  
	            workbook.write(out);  
	            out.close();  
	            System.out.println  
	                ("Excel written successfully..");
	            
	             
	    	// 	SchedulingService.uploadScheduleHorizoantalFile(in);
	        }   
	        catch (FileNotFoundException e)   
	        {  
	            e.printStackTrace();  
	        }   
	        catch (IOException e)   
	        {  
	            e.printStackTrace();  
	        }  
	       
*/
		
			 	
			
		}
		 
		return workbook;
		
	}

public static ArrayList<SchedulingDto> getAllInBetweenInAlterList(String fromDate, String toDate, LinkedHashMap<String, ArrayList<SchedulingDto>> dtoMap) {
		
		Calendar cal = Calendar.getInstance();
//		Calendar calT = Calendar.getInstance();
		Calendar calT1 = Calendar.getInstance();
		cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
		Calendar calEnd = Calendar.getInstance();
		Calendar calShEnd = Calendar.getInstance();
		 //calEnd.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingToDate())));
		 calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
		 ArrayList<SchedulingDto> dtoList=null; 
		System.out.println("Cal at start : "+ OtherFunctions.changeDateFromat(  cal.getTime())  );
		//SchedulingDto dto = dtoList.get(0);
		ArrayList<SchedulingDto> tempDtoList = new ArrayList<SchedulingDto>();
		Set<String> keySet = dtoMap.keySet();
		System.out.println(keySet.size() + " key size ");
		for(String key : keySet) {
			dtoList = dtoMap.get(key);
		
			cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate));
		 
//			System.out.println("Size : "+ dtoList.size());
			
			SchedulingDto preShDto = null;
			SchedulingDto dtoTemp = null;
			 Calendar calSubStart = Calendar.getInstance();
				Calendar calSubEnd = Calendar.getInstance();
				 System.out.println("List size>>>>>>>>>>>>>>>>>>"+dtoList.size());
				 int cnt=0;
			for(SchedulingDto dto : dtoList) 
				{
				System.out.println("Series <<<<<"+cnt);
				cnt++;
				
				if(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingToDate()  ) ).compareTo(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate))<0)
				{
					calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingToDate()  ) ));	
				}	
				else
				{
				calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
				}
				System.out.println("1.....schedule Id "+dto.getScheduleId());
					calSubStart.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy( dto.getSubscriptionDate() )));
					calSubEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  dto.getSubscriptionToDate() )));
				calT1.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingFromDate()  ) ) );
				System.out.println("value to schend "+OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingToDate()  ) ) );
				calShEnd.setTime( OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromat( dto.getSchedulingToDate()  ) ) );
				System.out.println("sh end "+calShEnd.getTime());
				
				if( (preShDto!=null && preShDto.getProject().equals(dto.getProject())) ==false) {
					System.out.println("if 111111111");
					
					if(dtoTemp!=null ) {
//						System.out.print(   dtoTemp.getEmployeeId() +"\t" + dtoTemp.getEmployeeName() +"\t" );
		/*				for(ScheduleAlterDto adto : dtoTemp.getAlterList()) {
							System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
						}*/
						cal.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(fromDate)); 	
						tempDtoList.add(dtoTemp);
//						System.out.println(" tempAddded");
					}
//					System.out.println(dtoTemp+ " dtoTemp initiated");
					dtoTemp = new SchedulingDto();
					dtoTemp.setEmployeeId(dto.getEmployeeId());
					dtoTemp.setEmployeeName(dto.getEmployeeName());
					dtoTemp.setProject(dto.getProject());
					dtoTemp.setScheduledFromDate(fromDate);
					dtoTemp.setScheduledToDate(toDate);
					 
					dtoTemp.setSubscriptionDate(dto.getSubscriptionDate());
					 
					dtoTemp.setSubscriptionToDate(dto.getSubscriptionToDate());
					
					
				}
				System.out.println("2.....schedule Id "+dto.getScheduleId());
				if(cal .compareTo(calT1)<0 && cal.compareTo(calEnd)<=0) {
					
					System.out.println("if 2222222222");
					
//				System.out.println(" inside interm.");
					while(cal .compareTo(calT1)<0 && cal.compareTo(calEnd)<=0) {
//				System.out.println(cal.getTime());		

						 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
						 
							if(cal.compareTo(calSubStart)<0  )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
							{
								System.out.println("kerunnnnnnnnnnnnnnnnnnnnnnu");
								alterDto .setLoginTime("off");
								 alterDto .setLogoutTime("off");
							} else if(cal.compareTo(calSubEnd)>0 )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							}
						 
							dtoTemp.getAlterList() .add(alterDto);
						 cal.add(Calendar.DATE, 1);
					}
				//	tempDtoList.add(dtoTemp);
				}
				System.out.println("3.....schedule Id "+dto.getScheduleId());	
				
			//		System.out.print(   dto.getEmployeeId() +"\t" + dto.getEmployeeName() +"\t" );
					//System.out.print( dto.getSchedulingFromDate());
				//	cal .setTime(  OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(dto.getSchedulingFromDate())));
				
					//System.out.println("...cal1 : ." + cal);
					 Calendar cal1 = Calendar.getInstance();
					 ArrayList<ScheduleAlterDto> raList = new ArrayList<ScheduleAlterDto>();
//					 System.out.println(" alter ...");
					for(ScheduleAlterDto adto : dto.getAlterList()) {
						System.out.println("Alter ");
					 
						 cal1.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(OtherFunctions.changeDateFromatToddmmyyyy(  adto.getDate())));
					 	 
//						  System.out.println("cal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) +" cal1 : " + OtherFunctions.changeDateFromat(  cal1.getTime() ) + " compare : " + cal.compareTo(cal1));
//						  System.out.println("calShEnd time : " + OtherFunctions.changeDateFromat(  calShEnd.getTime()) +" calEnd : " + OtherFunctions.changeDateFromat(  calEnd.getTime() ) + " compare : " + cal.compareTo(calEnd));
						 while(cal.compareTo(cal1)<0 && cal.compareTo(calShEnd)<=0 && cal.compareTo(calEnd)<=0 ) {
						 	 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
							 
							 ScheduleAlterDto alterDto = new ScheduleAlterDto();
							 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
								if(cal.compareTo(calSubStart)<0  )
								{
									
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
								{
									System.out.println("Login<<"+ dto.getLoginTime() + ">> Logout<<"+ dto.getLogoutTime() + ">> ");
									alterDto .setLoginTime(dto.getLoginTime());
									 alterDto .setLogoutTime(dto.getLogoutTime());
								} else if(cal.compareTo(calSubEnd)>0 )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								}
							 
						 
								dtoTemp.getAlterList().add(alterDto);
							 cal.add(Calendar.DATE, 1);
							 
						 }
						 
						 if(cal.compareTo(calShEnd)<=0 && cal.compareTo(calEnd)<=0) {
					 		 System.out.print(OtherFunctions.changeDateFromat( "Alt "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
							 ScheduleAlterDto alterDto = new ScheduleAlterDto();
							 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
								if(cal.compareTo(calSubStart)<0  )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
								{
									System.out.println("Login<<"+ adto.getLoginTime() + ">> Logout<<"+ adto.getLogoutTime() + ">> ");
									alterDto .setLoginTime(adto.getLoginTime());
									 alterDto .setLogoutTime(adto.getLogoutTime());
								} else if(cal.compareTo(calSubEnd)>0   && cal.compareTo(calEnd) <=0 )
								{
									alterDto.setLoginTime("INVALID");
									alterDto.setLogoutTime("INVALID");
								}
							 
							 
							 //raList.add(alterDto);
							dtoTemp.getAlterList().add(alterDto);
							 cal.add(Calendar.DATE, 1);
							 
						 }
				
						
					}
					System.out.println("4.....schedule Id "+dto.getScheduleId());
				 	  System.out.println("cal1 "+cal1.getTime());
					while(cal.compareTo(cal1)>0     && cal.compareTo(calEnd) <=0 ) {
						System.out.println("cal time "+cal.getTime()+"   cal1 time "+cal1.getTime()+"  cal end  "+calEnd.getTime());
					 	 System.out.print(OtherFunctions.changeDateFromat(  OtherFunctions.changeDateFromat(  cal.getTime() ) ) +"\t" +  dto.getLoginTime() +"\t" +  dto.getLogoutTime()  +"\t");
						 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
						
							if(cal.compareTo(calSubStart)<0  )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
							{
								if(cal.compareTo(calShEnd)<=0 ) {
									 
									 alterDto .setLoginTime(dto.getLoginTime());
									 alterDto .setLogoutTime(dto.getLogoutTime());
								} else {
									System.out.println("kerunnnnnnnnnnnnnnnnnnnnnnu22222222222222222"+cal.getTime()+" schedule Id "+dto.getScheduleId()+"  "+calShEnd.getTime());
									 alterDto .setLoginTime("off");
									 alterDto .setLogoutTime("off");
								}
//								System.out.println("Login<<"+ alterDto . getLoginTime() + ">> Logout<<"+ alterDto .getLogoutTime() + ">> ");
							} else if(cal.compareTo(calSubEnd)>0  && cal.compareTo(calEnd) <=0  )
							{
								//System.out.println("Last __>" + calSubEnd.getTime());
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							}
					
					
						 
						 //raList.add(alterDto);
							dtoTemp.getAlterList() .add(alterDto);
//						  System.out.println("\ncal time : " + OtherFunctions.changeDateFromat(  cal.getTime()) + "cal1 time : " + OtherFunctions.changeDateFromat(  cal1.getTime()) + " calend : " + OtherFunctions.changeDateFromat(  calEnd.getTime() ) + " compare2 : " + cal.compareTo(calEnd) + " compare 1 : " + cal.compareTo(cal1));
						 cal.add(Calendar.DATE, 1);
					}
					System.out.println("5.....schedule Id "+dto.getScheduleId());
					//dto.setAlterList(raList);
				//	System.out.println();
				
				preShDto = dto;
				}
			calEnd.setTime(OtherFunctions.changeDateFromat_ddmmyyy_to_UtilDate(toDate));
			    if(cal.compareTo(calEnd)<=0) {
			    	while(cal.compareTo(calEnd)<=0) {
			    		
			    		 ScheduleAlterDto alterDto = new ScheduleAlterDto();
						 alterDto .setDate( OtherFunctions.changeDateFromat(  cal.getTime() ));
							if(cal.compareTo(calSubStart)<0  )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							} else if(cal.compareTo(calSubStart)>=0 && cal.compareTo(calSubEnd) <=0  )
							{
								System.out.println("kerunnnnnnnnnnnnnnnnnnnnnnu333333333333333333333");
								alterDto .setLoginTime("off");
								 alterDto .setLogoutTime("off");
								 System.out.println("Login<<"+ alterDto . getLoginTime() + ">> Logout<<"+ alterDto .getLogoutTime() + ">> ");
							} else if(cal.compareTo(calSubEnd)>0 )
							{
								alterDto.setLoginTime("INVALID");
								alterDto.setLogoutTime("INVALID");
							}
						 
							dtoTemp.getAlterList() .add(alterDto);
						 cal.add(Calendar.DATE, 1);
			    	}
			    	
			    }
//			System.out.println("-------------------------------");
			if(dtoTemp!=null ) {
	/*			System.out.print(   dtoTemp.getEmployeeId() +"\t" + dtoTemp.getEmployeeName() +"\t" );
				for(ScheduleAlterDto adto : dtoTemp.getAlterList()) {
					System.out.print(OtherFunctions.changeDateFromat( "  "+ adto.getDate()) +"\t" + adto.getLoginTime() +"\t" + adto.getLogoutTime()  +"\t");
				}
	*/			
				tempDtoList.add(dtoTemp);
//				System.out.println(" tempAddded");
			}
			
		}
		return tempDtoList; 
	}



public int uploadHorizontalSchedule(InputStream inputStream, String site, String changedBy) {
	// TODO Auto-generated method stub
	int val=0;
	System.out.println("inside uploadSchedule");
	try {
	ArrayList<SchedulingDto>  dtoList = uploadScheduleHorizoantalFile(inputStream);
	if(getMessageList().size()>0) {
		message = "Validation Failure ";
		System.out.println("Error : "+message);
		 
	}else {
		if(dtoList!=null && dtoList.size()>0) {
			System.out.println(" uploadd cheyyalle ?");
			SchedulingDao schDao = new SchedulingDao();
			val = schDao.uploadSchedule(dtoList, changedBy);
			setMessage(schDao.getMessage());
			setMessageList(schDao.getMessageList());
			System.out.println("_____________________________");
		//	printSchedule(dtoList);
			
		}
	}
	} catch(Exception e) {
		System.out.println(" Error ");
	}
	return val;
}

public int uploadSchedule(ArrayList<SchedulingDto> dtoList, String changedBy)   {
	int result=0;
	result = new SchedulingDao().uploadSchedule(dtoList, changedBy);
	if(SettingsConstant.emailForScheduleEmployee.equalsIgnoreCase("yes") && result>0) {
		for(SchedulingDto dto : dtoList) {
		sendScheduledMessageToEmployee(dto);
		}
	}

	return result;
}


	
	}
