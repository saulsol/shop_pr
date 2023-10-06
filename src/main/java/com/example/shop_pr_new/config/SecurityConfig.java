package com.example.shop_pr_new.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    // filterChain 을 리턴하는 메소드 구현
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        // httpSecurity 객체 -> 세부적인 보안 기능을 설정할 수 있는 API 제공
        httpSecurity.formLogin()
                .loginPage("/members/login") // 로그인 페이지 URL 설정
                .defaultSuccessUrl("/") // 로그인 성공 시 이동할 URL 설정
                .usernameParameter("email") // -> UserDetail 객체에서 검증하기 위해, 로그인 시 사용할 파라미터 이름 email 지정
                .failureUrl("/members/login/error") // 로그인 실패 시 이동할 URL 지정
                .and()
                .logout()
                .logoutRequestMatcher(new AntPathRequestMatcher("/members/logout")) // 로그아웃 URL 성공 시 이동할 URl 설정
                .logoutSuccessUrl("/")
        ;

        httpSecurity.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;



        return httpSecurity.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
