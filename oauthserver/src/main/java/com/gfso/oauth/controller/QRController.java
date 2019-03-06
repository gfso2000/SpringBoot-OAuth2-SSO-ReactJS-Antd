package com.gfso.oauth.controller;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gfso.dto.ResponseDto;
import com.gfso.dto.UserQRLoginDto;
import com.gfso.hibernate.service.UserService;
import com.gfso.util.Constants;
import com.gfso.util.QrGenUtil;
import com.gfso.util.Utils;

@RestController
@RequestMapping(value = "/qrLogin")
public class QRController {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private HttpServletResponse response;
	@Autowired
    ApplicationContext context;
	@Autowired
	@Qualifier("userService")
	UserService userService;
	
	@RequestMapping(value = "/getQr", method = RequestMethod.GET)
	public QRBean getQRBean() {
		UUID randomUUID = UUID.randomUUID();
		Map<String, String> map = getUUIDMap();
		map.put(randomUUID.toString(), null);
		
		String url = Utils.getBaseURL(request)+"/qrLogin/scan?uuid=" + randomUUID;
		System.err.println(url);
		String clientId = (String)request.getSession().getAttribute(Constants.CLIENTID);
		String redirectUrl = (String)request.getSession().getAttribute(Constants.REDIRECTURL);
		System.err.println(clientId+":"+redirectUrl);
		
		QRBean bean = new QRBean();
		ByteArrayOutputStream qrOut;
		try {
			qrOut = QrGenUtil.createQrGen(url);
			String imgData = Base64.getEncoder().encodeToString(qrOut.toByteArray());
			bean.setRandomUUID(randomUUID);
			bean.setImgData(imgData);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return bean;
	}

	@RequestMapping(value = "/check", method = RequestMethod.GET)
	public ResponseDto check(@RequestParam("uuid") String uuid) {
		ResponseDto resultDto = new ResponseDto();
		Map<String, String> map = getUUIDMap();
		String userId = map.get(uuid);
		if(!map.containsKey(uuid) || userId == null) {
			resultDto.setStatus("fail");
			return resultDto;
		}
		map.remove(uuid);
		//now, the scan is successful, I will programmatically set the target user to securityContext
		//then in the frontend, the login page will be redirected to hello.html
		//why not use @Autowired JdbcUserDetailsManager userDetailsManager?
		//because it will throw cyclic error, the userDetailsManager is not created before come here
		JdbcUserDetailsManager userDetailsManager = (JdbcUserDetailsManager)context.getBean("userDetailsManager");
		UserDetails userDetails = userDetailsManager.loadUserByUsername (userId);
		Authentication auth = new UsernamePasswordAuthenticationToken (userDetails.getUsername(),userDetails.getPassword(),userDetails.getAuthorities());
		SecurityContextHolder.getContext().setAuthentication(auth);

		String redirectUrl = (String)request.getSession().getAttribute(Constants.REDIRECTURL);
		if(redirectUrl != null) {
			System.err.println("remove attribute");
			request.getSession().removeAttribute(Constants.CLIENTID);
			request.getSession().removeAttribute(Constants.REDIRECTURL);
			resultDto.setStatus(redirectUrl);
		} else {
			String url = Utils.getBaseURL(request)+"/hello.html";
			resultDto.setStatus(url);
		}
		return resultDto;
	}
	
	/**
	 * 
	 * in PostMan, use below URL to emulate QR scan via mobile
	 * POST https://10.59.164.241:8444/qrLogin/scan?uuid=b61d217b-70ec-43f8-a91d-7545d484a48c
	 * BODY:
	 * {
	 *	"userId":"alex"
	 * }
	 */
	@RequestMapping(value = "/scan", method = RequestMethod.POST)
	public ResponseDto scan(@RequestParam("uuid") String uuid, @RequestBody UserQRLoginDto userInfo) {
		ResponseDto resultDto = new ResponseDto();
		Map<String, String> map = getUUIDMap();
		if(!map.containsKey(uuid)) {
			resultDto.setFail("expired:"+uuid);
			return resultDto;
		}
		//if uuid is there, now verify mobile userid/token
		if(userService.verifyUsernameToken(userInfo.getUserId(), userInfo.getToken())) {
			map.put(uuid, userInfo.getUserId());
			resultDto.setSuccess();
			return resultDto;
		}
		resultDto.setFail("invalid token");
		return resultDto;
	}
	
	private Map<String, String> getUUIDMap() {
		Map<String, String> map = (Map) request.getServletContext().getAttribute("UUID_MAP");
		if (map == null) {
			map = new HashMap<String, String>();
			request.getServletContext().setAttribute("UUID_MAP", map);
		}
		return map;
	}
}