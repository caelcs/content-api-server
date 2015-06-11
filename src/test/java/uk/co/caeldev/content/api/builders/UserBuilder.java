package uk.co.caeldev.content.api.builders;

import org.springframework.security.core.GrantedAuthority;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.Set;
import java.util.UUID;

import static uk.co.caeldev.content.api.commons.ContentApiRDG.ofGrantedAuthority;
import static uk.org.fyodor.generators.RDG.set;
import static uk.org.fyodor.generators.RDG.string;

public class UserBuilder {

    private final String password = string().next();
    private final String username = string().next();
    private final UUID userUUID = UUID.randomUUID();
    private final Set<GrantedAuthority> grantedAuthorities = set(ofGrantedAuthority()).next();

    private UserBuilder() {
    }

    public static UserBuilder userBuilder() {
        return new UserBuilder();
    }

    public User build() {
        return new User(password, username, userUUID, grantedAuthorities, true, true, true, true);
    }


}
