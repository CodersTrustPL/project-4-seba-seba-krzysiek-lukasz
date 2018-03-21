package pl.coderstrust.service;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderstrust.model.Messages;
import pl.coderstrust.model.WithNameIdIssueDate;
import pl.coderstrust.model.WithValidation;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public abstract class AbstractController<T extends WithNameIdIssueDate & WithValidation> {

  protected AbstractService<T> service;
  protected EntriesFilter<T> byCustomerFilter;

  @RequestMapping(value = {"", "/{filterKey}"}, method = RequestMethod.POST)
  @ApiOperation(value = "Adds the entries and returning id")
  public ResponseEntity addEntry(
      @PathVariable(name = "filterKey", required = false) Optional<Long> filterKey,
      @RequestBody T entry) {

    List<String> entryState = entry.validate();

    if (filterKey.isPresent()) {
      if (!byCustomerFilter.hasObjectById(entry, filterKey.get())) {
        entryState.add(Messages.COMPANY_ID_NOT_MATCH);
      }
    }

    if (entryState.isEmpty()) {
      long id = service.addEntry(entry);
      return ResponseEntity.ok(Messages.CONTROLLER_ENTRY_ADDED + id);
    }
    return ResponseEntity.badRequest().body(entryState);
  }

  @RequestMapping(value = {"/{id}", "/{filterKey}/{id}"}, method = RequestMethod.GET)
  @ApiOperation(value = "Returns the entry by id in the specified date range")
  public ResponseEntity getEntryById(@PathVariable("id") long id,
      @PathVariable(name = "filterKey", required = false) Optional<Long> filterKey) {
    if (!service.idExist(id)) {
      return ResponseEntity.notFound().build();
    }

    if (filterKey.isPresent()) {
      if (!byCustomerFilter.hasField(service.findEntry(id), filterKey.get())) {
        return ResponseEntity.notFound().build();
      }
    }
    return ResponseEntity.ok(service.findEntry(id));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of entries in the specified date range")
  public ResponseEntity getEntryByDate(
      @RequestParam(name = "filterKey", required = false) Optional<Long> filterKey,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate) {

    if (startDate == null && endDate == null) {
      if (filterKey.isPresent()) {
        return ResponseEntity
            .ok(byCustomerFilter.filterByField(service.getEntry(), filterKey.get()));
      }
      return ResponseEntity.ok(service.getEntry());
    }

    if (filterKey.isPresent()) {
      return ResponseEntity.ok(byCustomerFilter.filterByField(service.getEntryByDate(startDate,
          endDate), filterKey.get()));
    }

    return ResponseEntity.ok(service.getEntryByDate(startDate,
        endDate));
  }

  @RequestMapping(value = {"/{id}", "/{filterKey}/{id}"}, method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the entries by id")
  public ResponseEntity updateInvoice(
      @PathVariable(name = "filterKey", required = false) Optional<Long> filterKey,
      @PathVariable("id") long id, @RequestBody T entry) {
    List<String> entryState = entry.validate();

    if (filterKey.isPresent()) {
      if (!byCustomerFilter.hasObjectById(entry, filterKey.get())) {
        entryState.add(Messages.COMPANY_ID_NOT_MATCH);
      }
    }

    if (!entryState.isEmpty()) {
      return ResponseEntity.badRequest().body(entryState);
    }
    entry.setId(id);
    service.updateEntry(entry);
    return ResponseEntity.ok().build();

  }

  @RequestMapping(value = {"/{id}", "/{filterKey}/{id}"}, method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the entries by id")
  public ResponseEntity removeEntry(@PathVariable("id") long id,
      @PathVariable(name = "filterKey", required = false) Optional<Long> filterKey) {
    List<String> entryState = new ArrayList<>();

    if (!service.idExist(id)) {
      return ResponseEntity.notFound().build();
    }

    if (filterKey.isPresent()) {
      if (!byCustomerFilter.hasField(service.findEntry(id), filterKey.get())) {
        entryState.add(Messages.COMPANY_ID_NOT_MATCH);
        return ResponseEntity.badRequest().body(entryState);
      }
    }

    service.deleteEntry(id);
    return ResponseEntity.ok().build();
  }
}