package uk.co.caeldev.content.api.features.content.repository;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/content/repository",
                "json:target/cucumber/content/repository/cucumber.json",
                "junit:target/cucumber/content/repository/cucumber.xml"},
        features = "src/integrationTest/resources/cucumber/uk/co/caeldev/content/api/features/content/repository",
        glue = {"uk.co.caeldev.content.api.features.content.repository"})
public class ContentRepositoryRunner {
}
