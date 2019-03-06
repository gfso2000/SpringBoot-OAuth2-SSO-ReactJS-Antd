package com.gfso.hibernate.service.impl;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.web.client.RestTemplate;

import com.gfso.dto.UserBasicDto;
import com.gfso.dto.UserRegisterDto;
import com.gfso.hibernate.dao.UserActivationDao;
import com.gfso.hibernate.dao.UserDao;
import com.gfso.hibernate.dao.UserMobileLoginDao;
import com.gfso.hibernate.dao.UserResetPwdDao;
import com.gfso.hibernate.dao.UserRoleDao;
import com.gfso.hibernate.model.UserActivationEntity;
import com.gfso.hibernate.model.UserEntity;
import com.gfso.hibernate.model.UserMobileLoginEntity;
import com.gfso.hibernate.model.UserResetPwdEntity;
import com.gfso.hibernate.model.UserRoleEntity;
import com.gfso.hibernate.service.EmailService;
import com.gfso.hibernate.service.UserService;
import com.gfso.util.Utils;

@Service("userService")
@Transactional
public class UserServiceImpl implements UserService{
	@Autowired
	UserDao userDao;
	@Autowired
	UserRoleDao userRoleDao;
	@Autowired
	UserActivationDao userActivationDao;
	@Autowired
	UserResetPwdDao userResetPwdDao;
	@Autowired
	UserMobileLoginDao userMobileLoginDao;
	
	@Autowired
    private PasswordEncoder passwordEncoder;
	
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	EmailService emailService;
	
	@Autowired
	ApplicationEventPublisher applicationEventPublisher;
	
