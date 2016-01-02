package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.TestingAuthenticationToken;

import java.security.Principal;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyZeroInteractions;
import static org.springframework.http.HttpStatus.*;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;

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

    @Test
    public void shouldChangeStatusToDeletedWhenPublisherExists() throws Exception {
        //Given
        final UUID publisherUUID = UUID.randomUUID();

        //When
        final ResponseEntity<PublisherResource> response = publisherController.delete(publisherUUID.toString());

        //Then
        verify(publisherService).delete(publisherUUID.toString());
        assertThat(response.getStatusCode()).isEqualTo(NO_CONTENT);
    }

    @Test
    public void shouldUpdatePublisherWhenExists() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();
        final Publisher publisherSaved = publisherBuilder().publisherUUID(publisherUUID).username(string().next()).status(Status.ACTIVE).build();
        final PublisherResource publisherResource = publisherResourceBuilder()
                .publisher(publisherSaved)
                .build();

        //And
        given(publisherService.getPublisherByUUID(publisherUUID)).willReturn(publisherSaved);

        //And
        final Publisher expectedPublisherToBeUpdated = publisherBuilder()
                .publisherUUID(publisherResource.getPublisherUUID())
                .status(publisherResource.getStatus())
                .username(publisherResource.getUsername())
                .build();

        given(publisherResourceAssembler.toDomain(publisherResource, publisherSaved))
                .willReturn(expectedPublisherToBeUpdated);

        //And
        given(publisherService.update(expectedPublisherToBeUpdated)).willReturn(expectedPublisherToBeUpdated);

        //And
        final PublisherResource publisherResourceUpdated = publisherResourceBuilder()
                .publisher(expectedPublisherToBeUpdated)
                .build();

        given(publisherResourceAssembler.toResource(expectedPublisherToBeUpdated))
                .willReturn(publisherResourceUpdated);

        //When
        final ResponseEntity<PublisherResource> response = publisherController.update(publisherUUID, publisherResource);

        //Then
        assertThat(response.getBody().getStatus()).isEqualTo(publisherResource.getStatus());
        assertThat(response.getStatusCode()).isEqualTo(OK);
    }

    @Test
    public void shouldGetPublisherByUUID() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder().publisherUUID(publisherUUID).build();
        given(publisherService.getPublisherByUUID(publisherUUID)).willReturn(expectedPublisher);

        //And
        given(publisherResourceAssembler.toResource(expectedPublisher))
                .willReturn(publisherResourceBuilder().publisher(expectedPublisher).build());

        //When
        final ResponseEntity<PublisherResource> response = publisherController.get(publisherUUID);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        final PublisherResource body = response.getBody();
        assertThat(body).isNotNull();
        assertThat(body.getPublisherUUID()).isEqualTo(expectedPublisher.getPublisherUUID());
        assertThat(body.getStatus()).isEqualTo(expectedPublisher.getStatus());
        assertThat(body.getCreationTime()).isEqualTo(expectedPublisher.getCreationTime());
        assertThat(body.getUsername()).isEqualTo(expectedPublisher.getUsername());
    }

    @Test
    public void shouldGetPublisherAssociatedToToken() throws Exception {
        //Given
        String username = string().next();
        Principal principal = new TestingAuthenticationToken(username, string().next());

        //And
        final Publisher expectedPublisher = publisherBuilder().username(username).build();
        given(publisherService.getPublisherByUsername(username))
                .willReturn(expectedPublisher);

        //And
        given(publisherResourceAssembler.toResource(expectedPublisher))
                .willReturn(publisherResourceBuilder().publisher(expectedPublisher).build());

        //When
        final ResponseEntity<PublisherResource> response = publisherController.currentPublisherFromToken(principal);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);

        final PublisherResource body = response.getBody();
        assertThat(body.getPublisherUUID()).isEqualTo(expectedPublisher.getPublisherUUID());
        assertThat(body.getStatus()).isEqualTo(expectedPublisher.getStatus());
        assertThat(body.getCreationTime()).isEqualTo(expectedPublisher.getCreationTime());
        assertThat(body.getUsername()).isEqualTo(expectedPublisher.getUsername());
    }

    @Test
    public void shouldNotGetPublisherAssociatedToTokenWhenPublisherDoesNotExists() throws Exception {
        //Given
        String username = string().next();
        Principal principal = new TestingAuthenticationToken(username, string().next());

        //And
        given(publisherService.getPublisherByUsername(username))
                .willReturn(null);

        //When
        final ResponseEntity<PublisherResource> response = publisherController.currentPublisherFromToken(principal);

        //Then
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
