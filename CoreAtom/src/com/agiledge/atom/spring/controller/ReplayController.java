package com.agiledge.atom.spring.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
@Controller
public class ReplayController {
	@RequestMapping(value="replayTrack",method=RequestMethod.GET)
	public ModelAndView getTripDetails(HttpServletRequest request) {
		String tripID=request.getParameter("tripid");
		ModelAndView mav = new ModelAndView("replayTrack");
		mav.addObject("cityLat", "13.023823");
		mav.addObject("cityLon", "77.540044");
		mav.addObject("tripId", tripID);
		return mav;
		
	}
}
