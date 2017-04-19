package com.benlinus92.synchronize.event;

import java.util.Map.Entry;

import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

@Component
public class VideolistReceivedEventHandler implements ApplicationListener<SessionConnectEvent> {

	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		for(Entry<String, Object> entry: sha.toMap().entrySet()) {
			System.out.println("MAP: " + entry.getKey() + " - " + entry.getValue());
		}
		System.out.println("\n\n");
	}

}
