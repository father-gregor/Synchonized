package com.benlinus92.synchronize.dao;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.benlinus92.synchronize.model.Profile;

@Repository
@Transactional
public class SynchronizeDaoImpl implements SynchronizeDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void saveUser(Profile user) {
		em.persist(user);
	}
}
