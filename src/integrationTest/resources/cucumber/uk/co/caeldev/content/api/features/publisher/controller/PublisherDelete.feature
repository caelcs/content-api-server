Feature: Publisher
  Scenario Outline: As a consumer, I should be able to delete an existing publisher, including success and failure scenarios
    Given an existing publisher with username <username>
    And credential details have been provided using username <credential_username>
    When delete publisher
    Then the Publisher Delete response is <status_code>

    Examples:
    | credential_username | username   | status_code |
    | randomUser          | randomUser | 204         |