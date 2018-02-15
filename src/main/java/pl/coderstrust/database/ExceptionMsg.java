package pl.coderstrust.database;

public class ExceptionMsg {

  public static final String INVOICE_NOT_EXIST =
      "Invoice does not exist in database.";
  public static final String IO_ERROR_WHILE_DELETING =
      "Filesystem I/O error during deleting invoice. Please try again later.";
  public static final String INVOICE_PROCESSING_INTERRUPT =
      "Invoice processing interrupted. Try again later.";
  public static final String IO_ERROR_WHILE_READING =
      "Filesystem I/O error reading invoice. Please try again later.";
  public static final String IO_ERROR_WHILE_ADDING =
      "Filesystem I/O error adding invoice. Please try again later.";
}
