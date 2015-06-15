package uk.co.caeldev.content.api.features.publisher.repository.config;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.*;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import uk.co.caeldev.content.api.config.*;
import uk.co.caeldev.content.api.features.publisher.config.MongoClientTestConfiguration;

@Configuration
@EnableAutoConfiguration
@ComponentScan(basePackages = {"uk.co.caeldev.content.api.config"},
        includeFilters = {
                @ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE,
                        value = {
                                MongoConfiguration.class
                        })
        })
@Profile("test")
@EnableMongoRepositories(basePackages = {"uk.co.caeldev.content.api.features.*.repository"})
@Import({MongoClientTestConfiguration.class, MongoConfiguration.class, MongoSettings.class})
public class TestRepositoryConfiguration {

}
