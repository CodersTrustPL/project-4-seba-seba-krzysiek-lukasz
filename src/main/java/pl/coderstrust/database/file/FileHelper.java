package pl.coderstrust.database.file;

import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class FileHelper {

  private static final int UNIT_SLEEP_TIME = 20;
  private Configuration dbConfig;
  private File dbFile;
  private File tempFile;
  private FileStateCheck canWrite = (File file) -> file.canWrite();
  private FileStateCheck isDeleted = (File file) -> !file.exists();

  public FileHelper() {
    dbConfig = new Configuration();
    dbFile = new File(dbConfig.getJsonFilePath());
    tempFile = new File(dbConfig.getJsonTempFilePath());
    initializeDatabaseFile();

  }

  private void initializeDatabaseFile() {
    if (!dbFile.exists()) {
      try {
        dbFile.createNewFile();
      } catch (IOException e) {
        throw new DbException(
            ExceptionMsg.IO_ERROR_WHILE_INITIALIZING);
      }
    }
  }

  public void addLine(String lineContent) {
    lineContent += System.lineSeparator();
    try {
      Files.write(dbFile.toPath(), lineContent.getBytes(), StandardOpenOption.APPEND);
    } catch (IOException e) {
      throw new DbException(
          ExceptionMsg.IO_ERROR_WHILE_ADDING);
      //TODO change to logging
    }
  }

  public void deleteLine(String lineKey) {
    try {
      deleteLineAndSaveToTempFile(lineKey);
      updateDatabaseFromTempFile();
    } catch (InterruptedException e) {
      throw new DbException(ExceptionMsg.INVOICE_PROCESSING_INTERRUPT);
      //TODO change to logging;
    } catch (IOException e) {
      throw new DbException(ExceptionMsg.IO_ERROR_WHILE_DELETING);
      //TODO change to logging;
    }
  }

  private void deleteLineAndSaveToTempFile(String lineKey) throws IOException {
    try (
        Stream<String> inputStream = Files.lines(dbFile.toPath());
        PrintWriter tempFileWriter = new PrintWriter(new FileWriter(tempFile))
    ) {
      inputStream
          .filter(line -> !line.contains(lineKey))
          .forEach(tempFileWriter::println);
    }
  }

  private void updateDatabaseFromTempFile() throws IOException, InterruptedException {
    waitForFileSystem(dbFile, canWrite);
    Files.delete(dbFile.toPath());
    waitForFileSystem(dbFile, isDeleted);
    Files.copy(tempFile.toPath(), dbFile.toPath());
    waitForFileSystem(tempFile, canWrite);
    Files.delete(tempFile.toPath());
  }

  private void waitForFileSystem(File checkedFile, FileStateCheck stateChecker)
      throws InterruptedException {
    int maxChecksCount = dbConfig.getFileSystemWaitTime() / UNIT_SLEEP_TIME;
    int checkNumber = 0;
    while (!stateChecker.fileState(checkedFile) && checkNumber < maxChecksCount) {
      Thread.sleep(UNIT_SLEEP_TIME);
      checkNumber++;
    }
  }

  public String getLine(String lineKey) {
    try (Stream<String> dbStream = Files.lines(dbFile.toPath())) {
      String lineFound = dbStream
          .filter(line -> line.contains(lineKey))
          .collect(Collectors.joining());
      return lineFound;
    } catch (IOException e) {
      throw new DbException(
          ExceptionMsg.IO_ERROR_WHILE_READING);
      //TODO change to logging;
    }
  }

  public ArrayList<String> getAllLines() {
    try (Stream<String> dbStream = Files.lines(dbFile.toPath())) {
      return dbStream.collect(Collectors.toCollection(ArrayList::new));
    } catch (IOException e) {
      throw new DbException(
          ExceptionMsg.IO_ERROR_WHILE_READING);
      //TODO change to logging;
    }
  }

  interface FileStateCheck {

    boolean fileState(File file);
  }
}