package pl.coderstrust.database.hibernate;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.util.List;

@Service
public class HibernateDatabase<T extends WithNameIdIssueDate> implements Database<T> {

    private String keyName;

    private final Logger logger = LoggerFactory.getLogger(HibernateDatabase.class);


    public HibernateDatabase(Class<T> entryClass, String dbKey) {
        this.keyName = dbKey;
    }

    public HibernateDatabase() {
    }


    @Autowired
    InvoiceRepository repository;


    @Override
    public long addEntry(T entry) {
        Invoice savedInvoice = (Invoice) repository.save(entry);
        return savedInvoice.getId();
    }

    @Override
    public void deleteEntry(long id) {
        repository.delete(id);
    }

    @Override
    public T getEntryById(long id) {
        return (T) repository.getOne(id);
    }

    @Override
    public void updateEntry(T entry) {
        repository.save(entry);
    }

    @Override
    public List<T> getEntries() {
        return (List<T>) repository.findAll();
    }

    @Override
    public boolean idExist(long id) {
        return repository.exists(id);
    }

    }
