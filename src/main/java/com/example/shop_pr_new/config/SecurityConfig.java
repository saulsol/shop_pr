package com.example.shop_pr_new.config;

import com.example.shop_pr_new.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final MemberService userDetailsService;


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

        // form 로그인 시 POST 요청으로 유저의 정보를 받아서 인증할 필요가 없다.
        // 시큐리티가 필터에서 가로채서 인증을 진행하기 때문에 위와 같이 설정하면 된다.

        httpSecurity.authorizeRequests()
                .mvcMatchers("/css/**", "/js/**", "/img/**").permitAll()
                .mvcMatchers("/", "/members/**", "/item/**", "/images/**").permitAll()
                .mvcMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        ;



        return httpSecurity.build();
    }


    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity, BCryptPasswordEncoder passwordEncoder) throws Exception{
        return httpSecurity.getSharedObject(AuthenticationManagerBuilder.class)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder)
                .and()
                .build();
    }

    // AuthenticationManager는 loadUserByUsername()을 통해 DB에서
    // 갖고와진 유저 인증 객체로 컨텍스트에 저장할 인증객체(Authentication) 객체를 생성하는 역할을 위임받는다.



}
