package pl.coderstrust.service.pdf;

public class PdfServiceException extends RuntimeException {

  public PdfServiceException(String message) {
    super(message);
  }

  public PdfServiceException(String message, Exception previousException) {
    super(message, previousException);
  }

}
