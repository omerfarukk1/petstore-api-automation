# PetStore API Test Plan

## 1. Test Scope

### In Scope
- Pet endpoints (`/pet`) - all CRUD operations
- GET `/pet/findByStatus` - Retrieve pets by status
- POST `/pet` - Add new pet to store
- GET `/pet/{id}` - Retrieve pet by ID
- PUT `/pet` - Update existing pet
- DELETE `/pet/{id}` - Remove pet from store

### Out of Scope
- Store endpoints (`/store/order`, `/store/inventory`)
- User endpoints (`/user`, `/user/login`)
- File upload functionality (`POST /pet/{id}/uploadImage`)

## 2. Test Objectives

- Verify all CRUD operations work correctly for pet resources
- Validate API returns correct HTTP status codes
- Ensure response data matches request data
- Test error handling for invalid inputs (negative testing)
- Verify API response times are acceptable (< 3 seconds)
- Confirm data persistence across operations

## 3. Test Approach

### Framework & Tools
- **Test Type**: API Automation Testing
- **Approach**: Behavior-Driven Development (BDD)
- **Framework**: Cucumber 7.30.0 with JUnit 4.13.2
- **Language**: Java 17
- **API Client**: REST Assured 5.5.6
- **Build Tool**: Maven 3.x
- **Reporting**: Cucumber HTML Reports 5.7.0
- **Execution**: Command-line and CI/CD integration

### Test Organization
- Feature files written in Gherkin syntax
- Step definitions implement test logic
- Service layer for API operations (PetServices)
- Reusable request specifications (PetReqSpec)
- Centralized configuration management (ConfigurationReader)
- Data factory pattern for test data (DataFactory_PetStore)
- Hooks for setup and cleanup

## 4. Test Scenarios

### 4.1 Pet Retrieval (GET /pet/findByStatus)
**Priority: High** | **Tag: @smoke @getPet**

| Scenario | Description | Expected Result |
|----------|-------------|-----------------|
| Get available pets | Request pets with status "available" | Status 200, list of available pets returned |
| Verify response contains pets | Validate response has pet data | List is not empty |

### 4.2 Pet Creation (POST /pet)
**Priority: High** | **Tag: @smoke @addPet**

| Scenario | Description | Expected Result |
|----------|-------------|-----------------|
| Create pet with ID 20001001 | Add new pet "Zeus" | Status 200, pet created with correct details |
| Create pet with ID 20001002 | Add new pet "Toby" | Status 200, pet created with correct details |
| Verify pet name in response | Check name matches request | Pet name is correct |
| Verify pet ID in response | Check ID matches request | Pet ID is correct |

### 4.3 Pet Update (PUT /pet)
**Priority: Medium** | **Tag: @regression @updatePet**

| Scenario | Description | Expected Result |
|----------|-------------|-----------------|
| Update pet 20001101 | Change Zeus to Leo with tag "owner" | Status 200, name and tag updated in response |
| Update pet 20001102 | Change Toby to Max with tag "owner" | Status 200, name and tag updated in response |
| Verify updated data | Check response contains new values | Updated name and tag present |

### 4.4 Pet Deletion (DELETE /pet/{id})
**Priority: Medium** | **Tag: @regression @deletePet**

| Scenario | Description | Expected Result |
|----------|-------------|-----------------|
| Delete pet 20001201 | Remove pet Zeus | Status 200, pet deleted |
| Delete pet 20001202 | Remove pet Toby | Status 200, pet deleted |

### 4.5 Negative Testing (Optional)
**Priority: Low** | **Tag: @negativeTesting**

| Scenario | Description | Expected Result |
|----------|-------------|-----------------|
| Create pet with invalid data | Submit pet with invalid ID type | Status 400 or appropriate error |
| Retrieve non-existent pet | Request pet with ID 999999 | Status 404, error message |

## 5. Test Data

### Static Test Data
- **Pet IDs**: 20001001, 20001002, 20001101, 20001102, 20001201, 20001202
- **Pet Names**: Zeus, Toby, Leo, Max
- **Statuses**: available, pending, sold
- **Tags**: owner, friendly, trained
- **Category**: Dogs (default)

