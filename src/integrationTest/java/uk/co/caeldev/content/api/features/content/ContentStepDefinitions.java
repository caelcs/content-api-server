package uk.co.caeldev.content.api.features.content;

import com.jayway.restassured.response.Response;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.BaseStepDefinitions;
import uk.co.caeldev.content.api.features.publisher.Publisher;
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
import static uk.co.caeldev.content.api.features.content.ContentStatus.UNREAD;
import static uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder.contentResourceBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.org.fyodor.generators.RDG.string;

public class ContentStepDefinitions extends BaseStepDefinitions {

    @Autowired
    private PublisherRepository publisherRepository;
    
    private Publisher publisher;
    private String accessToken;
    private ContentResource contentResource;
    private ContentResource responseBody;
    private int statusCode;

    @Before
    public void startWireMockServer() {
        super.startWireMockServer();
    }

    @After
    public void stopWireMockServer() {
        super.stopWireMockServer();
    }

    @Given("^a publisher$")
    public void a_publisher() throws Throwable {
        publisher = publisherBuilder().build();
        publisherRepository.save(publisher);
    }

    @And("^credentials validation is (.+)$")
    public void credentials_validation_is_are_credentials_valid(boolean areCredentialsValid) throws Throwable {
        accessToken = UUID.randomUUID().toString();
        String username = areCredentialsValid? publisher.getUsername() : string().next();
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(username).build());

        givenOauthServerMock(accessToken, userJson);
    }

    @And("^\"([^\"]*)\" as new content to be published$")
    public void as_new_content_to_be_published(String content) throws Throwable {
        contentResource = contentResourceBuilder()
                .content(content)
                .build();
    }

    @When("^publish new content$")
    public void publish_new_content() throws Throwable {
        Response response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
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

    @Then("^the response is (\\d+)$")
    public void the_response_is_status_code(int expectedStatusCode) throws Throwable {
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
}
