package uk.co.caeldev.content.api.features.content;

import org.joda.time.DateTime;

public final class ContentBuilder {

    private String content;
    private ContentStatus contentStatus;
    private String contentUUID;
    private DateTime creationDate;
    private String publisherId;

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

    public ContentBuilder contentUUID(final String contentUUID) {
        this.contentUUID = contentUUID;
        return this;
    }

    public ContentBuilder creationDate(final DateTime creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public ContentBuilder publisherId(final String publisherId) {
        this.publisherId = publisherId;
        return this;
    }

    public Content build() {
        final Content contentNew = new Content();
        contentNew.setContent(content);
        contentNew.setStatus(contentStatus);
        contentNew.setContentUUID(contentUUID);
        contentNew.setPublisherId(publisherId);
        contentNew.setCreationDate(creationDate);
        return contentNew;
    }
}
