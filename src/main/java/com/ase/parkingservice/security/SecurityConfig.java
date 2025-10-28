package com.ase.parkingservice.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

  /**
   * API-Security: rein stateless, nur Bearer-JWT, CSRF aus, eigene Authorize-Regeln.
   */
  @Bean
  @Order(1)
  public SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {
    JwtAuthenticationConverter jwtConverter = new JwtAuthenticationConverter();
    jwtConverter.setJwtGrantedAuthoritiesConverter(new JwtAuthConverter());

    http
        .securityMatcher("/api/**")
        .csrf(csrf -> csrf.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/api/admin/**").hasRole("ADMIN")
            .requestMatchers("/api/demo").hasRole("DEFAULT-ROLES-SAU")
            .anyRequest().authenticated())
        .oauth2ResourceServer(oauth2 -> oauth2
            .jwt(jwt -> jwt.jwtAuthenticationConverter(jwtConverter)));

    return http.build();
  }

  /**
   * Web-Security: OAuth2-Login (OIDC), Session-basiert, CSRF aktiv außer für /api/**.
   */
  @Bean
  @Order(2)
  public SecurityFilterChain webSecurity(HttpSecurity http, OAuth2UserService<OidcUserRequest,
      OidcUser> oidcUserService) throws Exception {

    http
        .securityMatcher("/**")
        .csrf(csrf -> csrf.ignoringRequestMatchers("/api/**"))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/", "/assets/**", "/login", "/error").permitAll()
            .anyRequest().authenticated())
        .oauth2Login(oauth -> oauth
            .userInfoEndpoint(u -> u.oidcUserService(oidcUserService)))
        .logout(l -> l.logoutSuccessUrl("/"));

    return http.build();
  }

  /**
   * OIDC-UserService: übernimmt Gruppen/Claims in ROLE_*-Authorities,
   * passend zum JwtAuthConverter (hasRole bleibt konsistent).
   */
  @Bean
  public OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
    OidcUserService delegate = new OidcUserService();
    return (OidcUserRequest req) -> {
      OidcUser user = delegate.loadUser(req);

      Set<GrantedAuthority> mapped = new HashSet<>(user.getAuthorities());

      // Versuche "groups" analog zum JWT
      @SuppressWarnings("unchecked")
      Collection<String> groups = (Collection<String>) user.getAttributes().get("groups");
      if (groups != null) {
        for (String g : groups) {
          mapped.add(new SimpleGrantedAuthority("ROLE_" + g.toUpperCase()));
        }
      }

      // Optional: realm_access.roles (z. B. Keycloak)
      Object realmAccess = user.getAttributes().get("realm_access");
      if (realmAccess instanceof java.util.Map<?, ?> realmMap) {
        Object rolesObj = realmMap.get("roles");
        if (rolesObj instanceof Collection<?> roles) {
          for (Object r : roles) {
            if (r != null) {
              mapped.add(new SimpleGrantedAuthority("ROLE_" + r.toString().toUpperCase()));
            }
          }
        }
      }

      return new DefaultOidcUser(mapped, user.getIdToken(),
          user.getUserInfo(), "preferred_username");
    };
  }
}
