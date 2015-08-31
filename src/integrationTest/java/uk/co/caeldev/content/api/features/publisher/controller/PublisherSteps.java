package uk.co.caeldev.content.api.features.publisher.controller;

import com.jayway.restassured.response.Response;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseControllerConfiguration;
import uk.co.caeldev.content.api.features.common.AuthenticationSteps;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherResource;
import uk.co.caeldev.content.api.features.publisher.Status;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;

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
}
