package com.gfso.hibernate.dao;

import com.gfso.hibernate.model.UserMobileLoginEntity;

public interface UserMobileLoginDao {
	
	void save(UserMobileLoginEntity mobileLogin);
	UserMobileLoginEntity find(String userName);
	int delete(String userName);
}
