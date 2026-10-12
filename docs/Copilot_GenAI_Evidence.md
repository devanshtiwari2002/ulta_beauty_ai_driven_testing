# GitHub Copilot / GenAI Evidence

## 1. Purpose

This document records the evidence-based contribution of GitHub Copilot / GenAI to the Ulta Generative AI Testing Solution. The objective is to identify where GenAI supported the testing lifecycle, distinguish that support from human-provided requirements and human review, and show which conclusions are supported by repository artifacts and the development workflow.

This document does not claim that Copilot independently produced the complete solution. The project contains human-provided testing intent, executable specifications, framework code, page objects, step definitions, and deterministic business-rule utilities. GenAI assistance is described only where the available project history, artifacts, or development evidence support it.

## 2. Project Context

- **Application:** Ulta Beauty web application.
- **User story:** The solution was developed around the testing of the Ulta shopping experience, including homepage access, product search, product selection, adding products to the bag, cart verification, and recommendation relevance.
- **Acceptance criteria:** The solution covers seven acceptance criteria, AC1 through AC7:
  - AC1: Homepage essentials and navigation controls.
  - AC2: Product search results for beauty products.
  - AC3: Product selection and add-to-bag behavior.
  - AC4: Adding and verifying three products in the cart.
  - AC5: Relevance of product recommendations to the viewed product.
  - AC6: Relevance of cart recommendations to cart items.
  - AC7: Beauty-category relevance across product and cart recommendations.
- **Automation stack:** Java, Selenium WebDriver, Cucumber BDD, TestNG, Maven, and Page Object Model.

The acceptance criteria are represented behaviorally in the feature files and in the test-case workbooks. No repository artifact contains a durable, separately maintained original requirements document or a machine-readable acceptance-criteria traceability record.

## 3. GenAI Contribution Across the Testing Lifecycle

The following table records the lifecycle stages that are supported by available project history or artifacts. “AI Generated” means the available development-session evidence shows an AI-assisted generation outcome. “AI Assisted” means GenAI supported analysis, implementation, review, or refinement without evidence that it independently owned the complete artifact. “Human Provided” identifies the human role. “Automated Framework” identifies framework execution rather than AI execution.

| Lifecycle Stage | Human Input | Copilot / GenAI Contribution | Output / Artifact | Contribution Type |
|---|---|---|---|---|
| Requirement / acceptance-criteria interpretation | User story and acceptance criteria supplied through the development workflow | Assisted interpretation of the intended behavior and mapping of acceptance criteria to test flows | Feature scenarios, test-case workbook, and implementation mapping | AI Assisted |
| Test scenario generation | Approved acceptance criteria, expected outcomes, and relevant product terms | Selected and structured behavior into Cucumber scenarios and Scenario Outlines | Five Cucumber feature files | AI Generated or AI Assisted, subject to artifact provenance |
| Formal test-case generation | Approved scenario list, step definitions, expected results, data, and priority information | Converted scenario behavior into formal test-case records with test-case IDs, steps, expected results, data, and traceability fields | Test_Cases_qTest.xlsx and automated-release workbook | AI Generated or AI Assisted, subject to artifact provenance |
| Gherkin feature generation | Acceptance criteria and scenario expectations | Helped create and refine executable Gherkin behavior | Cucumber feature files | AI Generated or AI Assisted |
| Page Object implementation | Application behavior, required UI controls, and observed DOM structure | Assisted implementation of Selenium page-object methods and locators | Page Object classes under src/test/java/pages | AI Generated or AI Assisted |
| Selenium automation implementation | Required browser flows and expected UI behavior | Assisted creation of WebDriver lifecycle, navigation, interaction, wait, and assertion code | WebDriver provider, page objects, and Selenium calls | AI Generated or AI Assisted |
| Step-definition implementation | Test behavior, feature steps, and expected outcomes | Assisted binding of Cucumber steps to Java methods and context-specific assertions | Step-definition classes | AI Generated or AI Assisted |
| Utility / recommendation analyzer implementation | Thresholds, recommendation definitions, and keyword behavior | Assisted generation of deterministic recommendation and beauty-category analysis utilities | RecommendationAnalyzer.java and BeautyCategoryAnalyzer.java | AI Generated or AI Assisted |
| Debugging and refinement | Test failures, stack traces, UI behavior, and framework output | Assisted diagnosis of locators, waits, recommendation extraction, and failing assertions | Refined utility/page-object code and execution evidence | AI Assisted |
| Test execution/result analysis | Execution request and generated test artifacts | Assisted interpretation of failures and results, without replacing Maven/Selenium/TestNG/Cucumber execution | Surefire/TestNG/Cucumber reports and result summaries | AI Assisted / Automated Framework |

