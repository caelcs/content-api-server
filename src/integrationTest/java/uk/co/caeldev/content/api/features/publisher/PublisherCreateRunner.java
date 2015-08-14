package uk.co.caeldev.content.api.features.publisher;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/content",
                "json:target/cucumber/content/cucumber.json",
                "junit:target/cucumber/content/cucumber.xml"},
        features = "src/integrationTest/resources/cucumber/uk/co/caeldev/content/api/features/publisher",
        glue = {"uk.co.caeldev.content.api.features.publisher.create"})
public class PublisherCreateRunner {
}
