package com.example.demo.domain.member.entity;

import com.example.demo.domain.member.enumerate.MemberRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Member {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="member_id")
    private Long id;

//    private String name;

    @Enumerated(EnumType.STRING)
    private MemberRole role;

    private String provider;

    // 구글 로그인한 유저의 고유 아이디가 들어감
    private String providerId;

    @Builder
    public Member(MemberRole role, String provider, String providerId) {
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
