package com.gfso.hibernate.dao;

import com.gfso.hibernate.model.UserEntity;

public interface UserDao {
	
	void save(UserEntity user);
	UserEntity find(String userName);
}
