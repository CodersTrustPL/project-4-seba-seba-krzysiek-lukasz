package pl.coderstrust.taxservice;

import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Messages;
import pl.coderstrust.service.CompanyService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RestController
@Configuration
public class TaxCalculatorController {

  private TaxCalculatorService taxService;
  private TaxSummary taxSummary;
  private CompanyService companyService;

  @Autowired
  public TaxCalculatorController(TaxCalculatorService taxService, TaxSummary taxSummary,
      CompanyService companyService) {
    this.taxService = taxService;
    this.taxSummary = taxSummary;
    this.companyService = companyService;
  }

  //TODO Maybe change companyID from RequestParam to Path Variable will look cleaner?

  @RequestMapping(value = "{companyId}/tax/income", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income in specific date range")
  public ResponseEntity calculateIncome(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncome(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/cost", method = RequestMethod.GET)
  @ApiOperation(value = "Returns cost in specific date range")
  public ResponseEntity calculateCost(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateCost(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/incomeTax", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Tax in specific date range")
  public ResponseEntity calculateIncomeTax(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal income = taxService.calculateIncome(companyId, startDate, endDate);
      BigDecimal cost = taxService.calculateCost(companyId, startDate, endDate);
      return ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/incVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns income Vat in specific date range")
  public ResponseEntity calculateIncomeVat(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncomeVat(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/outVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns outcome Vat in specific date range")
  public ResponseEntity calculateOutcomeVat(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      return ResponseEntity.ok(taxService.calculateOutcomeVat(companyId, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/diffVat", method = RequestMethod.GET)
  @ApiOperation(value = "Returns difference in Vat in specific date range")
  public ResponseEntity calculateDifferenceVat(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate", required = false) LocalDate startDate,
      @RequestParam(value = "endDate", required = false) LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    startDate = checkStartDateIfNull(startDate);
    endDate = checkEndDateIfNull(endDate);
    if (endDate.isAfter(startDate) || endDate.isEqual(startDate)) {
      BigDecimal outVat = taxService.calculateOutcomeVat(companyId, startDate, endDate);
      BigDecimal incVat = taxService.calculateIncomeVat(companyId, startDate, endDate);
      return ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "{companyId}/tax/summary/{year}", method = RequestMethod.GET)
  @ApiOperation(value = "Returns taxes summary in specific date range")
  public ResponseEntity calculateTaxSummary(
      @PathVariable("companyId") long companyId,
      @PathVariable("year") int year) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    if (year > LocalDate.now().getYear() + 50 || year < LocalDate.now().getYear() - 200) {
      return ResponseEntity.badRequest().body(Messages.INCORRECT_YEAR);
    }

    return ResponseEntity.ok(taxService.taxSummary(companyId, year));
  }

  @RequestMapping(value = "{companyId}/tax/incomeTaxAdvance/", method = RequestMethod.GET)
  @ApiOperation(value = "Returns value of income tax advance in specific date range")
  public ResponseEntity calculateIncomeTaxAdvance(
      @PathVariable("companyId") long companyId,
      @RequestParam(value = "startDate") LocalDate startDate,
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (!companyService.idExist(companyId)) {
      return ResponseEntity.notFound().build();
    }
    if ((startDate.isAfter(endDate)) || (endDate.getYear() != startDate.getYear())) {
      return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
    }
    return ResponseEntity.ok(
        taxService.calculateIncomeTaxAdvance(companyId, startDate, endDate)
            .setScale(2, RoundingMode.HALF_UP));
  }

  private LocalDate checkStartDateIfNull(LocalDate startDate) {
    return startDate == null ? LocalDate.of(1900, 1, 1) : startDate;
  }

  private LocalDate checkEndDateIfNull(LocalDate endDate) {
    return endDate == null ? LocalDate.of(3333, 1, 1) : endDate;
  }

}