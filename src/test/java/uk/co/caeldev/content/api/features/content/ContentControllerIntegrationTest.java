package uk.co.caeldev.content.api.features.content;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import org.junit.Test;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import uk.co.caeldev.content.api.features.AbstractControllerIntegrationTest;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static com.jayway.restassured.RestAssured.given;
import static java.lang.String.format;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.builders.UserBuilder.userBuilder;


public class ContentControllerIntegrationTest extends AbstractControllerIntegrationTest {

    @Test
    @UsingDataSet(loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void shouldPublishANewResource() throws Exception {

        //Given
        final String publisherUUID = "44d74b78-a235-45bf-9aa5-79b72e1531ad";
        final String accessToken = UUID.randomUUID().toString();

        final String userJson = objectMapper.writeValueAsString(userBuilder().username("(8/F#@]L0K?<c<>Jx*I|`0TjP79zjU").build());

        stubFor(get(urlEqualTo("/sso/user"))
                .willReturn(aResponse()
                        .withStatus(HttpStatus.OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withHeader(AUTHORIZATION, accessToken)
                        .withBody(userJson)));

        given().port(port).basePath(basePath).log().all()
                .when()
                .header(HttpHeaders.AUTHORIZATION, format("Bearer %s", accessToken))
                .body("{}")
                .contentType(MediaType.TEXT_PLAIN_VALUE)
                .post(format("/publishers/%s/contents", publisherUUID))
                .then()
                .assertThat()
                .statusCode(HttpStatus.CREATED.value());
    }
}
