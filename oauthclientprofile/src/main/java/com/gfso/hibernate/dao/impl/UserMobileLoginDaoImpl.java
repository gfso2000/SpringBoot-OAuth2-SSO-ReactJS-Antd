package com.gfso.hibernate.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gfso.hibernate.dao.UserMobileLoginDao;
import com.gfso.hibernate.model.UserMobileLoginEntity;

@Repository("userMobileLoginDao")
public class UserMobileLoginDaoImpl implements UserMobileLoginDao{
	@PersistenceContext	
	private EntityManager entityManager;
	
	public void save(UserMobileLoginEntity user){
		entityManager.persist(user);
	}

	@Override
	public UserMobileLoginEntity find(String userName) {
		return entityManager.find(UserMobileLoginEntity.class, userName);
	}

	@Override
	public int delete(String userName) {
		int count = entityManager.createQuery("delete from UserMobileLoginEntity p where p.userName=:username")
	            .setParameter("username", userName)
	            .executeUpdate();
		return count;
	}
}