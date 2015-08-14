Feature: Publisher
  Scenario Outline: As a consumer, I should be able to delete an existing publisher, including success and failure scenarios
    Given an existing publisher with username "<username>"
    And with valid credentials
    When delete publisher
    Then the response is <status_code>

    Examples:
    | username                  | status_code |
    | random                    | 204         |