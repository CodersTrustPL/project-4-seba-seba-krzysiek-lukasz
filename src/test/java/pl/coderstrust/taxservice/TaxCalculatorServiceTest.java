package pl.coderstrust.taxservice;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import pl.coderstrust.database.Database;

import java.time.LocalDate;

@RunWith(MockitoJUnitRunner.class)
public class TaxCalculatorServiceTest {

  private LocalDate startDate = LocalDate.of(2018, 2, 21);
  private LocalDate endDate = LocalDate.of(2018, 2, 22);
  private SampleInvoices sampleInvoices = new SampleInvoices();

  @Mock
  private Database database;

  @InjectMocks
  private TaxCalculatorService taxCalculatorService;

  @Test
  public void shouldCalculateIncomeCostWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    String calculateValue = taxCalculatorService.calculateIncomeCost(startDate,
        endDate);
    //then
    assertThat(calculateValue, is(equalTo("10000,00")));
  }

  @Test
  public void shouldCalculateVatDifferenceWholeInvoicesInDataRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    String calculateVat = taxCalculatorService.calculateVat(startDate,
        endDate);
    //then
    assertThat(calculateVat, is(equalTo("2300,00")));
  }

  @Test
  public void shouldNotCalculateIncomeCostWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    String calculateValue = taxCalculatorService.calculateIncomeCost(startDate,
        endDate);
    //then
    assertThat(calculateValue, is(equalTo("0,00")));
  }

  @Test
  public void shouldNotCalculateVatDifferenceWholeInvoicesOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesOutOfDate());
    //when
    String calculateVat = taxCalculatorService.calculateVat(startDate,
        endDate);
    //then
    assertThat(calculateVat, is(equalTo("0,00")));
  }

  @Test
  public void shouldCalculateIncomeCostWholeInvoicesInDataRangeAndOutOfData() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfinvoicesWholeDates());
    //when
    String calculateValue = taxCalculatorService.calculateIncomeCost(startDate,
        endDate);
    //then
    assertThat(calculateValue, is(equalTo("10000,00")));
  }

  @Test
  public void shouldCalculateVatDifferenceWholeInvoicesInDataRangeAndOutOfRange() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.listOfInvoicesInDateRange());
    //when
    String calculateVat = taxCalculatorService.calculateVat(startDate,
        endDate);
    //then
    assertThat(calculateVat, is(equalTo("2300,00")));
  }

  @Test
  public void shouldCalculateVatDifferenceInvoiceWithSmallPricesExpectedNegativeNumber() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    String calculateValue = taxCalculatorService.calculateIncomeCost(startDate,
        endDate);
    //then
    assertThat(calculateValue, is(equalTo("-2,00")));
  }

  @Test
  public void shouldCalculateIncomeCostInvoiceWithSmallPricesExpectedNegativeNumber() {
    //given
    when(database.getInvoices()).thenReturn(sampleInvoices.invoicesSmallPrices());
    //when
    String calculateVat = taxCalculatorService.calculateVat(startDate, endDate);
    //then
    assertThat(calculateVat, is(equalTo("-0,10")));
  }



}