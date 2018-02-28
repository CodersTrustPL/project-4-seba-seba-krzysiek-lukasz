package pl.coderstrust.taxservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDate;

@RestController
@Configuration
public class TaxCalculatorController {

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
    return ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate));
  }

  @RequestMapping(value = "cost")
  public ResponseEntity calculateCost(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    return ResponseEntity.ok(taxService.calculateCost(companyName, startDate, endDate));
  }

  @RequestMapping(value = "incomeCost")
  public ResponseEntity calculateIncomeCost(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    return ResponseEntity.ok(taxService.calculateIncome(companyName, startDate, endDate)
        .subtract(taxService.calculateCost(companyName, startDate, endDate)));
  }

  @RequestMapping(value = "incVat")
  public ResponseEntity calculateIncomeVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    return ResponseEntity.ok(taxService.calculateIncomeVat(companyName, startDate, endDate));
  }

  @RequestMapping(value = "outVat")
  public ResponseEntity calculateOutcomeVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    return ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate));
  }

  @RequestMapping(value = "diffVat")
  public ResponseEntity calculateDifferenceVat(
      @RequestParam(value = "companyName") String companyName,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "startDate") LocalDate startDate,
      @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
      @RequestParam(value = "endDate") LocalDate endDate
  ) {
    return ResponseEntity.ok(taxService.calculateOutcomeVat(companyName, startDate, endDate)
        .subtract(taxService.calculateIncomeVat(companyName, startDate, endDate)));
  }
}