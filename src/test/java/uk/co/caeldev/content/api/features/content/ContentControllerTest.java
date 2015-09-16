package uk.co.caeldev.content.api.features.content;

import com.google.common.collect.Lists;
import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.PagedResources;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import uk.co.caeldev.content.api.builders.UserBuilder;
import uk.co.caeldev.content.api.features.common.PageBuilder;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.content.ContentBuilder.contentBuilder;
import static uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder.contentResourceBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

@RunWith(MockitoJUnitRunner.class)
public class ContentControllerTest {

    @Mock
    private ContentResourceAssembler contentResourceAssembler;

    @Mock
    private PagedResourcesAssembler pagedResourcesAssembler;

    @Mock
    private ContentService contentService;

    @Mock
    private PublisherService publisherService;

    private ContentController contentController;

    @Before
    public void testee() {
        contentController = new ContentController(contentService, publisherService, contentResourceAssembler, pagedResourcesAssembler);
    }

    @Test
    public void shouldPublishContentForAPublisher() throws Exception {
        //Given
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUID = UUID.randomUUID().toString();
        final String content = string().next();
        final User user = UserBuilder.userBuilder().build();
        final String publisherId = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .id(publisherId)
                .publisherUUID(publisherUUID.toString())
                .username(user.getUsername())
                .build();

        given(publisherService.getPublisherByUUID(publisherUUID.toString())).willReturn(expectedPublisher);

        //And
        final Content expectedContent = contentBuilder().content(content).contentUUID(contentUUID).publisherId(publisherId).build();
        given(contentService.publish(content, expectedPublisher.getId())).willReturn(expectedContent);

        //And
        given(contentResourceAssembler.toResource(expectedContent)).willReturn(contentResourceBuilder().content(content).contentUUID(contentUUID).build());

        //When
        final ContentResource contentResource = contentResourceBuilder().content(content).build();
        final ResponseEntity<ContentResource> response = contentController.publish(publisherUUID, contentResource);

        //Then
        assertThat(response.getBody()).is(new Condition<ContentResource>() {
            @Override
            public boolean matches(ContentResource value) {
                return value.getContentUUID().equals(contentUUID) &&
                        value.getContent().equals(content);
            }
        });

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
    }

