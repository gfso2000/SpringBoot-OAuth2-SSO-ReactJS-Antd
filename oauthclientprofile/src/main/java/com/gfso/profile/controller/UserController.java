package com.gfso.profile.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gfso.dto.ResponseDto;
import com.gfso.dto.UserBasicDto;
import com.gfso.dto.UserRegisterDto;
import com.gfso.hibernate.service.UserService;
import com.gfso.util.GenericResponse;

@RestController
public class UserController {
	@Autowired
	@Qualifier("userService")
	UserService userService;
	
    @GetMapping("/api/getusername")
    public ResponseDto getUserName(Authentication authentication) {
    	ResponseDto resultDto = new ResponseDto();
    	resultDto.setStatus("success");
    	resultDto.setErrorMessage(authentication.getPrincipal().toString());
        return resultDto;
    }
    
    @RequestMapping(value = "/api/editprofile", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto editProfile(@RequestBody @Valid final UserRegisterDto accountDto) {
    	ResponseDto resultDto = new ResponseDto();
		String result = userService.editProfile(accountDto);
		if(result.equals("success")) {
			resultDto.setStatus("success");
		}else {
			resultDto.setStatus("success");
	    	resultDto.setErrorMessage(result);
		}
        return resultDto;
    }
}
