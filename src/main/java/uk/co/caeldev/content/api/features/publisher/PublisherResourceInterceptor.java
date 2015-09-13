package uk.co.caeldev.content.api.features.publisher;

import uk.co.caeldev.spring.mvc.ResourceInterceptor;

public class PublisherResourceInterceptor extends ResourceInterceptor {
    private final PublisherService publisherService;

    private final PublisherResourceAssembler publisherResourceAssembler;

    public PublisherResourceInterceptor(final PublisherService publisherService,
                                        final PublisherResourceAssembler publisherResourceAssembler) {
        this.publisherService = publisherService;
        this.publisherResourceAssembler = publisherResourceAssembler;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        final Publisher publisher = publisherService.getPublisherByUUID(uuid);
        return String.valueOf(publisherResourceAssembler.toResource(publisher).hashCode());
    }

    @Override
    protected String getUUIDVariableName() {
        return "publisherUUID";
    }
}