package com.gfso.hibernate.dao;

import com.gfso.hibernate.model.UserActivationEntity;

public interface UserActivationDao {
	
	void save(UserActivationEntity activation);
	UserActivationEntity find(String userName);
	int delete(String userName);
}
