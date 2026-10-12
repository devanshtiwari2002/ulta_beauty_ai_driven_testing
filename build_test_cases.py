from collections import Counter
from pathlib import Path

from openpyxl import load_workbook
from openpyxl.styles import Alignment, Border, Font, PatternFill, Side
from openpyxl.utils import get_column_letter

ROOT = Path(__file__).parent
SCENARIO_FILE = ROOT / "test-artifacts" / "Test_Scenarios.xlsx"
WORKBOOK_FILE = ROOT / "test-artifacts" / "Test_Cases_qTest.xlsx"

wb_scenarios = load_workbook(SCENARIO_FILE, data_only=True)
scenario_ws = wb_scenarios["Test Scenarios"]
scenarios = {
    row[0].value: {
        "ac": row[1].value,
        "title": row[2].value,
        "objective": row[3].value,
        "type": row[4].value,
        "priority": row[5].value,
        "outcome": row[6].value,
    }
    for row in scenario_ws.iter_rows(min_row=2, values_only=False)
    if row[0].value and str(row[0].value).startswith("SC-")
}

assert len(scenarios) == 34, f"Expected 34 approved scenarios, found {len(scenarios)}"
assert len(set(scenarios)) == 34

OUTCOME = {
    "SC-AC1-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC1-002": ("Automated", "Functional Positive", "Positive"),
    "SC-AC1-003": ("Automated", "Functional Positive", "Positive"),
    "SC-AC2-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC2-002": ("Automated", "Functional Positive", "Positive"),
    "SC-AC2-003": ("Automated", "Functional Positive", "Positive"),
    "SC-AC2-004": ("Automated", "Business Rule", "Positive"),
    "SC-AC2-005": ("Automated", "Negative", "Negative"),
    "SC-AC2-006": ("Automated", "Negative", "Negative"),
    "SC-AC3-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC3-002": ("Automated", "Functional Positive", "Positive"),
    "SC-AC3-003": ("Automated", "Functional Positive", "Positive"),
    "SC-AC3-004": ("Automated", "Negative", "Negative"),
    "SC-AC3-005": ("Automated", "Negative", "Negative"),
    "SC-AC4-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC4-002": ("Automated", "Business Rule", "Positive"),
    "SC-AC4-003": ("Automated", "Business Rule", "Positive"),
    "SC-AC4-004": ("Automated", "Negative", "Negative"),
    "SC-AC5-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC5-002": ("Automated", "Boundary Value", "Boundary"),
    "SC-AC5-003": ("Automated", "Negative", "Negative"),
    "SC-AC5-004": ("Automated", "Negative", "Negative"),
    "SC-AC5-005": ("Automated", "Negative", "Negative"),
    "SC-AC6-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC6-002": ("Automated", "Boundary Value", "Boundary"),
    "SC-AC6-003": ("Automated", "Negative", "Negative"),
    "SC-AC6-004": ("Automated", "Negative", "Negative"),
    "SC-AC6-005": ("Automated", "Negative", "Negative"),
    "SC-AC7-001": ("Automated", "Functional Positive", "Positive"),
    "SC-AC7-002": ("Automated", "Boundary Value", "Boundary"),
    "SC-AC7-003": ("Automated", "Negative", "Negative"),
    "SC-AC7-004": ("Automated", "Negative", "Negative"),
    "SC-AC7-005": ("Automated", "Negative", "Negative"),
    "SC-AC7-006": ("Automated", "Negative", "Negative"),
}

