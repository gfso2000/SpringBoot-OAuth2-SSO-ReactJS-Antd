package com.gfso.hibernate.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gfso.hibernate.dao.UserResetPwdDao;
import com.gfso.hibernate.model.UserResetPwdEntity;

@Repository("userResetPwdDao")
public class UserResetPwdDaoImpl implements UserResetPwdDao{
	@PersistenceContext	
	private EntityManager entityManager;
	
	public void save(UserResetPwdEntity user){
		entityManager.persist(user);
	}

	@Override
	public UserResetPwdEntity find(String userName) {
		return entityManager.find(UserResetPwdEntity.class, userName);
	}

	@Override
	public int delete(String userName) {
		int count = entityManager.createQuery("delete from UserResetPwdEntity p where p.userName=:username")
	            .setParameter("username", userName)
	            .executeUpdate();
		return count;
	}
}