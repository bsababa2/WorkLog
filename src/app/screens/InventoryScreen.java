package app.screens;

import app.utils.InventoryTableModel;
import app.utils.Utils;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.JXTable;

import javax.swing.*;
import java.awt.*;

public class InventoryScreen extends JDialog
{
	private InventoryTableModel inventoryTableModel = new InventoryTableModel();
	private JXTable inventoryTable = new JXTable(inventoryTableModel);
	private JXButton addButton = new JXButton("הוסף");
	private JXButton removeButton = new JXButton("הסר");

	public InventoryScreen(JFrame owner)
	{
		super(owner, true);
		this.setTitle("ניהול מלאי");
		Utils.setSoftSize(this, new Dimension(800, 600));
		this.setLocationRelativeTo(owner);

		JXPanel buttonPanel = new JXPanel();
		buttonPanel.add(addButton);
		buttonPanel.add(removeButton);

		initSizes();

		JXPanel mainPanel = new JXPanel();
		mainPanel.setLayout(new BorderLayout());
		mainPanel.add(buttonPanel, BorderLayout.NORTH);
		mainPanel.add(new JScrollPane(inventoryTable), BorderLayout.CENTER);

		this.getContentPane().add(mainPanel);
	}

	private void initSizes()
	{
		inventoryTable.getColumn(InventoryTableModel.PROFILE_TYPE_COL).setMinWidth(100);
		inventoryTable.getColumn(InventoryTableModel.PROFILE_TYPE_COL).setMaxWidth(200);
		inventoryTable.getColumn(InventoryTableModel.QUANTITY_COL).setMinWidth(70);
		inventoryTable.getColumn(InventoryTableModel.QUANTITY_COL).setMaxWidth(70);
	}
}