The repository does not contain checked-in Copilot prompts, generation logs, source-level AI attribution, or a Git history. Therefore, the table uses the available development-session evidence to describe assistance, while clearly noting that source-file authorship cannot be independently verified from repository contents.

## 4. Test Scenario Generation

The scenario source is [test-artifacts/Test_Scenarios.xlsx](../test-artifacts/Test_Scenarios.xlsx). It contains 34 approved scenarios covering AC1–AC7. The scenario set includes positive, negative, and boundary scenarios.

The corresponding executable behavior is represented in the five feature files:

- [src/test/resources/features/homepage.feature](../src/test/resources/features/homepage.feature)
- [src/test/resources/features/search.feature](../src/test/resources/features/search.feature)
- [src/test/resources/features/product.feature](../src/test/resources/features/product.feature)
- [src/test/resources/features/cart.feature](../src/test/resources/features/cart.feature)
- [src/test/resources/features/beauty-category.feature](../src/test/resources/features/beauty-category.feature)

GenAI supported the transformation of the acceptance criteria into scenario structures, examples, and expected outcomes. The available evidence does not establish that every scenario was generated by Copilot, and it does not prove that all 34 scenarios were executed. The workbook confirms the approved scenario inventory; execution evidence covers a separate run of 11 Cucumber scenarios.

## 5. Test Case Generation

The formal test-case source is [test-artifacts/Test_Cases_qTest.xlsx](../test-artifacts/Test_Cases_qTest.xlsx). The current workbook contains 34 test cases, 190 step rows, and traceability to 34 scenario identifiers. Each case contains detailed execution steps, expected results, test data, and acceptance-criteria mappings.

The current artifact also contains Manual and Automated classifications. The workbook currently records 112 Manual entries and 78 Automated entries, so a current-user review is required before using it as a final all-Automated release artifact. The automated-release workbook exists separately, but this document does not treat its status as proof of execution.

GenAI supported the conversion of approved scenarios into formal test-case records through scenario decomposition, step sequencing, expected-result wording, and test-data arrangement. Human review remains required to validate that the generated case structure matches the approved acceptance criteria and that classification, status, and execution evidence are accurate.

## 6. Automation Generation

Supported GenAI contribution is present in the following areas:

- **Cucumber feature files:** GenAI assistance supported conversion of acceptance behavior into Gherkin scenarios, steps, and Scenario Outline examples. The available repository cannot prove the authorship of every feature line.
- **Page Objects:** [src/test/java/pages/HomePage.java](../src/test/java/pages/HomePage.java), [src/test/java/pages/SearchResultsPage.java](../src/test/java/pages/SearchResultsPage.java), [src/test/java/pages/ProductPage.java](../src/test/java/pages/ProductPage.java), and [src/test/java/pages/CartPage.java](../src/test/java/pages/CartPage.java) contain Selenium locators, navigation, interaction methods, waits, and UI assertions. These are framework code and were not independently proven to be AI-authored from the repository alone.
- **Selenium WebDriver code:** [src/test/java/utils/WebDriverProvider.java](../src/test/java/utils/WebDriverProvider.java) provides the driver lifecycle. Page objects use Selenium WebDriver APIs and explicit waits. The execution itself was performed by Maven, Cucumber, Selenium, and TestNG.
- **Step definitions:** [src/test/java/stepdefinitions/HomePageSteps.java](../src/test/java/stepdefinitions/HomePageSteps.java), [src/test/java/stepdefinitions/SearchSteps.java](../src/test/java/stepdefinitions/SearchSteps.java), [src/test/java/stepdefinitions/ProductSteps.java](../src/test/java/stepdefinitions/ProductSteps.java), [src/test/java/stepdefinitions/CartSteps.java](../src/test/java/stepdefinitions/CartSteps.java), and [src/test/java/stepdefinitions/BeautyCategorySteps.java](../src/test/java/stepdefinitions/BeautyCategorySteps.java) bind the feature steps to Java behavior and assertions. The available history supports assisted implementation, but not a claim that every source line was generated by Copilot.
- **Reusable utilities:** [src/test/java/utils/RecommendationAnalyzer.java](../src/test/java/utils/RecommendationAnalyzer.java) and [src/test/java/utils/BeautyCategoryAnalyzer.java](../src/test/java/utils/BeautyCategoryAnalyzer.java) implement deterministic business-rule analysis. They are not AI/ML recommendation engines.

Human roles included supplying requirements, reviewing generated code, inspecting the application DOM, correcting issues, selecting product terms, validating thresholds, reviewing results, and deciding whether a failure is a real product defect, an environmental issue, or an automation issue.

