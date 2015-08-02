package uk.co.caeldev.content.api.features.publisher;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class PublisherControllerTest {

    private PublisherController publisherController;

    @Before
    public void testee() throws Exception {
        publisherController = new PublisherController();
    }

    @Test
    public void shouldCreatePublisher() throws Exception {
        //Given

        //When
        publisherController.create();

        //Then
    }
}
