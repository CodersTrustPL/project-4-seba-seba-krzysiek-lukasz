package pl.coderstrust.taxservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.closeTo;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;

import java.math.BigDecimal;
import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculatorServiceTest {

  private String companyName = "My company";
  private LocalDate startDate = LocalDate.of(2018, 2, 21);
  private LocalDate endDate = LocalDate.of(2018, 2, 22);
  private SampleInvoices sampleInvoices = new SampleInvoices();

  @Mock
  private Database database;

  @InjectMocks
  private TaxCalculatorService taxCalculatorService;

  @Test
  public void shouldCalculateIncomeWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(13300), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateCostWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(3300), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeCostWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncomeCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(10_000), new BigDecimal(0.001))));
  }

  @Test
  public void shouldNotCalculateIncomeWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(0), new BigDecimal(0.001))));
  }

  @Test
  public void shouldNotCalculateCostWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(0), new BigDecimal(0.001))));
  }

  @Test
  public void shouldNotCalculateIncomeCostWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncomeCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(0), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeWholeInvoicesInDataRangeAndOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfinvoicesWholeDates());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(13_300), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateCostInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfinvoicesWholeDates());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(3300), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeCostWholeInvoicesInDataRangeAndOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfinvoicesWholeDates());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncomeCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(10_000), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeInvoiceWithSmallPrices() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncome(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(10.5), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateCostInvoiceWithSmallPrices() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(12.5), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateIncomeCostInvoiceWithSmallPricesExpectedNegativeNumber() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateValue = taxCalculatorService.calculateIncomeCost(companyName, startDate,
        endDate);
    //then
    assertThat(calculateValue, is(closeTo(new BigDecimal(-2), new BigDecimal(0.001))));
  }

  @Test
  public void shouldCalculateVatOutcomeWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(3059), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatIncomeWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(759), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatDifferenceWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateDifferenceVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(2300), new BigDecimal(0.006))));
  }

  @Test
  public void shouldNotCalculateVatOutcomeWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0), new BigDecimal(0.006))));
  }

  @Test
  public void shouldNotCalculateVatIncomeWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0), new BigDecimal(0.006))));
  }

  @Test
  public void shouldNotCalculateVatDifferenceWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateDifferenceVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatOutcomeWholeInvoicesInDataRangeAndOutOfRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateOutcomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(3059), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatIncomeWholeInvoicesInDataRangeAndOutOfRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateIncomeVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(759), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatDifferenceWholeInvoicesInDataRangeAndOutOfRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    BigDecimal calculateVat = taxCalculatorService.calculateDifferenceVat(companyName, startDate,
        endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(2300), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatOutcomeInvoiceWithSmallPrices() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateVat = taxCalculatorService
        .calculateOutcomeVat(companyName, startDate, endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0.53), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatIncomeInvoiceWithSmallPrices() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateVat = taxCalculatorService
        .calculateIncomeVat(companyName, startDate, endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(0.63), new BigDecimal(0.006))));
  }

  @Test
  public void shouldCalculateVatDifferenceInvoiceWithSmallPricesExpectedNegativeNumber() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    BigDecimal calculateVat = taxCalculatorService
        .calculateDifferenceVat(companyName, startDate, endDate);
    //then
    assertThat(calculateVat, is(closeTo(new BigDecimal(-0.10), new BigDecimal(0.006))));
  }
}