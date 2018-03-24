package pl.coderstrust.database.file;

import static pl.coderstrust.database.ExceptionMsg.*;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;


public class InFileDatabase<T extends WithNameIdIssueDate> implements Database<T> {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;
  private final Logger logger = LoggerFactory.getLogger(InFileDatabase.class);

  private FileHelper fileHelper;
  private ObjectMapperHelper mapper;
  private HashSet<Long> savedIds;

  public InFileDatabase(Class<T> entryClass) {
    mapper = new ObjectMapperHelper(entryClass);
    fileHelper = new FileHelper(new Configuration(entryClass.getSimpleName()));
    savedIds = getIdsFromDbFile();
  }

  @Override
  public long addEntry(T entry) {
    entry.setId(getNextId());
    fileHelper.addLine(mapper.toJson(entry));
    savedIds.add(entry.getId());
    return entry.getId();
  }

  private long getNextId() {
    if (savedIds.isEmpty()) {
      return FIRST_ID;
    } else {
      return Collections.max(savedIds) + INCREMENT_ID;
    }
  }

  @Override
  public void deleteEntry(long systemId) {
    if (!idExist(systemId)) {
      logger.warn(" from deleteEntry (InFileDatabase): "
          + INVOICE_NOT_EXIST);
      throw new DbException(INVOICE_NOT_EXIST);
    } else {
      fileHelper.deleteLine(idToLineKey(systemId));
      savedIds.remove(systemId);
    }
  }

  @Override
  public T getEntryById(long systemId) {
    if (!idExist(systemId)) {
      logger.warn(" from getEntryById (InFileDatabase): "
          +INVOICE_NOT_EXIST);
      throw new DbException(INVOICE_NOT_EXIST);
    } else {
      String jsonEntry = fileHelper.getLine(idToLineKey(systemId));
      return (T) mapper.toObject(jsonEntry); //TODO can this be avoided unchecked cast?
    }
  }

  String idToLineKey(long systemId) {
    return "\"id\":" + String.valueOf(systemId) + ",";
  }

  @Override
  public void updateEntry(WithNameIdIssueDate entry) {
    deleteEntry(entry.getId());
    fileHelper.addLine(mapper.toJson(entry));
    savedIds.add(entry.getId());
  }

  @Override
  public List<T> getEntries() {
    return fileHelper.getAllLines().stream()
        .map(line -> (T) mapper.toObject(line))
        .collect(Collectors.toCollection(ArrayList::new));
    //TODO can this be avoided? unchecked cast
  }

  @Override
  public boolean idExist(long id) {
    return savedIds.contains(id);
  }

  private HashSet<Long> getIdsFromDbFile() {
    return getEntries().stream()
        .map(invoice -> invoice.getId())
        .collect(Collectors.toCollection(HashSet::new));
  }
}