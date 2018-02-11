package pl.coderstrust.database.file;

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

  int SLEEP_TIME = 20;
  int DEFAULT_FILE_SYSTEM_WAITING_TIME = 200;


  private Configuration config;
  File dataFile;
  File tempFile;
  FileStateCheck canWrite = (File file) -> file.canWrite();
  FileStateCheck isDeleted = (File file) -> !file.exists();

  public FileHelper() {
    config = new Configuration();
    dataFile = new File(config.getJsonFilePath());
    tempFile = new File(config.getJsonTempFilePath());

  }

  public void addLine(String lineContent) {
    Path dataPath = dataFile.toPath();
    lineContent += System.lineSeparator();
    OpenOption openOption;
    try {
      if (Files.exists(dataFile.toPath())) {
        openOption = StandardOpenOption.APPEND;
      } else {
        openOption = StandardOpenOption.CREATE;
      }
      Files.write(dataPath, lineContent.getBytes(), openOption);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void deleteLine(String key) {
    try (
        PrintWriter out =
            new PrintWriter(new FileWriter(tempFile));
        Stream<String> stream =
            Files.lines(dataFile.toPath())
    ) {
      isKeyPresent(key);
      stream
          .filter(line -> !line.contains(key))
          .forEach(out::println);

    } catch (Exception e) {
      e.printStackTrace();
    }
    try {
      waitForFileSystem(dataFile, canWrite);
      Files.delete(dataFile.toPath());
      waitForFileSystem(dataFile, isDeleted);
      Files.copy(tempFile.toPath(), dataFile.toPath());
      waitForFileSystem(tempFile, canWrite);
      Files.delete(tempFile.toPath());

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void isKeyPresent(String key) throws Exception {
    try (Stream<String> stream = Files.lines(dataFile.toPath())) {
      if ((stream.filter(line -> line.contains(key)).count()) == 0) {
        throw new IndexOutOfBoundsException();
      }
    }
  }

  private void waitForFileSystem(File file, FileStateCheck checker) throws Exception {
    int maxChecksCount = DEFAULT_FILE_SYSTEM_WAITING_TIME / SLEEP_TIME;
    int checks = 0;
    while (checker.FileState(file) && checks < maxChecksCount) {
      Thread.sleep(SLEEP_TIME);
      checks++;
    }
  }

  public String getLine(String key) {

    try (Stream<String> stream = Files.lines(dataFile.toPath())) {
      isKeyPresent(key);
      return stream
          .filter(line -> line.contains(key))
          .collect(Collectors.joining());
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public ArrayList<String> getAllLines() {
    try (Stream<String> stream = Files.lines(dataFile.toPath())) {
      return stream.collect(Collectors.toCollection(ArrayList::new));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  public void cleanDatabase() {
    try {
      waitForFileSystem(dataFile, canWrite);
      Files.delete(dataFile.toPath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  interface FileStateCheck {

    boolean FileState(File file);
  }
}


