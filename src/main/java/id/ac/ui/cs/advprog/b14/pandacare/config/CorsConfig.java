package id.ac.ui.cs.advprog.b14.pandacare.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
public class CorsConfig {

    @Bean
    public CorsFilter corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();

        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("https://pandacare.netlify.app");
        config.addAllowedOrigin("http://pandacare.netlify.app");
        config.addAllowedOrigin("http://159.89.209.87:3003");
        config.addAllowedOrigin("https://159.89.209.87:3003");
        config.addAllowedOrigin("https://pandacare.abhipraya.dev");
        // Allow credentials
        config.setAllowCredentials(true);
        // Allow all headers
        config.addAllowedHeader("*");
        // Allow all methods
        config.addAllowedMethod("*");

        source.registerCorsConfiguration("/**", config);
        return new CorsFilter(source);
    }
}