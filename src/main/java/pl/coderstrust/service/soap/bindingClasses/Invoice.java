//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.2.7 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2018.04.22 at 11:12:17 PM CEST 
//


package pl.coderstrust.service.soap.bindingClasses;

import java.util.ArrayList;
import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for invoice complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="invoice">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="id" type="{http://www.w3.org/2001/XMLSchema}long"/>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="buyer" type="{http://invoice-service.com}company"/>
 *         &lt;element name="seller" type="{http://invoice-service.com}company"/>
 *         &lt;element name="invoiceIssueDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paymentDate" type="{http://www.w3.org/2001/XMLSchema}string"/>
 *         &lt;element name="paymentState" type="{http://invoice-service.com}paymentState"/>
 *         &lt;element name="products" type="{http://invoice-service.com}invoiceEntry" maxOccurs="unbounded"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "invoice", propOrder = {
    "id",
    "name",
    "buyer",
    "seller",
    "invoiceIssueDate",
    "paymentDate",
    "paymentState",
    "products"
})
public class Invoice {

    protected long id;
    @XmlElement(required = true)
    protected String name;
    @XmlElement(required = true)
    protected Company buyer;
    @XmlElement(required = true)
    protected Company seller;
    @XmlElement(required = true)
    protected String invoiceIssueDate;
    @XmlElement(required = true)
    protected String paymentDate;
    @XmlElement(required = true)
    protected PaymentState paymentState;
    @XmlElement(required = true)
    protected List<InvoiceEntry> products;

    /**
     * Gets the value of the id property.
     * 
     */
    public long getId() {
        return id;
    }

    /**
     * Sets the value of the id property.
     * 
     */
    public void setId(long value) {
        this.id = value;
    }

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the buyer property.
     * 
     * @return
     *     possible object is
     *     {@link Company }
     *     
     */
    public Company getBuyer() {
        return buyer;
    }

    /**
     * Sets the value of the buyer property.
     * 
     * @param value
     *     allowed object is
     *     {@link Company }
     *     
     */
    public void setBuyer(Company value) {
        this.buyer = value;
    }

    /**
     * Gets the value of the seller property.
     * 
     * @return
     *     possible object is
     *     {@link Company }
     *     
     */
    public Company getSeller() {
        return seller;
    }

    /**
     * Sets the value of the seller property.
     * 
     * @param value
     *     allowed object is
     *     {@link Company }
     *     
     */
    public void setSeller(Company value) {
        this.seller = value;
    }

    /**
     * Gets the value of the invoiceIssueDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getInvoiceIssueDate() {
        return invoiceIssueDate;
    }

    /**
     * Sets the value of the invoiceIssueDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setInvoiceIssueDate(String value) {
        this.invoiceIssueDate = value;
    }

    /**
     * Gets the value of the paymentDate property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPaymentDate() {
        return paymentDate;
    }

    /**
     * Sets the value of the paymentDate property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPaymentDate(String value) {
        this.paymentDate = value;
    }

    /**
     * Gets the value of the paymentState property.
     * 
     * @return
     *     possible object is
     *     {@link PaymentState }
     *     
     */
    public PaymentState getPaymentState() {
        return paymentState;
    }

    /**
     * Sets the value of the paymentState property.
     * 
     * @param value
     *     allowed object is
     *     {@link PaymentState }
     *     
     */
    public void setPaymentState(PaymentState value) {
        this.paymentState = value;
    }

    /**
     * Gets the value of the products property.
     * 
     * <p>
     * This accessor method returns a reference to the live list,
     * not a snapshot. Therefore any modification you make to the
     * returned list will be present inside the JAXB object.
     * This is why there is not a <CODE>set</CODE> method for the products property.
     * 
     * <p>
     * For example, to add a new item, do as follows:
     * <pre>
     *    getProducts().add(newItem);
     * </pre>
     * 
     * 
     * <p>
     * Objects of the following type(s) are allowed in the list
     * {@link InvoiceEntry }
     * 
     * 
     */
    public List<InvoiceEntry> getProducts() {
        if (products == null) {
            products = new ArrayList<InvoiceEntry>();
        }
        return this.products;
    }

}
