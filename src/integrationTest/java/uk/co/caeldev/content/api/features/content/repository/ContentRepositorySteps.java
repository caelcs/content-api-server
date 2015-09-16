package uk.co.caeldev.content.api.features.content.repository;

import com.google.common.collect.Lists;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import uk.co.caeldev.content.api.features.BaseRepositoryConfiguration;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentBuilder;
import uk.co.caeldev.content.api.features.content.ContentStatus;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentRepositorySteps extends BaseRepositoryConfiguration {

    private final ContentRepository contentRepository;

    private Content content;
    private Content result;
    private List<Page<Content>> resultsPaginated;

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

    @When("^find all content by status (.+) paginated with page size (.+) and publisherId (.+)$")
    public void find_all_content_by_status_status_paginated_with_page_size_page_size_and_publisherId_publisher_id(ContentStatus status, int pageSize, String publisherId) throws Throwable {
        final Page<Content> page0 = contentRepository.findAllContentByStatusPublisherIdPaginated(status, publisherId, new PageRequest(0, pageSize));
        resultsPaginated = Lists.<Page<Content>>newArrayList(page0);

        final int totalPages = page0.getTotalPages();

        if (totalPages == 1) {return;}

        for (int page = 1; page < totalPages; page++) {
            final Page<Content> resultPage = contentRepository.findAllContentByStatusPublisherIdPaginated(status, publisherId, new PageRequest(page, pageSize));
            resultsPaginated.add(resultPage);
        }
    }

    @Then("^the number of pages is (.+)$")
    public void the_number_of_pages_is_expected_number_pages(int expectedNumberPages) throws Throwable {
        assertThat(resultsPaginated).hasSize(expectedNumberPages);
    }

    @And("^each page has size of (.+)$")
    public void each_page_has_size_of_page_size(int pageSize) throws Throwable {
        for (Page<Content> contents : resultsPaginated) {
            assertThat(contents).hasSize(pageSize);
        }
    }

    @And("^publisher id is (.+) for all content$")
    public void publisher_id_is_publisher_id_for_all_content(String publisherId) throws Throwable {
        for (Page<Content> contents : resultsPaginated) {
            for (Content contentResult : contents.getContent()) {
                assertThat(contentResult.getPublisherId()).isEqualTo(publisherId);
            }
        }
    }


}