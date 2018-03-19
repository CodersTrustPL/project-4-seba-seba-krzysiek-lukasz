package pl.coderstrust.e2e.performanceTests;


import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.TestsConfiguration;
import pl.coderstrust.e2e.ValidInputTests;
import pl.coderstrust.e2e.testHelpers.ObjectMapperHelper;

import java.time.LocalDate;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTests extends ValidInputTests{

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
        Runnable test = new Runnable() {
            @Override
            public void run() {
                validInputTests.shouldCorrectlyAddAndGetInvoiceById();
            }
        };
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
        Runnable test = new Runnable() {
            @Override
            public void run() {
                validInputTests.shouldAddSeveralInvoicesAndReturnCorrectMessage();
            }
        };
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
        Runnable test = new Runnable() {
            @Override
            public void run() {
                validInputTests.shouldCorrectlyDeleteInvoiceById();
            }
        };
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
        Runnable test = new Runnable() {
            @Override
            public void run() {
                validInputTests.shouldCorrectlyUpdateInvoice();
            }
        };
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
        Runnable test = new Runnable() {
            @Override
            public void run() {
                validInputTests.shouldAddSeveralInvoicesAndFindThemByIssueDate(newDate);
            }
        };
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


    @DataProvider(name = "validDates")
    Object[] validDatesProvider() {
        Object[] validDates = new Object[10];
        for (int i = 0; i < config.getTestInvoicesCount(); i++) {
            validDates[i] = LocalDate.now().plusYears(i);
        }
        return validDates;
    }
}