PRECONDITIONS = {
    "SC-AC1-001": "Chrome browser is available and the Ulta website is accessible.",
    "SC-AC1-002": "Chrome browser is available and the Ulta homepage is accessible.",
    "SC-AC1-003": "Chrome browser is available and the Ulta homepage is accessible.",
    "SC-AC2-001": "Ulta homepage is accessible and the product search control can be used.",
    "SC-AC2-002": "Ulta homepage is accessible and the product search control can be used.",
    "SC-AC2-003": "Ulta homepage is accessible and the product search control can be used.",
    "SC-AC2-004": "Ulta homepage is accessible and a product search has returned visible results.",
    "SC-AC2-005": "Ulta homepage is accessible and an empty or unsupported search input can be submitted.",
    "SC-AC2-006": "Ulta homepage is accessible and an unrecognized search term can be submitted.",
    "SC-AC3-001": "Ulta homepage is accessible and product search can return a visible product result.",
    "SC-AC3-002": "Ulta homepage is accessible and a product without a required variant can be selected.",
    "SC-AC3-003": "Ulta homepage is accessible and a product requiring a shade or size selection can be selected.",
    "SC-AC3-004": "Ulta homepage is accessible and a product requiring a shade or size selection is displayed.",
    "SC-AC3-005": "Ulta homepage is accessible and the cart operation can be made to fail through an unavailable or blocked cart service.",
    "SC-AC4-001": "Ulta homepage is accessible and the required product search can be performed.",
    "SC-AC4-002": "Ulta homepage is accessible and three products can be added to the cart.",
    "SC-AC4-003": "Ulta homepage is accessible and the cart contains three products with displayed item counts.",
    "SC-AC4-004": "Ulta homepage is accessible and the cart can be inspected for an incomplete population.",
    "SC-AC5-001": "Product/search flow has reached the applicable product recommendation section.",
    "SC-AC5-002": "Product/search flow has reached the applicable product recommendation section with exactly 30% relevant recommendations.",
    "SC-AC5-003": "Product/search flow has reached the applicable product recommendation section with fewer than 30% relevant recommendations.",
    "SC-AC5-004": "Product/search flow has reached the applicable product recommendation section with 0% relevant recommendations.",
    "SC-AC5-005": "Product/search flow has reached the applicable product recommendation section with an empty recommendation collection.",
    "SC-AC6-001": "Product/search flow has reached the cart and the applicable cart recommendation section.",
    "SC-AC6-002": "Product/search flow has reached the cart recommendation section with exactly 25% relevant recommendations.",
    "SC-AC6-003": "Product/search flow has reached the cart recommendation section with fewer than 25% relevant recommendations.",
    "SC-AC6-004": "Product/search flow has reached the cart recommendation section with 0% relevant recommendations.",
    "SC-AC6-005": "Product/search flow has reached the cart recommendation section with an empty recommendation collection.",
    "SC-AC7-001": "Product/search flow has reached product-page and cart-page recommendation sections for the combined collection.",
    "SC-AC7-002": "Product-page and cart-page recommendation sources are available with exactly 20% beauty, skincare, or makeup relevance.",
    "SC-AC7-003": "Product-page and cart-page recommendation sources are available with fewer than 20% beauty, skincare, or makeup relevance.",
    "SC-AC7-004": "Product-page and cart-page recommendation sources are available with 0% beauty, skincare, or makeup relevance.",
    "SC-AC7-005": "One or more product-page or cart-page recommendation sources are unavailable or incomplete.",
    "SC-AC7-006": "Product-page and cart-page recommendation sources are available but both recommendation collections are empty.",
}

