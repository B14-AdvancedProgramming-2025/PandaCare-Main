package id.ac.ui.cs.advprog.b14.pandacare.chat.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class WebSocketProfilingInterceptor implements ChannelInterceptor {
    
    private static final Logger logger = LoggerFactory.getLogger(WebSocketProfilingInterceptor.class);
    private final ConcurrentHashMap<String, Long> messageTimings = new ConcurrentHashMap<>();
    private final AtomicLong totalMessages = new AtomicLong(0);
    private final AtomicLong totalProcessingTime = new AtomicLong(0);
    
    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        
        if (sessionId != null && accessor.getCommand() != null) {
            messageTimings.put(sessionId + "_" + message.getHeaders().getId(), System.currentTimeMillis());
            
            if (accessor.getCommand() == StompCommand.SEND) {
                logger.debug("WebSocket SEND message from session: {}, destination: {}", 
                        sessionId, accessor.getDestination());
            }
        }
        
        return message;
    }
    
    @Override
    public void postSend(Message<?> message, MessageChannel channel, boolean sent) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);
        String sessionId = accessor.getSessionId();
        
        if (sessionId != null && message.getHeaders().getId() != null) {
            String key = sessionId + "_" + message.getHeaders().getId();
            Long startTime = messageTimings.remove(key);
            
            if (startTime != null) {
                long processingTime = System.currentTimeMillis() - startTime;
                totalMessages.incrementAndGet();
                totalProcessingTime.addAndGet(processingTime);
                
                if (processingTime > 50) {
                    logger.warn("WebSocket message processing took {} ms for session: {}", 
                            processingTime, sessionId);
                }
                
                if (totalMessages.get() % 100 == 0) {
                    long avgTime = totalProcessingTime.get() / totalMessages.get();
                    logger.info("WebSocket Stats - Total messages: {}, Avg processing time: {} ms", 
                            totalMessages.get(), avgTime);
                }
            }
        }
    }
    
    public long getTotalMessages() {
        return totalMessages.get();
    }
    
    public long getAverageProcessingTime() {
        long total = totalMessages.get();
        return total > 0 ? totalProcessingTime.get() / total : 0;
    }
} 