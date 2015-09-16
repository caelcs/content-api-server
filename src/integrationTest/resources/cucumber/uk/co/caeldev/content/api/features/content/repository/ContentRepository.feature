Feature: Content Repository
  Scenario: Expecting to test Content DAO layer using a Fongo.

    Given a random new content
    When save the content
    Then content should be persisted

  Scenario Outline: Expecting to find a content by uuid

    Given a persisted publisher
      | id                                   | publisherUUID                         | username       | status  |
      | f5c40191-22ec-4d34-9d8d-2cb9732635fc | 2a4b9066-1ef2-405c-9259-a5b846b64c4c  | randomUsername | ACTIVE  |

    And the following persisted content associated to a publisher
      | contentUUID                           | publisherId                           | content           | status |
      | d72b4c53-8993-4a5f-acc2-629456e81c6d  | f5c40191-22ec-4d34-9d8d-2cb9732635fc  | http://google.com | UNREAD |
      | 86e0a3f8-9871-41ab-a30a-a3c57f6032a1  | 4890775a-f0e7-4f22-b482-18cd75de7221  | http://google.com | UNREAD |
      | d244fce6-54f6-47e4-8eae-12150ae1791e  | 13163bf2-0c9b-4764-b8f6-f752a77cb131  | http://google.com | UNREAD |

    When find content by uuid <expected_content_uuid>
    Then the numbers of content should be <expected_count_content> and content uuid should be <expected_content_uuid>

  Examples:
    |expected_content_uuid                | expected_count_content |
    |d72b4c53-8993-4a5f-acc2-629456e81c6d | 1                      |
    |b1b28f59-6cc4-4569-824e-c06c8ff71754 | 0                      |

  Scenario Outline: Expecting to find all content by status paginated

    Given a persisted publisher
      | id                                   | publisherUUID                         | username       | status  |
      | f5c40191-22ec-4d34-9d8d-2cb9732635fc | 2a4b9066-1ef2-405c-9259-a5b846b64c4c  | randomUsername | ACTIVE  |

    And the following persisted content associated to a publisher
      | contentUUID                           | publisherId                           | content           | status |
      | d72b4c53-8993-4a5f-acc2-629456e81c6d  | f5c40191-22ec-4d34-9d8d-2cb9732635fc  | http://google.com | UNREAD |
      | 86e0a3f8-9871-41ab-a30a-a3c57f6032a1  | f5c40191-22ec-4d34-9d8d-2cb9732635fc  | http://google.com | UNREAD |
      | d244fce6-54f6-47e4-8eae-12150ae1791e  | f5c40191-22ec-4d34-9d8d-2cb9732635fc  | http://google.com | UNREAD |

    When find all content by status <status> paginated with page size <page_size> and publisherId <publisher_id>
    Then the number of pages is <expected_number_pages>
    And each page has size of <page_size>
    And publisher id is <publisher_id> for all content

    Examples:
      | page_size | status  | expected_number_pages | publisher_id                         |
      | 1         | UNREAD  | 3                     | f5c40191-22ec-4d34-9d8d-2cb9732635fc |
