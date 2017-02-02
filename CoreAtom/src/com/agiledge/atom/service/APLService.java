/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.service;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dao.APLDao;
import com.agiledge.atom.dao.RouteDao;
import com.agiledge.atom.dao.RouteUpload;
import com.agiledge.atom.dto.APLDto;
import com.agiledge.atom.dto.AplTree;
import com.agiledge.atom.dto.RouteDto;


/**
 * 
 * @author 123
 */
public class APLService {

	private APLDao dao = new APLDao();
	private String message;

	public APLDto getLandMarkAccurate(String landMarkID) {
		return dao.getLandMarkAccurate(landMarkID);
	}

	public ArrayList<APLDto> getAreas() {
		return dao.getAreas();
	}

	public ArrayList<APLDto> getAreas(String location) {
		return dao.getAreas(location);
	}

	public ArrayList<APLDto> getLandmarksByPlaceId(int placeId) {
		return dao.getLandmarksByPlaceId(placeId);
	}
	public ArrayList<APLDto> getShuttleLandmarksByPlaceId(int placeId) {
		return dao.getShuttleLandmarksByPlaceId(placeId);
	}

	public ArrayList<APLDto> getPlacesByAreaId(int areaId) {
		return dao.getPlacesByAreaId(areaId);
	}
	public ArrayList<APLDto> getShuttlePlacesByAreaId(int areaId) {
		return dao.getShuttlePlacesByAreaId(areaId);
	}
	public int insertArea(APLDto APLDtoObj) {
		return dao.insertArea(APLDtoObj);
	}

	public APLDto getAreaById(int areaId) {
		return dao.getAreaById(areaId);
	}

	public int updateArea(APLDto APLDtoObj) {
		return dao.updateArea(APLDtoObj);
	}

	public APLDto getPlaceById(int placeId) {
		return dao.getPlaceById(placeId);
	}
	public APLDto getShuttlePlaceById(int placeId) {
		return dao.getShuttlePlaceById(placeId);
	}

	public int insertPlace(APLDto APLDtoObj) {
		return dao.insertPlace(APLDtoObj);
	}

	public int insertLandmark(APLDto APLDtoObj) {
		return dao.insertLandmark(APLDtoObj);
	}
	public int insertShuttleLandmark(APLDto APLDtoObj) {
		return dao.insertShuttleLandmark(APLDtoObj);
	}

	public int updatePlace(APLDto APLDtoObj) {
		return dao.updatePlace(APLDtoObj);
	}
	public int updateShuttlePlace(APLDto APLDtoObj) {
		return dao.updateShuttlePlace(APLDtoObj);
	}

	public void updateLandmark(APLDto APLDtoObj) {
		dao.updateLandmark(APLDtoObj);
	}
	public void updateShuttleLandmark(APLDto APLDtoObj) {
		dao.updateShuttleLandmark(APLDtoObj);
	}

	public ArrayList<APLDto> getAllAPL() {
		return dao.getAllAPL();
	}
	public ArrayList<APLDto> getAllAPLForSpecific(String LAP) {
		return dao.getAllAPLForSpecific(LAP);
	}
	public ArrayList<APLDto> getAllAPL(String location) {
		return dao.getAllAPL(location);
	}
	public ArrayList<APLDto> getAllShuttleAPL(String location) {
		return dao.getAllShuttleAPL(location);
	}
	public ArrayList<APLDto> getAllAPLNotInRoute(int routeId) {
		return dao.getAllAPLNotInRoute(routeId);
	}
	public ArrayList<APLDto> getAllShuttleAPLNotInRoute(int routeId) {
		return dao.getAllShuttleAPLNotInRoute(routeId);
	}

	public int updateLandmarkPostion(APLDto aPLDtoObj) {
		return dao.updateLandmarkPostion(aPLDtoObj);

	}

	public int insertShuttlePlace(APLDto aPLDtoObj) {
		return dao.insertShuttlePlace(aPLDtoObj);
	}
	
