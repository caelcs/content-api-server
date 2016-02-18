package uk.co.caeldev.content.api.features.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.spring.mvc.ResponseEntityBuilder;

import java.security.Principal;

import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.content.api.features.checks.Preconditions.checkNullPublisher;

@RestController
@EnableOAuth2Resource
public class PublisherController {

    private final static Logger LOGGER = LoggerFactory.getLogger(PublisherController.class);

    private final PublisherService publisherService;
    private final PublisherResourceAssembler publisherResourceAssembler;

    @Autowired
    public PublisherController(final PublisherService publisherService,
                               final PublisherResourceAssembler publisherResourceAssembler) {
        this.publisherService = publisherService;
        this.publisherResourceAssembler = publisherResourceAssembler;
    }

    @RequestMapping(value = "/publishers",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PublisherResource> create(@RequestBody final PublisherResource publisherResource) {
        LOGGER.info("Creating publisher");

        String username = publisherResource.getUsername();
        String firstName = publisherResource.getFirstName();
        String lastName = publisherResource.getLastName();
        String email = publisherResource.getEmail();

        if (username == null || username.isEmpty()) {
            LOGGER.warn("Publisher username is not Invalid");
            return ResponseEntityBuilder
                    .<PublisherResource>responseEntityBuilder()
                    .statusCode(BAD_REQUEST)
                    .build();
        }

        if (publisherService.getPublisherByUsername(username) != null) {
            LOGGER.warn("Publisher already exists.");
            return ResponseEntityBuilder
                    .<PublisherResource>responseEntityBuilder()
                    .statusCode(BAD_REQUEST)
                    .build();
        }

        final Publisher publisher = publisherService.create(username, firstName, lastName, email);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .statusCode(CREATED)
                .entity(publisherResourceAssembler.toResource(publisher))
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}",
            method = RequestMethod.DELETE)
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PublisherResource> delete(@PathVariable String publisherUUID) {
        LOGGER.info("Deleting publisher");

        publisherService.delete(publisherUUID);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .statusCode(NO_CONTENT)
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}",
            method = RequestMethod.PUT,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PublisherResource> update(@PathVariable String publisherUUID,
                                                    @RequestBody final PublisherResource publisherResource) {
        LOGGER.info("Updating publisher");

        final Publisher publisherSaved = publisherService.getPublisherByUUID(publisherUUID);

        final Publisher publisherToBeUpdated = publisherResourceAssembler.toDomain(publisherResource, publisherSaved);

        final Publisher result = publisherService.update(publisherToBeUpdated);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .entity(publisherResourceAssembler.toResource(result))
                .statusCode(OK)
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}",
            method = RequestMethod.GET,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PublisherResource> get(@PathVariable String publisherUUID) {
        LOGGER.info("get publisher");

        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .entity(publisherResourceAssembler.toResource(publisher))
                .statusCode(OK)
                .build();
    }

    @RequestMapping(value = "/publisher/current",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasRole('ROLE_USER')")
    public ResponseEntity<PublisherResource> currentPublisherFromToken(@AuthenticationPrincipal Principal principal) {
        LOGGER.info("get publisher from token");
        final String username = principal.getName();

        final Publisher publisher = publisherService.getPublisherByUsername(username);
        checkNullPublisher(publisher);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .entity(publisherResourceAssembler.toResource(publisher))
                .statusCode(OK)
                .build();
    }
}
