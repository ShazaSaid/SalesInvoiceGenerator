package com.company;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import salesinvoice.InvoiceHeader;
import salesinvoice.InvoiceLine;

public class HelperFunctions {

	public static int getNextInvoiceId(List<InvoiceHeader> invoiceList) {
		int max = 0;
		for (int i = 0; i < invoiceList.stream().count(); i++) {
			if (invoiceList.get(i).getInvoiceId() > max) {
				max = invoiceList.get(i).getInvoiceId();
			}
		}
		return max + 1;
	}

	public static byte[] convertInvoiceLinesToByteArray(ArrayList<InvoiceLine> invoiceLinesList) {
		String invoiceItemsDataString = "";
		for (int i = 0; i < invoiceLinesList.size(); i++) {
			invoiceItemsDataString = invoiceItemsDataString + invoiceLinesList.get(i).writeMe();
		}
		return invoiceItemsDataString.getBytes();
	}

	public static byte[] convertInvoiceHeadersToByteArray(ArrayList<InvoiceHeader> invoiceHeadersList) {
		String invoiceHeadersDataString = "";
		for (int i = 0; i < invoiceHeadersList.size(); i++) {
			invoiceHeadersDataString = invoiceHeadersDataString + invoiceHeadersList.get(i).writeMe();
		}
		return invoiceHeadersDataString.getBytes();
	}

	public static String[] readFile(String filePath) throws IOException {
		FileInputStream inputStream = new FileInputStream(filePath);
		int fileSize = inputStream.available();
		byte[] b = new byte[fileSize];
		inputStream.read(b);
		inputStream.close();
		String output = new String(b);
		String[] S = output.split("[,\r\n]");
		return Arrays.stream(S).filter(value -> value != null && value.length() > 0).toArray(size -> new String[size]);
	}

	public static void saveDataToFile(String filePath, byte[] Data) throws IOException {
		FileOutputStream outputStream = new FileOutputStream(filePath);
		outputStream.write(Data);
		outputStream.close();
	}
}