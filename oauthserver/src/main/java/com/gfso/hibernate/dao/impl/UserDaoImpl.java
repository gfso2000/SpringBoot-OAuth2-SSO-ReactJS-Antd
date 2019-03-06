package com.gfso.hibernate.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gfso.hibernate.dao.UserDao;
import com.gfso.hibernate.model.UserEntity;

@Repository("userDao")
public class UserDaoImpl implements UserDao{
	@PersistenceContext	
	private EntityManager entityManager;
	
	public void save(UserEntity user){
		entityManager.persist(user);
	}

	@Override
	public UserEntity find(String userName) {
		return entityManager.find(UserEntity.class, userName);
	}
}