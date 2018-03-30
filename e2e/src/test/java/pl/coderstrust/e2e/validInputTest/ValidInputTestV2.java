package pl.coderstrust.e2e.validInputTest;

import static io.restassured.RestAssured.given;

import io.restassured.response.Response;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import pl.coderstrust.e2e.model.Company;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.TestUtils;

import java.time.LocalDate;

public class ValidInputTestV2 extends AbstractValidInputTests {

  private long testBuyerId;

  @BeforeClass
  public void setupClass() {
    for (int i = 0; i < config.getTestInvoicesCount(); i++) {
      testInvoices.add(generator.getTestInvoice(i + 1,
          config.getDefaultEntriesCount()));
      testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
      testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
      testInvoices.get(i).getSeller()
          .setId(TestUtils.registerCompany(testInvoices.get(i).getSeller()));
    }
  }

  @BeforeMethod
  public void setupMethod() {
    Company testBuyer;
    Company testSeller;
    long testSellerId;
    currentDate = LocalDate.now();
    testInvoice = generator
        .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());
    testSeller = testInvoice.getSeller();
    testBuyer = testInvoice.getBuyer();
    testSellerId = TestUtils.registerCompany(testSeller);
    testBuyerId = TestUtils.registerCompany(testBuyer);
    testSeller.setId(testSellerId);
    testBuyer.setId(testBuyerId);
  }

  @Override
  protected String getInvoicePath() {
    return TestUtils.getV2InvoicePath(testBuyerId);
  }

  @Override
  protected long addInvoice(Invoice testInvoice) {
    Response ServiceResponse = given()
        .contentType("application/json")
        .body(testInvoice)
        .when()
        .post(getInvoicePath());
    return TestUtils.getInvoiceIdFromServiceResponse(ServiceResponse.print());
  }

  @Override
  protected String getInvoicePathWithInvoiceId(long invoiceId) {
    return TestUtils.getV2InvoicePathWithInvoiceId(testBuyerId, invoiceId);
  }

  @Override
  protected String getInvoicePathWithDateRange(LocalDate dateFrom, LocalDate dateTo) {
    return TestUtils.getV2InvoicePath(testBuyerId) + "?startDate=" + dateFrom + "&endDate="
        + dateTo;
  }
}