
package pl.coderstrust.model;

import pl.coderstrust.database.inFileDatabase.inFileDatabase;

import java.time.LocalDate;

public class Main {
  /**
   * static Main function for checking basic functionality.
   * @param args application entry parameters.
   */
  public static void main(String[] args) {

    TestCasesGenerator gen = new TestCasesGenerator();

    inFileDatabase test  = new inFileDatabase();

    for (int i = 0; i <10000 ; i++) {
      Invoice invoice = gen.getTestInvoice(i,10);
          invoice.setSystemId(i);
      test.addInvoice(invoice);
      System.out.println("Done "+i);
    }
    test.getInvoiceById(1);

    for (int i = 0; i <9999 ; i++) {
      test.deleteInvoiceById(i);
      System.out.println("removing:"+i);
    }
   // test.updateInvoice(invoice);


//    ib.addInvoice("PP1", new Company("FirmaX"), new Company("FirmaY"),
//        10, 12, 2009, null, PaymentState.PAID);
//
//    ib.addInvoice("PP2", new Company("FirmaX"), new Company("FirmaY"),
//        15, 12, 2009, null, PaymentState.NOT_PAID);
//    System.out.println(ib.findInvoice("PP1").toString());
//    System.out.println("---------------");
//    System.out.println(ib.findInvoice("PP2").toString());
//
//    ib.removeInvoice("PP1");
//    System.out.println(ib.findInvoice("PP1").toString());
//    System.out.println(ib.findInvoice("PP2").toString());

  }

  public static void showDate(LocalDate localDate) {
    System.out.println(localDate);
  }
}
