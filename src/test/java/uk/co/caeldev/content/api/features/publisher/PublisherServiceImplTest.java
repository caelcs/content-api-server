package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
}