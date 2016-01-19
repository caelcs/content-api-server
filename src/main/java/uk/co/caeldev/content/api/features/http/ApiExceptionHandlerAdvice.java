package uk.co.caeldev.content.api.features.http;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import uk.co.caeldev.content.api.features.content.ContentForbiddenException;
import uk.co.caeldev.content.api.features.content.ContentNotFoundException;
import uk.co.caeldev.content.api.features.publisher.PublisherNotFoundException;

@ControllerAdvice(annotations = RestController.class)
public class ApiExceptionHandlerAdvice {

    private final static Logger LOGGER = LoggerFactory.getLogger(ApiExceptionHandlerAdvice.class);


    @ExceptionHandler({ContentNotFoundException.class, PublisherNotFoundException.class})
    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ResponseBody
    public String handleContentNotFound(RuntimeException ex) {
        LOGGER.warn("Error: " + ex.getMessage());
        return ex.getMessage();
    }

    @ExceptionHandler({ContentForbiddenException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    @ResponseBody
    public String handleContentForbidden(RuntimeException ex) {
        LOGGER.warn("Error: " + ex.getMessage());
        return ex.getMessage();
    }
}
