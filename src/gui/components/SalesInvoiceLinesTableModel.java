package gui.components;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import salesinvoice.InvoiceLine;

public class SalesInvoiceLinesTableModel extends AbstractTableModel {
	String[] columns;
	ArrayList<InvoiceLine> invoiceLinesList;

	public SalesInvoiceLinesTableModel(String[] columns, ArrayList<InvoiceLine> invoiceLinesList) {
		this.columns = columns;
		this.invoiceLinesList = invoiceLinesList;
	}

	@Override
	public int getRowCount() {
		return invoiceLinesList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return invoiceLinesList.get(rowIndex).getInvoiceId();
		} else if (columnIndex == 1) {
			return invoiceLinesList.get(rowIndex).getItemName();
		} else if (columnIndex == 2) {
			return invoiceLinesList.get(rowIndex).getItemPrice();
		} else if (columnIndex == 3) {
			return invoiceLinesList.get(rowIndex).getOrderedCount();
		} else if (columnIndex == 4) {
			return invoiceLinesList.get(rowIndex).getItemPriceTotal();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}
}
