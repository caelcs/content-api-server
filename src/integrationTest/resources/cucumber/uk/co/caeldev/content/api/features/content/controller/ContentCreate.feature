Feature: Content
  Scenario Outline: As a consumer, I should be able to create content always providing credentials details
    that match with the publisher.

    Given a publisher with username <username>
    And credential details have been provided using username <credential_username>
    And a new content <content> to be published
    When publish new content
    Then the content creation response is <status_code>

  Examples:
  | content                   | status_code | credential_username | username |
  | http://www.google.com     | 201         | usertest            | usertest |
  | EMPTY                     | 400         | usertest            | usertest |
  | http://www.google.com     | 403         | fakeuser            | usertest |
  | EMPTY                     | 403         | fakeuser            | usertest |