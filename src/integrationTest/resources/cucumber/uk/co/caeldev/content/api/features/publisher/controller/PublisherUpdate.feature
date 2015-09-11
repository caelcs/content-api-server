Feature: Publisher
  Scenario Outline: As a consumer, I should be able to test the update of an existing publisher.
    Given an existing group of publishers
    | id                                    |username   | publisherUUID                         | status |
    | ed2039a9-248f-47e7-b2a0-ae0669fa35ef  |testuser1  | 85a31d86-822a-4903-8ef4-17b88bc86f4b  | ACTIVE |
    And credential details have been provided with user role and using username <credential_username>
    When update publisher by username <username> and publisherUUID <publisherUUID> with the new status <new_status>
    Then the publisher update response is <status_code> and new status is <new_status>

    Examples:
    | credential_username | publisherUUID                        |username    | new_status | status_code |
    | testuser1           | 85a31d86-822a-4903-8ef4-17b88bc86f4b | testuser1  | INACTIVE  | 200          |