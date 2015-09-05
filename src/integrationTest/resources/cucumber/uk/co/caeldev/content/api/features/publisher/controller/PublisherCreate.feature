Feature: Publisher
  Scenario Outline: As a consumer, I should be able to test the creation of a new publisher when username has not been used, including success and failure scenarios
    Given a username <username>
    And credential details have been provided with user role and using username <credential_username>
    When create publisher
    Then the publisher creation response is <status_code>

    Examples:
    | credential_username | username    | status_code |
    | exitingUser         | randomUser  | 201         |

  Scenario Outline: As a consumer, I should be able to test the creation of a new publisher when username has been used, including success and failure scenarios
    Given an existing publisher with username <username>
    And a new Publisher with username already taken
    And credential details have been provided with user role and using username <credential_username>
    When create publisher
    Then the publisher creation response is <status_code>

    Examples:
    | credential_username | username    | status_code |
    | randomUser          | randomUser  | 400         |