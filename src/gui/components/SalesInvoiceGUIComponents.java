package gui.components;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Objects;

import javax.swing.*;

import com.company.HelperFunctions;

import salesinvoice.InvoiceHeader;
import salesinvoice.InvoiceLine;

public class SalesInvoiceGUIComponents extends JFrame implements ActionListener {
	public JButton createInvoiceBtn;
	public JButton deleteInvoiceBtn;
	public JButton saveInvoiceBtn;
	public JButton cancelInvoiceBtn;
	private JTextField invoiceDateTxtField;
	private JTextField customerNameTextField;
	private JLabel invoiceNumberLabel;
	private InvoiceHeadersTableModel headersTableModel;
	private SalesInvoiceLinesTableModel linesTableModel;
	private final JTable invoiceHeadersTable = new JTable(headersTableModel);
	private final JTable invoiceItemsTable = new JTable(linesTableModel);
	public JMenuBar fileMenuBar;
	public JMenu fileMenu;
	public JMenuItem loadFile;
	public JMenuItem saveFile;
	public JMenuItem exitFile;
	private final JPanel leftPanel = new JPanel();
	private final JPanel rightPanel = new JPanel();
	private final JSplitPane splitPane = new JSplitPane();
	ArrayList<InvoiceLine> invoiceItemsList = new ArrayList<>();
	ArrayList<InvoiceHeader> invoiceHeadersList = new ArrayList<>();
	InvoiceHeader selectedInvoiceHeader;
	ArrayList<InvoiceHeader> tempInvoiceHeadersList;
	ArrayList<InvoiceLine> tempInvoiceItemsList;

	public SalesInvoiceGUIComponents() {
		super("Sales Invoice");
		getContentPane().setLayout(new GridLayout());
		splitTheFrame();
		addFileMenu();
		readDataFiles();
		setLeftPanel();
		setRightPanel();
		setFrame();
	}

	private void setFrame() {
		pack();
		setLocation(800, 300);
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setVisible(true);
	}

	private void splitTheFrame() {
		getContentPane().add(splitPane);
		splitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
		splitPane.setDividerLocation(300);
		splitPane.setLeftComponent(leftPanel);
		splitPane.setRightComponent(rightPanel);
	}

