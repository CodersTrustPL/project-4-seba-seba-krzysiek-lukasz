package pl.coderstrust.database.multifile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.DbException;
import pl.coderstrust.database.ExceptionMsg;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@ConditionalOnProperty(name = "pl.coderstrust.database.Database", havingValue = "multiFile")
public class MultiFileDatabase implements Database {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;

  private ObjectMapperHelper objectMapper;
  private FileHelper fileHelper;
  private FileCache fileCache;
  private PathSelector pathSelector;

  @Autowired
  public MultiFileDatabase(ObjectMapperHelper objectMapper,
      FileHelper fileHelper, FileCache fileCache,
      PathSelector pathSelector) {
    this.objectMapper = objectMapper;
    this.fileHelper = fileHelper;
    this.fileCache = fileCache;
    this.pathSelector = pathSelector;
  }

  @Override
  public synchronized long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    fileHelper.addLine(objectMapper.toJson(invoice), invoice);
    fileCache.getCache().put(invoice.getId(), pathSelector.getFilePath(invoice));
    return invoice.getId();
  }

  private long getNextId() {
    return fileCache.getCache().isEmpty() ? FIRST_ID :
        (long) Collections.max(fileCache.getCache().keySet()) + INCREMENT_ID;
  }

  @Override
  public void deleteInvoice(long id) {
    if (!idExist(id)) {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    } else {
      fileHelper.deleteLine(id);
      fileCache.getCache().remove(id);
    }
  }

  @Override
  public Invoice getInvoiceById(long id) {
    Invoice invoice;
    if (fileCache.getCache().containsKey(id)) {
      invoice = objectMapper.toInvoice(fileHelper.getLine(id));
    } else {
      throw new DbException(ExceptionMsg.INVOICE_NOT_EXIST);
    }
    return invoice;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    if (fileCache.getCache().containsKey(invoice.getId())) {
      deleteInvoice(invoice.getId());
      fileHelper.addLine(objectMapper.toJson(invoice), invoice);
      fileCache.getCache().put(invoice.getId(), pathSelector.getFilePath(invoice));
    }
  }

  @Override
  public List<Invoice> getInvoices() {
    List<Invoice> invoices = new ArrayList<>();
    ArrayList<String> linesFromAllFiles;
    linesFromAllFiles = fileCache.getAllFilesEntries();
    for (String line : linesFromAllFiles) {
      invoices.add(objectMapper.toInvoice(line));
    }
    return invoices;
  }

  @Override
  public boolean idExist(long id) {
    return fileCache.getCache().keySet().contains(id);
  }
}