package uk.co.caeldev.content.api.features.publisher;

public interface PublisherService {
    Publisher getPublisherByUUIDAndUsername(String publisherUUID, String username);

    Publisher getPublisherByUUID(String publisherUUID);

    Publisher create(String username);

    Publisher getPublisherByUsername(String username);
}
