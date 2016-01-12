package com.benlinus92.synchronize.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller
@RequestMapping("/")
public class WebController {

	
	@Autowired
	private Environment env;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getHello(){
		ModelAndView model = new ModelAndView("/index");
		String test = "Everything OK";
		model.addObject("objTest", test);
		return model;
	}
	@RequestMapping(value="/create-room", method=RequestMethod.GET)
	public ModelAndView createRoom() {
		ModelAndView mav = new ModelAndView("/room");
		return mav;
	}
}
