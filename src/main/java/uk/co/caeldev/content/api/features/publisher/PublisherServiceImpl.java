package uk.co.caeldev.content.api.features.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

@Component
public class PublisherServiceImpl implements PublisherService {

    private final PublisherRepository publisherRepository;

    @Autowired
    public PublisherServiceImpl(final PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Override
    public Publisher getPublisherByUUIDAndUsername(String publisherUUID, String username) {
        return publisherRepository.findByUUIDAndUsername(publisherUUID, username);
    }

    @Override
    public Publisher getPublisherByUUID(String publisherUUID) {
        return publisherRepository.findByUUID(publisherUUID);
    }
}
