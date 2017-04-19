package com.benlinus92.synchronize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.AbstractWebSocketMessageBrokerConfigurer;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import com.benlinus92.synchronize.interceptor.SessionAttributeHandshakeInterceptor;

@Configuration
@EnableWebSocketMessageBroker
public class AppWebSocketConfig extends AbstractWebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry config) {
		config.enableSimpleBroker("/topic", "/queue");
		config.setApplicationDestinationPrefixes("/app");
	}
	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/videoroom").withSockJS().setInterceptors(sessionAttributeHandshakeInterceptor());
	}
	@Bean
	public SessionAttributeHandshakeInterceptor sessionAttributeHandshakeInterceptor() {
		return new SessionAttributeHandshakeInterceptor();
	}
	
}
