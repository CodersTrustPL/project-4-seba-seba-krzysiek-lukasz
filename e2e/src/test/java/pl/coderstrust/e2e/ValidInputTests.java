package pl.coderstrust.e2e;

import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;


public class ValidInputTests {

  private TestsConfiguration config = new TestsConfiguration();
  private TestCasesGenerator generator = new TestCasesGenerator();
  private ObjectMapperHelper mapper = new ObjectMapperHelper();
  private LocalDate currentDate = LocalDate.now();
  private Invoice testInvoice;
  private ArrayList<Invoice> testInvoices = new ArrayList<>();
  private Pattern extractIntFromString = Pattern.compile(config.getIntFromStringRegexPattern());

  @BeforeClass
  public void setupClass() {
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      testInvoices.add(generator.getTestInvoice(i + 1,
          config.getDefaultEntriesCount()));
      testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
      testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
    }
  }

  @BeforeMethod
  public void setupMethod() {
    currentDate = LocalDate.now();
    testInvoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
  }

  @Test
  public void shouldReturnCorrectStatusCodeWhenServiceIsUp() {
    given()
        .when()
        .get("")

        .then()
        .statusCode(config.getServerOkStatusCode());
  }

  @Test(groups={"ValidInputTests.shouldCorrectlyAddAndGetInvoiceById"})
  public void shouldCorrectlyAddAndGetInvoiceById() {
    long invoiceId = addInvoice(testInvoice);
    testInvoice.setId(invoiceId);
    given()
        .when()
        .get("/" + invoiceId).

        then()
        .assertThat()
        .body(equalTo(mapper.toJson(testInvoice)));
  }

  long addInvoice(Invoice testInvoice) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post("");
    return getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  long getInvoiceIdFromServiceResponse(String response) {
    Matcher matcher = extractIntFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  @Test(groups={"ValidInputTests.shouldCorrectlyUpdateInvoice"})
  public void shouldCorrectlyUpdateInvoice() {
    long invoiceId = addInvoice(testInvoice);
    Invoice updatedInvoice = generator.getTestInvoice(
        config.getDefaultTestInvoiceNumber() + 1, config.getDefaultEntriesCount());
    updatedInvoice.setId(invoiceId);
    given()
        .contentType("application/json")
        .body(updatedInvoice)

        .when()
        .put("/" + invoiceId);

    given()
        .when()
        .get("/" + invoiceId)

        .then()
        .assertThat()
        .body(equalTo(mapper.toJson(updatedInvoice)));
  }

  @Test(groups={"ValidInputTests.shouldCorrectlyDeleteInvoiceById"})
  public void shouldCorrectlyDeleteInvoiceById() {
    long invoiceId = addInvoice(testInvoice);
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .delete("/" + invoiceId);

    given()
        .when()
        .get("/" + invoiceId)

        .then()
        .assertThat()
        .body(equalTo(""));
  }

  @Test(groups={"ValidInputTests.shouldAddSeveralInvoicesAndReturnCorrectMessage"})
  public void shouldAddSeveralInvoicesAndReturnCorrectMessage() {
    for (Invoice invoice : testInvoices) {
      given()
          .contentType("application/json")
          .body(invoice)

          .when()
          .post("")

          .then()
          .assertThat()
          .body(containsString("Invoice added under id :"));
    }
  }

  @Test(dataProvider = "validDates")
  public void shouldAddSeveralInvoicesAndFindThemByIssueDate(LocalDate newDate) {
    int invoicesAtDateCount = getInvoicesCountForDateRange(newDate, newDate);
    testInvoice.setIssueDate(newDate);
    given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post("");

    int invoicesAdded = getInvoicesCountForDateRange(newDate, newDate) - invoicesAtDateCount;
    Assert.assertEquals(invoicesAdded, 1);
  }

  @DataProvider(name = "validDates")
  Object[] validDatesProvider() {
    Object[] validDates = new Object[10];
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      validDates[i] = LocalDate.now().plusYears(i);
    }
    return validDates;
  }

  private int getInvoicesCountForDateRange(LocalDate dateFrom, LocalDate dateTo) {
    String path = "/" + dateFrom + "/" + dateTo;
    String response = given()
        .body(path)
        .get("")
        .body().print();
    return mapper.toInvoiceList(response).size();
  }
}


