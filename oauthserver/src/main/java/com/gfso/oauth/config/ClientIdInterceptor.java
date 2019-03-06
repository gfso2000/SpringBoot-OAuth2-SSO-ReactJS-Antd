package com.gfso.oauth.config;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.filter.GenericFilterBean;

import com.gfso.util.Constants;

@Configuration
public class ClientIdInterceptor extends GenericFilterBean {
	
	@Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) 
    		throws IOException, ServletException {
		String clientId = request.getParameter(Constants.CLIENTID);
		String redirectUrl = request.getParameter(Constants.REDIRECTURL);
		if(clientId != null && redirectUrl != null) {
			System.err.println("set attribute");
			((HttpServletRequest)request).getSession().setAttribute(Constants.CLIENTID, clientId);
			((HttpServletRequest)request).getSession().setAttribute(Constants.REDIRECTURL, redirectUrl);
		}
        chain.doFilter(request, response);
    }
}