    @Test
    public void shouldGetContentByUUID() throws Exception {
        //Given
        final UUID contentUUID = UUID.randomUUID();
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUIDStr = contentUUID.toString();
        final String publisherUUIDStr = publisherUUID.toString();

        //And
        final Content expectedContent = contentBuilder()
                .contentUUID(contentUUIDStr)
                .build();

        given(contentService.findOneByUUID(contentUUIDStr)).willReturn(expectedContent);

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .publisherUUID(publisherUUIDStr)
                .id(expectedContent.getPublisherId())
                .build();

        given(publisherService.getPublisherByUUID(publisherUUIDStr)).willReturn(expectedPublisher);

        //And
        final ContentResource expectedContentResource = contentResourceBuilder()
                .content(expectedContent.getContent())
                .contentUUID(contentUUIDStr)
                .creationDate(expectedContent.getCreationDate())
                .status(expectedContent.getStatus())
                .build();

        given(contentResourceAssembler.toResource(expectedContent)).willReturn(expectedContentResource);

        //When
        final ResponseEntity<ContentResource> response = contentController.getContent(publisherUUID, contentUUID);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void shouldGetAllContentByStatusAndPublisherUUIPaginated() throws Exception {
        //Given
        final UUID contentUUID = UUID.randomUUID();
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUIDStr = contentUUID.toString();
        final String publisherUUIDStr = publisherUUID.toString();
        final ContentStatus contentStatus = ContentStatus.UNREAD;

        //And
        final Content expectedContent = contentBuilder()
                .contentUUID(contentUUIDStr)
                .status(contentStatus)
                .build();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .publisherUUID(publisherUUIDStr)
                .id(expectedContent.getPublisherId())
                .build();

        given(publisherService.getPublisherByUUID(publisherUUIDStr)).willReturn(expectedPublisher);

        //And
        final Page<Content> contentPage = PageBuilder.<Content>pageBuilder().page(expectedContent).build();

        final PageRequest pageable = new PageRequest(0, 1);
        given(contentService.findAllContentPaginatedBy(contentStatus, expectedPublisher.getId(), pageable)).willReturn(contentPage);

        //And
        final ContentResource expectedContentResource = contentResourceBuilder()
                .content(expectedContent.getContent())
                .contentUUID(contentUUIDStr)
                .creationDate(expectedContent.getCreationDate())
                .status(expectedContent.getStatus())
                .build();

        given(contentResourceAssembler.toResource(expectedContent)).willReturn(expectedContentResource);

        //And
        given(pagedResourcesAssembler.toResource(contentPage, contentResourceAssembler)).willReturn(PagedResources.wrap(Lists.newArrayList(expectedContent), new PagedResources.PageMetadata(1, 0, 1, 1)));

        //When
        final ResponseEntity<PagedResources<ContentResource>> response = contentController.getContentPaginatedBy(contentStatus, publisherUUID, pageable);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(OK);
        assertThat(response.getBody()).isNotNull();
    }

    @Test
    public void shouldNotGetContentWhenContentDoesNotBelongToPublisher() throws Exception {
        //Given
        final UUID contentUUID = UUID.randomUUID();
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUIDStr = contentUUID.toString();
        final String publisherUUIDStr = publisherUUID.toString();

        //And
        final Content expectedContent = contentBuilder()
                .contentUUID(contentUUIDStr)
                .build();

        given(contentService.findOneByUUID(contentUUIDStr)).willReturn(expectedContent);

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .publisherUUID(publisherUUIDStr)
                .build();

        given(publisherService.getPublisherByUUID(publisherUUIDStr)).willReturn(expectedPublisher);

        //And
        final ContentResource expectedContentResource = contentResourceBuilder()
                .content(expectedContent.getContent())
                .contentUUID(contentUUIDStr)
                .creationDate(expectedContent.getCreationDate())
                .status(expectedContent.getStatus())
                .build();

        given(contentResourceAssembler.toResource(expectedContent)).willReturn(expectedContentResource);

        //When
        final ResponseEntity<ContentResource> response = contentController.getContent(publisherUUID, contentUUID);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(FORBIDDEN);
        assertThat(response.getBody()).isNull();
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotGetContentWhenContentDoesNotExist() throws Exception {
        //Given
        final UUID contentUUID = UUID.randomUUID();
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUIDStr = contentUUID.toString();
        final String publisherUUIDStr = publisherUUID.toString();

        //And
        given(contentService.findOneByUUID(contentUUIDStr)).willReturn(null);

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .publisherUUID(publisherUUIDStr)
                .build();

        given(publisherService.getPublisherByUUID(publisherUUIDStr)).willReturn(expectedPublisher);

        //When
        contentController.getContent(publisherUUID, contentUUID);

        //Then
        verifyZeroInteractions(contentResourceAssembler);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotGetContentWhenPublisherDoesNotExist() throws Exception {
        //Given
        final UUID contentUUID = UUID.randomUUID();
        final UUID publisherUUID = UUID.randomUUID();
        final String contentUUIDStr = contentUUID.toString();
        final String publisherUUIDStr = publisherUUID.toString();

        //And
        final Content expectedContent = contentBuilder()
                .contentUUID(contentUUIDStr)
                .build();

        given(contentService.findOneByUUID(contentUUIDStr)).willReturn(expectedContent);

        //And
        given(publisherService.getPublisherByUUID(publisherUUIDStr)).willReturn(null);

        //When
        contentController.getContent(publisherUUID, contentUUID);

        //Then
        verifyZeroInteractions(contentResourceAssembler);
    }
}
