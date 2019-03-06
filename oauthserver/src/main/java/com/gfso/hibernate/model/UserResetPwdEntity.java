package com.gfso.hibernate.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="user_resetpwd")
public class UserResetPwdEntity implements java.io.Serializable {

	@Id 
    @Column(name="username", nullable=false, length=45)
	private String userName;
    
    @Column(name="resetpwdcode", nullable=false, length=100)
	private String resetpwdcode;
    
    @Column(name="createddate", nullable=false)
    private Date createdDate;
    
	public UserResetPwdEntity() {
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getResetpwdcode() {
		return resetpwdcode;
	}

	public void setResetpwdcode(String resetpwdcode) {
		this.resetpwdcode = resetpwdcode;
	}

	public Date getCreatedDate() {
		return createdDate;
	}

	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
}
