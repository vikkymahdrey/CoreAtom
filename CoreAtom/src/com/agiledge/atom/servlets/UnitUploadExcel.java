package com.agiledge.atom.servlets;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.agiledge.atom.dto.UnitMasterDTO;
import com.agiledge.atom.service.UnitService;

/**
 * Servlet implementation class UnitUploadExcel
 */
public class UnitUploadExcel extends HttpServlet {
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public UnitUploadExcel() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
    
    protected void processRequest(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {

		String contentType = request.getContentType();
		HttpSession session=request.getSession();

		if ((contentType != null)
				&& (contentType.indexOf("multipart/form-data") >= 0)) {

			DataInputStream in = new DataInputStream(request.getInputStream());

			int formDataLength = request.getContentLength();

			byte dataBytes[] = new byte[formDataLength];

			int byteRead = 0;

			int totalBytesRead = 0;

			// this loop converting the uploaded file into byte code
			while (totalBytesRead < formDataLength) {

				byteRead = in.read(dataBytes, totalBytesRead, formDataLength);
				totalBytesRead += byteRead;

			}
			String file = new String(dataBytes);

			// for saving the file name
			String saveFile = file.substring(file.indexOf("filename=\"") + 10);

			saveFile = saveFile.substring(0, saveFile.indexOf("\n"));

			saveFile = saveFile.substring(saveFile.lastIndexOf("\\") + 1,
					saveFile.indexOf("\""));

			int lastIndex = contentType.lastIndexOf("=");

			String boundary = contentType.substring(lastIndex + 1,
					contentType.length());
			int pos;

			// extracting the index of file
			pos = file.indexOf("filename=\"");

			pos = file.indexOf("\n", pos) + 1;

			pos = file.indexOf("\n", pos) + 1;

			pos = file.indexOf("\n", pos) + 1;

			int boundaryLocation = file.indexOf(boundary, pos) - 4;

			int startPos = ((file.substring(0, pos)).getBytes()).length;

			int endPos = ((file.substring(0, boundaryLocation)).getBytes()).length;

			// creating a new file with the same name and writing the

			System.out.println("Save File Name: " + saveFile);
			FileOutputStream fileOut = new FileOutputStream(saveFile);

			fileOut.write(dataBytes, startPos, (endPos - startPos));

			fileOut.flush();
			fileOut.close();

			int returnint=processXLFile(saveFile);
			String html="<html><script type=\"text/javascript\"> function doaction() { window.close(); opener.location.reload();	} </script><link href=\"css/style.css\" rel=\"stylesheet\" type=\"text/css\" /><body><center><h1>Uploaded Successfully!</h1></center><div></div><center><input type=\"button\" class=\"formbutton\" onClick=\"doaction()\" value=\"Close\"/></center></body></html> ";
			PrintWriter out = response.getWriter();
			out.write(html);
			out.flush();
			if(returnint==1||returnint>1)
			{
				session.setAttribute("status",
						"<div class='success'>Operation Successful!</div");
			}else
			{
				session.setAttribute("status",
						"<div class='failure'>Operation Failed Try Again</div");
			}
		}

	}
    
    private int processXLFile(String saveFile) {
		int returnint=0;
		try {
			ArrayList<UnitMasterDTO> list = new ArrayList<UnitMasterDTO>();
			FileInputStream file = new FileInputStream(new File(saveFile));
			XSSFWorkbook workbook = new XSSFWorkbook(file);
			XSSFSheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();
			rowIterator.next();
			while (rowIterator.hasNext()) {
				UnitMasterDTO dto = new UnitMasterDTO();
				Row row = rowIterator.next();
				Iterator<Cell> cellIterator = row.cellIterator();
				while (cellIterator.hasNext()) {
				Cell cell = cellIterator.next();
				cell.setCellType(Cell.CELL_TYPE_STRING);
				if (cell.getColumnIndex() == 0) {
					dto.setUnitcode(cell.getStringCellValue());

				} else if (cell.getColumnIndex() == 1) {
					dto.setUnitname(cell.getStringCellValue());
				} else if (cell.getColumnIndex() == 2) {
					dto.setStatus(cell.getStringCellValue());
				} else if (cell.getColumnIndex() == 3) {
					dto.setLocation_id(cell.getStringCellValue());
				}
			}
				list.add(dto);
			}
			returnint=new UnitService().insertunitfromexcel(list);
			file.close();
		}catch (FileNotFoundException fnfe) {
			fnfe.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return returnint;
		
		}
				
				
				
				
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		processRequest(request, response);
	}

}
