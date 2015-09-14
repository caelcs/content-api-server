package uk.co.caeldev.content.api.features.content.controller;

import com.jayway.restassured.response.Response;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseControllerConfiguration;
import uk.co.caeldev.content.api.features.common.AuthenticationSteps;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentResource;
import uk.co.caeldev.content.api.features.content.repository.ContentRepository;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.features.content.ContentStatus.UNREAD;
import static uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder.contentResourceBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;

public class ContentSteps extends BaseControllerConfiguration {

    private final PublisherRepository publisherRepository;
    private final ContentRepository contentRepository;
    private final AuthenticationSteps authenticationSteps;

    private Publisher publisher;
    private ContentResource contentResource;
    private ContentResource responseBody;
    private int statusCode;

    @Autowired
    public ContentSteps(final PublisherRepository publisherRepository,
                        final ContentRepository contentRepository,
                        final AuthenticationSteps authenticationSteps) {
        this.publisherRepository = publisherRepository;
        this.contentRepository = contentRepository;
        this.authenticationSteps = authenticationSteps;
    }

    @Before
    public void cleanMongo() {
        contentRepository.deleteAll();
        publisherRepository.deleteAll();
    }

    @Given("^a publisher with username (.+)$")
    public void a_publisher_with_username(String username) throws Throwable {
        publisher = publisherBuilder().username(username).build();
        publisherRepository.save(publisher);
    }

    @And("^a new content (.+) to be published$")
    public void a_new_content_content_to_be_published(String content) throws Throwable {
        final String valueOrEmpty = content.equals("EMPTY") ? "" : content;
        contentResource = contentResourceBuilder()
                .content(valueOrEmpty)
                .build();
    }

    @When("^publish new content$")
    public void publish_new_content() throws Throwable {
        Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .body(contentResource)
                .contentType(APPLICATION_JSON_VALUE)
                .post(format("/publishers/%s/contents", publisher.getPublisherUUID()));

        statusCode = response.then()
                .extract().statusCode();

        responseBody = null;

        if (statusCode == CREATED.value()) {
            responseBody = response.then()
                    .extract().body().as(ContentResource.class);
        }
    }

    @Then("^the content creation response is (\\d+)$")
    public void the_content_creation_response_is_status_code(int expectedStatusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(expectedStatusCode);

        if (statusCode == CREATED.value()) {
            assertThat(responseBody.getContent()).isEqualTo(contentResource.getContent());
            assertThat(responseBody.getContentStatus()).isEqualTo(UNREAD);
            assertThat(responseBody.getContentUUID()).isNotNull();
            assertThat(responseBody.getContentUUID()).isNotEmpty();
            assertThat(responseBody.getCreationDate()).isNotNull();
        }
    }

    @Given("^a publisher with id (.+), username (.+) and publisher uuid (.+)$")
    public void a_publisher_with_id_publisher_id_username_username_and_publisher_uuid_publisher_uuid(String publisherId, String username, String publisherUUID) throws Throwable {
        final Publisher publisher = publisherBuilder().id(publisherId).publisherUUID(publisherUUID).username(username).build();
        publisherRepository.save(publisher);
    }

    @And("^the following persisted content associated to a publisher$")
    public void the_following_persisted_content_associated_to_a_publisher_associated_publisher_id(List<Content> contents) throws Throwable {
        contentRepository.save(contents);
    }

    @When("^get a content by UUID (.+) and publisher UUID (.+)$")
    public void get_a_content_by_UUID_content_uuid_and_publisher_UUID_publisher_uuid(String contentUUID, String publisherUUID) throws Throwable {
        Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .contentType(APPLICATION_JSON_VALUE)
                .get(format("/publishers/%s/contents/%s", publisherUUID, contentUUID));

        statusCode = response.then()
                .extract().statusCode();

        responseBody = null;

        if (statusCode == OK.value()) {
            responseBody = response.then()
                    .extract().body().as(ContentResource.class);
        }
    }

    @Then("^the content get response is (.+)$")
    public void the_content_get_response_is_status_code(int expectedStatusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(this.statusCode).isEqualTo(expectedStatusCode);
    }
}
