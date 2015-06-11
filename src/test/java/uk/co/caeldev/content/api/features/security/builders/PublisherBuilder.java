package uk.co.caeldev.content.api.features.security.builders;

import org.bson.types.ObjectId;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import java.util.UUID;

import static uk.co.caeldev.content.api.commons.ContentApiRDG.objectId;
import static uk.org.fyodor.generators.RDG.string;

public class PublisherBuilder {

    private final ObjectId id = objectId().next();
    private final UUID publisherUUID = UUID.randomUUID();
    private final String username = string(30, CharacterSetFilter.LettersOnly).next();

    private PublisherBuilder() {
    }

    public static PublisherBuilder publisherBuilder() {
        return new PublisherBuilder();
    }

    public Publisher build() {
        final Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setPublisherUUID(publisherUUID);
        publisher.setUsername(username);
        return publisher;
    }
}
