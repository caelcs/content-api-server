Feature: Publisher
  Scenario Outline: As a consumer, I should be able to test get a publisher.
    Given an existing group of publishers
    | id                                    |username   | publisherUUID                         | status | creationTime |
    | ed2039a9-248f-47e7-b2a0-ae0669fa35ef  |testuser1  | 85a31d86-822a-4903-8ef4-17b88bc86f4b  | ACTIVE | 2005-03-25   |
    And credential details have been provided with user role and using username <credential_username>
    When get a publisher by publisherUUID <publisherUUID>
    Then the publisher get response is <status_code>

    Examples:
    | credential_username | publisherUUID                        | status_code |
    | testuser1           | 85a31d86-822a-4903-8ef4-17b88bc86f4b | 200         |
    | testuser1           | 44bfd901-cded-4497-b5fc-50d72c58bee9 | 204         |