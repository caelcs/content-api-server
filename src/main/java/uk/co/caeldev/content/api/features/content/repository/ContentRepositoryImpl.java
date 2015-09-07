package uk.co.caeldev.content.api.features.content.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.content.Content;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Component
public class ContentRepositoryImpl implements ContentRepositoryBase {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public ContentRepositoryImpl(final MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Content findOneByUUID(String uuid) {
        final Query query = new Query(where("contentUUID").is(uuid));
        return mongoTemplate.findOne(query, Content.class);
    }
}
