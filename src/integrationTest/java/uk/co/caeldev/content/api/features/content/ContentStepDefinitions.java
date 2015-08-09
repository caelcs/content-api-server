package uk.co.caeldev.content.api.features.content;

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
    private ContentResource response;

    @Before
    public void startWireMockServer() {
        wireMockServer.start();
    }

    @After
    public void stopWireMockServer() {
        wireMockServer.stop();
    }

    @Given("^a publisher$")
    public void a_publisher() throws Throwable {
        publisher = publisherBuilder().build();
        publisherRepository.save(publisher);
    }

    @And("^right permissions$")
    public void right_permissions() throws Throwable {
        accessToken = UUID.randomUUID().toString();
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(publisher.getUsername()).build());

        givenOauthServerMock(accessToken, userJson);
    }

    @When("^publish new content$")
    public void publish_new_content() throws Throwable {
        response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
                .body(contentResource)
                .contentType(APPLICATION_JSON_VALUE)
                .post(format("/publishers/%s/contents", publisher.getPublisherUUID()))
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .extract().body().as(ContentResource.class);
    }

    @And("^new content to be published$")
    public void new_content_to_be_published() throws Throwable {
        contentResource = contentResourceBuilder()
                .content(string().next())
                .build();
    }

    @Then("^the content should be persisted and be valid$")
    public void the_content_should_be_persisted_and_be_valid() throws Throwable {
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(response.getContent()).isEqualTo(contentResource.getContent());
        assertThat(response.getContentStatus()).isEqualTo(UNREAD);
        assertThat(response.getContentUUID()).isNotNull();
        assertThat(response.getContentUUID()).isNotEmpty();
        assertThat(response.getCreationDate()).isNotNull();
    }
}
