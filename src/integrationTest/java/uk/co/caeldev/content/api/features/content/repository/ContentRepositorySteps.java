package uk.co.caeldev.content.api.features.content.repository;

import cucumber.api.PendingException;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseRepositoryConfiguration;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentBuilder;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentRepositorySteps extends BaseRepositoryConfiguration {

    private final ContentRepository contentRepository;

    private Content content;
    private Content result;

    @Autowired
    public ContentRepositorySteps(final ContentRepository contentRepository) {
        this.contentRepository = contentRepository;
    }

    @Before
    public void cleanMongo() {
        contentRepository.deleteAll();
    }

    @Given("^a random new content$")
    public void a_random_new_content() throws Throwable {
        content = ContentBuilder.contentBuilder().build();
    }

    @When("^save the content$")
    public void save_the_content() throws Throwable {
        result = contentRepository.save(content);
    }

    @Then("^content should be persisted$")
    public void content_should_be_persisted() throws Throwable {
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotEmpty();
        assertThat(result).isEqualTo(content);
    }

    @And("^the following persisted content associated to a publisher$")
    public void the_following_persisted_content_associated_to_a_publisher_associated_publisher_id(List<Content> contents) throws Throwable {
        contentRepository.save(contents);
    }

    @When("^find content by uuid (.+)$")
    public void find_content_by_uuid_expected_content_uuid(String contentUUID) throws Throwable {
        result = contentRepository.findOneByUUID(contentUUID);
    }


    @Then("^the numbers of content should be (.+) and content uuid should be (.+)$")
    public void the_numbers_of_content_should_be_expected_count_content_and_content_uuid_should_be_expected_content_uuid(int expectedCountContent, String expectedContentUUID) throws Throwable {
        if (expectedCountContent == 0) {
            assertThat(result).isNull();
        } else {
            assertThat(result).isNotNull();
            assertThat(result.getContentUUID()).isEqualTo(expectedContentUUID);
        }
    }
}