# Pet Store API Test Automation 🐾

A comprehensive API test automation framework for the Swagger PetStore API using Cucumber BDD, RestAssured, and Allure Reports.

## 📋 Table of Contents
- [Features](#features)
- [Tech Stack](#tech-stack)
- [Prerequisites](#prerequisites)
- [Installation](#installation)
- [Running Tests](#running-tests)
- [Allure Reports](#allure-reports)
- [Project Structure](#project-structure)
- [Test Coverage](#test-coverage)
- [Contributing](#contributing)

## ✨ Features

- ✅ **BDD Framework** with Cucumber
- ✅ **REST API Testing** with RestAssured
- ✅ **Beautiful Reports** with Allure
- ✅ **Comprehensive Test Coverage** - CRUD operations, negative tests, edge cases
- ✅ **Data-Driven Testing** with Scenario Outlines
- ✅ **Automatic Cleanup** with Hooks
- ✅ **CI/CD Ready**

## 🛠 Tech Stack

- **Java 17**
- **Maven** - Dependency Management
- **Cucumber 7.14.0** - BDD Framework
- **RestAssured 5.5.6** - API Testing
- **Allure 2.24.0** - Test Reporting
- **JUnit 4** - Test Runner

## 📦 Prerequisites

Before running this project, ensure you have:

- **Java JDK 17** or higher installed
- **Maven 3.6+** installed
- **Allure Command Line** installed (for viewing reports)

### Install Allure

**macOS:**
```bash
brew install allure
```

**Windows:**
```bash
scoop install allure
```

**Or download from:** https://github.com/allure-framework/allure2/releases

## 🚀 Installation

1. **Clone the repository:**
```bash
git clone https://github.com/YOUR_USERNAME/PetStoreApiTest.git
cd PetStoreApiTest
```

2. **Install dependencies:**
```bash
mvn clean install
```

## ▶️ Running Tests

### Run All Tests
```bash
mvn clean test
```

### Run Specific Tags
```bash
# Run only smoke tests
mvn test -Dcucumber.filter.tags="@smoke"

# Run regression tests
mvn test -Dcucumber.filter.tags="@regression"

# Run negative tests
mvn test -Dcucumber.filter.tags="@negative"

# Run performance tests
mvn test -Dcucumber.filter.tags="@performance"

# Exclude negative tests
mvn test -Dcucumber.filter.tags="not @negative"
```

## 📊 Allure Reports

### Generate and View Report
```bash
# Run tests and generate report
mvn clean test

# Open Allure report in browser
mvn allure:serve
```

### Generate Static Report
```bash
# Generate report
mvn allure:report

# Report will be at: target/allure-report/index.html
```

## 📁 Project Structure

```
PetStoreApiTest/
├── src/
│   ├── main/
│   │   └── java/
│   └── test/
│       ├── java/
│       │   └── petStore/
│       │       ├── common/          # Common utilities
│       │       ├── hooks/           # Cucumber hooks
│       │       ├── runner/          # Test runner
│       │       ├── services/        # API service layer
│       │       ├── specs/           # Request specifications
│       │       └── stepDef/         # Step definitions
│       └── resources/
│           ├── features/            # Cucumber feature files
│           └── config/              # Configuration files
├── target/
│   ├── allure-results/             # Allure test results
│   └── allure-report/              # Generated Allure report
├── pom.xml                         # Maven configuration
└── README.md
```

## 🧪 Test Coverage

### Test Categories

| Category | Description | Tag |
|----------|-------------|-----|
| **Smoke Tests** | Critical path tests | `@smoke` |
| **Regression Tests** | Full test suite | `@regression` |
| **Negative Tests** | Error handling & validation | `@negative` |
| **Performance Tests** | Response time validation | `@performance` |
| **Edge Cases** | Boundary & special scenarios | `@edgeCase` |
| **Data Integrity** | CRUD verification | `@dataIntegrity` |
| **Integration Tests** | End-to-end workflows | `@integration` |

### API Endpoints Covered

- ✅ `GET /pet/findByStatus` - Get pets by status
- ✅ `GET /pet/{petId}` - Get pet by ID
- ✅ `POST /pet` - Create new pet
- ✅ `PUT /pet` - Update existing pet
- ✅ `DELETE /pet/{petId}` - Delete pet

### Test Scenarios

- **Positive Tests:** CRUD operations, data validation
- **Negative Tests:** Invalid IDs, missing fields, non-existent resources
- **Edge Cases:** Special characters, boundary values, empty data
- **Performance:** Response time validation
- **Data Integrity:** Verification after create/update/delete

## 🎯 Sample Test Results

```
Tests run: 37
✅ Passed: 30
❌ Failed: 7
⏭️ Skipped: 0
⏱️ Duration: ~75s
```

## 🔧 Configuration

API configuration can be modified in:
- `src/test/resources/config/config.properties`

Default configuration:
- **Base URI:** `https://petstore.swagger.io/v2`
- **Request Timeout:** 30 seconds
- **Content Type:** `application/json`

## 🤝 Contributing

Contributions are welcome! Please follow these steps:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📝 Best Practices

- ✅ Follow BDD naming conventions
- ✅ Keep step definitions reusable
- ✅ Use meaningful scenario names
- ✅ Add tags for test organization
- ✅ Clean up test data in hooks
- ✅ Document complex test logic

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- [Swagger PetStore API](https://petstore.swagger.io/)
- [Cucumber](https://cucumber.io/)
- [RestAssured](https://rest-assured.io/)
- [Allure Framework](https://docs.qameta.io/allure/)

---

⭐ If you find this project useful, please consider giving it a star!