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

  private static final int SLEEP_TIME = 20;
  private static final int DEFAULT_FILE_SYSTEM_WAITING_TIME = 200;
  private Configuration config;
  private File dataFile;
  private File tempFile;
  private FileStateCheck canWrite = (File file) -> file.canWrite();
  private FileStateCheck isDeleted = (File file) -> !file.exists();

  /**
   * Default construtor.
   */
  public FileHelper() {
    config = new Configuration();
    dataFile = new File(config.getJsonFilePath());
    tempFile = new File(config.getJsonTempFilePath());

  }

  /**
   * Adds line to database file.
   *
   * @param lineContent line to be added.
   */
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

  /**
   * Deletes line from database file
   *
   * @param key unique key to be present at line.
   */
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

  /**
   * Checks if key is present at database file.
   *
   * @param key key unique key to be present at line.
   * @throws Exception if key is not found
   */
  private void isKeyPresent(String key) throws Exception {
    try (Stream<String> stream = Files.lines(dataFile.toPath())) {
      if ((stream.filter(line -> line.contains(key)).count()) == 0) {
        throw new IndexOutOfBoundsException();
      }
    }
  }

  /**
   * Checks and waits for file system response for a predefined time.
   *
   * @param file file which state is to be checked.
   * @param checker lambda returning state of the file  ex. isPresent, isWritable.
   * @throws Exception if file condition is not satisfied after predefined time.
   */
  private void waitForFileSystem(File file, FileStateCheck checker) throws Exception {
    int maxChecksCount = DEFAULT_FILE_SYSTEM_WAITING_TIME / SLEEP_TIME;
    int checks = 0;
    while (checker.fileState(file) && checks < maxChecksCount) {
      Thread.sleep(SLEEP_TIME);
      checks++;
    }
  }

  /**
   * Gets a line from database file containing the key.
   *
   * @param key unique key
   * @return line content containing key
   */
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

  /**
   * Gets all lines from database file.
   *
   * @return list with all lines from database file.
   */
  public ArrayList<String> getAllLines() {
    try (Stream<String> stream = Files.lines(dataFile.toPath())) {
      return stream.collect(Collectors.toCollection(ArrayList::new));
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Removes all lines from database.
   */
  public void cleanDatabase() {
    try {
      waitForFileSystem(dataFile, canWrite);
      Files.delete(dataFile.toPath());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  interface FileStateCheck {

    boolean fileState(File file);
  }
}


