package com.gfso.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.gfso.dto.UserBasicDto;
import com.gfso.dto.UserRegisterDto;
import com.gfso.hibernate.service.UserService;
import com.gfso.util.GenericResponse;

@Controller
public class RegistrationController {
	@Autowired
	@Qualifier("userService")
	UserService userService;
	
	@RequestMapping(value = "/user/editProfile", method = RequestMethod.POST)
    @ResponseBody
    public GenericResponse editProfile(@Valid final UserBasicDto accountDto) {
		String result = userService.editProfile(accountDto);
        return new GenericResponse(result);
    }
	
	@RequestMapping(value = "/user/activation", method = RequestMethod.GET)
    public String activateUserAccount(@RequestParam("username") String username, @RequestParam("activationcode") String activationCode, 
    		final HttpServletRequest request) throws Exception {
		String result = userService.activateUser(username, activationCode);
		if(result == null) {
			return "redirect:/login"; 
		}else {
			request.setAttribute("errorMessage", result);
			return "forward:/error"; 
		}
    }
	
	@RequestMapping(value = "/user/resetPwdResponse", method = RequestMethod.GET)
    public String responseToResetPwd(@RequestParam("username") String username, @RequestParam("resetpwdcode") String resetpwdcode, 
    		final HttpServletRequest request, final HttpServletResponse response, Model model) throws Exception {
		String result = userService.resetUserPwdResponse(username, resetpwdcode);
		if(result == null) {
			return "redirect:/login"; 
		}else {
			request.setAttribute("errorMessage", result);
			return "forward:/error"; 
		}
    }
}
