package uk.co.caeldev.content.api.features;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.co.caeldev.content.api.config.MongoConfiguration;
import uk.co.caeldev.content.api.config.MongoSettings;
import uk.co.caeldev.content.api.features.publisher.config.MongoClientTestConfiguration;

@EnableAutoConfiguration
@ComponentScan(basePackages = {"uk.co.caeldev.spring.mvc", "uk.co.caeldev.content.api"})
@EnableMongoRepositories(basePackages = {"uk.co.caeldev"})
@Import({MongoClientTestConfiguration.class, MongoConfiguration.class, MongoSettings.class})
@Configuration
public class ApplicationTest {
}
