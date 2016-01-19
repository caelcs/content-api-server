package uk.co.caeldev.content.api.features.checks;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import uk.co.caeldev.content.api.features.content.Content;
import uk.co.caeldev.content.api.features.content.ContentForbiddenException;
import uk.co.caeldev.content.api.features.content.ContentNotFoundException;
import uk.co.caeldev.content.api.features.publisher.Publisher;
import uk.co.caeldev.content.api.features.publisher.PublisherNotFoundException;

import java.util.Objects;

public class Preconditions {

    private final static Logger LOGGER = LoggerFactory.getLogger(Preconditions.class);


    public static void checkNullPublisher(Publisher publisher) {
        if (publisher == null) {
            LOGGER.warn("Error: PublisherNotFoundException");
            throw new PublisherNotFoundException();
        }
    }

    public static void checkNullContent(Content content) {
        if (content == null) {
            LOGGER.warn("Error: ContentNotFoundException");
            throw new ContentNotFoundException();
        }
    }

    public static void checkForbiddenContent(String contentPublisherId, String publisherId) {
        if (!Objects.equals(contentPublisherId, publisherId)) {
            LOGGER.warn("Error: ContentForbiddenException");
            throw new ContentForbiddenException();
        }
    }
}
