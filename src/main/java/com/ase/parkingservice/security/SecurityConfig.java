package com.ase.parkingservice.security;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();

    jwtConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthConverter());

//the role always has to be capitalized

    http

        .csrf(

            csrf -> csrf.disable()) // Disable CSRF for API endpoints isnt needed

        .authorizeHttpRequests(authorize -> authorize

            .requestMatchers("/demo").hasRole("DEFAULT-ROLES-SAU")

            .requestMatchers("/admin/**").hasRole("admin")

            .anyRequest().authenticated())

// enable browser session login (keeps session if user logged in via browser)
        .oauth2Login(oauth2 -> oauth2
            .userInfoEndpoint(user -> user.userAuthoritiesMapper(keycloakAuthoritiesMapper())))

        .oauth2ResourceServer(oauth2 -> oauth2

            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

    return http.build();
  }

  // map OIDC groups to ROLE_... just like JwtAuthConverter does for JWT
  @Bean
  public GrantedAuthoritiesMapper keycloakAuthoritiesMapper() {
    return authorities -> authorities.stream().flatMap(a -> {
      if (a instanceof org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority oua) {
        var claims = oua.getIdToken().getClaims();
        @SuppressWarnings("unchecked")
        var groups = (List<String>) claims.getOrDefault("groups", List.of());
        return groups.stream()
            .map(g -> new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()));
      }
      return Stream.of(a);
    }).collect(Collectors.toSet());
  }
}
