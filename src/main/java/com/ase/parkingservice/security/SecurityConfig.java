package com.ase.parkingservice.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthConverter());

    http
        .csrf(csrf -> csrf.disable())
        .authorizeHttpRequests(authorize -> authorize
            .requestMatchers("/actuator/health").permitAll()
            .requestMatchers("/api/parkingservice").hasRole("DEFAULT-ROLES-SAU")
            .requestMatchers("/admin/**").hasRole("ADMIN")
            .anyRequest().authenticated() // Alle anderen Requests benötigen Auth
        )
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt
                .jwtAuthenticationConverter(jwtConverter)
            )
        )
        .exceptionHandling(exception -> exception
          .authenticationEntryPoint(new JwtAuthenticationEntryPoint()) // -> 401
          .accessDeniedHandler(new JwtAccessDeniedHandler()) // -> 403
        );

    return http.build();
  }
}