## 7. Debugging and Refinement

The following issues were actually encountered during development or execution and were supported by project evidence:

- Selenium locator issues involved product search, recommendation-carousel headings, and product-page controls.
- WebDriver waits were required while the live Ulta page loaded and rendered dynamic elements.
- Recommendation extraction failed or returned incomplete data when the recommendation section was not visible or rendered in the expected form.
- The cart recommendation flow failed because the live recommendation collection returned no matches against the moisturizer and mascara source keywords.
- The product recommendation flow failed when the “Similar items for you” heading was not found within the expected timeout.
- The live site produced overlay or click-interception behavior, including an overlay flyout intercepting a search-button click.
- The project’s two recommendation analyzers are deterministic keyword-based utilities, not machine-learning models.

Development-session evidence supports GenAI assistance in diagnosing and refining these issues. Repository reports and source code support the underlying failure conditions, but the files do not provide source-level provenance for precisely which lines were AI-generated.

## 8. Execution

Execution was performed by Maven, Selenium, Cucumber, TestNG, and the automated support code. GenAI did not execute the tests.

The supplied execution result for this deliverable is:

- **Cucumber scenario executions:** 11
- **Passed:** 8
- **Failed:** 3
- **Errors:** 0

The repository’s latest current Surefire report also records 11 scenarios, with 8 passed and 3 failed, and therefore is consistent with the supplied result. The failures are reported as actual execution failures rather than generated-test-case outcomes.

The current report contains an AC6 failure caused by a live recommendation result of 0% relevance against the required 25% threshold and separate product-recommendation timeout failures. The document does not claim that AC6 passed.

## 9. GenAI Limitations

- Generated scenarios are not proof that the corresponding behavior passed.
- Generated test cases require human review for traceability, expected outcomes, test data, and status.
- Generated automation can contain incorrect locators, waits, assumptions about the live application, or incomplete handling of dynamic UI states.
- Live website behavior can change and cause automation failures.
- Business-rule assertions require validation against the actual recommendation dataset and the documented thresholds.
- Execution evidence must come from an actual Maven/Selenium/TestNG/Cucumber run and its reports.
- Repository contents do not establish authorship for every source file.
- The workspace has no Git history or durable prompt/output audit trail from which complete provenance can be reconstructed.

## 10. Evidence / Artifacts

Relevant artifacts include:

- [test-artifacts/Test_Scenarios.xlsx](../test-artifacts/Test_Scenarios.xlsx): 34 approved scenarios and AC1–AC7 coverage.
- [test-artifacts/Test_Cases_qTest.xlsx](../test-artifacts/Test_Cases_qTest.xlsx): current formal test-case workbook, 34 cases and 190 step rows; it currently includes Manual and Automated classifications.
- Test_Cases_Automation_Execution_Scope.xlsx: **not found in the current workspace**. It cannot be cited as an existing artifact.
- [test-artifacts/Test_Cases_qTest_Automated_Release.xlsx](../test-artifacts/Test_Cases_qTest_Automated_Release.xlsx): separate release copy that was created during the current development workflow; it must not be treated as execution evidence.
- Cucumber feature files under [src/test/resources/features](../src/test/resources/features).
- Page Objects under [src/test/java/pages](../src/test/java/pages).
- Step definitions under [src/test/java/stepdefinitions](../src/test/java/stepdefinitions).
- Recommendation and category utilities under [src/test/java/utils](../src/test/java/utils).
- Maven execution output and Surefire/TestNG reports under [target/surefire-reports](../target/surefire-reports).
- Cucumber HTML report at [target/cucumber-reports/cucumber.html](../target/cucumber-reports/cucumber.html).
- Maven configuration in [pom.xml](../pom.xml) and the Cucumber runner in [src/test/java/runners/TestRunner.java](../src/test/java/runners/TestRunner.java).

No fabricated screenshots or Copilot chat transcripts are included. The development-session audit and history references are not treated as durable repository artifacts.

## 11. Summary

GitHub Copilot / GenAI supported the testing lifecycle by helping structure acceptance criteria into scenarios, develop Cucumber specifications, assist with page-object and step-definition implementation, create deterministic recommendation utilities, review failures, and interpret execution results. Human participants supplied the requirements, approved the scenario intent, reviewed generated code, inspected the live application, corrected automation issues, and made final decisions.

The evidence does not support statements that Copilot created the complete solution, authored every source file, or independently executed all scenarios. The project remains accountable to human review, validation, and evidence-based reporting. Only the actual Maven/Selenium/TestNG/Cucumber run can establish whether the implementation passes, and the recorded run contains 8 passes and 3 failures against 11 scenario executions.
