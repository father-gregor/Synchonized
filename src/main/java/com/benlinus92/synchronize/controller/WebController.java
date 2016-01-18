package com.benlinus92.synchronize.controller;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.List;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;
import com.benlinus92.synchronize.service.SynchronizeService;
import com.benlinus92.synchronize.validator.ProfileValidator;

@Controller
@RequestMapping("/")
@SessionAttributes("userName")
public class WebController {
	
	@Autowired
	private SynchronizeService service;
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView getIndexPage(){
		ModelAndView model = new ModelAndView("/index");
		String user = getPrincipal();
		model.addObject("objTest","Проверка проверочка");
		if(user != null)
			model.addObject("userName", user);
		model.addObject("roomsList", service.getAllRooms());
		return model;
	}
	@RequestMapping(value="/login", method=RequestMethod.GET)
	public String showLoginForm() {
		return "login";
	}
	@RequestMapping(value="/logout", method=RequestMethod.GET)
	public String logoutPage(HttpServletRequest req, HttpServletResponse resp, SessionStatus status) {
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		if(auth != null) {
			new SecurityContextLogoutHandler().logout(req, resp, auth);
			status.setComplete();
		}
		return "redirect:/";
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
	@RequestMapping(value="/profile/{user}", method=RequestMethod.GET)
	public String showProfilePage(@PathVariable String user, Model model) {
		if(getPrincipal().equals(user)) {
			model.addAttribute("profile", service.findUserByLogin(user, true));
			return "profile";
		}
		model.addAttribute("profile", service.findUserByLogin(user, true));
		return "profile";
	}
	@RequestMapping(value="/profile/edit", method=RequestMethod.GET)
	public String showEditUserPage(Model model) {
		model.addAttribute("editedUser", new Profile());
		return "edit-profile";
	}
	@RequestMapping(value="/profile/edit", method=RequestMethod.POST)
	public String editUserProfile(@ModelAttribute("editedUser") Profile editedUser, 
			BindingResult result, Model model) {
		editedUser.setLogin(getPrincipal());
		ProfileValidator validator = new ProfileValidator();
		validator.validate(editedUser, result);
		if(result.hasErrors())
			return "edit-profile";
		try {
			service.editUserProfile(editedUser, getPrincipal());
		} catch(Exception e) {
			e.printStackTrace();
			return "edit-profile";
		}
		
		return "redirect:/profile/{userName}";
	}
	@RequestMapping(value="/room/{roomName}", method=RequestMethod.GET)
	public String showRoomByName(@PathVariable String roomName, Model model) {
		System.out.println(roomName);
		model.addAttribute("roomName", roomName);
		return "room";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.GET)
	public String showCreateRoomPage(Model model) {
		model.addAttribute("newRoom", new Room());
		return "create-room";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.POST)
	public String addNewRoom(@Valid Room room, BindingResult result, Model model) {
		if(result.hasErrors())
			return "create-room";
		String userName = getPrincipal();
		service.saveRoom(room, userName);
		return "redirect:/room/"+room.getTitle();
	}
	@RequestMapping(value="/delete-room-{roomId}", method=RequestMethod.GET)
	public String deleteRoomById(@PathVariable int roomId) {
		service.deleteRoomById(roomId, getPrincipal());
		return "redirect:/profile/{userName}";
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
