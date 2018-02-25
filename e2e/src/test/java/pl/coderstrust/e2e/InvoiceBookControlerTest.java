package pl.coderstrust.e2e;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.model.InvoiceEntry;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class InvoiceBookControlerTest {

  private TestCasesGenerator generator = new TestCasesGenerator();
  private ObjectMapperHelper mapper = new ObjectMapperHelper();
  private LocalDate currentDate;
  private Invoice testInvoice;
  private Pattern intFromString;

  @BeforeClass
  public void setupClass() {
    String port = System.getProperty("server.port");
    if (port == null) {
      RestAssured.port = Integer.valueOf(8080);
    } else {
      RestAssured.port = Integer.valueOf(port);
    }

    String basePath = System.getProperty("server.base");
    if (basePath == null) {
      basePath = "/invoice/";
    }
    RestAssured.basePath = basePath;

    String baseHost = System.getProperty("server.host");
    if (baseHost == null) {
      baseHost = "http://localhost";
    }
    RestAssured.baseURI = baseHost;
    intFromString = Pattern.compile("([0-9])+");

  }

  @BeforeMethod
  public void setupMethod() {
    currentDate = LocalDate.now();
    testInvoice = generator.getTestInvoice(1, 1);
  }

  @Test
  public void shouldReturnCorrectStatusCode() {
    given()
        .when().get("")
        .then()
        .statusCode(200);
  }

  @Test
  public void shouldCorrectlyAddAndGetInvoice() {
    Response response = given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post("");

    long invoiceId = getInvoiceIdFromServerResponse(response.print());

    testInvoice.setId(invoiceId);
    given().
        when().
        get("/" + invoiceId).

        then().
        assertThat().
        body(equalTo(mapper.toJson(testInvoice)));
  }

  long getInvoiceIdFromServerResponse(String response){
    Matcher matcher = intFromString.matcher(response);
    matcher.find();
    return Long.parseLong(matcher.group(0));
  }

  @Test
  public void shouldCorrectlyAdd10Invoices() {
    ArrayList<Invoice> invoices = new ArrayList<>();
    for (int i = 0; i < 10; i++) {
      invoices.add(generator.getTestInvoice(i + 1, 1));
      invoices.get(i).setIssueDate(currentDate.plusYears(i));
      invoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));

      given().
          contentType("application/json").
          body(invoices.get(i)).

          when().
          post("").

          then().
          assertThat().
          body(containsString("Invoice added under id :"));
    }
  }

  @Test
  public void shouldRejectAddingInvoiceIssuedBeforeCurrentDate() {
    testInvoice.setIssueDate(currentDate.minusDays(1));

    given().
        contentType("application/json").
        body(testInvoice).

        when().
        post("").

        then().
        assertThat().
        body(equalTo("[\"Date is earlier then actual date.\"]"));
  }

  @Test
  public void shouldRejectAddingInvoiceWithProductListEmpty() {
    testInvoice.setProducts(new ArrayList<InvoiceEntry>());

    given().
        contentType("application/json").
        body(testInvoice).

        when().
        post("").

        then().
        assertThat().
        body(equalTo("[\"Products list is empty.\"]"));

  }

  @Test
  public void shouldRejectAddingInvoiceWithCompanyNameEmpty() {
    Company company = generator.getTestCompany(1, "sample_Prefix");
    company.setName("");
    testInvoice.setBuyer(company);

    given().
        contentType("application/json").
        body(testInvoice).

        when().
        post("").

        then().
        assertThat().
        body(equalTo("[\"Company name is empty.\"]"));
  }
}


