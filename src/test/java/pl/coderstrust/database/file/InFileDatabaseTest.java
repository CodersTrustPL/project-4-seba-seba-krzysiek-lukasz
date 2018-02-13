package pl.coderstrust.database.file;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

import java.io.File;

public class InFileDatabaseTest extends DatabaseTest {

  private static final int WAIT_TIME_FOR_FILESYSTEM = 2000;
  private Configuration config = new Configuration();

  @Override
  public Database getDatabase() {
    return new InFileDatabase();
  }

  @Test
  public void shouldCleanTemporaryFileAfterDeleteOperation() {
    database.addInvoice(testInvoice);
    database.deleteInvoiceById(1);
    File tempFile = new File(config.getJsonTempFilePath());

    try {
      Thread.sleep(WAIT_TIME_FOR_FILESYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertFalse(tempFile.exists());
  }

  @Test
  public void shouldStoreDatabaseInCorrectLocation() {
    database.addInvoice(testInvoice);
    File dataFile = new File(config.getJsonFilePath());
    try {
      Thread.sleep(WAIT_TIME_FOR_FILESYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertTrue(dataFile.exists());
  }

  @Test
  public void shouldReturnEmptyDbFileAfterCleanWasCalled() {
    database.addInvoice(testInvoice);
    database.cleanDatabase();
    File dataFile = new File(config.getJsonFilePath());
    try {
      Thread.sleep(WAIT_TIME_FOR_FILESYSTEM);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    assertEquals(0, dataFile.length());
  }
}




