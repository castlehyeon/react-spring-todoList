package com.example.demo.config;

import com.example.demo.security.JwtAuthenticationFilter;
import lombok.extern.slf4j.Slf4j;
import org.apache.catalina.filters.CorsFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;


@EnableWebSecurity
@Slf4j
public class WebSecurityConfig{
    //6.0이후로 기존 WebSecurityConfigurerAdapter를 상속 후에, configure 메소드를 오버라이딩 하는 방식은 deprecated

    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception{
        //http 시큐리티 빌더
        httpSecurity.cors(cors -> cors.disable()).csrf(csrf -> csrf.disable()) // csrf disable
                .httpBasic(httpBasic -> httpBasic.disable())//token 사용으로 basic 인증 disable
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests((authorize) -> authorize
                        .requestMatchers( "/auth/**", "/").permitAll()
                        .anyRequest().authenticated());
        //filter 등록
        //매 요청마다 CorsFilter 실행한 후에
        //jwtAuthenticationFilter 실행
        httpSecurity.addFilterAfter(
                jwtAuthenticationFilter,
                CorsFilter.class
        );

        return httpSecurity.build();
    }
}