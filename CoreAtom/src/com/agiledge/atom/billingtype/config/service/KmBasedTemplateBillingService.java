package com.agiledge.atom.billingtype.config.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.billingtype.config.dao.KmBasedTemplateBillingTypeConfigDao;
import com.agiledge.atom.billingtype.config.dto.AcConstraintDto;
import com.agiledge.atom.billingtype.config.dto.BillingTypeConfigConstants;
import com.agiledge.atom.billingtype.config.dto.KmBasedTemplateTripBillingConfigDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto;
import com.agiledge.atom.billingtype.config.dto.KmTemplateDto.KmTemplateChildDto;
import com.agiledge.atom.commons.OtherFunctions;

public class KmBasedTemplateBillingService {
	
	private KmBasedTemplateBillingTypeConfigDao dao = new KmBasedTemplateBillingTypeConfigDao();
	private String message="";
	
	
	private void processXLFile(KmTemplateDto template,InputStream in) {
		int r=0;
		try {
			boolean flag = false;
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			System.out.println("row 0");
			while (rowIterator.hasNext()) {
			KmTemplateChildDto 	dto =  template.new KmTemplateChildDto();
				if (flag)
					break;

				Row row = rowIterator.next();
				System.out.println("row " + row.getRowNum());
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					System.out.println("col " + cell.getColumnIndex());
					r=cell.getColumnIndex();
					if (cell.getColumnIndex() < 4) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

					}
 
					if (cell.getColumnIndex() == 0) {
						dto.getAplDto().setArea (cell.getStringCellValue());
						System.out.println("VALUE : " + dto.getAplDto().getArea());

					} else if (cell.getColumnIndex() == 1) {
						dto.getAplDto().setPlace (cell.getStringCellValue());
						 

					} else if (cell.getColumnIndex() == 2) {
						dto.setLandmarkName(cell.getStringCellValue());
						dto.getAplDto().setLandMark(cell.getStringCellValue());
						 

					} else if (cell.getColumnIndex() == 3) {
						dto.setDistance(Float.parseFloat( cell.getStringCellValue()));
						 
					}  
					
				}
				if (!flag)
					template.getChildList().add(dto);

			}
 			//System.out.println("returnint" + returnint);
		//	file.close();
			in.close();
			
		}

		catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("ERRRO on cell "+r+"  cause"+e);
			e.printStackTrace();
			
		}
		 
	}
	

	
	public int processTemplate(KmTemplateDto template, InputStream iin) {
		  processXLFile(template,iin);
		  int returnInt = 0;
		  if(template.getChildList()!=null && template.getChildList().size() > 0 ) {
			  returnInt = dao.insertKmBasedTemplate(template);
		  }  
		  return returnInt;
	}
	
	public KmTemplateDto getKmTemplateDto(String kmBillingId) {
		return dao.getKmTemplateDto( kmBillingId);
	}
	
	public ArrayList<KmBasedTemplateTripBillingConfigDto> getKmBasedTemplateBillingConfig(String refId) {
		return dao.getKmBasedTemplateBillingConfig(refId);
	}

	
	public boolean validateKmBasedTemplateBilling(KmBasedTemplateTripBillingConfigDto dto ) {
boolean flag=true;
		if(dto.getTemplateId()<1) {
			setMessage("Invalid access");
			flag = false;
		}
		if(dto.getTemplateId()<1) {
			setMessage("Template is not uploaded");
			flag = false;
		} else if( OtherFunctions.isEmpty( dto.getTripRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getVehicleTypeId() )) {
			setMessage("Vehicle type is not selected");
			flag = false;
			
		} else if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getEscortRateType() )) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 String lastToTime=" ";
				 for(AcConstraintDto adto: dto.getAcList()) {
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = true;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = true;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
					 }else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = true;
						 break;
					 }
					 lastToTime=adto.getToTime();
				 }
			 }
		} else if (checkDuplication(dto)) {
			 setMessage("Duplicate exists.");
			 flag = true;
		}
		
		return flag;
	
		
	 
	}

	
	public boolean validateKmBasedTemplateBillingForEdit(KmBasedTemplateTripBillingConfigDto dto ) {
boolean flag=true;
		 
		if(OtherFunctions.isEmpty( dto.getId() )) {
			setMessage("Unable to edit");
			flag = false;
		} else if( OtherFunctions.isEmpty( dto.getTripRate() )) {
			setMessage("Trip rate is not selected");
			flag = false;
			
		} else if( OtherFunctions.isEmpty( dto.getVehicleTypeId() )) {
			setMessage("Vehicle type is not selected");
			flag = false;
			
		} else if ( OtherFunctions.isEmpty(dto.getAcYes())) {
			setMessage("A/C constraint type is not chosen");
			flag = false;
		} else if ( OtherFunctions.isEmpty(dto.getEscortRateType() )) {
			setMessage("Escort rate type is not chosen");
			flag = false;
		} else if (dto.getAcYes().equalsIgnoreCase(BillingTypeConfigConstants.AC_YES)) {
			 ArrayList<AcConstraintDto> acList = dto.getAcList();
			 if(acList==null || acList.size()<=0) {
				 setMessage("No value in a/c constraints");
				 flag = false;
			 } else {
				 String lastToTime=" ";
				 for(AcConstraintDto adto: dto.getAcList()) {
					 if(OtherFunctions.isEmpty(adto.getFromTime())) {
						 setMessage("A/C From time is blank.");
						 flag = true;
						 break;
						 
					 } else  if(OtherFunctions.isEmpty(adto.getToTime())) {
						 setMessage("A/C To time is blank.");
						 flag = true;
						 break;
						 
					 } else  if( adto.getFromTime().compareTo(adto.getToTime())>0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
						 
					 } else  if(lastToTime.compareTo(adto.getFromTime())>=0) {
						 setMessage("A/C time is not in order");
						 flag = true;
						 break;
					 }else if(OtherFunctions.isEmpty(adto.getRate() )) {
						 setMessage("A/C rate is blank.");
						 flag = true;
						 break;
					 }
					 lastToTime=adto.getToTime();
				 }
			 }
		}  
		
		return flag;
	
		
	 
	}


	private boolean checkDuplication(KmBasedTemplateTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return dao.checkDuplication(dto);
	}



	public int addKmBasedTemplateConfig(KmBasedTemplateTripBillingConfigDto dto) {
		// TODO Auto-generated method stub
		return dao.addKmBasedTemplateBillingConfig(dto);
	}


	public String getMessage() {
		return message;
	}


	public void setMessage(String message) {
		this.message = message;
	}



	public int deleteKmBasedTemplateBillingConfig(
			KmBasedTemplateTripBillingConfigDto dto ) {
		 
		return dao.deleteKmBasedTemplateBillingConfig( dto);
	}



	public int updateKmBasedTemplateConfig(
			KmBasedTemplateTripBillingConfigDto dto) {
		
		return dao.updateKmBasedTemplateConfig(dto);
	}


}
