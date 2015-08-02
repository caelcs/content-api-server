package uk.co.caeldev.content.api.features.publisher;

import org.springframework.stereotype.Component;
import uk.co.caeldev.spring.mvc.resources.DomainResourceAssemblerSupport;

@Component
public class PublisherResourceAssembler extends DomainResourceAssemblerSupport<Publisher, PublisherResource>  {

    public PublisherResourceAssembler() {
        super(PublisherController.class, PublisherResource.class);
    }

    @Override
    public Publisher toDomain(final PublisherResource resource,
                              final Publisher domain) {
        domain.setStatus(resource.getStatus());
        return domain;
    }

    @Override
    public PublisherResource toResource(final Publisher entity) {
        return new PublisherResource(entity.getPublisherUUID(), entity.getUsername(), entity.getCreationTime(), entity.getStatus());
    }
}
