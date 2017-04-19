package com.benlinus92.synchronize.interceptor;

import java.io.ByteArrayOutputStream;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.HandshakeInterceptor;

public class SessionAttributeHandshakeInterceptor implements HandshakeInterceptor {

	@Override
	public boolean beforeHandshake(ServerHttpRequest req, ServerHttpResponse resp, WebSocketHandler ws,
			Map<String, Object> attributes) throws Exception {
		for(Entry<String, List<String>> ent: req.getHeaders().entrySet()) {
			System.out.println("Intercept: " + ent.getKey());
			for(String el: ent.getValue()) {
				System.out.println("   Value: " + el);
			}
		}
		for(Entry<String, List<String>> ent: resp.getHeaders().entrySet()) {
			System.out.println("Response: " + ent.getKey());
			for(String el: ent.getValue()) {
				System.out.println("   Value: " + el);
			}
		}
		return true;
	}
	
	@Override
	public void afterHandshake(ServerHttpRequest req, ServerHttpResponse resp,
			WebSocketHandler ws, Exception ex) {
		
	}

}