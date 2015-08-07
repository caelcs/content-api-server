package uk.co.caeldev.content.api.features.publisher;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import org.junit.Test;
import uk.co.caeldev.content.api.features.AbstractControllerIntegrationTest;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.builders.UserBuilder.userBuilder;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;

public class PublisherControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.DELETE_ALL)
    public void shouldPublishANewResourceWhenUsernameDoesNotExist() throws Exception {
        //Given
        final String accessToken = UUID.randomUUID().toString();

        final String username = string().next();
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(username).build());

        //And
        givenOauthServerMock(accessToken, userJson);

        //And
        final PublisherResource publisherResource = publisherResourceBuilder().username(username).build();

        //When
        final PublisherResource response = given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
                .body(publisherResource)
                .contentType(APPLICATION_JSON_VALUE)
                .post("/publishers")
                .then()
                .assertThat()
                .statusCode(CREATED.value())
                .extract().body().as(PublisherResource.class);

        //Then
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(response.getPublisherUUID()).isNotNull();
        assertThat(response.getStatus()).isEqualTo(Status.ACTIVE);
        assertThat(response.getCreationTime()).isNotNull();
        assertThat(response.getUsername()).isEqualTo(username);
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldNotPublishWhenUsernameAlreadyExists() throws Exception {
        //Given
        final String accessToken = UUID.randomUUID().toString();

        final String username = "test1";
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(username).build());

        //And
        givenOauthServerMock(accessToken, userJson);

        //And
        final PublisherResource publisherResource = publisherResourceBuilder().username(username).build();

        //When
        given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
                .body(publisherResource)
                .contentType(APPLICATION_JSON_VALUE)
                .post("/publishers")
                .then()
                .assertThat()
                .statusCode(BAD_REQUEST.value());

        //Then
        verify(getRequestedFor(urlMatching("/sso/user")));
    }
}
