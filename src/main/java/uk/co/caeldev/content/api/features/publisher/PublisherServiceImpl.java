package uk.co.caeldev.content.api.features.publisher;

import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class PublisherServiceImpl implements PublisherService {

    @Override
    public Publisher getPublisherByUUIDAndUsername(UUID publisherUUID, String username) {
        return null;
    }
}
