package uk.co.caeldev.content.api.features.content;

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
import static org.springframework.http.HttpStatus.*;
import static org.springframework.http.MediaType.TEXT_PLAIN_VALUE;
import static uk.co.caeldev.content.api.builders.UserBuilder.userBuilder;
import static uk.co.caeldev.content.api.features.content.ContentStatus.UNREAD;


public class ContentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldPublishANewResource() throws Exception {

        //Given
        final String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";
        final String accessToken = UUID.randomUUID().toString();
        final String content = "{}";

        final String userJson = objectMapper.writeValueAsString(userBuilder().username("(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU").build());

        //And
        givenOauthServerMock(accessToken, userJson);

        //When
        final ContentResource response = given().port(port).basePath(basePath).log().all()
                .when()
                    .header(AUTHORIZATION, format("Bearer %s", accessToken))
                    .body(content)
                    .contentType(TEXT_PLAIN_VALUE)
                    .post(format("/publishers/%s/contents", publisherUUID))
                .then()
                    .assertThat()
                    .statusCode(CREATED.value())
                    .extract().body().as(ContentResource.class);

        //Then
        verify(getRequestedFor(urlMatching("/sso/user")));
        assertThat(response.getContent()).isEqualTo(content);
        assertThat(response.getContentStatus()).isEqualTo(UNREAD);
        assertThat(response.getContentUUID()).isNotNull();
        assertThat(response.getContentUUID()).isNotEmpty();
        assertThat(response.getCreationDate()).isNotNull();
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldReturnBadRequestStatusWhenContentIsEmpty() throws Exception {

        //Given
        final String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";
        final String accessToken = UUID.randomUUID().toString();
        final String content = "";

        final String userJson = objectMapper.writeValueAsString(userBuilder().username("(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU").build());

        //And
        givenOauthServerMock(accessToken, userJson);

        //When
        given().port(port).basePath(basePath).log().all()
                .when()
                    .header(AUTHORIZATION, format("Bearer %s", accessToken))
                    .body(content)
                    .contentType(TEXT_PLAIN_VALUE)
                    .post(format("/publishers/%s/contents", publisherUUID))
                .then()
                    .assertThat()
                    .statusCode(BAD_REQUEST.value());

        //Then
        verify(getRequestedFor(urlMatching("/sso/user")));
    }

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldReturnForbiddenStatusWhenPublisherUUIDAndUsernameDoNotCorrespond() throws Exception {

        //Given
        final String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";
        final String accessToken = UUID.randomUUID().toString();
        final String content = "{}";

        final String userJson = objectMapper.writeValueAsString(userBuilder().username("test1").build());

        //And
        givenOauthServerMock(accessToken, userJson);

        //When
        given().port(port).basePath(basePath).log().all()
                .when()
                .header(AUTHORIZATION, format("Bearer %s", accessToken))
                .body(content)
                .contentType(TEXT_PLAIN_VALUE)
                .post(format("/publishers/%s/contents", publisherUUID))
                .then()
                .assertThat()
                .statusCode(FORBIDDEN.value());

        //Then
        verify(getRequestedFor(urlMatching("/sso/user")));
    }
}
