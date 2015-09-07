package uk.co.caeldev.content.api.features.publisher;

import uk.co.caeldev.spring.mvc.ResourceInterceptor;

public class PublisherResourceInterceptor extends ResourceInterceptor {
    private final PublisherService publisherService;

    public PublisherResourceInterceptor(final PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        return publisherService.getPublisherByUUID(uuid).toString();
    }

    @Override
    protected String getUUIDVariableName() {
        return "publisherUUID";
    }
}