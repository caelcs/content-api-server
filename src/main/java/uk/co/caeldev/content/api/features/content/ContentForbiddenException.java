package uk.co.caeldev.content.api.features.content;

public class ContentForbiddenException extends RuntimeException {

    public ContentForbiddenException() {
        super("Content forbidden for given publisher and content UUID");
    }
}
