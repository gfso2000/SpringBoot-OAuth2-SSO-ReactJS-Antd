package com.gfso.oauth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {
	@GetMapping("/index")
    public String index() {
        return "index";
    }
	@GetMapping("/login")
    public String login() {
        return "login";
    }
	@GetMapping("/error")
    public String error() {
        return "error";
    }
	@GetMapping("/resetPassword")
    public String resetPassword() {
        return "resetPassword";
    }
	@GetMapping("/editProfile")
    public String editProfile() {
        return "editProfile";
    }
	@GetMapping("/login_old_with_qr")
    public String login_old_with_qr() {
        return "login_old_with_qr";
    }
	@GetMapping("/register")
    public String register() {
        return "register";
    }
	@GetMapping("/user/successRegister")
    public String successRegister() {
        return "successRegister";
    }
}
