package pl.coderstrust.service;

import io.swagger.annotations.ApiOperation;
import org.springframework.http.ResponseEntity;
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

public abstract class AbstractController<T extends WithNameIdIssueDate & WithValidation> {

  protected AbstractService<T> service;
  protected EntriesFilter<T> byCustomerFilter;

  @RequestMapping(value = "", method = RequestMethod.POST)
  @ApiOperation(value = "Adds the entries and returning id")
  public ResponseEntity addEntry(
      @RequestParam(name = "filterKey", required = false) Long filterKey,
      @RequestBody T entry) {

    List<String> entryState = entry.validate();

    if (filterKey != null) {
      if (!byCustomerFilter.hasObjectById(entry, filterKey)) {
        entryState.add(Messages.COMPANY_ID_NOT_MATCH);
      }
    }

    if (entryState.isEmpty()) {
      long id = service.addEntry(entry);
      return ResponseEntity.ok(Messages.CONTROLLER_ENTRY_ADDED + id);
    }
    return ResponseEntity.badRequest().body(entryState);
  }

  @RequestMapping(value = "", params = "id", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the entry by id in the specified date range")
  public ResponseEntity getEntryById(
      @RequestParam(name = "id", required = true) long id,
      @RequestParam(name = "filterKey", required = false) Long filterKey) {
    if (!service.idExist(id)) {
      return ResponseEntity.notFound().build();
    }

    if (filterKey != null) {
      if (!byCustomerFilter.hasField(service.findEntry(id), filterKey)) {
        return ResponseEntity.notFound().build();
      }
    }
    return ResponseEntity.ok(service.findEntry(id));
  }

  @RequestMapping(value = "", method = RequestMethod.GET)
  @ApiOperation(value = "Returns the list of entries in the specified date range")
  public ResponseEntity getEntryByDate(
      @RequestParam(name = "filterKey", required = false) Long filterKey,
      @RequestParam(name = "startDate", required = false) LocalDate startDate,
      @RequestParam(name = "endDate", required = false) LocalDate endDate) {

    if (startDate == null && endDate == null) {
      if (filterKey != null) {
        return ResponseEntity
            .ok(byCustomerFilter.filterByField(service.getEntry(), filterKey));
      }
      return ResponseEntity.ok(service.getEntry());
    }

    if (filterKey != null) {
      return ResponseEntity.ok(byCustomerFilter.filterByField(service.getEntryByDate(startDate,
          endDate), filterKey));
    }

    return ResponseEntity.ok(service.getEntryByDate(startDate,
        endDate));
  }

  @RequestMapping(value = "", params = "id", method = RequestMethod.PUT)
  @ApiOperation(value = "Updates the entries by id")
  public ResponseEntity updateInvoice(
      @RequestParam(name = "filterKey", required = false) Long filterKey,
      @RequestParam(name = "id", required = true) long id,
      @RequestBody T entry) {
    List<String> entryState = entry.validate();

    if (filterKey != null) {
      if (!byCustomerFilter.hasObjectById(entry, filterKey)) {
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

  @RequestMapping(value = {""}, params = "id", method = RequestMethod.DELETE)
  @ApiOperation(value = "Deletes the entries by id")
  public ResponseEntity removeEntry(
      @RequestParam(name = "id", required = true) long id,
      @RequestParam(name = "filterKey", required = false) Long filterKey) {

    List<String> entryState = new ArrayList<>();
    if (!service.idExist(id)) {
      return ResponseEntity.notFound().build();
    }

    if (filterKey != null) {
      if (!byCustomerFilter.hasField(service.findEntry(id), filterKey)) {
        entryState.add(Messages.COMPANY_ID_NOT_MATCH);
        return ResponseEntity.badRequest().body(entryState);
      }
    }

    service.deleteEntry(id);
    return ResponseEntity.ok().build();
  }
}