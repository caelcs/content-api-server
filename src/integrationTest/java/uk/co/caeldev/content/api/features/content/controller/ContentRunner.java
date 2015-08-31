package uk.co.caeldev.content.api.features.content.controller;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "html:target/cucumber/content/controller",
                "json:target/cucumber/content/controller/cucumber.json",
                "junit:target/cucumber/content/controller/cucumber.xml"},
        features = "src/integrationTest/resources/cucumber/uk/co/caeldev/content/api/features/content/controller",
        glue = {"uk.co.caeldev.content.api.features.content.controller",
                "uk.co.caeldev.content.api.features.common"})
public class ContentRunner {
}
