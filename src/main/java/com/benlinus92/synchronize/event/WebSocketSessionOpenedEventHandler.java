package com.benlinus92.synchronize.event;

import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

import com.benlinus92.synchronize.service.SynchronizeService;

@Component
public class WebSocketSessionOpenedEventHandler implements ApplicationListener<SessionConnectEvent> {

	@Autowired
	public SynchronizeService service;
	@Override
	public void onApplicationEvent(SessionConnectEvent event) {
		StompHeaderAccessor sha = StompHeaderAccessor.wrap(event.getMessage());
		//service.addUserToRoomMap(sha.getNativeHeader("roomId").get(0), sha.getSessionId());
	}

}
