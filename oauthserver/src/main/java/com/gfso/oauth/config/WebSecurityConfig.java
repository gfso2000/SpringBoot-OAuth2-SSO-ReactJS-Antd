package com.gfso.oauth.config;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.Filter;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.oauth2.resource.ResourceServerProperties;
import org.springframework.boot.autoconfigure.security.oauth2.resource.UserInfoTokenServices;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.NestedConfigurationProperty;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configurers.provisioning.JdbcUserDetailsManagerConfigurer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.OAuth2ClientContext;
import org.springframework.security.oauth2.client.OAuth2RestTemplate;
import org.springframework.security.oauth2.client.filter.OAuth2ClientAuthenticationProcessingFilter;
import org.springframework.security.oauth2.client.filter.OAuth2ClientContextFilter;
import org.springframework.security.oauth2.client.token.grant.code.AuthorizationCodeResourceDetails;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableOAuth2Client;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationProcessingFilter;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.filter.CompositeFilter;

@Configuration
@EnableWebSecurity
@RestController
@EnableOAuth2Client
@Order(200)
//important,otherwise, client can't retrieve user, don't know why, 
//maybe the web security should after OAuth token security, because we already have access_token, if this security goes first, the access_token is ignored?
//or add "security.oauth2.resource.filter-order=3" in application.properties
class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    Logger log = LoggerFactory.getLogger(WebSecurityConfig.class);

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }
    
    @Configuration
	@EnableResourceServer
	protected static class ResourceServerConfiguration extends ResourceServerConfigurerAdapter {
	  @Override
	  public void configure(HttpSecurity http) throws Exception {
	    http
	      .antMatcher("/me")
	      .authorizeRequests().anyRequest().authenticated();
	  }
	}
    
    @RequestMapping({ "/user", "/me" })
//	public Map<String, Object> user(Principal principal) {
//    	Map<String, Object> map = new LinkedHashMap<>();
//		map.put("name", principal.getName());
//		if(principal instanceof OAuth2Authentication) {
//			OAuth2Authentication token = (OAuth2Authentication)principal;
//			map.put("authorities", token.getAuthorities());
//		} else {
//			UsernamePasswordAuthenticationToken token = (UsernamePasswordAuthenticationToken)principal;
//			map.put("authorities", token.getAuthorities());
//		}
//		return map;
//	}
    public Principal user(HttpServletRequest request, Principal principal) {
    	System.err.println("client ip:"+request.getRemoteAddr());
		return principal;
	}
    
    @Override
    protected void configure(HttpSecurity http) throws Exception {
    	http.addFilterBefore(
    	          new ClientIdInterceptor(), BasicAuthenticationFilter.class);//BasicAuthenticationFilter.class
    	
        http.cors().and()
            .csrf().disable()
                .exceptionHandling()
                //.authenticationEntryPoint((request, response, authException) -> response.sendError(HttpServletResponse.SC_UNAUTHORIZED))
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/login"))
                //.authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
            .and()
                .authorizeRequests()
                .antMatchers("/login**","/qrLogin/**","/error**","/resetPassword**","/mobile/login","/mobile/smslogin",
                		"/register**","/user/registration","/user/successRegister", "/user/activation", 
                		"/api/resetpassword", "/api/register",
                		"/index**","/favicon.ico","/css/**","/js/**","/static/**","/fonts/**","/images/**","/vendor/**","/webjars/**",
                		"/changeSessionLanauage","/downloadAPK").permitAll()
                .antMatchers("/hello.html").hasRole("ADMIN")//works
                .anyRequest().authenticated()
            .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .defaultSuccessUrl("/hello.html")//doesn't work
                .failureUrl("/login?error=true")
            .and()
                .logout().logoutSuccessUrl("/login")
            .and().addFilterBefore(ssoFilter(), BasicAuthenticationFilter.class);
//            .and()
//                .httpBasic().disable();
    }
    
    @Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
	}
    
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.inMemoryAuthentication()
//                .withUser("reader")
//                .password("reader")
//                .authorities("FOO_READ")
//                .and()
//                .withUser("writer")
//                .password("writer")
//                .authorities("FOO_READ", "FOO_WRITE");

