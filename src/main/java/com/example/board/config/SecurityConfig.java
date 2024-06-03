package com.example.board.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.header.writers.frameoptions.XFrameOptionsHeaderWriter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity // 모든 요청 URL이 스프링 시큐리티의 제어를 받는다.
public class SecurityConfig {

    /*
    SecurityFilterChain 클래스
    모든 요청 URL에 이 클래스가 필터로 적용되어 URL별로 특별한 설정을 할 수 있다.
    스프링 시큐리티의 세부 설정은 @Bean ㅇ어노테이션을 통해 SecurityFilterChain 빈을 생성하여 설정할 수 있다.
     */

    @Bean
    SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        // 해당 부분은 인증되지 않은 모든 페이지의 요청을 허락한다는 의미
        // 로그인 하지 않더라도 모든 페이지에 접근 가능
        http.authorizeHttpRequests((authorizeHttpRequests) -> authorizeHttpRequests
                        .requestMatchers(new AntPathRequestMatcher("/**")).permitAll())
                .csrf((csrf) -> csrf.ignoringRequestMatchers(new AntPathRequestMatcher("/**")))
                .headers((headers) -> headers.addHeaderWriter(
                        new XFrameOptionsHeaderWriter(XFrameOptionsHeaderWriter.XFrameOptionsMode.SAMEORIGIN)));

        return http.build();
    }
}
