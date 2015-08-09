Feature: Content
  Scenario: Publish resource sucessfully.
    Given a publisher
    And right permissions
    And new content to be published
    When publish new content
    Then the content should be persisted and be valid