/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.agiledge.atom.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.EmployeeDao;
import com.agiledge.atom.dao.SettingsDoa;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.EmployeeDto;
import com.agiledge.atom.mail.SendMailFactory;
import com.agiledge.atom.transporttype.dto.TransportTypeDto;
import com.agiledge.atom.usermanagement.dao.ViewManagementDao;



/**
 * 
 * @author 123
 */
public class EmployeeService {

	private EmployeeDao dao = new EmployeeDao();

	private String message="";
	public boolean hasPrivilegeToChangeAddress(String empid) {
		return dao.hasPrivilegeToChangeAddress(empid);
	}


	public EmployeeDto getEmployeeAccurate(String empid) {
		return dao.getEmployeeAccurate(empid);
	}	
	public EmployeeDto getEmployeeAccurate(String empid,String role) {
		return dao.getEmployeeAccurate(empid,role);
	}

	public List<EmployeeDto>  getProjectEmployees(EmployeeDto dto) {
		
		return dao. getProjectEmployees(dto);
	}
	public List<EmployeeDto> searchEmployee(EmployeeDto dto) {

		return dao.searchEmployee(dto);
	}

	public List<EmployeeDto> searchEmployeeUnderManager(EmployeeDto dto) {

		return dao.searchEmployeeUnderManager(dto);
	}

	public List<EmployeeDto> searchEmployee(EmployeeDto dto, char type) {
		if (type == 'A') {
			return dao.searchEmployee(dto);
		} else if (type == 'V') {
			//return dao.searchEmployeeVendor(dto);
			return dao.searchEmployee(dto);
		} else if (type == 'S') {
			return dao.searchEmployeeSecurity(dto);
		} else {
			//return null;
			return dao.searchEmployee(dto);
		}
	}

	public List<EmployeeDto> searchEmployeeForSpoc(EmployeeDto dto) {
		System.out.println("dfsd");
		 
		return dao.searchEmployeeForSpoc(dto);
	}

	public int updatemployeesRole(String[] empids,String role,String doneBy) {
		// TODO Auto-generated method stub
		int value= dao.updateEmployeesRole(empids, role,doneBy);
		if(value>0)
		{
			sendMailToRoleChangedEmployees(empids,role);
		}
		return value;

	}
	public boolean checkLoginId(String loginid)
	{
		return new EmployeeDao().checkLoginId(loginid);
	}
	
	public boolean checkPersonnelNo(String personnelNo)
	{
		return new EmployeeDao().checkPersonnelNo(personnelNo);
	}
	
	public void sendMailToRoleChangedEmployees(String []empids, String role)
	{
		String roleName=OtherFunctions.getRoleNamebyId(Integer.parseInt(role));
		try{
			
		
			for(String employeeId: empids)
			{
			 EmployeeDto dto =new EmployeeDao().getEmployeeAccurate(employeeId);
			 String message ="Hi " + dto.getDisplayName() + ", <br/><br/>Your role in ATOm application has been changed to " + roleName +". <br/><br/> <a href='" +  new SettingsDoa().getDomainName() + "'> Click here to login</a>  "
						+ "<br/><br/>Regards,<br/>Transport Team<br/>---------------<br/> <i><u>Note:</u> This is a system generated email. Please do not reply.</i>";
			 String title="Role Changed";
			 SendMailFactory.getMailInstance().send(dto.getEmailAddress(),title, message);
			}
		}catch(Exception e )
		{
			System.out.println(" Exception " + e);
		}
	}

	public List<EmployeeDto> searchEmployeeFromSubscription(EmployeeDto dto) {
		return dao.searchEmployeeFromSubscription(dto);
	}
	public List<EmployeeDto> searchEmployeeFromSubscription(String site) {
		return dao.searchEmployeeFromSubscription(site);
	}
	public EmployeeDto getSpoc(String empid) {
		return dao.getSpoc(empid);
	}
	
	public int UpdateSetSpecial(String[] empids,String setattrib) {
		// TODO Auto-generated method stub
		int value= dao.UpdateSetSpecial(empids, setattrib);
		return value;

	}
	public EmployeeDto getManager(String empid) {
		return dao.getManager(empid);
	}

	public String[] getEmails(ArrayList<EmployeeDto> spocDelegates) {
		String emails[]=new String[spocDelegates.size()];
		if(spocDelegates!=null&&spocDelegates.size()>0)
		{	int i=0;
		for(EmployeeDto dto : spocDelegates)
		{
			emails[i]= dto.getEmailAddress();
			i++;
		}
		}
		return emails;
	}
	
