package pl.coderstrust.e2e.performanceTests;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.ValidInputTests;
import pl.coderstrust.e2e.model.Invoice;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static io.restassured.RestAssured.given;

public class PerformanceTests extends ValidInputTests {

    private TestsConfiguration config = new TestsConfiguration();
    private ObjectMapperHelper mapper = new ObjectMapperHelper();
    private ValidInputTests validInputTests = new ValidInputTests();
    private int THREADS_NUMBER = 5;


    @BeforeClass
    public void setupClass() {
        validInputTests.setupClass();
        validInputTests.setupMethod();
    }


    @Test(dependsOnGroups = {"ValidInputTests.shouldCorrectlyAddAndGetInvoiceById"})
    public void shouldCorrectlyAddAndGetInvoiceByIdInThreads() {
        Runnable test = () -> validInputTests.shouldCorrectlyAddAndGetInvoiceById();
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }


    @Test(dependsOnGroups = {"ValidInputTests.shouldAddSeveralInvoicesAndReturnCorrectMessage"})
    public void shouldAddSeveralInvoicesAndReturnCorrectMessageInThreads() {
        Runnable test = () -> validInputTests.shouldAddSeveralInvoicesAndReturnCorrectMessage();
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }


    @Test(dependsOnGroups = {"ValidInputTests.shouldCorrectlyDeleteInvoiceById"})
    public void shouldCorrectlyDeleteInvoiceByIdInThreads() {
        Runnable test = () -> validInputTests.shouldCorrectlyDeleteInvoiceById();
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }


    @Test(dependsOnGroups = {"ValidInputTests.shouldCorrectlyUpdateInvoice"})
    public void shouldCorrectlyUpdateInvoiceInThreads() {
        Runnable test = () -> validInputTests.shouldCorrectlyUpdateInvoice();
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }


    @Test(dataProvider = "validDates", dependsOnGroups = {"ValidInputTests.shouldAddSeveralInvoicesAndFindThemByIssueDate"})
    public void shouldAddSeveralInvoicesAndFindThemByIssueDateInThreads(LocalDate newDate) {
        Runnable test = () -> validInputTests.shouldAddSeveralInvoicesAndFindThemByIssueDate(newDate);
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }

    @Test
    public void shouldAddSeveralInvoicesInThreadsAndCheckDatabaseSize() {
        int databaseStartSize = getDatabaseSize();
        Runnable test = () -> validInputTests.shouldAddSeveralInvoicesAndReturnCorrectMessage();
        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(THREADS_NUMBER);
        for (int i = 0; i < THREADS_NUMBER; i++) {
            newFixedThreadPool.submit(test);
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();

        int databaseEndSize = getDatabaseSize();
        int expectedDatabaseSize = databaseStartSize + 50;
        org.testng.Assert.assertEquals(expectedDatabaseSize, databaseEndSize);
    }

    @Test
    public void shouldCheckDatabaseSizeAndNumberOfIds() {
        int databaseSize = getDatabaseSize();
        String path = config.getBaseUri() + config.getBasePort();
        String response = given()
                .body(path)
                .get("")
                .body().print();
        List<Invoice> invoicesFromDatabase = mapper.toInvoiceList(response);
        Set ids = new TreeSet();
        for (Invoice invoice : invoicesFromDatabase) {
            ids.add(invoice.getId());
        }
        int idsCount = ids.size();
        org.testng.Assert.assertEquals(databaseSize, idsCount);
    }

    public int getDatabaseSize() {
        String path = config.getBaseUri() + config.getBasePort();
        String response = given()
                .body(path)
                .get("")
                .body().print();
        return mapper.toInvoiceList(response).size();
    }

    @DataProvider(name = "validDates")
    Object[] validDatesProvider() {
        Object[] validDates = new Object[10];
        for (int i = 0; i < config.getTestInvoicesCount(); i++) {
            validDates[i] = LocalDate.now().plusYears(i);
        }
        return validDates;
    }
}