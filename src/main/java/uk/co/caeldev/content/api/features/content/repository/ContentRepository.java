package uk.co.caeldev.content.api.features.content.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentStatus;

public interface ContentRepository extends MongoRepository<Content, String>, ContentRepositoryBase {

    @Query(value = "{ 'status' : ?0 }")
    Page<Content> findAllContentByStatusPaginated(ContentStatus status, Pageable pageable);

}
