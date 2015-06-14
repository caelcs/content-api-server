package uk.co.caeldev.content.api.features.content;

import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.content.repository.ContentRepository;

import java.util.UUID;

import static com.google.common.base.Preconditions.checkArgument;
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
}
