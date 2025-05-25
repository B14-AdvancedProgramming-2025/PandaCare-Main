package id.ac.ui.cs.advprog.b14.pandacare.chat.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class ChatProfilingAspect {
    
    private static final Logger logger = LoggerFactory.getLogger(ChatProfilingAspect.class);
    private final MeterRegistry meterRegistry;
    
    public ChatProfilingAspect(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
    }
    
    @Around("@annotation(org.springframework.messaging.handler.annotation.MessageMapping)")
    public Object profileChatEndpoints(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        Timer.Sample sample = Timer.start(meterRegistry);
        
        try {
            Object result = joinPoint.proceed();
            
            sample.stop(Timer.builder("chat.endpoint.duration")
                    .tag("method", methodName)
                    .tag("status", "success")
                    .register(meterRegistry));
            
            meterRegistry.counter("chat.endpoint.calls", "method", methodName).increment();
            
            logger.info("Chat endpoint {} executed successfully in {} ms", 
                    methodName, sample.stop(meterRegistry.timer("chat.timer")));
            
            return result;
        } catch (Exception e) {
            sample.stop(Timer.builder("chat.endpoint.duration")
                    .tag("method", methodName)
                    .tag("status", "error")
                    .register(meterRegistry));
            
            meterRegistry.counter("chat.endpoint.errors", "method", methodName).increment();
            
            logger.error("Chat endpoint {} failed", methodName, e);
            throw e;
        }
    }
    
    @Around("execution(* id.ac.ui.cs.advprog.b14.pandacare.chat.service.*.*(..))")
    public Object profileChatService(ProceedingJoinPoint joinPoint) throws Throwable {
        String methodName = joinPoint.getSignature().getName();
        long startTime = System.currentTimeMillis();
        
        try {
            Object result = joinPoint.proceed();
            long duration = System.currentTimeMillis() - startTime;
            
            meterRegistry.timer("chat.service.duration", "method", methodName)
                    .record(duration, java.util.concurrent.TimeUnit.MILLISECONDS);
            
            if (duration > 100) {
                logger.warn("Chat service method {} took {} ms", methodName, duration);
            }
            
            return result;
        } catch (Exception e) {
            meterRegistry.counter("chat.service.errors", "method", methodName).increment();
            throw e;
        }
    }
} 