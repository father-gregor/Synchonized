package com.benlinus92.synchronize.controller;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
import org.springframework.http.MediaType;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.SessionAttributes;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.ModelAndView;

import com.benlinus92.synchronize.config.AppConstants;
import com.benlinus92.synchronize.model.AjaxVideoTime;
import com.benlinus92.synchronize.model.Playlist;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Result;
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
		if(getPrincipal() != null)
			return "redirect:/";
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
		if(getPrincipal() != null)
			return "redirect:/";
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
		return "redirect:/";
	}
	@RequestMapping(value="/profile/{user}", method=RequestMethod.GET)
	public String showProfilePage(@PathVariable String user, Model model) {
		Profile profile = service.findUserByLogin(user, true);
		if(profile == null)
			return "redirect:/";
		String userName = getPrincipal();
		if(userName != null)
			model.addAttribute("userName", userName);
		model.addAttribute("profile", profile);
		return "profile";
	}
	@RequestMapping(value="/profile/edit", method=RequestMethod.GET)
	public String showEditUserPage(Model model) {
		model.addAttribute("editedUser", new Profile());
		String userName = getPrincipal();
		if(userName != null)
			model.addAttribute("userName", userName);
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
	@RequestMapping(value="/room/{roomId}", method=RequestMethod.GET)
	public String showRoomById(@PathVariable int roomId, Model model) {
		Room room = service.findRoomById(roomId);
		if(room == null)
			return "redirect:/";
		String userName = getPrincipal();
		if(userName != null)
			model.addAttribute("userName", userName);
		model.addAttribute("room", room);
		model.addAttribute("videoObj", new Playlist());
		return "room";
	}
	private volatile String roomTime = "0"; 
	@RequestMapping(value="/gettime-ajax-{videoId}", method=RequestMethod.GET)
	public @ResponseBody String sendVideoTimeByAjax(@PathVariable String videoId) {
		double editedTime = Double.parseDouble(service.findVideoById(videoId).getCurrTime()) + 1.0;
		return String.valueOf(editedTime);
		//return service.findVideoById(videoId).getCurrTime();
		//return roomTime;
	}
	@RequestMapping(value="/sendtime-ajax", method=RequestMethod.POST)
	public @ResponseBody String getVideoTimeByAjax(@RequestBody AjaxVideoTime video) {
		service.updateVideo(Integer.parseInt(video.getVideoId()), video.getCurrTime().toString());
		//roomTime = video.getCurrTime().toString();
		return "";
	}
	
	@MessageMapping("/disconn")
	@SendTo("/topic/disconnectTest")
	public Result disconnectionTest(AjaxVideoTime time) {
		return new Result("WEBSOCKET WORKING");
	}
	
	@RequestMapping(value="/deletevideo-ajax-{videoId}")
	public @ResponseBody String deleteVideoFromDb(@PathVariable String videoId) {
		service.deleteVideo(Integer.parseInt(videoId));
		return "";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.GET)
	public String showCreateRoomPage(Model model) {
		model.addAttribute("newRoom", new Room());
		return "create-room";
	}
	@RequestMapping(value="/create-room", method=RequestMethod.POST)
	public String addNewRoom(@Valid Room room, BindingResult result) throws UnsupportedEncodingException {
		if(result.hasErrors())
			return "create-room";
		service.saveRoom(room, getPrincipal());
		//URLEncoder.encode(room.getTitle(), "UTF-8"); maybe be valuable in future
		return "redirect:/room/" + room.getRoomId();
	}
	@RequestMapping(value="/delete-room-{roomId}", method=RequestMethod.GET)
	public String deleteRoomById(@PathVariable int roomId) {
		service.deleteRoomById(roomId, getPrincipal());
		return "redirect:/profile/{userName}";
	}
	@RequestMapping(value="/playlist/add-{videoType}-{roomId}", method=RequestMethod.POST)
	public String addVideoToPlaylist(@ModelAttribute("videoObj")  Playlist video, @PathVariable String videoType,
			@PathVariable int roomId, BindingResult result, HttpServletRequest request) {
		if(result.hasErrors()) {
			return "redirect:/room/" + roomId;
		}
		try {
			video.setType(videoType);
			video.setCurrTime("0");
			if(videoType.equals(AppConstants.TYPE_UPLOAD_VIDEO)) {
				String folderPath = AppConstants.VIDEOSTORE_LOCATION + roomId + "/";
				video.setUrl(video.getFile().getOriginalFilename());
				if(!new File(folderPath).exists())
					new File(folderPath).mkdir();
				video.getFile().transferTo(new File(folderPath + video.getFile().getOriginalFilename()));
			}
			service.saveVideo(video, roomId);
		} catch(IOException e) {
			e.printStackTrace();
			return "redirect:/room/" + roomId;
		}
		return "redirect:/room/" + roomId;
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
