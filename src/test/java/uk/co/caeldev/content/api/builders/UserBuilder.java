package uk.co.caeldev.content.api.builders;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.Set;
import java.util.UUID;

import static uk.co.caeldev.content.api.builders.UserBuilder.GrantedAuthorityBuilder.grantedAuthorityBuilder;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.*;

public class UserBuilder {

    private String password = string().next();
    private String username = string().next();
    private UUID userUUID = UUID.randomUUID();
    private Set<GrantedAuthority> grantedAuthorities = set(ofGrantedAuthority()).next();

    private UserBuilder() {
    }

    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }

    public UserBuilder username(String username) {
        this.username = username;
        return this;
    }

    public UserBuilder roleAdmin() {
        grantedAuthorities.add(grantedAuthorityBuilder().roleAdmin());
        return this;
    }

    public UserBuilder roleUser() {
        grantedAuthorities.add(grantedAuthorityBuilder().roleAdmin());
        return this;
    }

    public User build() {
        return new User(password, username, userUUID, grantedAuthorities, true, true, true, true);
    }

    protected static class GrantedAuthorityBuilder {

        private String role = "ROLE_USER";

        private GrantedAuthorityBuilder() {
        }

        public static GrantedAuthorityBuilder grantedAuthorityBuilder() {
            return new GrantedAuthorityBuilder();
        }

        public GrantedAuthority roleAdmin() {
            role = "ROLE_ADMIN";
            return build();
        }

        public GrantedAuthority roleUser() {
            role = "ROLE_USER";
            return build();
        }

        public GrantedAuthority build() {
            return new SimpleGrantedAuthority(role);
        }
    }
}
