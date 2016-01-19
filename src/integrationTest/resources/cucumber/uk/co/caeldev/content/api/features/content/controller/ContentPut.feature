Feature: Content

  Scenario Outline: As a consumer, I should be able to get content by UUID that belong to a particular publisher.
    Given a publisher with id <publisher_id>, username <username> and publisher uuid <publisher_uuid>
    And credential details have been provided using username <credential_username>
    And the following persisted content associated to a publisher
      | contentUUID                          | publisherId                          | content           | status |
      | d72b4c53-8993-4a5f-acc2-629456e81c6d | f5c40191-22ec-4d34-9d8d-2cb9732635fc | http://google.com | UNREAD |
    When get a content by UUID <content_uuid> and publisher UUID <publisher_uuid>
    And update status by UUID <content_uuid> and publisher UUID <publisher_uuid> to <new_content_status>
    Then the content put response is <status_code>

    Examples:
      | status_code | credential_username | username | publisher_uuid                       | publisher_id                         | content_uuid                         | new_content_status |
      | 200         | usertest            | usertest | 6cf1baaf-56fe-4e3b-844d-c73782d58917 | f5c40191-22ec-4d34-9d8d-2cb9732635fc | d72b4c53-8993-4a5f-acc2-629456e81c6d | READ               |


