# Ulta Generative AI Testing — Project Audit Report

**Date:** October 8, 2026  
**Audit Type:** Read-Only Technical & Deliverable Audit  
**Scope:** Wipro Assignment — Generative AI-driven Testing Solution for Ulta.com (AC1–AC7)

---

## 1. PROJECT STRUCTURE

```text
ulta-ai-automation/
├── pom.xml                                      # Maven POM (Java 21, Selenium 4.27.0, Cucumber 7.20.1, TestNG 7.10.2, Allure 2.29.0)
├── testng.xml                                   # Empty suite file (0 bytes)
├── docs/
│   ├── Copilot_GenAI_Evidence.md                # Detailed GenAI/Copilot contribution record
│   ├── Copilot_GenAI_Evidence.tmp.md            # Duplicate backup of GenAI evidence
│   └── PROJECT_AUDIT.md                         # This audit report
├── test-artifacts/
│   ├── Test_Scenarios.xlsx                      # 34 approved scenarios across AC1–AC7
│   ├── Test_Cases_qTest.xlsx                    # 34 formal test cases (190 step rows) with qTest schema
│   └── Test_Cases_qTest_Automated_Release.xlsx  # Automated release version of qTest test cases
├── src/test/
│   ├── java/
│   │   ├── pages/                               # Page Object Model classes
│   │   │   ├── HomePage.java                    # Ulta homepage navigation & header verification
│   │   │   ├── SearchResultsPage.java           # Search execution & result grid interactions
│   │   │   ├── ProductPage.java                 # PDP interactions, variant selection, bag CTA, PDP recommendations
│   │   │   └── CartPage.java                    # Bag navigation, cart items verification, cart recommendations
│   │   ├── runners/
│   │   │   └── TestRunner.java                  # TestNG Cucumber runner (AbstractTestNGCucumberTests)
│   │   ├── stepdefinitions/                     # Cucumber step definition classes
│   │   │   ├── Hooks.java                       # WebDriver lifecycle management (@Before, @After)
│   │   │   ├── HomePageSteps.java               # Steps for AC1 (homepage, search bar, account icon)
│   │   │   ├── SearchSteps.java                 # Steps for AC2 (product searches)
│   │   │   ├── ProductSteps.java                # Steps for AC3, AC4, AC5 (add to bag, cart count, PDP recs)
│   │   │   ├── CartSteps.java                   # Steps for AC6 (cart recommendations)
│   │   │   └── BeautyCategorySteps.java         # Steps for AC7 (combined beauty category recommendations)
│   │   └── utils/                               # Analysis & driver utilities
│   │       ├── WebDriverProvider.java           # ThreadLocal ChromeDriver management
│   │       ├── RecommendationAnalyzer.java      # Keyword-based relevance analyzer (AC5, AC6)
│   │       └── BeautyCategoryAnalyzer.java      # Beauty-domain taxonomy classifier (AC7)
│   └── resources/
│       └── features/                            # Gherkin feature files
│           ├── homepage.feature                 # AC1: Ulta homepage validation
│           ├── search.feature                   # AC2: Product searches (Scenario Outline with 3 terms)
│           ├── product.feature                  # AC3, AC4, AC5: Add to bag, 3-item cart, PDP recommendations
│           ├── cart.feature                     # AC6: Cart recommendations relevance
│           └── beauty-category.feature          # AC7: Combined recommendation beauty category ratio
├── target/
│   ├── cucumber-reports/cucumber.html           # Cucumber HTML execution report
│   └── surefire-reports/                        # TestNG/Surefire execution XML & HTML reports
├── allure-results/                              # 342 raw JSON result/container files from TestNG run
└── allure-report/                               # Static HTML/JS Allure report directory
```

---

## 2. REQUIREMENT → IMPLEMENTATION TRACEABILITY

