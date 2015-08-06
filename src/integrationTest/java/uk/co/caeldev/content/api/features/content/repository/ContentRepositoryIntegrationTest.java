package uk.co.caeldev.content.api.features.content.repository;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import uk.co.caeldev.content.api.features.AbstractRepositoryIntegrationTest;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentBuilder;

import static org.assertj.core.api.Assertions.assertThat;

public class ContentRepositoryIntegrationTest extends AbstractRepositoryIntegrationTest {

    @Autowired
    private ContentRepository contentRepository;

    @Test
    public void shouldSaveContent() throws Exception {
        //Given
        final Content content = ContentBuilder.contentBuilder().build();

        //When
        final Content result = contentRepository.save(content);

        //Then
        assertThat(result.getId()).isNotNull();
        assertThat(result.getId()).isNotEmpty();
        assertThat(result).isEqualTo(content);
    }

}