package uk.co.caeldev.content.api.features;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.boot.test.WebIntegrationTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(classes = ApplicationTest.class, loader = SpringApplicationContextLoader.class)
@WebIntegrationTest("server.port:0")
@ActiveProfiles("test")
public class BaseControllerConfiguration {

    @Value("${local.server.port}")
    protected int port;

    @Value("${server.contextPath}")
    protected String basePath;
}
