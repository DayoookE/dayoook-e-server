package inha.dayoook_e.common.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns("http://localhost:3000", "http://dayoook2-e-s3.s3-website.ap-northeast-2.amazonaws.com", "http://165.246.44.76:6262")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")  // OPTIONS 추가
                .allowedHeaders("*")
                .exposedHeaders("*")  // 추가
                .allowCredentials(true)
                .maxAge(3600);  // preflight 캐시 시간 추가
    }

}