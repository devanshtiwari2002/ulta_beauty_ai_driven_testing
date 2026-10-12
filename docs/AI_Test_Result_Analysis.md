# AI-Assisted Test Result Analysis & Triage Report

**Project:** Ulta Generative AI-driven Testing Solution  
**Date:** October 11, 2026  
**Execution Context:** TestNG + Cucumber BDD + Selenium WebDriver + Allure Test Run  
**Author:** QA & Test Automation Engineering / AI-Assisted Triage  

---

## 1. Executive Summary & Execution Metrics

The automation test suite was executed against the live Ulta Beauty web application (`https://www.ulta.com/`) using Maven Surefire, TestNG, Cucumber BDD, and Selenium WebDriver. Test reporting was recorded via the Cucumber HTML report and Allure 2.29.0 / 2.30.0 integration.

### High-Level Metrics
- **Total Scenarios Executed:** 11
- **Passed Scenarios:** 10 (90.9%)
- **Failed Scenarios:** 1 (9.1%)
- **Skipped / Broken Scenarios:** 0 (0.0%)
- **Overall Pass Rate:** **90.9%**
- **Total Test Execution Duration:** 827.1 seconds (~13.8 minutes)
- **Fastest Scenario:** `Validate homepage essentials` (12.6 seconds)
- **Longest Scenario:** `Add three products and verify the cart` (160.4 seconds)

---

## 2. Acceptance Criteria Traceability & Results Mapping (AC1–AC7)

| Acceptance Criteria | Description | Feature & Scenario | Outcome | Duration | Notes / Evidence |
|---|---|---|---|---|---|
| **AC1** | Homepage & Navigation Essentials | `homepage.feature`<br>`Scenario: Validate homepage essentials` | **PASSED** | 12.6s | Verified homepage load, title verification, search input enabled, account icon accessible. |
| **AC2** | Product Search (Moisturizer, Mascara, Vitamin C Serum) | `search.feature`<br>`Scenario Outline: Search for beauty products` (3 examples) | **PASSED** | 131.7s total<br>(49.5s, 44.0s, 38.2s) | All 3 search terms returned valid search result headers and visible product cards. |
| **AC3** | Add Product to Bag | `product.feature`<br>`Scenario: Add a moisturizer to the bag` | **PASSED** | 66.8s | Successfully navigated to PDP, handled variant selection, clicked purchase CTA, verified "IN BAG" confirmation. |
| **AC4** | Verify 3 Products in Cart | `product.feature`<br>`Scenario: Add three products and verify the cart` | **PASSED** | 160.4s | Added moisturizer, mascara, and vitamin C serum sequentially; cart displayed >= 3 items with matching product names. |
| **AC5** | Product-Page Recommendations Relevance (>= 30%) | `product.feature`<br>`Scenario Outline: Recommendations are relevant to the viewed product` (3 examples) | **PASSED** | 195.6s total<br>(65.3s, 66.1s, 64.2s) | Extracted "Similar items for you" carousel recommendations for all 3 products; measured relevance exceeded the 30% threshold. |
| **AC6** | Cart Recommendations Relevance (>= 25%) | `cart.feature`<br>`Scenario: Validate cart recommendations are relevant to cart items` | **FAILED** | 124.3s | Cart recommendations extracted (12 items) achieved **0.0% relevance** against the required **25.0% threshold**. |
| **AC7** | Combined Recommendations Beauty-Category Relevance (>= 20%) | `beauty-category.feature`<br>`Scenario: Validate beauty category recommendations across products and cart` | **PASSED** | 135.5s | Combined recommendations extracted across PDPs and cart met the >= 20.0% beauty-category classification threshold. |

---

## 3. AC6 Failure Deep Dive & Evidence

