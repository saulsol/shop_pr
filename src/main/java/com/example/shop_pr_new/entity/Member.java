package com.example.shop_pr_new.entity;

import com.example.shop_pr_new.constatnt.Role;
import com.example.shop_pr_new.dto.MemberFormDto;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;

@Entity
@Table(name = "member")
@Getter
@ToString
@NoArgsConstructor
public class Member {

    @Id
    @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Column(unique = true)
    private String email;

    private String password;

    private String address;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Builder
    public Member(String name, String email, String password, String address, Role role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
        this.role = role;
    }

    public static Member createMember(MemberFormDto memberFormDto, PasswordEncoder passwordEncoder){
        return Member.builder()
                .name(memberFormDto.getName())
                .email(memberFormDto.getEmail())
                .password(passwordEncoder.encode(memberFormDto.getPassword()))
                .role(Role.USER)
                .build();

    }

}