	private void setRightPanel() {
		rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.Y_AXIS));
		addInvoiceNumberLabel();
		addInvoiceNumberValueLabel();
		addInvoiceDateTextField();
		addCustomerNameTextField();
		addInvoiceItemsTable(false);
		addSaveInvoiceButton();
		addCancelInvoiceButton();
	}

	private void setLeftPanel() {
		leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.Y_AXIS));
		addInvoiceHeaderTable(false);
		addCreateInvoiceButton();
		addDeleteInvoiceButton();
	}

	private void readDataFiles() {
		try {
			readInvoiceItemsTableValues(System.getProperty("user.dir") + "\\InvoiceLine.csv");
		} catch (IOException ex) {
			JOptionPane.showMessageDialog(null, "Either the file doesnt exist or not in the correct format.");
		}
		try {
			readInvoiceHeadersTableValues(System.getProperty("user.dir") + "\\InvoiceHeader.csv");
		} catch (ParseException | IOException ex) {
			JOptionPane.showMessageDialog(null, "Either the file doesnt exist or not in the correct format.");
		}
	}

	private void addCreateInvoiceButton() {
		createInvoiceBtn = new JButton("Create New Invoice");
		leftPanel.add(createInvoiceBtn);
		createInvoiceBtn.addActionListener(e -> addCreateNewInvoiceDialog());
	}

	private void addCreateNewInvoiceDialog() {
		JDialog dialog = new JDialog(this, "add invoice");
		dialog.isVisible();
		JLabel customerNameLabel = new JLabel("Customer Name");
		dialog.add(customerNameLabel);
		JTextField customerNameTextField = new JTextField();
		dialog.add(customerNameTextField);

		JLabel itemNameLabel = new JLabel("Item Name");
		dialog.add(itemNameLabel);
		JTextField itemNameTextField = new JTextField();
		dialog.add(itemNameTextField);

		JLabel itemPriceLabel = new JLabel("Item Price");
		dialog.add(itemPriceLabel);
		JTextField itemPriceTextField = new JTextField();
		dialog.add(itemPriceTextField);

		JLabel orderedCountLabel = new JLabel("Ordered Count");
		dialog.add(orderedCountLabel);
		JTextField orderedCountTextField = new JTextField();
		dialog.add(orderedCountTextField);
		JButton saveButton = new JButton("Save");
		dialog.add(saveButton);
		saveButton.addActionListener(e -> {
			createNewInvoice(itemNameTextField.getText(), itemPriceTextField.getText(), orderedCountTextField.getText(),
					customerNameTextField.getText());
			dialog.setVisible(false);
		});
		dialog.pack();
		dialog.setSize(300, 500);
		dialog.setVisible(true);
		dialog.setLayout(new GridLayout(5, 1));
	}

	private void createNewInvoice(String itemName, String itemPrice, String orderedPrice, String customerName) {
		tempInvoiceItemsList = new ArrayList<>(invoiceItemsList);
		tempInvoiceHeadersList = new ArrayList<>(invoiceHeadersList);

		InvoiceLine newInvoiceLine = new InvoiceLine();
		newInvoiceLine.setItemName(itemName);
		newInvoiceLine.setItemPrice(Double.parseDouble(itemPrice));
		newInvoiceLine.setOrderedCount(Integer.parseInt(orderedPrice));
		newInvoiceLine.setInvoiceId(HelperFunctions.getNextInvoiceId(tempInvoiceHeadersList));
		tempInvoiceItemsList.add(newInvoiceLine);
		linesTableModel.invoiceLinesList = tempInvoiceItemsList;

		InvoiceHeader newInvoiceHeader = new InvoiceHeader();
		newInvoiceHeader.setInvoiceId(newInvoiceLine.getInvoiceId());
		Date date = new Date();
		newInvoiceHeader.setInvoiceDate(date);
		newInvoiceHeader.setCustomerName(customerName);
		newInvoiceHeader.addInvoiceLines(newInvoiceLine);
		tempInvoiceHeadersList.add(newInvoiceHeader);
		headersTableModel.invoiceHeadersList = tempInvoiceHeadersList;
		addInvoiceHeaderTable(true);
		addInvoiceItemsTable(true);
	}

	private void addDeleteInvoiceButton() {
		deleteInvoiceBtn = new JButton("Delete Invoice");
		leftPanel.add(deleteInvoiceBtn);
		deleteInvoiceBtn.addActionListener(e -> {
			if (Objects.isNull(selectedInvoiceHeader)) {
				JOptionPane.showMessageDialog(null, "Please select an invoice header!");
			} else {
				for (int i = 0; i < invoiceItemsList.size(); i++) {
					if (invoiceItemsList.get(i).getInvoiceId() == selectedInvoiceHeader.getInvoiceId()) {
						invoiceItemsList.remove(i);
						i = i - 1;// because the list will get smaller due to the remove.
					}
				}
				reselectInvoiceItemsTableValues();
				addInvoiceItemsTable(true);
				for (int i = 0; i < invoiceHeadersList.size(); i++) {
					if (invoiceHeadersList.get(i).getInvoiceId() == selectedInvoiceHeader.getInvoiceId()) {
						invoiceHeadersList.remove(i);
						break;
					}
				}
				addInvoiceHeaderTable(true);
			}
		});
	}

	private void addSaveInvoiceButton() {
		saveInvoiceBtn = new JButton("Save Invoice");
		rightPanel.add(saveInvoiceBtn);
		saveInvoiceBtn.addActionListener(e -> {
			if (!Objects.isNull(tempInvoiceHeadersList)) {// before saving the list is saved in a temp list, this is to
															// be able to cancel the new additions
				invoiceHeadersList = tempInvoiceHeadersList;
				tempInvoiceHeadersList = null;
			}
			if (!Objects.isNull(tempInvoiceItemsList)) {
				invoiceItemsList = tempInvoiceItemsList;
				tempInvoiceItemsList = null;
			}
			if (!Objects.isNull(selectedInvoiceHeader)) { // update the headers table data with the updates done in the
															// text fields
				for (int i = 0; i < invoiceHeadersList.size(); i++) {
					if (invoiceHeadersList.get(i).getInvoiceId() == selectedInvoiceHeader.getInvoiceId()) {
						invoiceHeadersList.get(i).setCustomerName(customerNameTextField.getText());
						try {
							String Guidate = invoiceDateTxtField.getText();
							invoiceHeadersList.get(i).setInvoiceDate(new SimpleDateFormat("dd-MM-yyyy").parse(Guidate));
						} catch (ParseException parseException) {
							JOptionPane.showMessageDialog(null, "Wrong date format!");
						}
					}
				}
				headersTableModel.invoiceHeadersList = invoiceHeadersList;
				addInvoiceHeaderTable(true);
				invoiceDateTxtField.setText("");
				customerNameTextField.setText("");
				invoiceNumberLabel.setText("");
			}
		});
	}

	private void addCancelInvoiceButton() {
		cancelInvoiceBtn = new JButton("Cancel");
		rightPanel.add(cancelInvoiceBtn);
		cancelInvoiceBtn.addActionListener(e -> {
			linesTableModel.invoiceLinesList = invoiceItemsList;

			headersTableModel.invoiceHeadersList = invoiceHeadersList;

			addInvoiceItemsTable(true);
			addInvoiceHeaderTable(true);
		});
	}

	private void addInvoiceNumberLabel() {
		rightPanel.add(new JLabel("Invoice Number"));

	}

	private void addInvoiceNumberValueLabel() {
		invoiceNumberLabel = new JLabel();
		rightPanel.add(invoiceNumberLabel);
	}

	private void addInvoiceDateTextField() {
		invoiceDateTxtField = new JTextField(5);
		rightPanel.add(new JLabel("Invoice Date"));
		rightPanel.add(invoiceDateTxtField);
	}

	private void addCustomerNameTextField() {
		customerNameTextField = new JTextField(5);
		rightPanel.add(new JLabel("Customer Name"));
		rightPanel.add(customerNameTextField);
	}

	private void addFileMenu() {
		fileMenuBar = new JMenuBar();
		loadFile = new JMenuItem("Load File", 'O');
		loadFile.setAccelerator(KeyStroke.getKeyStroke('O', KeyEvent.CTRL_DOWN_MASK));
		loadFile.addActionListener(this);
		loadFile.setActionCommand("O");

		saveFile = new JMenuItem("Save File");
		saveFile.setAccelerator(KeyStroke.getKeyStroke('S', KeyEvent.CTRL_DOWN_MASK));
		saveFile.addActionListener(e -> {
			if (!Objects.isNull(tempInvoiceHeadersList) || !Objects.isNull(tempInvoiceItemsList)) {
				JOptionPane.showMessageDialog(null, "Please save the changes first!");
			} else {
				try {

					saveInvoicesHeadersToFile();
					saveInvoicesLinesToFile();
				} catch (IOException ioException) {
					ioException.printStackTrace();
				}
			}
		});
		saveFile.setActionCommand("S");

		exitFile = new JMenuItem(("Exit"), 'X');
		exitFile.setAccelerator(KeyStroke.getKeyStroke('X', KeyEvent.CTRL_DOWN_MASK));
		exitFile.addActionListener(this);
		saveFile.setActionCommand("X");

		fileMenu = new JMenu("File");
		fileMenu.add(loadFile);
		fileMenu.add(saveFile);
		fileMenu.addSeparator();
		fileMenu.add(exitFile);
		fileMenuBar.add(fileMenu);
		setJMenuBar(fileMenuBar);
	}

	private void readInvoiceHeadersTableValues(String filePath) throws IOException, ParseException {
		InvoiceHeader invoiceHeader;
		invoiceHeadersList = new ArrayList<>();
		String[] invoiceHeadersFileEntries = HelperFunctions.readFile(filePath);
		for (int i = 0; i < invoiceHeadersFileEntries.length;) {
			invoiceHeader = new InvoiceHeader();
			try {
				invoiceHeader.setInvoiceId(Integer.parseInt(invoiceHeadersFileEntries[i]));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Wrong invoice ID format!");
				break;
			}
			i++;
			try {
				invoiceHeader.setInvoiceDate(new SimpleDateFormat("dd-MM-yyyy").parse(invoiceHeadersFileEntries[i]));
			} catch (Exception ex) {
				JOptionPane.showMessageDialog(null, "Wrong date format!");
				break;
			}
			i++;
			invoiceHeader.setCustomerName(invoiceHeadersFileEntries[i]);
			i++;
			this.invoiceHeadersList.add(invoiceHeader);
		}
		for (InvoiceLine invoiceLine : invoiceItemsList) {
			for (InvoiceHeader header : invoiceHeadersList) {
				if (invoiceLine.getInvoiceId() == header.getInvoiceId()) {
					header.addInvoiceLines(invoiceLine);
					break;
				}
			}
		}
		headersTableModel = new InvoiceHeadersTableModel(
				new String[]{"Invoice ID", "Date", "Customer Name", "Invoice Total"}, invoiceHeadersList);
	}

	private void addInvoiceHeaderTable(boolean alreadyExists) {
		invoiceHeadersTable.setModel(headersTableModel);
		invoiceHeadersTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		invoiceHeadersTable.getSelectionModel().addListSelectionListener(e -> {
			ArrayList<InvoiceHeader> selectionList;// In case the user added new invoices but still not saved then he
													// should navigate through the temp list not the saved one.
			if (!Objects.isNull(tempInvoiceHeadersList)) {
				selectionList = new ArrayList<>(tempInvoiceHeadersList);
			} else {
				selectionList = new ArrayList<>(invoiceHeadersList);
			}
			if (e.getValueIsAdjusting()) {
				return;
			}
			int rowIndex = invoiceHeadersTable.getSelectedRow();
			if (rowIndex < 0) {
				selectedInvoiceHeader = null;
			} else {
				selectedInvoiceHeader = selectionList.get(rowIndex);
				reselectInvoiceItemsTableValues();
				addInvoiceItemsTable(true);
				invoiceDateTxtField
						.setText(new SimpleDateFormat("dd-MM-yyyy").format(selectedInvoiceHeader.getInvoiceDate()));
				customerNameTextField.setText(selectedInvoiceHeader.getCustomerName());
				invoiceNumberLabel.setText(String.valueOf(selectedInvoiceHeader.getInvoiceId()));
			}
		});

		if (!alreadyExists) {
			leftPanel.add(invoiceHeadersTable);
			leftPanel.add(new JScrollPane(invoiceHeadersTable));
		} else {
			headersTableModel.fireTableDataChanged();
			invoiceHeadersTable.repaint();
			setSize(getWidth() + 1, getHeight() + 1);
		}
	}

	private void readInvoiceItemsTableValues(String filePath) throws IOException {
		InvoiceLine invoiceLine;
		invoiceItemsList = new ArrayList<>();
		String[] invoicesLinesFileEntries = HelperFunctions.readFile(filePath);
		for (int i = 0; i < invoicesLinesFileEntries.length;) {
			invoiceLine = new InvoiceLine();
			invoiceLine.setInvoiceId(Integer.parseInt(invoicesLinesFileEntries[i]));
			i++;
			invoiceLine.setItemName(invoicesLinesFileEntries[i]);
			i++;
			invoiceLine.setItemPrice(Double.parseDouble(invoicesLinesFileEntries[i]));
			i++;
			invoiceLine.setOrderedCount(Integer.parseInt(invoicesLinesFileEntries[i]));
			i++;
			invoiceItemsList.add(invoiceLine);
		}
		linesTableModel = new SalesInvoiceLinesTableModel(
				new String[]{"Invoice ID", "Item Name", "Item Price", "Count", "Total"}, invoiceItemsList);
	}

	private void addInvoiceItemsTable(boolean alreadyExists) {
		invoiceItemsTable.setModel(linesTableModel);
		rightPanel.add(new JScrollPane(invoiceItemsTable));
		if (!alreadyExists) {
			rightPanel.add(invoiceItemsTable);
			rightPanel.add(new JScrollPane(invoiceItemsTable));
		} else {
			linesTableModel.fireTableDataChanged();
			invoiceItemsTable.repaint();
			setSize(getWidth() + 1, getHeight() + 1);
		}
	}

	private void reselectInvoiceItemsTableValues() {
		ArrayList<InvoiceLine> invoiceItemsListTemp = new ArrayList<>();
		ArrayList<InvoiceLine> selectionList;
		if (!Objects.isNull(tempInvoiceItemsList)) { // in case new items were added in the table but not yet saved.
			selectionList = new ArrayList<>(tempInvoiceItemsList);
		} else {
			selectionList = new ArrayList<>(invoiceItemsList);
		}
		for (InvoiceLine invoiceLine : selectionList) {
			if (invoiceLine.getInvoiceId() == selectedInvoiceHeader.getInvoiceId()) {
				invoiceItemsListTemp.add(invoiceLine);
			}
		}
		linesTableModel.invoiceLinesList = invoiceItemsListTemp;
	}

	private void loadFiles() throws IOException, ParseException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getPath();
			readInvoiceHeadersTableValues(filePath);
			addInvoiceHeaderTable(true);
		}

		JFileChooser fileChooser2 = new JFileChooser();
		if (fileChooser2.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser2.getSelectedFile().getPath();
			readInvoiceItemsTableValues(filePath);
			addInvoiceItemsTable(true);
		}

	}

	private void saveInvoicesHeadersToFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getPath();
			HelperFunctions.saveDataToFile(filePath,
					HelperFunctions.convertInvoiceHeadersToByteArray(invoiceHeadersList));
		}
	}

	private void saveInvoicesLinesToFile() throws IOException {
		JFileChooser fileChooser = new JFileChooser();
		if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
			String filePath = fileChooser.getSelectedFile().getPath();
			HelperFunctions.saveDataToFile(filePath, HelperFunctions.convertInvoiceLinesToByteArray(invoiceItemsList));
		}
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		switch (e.getActionCommand()) {
			case "O" :
				try {
					loadFiles();
				} catch (IOException | ParseException ex) {
					System.out.println(ex.getMessage());
				}
				break;

			case "X" :
				System.exit(0);
				break;
		}

	}

}