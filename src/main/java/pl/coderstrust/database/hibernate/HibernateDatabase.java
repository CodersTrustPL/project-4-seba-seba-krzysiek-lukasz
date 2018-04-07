package pl.coderstrust.database.hibernate;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import pl.coderstrust.database.Database;
import pl.coderstrust.model.WithNameIdIssueDate;

import java.util.List;

@Service
public class HibernateDatabase<T extends WithNameIdIssueDate> implements Database<T> {

    @Autowired
    InvoiceRepository repository;


    @Override
    public long addEntry(T entry) {
        repository.save(entry);
        return entry.getId();
    }

    @Override
    public void deleteEntry(long id) {
        repository.delete(id);
    }

    @Override
    public T getEntryById(long id) {
        return (T) repository.findOne(id);
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
