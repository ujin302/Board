package com.example.board.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
// 스프링 동작 시, 설저을 관리하는 객체
public class WebConfig implements WebMvcConfigurer {
    // WebMvcConfigurer : 사용자가 원하는 세팅 추가 가능
    // ex. Override

    private String resourcePath = "/upload/**"; // view에서 접근할 경로
    private String savePath = "file:///E:/Board_File/"; // 실제 파일 저장 경로
    // 경로 지정 방법 : file:/// + 지정된 경로

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler(resourcePath) // 웹에서 접근할 URL
                .addResourceLocations(savePath); // 실제 파일이 존재하는 경로
        // resourcePath 로 접근하면 savePath에서 찾는다!
    }
}
