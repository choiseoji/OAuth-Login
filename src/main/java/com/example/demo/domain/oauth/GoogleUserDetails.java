package com.example.demo.domain.oauth;

import com.example.demo.domain.member.entity.Member;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

public class GoogleUserDetails implements OAuth2User {

    private final Member member;
    private final Map<String, Object> attributes;  // 구글에서 받아온 정보들

    public GoogleUserDetails(Member member, Map<String, Object> attributes) {
        this.member = member;
        this.attributes = attributes;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Collection<GrantedAuthority> collection = new ArrayList<>();
        collection.add(new GrantedAuthority() {
            @Override
            public String getAuthority() {
                return member.getRole().name();
            }
        });

        return collection;
    }

    @Override
    public String getName() {
        return (String)attributes.get("name");
    }

    // google oauth 관련 추가 메서드
    public String getProvider() {
        return "google";
    }

    public String getProviderId() {
        return (String)attributes.get("sub");
    }

    public String getEmail() {
        return (String)attributes.get("email");
    }
}
