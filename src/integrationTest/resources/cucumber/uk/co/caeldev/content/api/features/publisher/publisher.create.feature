Feature: Publisher
  Scenario Outline: As a consumer, I should be able to test the creation of a new publisher when username has not been used, including success and failure scenarios
    Given a username "<username>"
    And with valid credentials
    When create publisher
    Then the response is <status_code>

    Examples:
    | username                  | status_code |
    | random                    | 201         |
    |                           | 400         |

  Scenario Outline: As a consumer, I should be able to test the creation of a new publisher when username has been used, including success and failure scenarios
    Given an existing publisher with username "<username>"
    And a new Publisher with the same username
    And with valid credentials
    When create publisher
    Then the response is <status_code>

    Examples:
      | username                  | status_code |
      | random                    | 400         |