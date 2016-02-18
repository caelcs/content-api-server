package uk.co.caeldev.content.api.features.publisher;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ReflectionToStringBuilder;
import org.joda.time.LocalDateTime;
import org.springframework.hateoas.ResourceSupport;

public class PublisherResource extends ResourceSupport {

    private String publisherUUID;
    private String username;
    private LocalDateTime creationTime;
    private Status status;
    private String firstName;
    private String lastName;
    private String email;

    @JsonCreator
    public PublisherResource(@JsonProperty("publisherUUID") final String publisherUUID,
                             @JsonProperty("username") final String username,
                             @JsonProperty("creationTime") final LocalDateTime creationTime,
                             @JsonProperty("status") final Status status,
                             @JsonProperty("firstName") final String firstName,
                             @JsonProperty("lastName") final String lastName,
                             @JsonProperty("email") final String email) {
        this.publisherUUID = publisherUUID;
        this.username = username;
        this.creationTime = creationTime;
        this.status = status;
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getEmail() {
        return email;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public String toString() {
        return ReflectionToStringBuilder.toString(this);
    }
}
