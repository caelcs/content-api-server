package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class PublisherControllerTest {

    @Mock
    private PublisherService publisherService;

    @Mock
    private PublisherResourceAssembler publisherResourceAssembler;

    private PublisherController publisherController;

    @Before
    public void testee() throws Exception {
        publisherController = new PublisherController(publisherService, publisherResourceAssembler);
    }

    @Test
    public void shouldCreatePublisher() throws Exception {
        //Given
        final String username = string().next();
        final Publisher publisher = publisherBuilder().username(username).build();

        given(publisherService.create(username)).willReturn(publisher);

        //And
        given(publisherResourceAssembler.toResource(publisher))
                .willReturn(publisherResourceBuilder()
                        .publisher(publisher)
                        .build());

        //When
        final PublisherResource publisherResource = publisherResourceBuilder().username(username).build();
        final ResponseEntity<PublisherResource> response = publisherController.create(publisherResource);

        //Then
        assertThat(response.getBody().getPublisherUUID()).isEqualTo(publisher.getPublisherUUID());
        assertThat(response.getBody().getCreationTime()).isEqualTo(publisher.getCreationTime());
        assertThat(response.getBody().getStatus()).isEqualTo(publisher.getStatus());
        assertThat(response.getBody().getUsername()).isEqualTo(publisher.getUsername());
        assertThat(response.getStatusCode()).isEqualTo(CREATED);
    }

    @Test
    public void shouldNotCreatePublisherWhenBodyIsInvalid() throws Exception {
        //Given
        final String username = null;

        //When
        final PublisherResource publisherResource = publisherResourceBuilder().username(username).build();
        final ResponseEntity<PublisherResource> response = publisherController.create(publisherResource);

        //Then
        verifyZeroInteractions(publisherService);
        verifyZeroInteractions(publisherResourceAssembler);
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }

    @Test
    public void shouldNotCreatePublisherWhenUsernameExists() throws Exception {
        //Given
        final String username = string().next();

        //And
        given(publisherService.getPublisherByUsername(username)).willReturn(publisherBuilder().username(username).build());

        //When
        final PublisherResource publisherResource = publisherResourceBuilder().username(username).build();
        final ResponseEntity<PublisherResource> response = publisherController.create(publisherResource);

        //Then
        verifyZeroInteractions(publisherResourceAssembler);
        assertThat(response.getStatusCode()).isEqualTo(BAD_REQUEST);
    }
}