	public int uploadAPL(InputStream in, boolean areaDupe, boolean placeDupe, boolean landmarkDupe, String location) {
		int val = 0;
		try {
			if(location.isEmpty()||OtherFunctions.isInteger(location)==false) {
				message = "Location is invalid";
			} else {
		ArrayList<APLDto > aplList = uploadAPLFile(in);
	/*	for(APLDto apldto : aplList)
		System.out.println(apldto.getArea() + " " + apldto.getPlace() + " " + apldto.getLandMark());
	*/	System.out.println("apl uploaded ...");
		
		AplTree aplTree = createAplTee(aplList);
		System.out.println("apl tree created ...");
		showTree(aplTree);
		val = new APLDao().uploadAPL(aplTree, areaDupe, placeDupe, landmarkDupe, location);
			}
		}catch(Exception e) {
			val = 0;
			message = e.getMessage();
		}
		return val;
	}
	
	public ArrayList<APLDto >  uploadAPLFile( InputStream in) {
		int r=0;
		ArrayList<APLDto > dtoList = new ArrayList<APLDto >();
		try {
			
			boolean flag = false;
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			System.out.println("row 0");
			while (rowIterator.hasNext()) {
			
				if (flag)
					break;

				Row row = rowIterator.next();
				System.out.println("row " + row.getRowNum());
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				APLDto dto = new APLDto();
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					System.out.println("col " + cell.getColumnIndex());
					r=cell.getColumnIndex();
					if (cell.getColumnIndex() < 5) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

					}
 
					if (cell.getColumnIndex() == 0) {
						dto.setArea (cell.getStringCellValue().trim());
					 

					} else if (cell.getColumnIndex() == 1) {
						dto.setPlace (cell.getStringCellValue().trim());
						 

					} else if (cell.getColumnIndex() == 2) {
						 
						dto.setLandMark(cell.getStringCellValue().trim());
						 

					}  else if (cell.getColumnIndex() == 3) {
						 
						   
						  dto.setLattitude(cell.getStringCellValue().trim() );
					   
					} else if (cell.getColumnIndex() == 4) {
						 
						dto.setLongitude(cell.getStringCellValue().trim());
					
					}
					
				
				}
				
				if (!flag) {
					dtoList.add(dto);
				}
				

			}
 			//System.out.println("returnint" + returnint);
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
	
	
	private AplTree createAplTee(ArrayList<APLDto> apls) {
		int value =1;
		 
		AplTree areaTree = new AplTree(AplTree.AREA);
		if(apls!=null && apls.size()>0) {
			
			for(APLDto dto : apls) {
				areaTree.getMap().put(dto.getArea(), null);
			}
			System.out.println("Areas " + areaTree.getMap().size());
			
			for(APLDto dto : apls) {
				AplTree placeTree = areaTree.getMap().get(dto.getArea());
				if(placeTree==null) {
					placeTree = new AplTree(AplTree.PLACE);
					areaTree.getMap().put(dto.getArea(), placeTree);
				}
					placeTree.getMap().put(dto.getPlace(), null);
				
				 
			}
			 
			
			for(APLDto dto : apls) {
				AplTree placeTree = areaTree.getMap().get(dto.getArea());
				if(placeTree!=null) {
					 
				
					AplTree	landmarkTree = placeTree.getMap().get(dto.getPlace());
					if(landmarkTree==null ) {
						landmarkTree = new AplTree(AplTree.LANDMARK);
						placeTree.getMap().put(dto.getPlace(), landmarkTree);
					}
					AplTree latLong = null;
					try {
						if( OtherFunctions.isDouble(dto.getLattitude()) && 
								OtherFunctions.isDouble(dto.getLongitude()) ) {
							System.out.println("ZERRo");
							latLong = new AplTree();
							latLong.setAplDto(dto);
						}
					} catch(Exception e) {}
						landmarkTree.getMap().put(dto.getLandMark(), latLong);
				} else {
					 value = 0;
					message ="No places under "+ dto.getArea();
					areaTree= null;
				}
				
				 
			}
			
			
		}
		return areaTree;
	}
	
	
	private void showTree(AplTree aplTree) { 
		
		if(aplTree!=null && aplTree.getMap().size()>0) {
			
			for(String area : aplTree.getMap().keySet()) {
				if(aplTree.getMap().get(area)!=null  ) {
					AplTree placeTree = aplTree.getMap().get(area);
					 for(String place : placeTree.getMap().keySet()) {
						 if(placeTree.getMap().get(place)!=null) {
							 AplTree landmarkTree = placeTree.getMap().get(place);
							 for(String landmark : landmarkTree.getMap().keySet()) {
								 System.out.println(String.format(" AREA : %s | PLACE : %s | LANDMARK : %s ", area, place, landmark));
							 }
						 } else {
							 System.out.println(String.format(" AREA : %s | PLACE : %s | LANDMARK : %s ", area, place, "------------"));
						 }
					 }
				}  else {
					 System.out.println(String.format(" AREA : %s | PLACE : %s | LANDMARK : %s ", area, "--------------", "------------"));
				 }
			}
			
			
		}
		
	}
int count=1;
	public int uploadRoute(InputStream inputStream, String site, String type, boolean truncate  ) {
		int val=0;
		// TODO Auto-generated method stub
		try {
		ArrayList<RouteDto > dtoList = uploadRouteFile(inputStream);
		for(RouteDto dto: dtoList) {
			System.out.println("" + dto.getRouteId() + ", "+ dto.getArea() + ", "+ dto.getPlace() + ", " + dto.getLandmark() );
		}
		
			RouteUpload routeUpload =new RouteUpload();
			routeUpload.routeDtos = dtoList;
			routeUpload.type= type;
			routeUpload.count = count;
			routeUpload.site = site;
			routeUpload.setTruncate(truncate);
			val =routeUpload.insertRoutesToDB();
		}catch(Exception e) {
			val = 0;
		}
		return val;
	}
	
	
	public ArrayList<RouteDto >  uploadRouteFile( InputStream in) {
		int r=0;
		ArrayList<RouteDto > dtoList = new ArrayList<RouteDto >();
		try {
			
			boolean flag = false;
			XSSFWorkbook workbook = new XSSFWorkbook(in);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			while (rowIterator.hasNext()) {
			
				if (flag)
					break;

				Row row = rowIterator.next();
				
				System.out.println("row " + row.getRowNum());
				// For each row, iterate through each columns

				Iterator<Cell> cellIterator = row.cellIterator();
				RouteDto dto = new RouteDto();
				while (cellIterator.hasNext()) {
					
					Cell cell = cellIterator.next();
					r=cell.getColumnIndex();
					if (cell.getColumnIndex() < 5) {
						cell.setCellType(Cell.CELL_TYPE_STRING);
						String value = cell.getStringCellValue();
						
						if (value == null || value.equals("")) {
							System.out.println("End of file");
							flag = true;
							break;

						}

					}
 

					if (cell.getColumnIndex() == 0) {
						 if(OtherFunctions.isDouble(cell.getStringCellValue())==false) {

							 count ++;
							 dto.setRouteId(0);
							  break;
						 } else {
							 
							 dto.setRouteId(count);
						 }
					 

					} 
					else if (cell.getColumnIndex() == 1) {
						  
						dto.setArea (cell.getStringCellValue());
						  
					 

					} else if (cell.getColumnIndex() == 2) {
						dto.setPlace (cell.getStringCellValue());
						 

					} else if (cell.getColumnIndex() == 3) {
						 
						dto.setLandmark(cell.getStringCellValue());
						 
						 

					}  					
				
				
				}
				
				if (!flag) {
				
						dtoList.add(dto);
				 
				}
				

			}
 			//System.out.println("returnint" + returnint);
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
	
	
	public int deleteAllRoute(String site, String type) {
	
		int val=0;
		new RouteDao().deleteAllRoute(site,  type);
		return val;
	}
	

	
	


}
