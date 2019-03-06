package com.gfso;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan("com.gfso")//if application is not in top package, need this annotation
public class OauthserverApplication {

	public static void main(String[] args) throws KeyManagementException, NoSuchAlgorithmException {
		SpringApplication.run(OauthserverApplication.class, args);
		DisableSSLCertificateCheckUtil.disableChecks();//for self signed ssl of authorizatio server
	}

}
