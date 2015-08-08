package uk.co.caeldev.content.api.features.publisher;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.spring.mvc.ResponseEntityBuilder;

import static org.springframework.http.HttpStatus.*;

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
    public ResponseEntity<PublisherResource> create(@RequestBody final PublisherResource publisherResource) {
        LOGGER.info("Creating publisher");

        String username = publisherResource.getUsername();

        if (username == null || username.isEmpty()) {
            LOGGER.warn("Publisher userˆname is not Invalid");
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

        final Publisher publisher = publisherService.create(username);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .statusCode(CREATED)
                .entity(publisherResourceAssembler.toResource(publisher))
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}",
            method = RequestMethod.DELETE)
    public ResponseEntity<PublisherResource> delete(@PathVariable String publisherUUID) {
        LOGGER.info("Deleting publisher");

        publisherService.delete(publisherUUID);

        return ResponseEntityBuilder
                .<PublisherResource>responseEntityBuilder()
                .statusCode(NO_CONTENT)
                .build();
    }
}
