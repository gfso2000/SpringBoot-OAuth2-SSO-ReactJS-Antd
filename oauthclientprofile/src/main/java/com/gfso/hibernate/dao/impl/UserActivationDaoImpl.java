package com.gfso.hibernate.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gfso.hibernate.dao.UserActivationDao;
import com.gfso.hibernate.model.UserActivationEntity;

@Repository("userActivationDao")
public class UserActivationDaoImpl implements UserActivationDao{
	@PersistenceContext	
	private EntityManager entityManager;
	
	public void save(UserActivationEntity user){
		entityManager.persist(user);
	}

	@Override
	public UserActivationEntity find(String userName) {
		return entityManager.find(UserActivationEntity.class, userName);
	}

	@Override
	public int delete(String userName) {
		int count = entityManager.createQuery("delete from UserActivationEntity p where p.userName=:username")
	            .setParameter("username", userName)
	            .executeUpdate();
		return count;
	}
}