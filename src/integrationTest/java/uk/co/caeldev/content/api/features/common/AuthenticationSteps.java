package uk.co.caeldev.content.api.features.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.And;

import java.util.UUID;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpHeaders.CONTENT_TYPE;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static uk.co.caeldev.content.api.builders.UserBuilder.userBuilder;

public class AuthenticationSteps {

    private ObjectMapper objectMapper = new ObjectMapper();

    private WireMockServer wireMockServer = new WireMockServer(9080);

    private String accessToken;

    @Before
    public void startWireMockServer() {
        wireMockServer.start();
        WireMock.configureFor(9080);
        WireMock.reset();
    }

    @After
    public void stopWireMockServer() {
        wireMockServer.stop();
    }

    @And("^credential details have been provided using username (.+)$")
    public void credential_details_with_username_credential_username(String credentialUsername) throws Throwable {
        accessToken = UUID.randomUUID().toString();
        final String userJson = objectMapper.writeValueAsString(userBuilder().username(credentialUsername).build());

        givenOauthServerMock(accessToken, userJson);
    }

    private void givenOauthServerMock(String accessToken, String userJson) {
        stubFor(get(urlEqualTo("/sso/user"))
                .willReturn(aResponse()
                        .withStatus(OK.value())
                        .withHeader(CONTENT_TYPE, APPLICATION_JSON_VALUE)
                        .withHeader(AUTHORIZATION, accessToken)
                        .withBody(userJson)));
    }

    public String getAccessToken() {
        return accessToken;
    }
}
