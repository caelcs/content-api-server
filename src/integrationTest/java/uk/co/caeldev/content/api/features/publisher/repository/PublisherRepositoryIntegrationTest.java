package uk.co.caeldev.content.api.features.publisher.repository;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.AbstractRepositoryIntegrationTest;
import uk.co.caeldev.content.api.features.publisher.Publisher;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

public class PublisherRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Test
    public void shouldPersistPublisher() throws Exception {
        //Given
        final Publisher publisher = publisherBuilder()
                .publisherUUID(UUID.randomUUID().toString())
                .username(string().next()).build();

        //When
        final Publisher result = publisherRepository.save(publisher);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotEmpty();
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldFindPublisherByUUID() throws Exception {
        //Given
        String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";

        //When
        final Publisher result = publisherRepository.findByUUID(publisherUUID);

        //Then
        assertThat(result.getId()).isEqualTo("7749aa97-2e2f-4666-b396-475fd29c5da4");
        assertThat(result.getPublisherUUID()).isEqualTo(publisherUUID);
        assertThat(result.getUsername()).isEqualTo("(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU");
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldFindPublisherByUUIDAndUsername() throws Exception {
        //Given
        final String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";
        final String username = "(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU";

        //When
        final Publisher result = publisherRepository.findByUUIDAndUsername(publisherUUID, username);

        //Then
        assertThat(result.getId()).isEqualTo("7749aa97-2e2f-4666-b396-475fd29c5da4");
        assertThat(result.getPublisherUUID()).isEqualTo(publisherUUID);
        assertThat(result.getUsername()).isEqualTo(username);
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldNotFindPublisherWhenUUIDIsInvalid() throws Exception {
        //Given
        final String publisherUUID = "invalidUUID";
        final String username = "(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU";

        //When
        final Publisher result = publisherRepository.findByUUIDAndUsername(publisherUUID, username);

        //Then
        assertThat(result).isNull();
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldFindPublisherByUsername() throws Exception {
        //Given
        String username = "test1";

        //When
        final Publisher result = publisherRepository.findByUsername(username);

        //Then
        assertThat(result.getId()).isEqualTo("7749ba97-2e3f-4666-b396-475fd29c6da5");
        assertThat(result.getUsername()).isEqualTo(username);
    }
}
