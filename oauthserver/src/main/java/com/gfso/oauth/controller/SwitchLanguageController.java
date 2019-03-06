package com.gfso.oauth.controller;

import java.io.IOException;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.i18n.SessionLocaleResolver;
import org.springframework.web.servlet.support.RequestContextUtils;

@RestController
public class SwitchLanguageController {
	@Autowired
	private MessageSource messageSource;
	
	@RequestMapping("/changeSessionLanauage")
	public void changeSessionLanauage(HttpServletRequest request, HttpServletResponse response, String lang) throws IOException {
		if ("zh".equals(lang)) {
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
					new Locale("zh", "CN"));
		} else if ("en".equals(lang)) {
			request.getSession().setAttribute(SessionLocaleResolver.LOCALE_SESSION_ATTRIBUTE_NAME,
					new Locale("en", "US"));
		}
		Locale locale= RequestContextUtils.getLocale(request);
		String msg = messageSource.getMessage("login", null,locale);
		response.sendRedirect("/login");
	}
}
