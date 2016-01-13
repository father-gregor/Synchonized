package com.benlinus92.synchronize.controller;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.service.SynchronizeService;

@Controller
@RequestMapping("/")
public class WebController {
	
	@Autowired
	private Environment env;
	@Autowired
	private SynchronizeService service;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getHello(){
		ModelAndView model = new ModelAndView("/index");
		String test = "Everything OK";
		model.addObject("objTest", test);
		return model;
	}
	@RequestMapping(value="/login", method=RequestMethod.POST)
	public String authorizeUser() {
		return "/";
	}
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		Profile user = new Profile();
		model.addAttribute("user", user);
		return "/register";
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String saveUser(@Valid Profile user, BindingResult result, Model model) {
		if(result.hasErrors())
			return "/register";
		service.saveUser(user);
		return "/index";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.GET)
	public ModelAndView createRoom() {
		ModelAndView mav = new ModelAndView("/room");
		return mav;
	}
}