TEST_DATA = {
    "SC-AC1-001": "Homepage URL: https://www.ulta.com/; verify search control and account control.",
    "SC-AC1-002": "Search control is the primary Ulta search field; use search term 1: moisturizer.",
    "SC-AC1-003": "Account access control is the visible account or sign-in control on the Ulta homepage.",
    "SC-AC2-001": "Search term 1: moisturizer; expect at least one visible product result.",
    "SC-AC2-002": "Search term 2: mascara; expect at least one visible product result.",
    "SC-AC2-003": "Search term 3: vitamin c serum; expect at least one visible product result.",
    "SC-AC2-004": "Search term: moisturizer; verify returned product names correspond to the requested category.",
    "SC-AC2-005": "Search input: an empty string or unsupported search term; verify validation and no misleading results.",
    "SC-AC2-006": "Search term: a deliberately unrecognized product name; verify the no-results state.",
    "SC-AC3-001": "Search term 1: moisturizer; select the first visible product and open its product-detail page.",
    "SC-AC3-002": "Search term 1: moisturizer; select a product that does not require a shade or size variant.",
    "SC-AC3-003": "Search term 3: vitamin c serum; select a product that requires a shade or size and choose a valid variant.",
    "SC-AC3-004": "Search term 3: vitamin c serum; select a product with a required variant and omit the required selection.",
    "SC-AC3-005": "Search term 1: moisturizer; select a product and simulate or verify a cart-operation failure.",
    "SC-AC4-001": "Cart product set: moisturizer, mascara, vitamin c serum; verify at least three products are present.",
    "SC-AC4-002": "Cart product set: moisturizer, mascara, vitamin c serum; verify exact product identity in the cart.",
    "SC-AC4-003": "Cart product set: moisturizer, mascara, vitamin c serum; verify displayed count equals actual listed items.",
    "SC-AC4-004": "Cart product set is incomplete or missing one of moisturizer, mascara, or vitamin c serum.",
    "SC-AC5-001": "Viewed product: moisturizer; recommendation set has at least 30% relevant recommendations, threshold 30%.",
    "SC-AC5-002": "Viewed product: moisturizer; recommendation set contains exactly 30% relevant recommendations, threshold 30%.",
    "SC-AC5-003": "Viewed product: moisturizer; recommendation set contains less than 30% relevant recommendations, threshold 30%.",
    "SC-AC5-004": "Viewed product: moisturizer; recommendation set contains 0% relevant recommendations, threshold 30%.",
    "SC-AC5-005": "Viewed product: moisturizer; recommendation collection is empty, threshold 30%.",
    "SC-AC6-001": "Cart products: moisturizer and mascara; recommendation set has at least 25% relevant recommendations, threshold 25%.",
    "SC-AC6-002": "Cart products: moisturizer and mascara; recommendation set contains exactly 25% relevant recommendations, threshold 25%.",
    "SC-AC6-003": "Cart products: moisturizer and mascara; recommendation set contains less than 25% relevant recommendations, threshold 25%.",
    "SC-AC6-004": "Cart products: moisturizer and mascara; recommendation set contains 0% relevant recommendations, threshold 25%.",
    "SC-AC6-005": "Cart products: moisturizer and mascara; recommendation collection is empty, threshold 25%.",
    "SC-AC7-001": "Recommendation sources: product pages and cart page; combined relevance is at least 20%, threshold 20%.",
    "SC-AC7-002": "Recommendation sources: product pages and cart page; combined relevance is exactly 20%, threshold 20%.",
    "SC-AC7-003": "Recommendation sources: product pages and cart page; combined relevance is below 20%, threshold 20%.",
    "SC-AC7-004": "Recommendation sources: product pages and cart page; combined relevance is 0%, threshold 20%.",
    "SC-AC7-005": "Recommendation sources: product-page results and cart-page results; one source is incomplete or unavailable.",
    "SC-AC7-006": "Recommendation sources: product-page and cart-page collections are empty; verify no compliance result is reported.",
}


