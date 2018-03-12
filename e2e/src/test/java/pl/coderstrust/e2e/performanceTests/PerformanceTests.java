package pl.coderstrust.e2e.performanceTests;

import org.junit.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.TestCasesGenerator;

import java.time.LocalDate;
import java.util.ArrayList;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;

public class PerformanceTests implements Runnable {

    private TestsConfiguration config = new TestsConfiguration();
    private TestCasesGenerator generator = new TestCasesGenerator();
    private LocalDate currentDate = LocalDate.now();
    private Invoice testInvoice;
    private ArrayList<Invoice> testInvoices = new ArrayList<>();


    @Override
    @Test
    public void run() {

        currentDate = LocalDate.now();
        testInvoice = generator
                .getTestInvoice(config.getDefaultTestInvoiceNumber(), config.getDefaultEntriesCount());

        synchronized (testInvoice) {
            for (int i = 0; i < config.getTestInvoicesCount(); i++) {
                testInvoices.add(generator.getTestInvoice(i + 1,
                        config.getDefaultEntriesCount()));
                testInvoices.get(i).setIssueDate(currentDate.plusYears(i));
                testInvoices.get(i).setPaymentDate(currentDate.plusYears(i).plusDays(15));
            }

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
    }
}
