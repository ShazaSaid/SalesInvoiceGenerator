package salesinvoice;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class InvoiceHeader {
	private int invoiceId;
	private Date invoiceDate;
	private String customerName;
	public ArrayList<InvoiceLine> invoiceLines = new ArrayList<>();
	private Double invoiceTotal;
	public int getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}

	public String getCustomerName() {
		return this.customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public Date getInvoiceDate() {
		return invoiceDate;
	}

	public void setInvoiceDate(Date invoiceDate) {
		this.invoiceDate = invoiceDate;
	}

	public void addInvoiceLines(InvoiceLine invoiceLine) {
		this.invoiceLines.add(invoiceLine);
	}

	public Double getInvoiceTotal() {
		invoiceTotal = 0.0;
		for (int i = 0; i < invoiceLines.size(); i++) {
			invoiceTotal = invoiceTotal + invoiceLines.get(i).getItemPriceTotal();
		}
		return invoiceTotal;
	}
	public String writeMe() {
		StringBuffer bufer = new StringBuffer();
		bufer.append(String.valueOf(this.invoiceId)).append(",")
				.append(String.valueOf(new SimpleDateFormat("dd-MM-yyyy").format(this.invoiceDate))).append(",")
				.append(this.customerName).append("\n");
		return bufer.toString();
	}
}