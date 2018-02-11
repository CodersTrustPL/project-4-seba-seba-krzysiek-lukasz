package pl.coderstrust.database.file;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;
import pl.coderstrust.database.memory.InMemoryDatabase;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.testhelpers.TestCasesGenerator;

import java.io.File;

public class InFileDatabaseTest extends DatabaseTest {

  private static final int WAIT_TIME_FOR_FILE_SYSTEM = 200;

  private Configuration config = new Configuration();
  private InFileDatabase database = new InFileDatabase();
  private TestCasesGenerator generator = new TestCasesGenerator();

  @Override
  public Database getDatabase() {
    return new InMemoryDatabase();
  }

  @Test
  public void shouldCleanTemporaryFileAfterDeleteOperation() {
    Invoice testInvoice = generator.getTestInvoice(1, 1);
    testInvoice.setSystemId(1);
    database.addInvoice(testInvoice);
    database.deleteInvoiceById(1);
    File tempFile = new File(config.getJsonTempFilePath());

    try {
      Thread.sleep(WAIT_TIME_FOR_FILE_SYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertFalse(tempFile.exists());
  }

  @Test
  public void shouldStoreDatabaseInCorrectLocation() {
    Invoice testInvoice = generator.getTestInvoice(1, 1);
    testInvoice.setSystemId(1);
    database.addInvoice(testInvoice);
    File dataFile = new File(config.getJsonFilePath());
    try {
      Thread.sleep(WAIT_TIME_FOR_FILE_SYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(dataFile.exists());
  }

  @Test
  public void shouldRemoveDbFileAfterCleanWasCalled() {
    Invoice testInvoice = generator.getTestInvoice(1, 1);
    testInvoice.setSystemId(1);
    database.addInvoice(testInvoice);
    database.cleanDatabase();
    File dataFile = new File(config.getJsonFilePath());
    try {
      Thread.sleep(WAIT_TIME_FOR_FILE_SYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertFalse(dataFile.exists());
  }
}

