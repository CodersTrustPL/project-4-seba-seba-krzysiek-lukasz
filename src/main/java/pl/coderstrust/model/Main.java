
package pl.coderstrust.model;

import pl.coderstrust.database.file.InFileDatabase;

import java.time.LocalDate;

public class Main {
  /**
   * static Main function for checking basic functionality.
   * @param args application entry parameters.
   */
  public static void main(String[] args) {

    TestCasesGenerator gen = new TestCasesGenerator();

    InFileDatabase test  = new InFileDatabase();

    for (int i = 0; i <100 ; i++) {
      Invoice invoice = gen.getTestInvoice(i, 10);
      invoice.setSystemId(i);
      test.addInvoice(invoice);
      System.out.println("Done " + i);
    }
//   Invoice newInv =  test.getInvoiceById(21);
//   System.out.println(newInv.getBuyer().getAddress());
//    ArrayList<Invoice> faktury = test.getInvoices();
//    System.out.println(faktury.get(33).getBuyer().getAddress());
//
//    Invoice newwInv = test.getInvoiceById(22);
//    newwInv.setIssueDate(LocalDate.of(2018,9,14));
//    test.updateInvoice(newwInv);
//    System.out.println(test.getInvoiceById(22).getIssueDate());


    for (int i = 0; i <99 ; i++) {
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
