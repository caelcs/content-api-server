package uk.co.caeldev.content.api.features.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkNotNull;
import static org.joda.time.LocalDateTime.now;

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

    @Override
    public Publisher create(String username) {
        final Publisher publisher = new Publisher();
        publisher.setUsername(username);
        publisher.setCreationTime(now());
        publisher.setStatus(Status.ACTIVE);
        publisher.setPublisherUUID(UUID.randomUUID().toString());

        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher getPublisherByUsername(String username) {
        return publisherRepository.findByUsername(username);
    }

    @Override
    public void delete(String publisherUUID) {
        final Publisher publisherUpdated = publisherRepository.updateStatus(publisherUUID, Status.DELETED);
        checkNotNull(publisherUpdated);
    }

    @Override
    public Publisher update(Publisher publisherToBeUpdated) {
        final Publisher publisherUpdated = publisherRepository.updateStatus(publisherToBeUpdated.getPublisherUUID(), publisherToBeUpdated.getStatus());
        checkNotNull(publisherUpdated);
        return publisherUpdated;
    }
}
