package com.gfso.hibernate.dao;

import com.gfso.hibernate.model.UserResetPwdEntity;

public interface UserResetPwdDao {
	
	void save(UserResetPwdEntity resetPwd);
	UserResetPwdEntity find(String userName);
	int delete(String userName);
}
