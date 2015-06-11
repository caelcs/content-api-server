package uk.co.caeldev.content.api.features.content;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.security.oauth2.resource.EnableOAuth2Resource;
import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.spring.mvc.ResponseEntityBuilder;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;

@RestController
@EnableOAuth2Resource
public class ContentController {

    private final static Logger LOGGER = LoggerFactory.getLogger(ContentController.class);

    private final ContentService contentService;
    private final ContentResourceAssembler contentResourceAssembler;

    @Autowired
    public ContentController(final ContentService contentService,
                             final ContentResourceAssembler contentResourceAssembler) {
        this.contentService = contentService;
        this.contentResourceAssembler = contentResourceAssembler;
    }

    @RequestMapping(value = "/publishers/{publisherUUID}/contents", method = RequestMethod.POST)
    @PreAuthorize("hasPermission(#publisherUUID, 'PUBLISHER_OWN_CONTENT')")
    public HttpEntity<ContentResource> publish(@PathVariable UUID publisherUUID,
                                   @RequestBody String content,
                                   @AuthenticationPrincipal final User user) {

        LOGGER.info("Publishing content");
        if (content.isEmpty()) {
            LOGGER.warn("Content is not Invalid");
            return ResponseEntityBuilder.
                    <ContentResource>responseEntityBuilder()
                    .statusCode(BAD_REQUEST)
                    .build();
        }

        final Content publishedContent = contentService.publish(content, user.getUsername());

        return ResponseEntityBuilder.
                <ContentResource>responseEntityBuilder()
                .statusCode(CREATED)
                .entity(contentResourceAssembler.toResource(publishedContent))
                .build();
    }
}
