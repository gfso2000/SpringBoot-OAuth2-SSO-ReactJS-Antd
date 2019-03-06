package com.gfso.hibernate.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_activation")
public class UserActivationEntity implements java.io.Serializable {

	private static final long serialVersionUID = 105686375802957803L;

	@Id 
    @Column(name="username", nullable=false, length=45)
	private String userName;
    
    @Column(name="activationcode", nullable=false, length=100)
	private String activationcode;
    
    @Column(name="createddate", nullable=false)
    private Date createdDate;
    
	public UserActivationEntity() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getActivationcode() {
		return activationcode;
	}

	public void setActivationcode(String activationcode) {
		this.activationcode = activationcode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
