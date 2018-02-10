package pl.coderstrust.database.inFileDatabase;

import java.io.File;
@FunctionalInterface
public interface FileStateChecker {

  boolean fileState(File file);


}
