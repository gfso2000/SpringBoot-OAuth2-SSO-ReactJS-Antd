package com.gfso.hibernate.service;

import com.gfso.dto.UserBasicDto;
import com.gfso.dto.UserRegisterDto;

public interface UserService {
	String registerNewUserAccount(UserRegisterDto user);
	String editProfile(UserBasicDto user);
	String activateUser(String username, String activationCode);
	String resetUserPwdRequest(String username);
	String resetUserPwdResponse(String username, String resetpwdcode);
	boolean verifyUsernamePwd(String username, String pwd);
	boolean verifyPhoneSMS(String phoneNum, String smsCode);
	boolean verifyUsernameToken(String username, String token);
	String saveMobileLogin(String username, String token);
}
