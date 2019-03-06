package com.gfso.hibernate.dao.impl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.stereotype.Repository;

import com.gfso.hibernate.dao.UserRoleDao;
import com.gfso.hibernate.model.UserRoleEntity;

@Repository("userRoleDao")
public class UserRoleDaoImpl implements UserRoleDao{
	@PersistenceContext	
	private EntityManager entityManager;
	
	@Override
	public void save(UserRoleEntity userRole) {
		entityManager.persist(userRole);
	}
}