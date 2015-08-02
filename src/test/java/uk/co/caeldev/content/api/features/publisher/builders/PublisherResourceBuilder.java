package uk.co.caeldev.content.api.features.publisher.builders;

import org.joda.time.LocalDateTime;
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

    private PublisherResourceBuilder() {
    }

    public static PublisherResourceBuilder publisherResourceBuilder() {
        return new PublisherResourceBuilder();
    }

    public PublisherResource build() {
        return new PublisherResource(publisherUUID, username, creationTime, status);
    }
}
