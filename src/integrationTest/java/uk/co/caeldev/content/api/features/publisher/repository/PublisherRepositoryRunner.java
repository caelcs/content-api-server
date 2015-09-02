package uk.co.caeldev.content.api.features.publisher.repository;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/publisher/repository",
                "json:target/cucumber/publisher/repository/cucumber.json",
                "junit:target/cucumber/publisher/repository/cucumber.xml"},
        features = "src/integrationTest/resources/cucumber/uk/co/caeldev/content/api/features/publisher/repository",
        glue = {"uk.co.caeldev.content.api.features.publisher.repository"})
public class PublisherRepositoryRunner {
}