	public  int insertExternalEmployee(EmployeeDto dto)
	{
		return new EmployeeDao().insertExternalEmployee(dto);
	}

	
	public ArrayList<EmployeeDto> getAllExternalemployees()
	{
		return new EmployeeDao().getAllExternalemployees();
	}
	public ArrayList<EmployeeDto> getemployeeProjectInfo()
	{
		return new EmployeeDao().getemployeeProjectInfo();
	}
	public ArrayList<EmployeeDto> getEnabledExternalemployees()
	{
		return new EmployeeDao().getEnabledExternalemployees();
		
	}
	
	
	public int insertExternalEmployeefromexcel(ArrayList<EmployeeDto> list, String site, String overwrite, String sendMail) {
		EmployeeDao employeeDao = new EmployeeDao();
		 int value=employeeDao.insertExternalEmployeefromexcel(list, site, overwrite, sendMail);
		 if(value==0) {
				setMessage("No employees are inserted. Error :"+ employeeDao.getMessage());
			} else if(value<=list.size()) {
				setMessage(employeeDao.getMessage());
			 
			 
			}
		 return value;
	}
	public int updateExternalEmployeefromexcel(ArrayList<EmployeeDto> list, String site, String overwrite) {
		EmployeeDao employeeDao = new EmployeeDao();
		 int value=employeeDao.updateExternalEmployeefromexcel(list, site, overwrite);
		 if(value==0) {
				setMessage("No employees were updated. Error :"+ employeeDao.getMessage());
			}else if(value<=list.size()) {
				setMessage(value+" employees were updated");
			 
			 
			}
		 return value;
	}
	
	
	
	public int updateStatus(String empid,int active) {
		return new EmployeeDao().updateStatus(empid, active);
	}
	
	public int UpdatePassword(int empid,String password)
	{
		return new EmployeeDao().UpdatePassword(empid, password);
	}
	public int UpdatePassword(String email,String password)
	{
		return new EmployeeDao().UpdatePassword(email, password);
	}
	
	public int UpdateEmployee(EmployeeDto dto)
	{
		return new EmployeeDao().UpdateEmployee(dto);
	}
/*
	public int registerEmployee(ArrayList<TransportTypeDto> list) {
		return new EmployeeDao().registerEmployee(list);
		
	}
	*/
	public int registerEmployee(String empId) {
		return new EmployeeDao().registerEmployee(empId);
		
	}

	
	/* get landmark of employee from employee id
	 * 
	 */
	
	public APLDto getLandMark(String employeeId) {
		APLDto aplDto = new APLDto();
		aplDto.setLandMark("Arginan Sakshi");
		aplDto.setLandMarkID("1");
		aplDto.setArea("Aknisakshi");
		aplDto.setAreaID("1");
		aplDto.setPlace("Bhargavee nilayam");
		aplDto.setPlaceID("2");
		return aplDto;
				 
		
	}
	public List<EmployeeDto> searchspoc(EmployeeDto dto,Long man_id) {
		 
		return dao.searchspoc(dto,man_id);
	}
	
