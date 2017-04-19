package com.benlinus92.synchronize.dao;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
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

import com.benlinus92.synchronize.config.AppConstants;
import com.benlinus92.synchronize.model.Playlist;
import com.benlinus92.synchronize.model.Profile;
import com.benlinus92.synchronize.model.Room;
import com.benlinus92.synchronize.model.WaitingUser;

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
				if(room != null)
					Hibernate.initialize(room.getUserId());
			}
		} catch(NoResultException e) { 
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Room findRoomById(int id) {
		Room room = em.find(Room.class, id);
		if(room != null) {
			Hibernate.initialize(room.getUserId());
			Hibernate.initialize(room.getVideosList());
		}
		return room;
	}
	@Override
	public void deleteRoomById(int id) {
		Room room = em.find(Room.class, id);
		if(room != null)
			em.remove(room);
	}
	@Override
	public void saveVideo(Playlist video) {
		em.persist(video);
		em.flush();
		System.out.println(video.getVideoId());
	}
	@SuppressWarnings("unchecked")
	@Override
	public List<Playlist> getAllVideos() {
		List<Playlist> list = new ArrayList<Playlist>();
		try {
			Query q = em.createQuery("SELECT r from Playlist r");
			list = (List<Playlist>) q.getResultList();
			for(Playlist video: list) {
				if(video != null)
					Hibernate.initialize(video.getRoom());
			}
		} catch(NoResultException e) {
			e.printStackTrace();
		}
		return list;
	}
	@Override
	public Playlist findVideoById(int videoId) {
		return em.find(Playlist.class, videoId);
	}
	@Override
	public void deleteVideoById(int videoId) {
		Playlist video = em.find(Playlist.class, videoId);
		try {
			if(video != null) {
				int roomId = video.getRoom().getRoomId();
				if(video.getType().equals(AppConstants.TYPE_UPLOAD_VIDEO))
					Files.deleteIfExists(Paths.get(AppConstants.VIDEOSTORE_LOCATION + roomId + "/" + video.getUrl()));
				em.remove(video);
			}
		} catch(IOException e) {
			e.printStackTrace();
		}
	}
	@Override
	public void updateVideoTime(int videoId, String currTime) {
		Playlist video = em.find(Playlist.class, videoId);
		if(video != null)
			video.setCurrTime(currTime);
	}
}
