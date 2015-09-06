package uk.co.caeldev.content.api.features.common;

import uk.co.caeldev.content.api.features.content.ContentService;
import uk.co.caeldev.spring.mvc.ResourceInterceptor;

public class ContentResourceInterceptor extends ResourceInterceptor {
    private final ContentService contentService;

    public ContentResourceInterceptor(final ContentService contentService) {
        this.contentService = contentService;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        return contentService.findOneByUUID(uuid).toString();
    }

    @Override
    protected String getUUIDVariableName() {
        return "uuid";
    }
}