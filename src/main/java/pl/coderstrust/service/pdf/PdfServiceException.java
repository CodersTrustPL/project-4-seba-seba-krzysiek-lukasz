package pl.coderstrust.service.pdf;

public class PdfServiceException extends RuntimeException {

  public PdfServiceException(String message) {
    super(message);
  }

  PdfServiceException(String message, Exception previousException) {
    super(message, previousException);
  }

}
