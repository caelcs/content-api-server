package uk.co.caeldev.content.api.features.publisher.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.repository.config.TestRepositoryConfiguration;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@ActiveProfiles("test")
@SpringApplicationConfiguration(classes = {TestRepositoryConfiguration.class})
public class PublisherRepositoryIntegrationTest {

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
}