def steps_for(sid):
    if sid == "SC-AC1-001":
        return [
            (1, "Open https://www.ulta.com/.", "The Ulta homepage URL loads in Chrome."),
            (2, "Wait for the homepage to finish loading and verify the page title or main navigation.", "The homepage is displayed and no page-load error is shown."),
            (3, "Locate the primary product search control.", "The search control is visible and enabled."),
            (4, "Locate the account or sign-in control.", "The account or sign-in control is visible and accessible."),
            (5, "Perform an accessibility-visible check on the search and account controls.", "Both controls retain accessible names and are not obscured."),
        ]
    if sid == "SC-AC1-002":
        return [
            (1, "Open https://www.ulta.com/.", "The Ulta homepage loads."),
            (2, "Wait for the homepage to load completely.", "The homepage header and primary navigation are visible."),
            (3, "Locate the product search control.", "The search control is visible and enabled."),
            (4, "Enter search term 1: moisturizer into the search control.", "The entered search term is retained in the search field."),
            (5, "Submit the search and wait for the results view.", "The search-results page or visible result list is displayed."),
            (6, "Verify that the search control remains usable after submission.", "The search field remains enabled and responds to a new query."),
        ]
    if sid == "SC-AC1-003":
        return [
            (1, "Open https://www.ulta.com/.", "The Ulta homepage loads."),
            (2, "Wait for the main navigation and account affordance to render.", "The homepage content is available."),
            (3, "Locate the account or sign-in control.", "The account control is visible."),
            (4, "Read the accessible name or visible label of the account control.", "An accessible account or sign-in name is present."),
            (5, "Verify the control is enabled and not hidden behind an overlay.", "The account control is enabled and keyboard-accessible."),
        ]
    if sid in {"SC-AC2-001", "SC-AC2-002", "SC-AC2-003"}:
        term = {"SC-AC2-001": "moisturizer", "SC-AC2-002": "mascara", "SC-AC2-003": "vitamin c serum"}[sid]
        return [
            (1, f"Open https://www.ulta.com/ and wait for the homepage to load.", "The Ulta homepage is displayed."),
            (2, f"Locate the primary search control and enter {term!r}.", f"The search field contains {term!r}."),
            (3, "Submit the search and wait for results to appear.", "A search-results view is displayed."),
            (4, "Locate visible product result cards.", "At least one product result is visible."),
            (5, "Record the product names returned for the search.", "At least one product name is captured from the results."),
        ]
    if sid == "SC-AC2-004":
        return [
            (1, "Open https://www.ulta.com/ and wait for the homepage.", "The Ulta homepage is displayed."),
            (2, "Search for moisturizer and capture all visible product names.", "The search returns visible product results."),
            (3, "Compare every returned product name to the moisturizer category.", "Each returned product is associated with a relevant category or marked as unrelated."),
            (4, "Count the products whose category matches the moisturizer query.", "The query-relevance count is recorded."),
            (5, "Verify that at least one returned product is associated with the requested category.", "The search result is relevant to the requested category."),
        ]
    if sid == "SC-AC2-005":
        return [
            (1, "Open https://www.ulta.com/ and wait for the homepage.", "The Ulta homepage is displayed."),
            (2, "Submit an empty search input or an unsupported search term.", "The search request is submitted without a meaningful product query."),
            (3, "Wait for the search validation or empty-result state.", "A validation message or empty-result state is displayed."),
            (4, "Verify no unrelated stale product results are displayed.", "Only the appropriate empty or validation state is shown."),
            (5, "Verify the user can retry with a supported search term.", "A supported retry remains available."),
        ]
    if sid == "SC-AC2-006":
        return [
            (1, "Open https://www.ulta.com/ and wait for the homepage.", "The Ulta homepage is displayed."),
            (2, "Search for a deliberately unrecognized product name.", "The unrecognized search term is submitted."),
            (3, "Wait for the no-results state.", "No product result is shown as a valid match."),
            (4, "Verify the no-results message is clear and user-friendly.", "The user receives an explicit no-results message."),
            (5, "Verify no misleading product result is reported.", "The no-results state is not replaced by a false-positive result."),
        ]
    if sid == "SC-AC3-001":
        return [
            (1, "Open https://www.ulta.com/ and wait for the homepage.", "The homepage is displayed."),
            (2, "Search for moisturizer and wait for visible results.", "The moisturizer search results are displayed."),
            (3, "Select the first visible product result.", "The selected product-detail page loads."),
            (4, "Capture the product name displayed on the detail page.", "A nonblank product identity is visible."),
            (5, "Verify the product page contains the product identity and purchase controls.", "The selected product detail page is confirmed."),
        ]
    if sid == "SC-AC3-002":
        return [
            (1, "Open https://www.ulta.com/ and search for moisturizer.", "Moisturizer results are displayed."),
            (2, "Open the first visible product detail page.", "The selected product detail page loads."),
            (3, "Check whether the product requires a variant.", "The product variant requirement is identified."),
            (4, "If no variant is required, click the purchase call-to-action.", "The add-to-bag action is invoked."),
            (5, "Verify the bag confirmation and cart item.", "The product is added successfully and appears in the bag."),
        ]
    if sid == "SC-AC3-003":
        return [
            (1, "Open https://www.ulta.com/ and search for vitamin c serum.", "Vitamin C serum results are displayed."),
            (2, "Open the first visible product detail page.", "The product detail page loads."),
            (3, "Identify a required shade or size option.", "A valid variant option is available."),
            (4, "Select the required variant and click the purchase call-to-action.", "The selected variant is retained and add-to-bag starts."),
            (5, "Verify the selected variant and cart confirmation.", "The product with the selected variant is added to the bag."),
        ]
    if sid == "SC-AC3-004":
        return [
            (1, "Open https://www.ulta.com/ and search for vitamin c serum.", "Vitamin C serum results are displayed."),
            (2, "Open a product that requires a shade or size selection.", "The product detail page loads."),
            (3, "Do not select a required variant.", "No variant selection is made."),
            (4, "Click the purchase call-to-action.", "The add-to-bag operation is attempted."),
            (5, "Verify that the product is not added and a clear missing-variant message appears.", "The missing variant is identified and no false success is reported."),
        ]
    if sid == "SC-AC3-005":
        return [
            (1, "Open https://www.ulta.com/ and search for moisturizer.", "Moisturizer results are displayed."),
            (2, "Open a product detail page and trigger an unavailable or blocked cart operation.", "The cart operation fails safely."),
            (3, "Attempt to add the selected product to the bag.", "The add-to-bag attempt is submitted."),
            (4, "Verify the error state or retry option is visible.", "A clear failure message and recovery action are shown."),
            (5, "Do not report the product as added.", "The product is not marked as added after the failed operation."),
        ]
    if sid == "SC-AC4-001":
        return [
            (1, "Open https://www.ulta.com/ and wait for the homepage.", "The homepage is available."),
            (2, "Search for moisturizer, open the first result, and add it to the bag.", "Moisturizer is added to the cart."),
            (3, "Search for mascara, open the first result, and add it to the bag.", "Mascara is added to the cart."),
            (4, "Search for vitamin c serum, open the first result, and add it to the bag.", "Vitamin C serum is added to the cart."),
            (5, "Open the cart and count all displayed items.", "At least three items are present."),
        ]
    if sid == "SC-AC4-002":
        return [
            (1, "Open https://www.ulta.com/ and add moisturizer, mascara, and vitamin c serum to the cart.", "All three required products are in the cart."),
            (2, "Open the cart and capture the displayed product names.", "Product names are readable."),
            (3, "Compare the cart names with the exact required set.", "No required product is missing or mislabeled."),
            (4, "Verify each expected product name appears exactly once or as expected.", "Cart product identity is confirmed."),
        ]
    if sid == "SC-AC4-003":
        return [
            (1, "Open https://www.ulta.com/ and add moisturizer, mascara, and vitamin c serum to the cart.", "All three products are present."),
            (2, "Open the cart and capture the displayed item count.", "The count is recorded."),
            (3, "Capture the actual number of product entries in the cart.", "The number of listed products is recorded."),
            (4, "Compare the displayed count with the actual list count.", "The displayed count matches the actual cart contents."),
        ]
    if sid == "SC-AC4-004":
        return [
            (1, "Open https://www.ulta.ai/ and inspect the cart.", "The cart is accessible."),
            (2, "Remove or omit one of the required products: moisturizer, mascara, or vitamin c serum.", "The cart becomes incomplete."),
            (3, "Open the cart and capture the displayed product list.", "The incomplete list is visible."),
            (4, "Verify the cart state identifies the missing product or incomplete population.", "The cart is clearly reported as incomplete."),
        ]
    if sid.startswith("SC-AC5-"):
        cl = {
            "SC-AC5-001": "at least 30%",
            "SC-AC5-002": "exactly 30%",
            "SC-AC5-003": "less than 30%",
            "SC-AC5-004": "0%",
            "SC-AC5-005": "an empty collection",
        }[sid]
        return [
            (1, "Open https://www.ulta.com/ and search for moisturizer.", "Moisturizer search results are displayed."),
            (2, "Open the first product and wait for the product recommendations section.", "The product recommendation section is available."),
            (3, f"Provide or select a recommendation set with {cl} relevant products for the viewed product.", f"The recommendation set contains {cl} relevant data."),
            (4, "Calculate the recommendation relevance percentage using the product-name/recommendation analyzer.", "The relevance percentage is recorded."),
            (5, "Compare the calculated percentage with the 30% threshold.", "The result meets, equals, or fails the required AC5 boundary as specified."),
            (6, "Record the final pass or fail decision and evidence.", "A clear AC5 outcome is captured."),
        ]
    if sid.startswith("SC-AC6-"):
        cl = {
            "SC-AC6-001": "at least 25%",
            "SC-AC6-002": "exactly 25%",
            "SC-AC6-003": "less than 25%",
            "SC-AC6-004": "0%",
            "SC-AC6-005": "an empty collection",
        }[sid]
        return [
            (1, "Open https://www.ulta.com/ and add moisturizer and mascara to the cart.", "The cart contains the required source products."),
            (2, "Open the cart and wait for the cart recommendation section.", "Cart recommendations are displayed or an empty state is shown."),
            (3, f"Provide or select a recommendation set with {cl} relevant products for the cart items.", f"The recommendation set contains {cl} relevant data."),
            (4, "Calculate the cart recommendation relevance percentage.", "The relevance percentage is recorded."),
            (5, "Compare the calculated percentage with the 25% threshold.", "The result meets, equals, or fails the required AC6 boundary as specified."),
            (6, "Record the final pass or fail decision and evidence.", "A clear AC6 outcome is captured."),
        ]
    if sid.startswith("SC-AC7-"):
        cl = {
            "SC-AC7-001": "at least 20%",
            "SC-AC7-002": "exactly 20%",
            "SC-AC7-003": "less than 20%",
            "SC-AC7-004": "0%",
            "SC-AC7-005": "incomplete recommendation sources",
            "SC-AC7-006": "an empty recommendation collection",
        }[sid]
        return [
            (1, "Open https://www.ulta.com/ and add moisturizer and mascara to the cart.", "The required source products are available."),
            (2, "Collect product-page recommendations for moisturizer and mascara.", "Product-page recommendations are captured."),
            (3, "Open the cart and collect cart-page recommendations.", "Cart-page recommendations are captured."),
            (4, f"Create the combined recommendation set with {cl} for the beauty, skincare, or makeup category.", "The combined recommendation data is prepared."),
            (5, "Calculate the combined beauty-category percentage from all collected recommendations.", "The category relevance percentage is recorded."),
            (6, "Compare the percentage with the 20% threshold and inspect missing-source handling.", "The AC7 pass, boundary, or fail result is confirmed."),
            (7, "Record the final combined-result evidence.", "A clear AC7 outcome is captured."),
        ]
    raise ValueError(f"No step builder for {sid}")