	@Override
	public String registerNewUserAccount(UserRegisterDto user) {
		UserEntity entity = userDao.find(user.getEmail());
		if(entity!=null) {
			return "user already exists";
		}
		java.util.Date utilDate = new java.util.Date();
		
		entity = new UserEntity();
		entity.setUserName(user.getEmail());
		entity.setPassword(passwordEncoder.encode("123456"));
		entity.setEnabled(false);
		entity.setFirstName("firstName");
		entity.setLastName("lastName");
		entity.setCreatedDate(new Date(utilDate.getTime()));
		userDao.save(entity);
		
		UserRoleEntity roleEntity = new UserRoleEntity();
		roleEntity.setUsername(user.getEmail());
		roleEntity.setRole("ROLE_USER");
		userRoleDao.save(roleEntity);
		
		roleEntity = new UserRoleEntity();
		roleEntity.setUsername(user.getEmail());
		roleEntity.setRole("ROLE_ADMIN");
		userRoleDao.save(roleEntity);
		
		UserActivationEntity activationEntity = new UserActivationEntity();
		activationEntity.setUserName(user.getEmail());
		activationEntity.setCreatedDate(new Date(utilDate.getTime()));
		String activationCode = UUID.randomUUID().toString();
		activationEntity.setActivationcode(activationCode);
		userActivationDao.save(activationEntity);
		
		//publish event, it will be handled after commit
		String activationURL = null;
		try {
			activationURL = Utils.getBaseURL(request)+"/user/activation?username="+URLEncoder.encode(user.getEmail(), "UTF-8")+"&activationcode="+activationCode;
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		System.err.println(activationURL);
		SendEmailEvent event = new SendEmailEvent(user.getEmail(), activationURL);
		applicationEventPublisher.publishEvent(event);
		
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return "success";
	}

	@Override
	public String editProfile(UserBasicDto user) {
		String userName = SecurityContextHolder.getContext().getAuthentication().getName();
		UserEntity entity = userDao.find(userName);
		if(entity==null) {
			return "user not exists";
		}

		entity.setPassword(passwordEncoder.encode("123456"));
		entity.setFirstName("firstName");
		entity.setLastName("lastName");
		userDao.save(entity);
		
		//publish event, it will be handled after commit
		SendEmailEvent event = new SendEmailEvent(userName, "profile is changed");
		applicationEventPublisher.publishEvent(event);
		
		//TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		return "success";
	}
	
	@TransactionalEventListener(phase=TransactionPhase.AFTER_COMMIT)
	public void processSendEmailEvent(SendEmailEvent event) {
		emailService.sendSimpleMessage(event.getEmail(), "activation", event.getActivationURL());
	}
	
	@Override
	public String activateUser(String username, String activationCode) {
		UserActivationEntity entity = userActivationDao.find(username);
		if(entity != null && entity.getActivationcode().equals(activationCode)) {
			UserEntity user = userDao.find(username);
			user.setEnabled(true);
			java.util.Date utilDate = new java.util.Date();
			user.setActivatedDate(new Date(utilDate.getTime()));
			userDao.save(user);
			userActivationDao.delete(username);
			return null;
		}else {
			return "activation code expired";
		}
	}
	
	private class SendEmailEvent{
		private String email;
		private String activationURL;
		public SendEmailEvent(String email, String activationURL) {
			this.email = email;
			this.activationURL = activationURL;
		}
		public String getEmail() {
			return email;
		}
		public void setEmail(String email) {
			this.email = email;
		}
		public String getActivationURL() {
			return activationURL;
		}
		public void setActivationURL(String activationURL) {
			this.activationURL = activationURL;
		}
	}

	@Override
	public String resetUserPwdRequest(String username) {
		UserResetPwdEntity entity = userResetPwdDao.find(username);
		if(entity!=null) {
			return "already send reset email";
		}else {
			entity = new UserResetPwdEntity();
			entity.setUserName(username);
			java.util.Date utilDate = new java.util.Date();
			entity.setCreatedDate(new Date(utilDate.getTime()));
			String resetpwdcode = UUID.randomUUID().toString();
			entity.setResetpwdcode(resetpwdcode);
			userResetPwdDao.save(entity);
			
			//publish event, it will be handled after commit
			String resetPwdURL = null;
			try {
				resetPwdURL = Utils.getBaseURL(request)+"/user/resetPwdResponse?username="+URLEncoder.encode(username, "UTF-8")+"&resetpwdcode="+resetpwdcode;
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
			}
			System.err.println(resetPwdURL);
			SendEmailEvent event = new SendEmailEvent(username, resetPwdURL);
			applicationEventPublisher.publishEvent(event);
			
//			TransactionInterceptor.currentTransactionStatus().setRollbackOnly();
		}
		return null;
	}

	@Override
	public String resetUserPwdResponse(String username, String resetpwdcode) {
		UserResetPwdEntity entity = userResetPwdDao.find(username);
		if(entity != null && entity.getResetpwdcode().equals(resetpwdcode)) {
			UserEntity user = userDao.find(username);
			user.setEnabled(true);
			user.setPassword(passwordEncoder.encode("123456"));
			java.util.Date utilDate = new java.util.Date();
			user.setActivatedDate(new Date(utilDate.getTime()));
			userDao.save(user);
			userResetPwdDao.delete(username);
			return null;
		}else {
			return "reset password code expired";
		}
	}

	@Override
	public boolean verifyUsernamePwd(String username, String pwd) {
		UserEntity entity = userDao.find(username);
		if(entity!=null && passwordEncoder.matches(pwd, entity.getPassword())) {
			return true;
		}
		return false;
	}
	
	@Override
	public boolean verifyUsernameToken(String username, String token) {
		UserMobileLoginEntity loginEntity = userMobileLoginDao.find(username);
		if(loginEntity.getToken().equals(token)) {
			return true;
		}
		return false;
	}
	
	@Override
	public String saveMobileLogin(String username, String token) {
		UserMobileLoginEntity loginEntity = userMobileLoginDao.find(username);
		if(loginEntity == null) {
			loginEntity = new UserMobileLoginEntity();
		}
		loginEntity.setUserName(username);
		loginEntity.setToken(token);
		java.util.Date utilDate = new java.util.Date();
		loginEntity.setCreatedDate(new Date(utilDate.getTime()));
		userMobileLoginDao.save(loginEntity);
		return null;
	}

	@Override
	public boolean verifyPhoneSMS(String phoneNum, String smsCode) {
		String url = "https://webapi.sms.mob.com/sms/verify?appkey=2727b3a61c620&phone="+phoneNum+"&zone=86&code="+smsCode;
		HttpHeaders headers = new HttpHeaders();
		HttpEntity<String> entity = new HttpEntity<String>(headers);
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
 
        HttpStatus statusCode = response.getStatusCode();
        System.out.println("Response Satus Code: " + statusCode);
 
        // Status Code: 200
        if (statusCode == HttpStatus.OK) {
            // Response Body Data
            String result = response.getBody();
            try {
				JSONObject jsonObj = new JSONObject(result);
				int status = (int) jsonObj.get("status");
				if(200 == status) {
					return true;
				}else {
					System.err.println("verify status:"+result);
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
        }
 
		return false;
	}

//	public static String requestData(String address, String params) {
//		HttpURLConnection conn = null;
//		try {
//			// Create a trust manager that does not validate certificate chains
//			TrustManager[] trustAllCerts = new TrustManager[] { new X509TrustManager() {
//				public X509Certificate[] getAcceptedIssuers() {
//					return null;
//				}
//
//				public void checkClientTrusted(X509Certificate[] certs, String authType) {
//				}
//
//				public void checkServerTrusted(X509Certificate[] certs, String authType) {
//				}
//			} };
//
//			// Install the all-trusting trust manager
//			SSLContext sc = SSLContext.getInstance("TLS");
//			sc.init(null, trustAllCerts, new SecureRandom());
//
//			// ip host verify
//			HostnameVerifier hv = new HostnameVerifier() {
//				public boolean verify(String urlHostName, SSLSession session) {
//					return urlHostName.equals(session.getPeerHost());
//				}
//			};
//
//			// set ip host verify
//			HttpsURLConnection.setDefaultHostnameVerifier(hv);
//
//			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
//
//			URL url = new URL(address);
//			conn = (HttpURLConnection) url.openConnection();
//			conn.setRequestMethod("POST");// POST
//			conn.setConnectTimeout(3000);
//			conn.setReadTimeout(3000);
//			// set params ;post params
//			if (params != null) {
//				conn.setDoOutput(true);
//				DataOutputStream out = new DataOutputStream(conn.getOutputStream());
//				out.write(params.getBytes(Charset.forName("UTF-8")));
//				out.flush();
//				out.close();
//			}
//			conn.connect();
//			// get result
//			if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
//				String result = parsRtn(conn.getInputStream());
//				return result;
//			} else {
//				System.out.println(conn.getResponseCode() + " " + conn.getResponseMessage());
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//		} finally {
//			if (conn != null)
//				conn.disconnect();
//		}
//		return null;
//	}
}
