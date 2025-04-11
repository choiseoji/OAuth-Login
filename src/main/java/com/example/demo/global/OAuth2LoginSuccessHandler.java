package com.example.demo.global;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.enumerate.MemberRole;
import com.example.demo.domain.oauth.GoogleUserDetails;
import com.example.demo.global.jwt.JwtTokenProvider;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {

        // 멤버 추출
        GoogleUserDetails googleUser = (GoogleUserDetails) authentication.getPrincipal();
        Member member = googleUser.getMember();

        // accessToken 발급
        String accessToken = jwtTokenProvider.generateToken(
                "access",
                member.getProviderId(),
                MemberRole.USER,
                1000L * 60 * 60 * 5);
        response.setHeader("Authorization", "Bearer " + accessToken);

        // refreshToken 발급
        String refreshToken = jwtTokenProvider.generateToken(
                "refresh",
                member.getProviderId(),
                MemberRole.USER,
                1000L * 60 * 60 * 24 * 7);
        // refreshToken을 쿠키에 저장하는 로직 필요

        response.sendRedirect("/oauth-success.html");
    }
}
