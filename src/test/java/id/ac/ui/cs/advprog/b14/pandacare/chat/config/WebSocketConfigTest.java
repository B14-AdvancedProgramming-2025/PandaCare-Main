package id.ac.ui.cs.advprog.b14.pandacare.chat.config;

import org.junit.jupiter.api.Test;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;

import static org.mockito.Mockito.*;

class WebSocketConfigTest {

    @Test
    void testConfigureMessageBroker() {
        MessageBrokerRegistry registry = mock(MessageBrokerRegistry.class);
        
        WebSocketConfig config = new WebSocketConfig();
        config.configureMessageBroker(registry);
        
        verify(registry).enableSimpleBroker("/topic");
        verify(registry).setApplicationDestinationPrefixes("/app");
    }

    @Test
    void testRegisterStompEndpoints() {
        StompEndpointRegistry registry = mock(StompEndpointRegistry.class);
        when(registry.addEndpoint(anyString())).thenReturn(registry);
        
        WebSocketConfig config = new WebSocketConfig();
        config.registerStompEndpoints(registry);
        
        verify(registry).addEndpoint("/ws");
        verify(registry).withSockJS();
    }
} 