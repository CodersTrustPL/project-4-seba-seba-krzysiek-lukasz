package pl.coderstrust.e2e.performanceTests;

import org.junit.Test;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class PerformanceTestsRunner {

    @Test
    public void shouldAddInvoicesInMultiThreads() {

        final ExecutorService newFixedThreadPool = Executors.newFixedThreadPool(3);
        for (int i = 0; i <= 3; i++) {
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
    public void shouldCheckAllAddedInvoices() {


    }
}