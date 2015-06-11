package uk.co.caeldev.content.api.features.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import uk.co.caeldev.spring.mvc.resources.DomainResourceAssemblerSupport;

@Component
public class ContentResourceAssembler extends DomainResourceAssemblerSupport<Content, ContentResource> {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContentResourceAssembler.class);

    public ContentResourceAssembler() {
        super(ContentController.class, ContentResource.class);
    }

    @Override
    public ContentResource toResource(final Content content) {
        LOGGER.debug("Transform Content to ContentResource");
        return new ContentResource(content.getContentUUID(), content.getContent(), content.getStatus(), content.getCreationDate());
    }

    @Override
    public Content toDomain(final ContentResource resource, final Content content) {
        LOGGER.debug("Transform ContentResource to Content");
        content.setContent(resource.getContent());
        content.setStatus(resource.getContentStatus());
        return content;
    }
}
