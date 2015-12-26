package uk.co.caeldev.content.api.features;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ApplicationTestConfiguration.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest("server.port:8888")
@ActiveProfiles("test")
public class BaseControllerConfiguration {

    @Value("${local.server.port}")
    protected int port;

    @Value("${server.contextPath}")
    protected String basePath;

    protected ObjectMapper jsonMapper = new ObjectMapper();
}
