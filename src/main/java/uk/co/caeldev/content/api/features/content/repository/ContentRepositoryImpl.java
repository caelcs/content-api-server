package uk.co.caeldev.content.api.features.content.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentStatus;

import static org.springframework.data.mongodb.core.query.Criteria.where;
import static org.springframework.data.mongodb.core.query.Update.update;

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

    @Override
    public Content updateStatus(String contentUUID, ContentStatus contentStatusNew) {
        final Query query = new Query(where("contentUUID").is(contentUUID));
        return mongoTemplate.findAndModify(query, update("status", contentStatusNew), Content.class);
    }
}
