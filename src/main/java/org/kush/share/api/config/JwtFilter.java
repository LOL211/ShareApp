package org.kush.share.api.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.kush.share.api.service.AuthService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.time.Instant;

@RequiredArgsConstructor
@Component
@Slf4j
public class JwtFilter extends OncePerRequestFilter {

    @Value("${vaulty.scopes}")
    private String scope;

    private final AuthService authService;

    @SneakyThrows
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try{
            String bearerToken = request.getHeader("Authorization").split("Bearer ")[1];
            JwtDecoder publicKey = authService.getJwtDecoder();
            Jwt token = publicKey.decode(bearerToken);
            assert token != null;

            if (Instant.now().isAfter(token.getExpiresAt()))
            {
                throw new Exception("Token is expired");
            }
            JwtToken jwtToken = new JwtToken(token.getClaimAsStringList("scope"), token.getSubject(), token.getTokenValue());

            jwtToken.setAuthenticated(
                    jwtToken.getAuthorities()
                    .stream()
                    .map(GrantedAuthority::getAuthority)
                    .anyMatch(authority -> authority.equals(scope))
            );

            if (!jwtToken.isAuthenticated())
            {
                throw new Exception("Incorrect scopes!");
            }

            SecurityContextHolder.getContext().setAuthentication(jwtToken);
            filterChain.doFilter(request, response);
        }
        catch (Exception e){
            log.error("Jwt token authentication failed", e);
            SecurityContextHolder.clearContext();
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }
}
