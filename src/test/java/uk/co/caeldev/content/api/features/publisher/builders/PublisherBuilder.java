package uk.co.caeldev.content.api.features.publisher.builders;

import org.joda.time.LocalDateTime;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.Status;
import uk.org.fyodor.generators.characters.CharacterSetFilter;

import java.util.UUID;

import static org.joda.time.LocalDateTime.now;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.value;
import static uk.org.fyodor.generators.RDG.emailAddress;

public class PublisherBuilder {

    private String email = emailAddress().next();
    private String id = UUID.randomUUID().toString();
    private String publisherUUID = UUID.randomUUID().toString();
    private String username = string(30, CharacterSetFilter.LettersOnly).next();
    private Status status = value(Status.class).next();
    private LocalDateTime creationDate = now();
    private String firstName = string().next();
    private String lastName = string().next();

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

    public PublisherBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public Publisher build() {
        final Publisher publisher = new Publisher();
        publisher.setId(id);
        publisher.setPublisherUUID(publisherUUID);
        publisher.setUsername(username);
        publisher.setCreationTime(creationDate);
        publisher.setStatus(status);
        publisher.setFirstName(firstName);
        publisher.setLastName(lastName);
        publisher.setEmail(email);
        return publisher;
    }

    public PublisherBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PublisherBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public PublisherBuilder email(String email) {
        this.email = email;
        return this;
    }
}
