package com.benlinus92.synchronize.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;

@Repository
@Transactional
public class SynchronizeDaoImpl implements SynchronizeDao {
	
	@PersistenceContext
	private EntityManager em;
	
	@Override
	public void saveUser(Profile user) {
		em.persist(user);
	}

	@Override
	public boolean isUserUnique(Profile user) {
		Profile userTest = null;
		try {
			Query q = em.createQuery("SELECT r from Profile r WHERE r.login=:login OR r.email=:email", Profile.class);
			q.setParameter("login", user.getLogin());
			q.setParameter("email", user.getEmail());
			userTest = (Profile) q.getSingleResult();
		} catch(NoResultException ex) { }
		if(userTest != null)
			return false;
		return true;
	}

	@Override
	public Profile findUserByLogin(String login, boolean lazyInitialize) {
		Profile user = null;
		try {
			Query q = em.createQuery("SELECT r from Profile r WHERE r.login=:login", Profile.class);
			q.setParameter("login", login);
			user = (Profile) q.getSingleResult();
			if(lazyInitialize)
				Hibernate.initialize(user.getRoomsList());
		} catch(NoResultException ex) { }
		
		return user;
	}
	@Override
	public void saveRoom(Room room) {
		em.persist(room);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Room> getAllRooms() {
		List<Room> list = new ArrayList<Room>();
		try {
			Query q = em.createQuery("SELECT r from Room r");
			list = (List<Room>) q.getResultList();
			for(Room room: list) {
				Hibernate.initialize(room.getUserId());
			}
		} catch(NoResultException e) { }
		return list;
	}
	@Override
	public Room findRoomById(int id) {
		Room room = em.find(Room.class, id);
		Hibernate.initialize(room.getUserId());
		return room;
	}
	@Override
	public void deleteRoomById(int id) {
		Room room = em.find(Room.class, id);
		if(room != null)
			em.remove(room);
	}
}
