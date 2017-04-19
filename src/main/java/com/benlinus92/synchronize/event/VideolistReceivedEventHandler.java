package com.benlinus92.synchronize.event;

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
		System.out.println("Room id: " + sha.getNativeHeader("roomId").get(0));
		System.out.println("SUBSCRIBE: " + event.getUser().getName() + "  ; PLACE: " + event.getMessage().toString());
		System.out.println("ID: " + event.getMessage().getHeaders().ID);
		for(String header: event.getMessage().getHeaders().keySet()) {
			System.out.println("HEADER: " + header);
		}
		System.out.println("\n\n");
	}

}
