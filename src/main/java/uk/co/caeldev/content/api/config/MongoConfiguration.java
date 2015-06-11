package uk.co.caeldev.content.api.config;

import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.authentication.UserCredentials;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableConfigurationProperties(MongoSettings.class)
@EnableMongoRepositories(basePackages = {"uk.co.caeldev"})
public class MongoConfiguration {

    @Bean
    public MongoDbFactory mongoDbFactory(final MongoClient mongoClient, final MongoSettings mongoSettings) throws Exception {
        final UserCredentials userCredentials = new UserCredentials(mongoSettings.getUsername(), mongoSettings.getPassword());

        return new SimpleMongoDbFactory(mongoClient, mongoSettings.getDatabase(), userCredentials);
    }

    @Bean
    public MongoTemplate mongoTemplate(final MongoDbFactory mongoDbFactory) throws Exception {
        return new MongoTemplate(mongoDbFactory);
    }

}
