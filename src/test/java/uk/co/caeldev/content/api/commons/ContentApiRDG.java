package uk.co.caeldev.content.api.commons;

import org.bson.types.ObjectId;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentBuilder;
import uk.co.caeldev.content.api.features.content.ContentStatus;
import uk.org.fyodor.generators.Generator;
import uk.org.fyodor.generators.RDG;

import java.util.UUID;

import static uk.org.fyodor.jodatime.generators.RDG.localDate;

public class ContentApiRDG extends RDG {

    public static Generator<Content> ofContent() {
        return new Generator<Content>() {
                @Override
                public Content next() {
                    return ContentBuilder.contentBuilder()
                            .content(string().next())
                            .contentUUID(UUID.randomUUID().toString())
                            .creationDate(localDate().next().toDateTimeAtCurrentTime())
                            .status(value(ContentStatus.class).next())
                            .build();
                }
            };
    }

    public static Generator<GrantedAuthority> ofGrantedAuthority() {
        return new Generator<GrantedAuthority>() {
            @Override
            public GrantedAuthority next() {
                return new SimpleGrantedAuthority(string().next());
            }
        };
    }
}
