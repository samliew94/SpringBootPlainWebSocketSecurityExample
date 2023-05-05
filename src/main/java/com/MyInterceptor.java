package com;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketHandler;
import org.springframework.web.socket.server.support.HttpSessionHandshakeInterceptor;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class MyInterceptor extends HttpSessionHandshakeInterceptor {

	@Autowired
	UserDetailsService userDetailsService;
	
	@Override
	public boolean beforeHandshake(ServerHttpRequest request, ServerHttpResponse response, WebSocketHandler wsHandler,
			Map<String, Object> attributes) throws Exception {

		HttpServletRequest request2 = ((ServletServerHttpRequest) request).getServletRequest();
		
		try {
			/*
			 * naturally, we can't get the decoded password because BCryptPassword is one-way.
			 * when login, the password is encoded and is FINAL.
			 * There's no way to programmatically decode it even if you have access to database.
			 * This is where spring session comes in.
			 * with spring session, we are able to get the sessionId via request2, and that means we're able to lookup principal via autowired SpringRepository.
			 * 
			 */
			request2.login("sam", "password"); // naturally, we can't get the unencoded password because BCryptPassword is one-way. This is where spring session comes in
		} catch (Exception e) {
			System.err.println("failed to login websocket due to " + e.getMessage());
			return false;
		}
		
		return super.beforeHandshake(request, response, wsHandler, attributes); // basically returns true
	}
	
}
