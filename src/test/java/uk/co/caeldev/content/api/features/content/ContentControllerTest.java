package uk.co.caeldev.content.api.features.content;

import org.assertj.core.api.Condition;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.http.HttpEntity;
import uk.co.caeldev.content.api.builders.UserBuilder;
import uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder;
import uk.co.caeldev.springsecuritymongo.domain.User;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static uk.org.fyodor.generators.RDG.string;

@RunWith(MockitoJUnitRunner.class)
public class ContentControllerTest {

    @Mock
    private ContentResourceAssembler contentResourceAssembler;

    @Mock
    private ContentService contentService;

    private ContentController contentController;

    @Before
    public void testee() {
        contentController = new ContentController(contentService, contentResourceAssembler);
    }

    @Test
    public void shouldPublishContentForAPublisher() throws Exception {
        //Given
        final UUID publisherUUID = UUID.randomUUID();
        final UUID contentUUID = UUID.randomUUID();
        final String content = string().next();
        final User user = UserBuilder.userBuilder().build();

        //And
        final Content expectedContent = ContentBuilder.contentBuilder().content(content).contentUUID(contentUUID).username(user.getUsername()).build();
        given(contentService.publish(content, user.getUsername())).willReturn(expectedContent);

        //And
        given(contentResourceAssembler.toResource(expectedContent)).willReturn(ContentResourceBuilder.contentResourceBuilder().content(content).contentUUID(contentUUID).build());

        //When
        final HttpEntity<ContentResource> publishedContent = contentController.publish(publisherUUID, content, user);

        //Then
        assertThat(publishedContent.getBody()).is(new Condition<ContentResource>() {
            @Override
            public boolean matches(ContentResource value) {
                return value.getContentUUID().equals(contentUUID) &&
                        value.getContent().equals(content);
            }
        });
    }
}
