package uk.co.caeldev.content.api.features.content;

import org.joda.time.DateTime;

import java.util.UUID;

public final class ContentBuilder {

    private String content;
    private ContentStatus contentStatus;
    private UUID contentUUID;
    private DateTime creationDate;
    private String username;

    private ContentBuilder() {
    }

    public static ContentBuilder contentBuilder() {
        return new ContentBuilder();
    }

    public ContentBuilder content(final String content) {
        this.content = content;
        return this;
    }

    public ContentBuilder status(final ContentStatus contentStatus) {
        this.contentStatus = contentStatus;
        return this;
    }

    public ContentBuilder contentUUID(final UUID contentUUID) {
        this.contentUUID = contentUUID;
        return this;
    }

    public ContentBuilder creationDate(final DateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ContentBuilder username(String username) {
        this.username = username;
        return this;
    }

    public Content build() {
        final Content contentNew = new Content();
        contentNew.setContent(content);
        contentNew.setStatus(contentStatus);
        contentNew.setContentUUID(contentUUID);
        contentNew.setUsername(username);
        contentNew.setCreationDate(creationDate);
        return contentNew;
    }
}
