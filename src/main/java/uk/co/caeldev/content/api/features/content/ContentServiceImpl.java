package uk.co.caeldev.content.api.features.content;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.content.repository.ContentRepository;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
import static uk.co.caeldev.content.api.features.checks.Preconditions.checkNullContent;
import static uk.co.caeldev.content.api.features.content.ContentBuilder.contentBuilder;

@Component
public class ContentServiceImpl implements ContentService {

    private final ContentRepository contentRepository;

    @Autowired
    public ContentServiceImpl(final ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Override
    public Content publish(String content, String publisherId) {
        checkArgument(content != null && !content.isEmpty(), "Content is null or empty.");

        final Content contentToBePublished = contentBuilder()
                .content(content)
                .contentUUID(UUID.randomUUID().toString())
                .publisherId(publisherId)
                .creationDate(DateTime.now())
                .status(ContentStatus.UNREAD).build();

        return contentRepository.save(contentToBePublished);
    }

    @Override
    public Content findOneByUUID(String uuid) {
        checkArgument(uuid != null && !uuid.isEmpty(), "UUID is null or empty.");
        return contentRepository.findOneByUUID(uuid);
    }

    @Override
    public Page<Content> findAllContentPaginatedBy(ContentStatus contentStatus, String publisherId, Pageable pageable) {
        return contentRepository.findAllContentByStatusPublisherIdPaginated(contentStatus, publisherId, pageable);
    }

    @Override
    public Content updateStatus(UUID contentUUID, ContentStatus contentStatus) {
        final Content currentContent = contentRepository.findOneByUUID(contentUUID.toString());
        checkNullContent(currentContent);
        return contentRepository.updateStatus(currentContent.getContentUUID(), contentStatus);
    }
}
