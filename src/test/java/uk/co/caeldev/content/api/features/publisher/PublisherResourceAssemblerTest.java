package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherBuilder.publisherBuilder;
import static uk.co.caeldev.content.api.features.publisher.builders.PublisherResourceBuilder.publisherResourceBuilder;

public class PublisherResourceAssemblerTest {

    private PublisherResourceAssembler publisherResourceAssembler;

    @Before
    public void testee() throws Exception {
        publisherResourceAssembler = new PublisherResourceAssembler();
    }

    @Test
    public void shouldConvertPublisherToPublisherResource() throws Exception {
        //Given
        final Publisher publisher = publisherBuilder().build();

        //When
        final PublisherResource resource = publisherResourceAssembler.toResource(publisher);

        //Then
        assertThat(resource.getPublisherUUID()).isEqualTo(publisher.getPublisherUUID());
        assertThat(resource.getStatus()).isEqualTo(publisher.getStatus());
        assertThat(resource.getUsername()).isEqualTo(publisher.getUsername());
        assertThat(resource.getFirstName()).isEqualTo(publisher.getFirstName());
        assertThat(resource.getLastName()).isEqualTo(publisher.getLastName());
        assertThat(resource.getCreationTime()).isEqualTo(publisher.getCreationTime());
    }

    @Test
    public void shouldConvertPublisherResourceToPublisher() throws Exception {
        //Given
        final PublisherResource publisherResource = publisherResourceBuilder().build();
        final Publisher publisher = publisherBuilder().build();

        //When
        final Publisher result = publisherResourceAssembler.toDomain(publisherResource, publisher);

        //Then
        assertThat(result.getPublisherUUID()).isEqualTo(publisher.getPublisherUUID());
        assertThat(result.getStatus()).isEqualTo(publisherResource.getStatus());
        assertThat(result.getUsername()).isEqualTo(publisher.getUsername());
        assertThat(result.getFirstName()).isEqualTo(publisher.getFirstName());
        assertThat(result.getLastName()).isEqualTo(publisher.getLastName());
        assertThat(result.getCreationTime()).isEqualTo(publisher.getCreationTime());
    }

}