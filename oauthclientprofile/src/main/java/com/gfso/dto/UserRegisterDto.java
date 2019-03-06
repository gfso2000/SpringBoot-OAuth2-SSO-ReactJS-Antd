package com.gfso.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.gfso.validation.PasswordMatches;
import com.gfso.validation.ValidEmail;

@PasswordMatches
public class UserRegisterDto extends UserBasicDto{
    @ValidEmail
    @NotNull
    @Size(min = 1)
    private String email;

    public String getEmail() {
        return email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }
}
