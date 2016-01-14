package com.benlinus92.synchronize.controller;

import java.util.Locale;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.env.Environment;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.service.SynchronizeService;
import com.benlinus92.synchronize.validator.ProfileValidator;

@Controller
@RequestMapping("/")
public class WebController {
	
	@Autowired
	private SynchronizeService service;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getHello(){
		ModelAndView model = new ModelAndView("/index");
		String test = "Everything OK";
		model.addObject("objTest", test);
		String user = getPrincipal();
		if(user != null)
			model.addObject("userName", user);
		return model;
	}
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showLoginForm() {
		return "login";
	}
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logoutPage(HttpServletRequest req, HttpServletResponse resp) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(req, resp, auth);
		}
		return "redirect:/?logout";
	}
	@RequestMapping(value="/register", method=RequestMethod.GET)
	public String showRegistrationForm(Model model) {
		model.addAttribute("user", new Profile());
		return "register";
	}
	@RequestMapping(value="/register", method=RequestMethod.POST)
	public String registerUser(@Valid @ModelAttribute("user") Profile user, BindingResult result, Model model) {	
		ProfileValidator validator = new ProfileValidator();
		validator.validate(user, result);
		if(result.hasErrors())
			return "register";
		if(service.saveUser(user) != true) {
			FieldError erLogin = new FieldError("user", "login", messageSource.getMessage("NotUnique.user.login", 
					new String[]{user.getLogin()}, Locale.getDefault()));
			FieldError erEmail = new FieldError("user", "email", messageSource.getMessage("NotUnique.user.email", 
					new String[]{user.getEmail()}, Locale.getDefault()));
			result.addError(erLogin);
			result.addError(erEmail);
			return "register";
		}
		return "index";
	}
	@RequestMapping(value="/profile/{userName}", method=RequestMethod.GET)
	public String showProfilePage() {
		//ModelAndView mav = new ModelAndView("/room");
		return "room";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.GET)
	public String createRoom() {
		//ModelAndView mav = new ModelAndView("/room");
		return "room";
	}
	
	private String getPrincipal() {
		String userName = null;
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal instanceof UserDetails) {
			userName = ((UserDetails)principal).getUsername();
		} 
		return userName;
	}
}
