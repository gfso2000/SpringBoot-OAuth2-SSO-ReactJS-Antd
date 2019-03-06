package com.gfso.profile.config;

import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@Configuration
@EnableOAuth2Sso
@RestController
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	// sso测试接口
    @GetMapping("/user")
    public Authentication getUser(Authentication authentication) {
        return authentication;
    }
    
    @GetMapping("/hello")
    public String sayHello(Authentication authentication) {
        return "hello everyone";
    }
    
	//this is required, otherwise, /login url can't invoke
//	@Bean
//	public FilterRegistrationBean oauth2ClientFilterRegistration(
//	    OAuth2ClientContextFilter filter) {
//	  FilterRegistrationBean registration = new FilterRegistrationBean();
//	  registration.setFilter(filter);
//	  registration.setOrder(-100);
//	  return registration;
//	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//disable csrf, otherwise ajax post will get 403 forbidden error
		http.csrf().disable()
		.antMatcher("/**")
		.authorizeRequests()
			.antMatchers("/", "/login**", "/webjars/**","/logout**","/index**","/hello",
					"/home**","/styles/**","/plugins/**","/js/**","/images/**",
					"/changeSessionLanauage").permitAll()
			.anyRequest().authenticated()
		.and().logout().logoutSuccessUrl("/index").permitAll();
//		.and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
//		.and().exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"));

//		http
//        .sessionManagement()
//        .maximumSessions(1)
//        .expiredUrl("/multipleLogin.html")//for multiple login, but doesn't work? due to oauth?
//        .maxSessionsPreventsLogin(true)//.sessionRegistry(sessionRegistry())
//        .and()
//        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
//        .invalidSessionUrl("/expired.html");//for session timeout
	}
	
    @Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
	
}
