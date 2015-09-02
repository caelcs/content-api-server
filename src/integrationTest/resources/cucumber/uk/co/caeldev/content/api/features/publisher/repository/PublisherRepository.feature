Feature: Publisher Repository
  Scenario: Expecting to test Publisher DAO layer using a Fongo.

    Given a random new publisher
    When save the publisher
    Then publisher should be persisted

  Scenario Outline: Expecting to be able to find a existing publisher by publisher UUID.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to find the publisher by UUID <publisher_uuid>
    Then should return the persisted publisher

    Examples:
      | id                                   | publisher_uuid                        | username       | status |
      | 7749aa97-2e2f-4666-b396-475fd29c5da4 | 44d74b78-a235-45bf-9aa5-79b72e1531ad  | randomUsername | ACTIVE |

  Scenario Outline: Expecting to be able to find a existing publisher by username.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to find the publisher by username <username>
    Then should return the persisted publisher

    Examples:
      | id                                   | publisher_uuid                        | username         | status |
      | 48311714-6b16-425f-b766-7700a0622689 | 847e945f-6b46-4cff-91c4-a2efe878aa6c  | randomUsername  | ACTIVE |

  Scenario Outline: Expecting not to be able to find a publisher when username does not exist.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to find the publisher by username <non_expecting_username>
    Then should not return any publisher

    Examples:
      | id                                   | publisher_uuid                        | username       | status | non_expecting_username |
      | 06492a70-1e15-4f05-a1f0-6f001ceaff8f | a274acc8-e6a4-4d49-9601-dc6f1c090f80  | randomUsername | ACTIVE | non_expecting_username |

  Scenario Outline: Expecting to be able to find a publisher by publisher UUID and username.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to find the publisher by publisher UUID <publisher_uuid> and username <username>
    Then should return the persisted publisher

    Examples:
      | id                                   | publisher_uuid                        | username       | status |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE |

  Scenario Outline: Expecting not to be able to find a publisher by publisher UUID and username when some data is wrong.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to find the publisher by publisher UUID <expecting_publisher_uuid> and username <expecting_username>
    Then should not return any publisher

    Examples:
      | id                                   | publisher_uuid                        | username       | status  | expecting_username | expecting_publisher_uuid             |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE  | randomUsername     | 4c7030b1-c1f9-4a00-89f4-b423727f81c0 |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE  | wrong_username     | 67fd4e26-f81e-424e-95c6-0999a44bafc3 |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE  | wrong_username     | 4c7030b1-c1f9-4a00-89f4-b423727f81c0 |

  Scenario Outline: Expecting to be able to update publisher status.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to update publisher status by publisher UUID <publisher_uuid> to <new_status>
    Then should have the status <new_status>

    Examples:
      | id                                   | publisher_uuid                        | username       | status    | new_status  |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE    | INACTIVE    |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE    | DELETED     |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | INACTIVE  | ACTIVE      |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE    | ACTIVE      |

  Scenario Outline: Expecting not to be able to update publisher status when using an invalid publisherUUID.

    Given a persisted publisher with id <id> and publisher UUID <publisher_uuid> and username <username> and status <status>
    When try to update publisher status by publisher UUID <expecting_publisher_uuid> to <new_status>
    Then should not update any publisher

    Examples:
      | id                                   | publisher_uuid                        | username       | status    | new_status  | expecting_publisher_uuid              |
      | 0b9c66cb-74fd-433e-82fd-d07f5abd329a | 67fd4e26-f81e-424e-95c6-0999a44bafc3  | randomUsername | ACTIVE    | INACTIVE    | 27cf7c9c-ba8d-43cd-8020-309d3de693b5  |

