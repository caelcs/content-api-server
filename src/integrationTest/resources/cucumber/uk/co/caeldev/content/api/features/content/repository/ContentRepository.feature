Feature: Content Repository
  Scenario: Expecting to test Content DAO layer using a Fongo.

    Given a random new content
    When save the content
    Then content should be persisted