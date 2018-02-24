package pl.coderstrust.e2e;

import org.junit.Assert;

public class MyCalsTest {

  @org.testng.annotations.Test
  public void testSum() throws Exception {

    MyCals cals = new MyCals();

    int res = cals.sum(1,2);

    Assert.assertEquals(res,3);

  }

  @org.testng.annotations.Test(expectedExceptions = ArithmeticException.class)
  public void divisionWithException() {
    int i = 1 / 0;
  }

}