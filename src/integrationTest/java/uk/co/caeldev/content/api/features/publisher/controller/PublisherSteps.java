package uk.co.caeldev.content.api.features.publisher.controller;

import com.jayway.restassured.response.Response;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseControllerConfiguration;
import uk.co.caeldev.content.api.features.common.AuthenticationSteps;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherResource;
import uk.co.caeldev.content.api.features.publisher.PublisherResourceAssembler;
import uk.co.caeldev.content.api.features.publisher.Status;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.List;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.IF_MATCH;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;
import static uk.co.caeldev.spring.mvc.ETagBuilder.eTagBuilder;
import static uk.org.fyodor.generators.RDG.string;

public class PublisherSteps extends BaseControllerConfiguration {

    private PublisherRepository publisherRepository;
    private AuthenticationSteps authenticationSteps;

    private PublisherResource publisherResourceToBePersist;
    private int statusCode;
    private PublisherResource responseBody;
    private Publisher existingPublisher;

    @Autowired
    public PublisherSteps(final PublisherRepository publisherRepository,
                          final AuthenticationSteps authenticationSteps) {
        this.publisherRepository = publisherRepository;
        this.authenticationSteps = authenticationSteps;
    }

    @Before
    public void cleanMongo() {
        publisherRepository.deleteAll();
    }

    @Given("^a username (.+)$")
    public void a_username(String username) throws Throwable {
        publisherResourceToBePersist = publisherResourceBuilder().username(username).build();
    }

    @When("^create publisher$")
    public void create_publisher() throws Throwable {
        final Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .body(publisherResourceToBePersist)
                .contentType(APPLICATION_JSON_VALUE)
                .post("/publishers");

        statusCode = response.then()
                .extract().statusCode();

        responseBody = null;

        if (statusCode == CREATED.value()) {
            responseBody = response.then()
                    .extract().body().as(PublisherResource.class);
        }
    }

    @Given("^an existing publisher with username (.+)$")
    public void an_existing_publisher_with_username(String username) throws Throwable {
        existingPublisher = publisherBuilder().username(username).build();
        publisherRepository.save(existingPublisher);
    }

    @And("^a new Publisher with username already taken$")
    public void a_new_Publisher_with_username_already_taken() throws Throwable {
        publisherResourceToBePersist = publisherResourceBuilder().username(existingPublisher.getUsername()).build();
    }

    @When("^delete publisher$")
    public void delete_publisher() throws Throwable {
        final Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .delete("/publishers/{publisherUUID}", existingPublisher.getPublisherUUID());

        statusCode = response.then()
                .extract().statusCode();
    }

    @Then("^the publisher creation response is (\\d+)$")
    public void the_publisher_creation_response_is_status_code(int expectedStatusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(expectedStatusCode);
        if (statusCode == CREATED.value()) {
            assertThat(responseBody.getStatus()).isEqualTo(Status.ACTIVE);
            assertThat(responseBody.getPublisherUUID()).isNotNull();
            assertThat(responseBody.getUsername()).isEqualTo(publisherResourceToBePersist.getUsername());
            assertThat(responseBody.getCreationTime()).isNotNull();
        }
    }

    @Then("^the Publisher Delete response is (\\d+)$")
    public void the_Publisher_Delete_response_is_status_code(int expectedStatusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(expectedStatusCode);
    }

    @Given("^an existing group of publishers$")
    public void an_existing_group_of_publishers(List<Publisher> publishers) throws Throwable {
        publisherRepository.save(publishers);
    }

    @When("^update publisher by username (.+) and publisherUUID (.+) with the new status (.+)$")
    public void update_publisher_by_username_username_and_publisherUUID_publisherUUID_with_the_new_status_new_status(String username, String publisherUUID, Status status) throws Throwable {

        final Publisher publisherToBeUpdate = publisherRepository.findByUsername(username);
        publisherResourceToBePersist = new PublisherResourceAssembler().toResource(publisherToBeUpdate);

        final String eTag = eTagBuilder().value(String.valueOf(publisherResourceToBePersist.hashCode())).build();

        publisherToBeUpdate.setStatus(status);
        final PublisherResource publisherResource = new PublisherResourceAssembler().toResource(publisherToBeUpdate);

        final Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .header(IF_MATCH, eTag)
                .body(publisherResource)
                .contentType(APPLICATION_JSON_VALUE)
                .put(String.format("/publishers/%s", publisherUUID));

        statusCode = response.then()
                .extract().statusCode();

        responseBody = null;

        if (statusCode == OK.value()) {
            responseBody = response.then()
                    .extract().body().as(PublisherResource.class);
        }
    }

    @Then("^the publisher update response is (.+) and new status is (.+)$")
    public void the_publisher_update_response_is_status_code_and_new_status_is_new_status(int statusCode, Status status) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(statusCode);
        if (statusCode == OK.value()) {
            assertThat(responseBody.getStatus()).isEqualTo(status);
            assertThat(responseBody.getPublisherUUID()).isEqualTo(publisherResourceToBePersist.getPublisherUUID());
            assertThat(responseBody.getUsername()).isEqualTo(publisherResourceToBePersist.getUsername());
            assertThat(responseBody.getCreationTime()).isNotNull();
        }
    }

    @When("^update publisher using with wrong ETag by username (.+) and publisherUUID (.+) with the new status (.+)$")
    public void update_publisher_using_with_wrong_ETag_by_username_username_and_publisherUUID_publisherUUID_with_the_new_status_new_status(String username, String publisherUUID, Status status) throws Throwable {
        final String eTag = string().next();

        final PublisherResource publisherResource = publisherResourceBuilder().build();

        final Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", authenticationSteps.getAccessToken()))
                .header(IF_MATCH, eTag)
                .body(publisherResource)
                .contentType(APPLICATION_JSON_VALUE)
                .put(String.format("/publishers/%s", publisherUUID));

        statusCode = response.then()
                .extract().statusCode();
    }

    @Then("^the publisher update response status is (.+)$")
    public void the_publisher_update_response_status_is_status_code(int statusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(statusCode);
    }
}
