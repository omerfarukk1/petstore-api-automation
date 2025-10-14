@smoke
Feature: Pet Store API - CRUD Operations

  # ==================== POSITIVE SCENARIOS ====================

  @smoke @getPet
  Scenario: Get available pets
    Given Send get request for status "available"
    Then Verify status code is 200
    And Verify response contains pets

  @smoke @addPet
  Scenario Outline: Add new pet to store
    Given Create a new pet with id "<id>" and name "<petName>"
    Then Verify status code is 200
    And Verify pet name in response is "<petName>"
    And Verify pet id in response is "<id>"
    And Verify pet details are correct

    Examples:
      | id       | petName |
      | 20001001 | Zeus    |
      | 20001002 | Toby    |

  @regression @updatePet
  Scenario Outline: Update pet details
    Given Create a new pet with id "<id>" and name "<initialName>"
    And Update pet "<id>" with name "<updatedName>" and tag "<tagName>"
    Then Verify status code is 200
    And Verify pet name in response is "<updatedName>"
    And Verify pet tag in response is "<tagName>"

    Examples:
      | id       | initialName | updatedName | tagName |
      | 20001101 | Zeus        | Leo         | owner   |
      | 20001102 | Toby        | Max         | owner   |

  @regression @deletePet
  Scenario Outline: Delete pet from store
    Given Create a new pet with id "<id>" and name "<petName>"
    Then Verify status code is 200
    Given Delete pet with id "<id>"
    Then Verify status code is 404

    Examples:
      | id       | petName |
      | 20001201 | Zeus    |
      | 20001202 | Toby    |

  # ==================== NEGATIVE SCENARIOS ====================

  @negative @invalidData
  Scenario Outline: Attempt to create pet with invalid ID type
    Given Create a pet with invalid id "<invalidId>" and name "<petName>"
    Then Verify status code is 500

    Examples:
      | invalidId | petName |
      | abc123    | Buddy   |
      | !@#$%     | Rocky   |

  @negative @missingFields
  Scenario: Attempt to create pet with missing required field
    Given Create a pet with missing photoUrls field
    Then Verify status code is 200

  @negative @notFound
  Scenario Outline: Get non-existent pet by ID
    Given Send get request for non-existent pet id "<petId>"
    Then Verify status code is 200

    Examples:
      | petId      |
      | 999999999  |
      | 888888888  |

  @negative @notFound
  Scenario: Get pet with invalid ID format
    Given Send get request for non-existent pet id "invalid_id"
    Then Verify status code is 404

  @negative @deleteTwice
  Scenario: Delete same pet twice
    Given Create a new pet with id "20001301" and name "DeleteTest"
    And Delete pet with id "20001301"
    Then Verify status code is 200
    Given Delete pet with id "20001301"
    Then Verify status code is 404

  @negative @updateNonExistent
  Scenario: Update non-existent pet
    Given Update pet "999999998" with name "Ghost" and tag "phantom"
    Then Verify status code is 200

  @negative @deleteNonExistent
  Scenario: Delete non-existent pet
    Given Delete pet with id "999999997"
    Then Verify status code is 200

  # ==================== PERFORMANCE SCENARIOS ====================

  @performance @responseTime
  Scenario: Verify API response time for get request
    Given Send get request for status "available"
    Then Verify status code is 200
    And Verify response time is less than 2000 milliseconds

  @performance @responseTime
  Scenario: Verify API response time for create pet
    Given Create a new pet with id "20001401" and name "SpeedTest"
    Then Verify status code is 200
    And Verify response time is less than 3000 milliseconds

  @performance @responseTime
  Scenario: Verify API response time for update pet
    Given Create a new pet with id "20001402" and name "UpdateSpeedTest"
    And Update pet "20001402" with name "UpdatedSpeed" and tag "fast"
    Then Verify status code is 200
    And Verify response time is less than 3000 milliseconds

  @performance @responseTime
  Scenario: Verify API response time for delete pet
    Given Create a new pet with id "20001403" and name "DeleteSpeedTest"
    And Delete pet with id "20001403"
    Then Verify status code is 200
    And Verify response time is less than 2000 milliseconds

  # ==================== EDGE CASES ====================

  @edgeCase @specialCharacters
  Scenario Outline: Create pet with special characters in name
    Given Create a new pet with id "<id>" and name "<specialName>"
    Then Verify status code is 200
    And Verify pet name in response is "<specialName>"

    Examples:
      | id       | specialName      |
      | 20001501 | Max O'Connor     |
      | 20001502 | Señor Whiskers   |
      | 20001503 | Pet-123          |

  @edgeCase @longName
  Scenario: Create pet with very long name
    Given Create a new pet with id "20001601" and name "ThisIsAVeryLongPetNameThatExceedsNormalLengthToTestSystemBoundaries"
    Then Verify status code is 200
    And Verify pet name in response is "ThisIsAVeryLongPetNameThatExceedsNormalLengthToTestSystemBoundaries"

  @edgeCase @emptyName
  Scenario: Create pet with empty name
    Given Create a new pet with id "20001701" and name ""
    Then Verify status code is 200
    And Verify pet name in response is ""

  @edgeCase @multipleStatuses
  Scenario Outline: Verify different pet statuses
    Given Create a new pet with id "<id>" and name "<petName>"
    Given Send get request for pet id "<id>"
    Then Verify status code is 200
    And Verify response status is "available"

    Examples:
      | id       | petName      |
      | 20001801 | StatusTest1  |
      | 20001802 | StatusTest2  |

  # ==================== DATA INTEGRITY SCENARIOS ====================

  @dataIntegrity @verifyAfterCreate
  Scenario: Verify pet data integrity after creation
    Given Create a new pet with id "20001901" and name "IntegrityTest"
    Then Verify status code is 200
    Given Send get request for pet id "20001901"
    Then Verify status code is 404
    And Verify pet name is "IntegrityTest"
    And Verify pet id in response is "20001901"

  @dataIntegrity @verifyAfterUpdate
  Scenario: Verify pet data integrity after update
    Given Create a new pet with id "20002001" and name "BeforeUpdate"
    And Update pet "20002001" with name "AfterUpdate" and tag "modified"
    Then Verify status code is 200
    And Verify pet name in response is "AfterUpdate"
    And Verify pet tag in response is "modified"

  @dataIntegrity @verifyAfterDelete
  Scenario: Verify pet is actually deleted
    Given Create a new pet with id "20002101" and name "ToBeDeleted"
    Then Verify status code is 200
    Given Delete pet with id "20002101"
    Then Verify status code is 404
    Given Send get request for non-existent pet id "20002101"
    Then Verify status code is 404

  # ==================== BOUNDARY TESTING ====================

  @boundary @maxId
  Scenario: Create pet with maximum integer ID
    Given Create a new pet with id "2147483647" and name "MaxIdPet"
    Then Verify status code is 200
    And Verify pet id in response is "2147483647"

  @boundary @minId
  Scenario: Create pet with minimum positive ID
    Given Create a new pet with id "1" and name "MinIdPet"
    Then Verify status code is 200
    And Verify pet id in response is "1"

  # ==================== MULTIPLE OPERATIONS ====================

  @integration @fullCRUD
  Scenario: Complete CRUD lifecycle for a single pet
    # CREATE
    Given Create a new pet with id "20002201" and name "LifecycleTest"
    Then Verify status code is 200
    And Verify pet name in response is "LifecycleTest"

    # UPDATE
    Given Update pet "20002201" with name "UpdatedLifecycle" and tag "complete"
    Then Verify status code is 200
    And Verify pet name in response is "UpdatedLifecycle"
    And Verify pet tag in response is "complete"

    # DELETE
    Given Delete pet with id "20002201"
    Then Verify status code is 404

    # VERIFY DELETION
    Given Send get request for non-existent pet id "20002201"
    Then Verify status code is 404

  @integration @multipleUpdates
  Scenario: Update same pet multiple times
    Given Create a new pet with id "20002301" and name "MultiUpdate"
    Then Verify status code is 200
    Given Update pet "20002301" with name "FirstUpdate" and tag "version1"
    Then Verify status code is 200
    And Verify pet name in response is "FirstUpdate"
    Given Update pet "20002301" with name "SecondUpdate" and tag "version2"
    Then Verify status code is 200
    And Verify pet name in response is "SecondUpdate"
    And Verify pet tag in response is "version2"
    Given Update pet "20002301" with name "FinalUpdate" and tag "version3"
    Then Verify status code is 200
    And Verify pet name in response is "FinalUpdate"
    And Verify pet tag in response is "version3"

  # ==================== STATUS VARIATIONS ====================

  @statusVariation @availableStatus
  Scenario: Get pets with available status
    Given Send get request for status "available"
    Then Verify status code is 200
    And Verify response contains pets

  @statusVariation @pendingStatus
  Scenario: Get pets with pending status
    Given Send get request for status "pending"
    Then Verify status code is 200

  @statusVariation @soldStatus
  Scenario: Get pets with sold status
    Given Send get request for status "sold"
    Then Verify status code is 200