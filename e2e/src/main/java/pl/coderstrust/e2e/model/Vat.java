package pl.coderstrust.e2e.model;


public enum Vat {
  VAT_23(0.23),
  VAT_7(0.07),
  VAT_0(0);

  private final double vatPercent;

  Vat(double vatPercent) {
    this.vatPercent = vatPercent;
  }

  private double getVatPercent() {
    return vatPercent;
  }
}
