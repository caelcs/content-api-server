Feature: Content
  Scenario Outline: Should be able to test Content API Endpoint, including success and failure scenarios
    Given a publisher
    And credentials validation is <are_credentials_valid>
    And "<content>" as new content to be published
    When publish new content
    Then the response is <status_code>

  Examples:
  | content                   | status_code | are_credentials_valid |
  | http://www.google.com     | 201         | true                  |
  |                           | 400         | true                  |
  | http://www.google.com     | 403         | false                 |
  |                           | 403         | false                 |