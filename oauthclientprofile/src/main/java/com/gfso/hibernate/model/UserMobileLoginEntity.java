package com.gfso.hibernate.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_mobilelogin")
public class UserMobileLoginEntity implements java.io.Serializable {

	@Id 
    @Column(name="username", nullable=false, length=45)
	private String userName;
    
    @Column(name="token", nullable=false, length=100)
	private String token;
    
    @Column(name="createddate", nullable=false)
    private Date createdDate;
    
	public UserMobileLoginEntity() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
