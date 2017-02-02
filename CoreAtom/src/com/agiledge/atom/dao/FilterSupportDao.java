package com.agiledge.atom.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import com.agiledge.atom.commons.OtherFunctions;
import com.agiledge.atom.dbConnect.DbConnect;
import com.agiledge.atom.servlets.apl.Pre;
import com.agiledge.atom.settings.dto.SettingStatus;
import com.agiledge.atom.usermanagement.dto.PageDto;
import com.agiledge.atom.usermanagement.dto.PageManagementStatus;
import com.agiledge.atom.usermanagement.service.PageService;



public class FilterSupportDao {
	
	 
	  
	public boolean authenticatePageRoleFromServeltContext(HttpServletRequest request , String role,  String page ) {
		String authName = page + "_" + role;
		ServletContext context = request.getSession().getServletContext();
		String lastWellPageStatus = page + "_" + role+"_MainPage";
		boolean flag = false;
		try {
			flag= Boolean.parseBoolean(context.getAttribute(authName).toString());
			
			 try {
				 if ( context.getAttribute(lastWellPageStatus ).equals("YES") ) {
					 
						request.getSession().setAttribute("lastAutherisedPage", page);
					 
				 }
			 }catch(Exception e1) {
				 
			 }
		} catch(Exception e) {
			flag = false;
		}
		return flag;
	}

	public boolean authenticatePageRole(HttpServletRequest request,  String role, String page, String employeeId) {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		ResultSet rsp = null;
		boolean flag = false;
		Connection con = ob.connectDB();
		PreparedStatement pst = null;
		String wellPageStatus = page + "_" + role+"_MainPage";
		ServletContext context = request.getSession().getServletContext();
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con
			String lastWellPage="";
			String pageCheckQuery="select * from page p where  p.url='"
					+ page + "' ";
			try {
				 lastWellPage=request.getSession().getAttribute("lastAutherisedPage").toString();
				
				} catch(Exception e) {
					;
				}
			pst=con.prepareStatement(pageCheckQuery);
			rsp = pst.executeQuery();
			if(rsp.next()) {
				System.out.println("Page Check Query : "+ pageCheckQuery);
				String pageId =rsp.getString("id") ;
			
				
			st = con.createStatement();
		/*	rs = st.executeQuery("select * from page p, role_page r where p.id=r.page and p.url='"
					+ page + "' and r.role='" + role + "'");
		*/
		
			String query = "select * from page p join menu_url m on p.id = m.url  join role_page r on m.menuKey=r.page where  p.url='" + page + "' and r.role='" + role + "'";
			
			 
			System.out.println("auth query : " + query);
			if(new PageService().isWellPage(page, role)) {
				
				context.setAttribute(wellPageStatus,"YES" );
				flag = true;
			}
			 System.out.println("flag : "+ flag);
			rs = st.executeQuery(query);		
			if (flag==false && rs.next()) {
				 
				 
				//context.setAttribute(wellPageStatus,"YES" );
				flag = true;
				

			} else if(flag==false ) {
				
				if(giveAutherisationOfPageItselfForRole(page,role)>0) {
				 
					context.setAttribute(wellPageStatus,"YES" );
					
					 flag = true;
				 }else if(lastWellPage.trim().equals("")==false&&Pre.autherisation_insertion_mode.equalsIgnoreCase(SettingStatus.AUTHERISATION_INSERTION_MODE_VALUE_ON)) {
					System.out.println("Lat well page : "+ lastWellPage);
					 if(giveAutherisationOfPageForRole(lastWellPage,page,role)>0) {
						 flag=true;
					 }
					  
				} else {
					int insVal=0;
					if(OtherFunctions.isEmpty( lastWellPage)) {
						if(Pre.autherisation_insertion_mode.equalsIgnoreCase(SettingStatus.AUTHERISATION_INSERTION_MODE_VALUE_ON)) {
							insVal=insertRolePage(role,page);
							flag = true;
						}
					}  
					if(insVal<=0 ) {  
						insertUnautherisedRequest(role, pageId, employeeId,lastWellPage);
					}
					 
						 
					 
				}
			}
			if(flag==true) {
				request.getSession().setAttribute("lastAutherisedPage", page);
			}
			} else {
				flag = true;
			}

		} catch (Exception e) {
			System.out
					.println("Error in FiltrSupportDao-> authenticatePageRole :   "
							+ e);

		} finally {
			DbConnect.closeResultSet(rs, rsp);
			 
			DbConnect.closeStatement(pst, st);
			DbConnect.closeConnection(con);
		}
	//	System.out.println("FLAG: "+ flag);
		if(flag ==true) {
			String authName = page + "_" + role;
			context.setAttribute(authName, true);
		}
		return flag;
	}

	public boolean hasAuthentication() {

		DbConnect ob = DbConnect.getInstance();
		Statement st = null;
		ResultSet rs = null;
		boolean flag = false;
		Connection con = ob.connectDB();
		// PreparedStatement pst = null;
		try {
			// Connection pooling implementation

			// replace ob.connectDB() with Connection con=ob.connectDB();
			// replace ob.con with con

			st = con.createStatement();
			rs = st.executeQuery("select  * from settings where authentication='true'");
			if (rs.next()) {
				flag = true;
			}

			DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(st);
			DbConnect.closeConnection(con);
		} catch (Exception e) {
			DbConnect.closeConnection(con);
			System.out
					.println("Error in FiltrSupportDao-> hasAuthentication :   "
							+ e);

		}
		return flag;
	}
	
	
	/* to insert unautherised requests */
	