| AC | Acceptance Criterion | Feature & Scenario | Step Definition Class | Page Object / Utility | Automation Status | Gaps / Observations |
|---|---|---|---|---|---|---|
| **AC1** | Homepage & Navigation: verify homepage loads, search bar visible & enabled, account icon accessible | [homepage.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/homepage.feature)<br>`Scenario: Validate homepage essentials` | [HomePageSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/HomePageSteps.java) | [HomePage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/HomePage.java), [WebDriverProvider.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/WebDriverProvider.java) | **Automated (PASSED)** | None. Validated in recorded execution. |
| **AC2** | Product Searches: search for moisturizer, mascara, vitamin c serum and verify results | [search.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/search.feature)<br>`Scenario Outline: Search for beauty products` (3 examples) | [SearchSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/SearchSteps.java), [HomePageSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/HomePageSteps.java) | [SearchResultsPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/SearchResultsPage.java), [HomePage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/HomePage.java) | **Automated (PASSED)** | All 3 search terms passed in recorded execution. |
| **AC3** | Add Products to Bag: select product, handle variants, click Add to Bag, verify success | [product.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/product.feature)<br>`Scenario: Add a moisturizer to the bag` | [ProductSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/ProductSteps.java) | [ProductPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/ProductPage.java), [SearchResultsPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/SearchResultsPage.java) | **Automated (PASSED)** | Passed in recorded execution. |
| **AC4** | Verify 3 Products in Cart: add moisturizer, mascara, vitamin c serum and verify in cart | [product.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/product.feature)<br>`Scenario: Add three products and verify the cart` | [ProductSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/ProductSteps.java) | [CartPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/CartPage.java), [ProductPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/ProductPage.java) | **Automated (PASSED)** | Passed in recorded execution. |
| **AC5** | Product Recommendations >=30%: verify recommendations on PDP are >=30% relevant | [product.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/product.feature)<br>`Scenario Outline: Recommendations are relevant to the viewed product` (3 examples) | [ProductSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/ProductSteps.java) | [ProductPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/ProductPage.java), [RecommendationAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java) | **Automated (PASSED)** | All 3 product terms met >=30% relevance in latest execution. Carousel locator relies on live site `Similar items for you` header. |
| **AC6** | Cart Recommendations >=25%: verify recommendations in cart are >=25% relevant to cart items | [cart.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/cart.feature)<br>`Scenario: Validate cart recommendations are relevant to cart items` | [CartSteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/CartSteps.java) | [CartPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/CartPage.java), [RecommendationAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java) | **Automated (FAILED in test log)** | **Gap:** Failed with `TimeoutException` waiting for `//h2[normalize-space()='Beauty under $20']` on live Ulta bag page. Live site cart recommendations vary by user session and may not meet 25% keyword match. |
| **AC7** | Combined Recommendations >=20%: collect recs from PDPs + cart and verify >=20% beauty category | [beauty-category.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/beauty-category.feature)<br>`Scenario: Validate beauty category recommendations across products and cart` | [BeautyCategorySteps.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/BeautyCategorySteps.java) | [BeautyCategoryAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/BeautyCategoryAnalyzer.java), [CartPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/CartPage.java), [ProductPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/ProductPage.java) | **Automated (FAILED in test log)** | **Gap:** Failed with `TimeoutException` during product recommendation extraction actions across multi-page execution flow. |

---

## 3. TEST ARTIFACTS

| Artifact | File Location | Present in Project? | Details / Findings |
|---|---|---|---|
| **Test Scenarios Workbook** | [test-artifacts/Test_Scenarios.xlsx](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/test-artifacts/Test_Scenarios.xlsx) | **Yes** | 34 approved scenarios mapped to AC1–AC7 with Scenario IDs (`SC-001` to `SC-034`), Test Types (Positive, Negative, Boundary), and Priority. |
| **Test Cases qTest Workbook** | [test-artifacts/Test_Cases_qTest.xlsx](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/test-artifacts/Test_Cases_qTest.xlsx) | **Yes** | 34 formal test cases across 190 step rows following the standard 16-column qTest import schema (Test Case ID, Scenario ID, AC, Steps, Expected Results, Preconditions, Data, Status). |
| **Automated Release Workbook** | [test-artifacts/Test_Cases_qTest_Automated_Release.xlsx](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/test-artifacts/Test_Cases_qTest_Automated_Release.xlsx) | **Yes** | Derived qTest workbook containing all 34 test cases classified with Automation Status = "Automated". |
| **Execution-Scope Workbook** | `test-artifacts/Test_Cases_Automation_Execution_Scope.xlsx` | **No (Missing)** | File `Test_Cases_Automation_Execution_Scope.xlsx` does **not** exist in the workspace. |
| **Copilot / GenAI Evidence** | [docs/Copilot_GenAI_Evidence.md](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/docs/Copilot_GenAI_Evidence.md) | **Yes** | Comprehensive document recording GenAI contributions across lifecycle phases, limitations, human roles, and failure analyses. |
| **Audit Scripts** | [audit_test_cases.py](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/audit_test_cases.py), [build_test_cases.py](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/build_test_cases.py), [correct_automation_metadata.py](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/correct_automation_metadata.py) | **Yes** | Python validation scripts used to construct and verify 100% traceability between scenarios and qTest workbooks. |

