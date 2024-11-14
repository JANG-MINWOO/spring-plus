// package org.example.expert.config;
//
// import org.springframework.context.annotation.Bean;
// import org.springframework.context.annotation.Configuration;
// import org.springframework.security.authentication.AuthenticationManager;
// import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
// import org.springframework.security.config.annotation.web.builders.HttpSecurity;
// import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
// import org.springframework.security.web.SecurityFilterChain;
//
// @Configuration
// @EnableWebSecurity // Spring Security 지원을 가능하게 함
// public class WebSecurityConfig {
//
// 	@Bean
// 	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
// 		// CSRF 설정
// 		http.csrf((csrf) -> csrf.disable());
//
// 		http.authorizeHttpRequests((authorizeHttpRequests) ->
// 			authorizeHttpRequests
// 				.requestMatchers("/auth/**").permitAll() // '/auth/'로 시작하는 요청 모두 접근 허가
// 				.anyRequest().authenticated() // 그 외 모든 요청 인증처리
// 		);
//
// 		http.authenticationManager(
// 			authenticationManager(http.getSharedObject(AuthenticationConfiguration.class))
// 		);
//
// 		return http.build();
// 	}
//
// 	@Bean
// 	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
// 		return authenticationConfiguration.getAuthenticationManager();
// 	}
// }
