package com.prgrms.needit.common.config;

import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

	@Override
	public void configureMessageBroker(MessageBrokerRegistry registry) {
		// "/topic/center/**": subscription for center's donation wish articles.
		// "/topic/notifications": subscription for general activity notification for user.
		registry.enableSimpleBroker("/topic/center/**", "/topic/notifications");
		// not using now. client have nothing to send at now.
		registry.setApplicationDestinationPrefixes("/app");
	}

	@Override
	public void registerStompEndpoints(StompEndpointRegistry registry) {
		registry.addEndpoint("/stomp-handshake").withSockJS();
	}
}
