package uk.co.caeldev.content.api.features.publisher.repository;

import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseRepositoryConfiguration;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.Status;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

public class PublisherRepositorySteps extends BaseRepositoryConfiguration {

    private final PublisherRepository publisherRepository;

    private Publisher publisher;
    private Publisher result;

    @Autowired
    public PublisherRepositorySteps(final PublisherRepository publisherRepository) {
        this.publisherRepository = publisherRepository;
    }

    @Before
    public void cleanMongo() {
        publisherRepository.deleteAll();
    }

    @Given("^a random new publisher$")
    public void a_random_new_publisher() throws Throwable {
        publisher = publisherBuilder()
                .publisherUUID(UUID.randomUUID().toString())
                .username(string().next()).build();
    }

    @When("^save the publisher$")
    public void save_the_publisher() throws Throwable {
        result = publisherRepository.save(publisher);
    }

    @Then("^publisher should be persisted$")
    public void publisher_should_be_persisted() throws Throwable {
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotEmpty();
    }

    @When("^try to find the publisher by UUID (.+)$")
    public void try_to_find_the_publisher_by_UUID_publisher_uuid(String publisherUUID) throws Throwable {
        result = publisherRepository.findByUUID(publisherUUID);
    }

    @Then("^should return the persisted publisher$")
    public void should_return_the_persisted_publisher() throws Throwable {
        assertThat(result.getId()).isEqualTo(publisher.getId());
        assertThat(result.getPublisherUUID()).isEqualTo(publisher.getPublisherUUID());
        assertThat(result.getUsername()).isEqualTo(publisher.getUsername());
        assertThat(result.getStatus()).isEqualTo(publisher.getStatus());
    }

    @When("^try to find the publisher by username (.+)$")
    public void try_to_find_the_publisher_by_username_username(String username) throws Throwable {
        result = publisherRepository.findByUsername(username);
    }

    @Then("^should not return any publisher$")
    public void should_not_return_any_publisher() throws Throwable {
        assertThat(result).isNull();
    }

    @When("^try to find the publisher by publisher UUID (.+) and username (.+)$")
    public void try_to_find_the_publisher_by_publisher_UUID_publisher_uuid_and_username_username(String publisherUUID, String username) throws Throwable {
        result = publisherRepository.findByUUIDAndUsername(publisherUUID, username);
    }

    @Given("^a persisted publisher with id (.+) and publisher UUID (.+) and username (.+) and status (.+)$")
    public void a_persisted_publisher_with_publisher_UUID_publisher_uuid(String id, String publisherUUID, String username, String status) throws Throwable {
        final Publisher publisherPersisted = publisherBuilder().id(id).publisherUUID(publisherUUID).username(username).status(Status.valueOf(status)).build();
        publisher = publisherRepository.save(publisherPersisted);
    }

    @When("^try to update publisher status by publisher UUID (.+) to (.+)$")
    public void try_to_update_publisher_status_by_publisher_UUID_publisher_uuid_to_new_status(String publisherUUID, String newStatus) throws Throwable {
        result = publisherRepository.updateStatus(publisherUUID, Status.valueOf(newStatus));
    }

    @Then("^should have the status (.+)$")
    public void should_have_the_status_new_status(String expectedStatus) throws Throwable {
        assertThat(result).isNotNull();
        assertThat(result.getStatus()).isEqualTo(Status.valueOf(expectedStatus));
    }

    @Then("^should not update any publisher$")
    public void should_not_update_any_publisher() throws Throwable {
        assertThat(result).isNull();
    }

    @Given("^a persisted publisher$")
    public void a_persisted_publisher(List<Publisher> publishers) throws Throwable {
        publisherRepository.save(publishers);
    }
}
