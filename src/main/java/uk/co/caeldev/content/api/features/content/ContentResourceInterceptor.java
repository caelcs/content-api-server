package uk.co.caeldev.content.api.features.content;

import uk.co.caeldev.spring.mvc.ResourceInterceptor;

public class ContentResourceInterceptor extends ResourceInterceptor {
    private final ContentService contentService;
    private final ContentResourceAssembler contentResourceAssembler;

    public ContentResourceInterceptor(final ContentService contentService,
                                      final ContentResourceAssembler contentResourceAssembler) {
        this.contentService = contentService;
        this.contentResourceAssembler = contentResourceAssembler;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        final Content content = contentService.findOneByUUID(uuid);
        return String.valueOf(contentResourceAssembler.toResource(content).hashCode());
    }

    @Override
    protected String getUUIDVariableName() {
        return "content_uuid";
    }
}