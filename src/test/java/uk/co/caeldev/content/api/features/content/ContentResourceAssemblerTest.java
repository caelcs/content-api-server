package uk.co.caeldev.content.api.features.content;

import org.assertj.core.api.Condition;
import org.joda.time.DateTime;
import org.junit.Before;
import org.junit.Test;
import uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.ofContent;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;
import static uk.co.caeldev.content.api.features.content.builders.ContentResourceBuilder.contentResourceBuilder;

public class ContentResourceAssemblerTest {

    private ContentResourceAssembler contentResourceAssembler;

    @Before
    public void setup() {
        contentResourceAssembler = new ContentResourceAssembler();
    }

    @Test
    public void shouldConvertContentToResourceContent() throws Exception {
        // Given
        final Content content = ofContent().next();

        // When
        final ContentResource contentResource = contentResourceAssembler.toResource(content);

        // Then
        final ContentResource expectedContentResource = contentResourceBuilder().content(content.getContent()).contentUUID(content.getContentUUID()).status(content.getStatus()).creationDate(content.getCreationDate()).build();
        assertThat(contentResource).isEqualTo(expectedContentResource);
    }

    @Test
    public void shouldConvertResourceContentToContent() throws Exception {
        // Given
        final String content = string().next();
        final UUID contentUUID = UUID.randomUUID();
        final DateTime creationDate = DateTime.now();
        final ContentStatus contentStatus = ContentStatus.UNREAD;
        final ContentResource contentResource = ContentResourceBuilder.contentResourceBuilder().content(content).contentUUID(contentUUID).creationDate(creationDate).status(contentStatus).build();

        // And
        final Content contentToBeMap = ContentBuilder.contentBuilder().build();

        // When
        final Content result = contentResourceAssembler.toDomain(contentResource, contentToBeMap);

        // Then
        assertThat(result).is(validContent(content, contentStatus, result));
    }

    private Condition<Content> validContent(final String content, final ContentStatus contentStatus, final Content result) {
        return new Condition<Content>() {
            @Override
            public boolean matches(Content value) {
                return result.getContent().equals(content) &&
                        result.getStatus().equals(contentStatus);
            }
        };
    }

}