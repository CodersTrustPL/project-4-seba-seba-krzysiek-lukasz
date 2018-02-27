package pl.coderstrust.database.file;

public class Configuration {

  private static final String jsonFilePath = "src/main/resources/database.json";
  private static final String jsonTempFilePath = "src/main/resources/temporary.json";
  private static final int fileSystemWaitTimeMs = 2000;
  private static final int UNIT_SLEEP_TIME_MS = 20;

  public String getJsonFilePath() {
    return jsonFilePath;
  }

  public String getJsonTempFilePath() {
    return jsonTempFilePath;
  }

  public int getFileSystemWaitTimeMs() {
    return fileSystemWaitTimeMs;
  }

  public int getUnitSleepTimeMs() {
    return UNIT_SLEEP_TIME_MS;
  }
}