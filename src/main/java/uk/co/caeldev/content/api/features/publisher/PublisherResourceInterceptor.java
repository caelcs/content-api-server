package uk.co.caeldev.content.api.features.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caeldev.spring.mvc.ResourceInterceptor;

import static uk.co.caeldev.content.api.features.checks.Preconditions.checkNullPublisher;

public class PublisherResourceInterceptor extends ResourceInterceptor {

    private final static Logger LOGGER = LoggerFactory.getLogger(PublisherResourceInterceptor.class);

    private final PublisherService publisherService;

    private final PublisherResourceAssembler publisherResourceAssembler;

    public PublisherResourceInterceptor(final PublisherService publisherService,
                                        final PublisherResourceAssembler publisherResourceAssembler) {
        this.publisherService = publisherService;
        this.publisherResourceAssembler = publisherResourceAssembler;
    }

    @Override
    protected String entityHashCodeByUUID(String uuid) {
        LOGGER.info("entityHashCodeByUUID execute");
        final Publisher publisher = publisherService.getPublisherByUUID(uuid);
        checkNullPublisher(publisher);
        return String.valueOf(publisherResourceAssembler.toResource(publisher).hashCode());
    }

    @Override
    protected String getUUIDVariableName() {
        return "publisherUUID";
    }
}