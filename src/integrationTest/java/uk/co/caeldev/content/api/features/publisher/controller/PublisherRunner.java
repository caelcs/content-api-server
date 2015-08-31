package uk.co.caeldev.content.api.features.publisher.controller;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/publisher/controller",
                "json:target/cucumber/publisher/controller/cucumber.json",
                "junit:target/cucumber/publisher/controller/cucumber.xml"},
        features = "src/integrationTest/resources/cucumber/uk/co/caeldev/content/api/features/publisher/controller",
        glue = {"uk.co.caeldev.content.api.features.publisher.controller",
                "uk.co.caeldev.content.api.features.common"})
public class PublisherRunner {
}
