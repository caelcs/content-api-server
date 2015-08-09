Feature: Content
  Scenario Outline: Should Publish a valid content resource successfully.
    Given a publisher
    And valid credentials to use the API
    And "<content>" as new content to be published
    When publish new content
    Then the response is <status_code>

  Examples:
  | content                   | status_code |
  | http://www.google.com     | 201         |