package uk.co.caeldev.content.api.features.publisher.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.co.caeldev.content.api.features.publisher.Publisher;

public interface PublisherRepository extends MongoRepository<Publisher, String>, PublisherRepositoryBase {
}
