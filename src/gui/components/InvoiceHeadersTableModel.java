package gui.components;

import java.util.ArrayList;

import javax.swing.table.AbstractTableModel;

import salesinvoice.InvoiceHeader;

public class InvoiceHeadersTableModel extends AbstractTableModel {
	String[] columns;
	ArrayList<InvoiceHeader> invoiceHeadersList;

	public InvoiceHeadersTableModel(String[] columns, ArrayList<InvoiceHeader> invoiceHeadersList) {
		this.columns = columns;
		this.invoiceHeadersList = invoiceHeadersList;
	}

	@Override
	public int getRowCount() {
		return invoiceHeadersList.size();
	}

	@Override
	public int getColumnCount() {
		return columns.length;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		if (columnIndex == 0) {
			return invoiceHeadersList.get(rowIndex).getInvoiceId();
		} else if (columnIndex == 1) {
			return invoiceHeadersList.get(rowIndex).getInvoiceDate();
		} else if (columnIndex == 2) {
			return invoiceHeadersList.get(rowIndex).getCustomerName();
		} else if (columnIndex == 3) {
			return invoiceHeadersList.get(rowIndex).getInvoiceTotal();
		}
		return null;
	}

	@Override
	public String getColumnName(int column) {
		return columns[column];
	}

}