---

## 4. AUTOMATION FLOW

The project executes tests via the following end-to-end flow:

```text
[ Gherkin Feature File ] (*.feature under src/test/resources/features)
         │
         ▼
[ TestRunner.java ] (Extends AbstractTestNGCucumberTests; triggers Cucumber scenarios)
         │
         ▼
[ Hooks.java ] (@Before starts ChromeDriver via WebDriverProvider; @After quits driver)
         │
         ▼
[ Step Definitions ] (HomePageSteps, SearchSteps, ProductSteps, CartSteps, BeautyCategorySteps)
         │
         ▼
[ Page Object Model ] (HomePage, SearchResultsPage, ProductPage, CartPage)
         │
         ▼
[ Selenium WebDriver & Explicit Waits ] (Interacts with live DOM on ulta.com)
         │
         ▼
[ Business-Rule Analyzers ] (RecommendationAnalyzer, BeautyCategoryAnalyzer calculate keyword relevance & category ratios)
         │
         ▼
[ TestNG Assertions ] (Assert.assertTrue on thresholds: >=30% for AC5, >=25% for AC6, >=20% for AC7)
         │
         ▼
[ Reporting Outputs ]
  ├── Cucumber HTML: target/cucumber-reports/cucumber.html
  ├── Surefire / TestNG XML: target/surefire-reports/testng-results.xml
  └── Allure Results / Report: allure-results/ (raw JSON) & allure-report/ (HTML)
```

---

## 5. ALLURE CONFIGURATION & REPORT STATE

