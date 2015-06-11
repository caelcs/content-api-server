package uk.co.caeldev.content.api.features.content.builders;

import org.joda.time.DateTime;
import uk.co.caeldev.content.api.features.content.ContentResource;
import uk.co.caeldev.content.api.features.content.ContentStatus;

import java.util.UUID;

import static uk.org.fyodor.generators.RDG.string;
import static uk.org.fyodor.generators.RDG.value;
import static uk.org.fyodor.jodatime.generators.RDG.localDate;

public class ContentResourceBuilder {

    private String content = string().next();
    private UUID contentUUID = UUID.randomUUID();
    private ContentStatus contentStatus = value(ContentStatus.class).next();
    private DateTime creationDate = localDate().next().toDateTimeAtCurrentTime();

    private ContentResourceBuilder() {
    }

    public static ContentResourceBuilder contentResourceBuilder() {
        return new ContentResourceBuilder();
    }

    public ContentResourceBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public ContentResourceBuilder contentUUID(final UUID contentUUID) {
        this.contentUUID = contentUUID;
        return this;
    }

    public ContentResourceBuilder status(final ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
        return this;
    }

    public ContentResourceBuilder creationDate(final DateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ContentResource build() {
        return new ContentResource(contentUUID, content, contentStatus, creationDate);
    }
}