### Data Management Strategy
- Test data created at test runtime via API calls
- Cleanup performed after each scenario using Cucumber hooks
- Unique pet IDs (20000000+ range) to avoid conflicts on public API
- No dependency on pre-existing data

## 6. Test Environment

### API Details
- **Base URL**: https://petstore.swagger.io/v2
- **Authentication**: None required (public API)
- **API Documentation**: https://petstore.swagger.io/
- **API Version**: v2

### Test Execution Environment
- **Java Version**: 17+
- **Maven Version**: 3.6+
- **Operating System**: Cross-platform (Windows, macOS, Linux)
- **IDE**: IntelliJ IDEA (or any Java IDE)

## 7. Entry Criteria

Before test execution can begin:
- [x] API is accessible and operational
- [x] Test framework is set up and configured
- [x] All dependencies installed (Java 17, Maven)
- [x] Feature files written and reviewed
- [x] Step definitions implemented
- [x] Test data strategy defined
- [x] Configuration file updated with correct URLs

## 8. Exit Criteria

Testing is considered complete when:
- [x] All smoke tests (@smoke) passing (100%)
- [x] At least 90% of regression tests passing
- [x] All CRUD operations tested and verified
- [x] HTML test report generated successfully
- [x] No critical or high-priority defects open
- [x] Test artifacts delivered (code, reports, documentation)

## 9. Test Execution Strategy

### Test Execution Phases

#### Phase 1: Smoke Testing
- **Tags**: @smoke
- **Duration**: ~5 minutes
- **Scenarios**: Critical path tests (Get, Add pets)
- **Frequency**: Every commit/build

#### Phase 2: Regression Testing
- **Tags**: @regression
- **Duration**: ~10 minutes
- **Scenarios**: Full test suite (Update, Delete pets)
- **Frequency**: Daily/before release

#### Phase 3: Negative Testing (Optional)
- **Tags**: @negativeTesting
- **Duration**: ~5 minutes
- **Scenarios**: Error handling tests
- **Frequency**: Before release

### Execution Commands

```bash
# Run all tests
mvn clean test

# Run smoke tests only
mvn test -Dtest=SmokeTestRunner

# Run regression tests
mvn test -Dtest=RegressionTestRunner

# Run specific tag
mvn test -Dcucumber.filter.tags="@getPet"

# Run with specific feature
mvn test -Dcucumber.filter.tags="@addPet"

# Generate and view reports
mvn clean test
open target/cucumber-reports/cucumber-html-reports/overview-features.html
```

## 10. Risks & Mitigation

| Risk | Impact | Probability | Mitigation |
|------|--------|-------------|------------|
| API is public - data may be modified by others | Medium | High | Use unique pet IDs (20000000+ range), implement cleanup hooks |
| API may be unavailable | High | Low | Retry mechanism, verify API status before tests |
| Test data conflicts | Medium | Medium | Clean up data after each test, use unique timestamps |
| API behavior differs from documentation | Medium | Low | Verify with actual API calls, document discrepancies |
| Slow API responses | Low | Medium | Set reasonable timeouts (3s), performance assertions |
| Updates/deletes may not persist | Medium | High | Verify in response body, not subsequent GET requests |

## 11. Assumptions

- API is stable and matches Swagger documentation
- No authentication required for pet endpoints
- API supports standard HTTP methods (GET, POST, PUT, DELETE)
- API accepts and returns JSON format
- Test data can be freely created and deleted
- No rate limiting on API requests
- Internet connectivity is available during test execution
- Public API may not guarantee data persistence

## 12. Test Deliverables

### Documentation
- [x] Test Plan (this document)
- [x] README with setup instructions
- [x] Feature files with test scenarios

### Code Artifacts
- [x] Step definitions (`PetStoreStepDef.java`)
- [x] Service layer (`PetServices.java`)
- [x] Request specifications (`PetReqSpec.java`, `CommonRequestSpec.java`)
- [x] Utility classes (`ApiGenericFunctions.java`, `CommonRestCRUD.java`)
- [x] Configuration management (`ConfigurationReader.java`, `configuration.properties`)
- [x] Data factory (`DataFactory_PetStore.java`)
- [x] Test runners (`CukesRunner.java`, `SmokeTestRunner.java`, `RegressionTestRunner.java`)
- [x] Hooks for setup/cleanup (`Hooks.java`)