//    	option1
//    	auth.jdbcAuthentication().dataSource(dataSource)
//    	.passwordEncoder(passwordEncoder())
//		.usersByUsernameQuery("select username,password, enabled from users where username=?")
//		.authoritiesByUsernameQuery("select username, role from user_roles where username=?");
    	
//    	option2
    	auth.userDetailsService(userDetailsManager);
    	JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder> conf =
                new JdbcUserDetailsManagerConfigurer<AuthenticationManagerBuilder>(userDetailsManager);
    	auth.apply(conf);
    	conf.dataSource(dataSource);
    	conf.passwordEncoder(passwordEncoder());
    }
    
    @Bean
    public JdbcUserDetailsManager userDetailsManager() {
        JdbcUserDetailsManager manager = new JdbcUserDetailsManager();
        manager.setDataSource(dataSource);
        manager.setUsersByUsernameQuery("select username,password, enabled from users where username=? and enabled=1");
        manager.setAuthoritiesByUsernameQuery("select username, role from user_roles where username=?");
        return manager;
    }
    
    @Bean(name = "dataSource")
	public DriverManagerDataSource dataSource() {
	    DriverManagerDataSource driverManagerDataSource = new DriverManagerDataSource();
	    driverManagerDataSource.setDriverClassName("com.mysql.jdbc.Driver");
	    driverManagerDataSource.setUrl("jdbc:mysql://localhost:3306/test?verifyServerCertificate=false&useSSL=true");
	    driverManagerDataSource.setUsername("root");
	    driverManagerDataSource.setPassword("root");
	    return driverManagerDataSource;
	}
    
    @Bean
	public PasswordEncoder passwordEncoder(){
		PasswordEncoder encoder = new BCryptPasswordEncoder();
		return encoder;
	}
    
    @Lazy
    @Autowired
	DataSource dataSource;
    
    @Lazy
    @Autowired
	private JdbcUserDetailsManager userDetailsManager; 
    
    @Autowired
	OAuth2ClientContext oauth2ClientContext;
    
    @Bean
	public FilterRegistrationBean<OAuth2ClientContextFilter> oauth2ClientFilterRegistration(OAuth2ClientContextFilter filter) {
		FilterRegistrationBean<OAuth2ClientContextFilter> registration = new FilterRegistrationBean<OAuth2ClientContextFilter>();
		registration.setFilter(filter);
		registration.setOrder(-100);
		return registration;
	}
    
	@Bean
	@ConfigurationProperties("github")
	public ClientResources github() {
		return new ClientResources();
	}

	@Bean
	@ConfigurationProperties("facebook")
	public ClientResources facebook() {
		return new ClientResources();
	}

	private Filter ssoFilter() {
		CompositeFilter filter = new CompositeFilter();
		List<Filter> filters = new ArrayList<>();
		filters.add(ssoFilter(facebook(), "/login/facebook"));
		filters.add(ssoFilter(github(), "/login/github"));
		filter.setFilters(filters);
		return filter;
	}

	private Filter ssoFilter(ClientResources client, String path) {
		OAuth2ClientAuthenticationProcessingFilter filter = new OAuth2ClientAuthenticationProcessingFilter(
				path);
		OAuth2RestTemplate template = new OAuth2RestTemplate(client.getClient(), oauth2ClientContext);
		filter.setRestTemplate(template);
		UserInfoTokenServices tokenServices = new UserInfoTokenServices(
				client.getResource().getUserInfoUri(), client.getClient().getClientId());
		tokenServices.setRestTemplate(template);
		filter.setTokenServices(tokenServices);
		return filter;
	}
}

class ClientResources {

	@NestedConfigurationProperty
	private AuthorizationCodeResourceDetails client = new AuthorizationCodeResourceDetails();

	@NestedConfigurationProperty
	private ResourceServerProperties resource = new ResourceServerProperties();

	public AuthorizationCodeResourceDetails getClient() {
		return client;
	}

	public ResourceServerProperties getResource() {
		return resource;
	}
}