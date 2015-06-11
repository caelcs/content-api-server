package uk.co.caeldev.content.api.features.publisher;

import java.util.UUID;

public interface PublisherService {
    Publisher getPublisherByUUIDAndUsername(UUID publisherUUID, String username);
}
