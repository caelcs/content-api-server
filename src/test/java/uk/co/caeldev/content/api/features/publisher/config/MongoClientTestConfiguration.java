package uk.co.caeldev.content.api.features.publisher.config;

import com.github.fakemongo.Fongo;
import com.mongodb.MongoClient;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import uk.co.caeldev.content.api.config.MongoSettings;

import java.io.IOException;

@Configuration
@Profile("test")
@EnableConfigurationProperties(MongoSettings.class)
public class MongoClientTestConfiguration {

    @Bean
    public MongoClient mongoClient(MongoSettings mongoSettings) throws IOException {
        Fongo fongo = new Fongo(mongoSettings.getDatabase());
        return fongo.getMongo();
    }

}
