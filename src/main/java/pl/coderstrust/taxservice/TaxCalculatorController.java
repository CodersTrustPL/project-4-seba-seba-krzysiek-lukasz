package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pl.coderstrust.model.Messages;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;

@RestController
@Configuration
public class TaxCalculatorController {

  //TODO change "@DateTimeFormat(iso = DateTimeFormat.ISO.DATE)" to not repeat every time

  private TaxCalculatorService taxService;

  @Autowired
  public TaxCalculatorController(TaxCalculatorService taxService) {
    this.taxService = taxService;
  }

  @RequestMapping(value = "income")
  public ResponseEntity calculateIncome(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "cost")
  public ResponseEntity calculateCost(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate)) {
      return ResponseEntity.ok(taxService.calculateCost(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incomeTax")
  public ResponseEntity calculateIncomeTax(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate)) {
      BigDecimal income = taxService.calculateIncome(companyName, startDate, endDate);
      BigDecimal cost = taxService.calculateCost(companyName, startDate, endDate);
      return ResponseEntity.ok(income.subtract(cost).setScale(2, RoundingMode.HALF_UP));
    }

    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "incVat")
  public ResponseEntity calculateIncomeVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {

    if (endDate.isAfter(startDate)) {
      return ResponseEntity.ok(taxService.calculateIncomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "outVat")
  public ResponseEntity calculateOutcomeVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate)) {
      return ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate)
          .setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }

  @RequestMapping(value = "diffVat")
  public ResponseEntity calculateDifferenceVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    if (endDate.isAfter(startDate)) {
      BigDecimal outVat = taxService.calculateOutcomeVat(companyName, startDate, endDate);
      BigDecimal incVat = taxService.calculateIncomeVat(companyName, startDate, endDate);
      return ResponseEntity.ok(outVat.subtract(incVat).setScale(2, RoundingMode.HALF_UP));
    }
    return ResponseEntity.badRequest().body(Messages.END_BEFORE_START);
  }
}