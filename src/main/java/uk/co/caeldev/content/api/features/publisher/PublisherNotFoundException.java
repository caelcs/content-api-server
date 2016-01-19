package uk.co.caeldev.content.api.features.publisher;

public class PublisherNotFoundException extends RuntimeException {

    public PublisherNotFoundException() {
        super("Publisher not found for given UUID");
    }
}
