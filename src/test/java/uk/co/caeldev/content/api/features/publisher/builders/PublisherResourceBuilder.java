package uk.co.caeldev.content.api.features.publisher.builders;

import org.joda.time.LocalDateTime;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherResource;
import uk.co.caeldev.content.api.features.publisher.Status;

import java.util.UUID;

import static org.joda.time.LocalDateTime.now;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.value;

public class PublisherResourceBuilder {

    private String publisherUUID = UUID.randomUUID().toString();
    private String username = string().next();
    private LocalDateTime creationTime = now();
    private Status status = value(Status.class).next();
    private String firstName = string().next();
    private String lastName = string().next();

    private PublisherResourceBuilder() {
    }

    public static PublisherResourceBuilder publisherResourceBuilder() {
        return new PublisherResourceBuilder();
    }

    public PublisherResourceBuilder username(String username) {
        this.username = username;
        return this;
    }

    public PublisherResourceBuilder publisher(final Publisher publisher) {
        publisherUUID = publisher.getPublisherUUID();
        username = publisher.getUsername();
        creationTime = publisher.getCreationTime();
        status = publisher.getStatus();
        firstName = publisher.getFirstName();
        lastName = publisher.getLastName();
        return this;
    }

    public PublisherResourceBuilder uuid(String publisherUUID) {
        this.publisherUUID = publisherUUID;
        return this;
    }

    public PublisherResourceBuilder status(Status status) {
        this.status = status;
        return this;
    }

    public PublisherResource build() {
        return new PublisherResource(publisherUUID, username, creationTime, status, firstName, lastName);
    }

    public PublisherResourceBuilder firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public PublisherResourceBuilder lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }
}
