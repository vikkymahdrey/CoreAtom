/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.agiledge.atom.servlets;

import java.io.IOException;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.agiledge.atom.dao.ScheduleModifyCancelDao;
import com.agiledge.atom.dto.ScheduleModifyCancelDto;
import com.agiledge.atom.service.SchedulingAlterService;


/**
 *
 * @author Administrator
 */
public class ScheduleModifyCancel extends HttpServlet {

    /** 
     * Processes requests for both HTTP <code>GET</code> and <code>POST</code> methods.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String bookModify = request.getParameter("modify");
        String source=request.getParameter("source");
        ScheduleModifyCancelDao scheduleModifyCancelDao=new ScheduleModifyCancelDao();
        String user = request.getSession().getAttribute("user").toString();
        if (bookModify != null) {
 //           System.out.println("Modify");
            try {
                /*HttpSession session=request.getSession(true);
        String scheduledBy =session.getAttribute("user").toString(); */       
        String scheduleIds[] = request.getParameterValues("scheduleId");
        ScheduleModifyCancelDto scheduleCancelModifyDto  = null;        
        ArrayList<ScheduleModifyCancelDto> scheduledEmpList = new ArrayList<ScheduleModifyCancelDto>();
        
            for (int i = 0; i < scheduleIds.length; i++) {
                scheduleCancelModifyDto = new ScheduleModifyCancelDto();  
                scheduleCancelModifyDto.setScheduledBy(request.getSession().getAttribute("user").toString());
   //             System.out.println("user id : "+ scheduleCancelModifyDto.getScheduledBy());
                scheduleCancelModifyDto.setScheduleId(scheduleIds[i]);
                scheduleCancelModifyDto.setProject(request.getParameter("project" + scheduleIds[i]));
                scheduleCancelModifyDto.setFromDate(request.getParameter("fromDate" + scheduleIds[i]));
                scheduleCancelModifyDto.setToDate(request.getParameter("toDate" + scheduleIds[i]));
                scheduleCancelModifyDto.setLoginTime(request.getParameter("logintime" + scheduleIds[i]));
                scheduleCancelModifyDto.setLogoutTime(request.getParameter("logouttime" + scheduleIds[i]));
                scheduleCancelModifyDto.setWeeklyoff(request.getParameter("weeklyoff"+scheduleIds[i]));
                scheduledEmpList.add(scheduleCancelModifyDto);
            }
           int status= new SchedulingAlterService().scheduleModify(scheduledEmpList);
 //          System.out.println("status : "+ status);
           if(status>0)
     		{
//        	   new SchedulingAlterService().sendMailForScheduleModify(scheduledEmpList,user);
     			request.getSession().setAttribute("status", "<div class='success'>Schedule Modified Successfully</div");	
     		}
     		else
     		{
     			request.getSession().setAttribute("status", "<div class='failure'>Schedule Modification Failed</div");
     		}
        } catch(Exception e) {
        	request.getSession().setAttribute("status", "<div class='failure'>Schedule Modification Failed</div");
                System.out.println("ERRORin Serlet"+e);         
        }
        } else {
        	try{
            String[] scheduleIds=request.getParameterValues("scheduleId");
            ScheduleModifyCancelDto scheduleCancelModifyDto  = null;        
            ArrayList<ScheduleModifyCancelDto> scheduledEmpList = new ArrayList<ScheduleModifyCancelDto>();
            
                for (int i = 0; i < scheduleIds.length; i++) {
                    scheduleCancelModifyDto = new ScheduleModifyCancelDto();  
                    scheduleCancelModifyDto.setScheduledBy(request.getSession().getAttribute("user").toString());       //             
                    scheduleCancelModifyDto.setScheduleId(scheduleIds[i]);                    
                    scheduleCancelModifyDto.setFromDate(request.getParameter("fromDate" + scheduleIds[i]));
                    scheduleCancelModifyDto.setToDate(request.getParameter("toDate" + scheduleIds[i]));                                  
                    scheduledEmpList.add(scheduleCancelModifyDto);
                }
            
            
           int status= scheduleModifyCancelDao.scheduleCancel(scheduleIds,scheduleCancelModifyDto);
           
           
           
           
           if(status==1)
    		{
        	   String schID=  request.getSession().getAttribute("user").toString();
//        	   new SchedulingAlterService().sendMailForScheduleCancel(scheduledEmpList,user);
    			request.getSession().setAttribute("status", "<div class='success'>Schedule Cancelled Successfully</div");	
    		}
    		else
    		{
    			request.getSession().setAttribute("status", "<div class='failure'>Schedule Cancellation Failed</div");
    		}
          
       
        }catch(Exception e)
        {
        	request.getSession().setAttribute("status", "<div class='failure'>Schedule Cancellation Failed</div");
        }
    }
        if(source==null)
        {
        response.sendRedirect("scheduled_employee.jsp");
        }else
        {
        	response.sendRedirect("transadmin_scheduledemployee.jsp");	        	
        }
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /** 
     * Handles the HTTP <code>GET</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Handles the HTTP <code>POST</code> method.
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    /** 
     * Returns a short description of the servlet.
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>
}