- **Maven Dependency Present:** `io.qameta.allure:allure-testng:2.29.0` is declared in [pom.xml](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/pom.xml#L43-L46).
- **Missing Dependency:** `io.qameta.allure:allure-cucumber7-jvm` is **missing** from `pom.xml`.
- **Missing Runner Plugin:** [TestRunner.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/runners/TestRunner.java#L9) registers only `{"pretty", "html:target/cucumber-reports/cucumber.html"}`. The Allure Cucumber adapter `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` is **not configured** in `@CucumberOptions`.
- **Missing Maven Plugin:** `allure-maven` plugin is **not configured** in `pom.xml` build plugins.
- **allure-results Status:** `allure-results/` directory exists and contains 342 raw JSON result/container files generated by TestNG.
- **allure-report Status:** `allure-report/` directory exists with static HTML/JS/CSS assets.
- **Reason HTML Report Shows "Loading... / No Data":**
  1. **CORS / Local File Protocol Issue:** When `allure-report/index.html` is opened directly via `file:///` in a web browser, modern browser security prevents JavaScript (`app.js`) from fetching data files (`data/*.json`) via `fetch()`. Allure reports require a local web server (e.g., `allure open`, `npx http-server`, or `python -m http.server`) or generating with `allure generate --single-file`.
  2. **Cucumber Adapter Omission:** Because `allure-cucumber7-jvm` is missing, Allure only captures top-level TestNG `runScenario` calls rather than detailed Gherkin steps.

---

## 6. EXECUTION STATUS (BASED ON EXISTING LOGS)

*(Audit performed strictly from existing log and report files without running tests)*

- **Execution Engine:** Maven Surefire + TestNG + Cucumber BDD.
- **Existing Report Source:** [target/surefire-reports/testng-results.xml](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/target/surefire-reports/testng-results.xml) & [target/surefire-reports/runners.TestRunner.txt](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/target/surefire-reports/runners.TestRunner.txt).
- **Executed Scenarios:** 11 scenario runs recorded.
- **Passed:** **9**
  - AC1: `Validate homepage essentials` (Passed)
  - AC2: `Search for beauty products` — moisturizer (Passed)
  - AC2: `Search for beauty products` — mascara (Passed)
  - AC2: `Search for beauty products` — vitamin c serum (Passed)
  - AC3: `Add a moisturizer to the bag` (Passed)
  - AC4: `Add three products and verify the cart` (Passed)
  - AC5: `Recommendations are relevant to the viewed product` — moisturizer (Passed)
  - AC5: `Recommendations are relevant to the viewed product` — mascara (Passed)
  - AC5: `Recommendations are relevant to the viewed product` — vitamin c serum (Passed)
- **Failed:** **2**
  - AC6: `Validate cart recommendations are relevant to cart items` (Failed — `TimeoutException` waiting for `//h2[normalize-space()='Beauty under $20']` on cart page)
  - AC7: `Validate beauty category recommendations across products and cart` (Failed — `TimeoutException` during recommendation extraction)
- **Skipped:** 0
- **Duration Recorded:** 1134.5 seconds (~18.9 minutes).

---

## 7. GENAI / GITHUB COPILOT USAGE EVIDENCE

- **Documented Evidence File:** [docs/Copilot_GenAI_Evidence.md](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/docs/Copilot_GenAI_Evidence.md) contains documented records of AI-assisted activities.
- **Supported GenAI Contributions:**
  1. *Requirement & AC Interpretation:* Assisting in decomposing business requirements into executable Gherkin scenarios.
  2. *Scenario & Case Structuring:* Transforming AC1–AC7 acceptance criteria into 34 scenario entries in [Test_Scenarios.xlsx](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/test-artifacts/Test_Scenarios.xlsx) and 190 step rows in [Test_Cases_qTest.xlsx](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/test-artifacts/Test_Cases_qTest.xlsx).
  3. *Page Object & Step Definition Generation:* Implementing Selenium locators, explicit waits, DOM scroll routines, and TestNG assertions.
  4. *Deterministic Analyzer Implementation:* Creating keyword normalization, domain dictionary matching, and threshold calculation logic in [RecommendationAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java) and [BeautyCategoryAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/BeautyCategoryAnalyzer.java).
  5. *Failure Analysis & Debugging:* Analyzing `TimeoutException` and DOM click intercept issues during live Ulta testing.
- **Repository Evidence Boundaries:**
  - The repository does **not** store raw Copilot prompt transcripts or a Git commit history.
  - Analyzers are rule-based Java utilities, not AI/ML recommendation models.

---

## 8. FINAL GAPS

The following gaps exist relative to official assignment deliverables:

1. **Framework & TestNG Suite:**
   - [testng.xml](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/testng.xml) is 0 bytes (empty). Tests rely entirely on default Surefire class scanning.
2. **BDD Allure Reporting Integration:**
   - `allure-cucumber7-jvm` dependency is missing in [pom.xml](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/pom.xml).
   - `io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm` is missing from `@CucumberOptions` in [TestRunner.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/runners/TestRunner.java).
   - `allure-maven` plugin is not configured in `pom.xml`.
3. **Allure Report Serving / Viewing:**
   - Static `allure-report/index.html` fails to load in browsers without an HTTP server or `--single-file` bundle due to CORS.
4. **Selenium Robustness on Live Ulta Application:**
   - AC6 (`cart.feature`) and AC7 (`beauty-category.feature`) failed due to live site DOM instability (`Beauty under $20` carousel visibility timeouts).
5. **Test Artifact Deliverables:**
   - `Test_Cases_Automation_Execution_Scope.xlsx` is missing from `test-artifacts/`.
6. **Sources & Prompts Documentation:**
   - Raw prompt/response transcripts used during GenAI sessions are not compiled into a dedicated prompt archive.
7. **Video Demonstration Deliverable:**
   - No project demo recording/video artifact is present in the repository.

---

## NEXT ACTIONS

1. **Configure Allure Cucumber Adapter:** Add `allure-cucumber7-jvm` dependency and `allure-maven` plugin to `pom.xml`, and add `"io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm"` to `@CucumberOptions` in `TestRunner.java`.
2. **Populate `testng.xml`:** Configure `testng.xml` with `<suite>` and `<test>` pointing to `runners.TestRunner` for standard TestNG execution.
3. **Harden AC6 & AC7 Page Object Selectors:** Update `CartPage.java` and `ProductPage.java` recommendation carousel locators and dynamic waits to handle varying Ulta carousel headers gracefully.
4. **Create Missing Execution-Scope Workbook:** Generate `test-artifacts/Test_Cases_Automation_Execution_Scope.xlsx` aligned with the 34 scenarios and current automation coverage.
5. **Generate Self-Contained Allure Report & Demo Video:** Generate a single-file Allure report (`allure generate --single-file`) and record the required execution walkthrough video.
