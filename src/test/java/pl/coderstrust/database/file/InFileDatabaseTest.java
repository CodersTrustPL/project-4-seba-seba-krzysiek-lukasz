package pl.coderstrust.database.file;

import static junit.framework.TestCase.assertFalse;
import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DatabaseTest;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class InFileDatabaseTest extends DatabaseTest {

  private static final int WAIT_TIME_FOR_FILESYSTEM = 2000;
  private Configuration config = new Configuration();
  private FileHelper fileHelper = new FileHelper();
  private File dataFile = new File(config.getJsonFilePath());

  @Override
  public Database getCleanDatabase() {
    File dbFile = new File(config.getJsonFilePath());
    if (dbFile.exists()) {
      try {
        Files.delete(dbFile.toPath());
        Files.createFile(dbFile.toPath());
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
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
  public void shouldAddOneLineToDbFile(){
    //given
    getCleanDatabase();
    //when
    fileHelper.addLine("test line1");
    fileHelper.addLine("test line2");

    //then
    ArrayList<String> fileContent = getFileContent(dataFile);
    assertEquals("test line1test line2",String.join("",fileContent));

  }

  @Test
  public void shouldRemoveOneLineFromDbFile(){
    //given
    getCleanDatabase();
    //when
    fileHelper.addLine("test line1");
    fileHelper.addLine("test line2");
    fileHelper.deleteLine("test line2");

    //then
    ArrayList<String> fileContent = getFileContent(dataFile);
    assertEquals("test line1",String.join("",fileContent));

  }

  @Test
  public void shouldGetOneLineFromDbFile(){
    getCleanDatabase();
    //when
    fileHelper.addLine("test line1");
    fileHelper.addLine("test line2");
    String output = fileHelper.getLine("test line2");

    //then
    assertEquals("test line2",output);

  }
  @Test
  public void shouldReturnAllLinesAtDbFile(){
    getCleanDatabase();

    //when
    fileHelper.addLine("test line1");
    fileHelper.addLine("test line2");
    ArrayList output = fileHelper.getAllLines();

    //then
    ArrayList<String> fileContent = getFileContent(dataFile);
    assertArrayEquals(fileContent.toArray(),output.toArray());

  }

  ArrayList<String> getFileContent(File file) {
    try (Stream<String> dbStream = Files.lines(file.toPath())) {
      return dbStream
          .collect(Collectors.toCollection(ArrayList::new));
    } catch (IOException e) {
      e.printStackTrace();
    }
    return new ArrayList<>();
  }
}