wb = load_workbook(WORKBOOK_FILE, data_only=False)
ws = wb["Test Cases"]
for row in ws.iter_rows(min_row=2):
    for cell in row:
        cell.value = None
ws.delete_rows(2, ws.max_row)

headers = [
    "Test Case ID", "Test Scenario ID", "Acceptance Criteria", "Test Case Name",
    "Test Type", "Priority", "Preconditions", "Test Data", "Step No", "Test Step",
    "Expected Result", "Automation Status", "Actual Result", "Status", "Defect ID", "Comments",
]
for col, header in enumerate(headers, 1):
    ws.cell(1, col, header)

row_number = 2
for index, scenario_id in enumerate(scenarios, 1):
    scenario = scenarios[scenario_id]
    automation, test_type, intent = OUTCOME[scenario_id]
    tc_id = f"TC-{index:03d}"
    step_rows = steps_for(scenario_id)
    ac = scenario["ac"]
    if ac in {"AC5", "AC6", "AC7"}:
        automation_note = (
            "Business Rule / Boundary Automation: direct Java analyzer validation with controlled recommendation datasets; "
            "thresholds are 30% for AC5, 25% for AC6, and 20% for AC7."
        )
    else:
        automation_note = (
            "UI Automation: Selenium/Cucumber/Page Object Model execution against the Ulta test application."
        )
    for step_no, step, expected in step_rows:
        ws.cell(row_number, 1, tc_id)
        ws.cell(row_number, 2, scenario_id)
        ws.cell(row_number, 3, scenario["ac"])
        ws.cell(row_number, 4, scenario["title"])
        ws.cell(row_number, 5, test_type)
        ws.cell(row_number, 6, scenario["priority"])
        ws.cell(row_number, 7, PRECONDITIONS[scenario_id])
        ws.cell(row_number, 8, TEST_DATA[scenario_id])
        ws.cell(row_number, 9, step_no)
        ws.cell(row_number, 10, step)
        ws.cell(row_number, 11, expected)
        ws.cell(row_number, 12, automation)
        ws.cell(row_number, 13, None)
        ws.cell(row_number, 14, "Not Executed")
        ws.cell(row_number, 15, None)
        ws.cell(row_number, 16, automation_note)
        row_number += 1

