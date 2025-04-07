package com.example.demo.domain.oauth;

import com.example.demo.domain.member.entity.Member;
import com.example.demo.domain.member.enumerate.MemberRole;
import com.example.demo.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class GoogleUserDetailsService extends DefaultOAuth2UserService {

    private final MemberRepository memberRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {

        // 1. 구글에서 유저 정보 받아오기
        OAuth2User oAuth2User = super.loadUser(userRequest);
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 2. 구글의 고유 식별자(sub)와 provider 추출
        String providerId = (String) attributes.get("sub");
        String provider = userRequest.getClientRegistration().getRegistrationId();

        // 3. DB에서 사용자 찾기
        // 나중에 다른 oauth를 추가할수도 있으니깐 provider+id 조합으로 조회하기
        Member member = memberRepository.findByProviderAndProviderId(provider, providerId)
                .orElseGet(() -> {

                    Member newMember = Member.builder()
                            .provider("google")
                            .providerId(providerId)
                            .role(MemberRole.USER)
                            .build();
                    return memberRepository.save(newMember);
                });

        // 4. GoogleUserDetails 반환
        return new GoogleUserDetails(member, attributes);
    }
}
