package uk.co.caeldev.content.api.features.content.repository;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseRepositoryConfiguration;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentRepositorySteps extends BaseRepositoryConfiguration {

    @Autowired
    private ContentRepository contentRepository;

    private Content content;
    private Content result;

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
}