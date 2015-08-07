package uk.co.caeldev.content.api.features.security;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.security.authentication.TestingAuthenticationToken;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherService;
import uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;

@RunWith(MockitoJUnitRunner.class)
public class ResourceOwnerEvaluatorTest {

    @Mock
    private PublisherService publisherService;

    private ResourceOwnerEvaluator resourceOwnerEvaluator;

    @Before
    public void testee() throws Exception {
        resourceOwnerEvaluator = new ResourceOwnerEvaluator(publisherService);
    }

    @Test
    public void shouldAllowAccessWhenPublisherMatchWithUsernameAndHasRightPermission() throws Exception {
        //Given
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken("testusername", "testpassword");
        final UUID publisherUUID = UUID.randomUUID();

        //And
        final Publisher expectedPublisher = PublisherBuilder.publisherBuilder().build();
        given(publisherService.getPublisherByUUIDAndUsername(publisherUUID.toString(), authentication.getName())).willReturn(expectedPublisher);

        //When
        final boolean result = resourceOwnerEvaluator.hasPermission(authentication, publisherUUID, Permissions.PUBLISHER_OWN_CONTENT.toString());

        //Then
        assertThat(result).isTrue();
    }

    @Test
    public void shouldNotAllowAccessWhenPublisherDoesNotMatchWithUsernameAndHasRightPermission() throws Exception {
        //Given
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken("testusername", "testpassword");
        final UUID publisherUUID = UUID.randomUUID();

        //And
        given(publisherService.getPublisherByUUIDAndUsername(publisherUUID.toString(), authentication.getName())).willReturn(null);

        //When
        final boolean result = resourceOwnerEvaluator.hasPermission(authentication, publisherUUID, Permissions.PUBLISHER_OWN_CONTENT.toString());

        //Then
        assertThat(result).isFalse();
    }

    @Test
    public void shouldNotAllowAccessWhenPublisherMatchWithUsernameAndHasWrongPermission() throws Exception {
        //Given
        final TestingAuthenticationToken authentication = new TestingAuthenticationToken("testusername", "testpassword");
        final UUID publisherUUID = UUID.randomUUID();

        //And
        final Publisher expectedPublisher = PublisherBuilder.publisherBuilder().build();
        given(publisherService.getPublisherByUUIDAndUsername(publisherUUID.toString(), authentication.getName())).willReturn(expectedPublisher);

        //When
        final boolean result = resourceOwnerEvaluator.hasPermission(authentication, publisherUUID, string().next());

        //Then
        assertThat(result).isFalse();
    }

}