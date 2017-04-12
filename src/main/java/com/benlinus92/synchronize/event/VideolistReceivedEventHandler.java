package com.benlinus92.synchronize.event;

import org.springframework.context.ApplicationListener;
import org.springframework.web.socket.messaging.SessionSubscribeEvent;

public class VideolistReceivedEventHandler implements ApplicationListener<SessionSubscribeEvent> {

	@Override
	public void onApplicationEvent(SessionSubscribeEvent event) {
		System.out.println("SUBSCRIBE: " + event.getUser().getName() + "  ; PLACE: " + event.getMessage().toString());
	}

}
