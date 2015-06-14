package uk.co.caeldev.content.api.features.content;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.joda.time.DateTime;
import org.springframework.hateoas.ResourceSupport;

public class ContentResource extends ResourceSupport {

    private final String contentUUID;
    private final String content;
    private final ContentStatus contentStatus;
    private final DateTime creationDate;

    @JsonCreator
    public ContentResource(@JsonProperty("contentUUID") final String contentUUID,
                           @JsonProperty("content") final String content,
                           @JsonProperty("contentStatus") final ContentStatus contentStatus,
                           @JsonProperty("creationDate") final DateTime creationDate) {
        this.contentUUID = contentUUID;
        this.content = content;
        this.contentStatus = contentStatus;
        this.creationDate = creationDate;
    }

    public String getContentUUID() {
        return contentUUID;
    }

    public String getContent() {
        return content;
    }

    public ContentStatus getContentStatus() {
        return contentStatus;
    }

    public DateTime getCreationDate() {
        return creationDate;
    }
}
