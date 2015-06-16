package uk.co.caeldev.content.api.features.publisher.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.Publisher;

@Component
public class PublisherRepositoryImpl implements PublisherRepositoryBase {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PublisherRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Publisher findByUUIDAndUsername(String publisherUUID, String username) {
        return new Publisher();
    }

    @Override
    public Publisher findByUUID(String publisherUUID) {
        final Query query = new Query(Criteria.where("publisherUUID").is(publisherUUID));
        return mongoTemplate.findOne(query, Publisher.class);
    }
}
