package uk.co.caeldev.content.api.features.publisher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.LocalDateTime;
import org.springframework.hateoas.ResourceSupport;

public class PublisherResource extends ResourceSupport {

    private String publisherUUID;
    private String username;
    private LocalDateTime creationTime;
    private Status status;

    @JsonCreator
    public PublisherResource(@JsonProperty("publisherUUID") final String publisherUUID,
                             @JsonProperty("username") final String username,
                             @JsonProperty("creationTime") final LocalDateTime creationTime,
                             @JsonProperty("status") final Status status) {
        this.publisherUUID = publisherUUID;
        this.username = username;
        this.creationTime = creationTime;
        this.status = status;
    }

    public String getPublisherUUID() {
        return publisherUUID;
    }

    public String getUsername() {
        return username;
    }

    public LocalDateTime getCreationTime() {
        return creationTime;
    }

    public Status getStatus() {
        return status;
    }
}
