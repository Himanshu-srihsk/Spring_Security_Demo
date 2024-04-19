package com.example.SpringSecurityJWT.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import jakarta.servlet.Filter;

import static com.example.SpringSecurityJWT.User.Permission.*;
import static com.example.SpringSecurityJWT.User.Role.ADMIN;
import static com.example.SpringSecurityJWT.User.Role.MEMBER;
import static org.springframework.http.HttpMethod.GET;
import static org.springframework.http.HttpMethod.POST;
import static org.springframework.security.config.http.SessionCreationPolicy.STATELESS;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
@EnableMethodSecurity
public class SecurityConfiguration {
	private final AuthenticationProvider authenticationProvider;
    private final JwtAuthFilter jwtAuthFilter;
    
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(req ->
                        req.requestMatchers("/v1/auth/*")
                                .permitAll()
                                .requestMatchers("/v1/management/**").hasAnyRole(ADMIN.name(), MEMBER.name())
                                .requestMatchers(GET, "/v1/management/**").hasAnyAuthority(ADMIN_READ.name(), MEMBER_READ.name())
                                .requestMatchers(POST, "/v1/management/**").hasAnyAuthority(ADMIN_CREATE.name(), MEMBER_CREATE.name())
                                .anyRequest()
                                .authenticated())
                .sessionManagement(session -> session.sessionCreationPolicy(STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore((Filter)jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }
}