	public int processXLFile(String saveFile, String site, String overwrite, String sendMail) {
		int returnint=0;
		try {
			boolean flag = false;
			System.out.println("track......1");
			ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
			System.out.println("track......2");
			FileInputStream file = new FileInputStream(new File(saveFile));
			System.out.println("track......3");
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			System.out.println("track......4");
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("track......5");
			Iterator<Row> rowIterator = sheet.iterator();
			System.out.println("track......6");
			rowIterator.next();
			System.out.println("track......7");
			while (rowIterator.hasNext()) {
				EmployeeDto dto = new EmployeeDto();
				if (flag)
					break;

				Row row = rowIterator.next();
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if (cell.getColumnIndex() == 0) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

					}
					if (cell.getColumnIndex() != 8) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						System.out.println( ""+cell.getColumnIndex() + "  " +  cell.getStringCellValue() );
					} 
	

					if (cell.getColumnIndex() == 0) {
						dto.setPersonnelNo(cell.getStringCellValue());

					} else if (cell.getColumnIndex() == 1) {
						dto.setEmployeeFirstName(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 2) {
						dto.setEmployeeMiddleName(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 3) {
						dto.setEmployeeLastName(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 4) {
						dto.setDisplayName(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 5) {
						dto.setGender(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 6) {
						dto.setContactNo(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 7) {
						dto.setEmailAddress(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 8) {
		 				dto.setDateOfJoining(OtherFunctions
								.changeDateFromatToSqlFormat(cell
										.getDateCellValue())); 
					} else if (cell.getColumnIndex() == 9) {
						dto.setAddress(cell.getStringCellValue());
						System.out.println("9 th col : "+ cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 10) {
						dto.setLoginId(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 11) {
						dto.setUserType(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 12) {
						dto.setAuthtype(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 13) {
						dto.setContract(cell.getStringCellValue());
					} else if (cell.getColumnIndex() == 14) {
						dto.setLineManager(cell.getStringCellValue());
					} else if(cell.getColumnIndex() == 15) {
						dto.setRoutingType(cell.getStringCellValue());
						  
					} else if(cell.getColumnIndex() == 16) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProjectCode(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProjectCode())) {
								dto.setProjectCode(null);
							}
						}
					} else if(cell.getColumnIndex() == 17) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProject(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProject ())) {
								dto.setProject (null);
							}
						}
					} else if(cell.getColumnIndex() == 18) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProjectUnit(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProjectUnit())) {
								dto.setProjectUnit(null);
							}
						}
					} 

				}
				if (!flag)
					list.add(dto);

			}

/*			for(EmployeeDto dto : list) {
				System.out.println("login id  :"+ dto.getLoginId() + " name :"+ dto.getDisplayName());
			}*/
			returnint = this.insertExternalEmployeefromexcel(list, site, overwrite, sendMail);
			
			System.out.println("returnint" + returnint);
			file.close();
			
		}

		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			setMessage("Unable to read file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			setMessage(""+ e);
		}
		return returnint;
	}
	public int processEXCELFile(String saveFile, String site, String overwrite) {
		int returnint=0;
		try {
			boolean flag = false;
			System.out.println("track......1");
			ArrayList<EmployeeDto> list = new ArrayList<EmployeeDto>();
			System.out.println("track......2");
			FileInputStream file = new FileInputStream(new File(saveFile));
			System.out.println("track......3");
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			System.out.println("track......4");
			XSSFSheet sheet = workbook.getSheetAt(0);
			System.out.println("track......5");
			Iterator<Row> rowIterator = sheet.iterator();
			System.out.println("track......6");
			rowIterator.next();
			System.out.println("track......7");
			while (rowIterator.hasNext()) {
				EmployeeDto dto = new EmployeeDto();
				if (flag)
					break;

				Row row = rowIterator.next();
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					Cell cell = cellIterator.next();

					if (cell.getColumnIndex() == 0) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

					}
					/*if (cell.getColumnIndex() != 8) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						System.out.println( ""+cell.getColumnIndex() + "  " +  cell.getStringCellValue() );
					}*/ 
					/*<td align="left"><%=employeeDto.getPersonnelNo() %></td>
					<td align="left"><%=employeeDto.getDisplayName() %></td>
					<td align="left"><%=employeeDto.getContactNo() %></td>
					<td align="left"><%=employeeDto.getProjectid() %></td>
					<td align="left"><%=employeeDto.getProject() %></td>
					<td align="left"><%=employeeDto.getProjectUnit() %></td>*/

					if (cell.getColumnIndex() == 0) {
						dto.setPersonnelNo(cell.getStringCellValue());
					}else if (cell.getColumnIndex() == 1) {
						dto.setDisplayName(cell.getStringCellValue());
					}   else if(cell.getColumnIndex() == 2) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProjectCode(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProjectCode())) {
								dto.setProjectCode(null);
							}
						}
					} else if(cell.getColumnIndex() == 3) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProject(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProject ())) {
								dto.setProject (null);
							}
						}
					} else if(cell.getColumnIndex() == 4) {
						if(cell.getCellType()!=Cell.CELL_TYPE_BLANK) {
							cell.setCellType(Cell.CELL_TYPE_STRING);
							dto.setProjectUnit(cell.getStringCellValue());
							if(OtherFunctions.isEmpty(dto.getProjectUnit())) {
								dto.setProjectUnit(null);
							}
						}
					} 

				}
				if (!flag)
					list.add(dto);

			}

/*			for(EmployeeDto dto : list) {
				System.out.println("login id  :"+ dto.getLoginId() + " name :"+ dto.getDisplayName());
			}*/
			returnint = this.updateExternalEmployeefromexcel(list, site, overwrite);
			
			System.out.println("returnint" + returnint);
			file.close();
			
		}

		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
			setMessage("Unable to read file");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			
			//e.printStackTrace();
			setMessage(""+ e);
		}
		return returnint;
	}
	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}
	
	
	public ArrayList<EmployeeDto> getAllDirectSubordinate(String empid) {
		System.out.println(empid);
		return dao.getAllDirectSubordinate(empid);
	}

	public EmployeeDto getEmployeeToSubscribe(String empid) {
		return dao.getEmployeeToSubscribe(empid);
	}
	
	public int UpdateNoShowConfig(String type, String count,String days) {
		return dao.UpdateNoShowConfig(type, count, days);
	}
	public EmployeeDto getNoShowConfig(String type) {
		return dao.getNoShowConfig(type);
	}
	public ArrayList<EmployeeDto> NoshowEmps()
	{
		return dao.NoshowEmps();
	}
	public ArrayList<EmployeeDto> getEmployeesForReminderMail()
	{
		return dao.getEmployeesForReminderMail();
	}
	
	public int UpdateProject(String empid, String project) {
		return dao.UpdateProject(empid, project);
	}
	
	public String[] getEmpLatLong(String empid)
	{
		return dao.getEmpLatLong(empid);
	}
	
	public int UpdateEmpLatLong(String empid, String latitude,String longitude) {
		return dao.UpdateEmpLatLong(empid, latitude, longitude);
	}
	public ArrayList<EmployeeDto> getSafeTravelAppUsers()
	{
		return dao.getSafeTravelAppUsers();
	}
	public ArrayList<EmployeeDto> NoshowReport(String fromdate,String todate)
	{
		return dao.NoshowReport( fromdate, todate);
	}
	public ArrayList<EmployeeDto> getSubscriptionRequestUsers()
	{
		return dao.getSubscriptionRequestUsers();
	}
	/*public int getActiveEmployeeCount(){
		return dao.getActiveEmployeeCount();
	}*/
}
