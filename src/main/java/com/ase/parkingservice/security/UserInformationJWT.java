package com.ase.parkingservice.security;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;

/**
 * Helper-Klasse, die Benutzerinformationen sowohl aus JWT (Bearer)
 * als auch aus OIDC/OAuth2 (Session-Login) ausliest.
 */
public final class UserInformationJWT {

  private UserInformationJWT() {}

  private static Authentication getAuth() {
    return SecurityContextHolder.getContext().getAuthentication();
  }

  private static Jwt getCurrentJwt() {
    Authentication authentication = getAuth();
    if (authentication instanceof JwtAuthenticationToken) {
      return ((JwtAuthenticationToken) authentication).getToken();
    }
    return null;
  }

  public static boolean isAuthenticated() {
    return getAuth() != null && getAuth().isAuthenticated();
  }

  public static String getEmail() {
    Authentication auth = getAuth();
    if (auth instanceof JwtAuthenticationToken) {
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      String email = jwt.getClaimAsString("email");
      if (email == null) {
        email = jwt.getClaimAsString("preferred_username");
      }
      return email;
    }
    if (auth instanceof OAuth2AuthenticationToken) {
      Object principal = ((OAuth2AuthenticationToken) auth).getPrincipal();
      if (principal instanceof OidcUser) {
        OidcUser oidc = (OidcUser) principal;
        String email = oidc.getEmail();
        if (email == null) {
          Object attr = oidc.getAttributes().get("email");
          email = (attr != null) ? attr.toString() : null;
        }
        return email;
      }
      else {
        Object attr =
            ((OAuth2AuthenticationToken) auth).getPrincipal().getAttributes().get("email");
        return (attr != null) ? attr.toString() : null;
      }
    }
    return null;
  }

  public static String getUserId() {
    Authentication auth = getAuth();
    if (auth instanceof JwtAuthenticationToken) {
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      String sub = jwt.getClaimAsString("sub");
      return sub != null ? sub : jwt.getSubject();
    }
    if (auth instanceof OAuth2AuthenticationToken) {
      Object principal = ((OAuth2AuthenticationToken) auth).getPrincipal();
      if (principal instanceof OidcUser) {
        return ((OidcUser) principal).getSubject();
      }
      else {
        Object sub = ((OAuth2AuthenticationToken) auth).getPrincipal().getAttributes().get("sub");
        return sub != null ? sub.toString() : null;
      }
    }
    return null;
  }

  public static String getUsername() {
    Authentication auth = getAuth();
    if (auth instanceof JwtAuthenticationToken) {
      Jwt jwt = ((JwtAuthenticationToken) auth).getToken();
      String preferred = jwt.getClaimAsString("preferred_username");
      if (preferred != null) {
        return preferred;
      }
      String upn = jwt.getClaimAsString("upn");
      if (upn != null) {
        return upn;
      }
      return jwt.getClaimAsString("name");
    }
    if (auth instanceof OAuth2AuthenticationToken) {
      Object principal = ((OAuth2AuthenticationToken) auth).getPrincipal();
      if (principal instanceof OidcUser) {
        OidcUser oidc = (OidcUser) principal;
        String preferred = (String) oidc.getAttributes().get("preferred_username");
        if (preferred != null) {
          return preferred;
        }
        String name = oidc.getFullName();
        if (name != null) {
          return name;
        }
        Object n = oidc.getAttributes().get("name");
        return n != null ? n.toString() : null;
      }
      else {
        Object preferred = ((OAuth2AuthenticationToken) auth)
            .getPrincipal().getAttributes().get("preferred_username");
        if (preferred != null) {
          return preferred.toString();
        }
        Object n = ((OAuth2AuthenticationToken) auth).getPrincipal().getAttributes().get("name");
        return n != null ? n.toString() : null;
      }
    }
    return null;
  }
  private static final int ROLE_PREFIX_LENGTH = 5;
  public static Collection<String> getRoles() {
    Authentication auth = getAuth();
    if (auth == null) {
      return List.of();
    }
    return auth.getAuthorities()
        .stream()
        .map(GrantedAuthority::getAuthority)
        .map(a -> a.startsWith("ROLE_") ? a.substring(ROLE_PREFIX_LENGTH) : a)
        .collect(Collectors.toList());
  }

  public static Jwt getJwtOrNull() {
    return getCurrentJwt();
  }
}
