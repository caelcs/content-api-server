package uk.co.caeldev.content.api.features.publisher.repository;

import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.Status;

public interface PublisherRepositoryBase {

    Publisher findByUUIDAndUsername(String publisherUUID, String username);

    Publisher findByUUID(String publisherUUID);

    Publisher findByUsername(String username);

    Publisher updateStatus(String publisherUUID, Status status);
}
