package com.gfso.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gfso.validation.PasswordMatches;
import com.gfso.validation.ValidPassword;

@PasswordMatches
public class UserBasicDto {
    @NotNull
    @Size(min = 1)
    private String email;

    @ValidPassword
    private String password;

    @NotNull
    @Size(min = 1)
    private String matchingPassword;

    @NotNull
    @Size(min = 1)
    private String mobile;
    
    public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public String getPassword() {
		return password;
	}


	public void setPassword(String password) {
		this.password = password;
	}


	public String getMatchingPassword() {
		return matchingPassword;
	}


	public void setMatchingPassword(String matchingPassword) {
		this.matchingPassword = matchingPassword;
	}


	public String getMobile() {
		return mobile;
	}


	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	@Override
    public String toString() {
        final StringBuilder builder = new StringBuilder();
        builder.append("UserDto [email=").append(email).
        	append(", password=").append(password).
        	append(", matchingPassword=").append(matchingPassword).
        	append(", mobile=").append(mobile).append("]");
        return builder.toString();
    }

}
