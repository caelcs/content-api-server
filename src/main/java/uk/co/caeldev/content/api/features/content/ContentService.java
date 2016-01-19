package uk.co.caeldev.content.api.features.content;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.UUID;

public interface ContentService {
    Content publish(String content, String publisherId);

    Content findOneByUUID(String uuid);

    Page<Content> findAllContentPaginatedBy(ContentStatus contentStatus, String publisherId, Pageable pageable);

    Content updateStatus(UUID contentUUID, ContentStatus contentStatus);
}
