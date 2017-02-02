package com.agiledge.atom.spring.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.agiledge.atom.constants.SettingsConstant;
import com.agiledge.atom.dao.GeneralShiftDAO;
import com.agiledge.atom.dto.EmployeeSubscriptionDto;
import com.agiledge.atom.dto.GeneralShiftDTO;
import com.agiledge.atom.dto.RouteDto;
import com.agiledge.atom.service.RouteService;
import com.agiledge.atom.service.ShuttleServcie;

@Controller
public class Shuttle {
	@RequestMapping(value = "/SubscribeForShuttle", method = RequestMethod.GET)
	public ModelAndView showShuttleSubscribe(HttpSession session) {
		ModelAndView mav = new ModelAndView("subscribeForShuttle");
		EmployeeSubscriptionDto EmployeeDto = new EmployeeSubscriptionDto();
		String site = session.getAttribute("site").toString();
		EmployeeDto.setSite(site);
		EmployeeDto.setEmployeeID(session.getAttribute("user").toString());
		if (new ShuttleServcie().isSubscriptionRequestMade(EmployeeDto
				.getEmployeeID())) {
			mav= new ModelAndView("redirect:/shuttleBookingDetails.jsp");
		} else {
			Map<Integer, String> inRoutes = new java.util.HashMap();
			Map<Integer, String> outRoutes = new java.util.HashMap();
			ArrayList<RouteDto> inRouteList=new RouteService().getInOutShuttleRoutes(site,"IN");
			ArrayList<RouteDto> outRouteList=new RouteService().getInOutShuttleRoutes(site,"OUT");
			ArrayList<GeneralShiftDTO> shiftIn = new GeneralShiftDAO().getActiveLogData("IN");
			ArrayList<GeneralShiftDTO> shiftOut	 = new GeneralShiftDAO().getActiveLogData("OUT");
			inRoutes.put(null, "Select");
			for (RouteDto dto : inRouteList) {
				inRoutes.put(dto.getRouteId(), dto.getRouteName());
			}
			outRoutes.put(null, "Select");
			for (RouteDto dto : outRouteList) {
				outRoutes.put(dto.getRouteId(), dto.getRouteName());
			}
			Map<String, String> INshifts = new java.util.HashMap();
			Map<String, String> OUTshifts = new java.util.HashMap();			
			for (GeneralShiftDTO dto : shiftIn) {
				INshifts.put(dto.getLogtime(), dto.getLogtime());
			}
			INshifts.put(null, "Select");
			OUTshifts.put(null, "Select");
			for (GeneralShiftDTO dto : shiftOut) {
				OUTshifts.put(dto.getLogtime(), dto.getLogtime());
			}
			
			mav.addObject("inRoutes", inRoutes);
			mav.addObject("outRoutes", outRoutes);
			mav.addObject("INshifts", INshifts);
			mav.addObject("OUTshifts", OUTshifts);
			mav.addObject("shuttleSubscription", EmployeeDto);
		}
		return mav;
	}

	@RequestMapping(value = "/SubscribeForShuttleSubmit", method = RequestMethod.POST)
	public void SubscribeShuttle(
			@ModelAttribute("shuttleSubscription") EmployeeSubscriptionDto dto,
			HttpSession session,HttpServletResponse response) throws IOException {
		String status = new ShuttleServcie().subscribeForShuttle(dto);
		System.out.println("shuttle booking status"+status);
		session.setAttribute("status",status);
		/*
		if (status.equalsIgnoreCase(SettingsConstant.ACTIVE_STATUS))

			session.setAttribute("status",
					"<div class=\"success\" width=\"100%\" > Subscription successful</div>");
		else if (status.equalsIgnoreCase(SettingsConstant.WAITING_LIST))
			session.setAttribute(
					"status",
					"<div class=\"success\" width=\"100%\" > Subscription successful with Waiting List</div>");
		else
			session.setAttribute("status",
					"<div class=\"failure\" > Subscription failed !</div>");
*/
			response.sendRedirect("shuttleBookingDetails.jsp");
		
	}
}