### Reports
- [x] HTML test execution report
- [x] JSON report for CI/CD integration
- [x] Rerun file for failed scenarios

## 13. Project Structure

```
PetStoreApiTest/
├── src/test/
│   ├── java/petStore/
│   │   ├── runner/
│   │   │   ├── CukesRunner.java
│   │   │   ├── SmokeTestRunner.java
│   │   │   └── RegressionTestRunner.java
│   │   ├── stepDef/
│   │   │   └── PetStoreStepDef.java
│   │   ├── hooks/
│   │   │   └── Hooks.java
│   │   ├── services/
│   │   │   └── PetServices.java
│   │   ├── specs/
│   │   │   ├── CommonRequestSpec.java
│   │   │   └── PetReqSpec.java
│   │   ├── common/
│   │   │   ├── ApiGenericFunctions.java
│   │   │   ├── CommonRestCRUD.java
│   │   │   └── ConfigurationReader.java
│   │   └── dataFactory/
│   │       └── DataFactory_PetStore.java
│   └── resources/
│       ├── features/
│       │   └── petServices.feature
│       └── configuration.properties
├── pom.xml
├── README.md
├── TEST_PLAN.md
└── .gitignore
```

## 14. Schedule

| Phase | Duration | Owner |
|-------|----------|-------|
| Test planning | 0.5 days | QA Engineer |
| Framework setup | 1 day | QA Engineer |
| Test development | 2 days | QA Engineer |
| Test execution | 30 minutes | QA Engineer |
| Reporting & analysis | 0.5 days | QA Engineer |
| **Total** | **4.5 days** | |

## 15. Success Criteria

The project is successful if:
- ✅ All CRUD operations are automated and tested
- ✅ 100% of smoke tests pass consistently
- ✅ 90%+ of all tests pass
- ✅ Test execution time < 20 minutes
- ✅ Reports are clear and actionable
- ✅ Framework is maintainable and extensible
- ✅ Documentation is complete and accurate

## 16. Test Metrics

### Tracked Metrics
- **Total test scenarios**: 5 main scenarios (with variations via Scenario Outline)
- **Test pass rate**: Target > 95%
- **Test execution time**: < 10 minutes for full suite
- **Code coverage**: All pet endpoints covered (GET, POST, PUT, DELETE)
- **Average response time**: < 2000ms per API call
- **Defects found**: Tracked and reported in test execution reports

### Reporting
- HTML reports generated after each test run
- JSON reports available for CI/CD pipeline integration
- Pass/fail status visible in console output
- Failed scenarios logged in rerun.txt for easy retry

## 17. Dependencies

### Software Dependencies
- Java Development Kit (JDK) 17+
- Apache Maven 3.6+
- Internet connection (for API access)

### Maven Dependencies
- Selenium WebDriver 4.36.0
- Cucumber Java 7.30.0
- Cucumber JUnit 7.30.0
- REST Assured 5.5.6
- GSON 2.13.2
- JUnit 4.13.2
- Maven Cucumber Reporting 5.7.0

## 18. Known Limitations

1. **Public API Constraints**
    - API is shared - other users may modify test data
    - Updates and deletes may not persist reliably
    - Cannot guarantee exclusive access to test IDs

2. **Test Scope Limitations**
    - Only pet endpoints are tested
    - Store and User endpoints are out of scope
    - File upload functionality not tested

3. **Environment Limitations**
    - No control over API uptime
    - Cannot test authentication (API is public)
    - Cannot test rate limiting or performance under load

## 19. Future Enhancements

- Add contract testing with Pact
- Implement performance testing with JMeter integration
- Add API schema validation
- Expand coverage to Store and User endpoints
- Integrate with CI/CD pipeline (Jenkins, GitHub Actions)
- Add database validation (if backend access becomes available)
- Implement test data management with database cleanup
- Add visual regression testing for API documentation
- Create custom Cucumber reports with charts and graphs



**Document Version**: 1.0  
**Date**: October 13, 2025  
**Author**: QA Engineering Team  
**Status**: Approved  
**Last Updated**: October 13, 2025

---



**End of Test Plan**