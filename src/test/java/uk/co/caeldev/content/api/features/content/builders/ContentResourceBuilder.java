package uk.co.caeldev.content.api.features.content.builders;

import org.joda.time.DateTime;
import uk.co.caeldev.content.api.features.content.ContentResource;
import uk.co.caeldev.content.api.features.content.ContentStatus;

import java.util.UUID;

import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.value;
import static uk.org.fyodor.jodatime.generators.RDG.localDate;

public class ContentResourceBuilder {

    private String content = string().next();
    private String contentUUID = UUID.randomUUID().toString();
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

    public ContentResourceBuilder contentUUID(final String contentUUID) {
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

    public ContentResourceBuilder noRandomData() {
        this.content = null;
        this.contentStatus = null;
        this.contentUUID = null;
        this.creationDate = null;
        return this;
    }
}
