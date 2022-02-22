package salesinvoice;

public class InvoiceLine {
	private int invoiceId;
	private String itemName;
	private double itemPrice;
	private int orderedCount;

	public int getInvoiceId() {
		return this.invoiceId;
	}

	public void setInvoiceId(int invoiceId) {
		this.invoiceId = invoiceId;
	}
	public String getItemName() {
		return this.itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getItemPrice() {
		return this.itemPrice;
	}
	public void setItemPrice(double itemPrice) {
		this.itemPrice = itemPrice;
	}
	public int getOrderedCount() {
		return this.orderedCount;
	}
	public void setOrderedCount(int orderedCount) {
		this.orderedCount = orderedCount;
	}
	public double getItemPriceTotal() {
		return this.itemPrice * this.orderedCount;
	}
	public String writeMe() {
		StringBuffer bufer = new StringBuffer();
		bufer.append(String.valueOf(this.invoiceId)).append(",").append(this.itemName).append(",")
				.append(this.itemPrice).append(",").append(this.orderedCount).append("\n");
		return bufer.toString();
	}
}
