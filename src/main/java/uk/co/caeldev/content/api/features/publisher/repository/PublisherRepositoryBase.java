package uk.co.caeldev.content.api.features.publisher.repository;

import uk.co.caeldev.content.api.features.publisher.Publisher;

public interface PublisherRepositoryBase {

    Publisher findByUUIDAndUsername(String publisherUUID, String username);

    Publisher findByUUID(String publisherUUID);

}
