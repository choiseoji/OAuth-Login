package com.example.demo.global.config;

import com.example.demo.domain.member.enumerate.MemberRole;
import com.example.demo.domain.oauth.GoogleUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GoogleUserDetailsService googleUserDetailsService;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        // CSRF 비활성화
        http
                .csrf(AbstractHttpConfigurer::disable);

        // STATELESS
        http.sessionManagement(session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // 접근 권한 설정
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/oauth-login/admin").hasRole(MemberRole.ADMIN.name())
                        .requestMatchers("/oauth-login/info").authenticated()
                        .anyRequest().permitAll()
                );

        // OAuth 2.0 로그인 방식 설정
        http
                .oauth2Login((auth) -> auth.loginPage("/oauth-login/login")  // 여긴 로그인 페이지로 이동하는 url (커스튬 페이지인데 만들지 X)
                        .defaultSuccessUrl("/oauth-success.html")  // 로그인 성공했을 때 이동하는 url
                        .failureUrl("/oauth-login/login")
                        .userInfoEndpoint(userInfo -> userInfo
                                .userService(googleUserDetailsService))  // 구글이 accessToken을 넘겨주면, 토큰을 가지고 구글의 UserInfo 엔드포인트로 가서 사용자 정보를 가져옴
                        .permitAll());

        return http.build();
    }

    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
