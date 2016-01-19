package uk.co.caeldev.content.api.features.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caeldev.spring.mvc.ResourceInterceptor;

import static uk.co.caeldev.content.api.features.checks.Preconditions.checkNullContent;

public class ContentResourceInterceptor extends ResourceInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContentResourceInterceptor.class);

    private final ContentService contentService;

    private final ContentResourceAssembler contentResourceAssembler;

    public ContentResourceInterceptor(final ContentService contentService,
                                      final ContentResourceAssembler contentResourceAssembler) {
        this.contentService = contentService;
        this.contentResourceAssembler = contentResourceAssembler;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        LOGGER.info("entityHashCodeByUUID execute");
        final Content content = contentService.findOneByUUID(uuid);
        checkNullContent(content);
        return String.valueOf(contentResourceAssembler.toResource(content).hashCode());
    }

    @Override
    protected String getUUIDVariableName() {
        return "contentUUID";
    }
}