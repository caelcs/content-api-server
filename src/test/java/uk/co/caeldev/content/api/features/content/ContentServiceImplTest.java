package uk.co.caeldev.content.api.features.content;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import uk.co.caeldev.content.api.features.content.repository.ContentRepository;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static uk.co.caeldev.content.api.commons.ContentApiRDG.string;

@RunWith(MockitoJUnitRunner.class)
public class ContentServiceImplTest {

    @Mock
    private ContentRepository contentRepository;

    private ContentServiceImpl contentService;

    @Before
    public void testee() throws Exception {
        contentService = new ContentServiceImpl(contentRepository);
    }

    @Test
    public void shouldPublishContentForGivenPublisher() throws Exception {
        //Given
        final String content = string().next();
        final String id = UUID.randomUUID().toString();

        //And
        final ArgumentCaptor<Content> contentArgumentCaptor = ArgumentCaptor.forClass(Content.class);

        //When
        contentService.publish(content, id);

        //Then
        verify(contentRepository).save(contentArgumentCaptor.capture());
        final Content expectedResult = contentArgumentCaptor.getValue();

        assertThat(expectedResult.getContent()).isEqualTo(content);
        assertThat(expectedResult.getContentUUID()).isNotNull();
        assertThat(expectedResult.getStatus()).isEqualTo(ContentStatus.UNREAD);
        assertThat(expectedResult.getPublisherId()).isEqualTo(id);
    }
}