package uk.co.caeldev.content.api.features.content;

public class ContentNotFoundException extends RuntimeException {

    public ContentNotFoundException() {
        super("Content not found for given UUID");
    }
}
