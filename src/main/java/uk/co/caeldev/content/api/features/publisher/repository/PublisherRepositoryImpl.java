package uk.co.caeldev.content.api.features.publisher.repository;

import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.Publisher;

@Component
public class PublisherRepositoryImpl implements PublisherRepositoryBase {

    @Override
    public Publisher findByUUIDAndUsername(String publisherUUID, String username) {
        return null;
    }

    @Override
    public Publisher findByUUID(String publisherUUID) {
        return null;
    }
}
