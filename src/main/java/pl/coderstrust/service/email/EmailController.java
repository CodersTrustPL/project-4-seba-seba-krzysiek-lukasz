package pl.coderstrust.service.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.thymeleaf.TemplateEngine;
import pl.coderstrust.model.EmailTemplateNames;
import pl.coderstrust.model.Invoice;
import pl.coderstrust.service.InvoiceService;

@Controller
public class EmailController {

  private final EmailService emailService;
  private final TemplateEngine templateEngine;
  private final InvoiceService invoiceService;

  @Autowired
  public EmailController(EmailService emailService, TemplateEngine templateEngine,
      InvoiceService invoiceService) {
    this.emailService = emailService;
    this.templateEngine = templateEngine;
    this.invoiceService = invoiceService;
  }

  @PostMapping(value = "email/{invoice_id}")
  public ResponseEntity<String> send(@PathVariable("invoice_id") Long invoiceId,
      @RequestParam(name = "send email to") String email) {

    if (invoiceService.idExist(invoiceId)) {
      Invoice invoice = invoiceService.findEntry(invoiceId);
      EmailTemplate template = new EmailTemplate(invoice);

      String body = templateEngine.process(EmailTemplateNames.TEMPLATE, template.template());
      emailService.sendEmail(email, EmailTemplateNames.HEADER_INFO, body);
      return ResponseEntity.ok(body);
    }
    return ResponseEntity.notFound().build();
  }
}