public int insertUnautherisedRequest(String role, String page, String employeeId, String lastWellPage) {

	DbConnect ob = DbConnect.getInstance();
	Connection con = null;
	PreparedStatement st = null;
 
	int returnInt = 0;
	try {
		con = ob.connectDB();
		con.setAutoCommit(false);
		String lwpQueryLabel ="";
		String lwpQueryValue ="";
		if(lastWellPage!=null&&lastWellPage.trim().equals("")==false) {
			
			 lwpQueryLabel =" , lastWellPage ";
			 lwpQueryValue =" , ? ";
		}
		st = con.prepareStatement("insert into unautherised_requests ( url, role, employeeId " + lwpQueryLabel + "  ) values (?,?,?" + lwpQueryValue
				+ ")",Statement.RETURN_GENERATED_KEYS);
		System.out.println(" in addpage pagedao");
		st.setString(1, page);
		st.setString(2, role);
		st.setString(3, employeeId);
		if(lastWellPage!=null&&lastWellPage.trim().equals("")==false) {
		st.setString(4, lastWellPage);
		}
		returnInt = st.executeUpdate();
	 		
		con.commit();

	} catch (Exception e) {
		try {
			System.out.println(" Error in insertUnautherisedRequest "+e);
			con.rollback();
			returnInt=0;
			
		} catch (Exception ignore) {

		}
		e.printStackTrace();
	} finally {
		DbConnect.closeStatement(st);
		DbConnect.closeConnection(con);

	}
		return returnInt;
 
 
	}



public int giveAutherisationOfPageItselfForRole(String  subPage, String role){
	System.out.println("FilterSupportDao givAutherisation ..");
	int returnInt=0;
	if(Pre.autherisation_insertion_mode.equalsIgnoreCase(SettingStatus.AUTHERISATION_INSERTION_MODE_VALUE_ON)) {
	PageService pageService = new PageService();
	  
	PageDto subPageDto = pageService.getPageFromUrlString(subPage);
	if( subPageDto!=null) {
		PageDto paramDto = new PageDto();
		paramDto.setParentPageId(subPageDto.getId());
		paramDto.setUrl(subPageDto.getId());
		   
		paramDto.setUserType(role);
		paramDto.setUrlType(PageManagementStatus.MAIN);
		returnInt = pageService.groupAssignedUrlItself(paramDto);
	}
	
	}
	System.out.println("in giveAutherisationOfPageItselfForRole : "+ returnInt);
	return returnInt;
}


	public int giveAutherisationOfPageForRole(String mainPage, String subPage, String role){
		System.out.println("FilterSupportDao givAutherisation ..");
		int returnInt=0;
		PageService pageService = new PageService();
		 
		PageDto mainPageDto = pageService.getPageFromUrlString(mainPage);
		
		PageDto subPageDto = pageService.getPageFromUrlString(subPage);
		if(mainPageDto!=null&& subPageDto!=null) {
			PageDto paramDto = new PageDto();
			paramDto.setParentPageId(mainPageDto.getId());
			paramDto.setUrl(subPageDto.getId());
			   
			paramDto.setUserType(role);
			paramDto.setUrlType(PageManagementStatus.SUB);
			returnInt = pageService.groupAssignedUrl(paramDto);
		}
		return returnInt;
	}
	

	public int insertRolePage(String usertype, String page ) {
		PreparedStatement pst = null;
		PreparedStatement checkPst = null;
		ResultSet rs = null;
		int returnData = 0;
		Connection con = null;
		 
		 
		try {
			con = DbConnect.getInstance().connectDB();
				
			con.setAutoCommit(false);
			  
			checkPst = con.prepareStatement("select * from role_page rp, page p where rp.page=p.id and rp.role='"+usertype+"'  and p.url= '"+page+"' "); 
			pst = con
					.prepareStatement("insert into role_page(role,page ) select ?, p.id from page p where p.url=? ");
			 
			rs = checkPst.executeQuery();
			if(rs.next()==false) {
						pst.setString(1, usertype);
						pst.setString(2, page);
				 
						returnData = pst.executeUpdate();
					 System.out.println("Inserted role_page (" + usertype + ", " + page + ")");
						con.commit();
			} else {
				returnData = 1;
			}
			 
		} catch (Exception e) {
			try {
				con.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
			;
			}
			System.out.println("Error" + e);
		} finally {
			 DbConnect.closeResultSet(rs);
			DbConnect.closeStatement(pst,checkPst);
			DbConnect.closeConnection(con);
		}
		return returnData;
	}

}
