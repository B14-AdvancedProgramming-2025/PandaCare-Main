package id.ac.ui.cs.advprog.b14.pandacare.chat.controller;

import id.ac.ui.cs.advprog.b14.pandacare.chat.config.WebSocketProfilingInterceptor;
import id.ac.ui.cs.advprog.b14.pandacare.chat.service.ChatService;
import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/chat/metrics")
public class ChatMetricsController {
    
    private final WebSocketProfilingInterceptor profilingInterceptor;
    private final MeterRegistry meterRegistry;
    private final ChatService chatService;
    
    public ChatMetricsController(WebSocketProfilingInterceptor profilingInterceptor, 
                                MeterRegistry meterRegistry,
                                ChatService chatService) {
        this.profilingInterceptor = profilingInterceptor;
        this.meterRegistry = meterRegistry;
        this.chatService = chatService;
    }
    
    @GetMapping
    public Map<String, Object> getChatMetrics() {
        Map<String, Object> metrics = new HashMap<>();
        
        metrics.put("totalMessages", profilingInterceptor.getTotalMessages());
        metrics.put("averageProcessingTimeMs", profilingInterceptor.getAverageProcessingTime());
        
        meterRegistry.getMeters().stream()
                .filter(meter -> meter.getId().getName().startsWith("chat."))
                .forEach(meter -> {
                    String name = meter.getId().getName();
                    if (meter.measure().iterator().hasNext()) {
                        metrics.put(name, meter.measure().iterator().next().getValue());
                    }
                });
        
        return metrics;
    }
    
    @GetMapping("/performance")
    public Map<String, Object> getPerformanceMetrics() {
        Map<String, Object> performance = new HashMap<>();
        
        performance.put("websocket", Map.of(
                "totalMessages", profilingInterceptor.getTotalMessages(),
                "avgProcessingTimeMs", profilingInterceptor.getAverageProcessingTime()
        ));
        
        performance.put("jvm", Map.of(
                "heapUsedMB", Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory() / 1024 / 1024,
                "heapMaxMB", Runtime.getRuntime().maxMemory() / 1024 / 1024,
                "threads", Thread.activeCount()
        ));
        
        return performance;
    }
} 