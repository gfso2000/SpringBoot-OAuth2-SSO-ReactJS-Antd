package com.gfso.oauth.controller;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.gfso.dto.ResponseDto;
import com.gfso.dto.UserRegisterDto;
import com.gfso.hibernate.service.UserService;

@RestController
public class UserController {
	@Autowired
	@Qualifier("userService")
	UserService userService;
	
	@RequestMapping(value = "/api/resetpassword", method = RequestMethod.POST)
    public ResponseDto requestToResetPwd(@RequestParam("userName") String username, final HttpServletRequest request) throws Exception {
		String result = userService.resetUserPwdRequest(username);
		ResponseDto resultDto = new ResponseDto();
		resultDto.setSuccess();
		return resultDto;
    }
	
	@RequestMapping(value = "/api/register", method = RequestMethod.POST)
    @ResponseBody
    public ResponseDto registerUserAccount(@RequestBody @Valid final UserRegisterDto accountDto) {
		String result = userService.registerNewUserAccount(accountDto);
		ResponseDto resultDto = new ResponseDto();
		if("success".equals(result)) {
			resultDto.setSuccess();
		}else {
			resultDto.setFail(result);
		}
		return resultDto;
    }
}
