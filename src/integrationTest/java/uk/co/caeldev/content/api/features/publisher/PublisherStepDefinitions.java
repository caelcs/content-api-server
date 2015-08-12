package uk.co.caeldev.content.api.features.publisher;

import com.jayway.restassured.response.Response;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseControllerConfiguration;
import uk.co.caeldev.content.api.features.publisher.repository.PublisherRepository;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.builders.UserBuilder.userBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;
import static uk.org.fyodor.generators.RDG.string;

public class PublisherStepDefinitions extends BaseControllerConfiguration {

    @Autowired
    private PublisherRepository publisherRepository;

    private PublisherResource publisherResourceToBePersist;
    private String accessToken;
    private int statusCode;
    private PublisherResource responseBody;
    private Publisher existingPublisher;

    @Before
    public void startWireMockServer() {
        super.startWireMockServer();
    }

    @After
    public void stopWireMockServer() {
        super.stopWireMockServer();
    }

    @Given("^a username \"([^\"]*)\"$")
    public void a_username(String username) throws Throwable {
        publisherResourceToBePersist = publisherResourceBuilder().username(username).build();
    }

    @And("^with valid credentials$")
    public void with_valid_credentials() throws Throwable {
        accessToken = UUID.randomUUID().toString();
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(string().next()).build());

        givenOauthServerMock(accessToken, userJson);
    }

    @When("^create publisher$")
    public void create_publisher() throws Throwable {
        final Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
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

    @Then("^the response is (\\d+)$")
    public void the_response_is_status_code(int expectedStatusCode) throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(statusCode).isEqualTo(expectedStatusCode);
        if (statusCode == CREATED.value()) {
            assertThat(responseBody.getStatus()).isEqualTo(Status.ACTIVE);
            assertThat(responseBody.getPublisherUUID()).isNotNull();
            assertThat(responseBody.getUsername()).isEqualTo(publisherResourceToBePersist.getUsername());
            assertThat(responseBody.getCreationTime()).isNotNull();
        }
    }

    @Given("^an existing publisher with username \"([^\"]*)\"$")
    public void an_existing_publisher_with_username(String username) throws Throwable {
        existingPublisher = publisherBuilder().username(username).build();
        publisherRepository.save(existingPublisher);
    }

    @And("^a new Publisher with the same username$")
    public void a_new_Publisher_with_the_same_username() throws Throwable {
        publisherResourceToBePersist = publisherResourceBuilder().username(existingPublisher.getUsername()).build();
    }
}
