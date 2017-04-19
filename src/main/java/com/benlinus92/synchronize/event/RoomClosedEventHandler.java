package com.benlinus92.synchronize.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionUnsubscribeEvent;

import com.benlinus92.synchronize.service.SynchronizeService;

@Component
public class RoomClosedEventHandler implements ApplicationListener<SessionUnsubscribeEvent> {

	@Autowired
	private SynchronizeService service;
	
	@Override
	public void onApplicationEvent(SessionUnsubscribeEvent event) {
		System.out.println("UNSUBSCRIBE: " + event.getUser().getName() + "  ; PLACE: " + event.getMessage().toString());
		System.out.println("ID: " + event.getMessage().getHeaders().ID);
		for(String header: event.getMessage().getHeaders().keySet()) {
			System.out.println("HEADER: " + header);
		}
		System.out.println("\n\n");
	}

}
