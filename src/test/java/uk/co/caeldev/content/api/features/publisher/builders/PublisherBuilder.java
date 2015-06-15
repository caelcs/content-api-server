package uk.co.caeldev.content.api.features.publisher.builders;

import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import java.util.UUID;

import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;

public class PublisherBuilder {

    private String id = UUID.randomUUID().toString();
    private String publisherUUID = UUID.randomUUID().toString();
    private String username = string(30, CharacterSetFilter.LettersOnly).next();

    private PublisherBuilder() {
    }

    public static PublisherBuilder publisherBuilder() {
        return new PublisherBuilder();
    }

    public PublisherBuilder id(String id) {
        this.id = id;
        return this;
    }

    public PublisherBuilder publisherUUID(String publisherUUID) {
        this.publisherUUID = publisherUUID;
        return this;
    }

    public PublisherBuilder username(String username) {
        this.username = username;
        return this;
    }

    public Publisher build() {
        final Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setPublisherUUID(publisherUUID);
        publisher.setUsername(username);
        return publisher;
    }
}
