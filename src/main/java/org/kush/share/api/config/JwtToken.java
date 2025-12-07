package org.kush.share.api.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.List;
import java.util.stream.Collectors;

public class JwtToken extends AbstractAuthenticationToken
{
    private final String subject;
    private final String token;

    public JwtToken(List<String> scopes, String subject, String token) {
        super(scopes.stream().map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.subject = subject;
        this.token = token;

    }

    @Override
    public Object getCredentials() {
        return token;
    }

    @Override
    public Object getPrincipal() {
        return subject;
    }

    /**
     * Returns {@code true} if the specified subject is implied by this
     * {@code Principal}.
     *
     * @param subject the {@code Subject}
     * @return {@code true} if {@code subject} is non-null and is
     * implied by this {@code Principal}, or false otherwise.
     * @implSpec The default implementation of this method returns {@code true} if
     * {@code subject} is non-null and contains at least one
     * {@code Principal} that is equal to this {@code Principal}.
     *
     * <p>Subclasses may override this with a different implementation, if
     * necessary.
     * @since 1.8
     */
    @Override
    public boolean implies(Subject subject) {
        return super.implies(subject);
    }
}