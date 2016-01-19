package uk.co.caeldev.content.api.features.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;
import uk.co.caeldev.spring.mvc.ResponseEntityBuilder;

import java.util.UUID;

import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.content.api.features.checks.Preconditions.*;

@RestController
@EnableOAuth2Resource
public class ContentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;
    private final PublisherService publisherService;
    private final ContentResourceAssembler contentResourceAssembler;
    private final PagedResourcesAssembler pagedResourcesAssembler;

    @Autowired
    public ContentController(final ContentService contentService,
                             final PublisherService publisherService,
                             final ContentResourceAssembler contentResourceAssembler,
                             final PagedResourcesAssembler pagedResourcesAssembler) {
        this.contentService = contentService;
        this.publisherService = publisherService;
        this.contentResourceAssembler = contentResourceAssembler;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents",
            method = RequestMethod.POST,
            consumes = {MediaType.APPLICATION_JSON_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<ContentResource> publish(@PathVariable UUID publisherUUID,
                                                   @RequestBody ContentResource contentResource) {

        LOGGER.info("Publishing content");

        String content = contentResource.getContent();

        if (content.isEmpty()) {
            LOGGER.warn("Content is not Invalid");
            return ResponseEntityBuilder.
                    <ContentResource>responseEntityBuilder()
                    .statusCode(BAD_REQUEST)
                    .build();
        }

        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID.toString());
        checkNullPublisher(publisher);

        final Content publishedContent = contentService.publish(content, publisher.getId());

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(CREATED)
                .entity(contentResourceAssembler.toResource(publishedContent))
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents/{contentUUID}",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<ContentResource> getContent(@PathVariable UUID publisherUUID,
                                                      @PathVariable UUID contentUUID) {
        LOGGER.info("get content");

        final Content content = contentService.findOneByUUID(contentUUID.toString());
        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID.toString());

        checkNullContent(content);
        checkNullPublisher(publisher);
        checkForbiddenContent(content.getPublisherId(), publisher.getId());

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(OK)
                .entity(contentResourceAssembler.toResource(content))
                .build();

    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents",
            method = RequestMethod.GET,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<PagedResources<ContentResource>> getContentPaginatedBy(@RequestParam ContentStatus contentStatus,
                                                                                 @PathVariable UUID publisherUUID,
                                                                                 final Pageable pageable) {

        LOGGER.info("get all content paginated by Content Status and Publisher UUID");

        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID.toString());
        checkNullPublisher(publisher);

        final Page<Content> pageContent = contentService.findAllContentPaginatedBy(contentStatus, publisher.getId(), pageable);

        return ResponseEntityBuilder.
                <PagedResources<ContentResource>>responseEntityBuilder()
                .statusCode(OK)
                .entity(pagedResourcesAssembler.toResource(pageContent, contentResourceAssembler))
                .build();
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents/{contentUUID}",
            method = RequestMethod.PUT,
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public ResponseEntity<ContentResource> updateStatus(@PathVariable UUID publisherUUID,
                                                        @PathVariable UUID contentUUID,
                                                        @RequestBody ContentResource contentResourceNew) {
        LOGGER.info("update content stauts by content UUID");

        final Publisher publisher = publisherService.getPublisherByUUID(publisherUUID.toString());
        final Content currentContent = contentService.findOneByUUID(contentUUID.toString());

        checkNullContent(currentContent);
        checkNullPublisher(publisher);
        checkForbiddenContent(currentContent.getPublisherId(), publisher.getId());

        final Content contentUpdated = contentService.updateStatus(contentUUID, contentResourceNew.getContentStatus());

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(OK)
                .entity(contentResourceAssembler.toResource(contentUpdated))
                .build();
    }
}
