package pl.coderstrust.database.file;

import pl.coderstrust.database.memory.InvoiceProcessingException;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.OpenOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {

  private static final int UNIT_SLEEP_TIME = 20;
  private static final int DEFAULT_FILE_SYSTEM_WAITING_TIME = 200;
  private Configuration dbConfig;
  private File dbFile;
  private File tempFile;
  private FileStateCheck canWrite = (File file) -> file.canWrite();
  private FileStateCheck isDeleted = (File file) -> !file.exists();


  public FileHelper() {
    dbConfig = new Configuration();
    dbFile = new File(dbConfig.getJsonFilePath());
    tempFile = new File(dbConfig.getJsonTempFilePath());

  }

  /**
   * Adds line to database file.
   *
   * @param lineContent line to be added.
   */
  public void addLine(String lineContent) {
    Path dbFilePath = dbFile.toPath();
    lineContent += System.lineSeparator();
    try {
      Files.write(dbFilePath, lineContent.getBytes(), getFileOpenOption(dbFile));
    } catch (IOException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error during adding invoice. Try again later");
      //TODO change to logging
    }
  }

  private OpenOption getFileOpenOption(File fileToCheck) {
    if (Files.exists(fileToCheck.toPath())) {
      return StandardOpenOption.APPEND;
    } else {
      return StandardOpenOption.CREATE;
    }
  }

  /**
   * Deletes line from database file
   *
   * @param lineKey unique key to be present at line.
   */
  public void deleteLine(String lineKey) {
    try (
        PrintWriter printWriterTempFile =
            new PrintWriter(new FileWriter(tempFile));
        Stream<String> stream =
            Files.lines(dbFile.toPath())
    ) {
      if (!isKeyPresent(lineKey)) {
        throw new IndexOutOfBoundsException();
      }
      stream
          .filter(line -> !line.contains(lineKey))
          .forEach(printWriterTempFile::println);

    } catch (IOException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error during deleting invoice. Try again later");
      //TODO change to logging;;
    } catch (IndexOutOfBoundsException e) {
      throw new InvoiceProcessingException("Invoice designated to delete does not exist.");
      //TODO change to logging;;
    }
    try {
      waitForFileSystem(dbFile, canWrite);
      Files.delete(dbFile.toPath());
      waitForFileSystem(dbFile, isDeleted);
      Files.copy(tempFile.toPath(), dbFile.toPath());
      waitForFileSystem(tempFile, canWrite);
      Files.delete(tempFile.toPath());
    } catch (IOException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error during deleting invoice. Try again later");
      //TODO change to logging;
    } catch (InterruptedException e) {
      throw new InvoiceProcessingException("Invoice processing interrupted. Try again later.");
      //TODO change to logging;
    }
  }

  /**
   * Checks if key is present at database file.
   *
   * @param lineKey key unique key to be present at line.
   * @throws Exception if key is not found
   */
  private boolean isKeyPresent(String lineKey) throws IOException {
    try (Stream<String> stream = Files.lines(dbFile.toPath())) {
      return !((stream.filter(line -> line.contains(lineKey)).count()) == 0);
    }
  }

  /**
   * Checks and waits for file system response for a predefined time.
   *
   * @param checkedFile file which state is to be checked.
   * @param stateChecker lambda returning state of the file  ex. isPresent, isWritable.
   * @throws Exception if file condition is not satisfied after predefined time.
   */
  private void waitForFileSystem(File checkedFile, FileStateCheck stateChecker)
      throws InterruptedException {
    int maxChecksCount = DEFAULT_FILE_SYSTEM_WAITING_TIME / UNIT_SLEEP_TIME;
    int checkNumber = 0;
    while (stateChecker.fileState(checkedFile) && checkNumber < maxChecksCount) {
      Thread.sleep(UNIT_SLEEP_TIME);
      checkNumber++;
    }
  }

  /**
   * Gets a line from database file containing the key.
   *
   * @param lineKey unique key
   * @return line content containing key
   */
  public String getLine(String lineKey) {
    try (Stream<String> dbStream = Files.lines(dbFile.toPath())) {
      if (!isKeyPresent(lineKey)) {
        throw new IndexOutOfBoundsException();
      }
      return dbStream
          .filter(line -> line.contains(lineKey))
          .collect(Collectors.joining());
    } catch (IndexOutOfBoundsException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error while retrieving invoice. Try again later");
      //TODO change to logging;
    } catch (IOException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error during deleting invoice. Try again later");
      //TODO change to logging;
    }
  }

  /**
   * Gets all lines from database file.
   *
   * @return list with all lines from database file.
   */
  public ArrayList<String> getAllLines() {
    try (Stream<String> dbStream = Files.lines(dbFile.toPath())) {
      return dbStream.collect(Collectors.toCollection(ArrayList::new));
    } catch (IOException e) {
      throw new InvoiceProcessingException(
          "Filesystem I/O error while retrieving. Try again later");
      //TODO change to logging;
    }
  }

  interface FileStateCheck {

    boolean fileState(File file);
  }
}