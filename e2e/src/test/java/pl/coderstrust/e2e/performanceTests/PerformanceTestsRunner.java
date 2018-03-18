package pl.coderstrust.e2e.performanceTests;



import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import pl.coderstrust.e2e.ValidInputTests;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTestsRunner extends ValidInputTests{

    public PerformanceTestsRunner() {
        this.validInputTests = new ValidInputTests();
    }

    private ValidInputTests validInputTests;
    private int THREADS_NUMBER = 10;


    @BeforeClass
    public void setupClass() {
        validInputTests.setupClass();
        validInputTests.setupMethod();
    }

    @Test
    public void shouldAddInvoicesIn5Threads() {

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 5; i++) {
            newFixedThreadPool.execute(new PerformanceTests());
        }
        newFixedThreadPool.shutdown();
        try {
            newFixedThreadPool.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        newFixedThreadPool.shutdown();
    }

    @Test//(dependsOnGroups = {"PerformanceTests.run"})
    public void shouldAddInvoicesIn10Threads() {

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            newFixedThreadPool.execute(new PerformanceTests());
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

    @Test
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

    @Test
    public void shouldAdshouldCorrectlyDeleteInvoiceByIdInThreads() {
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

    @Test
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
}