# Formatting and professional qTest-style presentation.
wide = {
    1: 16, 2: 16, 3: 14, 4: 55, 5: 24, 6: 12, 7: 95, 8: 95,
    9: 10, 10: 95, 11: 95, 12: 17, 13: 95, 14: 16, 15: 18, 16: 95,
}
for col, width in wide.items():
    ws.column_dimensions[get_column_letter(col)].width = width
ws.freeze_panes = "A2"
ws.auto_filter.ref = ws.dimensions
ws.sheet_view.showGridLines = False
for cell in ws[1]:
    cell.font = Font(bold=True, color="FFFFFF")
    cell.fill = PatternFill("solid", fgColor="1F4E78")
    cell.alignment = Alignment(horizontal="center", vertical="center", wrap_text=True)
    cell.border = Border(bottom=Side(style="medium", color="FFFFFF"))
for row in ws.iter_rows(min_row=2):
    for cell in row:
        cell.alignment = Alignment(vertical="top", wrap_text=True)
        cell.border = Border(bottom=Side(style="thin", color="D9E2F3"))
        if cell.column == 1:
            cell.font = Font(bold=True, color="1F4E78")
        if cell.column == 2:
            cell.font = Font(bold=True, color="1F4E78")
        if cell.column == 14 and cell.value == "Not Executed":
            cell.fill = PatternFill("solid", fgColor="F2F2F2")
            cell.font = Font(color="595959")
