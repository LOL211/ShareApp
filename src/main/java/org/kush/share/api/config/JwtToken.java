package org.kush.share.api.config;

import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import javax.security.auth.Subject;
import java.util.Arrays;
import java.util.stream.Collectors;

public class JwtToken extends AbstractAuthenticationToken
{
    private final String subject;

    public JwtToken(String scopes, String subject) {
        super(Arrays.stream(scopes.split(";")).map(SimpleGrantedAuthority::new).collect(Collectors.toSet()));
        this.subject = subject;
    }

    @Override
    public Object getCredentials() {
        return null;
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