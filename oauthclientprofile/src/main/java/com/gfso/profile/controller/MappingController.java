package com.gfso.profile.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MappingController {
	@Value("${register_url}")
    private String registerURL;
	
	@GetMapping({"/index", "/"})
    public String getInternationalPage(Model model) {
		System.err.println("registerURL:"+registerURL);
		model.addAttribute("registerURL", registerURL);
        return "index";
    }
	@GetMapping("/login")
    public String getLogin() {
        return "login";
    }
	@GetMapping("/logout")
    public String getRegister() {
        return "logout";
    }
	@GetMapping("/securedPage")
    public String getSecuredPage() {
        return "securedPage";
    }
	@GetMapping("/profile")
    public String getProfile() {
        return "profile";
    }
}
