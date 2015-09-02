package uk.co.caeldev.content.api.features;

import org.springframework.boot.test.SpringApplicationContextLoader;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import uk.co.caeldev.content.api.features.config.RepositoryTestConfiguration;

@ContextConfiguration(classes = {RepositoryTestConfiguration.class}, loader = SpringApplicationContextLoader.class)
@ActiveProfiles("test")
public class BaseRepositoryConfiguration {
}
