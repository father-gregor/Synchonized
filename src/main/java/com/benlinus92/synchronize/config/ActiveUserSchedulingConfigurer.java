package com.benlinus92.synchronize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;

import com.benlinus92.synchronize.scheduler.ActiveUserScheduler;

@Configuration
@EnableScheduling
public class ActiveUserSchedulingConfigurer implements SchedulingConfigurer {

	@Bean
	public ThreadPoolTaskScheduler taskScheduler() {
		return new ThreadPoolTaskScheduler();
	}
	@Bean
	public ActiveUserScheduler activeUserTask() {
		return new ActiveUserScheduler();
	}
	@Override
	public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
		taskRegistrar.setScheduler(taskScheduler());
	}
}
