package uk.co.caeldev.content.api.features.publisher.repository;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import com.lordofthejars.nosqlunit.mongodb.MongoDbRule;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.ApplicationContext;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.config.TestRepositoryConfiguration;

import java.util.UUID;

import static com.lordofthejars.nosqlunit.mongodb.MongoDbRule.MongoDbRuleBuilder.newMongoDbRule;
import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {TestRepositoryConfiguration.class})
public class PublisherRepositoryIntegrationTest {

    @Rule
    public MongoDbRule mongoDbRule = newMongoDbRule().defaultSpringMongoDb("contentapi");

    @Autowired
    private ApplicationContext context;

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
}