### 3.1 Scenario Under Test
- **Feature:** [src/test/resources/features/cart.feature](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/cart.feature)
- **Scenario:** `Validate cart recommendations are relevant to cart items`
- **Failing Step:** `Then at least 25 percent of recommendations should be relevant to the cart items`
- **Step Implementation:** [CartSteps.java: line 53–58](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/stepdefinitions/CartSteps.java#L53-L58)
- **Assertion Engine:** TestNG `Assert.assertTrue(analysis.meetsThreshold(25.0), analysis.diagnosticMessage(25.0))`

### 3.2 Expected vs. Actual Outcome
- **Expected Outcome:** At least 25.0% of the recommendations displayed on the cart page share relevant beauty category keywords with the items present in the cart.
- **Actual Outcome:** 0 out of 12 recommendations matched the cart items' keywords, resulting in a measured relevance of **0.0%**.

### 3.3 Raw Failure Message & Diagnostic Output
```text
java.lang.AssertionError: Source products: [Good Molecules Lightweight Daily Moisturizer, Maybelline Lash Sensational Sky High Mascara, Maybelline Free Lifter Liner mini with select Maybelline purchase]; combined keywords: [moisturizer, mascara]; total recommendations: 12; relevant recommendations: 0; relevance: 0.0% (required: 25.0%); relevant: []; non-relevant: [Glycolic Acid 7% Exfoliating and Brightening Daily Toner, Power Grip Primer, Lifeproof 16-Hour Continuous Setting Mist, Hair Perfume Fragrance Spray, Infallible 3-Second Setting Mist Spray, Micro Brow Pencil Eyebrow Pencil, Peel Off Lip Liner STAY-N, Signature Lip Pencil, ChromaPlus 6-Pan Eyeshadow Palette, Toleriane Purifying Foaming Face Wash for Oily Skin, Power Mist Hydrating Hand Sanitizer, Hyaluronic Acid 2% + B5 Hydrating Serum with Ceramides] expected [true] but found [false]
	at org.testng.Assert.fail(Assert.java:111)
	at org.testng.Assert.failNotEquals(Assert.java:1578)
	at org.testng.Assert.assertTrue(Assert.java:57)
	at stepdefinitions.CartSteps.recommendationsShouldBeRelevantToCartItems(CartSteps.java:57)
	at ✽.at least 25 percent of recommendations should be relevant to the cart items(file:///C:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/resources/features/cart.feature:6)
```

---

## 4. Failure Clustering: Facts vs. Hypotheses

To avoid jumping to conclusions or masking underlying issues, the evidence is separated into verified repository facts and investigatory hypotheses.

### Confirmed Facts (Verified from Execution Logs & Code)
1. **Cart Contents:** The cart successfully held 3 items:
   - `Good Molecules Lightweight Daily Moisturizer`
   - `Maybelline Lash Sensational Sky High Mascara`
   - `Maybelline Free Lifter Liner mini with select Maybelline purchase` (promotional gift with purchase added by Ulta).
2. **Derived Target Keywords:** The deterministic rule-based utility [RecommendationAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java) matched words in the source product names against its predefined beauty taxonomy list and extracted two target keywords:
   - `moisturizer`
   - `mascara`
3. **Scraped Carousel:** The automation page object [CartPage.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/CartPage.java#L23-L28) specifically targets:
   - Heading: `//h2[normalize-space()='Beauty under $20']`
   - Region: `div[role='region'][aria-label='Beauty under $20 Product Carousel']`
4. **Scraped Products:** Exactly 12 product names were parsed from the cards in that carousel:
   1. *Glycolic Acid 7% Exfoliating and Brightening Daily Toner* (Category: toner / exfoliant)
   2. *Power Grip Primer* (Category: primer)
   3. *Lifeproof 16-Hour Continuous Setting Mist* (Category: setting spray / mist)
   4. *Hair Perfume Fragrance Spray* (Category: hair perfume / fragrance)
   5. *Infallible 3-Second Setting Mist Spray* (Category: setting mist)
   6. *Micro Brow Pencil Eyebrow Pencil* (Category: brow pencil)
   7. *Peel Off Lip Liner STAY-N* (Category: lip liner)
   8. *Signature Lip Pencil* (Category: lip pencil)
   9. *ChromaPlus 6-Pan Eyeshadow Palette* (Category: eyeshadow)
   10. *Toleriane Purifying Foaming Face Wash for Oily Skin* (Category: cleanser / face wash)
   11. *Power Mist Hydrating Hand Sanitizer* (Category: hand sanitizer)
   12. *Hyaluronic Acid 2% + B5 Hydrating Serum with Ceramides* (Category: serum)
5. **Exact Lexical Evaluation:** None of the 12 extracted product names contained the literal substring/word `moisturizer` or `mascara`.
6. **Mathematical Calculation:** `0 relevant / 12 total = 0.0% < 25.0%`. Hence, the test failed strictly on the threshold assertion.

### Unverified Hypotheses (To Investigate)
- **Hypothesis A (Carousel Selection Mismatch):** The "Beauty under $20" section is a general price-bracket merchandising promotion rather than an algorithmic cross-sell recommendation carousel (such as "Frequently Bought Together" or "Recommended for You").
- **Hypothesis B (Application Business Logic):** Ulta's live cart recommendation engine does not tailor suggestions to cart item categories, but instead pushes high-margin impulse-buy beauty items under $20.
- **Hypothesis C (Evaluation Metric Inflexibility):** The keyword matching logic only looks for the exact product category terms present in the cart (`moisturizer`, `mascara`), ignoring related complementary beauty categories (e.g., face wash, serum, toner, lip liner) that a recommendation engine would naturally suggest.

---

## 5. Potential Causes to Investigate

### 1. Carousel Targeting & Selector Behavior in CartPage
- **Location:** [CartPage.java: lines 23–28](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/pages/CartPage.java#L23-L28)
- **Detail:** The page object targets `Beauty under $20 Product Carousel`.
- **Question:** Does Ulta.com render an algorithmic recommendation carousel on the bag page (e.g., `You may also like`, `Complete your routine`, `Recommended for you`) that is dynamically injected below or above "Beauty under $20"?
- **Status:** *Not verified without live DOM inspection of Ulta's cart page.*

### 2. Lexical Keyword Matching vs. Semantic Relevance
- **Location:** [RecommendationAnalyzer.java: lines 45–66](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java#L45-L66)
- **Detail:** The analyzer normalizes product titles and performs substring keyword checks:
  ```java
  boolean contains = (" " + normalizedRecommendationName + " ").contains(" " + normalizedKeyword + " ");
  ```
- **Observation:** If a user puts a moisturizer in the cart, an e-commerce recommendation algorithm will frequently recommend a complementary product (such as a *cleanser*, *serum*, or *toner* from the skincare routine) rather than another redundant moisturizer. The test logic currently expects recommendations to share the *exact source keywords* (`moisturizer` or `mascara`).

### 3. Promotional Gift-with-Purchase (GWP) Injection
- **Detail:** Ulta automatically injected a 3rd item into the cart: `Maybelline Free Lifter Liner mini with select Maybelline purchase`.
- **Observation:** `RecommendationAnalyzer` extracted keywords only for `moisturizer` and `mascara` from the explicit items, but the live cart state included promotional items.

---

## 6. Recommended Debugging & Remediation Steps

### Step 1: Live DOM Inspection of Ulta Cart Page (Priority: High)
- Manually or via automated diagnostic script navigate to `https://www.ulta.com/bag` with items in cart.
- Check all `h2` headings and carousel containers (`div[role='region']`, `section`) present on the page.
- Determine whether a dedicated personalized recommendation carousel exists (e.g., `You Might Also Like`, `Pairs Well With`) versus the price-promotional `Beauty under $20` carousel.

### Step 2: Validate Acceptance Criteria Definition with Stakeholders (Priority: High)
- Review the requirement for AC6: *"Cart recommendations >=25% relevant"*.
- Clarify whether "relevant" implies:
  1. *Same-category redundancy:* Cart has moisturizer -> recommendations must be moisturizers.
  2. *Cross-category routine compatibility:* Cart has skincare/makeup -> recommendations are compatible beauty products (skincare routine items or eye makeup).
  3. *General beauty domain relevance:* Recommendations belong to the beauty domain (similar to AC7).

### Step 3: Selector Fallback & Resiliency Enhancement (Priority: Medium)
- If Ulta's cart carousel headings change dynamically (e.g., `Beauty under $20`, `Recommended for you`, `Top picks for you`), update `CartPage.java` to support dynamic carousel discovery with logging of the carousel heading encountered.

### Step 4: Diagnostic Artifact Logging (Priority: Medium)
- In `CartSteps.java` and `CartPage.java`, log all candidate carousel headings, all scraped item names, and the exact keyword match results to Allure attachments so future runs capture DOM snapshots upon assertion failure.

---

## 7. Limitations & Technical Constraints

1. **Deterministic Rule Engine (Not AI/ML):**
   - Note: Neither [RecommendationAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/RecommendationAnalyzer.java) nor [BeautyCategoryAnalyzer.java](file:///c:/Users/Devansh%20Tiwari/Desktop/ulta-ai-automation/src/test/java/utils/BeautyCategoryAnalyzer.java) is a generative AI or machine-learning model. They are deterministic, rule-based Java pattern-matching utilities using static dictionary lookups.
2. **Third-Party Application Volatility:**
   - Ulta.com is a production e-commerce platform with dynamic A/B testing, regional inventory differences, and seasonal merchandising carousels. Test results can vary based on live site promotional campaigns.
3. **Absence of Historical DOM Snapshots:**
   - The test run captured execution timings and assertion messages in Surefire XML and Allure JSON, but did not serialize a full HTML DOM page source of the cart page at failure time.

---

## 8. Role of AI-Assisted Analysis in Test Triage

AI-assisted engineering and analysis tools (e.g., GitHub Copilot, LLM-driven test triage) enhance the QA lifecycle in this context through:

1. **Automated Root-Cause Clustering:**
   - Instantly parsing high-volume assertion messages, isolating that 0/12 matches occurred, identifying the target keywords (`[moisturizer, mascara]`), and classifying the failure as a **domain-relevance/selector mismatch** rather than a WebDriver crash or locator timeout.
2. **Taxonomy & Semantic Analysis:**
   - Detecting that the 12 non-matching items are indeed legitimate beauty products (toner, primer, setting spray, brow pencil, lip liner, face wash, serum), thereby confirming that the carousel returned beauty products, but failed a strict lexical keyword filter.
3. **Selector Refactoring & Heuristics:**
   - Recommending multi-strategy selector chains and resilient wait patterns when e-commerce applications rotate promotional banners.
4. **Traceability Auditing:**
   - Cross-referencing raw Allure result records, Surefire XML outputs, and Cucumber step definitions against original acceptance criteria (AC1–AC7) to ensure factual, evidence-based reporting without hallucinated outcomes.
