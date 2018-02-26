package pl.coderstrust.database.multifile;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Repository;
import pl.coderstrust.database.Database;
import pl.coderstrust.database.ObjectMapperHelper;
import pl.coderstrust.model.Invoice;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Repository
@Primary
public class MultiFileDatabase implements Database {

  private static final int FIRST_ID = 0;
  private static final int INCREMENT_ID = 1;

  private ObjectMapperHelper objectMapper;
  private FileHelper fileHelper;
  private Invoice invoice;
  FileCache fileCache;
  private PathSelector pathSelector;

  public MultiFileDatabase() {
    objectMapper = new ObjectMapperHelper();
    fileHelper = new FileHelper();
    invoice = new Invoice();
    fileCache = new FileCache();
    pathSelector = new PathSelector();
  }


  @Override
  public long addInvoice(Invoice invoice) {
    invoice.setId(getNextId());
    fileHelper.addLine(objectMapper.toJson(invoice), invoice);
    fileCache.cashe.put(invoice.getId(), pathSelector.getFilePath(invoice));
    return invoice.getId();
  }

  private long getNextId() {
    if (fileCache.cashe.isEmpty()) {
      return FIRST_ID;
    } else {
      List keys = new ArrayList<>(fileCache.cashe.keySet());
      return (long) Collections.max(keys) + INCREMENT_ID;
    }
  }


  @Override
  public void deleteInvoice(long id) {
    if (fileCache.cashe.containsKey(id)) {
      try {
        fileHelper.deleteLine(id);
      } catch (IOException e) {
        e.printStackTrace();
      }
      fileCache.cashe.remove(id);
    } else {
      System.out.println("Invoice with id " + id + " does not exist");
    }
  }


  @Override
  public Invoice getInvoiceById(long id) {
    if (fileCache.cashe.containsKey(id)) {
      try {
        invoice = objectMapper.toInvoice(fileHelper.getLine(id));
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      System.out.println("Invoice with id " + id + " does not exist");
    }
    return invoice;
  }

  @Override
  public void updateInvoice(Invoice invoice) {
    if (fileCache.cashe.containsKey(invoice.getId())) {
      deleteInvoice(invoice.getId());
      addInvoice(invoice);
    }
  }


  @Override
  public List<Invoice> getInvoices() {

    List<Invoice> invoices = new ArrayList<>();
    ArrayList<String> linesFromAllFiles;
    try {
      linesFromAllFiles = fileHelper.getAllFilesEntries();
      for (int i = 0; i < linesFromAllFiles.size(); i++) {
        invoices.add(objectMapper.toInvoice(linesFromAllFiles.get(i)));
      }
    } catch (IOException e) {
      e.printStackTrace();
    }
    return invoices;
  }

  @Override
  public boolean idExist(long id) {
    boolean idCheck;
    if (fileCache.cashe.containsKey(id)) {
      idCheck = true;
    } else {
      idCheck = false;
    }
    return idCheck;
  }
}