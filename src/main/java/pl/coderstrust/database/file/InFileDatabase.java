package pl.coderstrust.database.file;

import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.stream.Collectors;

public class InFileDatabase implements Database {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;

  private FileHelper fileHelper;
  private ObjectMapperHelper mapper;
  private HashSet<Long> savedIds;

  public InFileDatabase() {
    mapper = new ObjectMapperHelper();
    fileHelper = new FileHelper();
    savedIds = getIdsFromDbFile();
  }

  @Override
  public long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    fileHelper.addLine(mapper.toJson(invoice))
    ;
    savedIds.add(invoice.getId());
    return invoice.getId();
  }

  private long getNextId() {
    if (savedIds.isEmpty()) {
      return FIRST_ID;
    } else {
      return Collections.max(savedIds) + INCREMENT_ID;
    }
  }

  @Override
  public void deleteInvoice(long systemId) {
    if (!idExist(systemId)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO change to logging;
    } else {
      fileHelper.deleteLine(idToLineKey(systemId));
      savedIds.remove(systemId);
    }
  }

  @Override
  public Invoice getInvoiceById(long systemId) {
    if (!idExist(systemId)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
      //TODO change to logging;
    } else {

      String jsonInvoice = fileHelper.getLine(idToLineKey(systemId));
      return mapper.toInvoice(jsonInvoice);
    }
  }

  String idToLineKey(long systemId) {
    return "\"id\":" + String.valueOf(systemId) + ",";
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    deleteInvoice(invoice.getId());
    fileHelper.addLine(mapper.toJson(invoice));
    savedIds.add(invoice.getId());
  }

  @Override
  public ArrayList<Invoice> getInvoices() {
    return fileHelper.getAllLines().stream()
        .map(line -> mapper.toInvoice(line))
        .collect(Collectors.toCollection(ArrayList::new));
  }

  @Override
  public boolean idExist(long id) {
    return savedIds.contains(id);
  }

  private HashSet<Long> getIdsFromDbFile() {
    return getInvoices().stream()
        .map(invoice -> invoice.getId())
        .collect(Collectors.toCollection(HashSet::new));
  }
}