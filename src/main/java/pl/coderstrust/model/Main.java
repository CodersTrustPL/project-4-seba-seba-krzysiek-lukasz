
package pl.coderstrust.model;

import pl.coderstrust.service.InvoiceBook;

import java.time.LocalDate;

public class Main {
  /**
   * static Main function for checking basic functionality.
   * @param args application entry parameters.
   */
  public static void main(String[] args) {
    InvoiceBook ib = new InvoiceBook();
    ib.addInvoice("PP1", new Company("FirmaX"), new Company("FirmaY"),
        10, 12, 2009, null, PaymentState.PAID);

    ib.addInvoice("PP2", new Company("FirmaX"), new Company("FirmaY"),
        15, 12, 2009, null, PaymentState.NOT_PAID);
    System.out.println(ib.findInvoice("PP1").toString());
    System.out.println("---------------");
    System.out.println(ib.findInvoice("PP2").toString());

    ib.removeInvoice("PP1");
    System.out.println(ib.findInvoice("PP1").toString());
    System.out.println(ib.findInvoice("PP2").toString());

  }

  public static void showDate(LocalDate localDate) {
    System.out.println(localDate);
  }
}
