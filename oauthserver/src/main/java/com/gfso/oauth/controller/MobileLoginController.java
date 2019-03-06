package com.gfso.oauth.controller;

import java.util.HashMap;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.gfso.dto.ResponseDto;
import com.gfso.dto.UserMobileLoginDto;
import com.gfso.hibernate.service.UserService;

@RestController
@RequestMapping(value = "/mobile")
public class MobileLoginController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
    ApplicationContext context;
	@Autowired
	@Qualifier("userService")
	UserService userService;
	
	@RequestMapping(value = "/login", method = RequestMethod.POST)
	public ResponseDto mobileLogin(@RequestBody UserMobileLoginDto userInfo) {
		ResponseDto resultDto = new ResponseDto();
		
		boolean verified = userService.verifyUsernamePwd(userInfo.getUserId(), userInfo.getPassword());
		if(verified) {
			UUID uuid = UUID.randomUUID();
			String result = userService.saveMobileLogin(userInfo.getUserId(), uuid.toString());
			if(result == null) {
				HashMap<String, String> data = new HashMap<>();
				data.put("token", uuid.toString());
				resultDto.setSuccess();
				resultDto.setData(data);
				return resultDto;
			} else {
				resultDto.setFail(result);
			}
		} else {
			resultDto.setFail("invalid username/password");
		}
		return resultDto;
	}
	
	@RequestMapping(value = "/smslogin", method = RequestMethod.POST)
	public ResponseDto mobileSMSLogin(@RequestBody UserMobileLoginDto userInfo) {
		ResponseDto resultDto = new ResponseDto();
		
		boolean verified = userService.verifyPhoneSMS(userInfo.getPhoneNum(), userInfo.getSmsCode());
		if(verified) {
			UUID uuid = UUID.randomUUID();
			String result = userService.saveMobileLogin(userInfo.getPhoneNum(), uuid.toString());
			if(result == null) {
				HashMap<String, String> data = new HashMap<>();
				data.put("token", uuid.toString());
				resultDto.setSuccess();
				resultDto.setData(data);
				return resultDto;
			} else {
				resultDto.setFail(result);
			}
		} else {
			resultDto.setFail("invalid sms code");
		}
		return resultDto;
	}
}