package com.gfso.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gfso.validation.PasswordMatches;

@PasswordMatches
public class UserRegisterDto extends UserBasicDto{
	@NotNull
    @Size(min = 1)
    private String captcha;

	public String getCaptcha() {
		return captcha;
	}

	public void setCaptcha(String captcha) {
		this.captcha = captcha;
	}
    
}