ws.row_dimensions[1].height = 36
for row in range(2, ws.max_row + 1):
    ws.row_dimensions[row].height = 78

wb.save(WORKBOOK_FILE)

# Independent verification of the generated artifact.
reloaded = load_workbook(WORKBOOK_FILE, data_only=True)["Test Cases"]
ids = [reloaded.cell(row, 1).value for row in range(2, reloaded.max_row + 1)]
scenario_ids = [reloaded.cell(row, 2).value for row in range(2, reloaded.max_row + 1)]
case_counts = Counter(ids)
scenario_counts = Counter(scenario_ids)
assert len(case_counts) == 34
assert all(count == len([r for r in range(2, reloaded.max_row + 1) if reloaded.cell(r, 1).value == tc]) for tc, count in case_counts.items())
assert len(scenario_counts) == 34
assert set(scenario_counts) == set(scenarios)
assert all(reloaded.cell(row, 13).value is None for row in range(2, reloaded.max_row + 1))
assert all(reloaded.cell(row, 14).value == "Not Executed" for row in range(2, reloaded.max_row + 1))
assert all(reloaded.cell(row, 15).value is None for row in range(2, reloaded.max_row + 1))
assert all(reloaded.cell(row, 11).value for row in range(2, reloaded.max_row + 1))
assert all(reloaded.cell(row, 10).value for row in range(2, reloaded.max_row + 1))
assert all(reloaded.cell(row, 11).value for row in range(2, reloaded.max_row + 1))

print(f"Generated {len(scenarios)} approved scenarios and {len(case_counts)} unique Test Case IDs")
print("By AC:", dict(Counter(scenarios[sid]["ac"] for sid in scenarios)))
print("Automation:", dict(Counter(reloaded.cell(row, 12).value for row in range(2, reloaded.max_row + 1))))
print("Intent:", dict(Counter(OUTCOME[sid][2] for sid in scenarios)))
print("Rows:", reloaded.max_row - 1)
