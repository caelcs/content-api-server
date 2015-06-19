package uk.co.caeldev.content.api.config;

import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
@EnableConfigurationProperties(MongoSettings.class)
public class MongoClientConfiguration {

    @Bean
    public MongoClient mongoClient(MongoSettings mongoSettings) throws Exception {
        return new MongoClient(mongoSettings.getHost(), mongoSettings.getPort());
    }

}
