package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

@RunWith(MockitoJUnitRunner.class)
public class PublisherServiceImplTest {

    @Mock
    private PublisherRepository publisherRepository;

    private PublisherService publisherService;

    @Before
    public void testee() throws Exception {
        publisherService = new PublisherServiceImpl(publisherRepository);
    }

    @Test
    public void shouldGetPublisherByUUID() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();
        final String username = string().next();
        final String id = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .id(id)
                .publisherUUID(publisherUUID)
                .username(username).build();

        given(publisherRepository.findByUUID(publisherUUID)).willReturn(expectedPublisher);

        //When
        final Publisher result = publisherService.getPublisherByUUID(publisherUUID);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPublisherUUID()).isEqualTo(publisherUUID);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldGetPublisherByUUIDAndUsername() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();
        final String username = string().next();
        final String id = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .id(id)
                .publisherUUID(publisherUUID)
                .username(username).build();

        given(publisherRepository.findByUUIDAndUsername(publisherUUID, username)).willReturn(expectedPublisher);

        //When
        final Publisher result = publisherService.getPublisherByUUIDAndUsername(publisherUUID, username);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPublisherUUID()).isEqualTo(publisherUUID);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldGetPublisherByUsername() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();
        final String username = string().next();
        final String id = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder()
                .id(id)
                .publisherUUID(publisherUUID)
                .username(username).build();

        given(publisherRepository.findByUsername(username)).willReturn(expectedPublisher);

        //When
        final Publisher result = publisherService.getPublisherByUsername(username);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getPublisherUUID()).isEqualTo(publisherUUID);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    public void shouldCreatePublisher() throws Exception {
        //Given
        final String username = string().next();

        //And
        final ArgumentCaptor<Publisher> argumentCaptor = ArgumentCaptor.forClass(Publisher.class);

        //When
        publisherService.create(username);

        //Then
        verify(publisherRepository).save(argumentCaptor.capture());

        final Publisher argumentCaptorValue = argumentCaptor.getValue();
        assertThat(argumentCaptorValue.getPublisherUUID()).isNotEmpty();
        assertThat(argumentCaptorValue.getUsername()).isEqualTo(username);
        assertThat(argumentCaptorValue.getCreationTime()).isNotNull();
        assertThat(argumentCaptorValue.getStatus()).isEqualTo(Status.ACTIVE);
    }

    @Test
    public void shouldDeletePublisher() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();

        //And
        final Publisher expectedPublisher = publisherBuilder().publisherUUID(publisherUUID).build();
        given(publisherRepository.updateStatus(publisherUUID, Status.DELETED)).willReturn(expectedPublisher);

        //When
        publisherService.delete(publisherUUID);

        //Then
        verify(publisherRepository).updateStatus(publisherUUID, Status.DELETED);
    }

    @Test(expected = NullPointerException.class)
    public void shouldNotDeleteWhenPublisherDoesNotExists() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();

        //And
        given(publisherRepository.updateStatus(publisherUUID, Status.DELETED)).willReturn(null);

        //When
        publisherService.delete(publisherUUID);
    }

    @Test
    public void shouldUpdatePublisherWhenUUIDIsValid() throws Exception {
        //Given
        final String publisherUUID = UUID.randomUUID().toString();

        //And
        final Publisher publisher = PublisherBuilder.publisherBuilder().publisherUUID(publisherUUID).build();
        given(publisherRepository.update(publisher)).willReturn(publisher);

        //When
        final Publisher result = publisherService.update(publisher);

        //Then
        assertThat(result).isEqualTo(publisher);
    }
}