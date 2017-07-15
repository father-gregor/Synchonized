package com.benlinus92.synchronize.scheduler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Scheduled;

public class ActiveUserScheduler {
	
	@Autowired
	private SimpMessagingTemplate simp;
	
	@Scheduled(fixedDelay=5000)
	public void checkUsers() {
		simp.convertAndSend("/topic/alive", "");
	}
}
