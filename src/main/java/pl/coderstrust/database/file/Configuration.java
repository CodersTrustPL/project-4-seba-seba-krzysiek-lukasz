package pl.coderstrust.database.file;

public class Configuration {

  private static final String DEFAULT_JSON_FILE_PATH = "src\\main\\resources\\database.json";
  private static final String DEFAULT_TEMP_JSON_FILE_PATH = "src\\main\\resources\\temporary.json";
  private static final int DEFAULT_FILE_SYSTEM_WAITING_TIME = 200;

  private String jsonFilePath = DEFAULT_JSON_FILE_PATH;
  private String jsonTempFilePath = DEFAULT_TEMP_JSON_FILE_PATH;
  private int fileSystemWaitTime = DEFAULT_FILE_SYSTEM_WAITING_TIME;

  public String getJsonFilePath() {
    return jsonFilePath;
  }

  public void setJsonFilePath(String jsonFilePath) {
    this.jsonFilePath = jsonFilePath;
  }

  public String getJsonTempFilePath() {
    return jsonTempFilePath;
  }

  public void setJsonTempFilePath(String jsonTempFilePath) {
    this.jsonTempFilePath = jsonTempFilePath;
  }

  public int getFileSystemWaitTime() {
    return fileSystemWaitTime;
  }

  public void setFileSystemWaitTime(int fileSystemWaitTime) {
    this.fileSystemWaitTime = fileSystemWaitTime;
  }
}