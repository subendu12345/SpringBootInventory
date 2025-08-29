package com.prod.GreenValley.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
// Enable method-level security with @PreAuthorize, etc.
@EnableMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfig {

     @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailService();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }   
    

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, CustomAuthenticationSuccessHandler successHandler) throws Exception {
       
        http.authorizeHttpRequests(authorizeRequests ->
            authorizeRequests
                .requestMatchers(
                    "/img/**",          // Allows all images
                    "/css/**"       // Allows all CSS files          // Allows the form action URL
                ).permitAll()
                .anyRequest().authenticated()
        ).formLogin(formLogin ->
            formLogin
                .loginPage("/signin")
                .successHandler(successHandler)
                .permitAll()
        ).logout(logout ->
            logout
                .logoutSuccessUrl("/")
        ).csrf().disable();
        
		return http.build();
    }

    // Configuration of the authentication manager 
    @Bean public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception 
    { 
        return http.getSharedObject(AuthenticationManagerBuilder.class) 
        .authenticationProvider(authenticationProvider()).build(); 
    }
}