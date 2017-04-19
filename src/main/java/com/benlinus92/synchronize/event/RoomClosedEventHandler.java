package com.benlinus92.synchronize.event;

import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.benlinus92.synchronize.service.SynchronizeService;

@Component
public class RoomClosedEventHandler implements ApplicationListener<SessionDisconnectEvent> {

	@Autowired
	private SynchronizeService service;
	
	@Override
	public void onApplicationEvent(SessionDisconnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		//System.out.println("ID: " + event.getMessage().getHeaders().ID);
		for(Entry<String, Object> entry: sha.toMap().entrySet()) {
			System.out.println("MAP: " + entry.getKey() + " - " + entry.getValue());
		}
		System.out.println("\n\n");
	}

}
