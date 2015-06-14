package uk.co.caeldev.content.api.features.content.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import uk.co.caeldev.content.api.features.content.Content;

public interface ContentRepository extends MongoRepository<Content, String>, ContentRepositoryBase {

}
