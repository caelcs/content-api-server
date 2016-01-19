package uk.co.caeldev.content.api.features.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.access.PermissionEvaluator;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;

import java.io.Serializable;
import java.util.UUID;

import static uk.co.caeldev.content.api.features.security.Permissions.PUBLISHER_OWN_CONTENT;

@Component
public class ResourceOwnerEvaluator implements PermissionEvaluator {

    private final static Logger LOGGER = LoggerFactory.getLogger(ResourceOwnerEvaluator.class);

    private final PublisherService publisherService;

    @Autowired
    @Lazy
    public ResourceOwnerEvaluator(final PublisherService publisherService) {
        this.publisherService = publisherService;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
        LOGGER.info("Eval if hasPermission");

        if (PUBLISHER_OWN_CONTENT.name().equals(permission.toString()) && targetDomainObject instanceof UUID) {
            LOGGER.debug("Verify Publisher exists");
            final UUID publisherUUID = (UUID) targetDomainObject;
            LOGGER.debug("Publisher UUID: " + publisherUUID.toString() + " - ID: " + authentication.getName());
            final Publisher publisher = publisherService.getPublisherByUUIDAndUsername(publisherUUID.toString(), authentication.getName());
            return publisher != null;
        }

        return false;
    }

    @Override
    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
        throw new UnsupportedOperationException();
    }
}
