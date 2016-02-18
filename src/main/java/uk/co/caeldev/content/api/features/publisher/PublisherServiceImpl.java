package uk.co.caeldev.content.api.features.publisher;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import static com.google.common.base.Preconditions.checkNotNull;
import static java.util.UUID.randomUUID;
import static org.joda.time.LocalDateTime.now;
import static uk.co.caeldev.content.api.features.publisher.Status.ACTIVE;
import static uk.co.caeldev.content.api.features.publisher.Status.DELETED;

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
    public Publisher create(String username, String firstName, String lastName, String email) {
        final Publisher publisher = new Publisher();
        publisher.setUsername(username);
        publisher.setFirstName(firstName);
        publisher.setLastName(lastName);
        publisher.setEmail(email);
        publisher.setCreationTime(now());
        publisher.setStatus(ACTIVE);
        publisher.setPublisherUUID(randomUUID().toString());

        return publisherRepository.save(publisher);
    }

    @Override
    public Publisher getPublisherByUsername(String username) {
        return publisherRepository.findByUsername(username);
    }

    @Override
    public void delete(String publisherUUID) {
        final Publisher publisherUpdated = publisherRepository.updateStatus(publisherUUID, DELETED);
        checkNotNull(publisherUpdated);
    }

    @Override
    public Publisher update(Publisher publisherToBeUpdated) {
        final Publisher publisherUpdated = publisherRepository.updateStatus(publisherToBeUpdated.getPublisherUUID(), publisherToBeUpdated.getStatus());
        checkNotNull(publisherUpdated);
        return publisherUpdated;
    }
}
